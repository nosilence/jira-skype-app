package com.skype.ipc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TransportFactory {

    public class Result {
        InputTransporting  in;
        OutputTransporting out;
    }

    public TransportFactory() {
    }

    public Result init(ClientConfiguration cfg, ConnectionListener listener) {
       if (cfg.useTcpTransport()) {
          return createTcpConnection(cfg, listener);
       }
       return createLocalConnection(cfg, listener);
    }

    private Result createTcpConnection(ClientConfiguration cfg, ConnectionListener listener) {
        // create the socket and connect it
        Socket socket = new Socket();
        InetSocketAddress endpoint;

        try {
            endpoint = new InetSocketAddress(InetAddress.getByName(cfg.getIp()), cfg.getPort());
     } catch (java.net.UnknownHostException e) {
             if (listener != null) {
                 listener.sidOnDisconnected("UnknownHostException: "+cfg.getIp());
             }
             return null;
     }

         int retry   = cfg.getConnectionNumRetries();
         int timeout = cfg.getConnectionRetryInitialLatency();
         while (retry != 0) {
             try {
                 if (listener != null)
                     listener.sidOnConnecting();
                 socket.connect(endpoint);
                 socket.setTcpNoDelay(true);
                 socket.setKeepAlive(true);
                 retry = 0;
             } catch (IOException to) {
                 if (retry-- > 0) {
                     try {
                         Thread.sleep(timeout);
                     } catch (InterruptedException e) {
                         if (listener != null) {
                             listener.sidOnDisconnected("SocketTimeoutException when connecting");
                         }
                         return null;
                    }
                    timeout += timeout/2;
                 } else {
                    if (listener != null) {
                        listener.sidOnDisconnected("SocketTimeoutException when connecting");
                    }
                    return null;
                }
             }
         }

         InputStream  ins;
         OutputStream outs;

         try {
             ins   = socket.getInputStream();
             outs  = socket.getOutputStream();
         } catch (IOException e) {
             listener.sidOnDisconnected("socket disconnected\n");
             return null;
         }

         return createConnection(cfg, listener, ins, outs);
    }

    protected Result createConnection(ClientConfiguration cfg, ConnectionListener listener, InputStream ins, OutputStream outs) {
         Result result = new Result();

         if (cfg.isWithoutTls()) {
             result.in = new LoggedBufferedInputStream (ins, cfg);
             result.out= new LoggedBufferedOutputStream(outs, cfg);
         } else {
             try {
                 LoggedTlsInputOutputStream wrapper = new LoggedTlsInputOutputStream(ins, outs, cfg); 
                 result.in = wrapper;
                 result.out= wrapper;
              } catch (FileNotFoundException e) {
                 listener.sidOnDisconnected(e.toString());
                 return null;
              } catch (IOException e) {
                 listener.sidOnDisconnected("TLS handshake failure\n");
                 return null;
              }
        }

         if (!doHandshake(cfg, listener, result.in, result.out)) 
             result = null;

         return result;
    }

    protected boolean doHandshake(ClientConfiguration configuration, ConnectionListener listener, InputTransporting in, OutputTransporting out) {
        String setup = "";

        boolean ok = true;
        if (!configuration.isWithoutTls()) {
            try {
                setup = configuration.getCertificate();
            } catch (IOException e) {
                listener.sidOnDisconnected("unable to read certificate file");
                ok    = false;
            }
        }       

        if (ok) {
            setup += configuration.getHandshakeSetup();

            String appTokenLenHexStr=Integer.toHexString(setup.length());
            while(appTokenLenHexStr.length()<8)  {
                appTokenLenHexStr="0"+appTokenLenHexStr;
            }
      
            try {
                out.writeBytes(appTokenLenHexStr.getBytes());
                out.writeBytesAndFlush(setup.getBytes());
                byte[] rsp = new byte[2];
                in.readBytes(rsp);
                ok = rsp[0] == 'O' && rsp[1] == 'K';
            } catch (IOException e) {
                ok = false;
            }
        }

        if (ok) {
             listener.sidOnConnected();
        } else {
             listener.sidOnDisconnected("failed handshake\n");
        }

        return ok;
    }

    protected Result createLocalConnection(ClientConfiguration cfg, ConnectionListener listener) {
        if (listener != null) {
            listener.sidOnDisconnected("local transport not supported\n");
        }
        return null;
    }

}

