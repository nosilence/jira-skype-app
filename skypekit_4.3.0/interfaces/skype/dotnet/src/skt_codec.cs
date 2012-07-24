using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Security;
using System.Net.Sockets;

namespace SkypeKit
{
    internal class SktDecoder
    {
        SktSkypeBase skype;
        SktTransport transport;

        public SktDecoder(SktSkypeBase Skype)
        {
            skype = Skype;
            transport = skype.transport;
        }

        int uint2int(uint number)
        {
            int result;
            if ((1 & number) != 0)
            {
                result = (int)((number ^ (~0)) >> 1); // negative
                return result;
            }
            result = (int)(number >> 1); // positive
            return result;
        }

        public uint DecodeUint()
        {
            uint val = 0;
            byte b;
            byte shift = 0;
            do {
                b = (byte)skype.transport.ReadByte();
                val |= ((uint)b & 0x7f) << shift;
                shift+=7;
            } while ((b >> 7) != 0);
            return val;
        }

        public int DecodeInt()
        {
            uint u = DecodeUint();
            long l = (u >> 1) ^ (-(u & 1));
            int i = (int)l;
            return i;
        }

        long decodeUint64()
        {
          int shift = 0;
          long result = 0;
          while (true)
          {
            int value = skype.transport.ReadByte() & 0xFF;
            result = result | (uint)((value & 0x7f) << shift);
            shift = shift + 7;
            if ((value & 0x80) == 0)
              break;
          }
          return result;
        }

        public SktObject DecodeObject(uint ClassID)
        {
            uint ObjectID = DecodeUint();
            return skype.GetObject(ClassID, ObjectID);
        }

        public String DecodeString()
        {
            uint uft8len = DecodeUint();
            if (uft8len > 0)
            {
                byte[] buf = new byte[uft8len];
                skype.transport.Read(ref buf, (int)uft8len);
                String result = System.Text.UTF8Encoding.UTF8.GetString(buf);
                if (result == null) result = "";
                return result;
            }
            return "";
        }

        public String DecodeFileName()
        {
            return DecodeString();
        }

        public String DecodeXML()
        {
            return DecodeString();
        }

        public List<String> DecodeStringList(char stringType)
        {
            int tag;
            List<String> list = new List<String>();
            do
            {
                tag = skype.transport.ReadByte();
                if (tag == stringType)
                {
                    list.Add(DecodeString());
                }
                else 
                {
                    if (tag != ']') throw new Exception("Unexpected byte received from socket instead of string list end marker.");
                }
            } while (tag != ']');
            return list;
        }

        public List<String> DecodeStringList()
        {
            return DecodeStringList('S');
        }

        public SktObjectList DecodeObjectList(uint classId)
        {
            int tag;

            SktObjectList list = skype.CreateObjectList(classId);
            do
            {
                tag = skype.transport.ReadByte();
                if (tag == 'O')
                {
                    SktObject o = skype.GetObject(classId, DecodeUint());
                    list.Add(o);
                }
                else 
                {
                    if (tag != ']') throw new Exception("Unexpected byte received from socket instead of object list end marker.");
                }
            } while (tag != ']');

            return list;
        }

        List<uint> DecodeUintList(int elementType)
        {
            int tag = skype.transport.ReadByte();
            if (tag != '[') throw new Exception("Unexpected byte received from socket instead of uint list start marker.");

            List<uint> list = new List<uint>();

            do
            {
                tag = skype.transport.ReadByte();
                if (tag == elementType)
                {
                    list.Add(DecodeUint());
                }
                else
                {
                    if (tag != ']') throw new Exception("Unexpected byte received from socket instead of uint list end marker.");
                }
            } while (tag != ']');

            return list;
        }

        public List<uint> DecodeUintList()
        {
            return DecodeUintList('U');
        }

        public byte[] DecodeBinary()
        {
            uint len = DecodeUint();
            if (len > 0)
            {
                byte[] buf = new byte[len];
                skype.transport.Read(ref buf, (int)len);
                return buf;
            }
            return null;
        }


        public Boolean DecodeBool(ref bool endMarker)
        {
            int c = skype.transport.ReadByte();
            if (c == 'z')
            {
                endMarker = true;
                return true;
            }
            return c == 'T';
        }

        public Boolean DecodeBool()
        {
            int c = skype.transport.ReadByte();
            if (c == 'z') return true;
            return (c == 'T');
        }

        public object DecodePropertyUpdate(int propType, uint propClassId)
        {
            object prop;

            switch (propType)
            {
                case 'i':
                    prop = DecodeInt();
                    break;
                
                case 'u':
                    prop = DecodeUint();
                    break;

                case 'e':
                    prop = (int)DecodeUint();
                    break;

                case 'U':
                    prop = DecodeUint();
                    break;

                case 'T':
                    prop = true;
                    break;

                case 'F':
                    prop = false;
                    break;

                case 'S':
                    prop = DecodeString();
                    break;

                case 'X':
                    prop = DecodeString();
                    break;

                case 'f':
                    prop = DecodeString();
                    break;

                case 'B':
                    prop = DecodeBinary();
                    break;

                case 'O':
                    uint objectId = DecodeUint();
                    prop = skype.GetObject(propClassId, objectId);
                    break;                

                default:
                    prop = null;                    
                    throw new Exception(String.Format("Unexpected property type {0} tag while decoding property update.", propType));
            }
            return prop;
        }

        public void FetchPropGetFooter()
        {
            skype.transport.ReadByte();
            skype.transport.ReadByte();
            skype.transport.ReadByte();
            int result = skype.transport.ReadByte();
            if (result != 122) throw new Exception(String.Format("Unexpected end tag {0} while decoding PropGet response .", result));
        }

        public void DecodeMethodResponseArguments(int count, ref object[] args, uint[] classIds, ref Dictionary<uint, uint> tagMap, String methodName)
        {
            int typeMarker;
            uint argTag;
            int argNr = -1;
            do
            {
                typeMarker = skype.transport.ReadByte();
                if (typeMarker != 'z')
                {
                    argNr++;
                    if (typeMarker == 'N') throw new Exception(methodName + " failed.");                  
                    if ((argNr + 1) > count) throw new Exception(String.Format("Got unexpected response argument {0} from runtime in " + methodName, argNr));
                    argTag = tagMap[DecodeUint()] - 1;
                    args[argTag] = DecodePropertyUpdate(typeMarker, classIds[argTag]);
                }
            } while (typeMarker != 'z');
            skype.transport.ResumeSocketReaderFromMethod();
        }

        public void DecodeMethodResponseWithNoArguments(String methodName)
        {
            int typeMarker;
            int argNr = -1;
            do
            {
                typeMarker = skype.transport.ReadByte();
                if (typeMarker != 'z')
                {
                    argNr++;
                    if (typeMarker == 'N') throw new Exception(methodName + " failed.");
                    if ((argNr + 1) > 0) throw new Exception(String.Format("Got unexpected response argument {0} from runtime in " + methodName, argNr));
                }
            } while (typeMarker != 'z');
            skype.transport.ResumeSocketReaderFromMethod();
        }

    } // SktDecoder



    internal class SktEncoder
    {
        SktSkypeBase skype;
        SktTransport transport;

        public SktEncoder(SktSkypeBase Skype)
        {
            skype = Skype;
            transport = skype.transport;
        }

        int uint2int(int number)
        {
            if ((1 & number) != 0) return (number ^ (~0)) >> 1; // negative
            return number = (number >> 1); // positive
        }

        void EncodeUint(uint value)
        {
            while (true)
            {
                uint towrite = value & 0x7f;
                value = value >> 7;
                if (value == 0)
                {
                    transport.WriteByte((byte)towrite);
                    break;
                }
                transport.WriteByte((byte)(0x80 | towrite));
            }
        }

        void EncodeInt(int value)
        {
            value = value << 1;
            if (value < 0) value = value ^ -1;
            while (true)
            {
                int towrite = value & 0x7f;
                value = value >> 7;
                if (value == 0)
                {
                    transport.WriteByte((byte)towrite);
                    break;
                }
                transport.WriteByte((byte)(0x80 | towrite));
            }
        }

        void EncodeUint64(long value)
        {
            while (true)
            {
                long towrite = value & 0x7f;
                value = value >> 7;
                if (value == 0)
                {
                    transport.WriteByte((byte)towrite);
                    break;
                }
                transport.WriteByte((byte)(0x80 | towrite));
            }
        }

        void EncodeString(String value)
        {
            if (value == null || value.Length == 0)
            {
                transport.WriteByte((byte)0);
            }
            else
            {
                byte[] utf8 = System.Text.Encoding.UTF8.GetBytes(value);
                EncodeUint((uint)utf8.Length);
                transport.WriteBytes(utf8);
            }
        }

        void EncodeBinary(byte[] value)
        {
            if (value == null || value.Length == 0)
            {
                transport.WriteByte((byte)0);
            }
            else
            {
                EncodeUint((uint)value.Length);
                transport.WriteBytes(value);
            }
        }

        public void AddBoolParam(byte tag, bool value)
        {
            if (value)
            {
                transport.WriteByte('T');
            }
            else
            {
                transport.WriteByte('F');
            }
            
            transport.WriteByte(tag);
        }

        public void AddBoolParam(byte tag, bool value, bool defaultValue)
        {
            if (value == defaultValue) return;
            transport.WriteByte(value ? 'T' : 'F');
            transport.WriteByte(tag);
        }

        public void AddBoolListParam(byte tag, List<bool> values)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);
            for (int i = 0; i < values.Count; i++) transport.WriteByte(values[i] ? 'T' : 'F');
            transport.WriteByte(']');
        }

        public void AddUintParam(byte tag, uint value)
        {
            if (value == 0) return;
            transport.WriteByte('u');
            transport.WriteByte(tag);
            EncodeUint(value);
        }

        public void AddUintParam(byte tag, uint value, int defaultValue)
        {
            if (value == defaultValue) return;
            transport.WriteByte('u');
            transport.WriteByte(tag);
            EncodeUint(value);
        }

        public void AddUintListParam(byte tag, List<uint> values)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);
            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte('u');
                EncodeUint(values[i]);
            }
            transport.WriteByte(']');
        }

        public void AddIntParam(byte tag, int value)
        {
            if (value == 0) return;
            transport.WriteByte('i');
            transport.WriteByte(tag);
            EncodeInt(value);
        }

        public void AddPropkeyParam(byte tag, int value)
        {
            if (value == 0) return;
            transport.WriteByte('e');
            transport.WriteByte(tag);
            EncodeUint((uint)value);
            //EncodeInt(value);
        }

        public void AddIntParam(byte tag, int value, int defaultValue)
        {
            if (value == defaultValue) return;
            transport.WriteByte('i');
            transport.WriteByte(tag);
            EncodeInt(value);
        }

        public void AddIntListParam(byte tag, List<int> values)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);

            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte('i');
                EncodeInt(values[i]);
            }
            transport.WriteByte(']');
        }

        
        public void AddEnumParam(byte tag, uint value)
        {
            if (value == 0) return;
            transport.WriteByte('e');
            transport.WriteByte(tag);
            EncodeUint(value);
        }

        public void AddEnumParam(byte tag, uint value, uint defaultValue)
        {
            if (value == defaultValue) return;
            transport.WriteByte('e');
            transport.WriteByte(tag);
            EncodeUint(value);
        }
        
        public void AddEnumListParam(byte tag, List<uint> values)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);

            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte('e');
                EncodeUint(values[i]);
            }
            transport.WriteByte(']');
        }

        public void AddUint64Param(byte tag, long value)
        {
            if (value == 0) return;
            transport.WriteByte('U');
            transport.WriteByte(tag);
            EncodeUint64(value);
        }

        public void AddUint64Param(byte tag, long value, long defaultValue)
        {
            if (value == defaultValue) return;
            transport.WriteByte('U');
            transport.WriteByte(tag);
            EncodeUint64(value);
        }


        public void AddUint64ListParam(byte tag, List<long> values)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);

            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte('U');
                EncodeUint64(values[i]);
            }
            transport.WriteByte(']');
        }

        public void AddObjectParam(byte tag, SktObject value)
        {

            if (value == null || value.OID == 0) return;
            transport.WriteByte('O');
            transport.WriteByte(tag);
            EncodeUint(value.OID);
        }

        public void AddObjectListParam(byte tag, SktObjectList values)
        {

            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);
            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte('O');
                EncodeUint(values[i].OID);
            };
            transport.WriteByte(']');
        }

        public void AddStringParam(byte tag, String value)
        {
            if (value == null || value.Length == 0) return;
            transport.WriteByte('S');
            transport.WriteByte(tag);
            EncodeString(value);
        }

        public void AddStringParam(byte tag, String value, String defaultValue)
        {
            if (value != null || value.Equals(defaultValue)) return;
            transport.WriteByte('S');
            transport.WriteByte(tag);
            EncodeString(value);
        }

        private void AddStringListParam(byte tag, List<String> values, char kind)
        {
            if (values == null || values.Count == 0) return;
            transport.WriteByte('[');
            transport.WriteByte(tag);

            for (int i = 0; i < values.Count; i++)
            {
                transport.WriteByte(kind);
                EncodeString(values[i]);
            }
            transport.WriteByte(']');
        }

        public void AddStringListParam(byte tag, List<String> values)
        {
            AddStringListParam(tag, values, 'S');
        }

        public void AddFileNameParam(byte tag, String value)
        {
            if (value == null || value.Length == 0) return;
            transport.WriteByte('f');
            transport.WriteByte(tag);
            EncodeString(value);
        }

        public void AddFilenameListParam(byte tag, List<String> values)
        {
            AddStringListParam(tag, values, 'f');
        }

        public void AddXmlParam(byte tag, String value)
        {
            if (value == null || value.Length == 0) return;
            transport.WriteByte('X');
            transport.WriteByte(tag);
            EncodeString(value);
        }

        public void AddXmlParam(byte tag, String value, String defaultValue)
        {
            if (value != null || value.Equals(defaultValue)) return;
            transport.WriteByte('X');
            transport.WriteByte(tag);
            EncodeString(value);
        }

        public void AddXmlListParam(byte tag, List<String> values)
        {
            AddStringListParam(tag, values, 'X');
        }

        public void AddBinaryParam(byte tag, byte[] value, byte[] defaultValue)
        {
            if (value != null || value.Equals(defaultValue)) return;
            transport.WriteByte('B');
            transport.WriteByte(tag);
            EncodeBinary(value);
        }

        public void AddBinaryParam(byte tag, byte[] value)
        {
            if (value == null || value.Length == 0) return;
            transport.WriteByte('B');
            transport.WriteByte(tag);
            EncodeBinary(value);
        }

        public void AddTimeStampParam(byte tag, DateTime value, DateTime defaultValue)
        {
            if (value.Equals(defaultValue)) return;
            uint unixTimeStamp = skype.DateTimeToUnixTimestamp(value);
            AddUintParam(tag, unixTimeStamp);
        }

        public void AddTimeStampParam(byte tag, DateTime value)
        {
            uint unixTimeStamp = skype.DateTimeToUnixTimestamp(value);
            AddUintParam(tag, unixTimeStamp);
        }


        //--------------------------------------------------------------------------------------------------

        public uint AddMethodHeader(uint ClassID, uint MethodID, uint ObjectID)
        {
            transport.WriteByte('Z');
            transport.WriteByte('R');

            EncodeUint(ClassID);
            EncodeUint(MethodID);

            uint RID = skype.GetNextRequestID();
            EncodeUint(RID);

            if (ClassID != 0)
            {
                transport.WriteByte('O');
                transport.WriteByte(0);
                EncodeUint(ObjectID);
            }

            return RID;
        }

        public void AddPropGetHeader(uint ClassID, uint PropID, uint ObjectID)
        {
            transport.WriteByte('Z');
            transport.WriteByte('G');

            EncodeUint(PropID);
            transport.WriteByte(']');
            EncodeUint(ClassID);
            EncodeUint(ObjectID);
            transport.WriteByte(']');
            transport.WriteByte(']');
        }

        internal void AddFilenameParam()
        {
            throw new NotImplementedException();
        }
    }
}
