package com.skype.ipc;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LoggedBufferedOutputStream extends BufferedOutputStream implements OutputTransporting {

    public LoggedBufferedOutputStream(OutputStream out, ClientConfiguration cfg) {
        super(out);
        if (cfg.generateTransportLog()) {
            try {
                mTransportLog = new FileOutputStream(cfg.getOutputTransportLogName());
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
    }

    public void writeBytes(byte [] src) throws IOException {
        super.write(src, 0, src.length);
        if (mTransportLog != null) {
            try {
                mTransportLog.write(src, 0, src.length);
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
    }

    public void writeBytesAndFlush(byte [] src) throws IOException {
        super.write(src, 0, src.length);
        super.flush();
        if (mTransportLog != null) {
            try {
                mTransportLog.write(src, 0, src.length);
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
    }

    public void writeByte(int value) throws IOException {
        super.write(value);
        if (mTransportLog != null) {
            try {
                mTransportLog.write(value);
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
    }

    public void writeByteAndFlush(int value) throws IOException {
        super.write(value);
        super.flush();
        if (mTransportLog != null) {
            try {
                mTransportLog.write(value);
            } catch (IOException e) {
                mTransportLog = null;
            }
        }
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

