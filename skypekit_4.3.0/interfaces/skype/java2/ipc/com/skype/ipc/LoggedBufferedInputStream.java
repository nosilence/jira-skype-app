package com.skype.ipc;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoggedBufferedInputStream extends BufferedInputStream implements InputTransporting {

    public LoggedBufferedInputStream(InputStream in, ClientConfiguration cfg) {
        super(in);
        if (cfg.generateTransportLog()) {
            try {
                mTransportLog = new FileOutputStream(cfg.getInputTransportLogName());
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
    }

    public void skipBytes(final int numBytes) throws IOException {
        if (mTransportLog != null) {
            byte[] chunk = new byte[numBytes];
            readBytes(chunk, 0, numBytes);
        } else {
           super.skip(numBytes);
        }
    }

    public void readBytes(final byte [] dest) throws IOException {
        readBytes(dest, 0, dest.length);
    }

    public void readBytes(final byte [] dest, final int offset, final int numBytes) throws IOException {
        int consumed = 0;
        while (consumed < numBytes) {
            int r = super.read(dest, offset+consumed, numBytes - consumed);
            if (r == -1) {
                throw new IOException("EOF");
            } else {
                if (mTransportLog != null) {
                    try {
                        mTransportLog.write(dest, offset+consumed, r);
                    } catch (IOException e) {
                        mTransportLog = null; 
                    }
                }
                consumed += r;
                if (consumed < numBytes) Thread.yield();
            }
        }
    }

    public int readByte() throws IOException {
        int b = super.read();
        if (mTransportLog != null && b != -1) {
            try {
                mTransportLog.write(b);
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
        return b;
    }

    public void close() throws IOException {
        try {
            if (mTransportLog != null) 
                 mTransportLog.close();
        } catch (IOException e) {
        }
        super.close();
    }

    private FileOutputStream mTransportLog;
}

