using System;
using System.IO;
using System.Net;
using System.Collections.Generic;
using System.Text;
using System.Net.Security;
using System.Net.Sockets;
using System.Security.Cryptography.X509Certificates;
using System.Threading;

namespace SkypeKit
{
    public class SktTransport
    {
        public volatile bool SocketReaderStopping;

        public ManualResetEvent socketReentrancyBarrier;
        public ManualResetEvent mainThreadBlock;

        SktSkypeBase skype;
        public Thread SocketReader;
        public TcpClient tcpClient;
        public SslStream sslStream;

        X509Certificate2 certificate;
        byte[] handshakeBuffer = new byte[2048];

        public const int writeBufferLength = 16 * 1024;
        public byte[] writeBuffer;
        public int writeBufferPos;

        private object transportLogLock;
        private StreamWriter transportLogFile;
        public readonly String transportLogFileName = "transport.log";


        public SktTransport(SktSkypeBase Skype, X509Certificate2 cert)
        {
            writeBuffer = new byte[writeBufferLength];
            writeBufferPos = -1;
            
            SocketReaderStopping = false;
            socketReentrancyBarrier = new ManualResetEvent(true);
            mainThreadBlock = new ManualResetEvent(true);
            
            skype = Skype;
            certificate = cert;

            tcpClient = new TcpClient(AddressFamily.InterNetwork);

            if (skype.transportLogging)
            {
                transportLogLock = new object();
                transportLogFile = new StreamWriter(transportLogFileName);
                transportLogFile.AutoFlush = true;
            }
        }

        public void Connect(IPAddress ipAddress, int port)
        {
            int attempts = 0;
            do 
            {
                attempts++;
                try
                {
                    tcpClient.Connect(ipAddress, port);
                }
                catch { /* Ignoring this for awhile, let's see if next attempt will succeed.. */ }

                if (tcpClient.Connected)
                {
                    Authenticate();
                    return;
                } 

            } while (attempts <= 5);
            throw new Exception("Connection to the runtime was refused. Are you quite sure the runtime is running?");
        }

        void Authenticate()
        {
            sslStream = new SslStream(tcpClient.GetStream());
            sslStream.BeginAuthenticateAsServer(certificate, AuthenticateCallback, null);
        }

        void AuthenticateCallback(IAsyncResult ar)
        {
            sslStream.EndAuthenticateAsServer(ar);
            if (sslStream.IsAuthenticated)
            {
                if (skype.logging)
                {
                    skype.Log("SSL stream is authenticated.");
                    skype.Log(String.Format("IsAuthenticated={0} IsEncrypted={1}", sslStream.IsAuthenticated, sslStream.IsEncrypted));
                    skype.Log(String.Format("Cipher: {0} strength {1}", sslStream.CipherAlgorithm, sslStream.CipherStrength));
                    skype.Log(String.Format("Hash: {0} strength {1}", sslStream.HashAlgorithm, sslStream.HashStrength));
                    skype.Log(String.Format("Key exchange: {0} strength {1}", sslStream.KeyExchangeAlgorithm, sslStream.KeyExchangeStrength));
                    skype.Log(String.Format("Protocol: {0}", sslStream.SslProtocol));
                }
                Handshake();
            }
        }

        void Handshake()
        {
            System.Text.ASCIIEncoding encoding = new System.Text.ASCIIEncoding();

            string certData = Convert.ToBase64String(certificate.GetRawCertData());
            string propSubscripton = skype.GetPropSubscriptionString();
            string propGetStringsAlways = "SkypeKit/FowardStringChangedValue=1\n";


            string certLength = string.Format("{0:x8}", certData.Length + propSubscripton.Length + propGetStringsAlways.Length);
            sslStream.Write(encoding.GetBytes(certLength));
            sslStream.Write(encoding.GetBytes(certData));
            sslStream.Write(encoding.GetBytes(propSubscripton));
            sslStream.Write(encoding.GetBytes(propGetStringsAlways));

            sslStream.BeginRead(handshakeBuffer, 0, 2, HandshakeCallback, null);
        }

        void HandshakeCallback(IAsyncResult ar)
        {
            int byteCount = sslStream.EndRead(ar);            
            Decoder decoder = Encoding.UTF8.GetDecoder();
            char[] chars = new char[decoder.GetCharCount(handshakeBuffer, 0, byteCount)];
            decoder.GetChars(handshakeBuffer, 0, byteCount, chars, 0);            
            String reply = new string(chars);

            SktEvents.OnConnectArgs args = new SktEvents.OnConnectArgs();
            args.success = (reply == "OK");
            args.handshakeResult = reply;
            skype.events.FireOnConnect(this, args);
        }


        private void AppendToTransportLog (int b, Boolean incoming)
        {
            lock (transportLogLock)
            {
                char c = (char)b;
                if ((b == 10) | (b == 13)) c = ' ';
                if (incoming)
                {
                    transportLogFile.WriteLine("<- " + b.ToString() + " - " + c);
                }
                else
                {
                    transportLogFile.WriteLine("-> " + b.ToString() + " - " + c);
                }
            }
        }

        public void WriteByte(byte b)
        {
            writeBufferPos++;
            writeBuffer[writeBufferPos] = b;
            if (skype.transportLogging) AppendToTransportLog(b, false);
        }

        public void WriteByte(char c)
        {
            writeBufferPos++;
            writeBuffer[writeBufferPos] = (byte)c;
            if (skype.transportLogging) AppendToTransportLog(c, false);
        }

        public void WriteBytes(byte[] b)
        {
            b.CopyTo(writeBuffer, writeBufferPos + 1);
            writeBufferPos += b.Length;

            if (skype.transportLogging) 
            {
                int len = b.Length;
                for (int i = 0; i < len; i++) { AppendToTransportLog(b[i], false); };
            }
        }

        public void SendData()
        {
            WriteByte('z');
            try
            {
                sslStream.Write(writeBuffer, 0, writeBufferPos + 1);
                sslStream.Flush();
            }
            catch (Exception e)
            {
                if (skype.logging) skype.Log("Socket stream error: " + e.Message);
                skype.events.FireOnDisconnect(skype, EventArgs.Empty);
                SocketReaderStopping = true;
            }
            writeBufferPos = -1;
        }

        public int ReadByte()
        {
            int b = -1;
            do
            {
                try
                {
                    b = sslStream.ReadByte();
                }
                catch (Exception e)
                {
                    if (skype.logging) skype.Log("Socket stream error: " + e.Message);
                    skype.events.FireOnDisconnect(skype, EventArgs.Empty);
                    SocketReaderStopping = true;
                }
                if (SocketReaderStopping) return -1;
            } while (b == -1);

            if (skype.transportLogging) AppendToTransportLog(b, true);
            
            return b;
        }

        public void Read(ref byte[] buf, int byteCount)
        {
            int fromSocket;
            for (int i = 0; i < byteCount; i++)
            {
                fromSocket = ReadByte();
                if (fromSocket == -1) return;
                buf[i] = (byte)fromSocket;
            }
        }

        public void Dump()
        {
            int b = 0;
            while (b != 'z')
            {
                b = ReadByte();
                if (skype.logging) skype.Log(b.ToString() + " - " + (char)b);
            };
            LowerReentrancyBarrier();
        }

        void DecodeEvent()
        {
            uint classId = skype.decoder.DecodeUint();
            uint eventId = skype.decoder.DecodeUint();

            uint objectId = 0;

            if (classId != 0)
            {
                int objectTag = ReadByte();
                if (objectTag != 'O') throw new Exception("Object tag missing while decoding header of a non-skype class event");
                uint paramTag = skype.decoder.DecodeUint();
                if (paramTag != 0) throw new Exception("Got non-zero as first argument tag while decoding header of a non-skype class event");
                objectId = skype.decoder.DecodeUint();
                if (objectId == 0) throw new Exception("Got 0 as object ID while decoding header of a non-skype class event");
            }

            SktObject targetObject = skype.GetObject(classId, objectId);            
            targetObject.DispatchEvent(eventId);
        }

        void DecodeSpontaneousPropChange()
        {
            uint classId    = skype.decoder.DecodeUint();
            uint objectId   = skype.decoder.DecodeUint();

            SktObject targetObject = skype.GetObject(classId, objectId);

            int  propType = ReadByte();
            uint propId = skype.decoder.DecodeUint();

            Boolean thereIsActualPropValue = (propType != 'N');

            uint propClassId = 0;
            if (propType == 'O') propClassId = targetObject.MapPropIdToClassId(propId);

            object prop = null;
            if (thereIsActualPropValue) prop = skype.decoder.DecodePropertyUpdate(propType, propClassId);

            int b;
            for (int i=0; i < 3; i++)
            {
                b = ReadByte();
                if (b != ']') throw new Exception(String.Format("Unexpected property update end tag {0}", b));
            }

            b = ReadByte();
            if (b != 'z') throw new Exception(String.Format("Unexpected property update packet end tag {0}", b));

            LowerReentrancyBarrier();
            targetObject.DispatchPropertyUpdate(propId, prop, thereIsActualPropValue);
        }

        void DecodeMethodResponse()
        {
            ResumeMainThread();
            if (skype.logging) skype.Log("Resuming method thread.");
        }

        void DecodePropGetResponse()
        {
            uint classId = skype.decoder.DecodeUint();
            uint objetId = skype.decoder.DecodeUint();           
            ResumeMainThread();
            if (skype.logging) skype.Log("Resuming property get thread.");
        }

        internal Boolean PropResponseWasOk(uint PropId)
        {
            int propType = ReadByte();
            uint propIdFromSocket = skype.decoder.DecodeUint();
            if (propIdFromSocket != PropId) 
                throw new Exception(String.Format("Invalid property ID {0} while decoding property get response", propIdFromSocket));
            return (propType != 'N');
        }

        void SocketReaderLoop ()
        {
            if (skype.logging) skype.Log("Socket reader thread has started.");
            int b;
            do
            {
                WaitForReentrancyBarrier();
                if (SocketReaderStopping) return; 

                b = ReadByte();
                if (SocketReaderStopping) return; 

                RaiseReentrancyBarrier();

                if (b == 'Z')
                {
                    b = ReadByte();
                    if (SocketReaderStopping) return; 
                    switch (b)
                    {
                        case 'r':
                            DecodeMethodResponse();
                            break;
                        case 'g':
                            DecodePropGetResponse();
                            break;
                        case 'C':
                            DecodeSpontaneousPropChange();
                            break;
                        case 'E':
                            DecodeEvent();
                            break;
                        case -1:
                            return;
                        default:
                            throw new Exception("Received invalid packet start marker");
                    }
                }
                else
                {
                    throw new Exception("Received non-Z packet start marker");
                }

            } while (!SocketReaderStopping);            
        }

        public void StartSocketReader()
        {
            SocketReader = new Thread(new ThreadStart(SocketReaderLoop));
            SocketReader.Name = "SkypeKit socket reader thread";
            SocketReader.IsBackground = true;
            SocketReader.Start();
            Thread.Sleep(50);
        }

        public void StopSocketReader()
        {
            if (skype.logging) skype.Log("Closing socket reader thread.");
            SocketReaderStopping = true;
            LowerReentrancyBarrier();
            tcpClient.Close();
        }

        public void RaiseReentrancyBarrier()
        {
            socketReentrancyBarrier.Reset();
        }

        public void LowerReentrancyBarrier()
        {
            socketReentrancyBarrier.Set();
        }

        public void WaitForReentrancyBarrier()
        {
            socketReentrancyBarrier.WaitOne();
        }


        // This is where we end up when the runtime fails to respond in a timely manner..
        TimeSpan timeout = TimeSpan.FromSeconds(10);

        public void PreBlockMainThread()
        {
            mainThreadBlock.Reset();
        }

        public void BlockMainThread()
        {
            if (!mainThreadBlock.WaitOne(timeout)) throw new Exception("IPC socket timeout.");
        }

        public void ResumeMainThread()
        {
            mainThreadBlock.Set();
        }

        object requestLock = new object();

        public void SubmitPropertyRequest(uint ClassId, uint PropId, uint ObjectId)
        {
            lock (requestLock)
            {
                skype.encoder.AddPropGetHeader(ClassId, PropId, ObjectId);
                PreBlockMainThread();
                SendData();
                BlockMainThread();
            }
        }

        public void SubmitMethodRequest (uint RequestId)
        {
            lock (requestLock)
            {
                PreBlockMainThread();
                SendData();
                BlockMainThread();
                uint ResponseId = skype.decoder.DecodeUint();
                if (ResponseId != RequestId) throw new Exception("Method request and response ID mismatch.");
            }
        }

        public void ResumeSocketReaderFromPropRequest()
        {
            skype.decoder.FetchPropGetFooter();
            LowerReentrancyBarrier();
        }

        public void ResumeSocketReaderFromMethod()
        {
            LowerReentrancyBarrier();
            if (skype.logging) skype.Log("Method execution finished. Resuming socket thread.");
        }

        public void ResumeSocketReaderFromEvent()
        {
            LowerReentrancyBarrier();
            if (skype.logging) skype.Log("Event data decoded. Resuming socket thread.");
        }
    }
}
