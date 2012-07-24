package com.skype.ipc.android;

import java.util.concurrent.TimeoutException;

import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import com.skype.ipc.ConnectionListener;
import com.skype.ipc.TransportFactory.Result;

public class TransportFactory extends com.skype.ipc.TransportFactory {

    protected Result 
    createLocalConnection(com.skype.ipc.ClientConfiguration cfg, ConnectionListener listener) {
        // create the socket and connect it
        ClientConfiguration androidCfg = (ClientConfiguration) cfg;

        String socketName = androidCfg.getLocalSocketName();
        Namespace socketNamespace = androidCfg.getLocalSocketNamespace();
        LocalSocketAddress endpoint = new LocalSocketAddress(socketName, socketNamespace);
        LocalSocket socket = new LocalSocket();

         int retry   = cfg.getConnectionNumRetries();
         int timeout = cfg.getConnectionRetryInitialLatency();

         InputStream  ins = null;
         OutputStream outs = null;

         while (retry >= 0) {
             try {
                 if (listener != null)
                     listener.sidOnConnecting();
                 socket.connect(endpoint);
                 ins   = socket.getInputStream();
                 outs  = socket.getOutputStream();
                 retry = -1;
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
                	 System.out.println("connect give up ");
                     if (listener != null) {
                         listener.sidOnDisconnected("SocketTimeoutException when connecting");
                     }
                     return null;
                 }
             }
         }

         return createConnection(cfg, listener, ins, outs);
    }
}

