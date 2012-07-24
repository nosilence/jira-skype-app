package com.skype.ipc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;

public class LoggedTlsInputOutputStream implements OutputTransporting, InputTransporting {

	public LoggedTlsInputOutputStream(InputStream input, OutputStream output, ClientConfiguration cfg) throws IOException {

		mInput                  = input;
		mOutput                 = output;

		if (cfg.generateTransportLog()) {
			try {
				mInputTransportLog  = new FileOutputStream(cfg.getInputTransportLogName());
				mOutputTransportLog = new FileOutputStream(cfg.getOutputTransportLogName());
			} catch (IOException e) {
				mInputTransportLog  = null;
				mOutputTransportLog = null;
			}
		}

		init(cfg);
	}

	public void skipBytes(int numBytes) throws IOException {
		if (mApplicationDataToRead == null)
			throw new  IOException("Connection was closed");

		while (numBytes > mAvailable) {
			numBytes  -= mAvailable;
			mApplicationDataToRead.clear();
			mAvailable = read(mApplicationDataToRead);
		}

		mAvailable -= numBytes;
		if (mAvailable > 0) {
			mApplicationDataToRead.position(mApplicationDataToRead.position()+numBytes);
		} else {
			mApplicationDataToRead.clear();
		}
	}

	public void readBytes(byte[] dest) throws IOException {
		readBytes(dest, 0, dest.length);
	}

	private int read(ByteBuffer dest) throws IOException {
		// assert dest.capacity() == ApplicationBufferSize
		int produced = 0;
		int rd = 0;
		if (mPacketToRead.position() == 0) { // need to fill,  packet shall be clear
			rd = mInput.read(mPacketToRead.array(), 0, mPacketBufferSize);
			if (rd < 0) throw new IOException("Connection closed");
			mPacketToRead.limit(rd);
		} else {
			mPacketToRead.flip();
		}
//		boolean cont = true; 
//		while (cont) {
			SSLEngineResult result;
			try {
				result = mEngine.unwrap(mPacketToRead, dest);
			} catch (java.lang.Exception e) {
				throw new IOException("error when unwrapping " + e.getMessage());
			}		
			produced += result.bytesProduced();
			switch (result.getStatus()) {
			case OK:
				if (mPacketToRead.remaining() == 0) {
					mPacketToRead.clear();
//					cont = false;
					break;
				}
				mPacketToRead.compact();
//				mPacketToRead.flip();
				break;
			case BUFFER_UNDERFLOW:
//				cont = false; // better flushing what has been produced, to avoid overflow as we may already have decoded
				              // quite some packets
				int offset = mPacketToRead.remaining();
				if (offset > 0) {
					// there are some data left, prefill for the next read call as we must force input read
					rd = mPacketBufferSize - offset;
					mPacketToRead.compact();
					rd = mInput.read(mPacketToRead.array(), offset, rd);
					if (rd < 0) throw new IOException("Connection closed");
					mPacketToRead.position(offset+rd);
				} else {
					mPacketToRead.clear();
				}
				break;
			default:
				// assumption a PacketBufferSize can fill at most an ApplicationBufferSize, thus no overflow expected: => ApplicationBufferSize == 0 when reading...
				throw new IOException("TLS "+result.getStatus() + " error while reading "+mPacketToRead+" dest:"+dest);
			}
//		}
		if (mInputTransportLog != null && produced > 0) {
			mInputTransportLog.write(dest.array(), 0, produced);
		}
		return produced;
	}

	public void readBytes(byte[] dest, int offset, int length) throws IOException {
		if (mApplicationDataToRead == null)
			throw new  IOException("Connection was closed");
		if (length > mAvailable) {

			if (mAvailable > 0) {
				mApplicationDataToRead.get(dest, 0, mAvailable);
				length -= mAvailable;
				offset  = mAvailable;
				mApplicationDataToRead.clear();
			}

			while (length >= mApplicationBufferSize) {
				ByteBuffer dst = ByteBuffer.wrap(dest, offset, length);
				int rd = read(dst);
				length -= rd;
				offset += rd;
			}

			while (length > 0) {
				int rd = read(mApplicationDataToRead);
				mApplicationDataToRead.position(0);
				mApplicationDataToRead.limit(rd);

				if (rd >= length) {
					mAvailable = rd;
					break;
				}

				mApplicationDataToRead.get(dest, offset, rd);
				mApplicationDataToRead.clear();
				offset += rd;
				length -= rd;
			}

		}

		mApplicationDataToRead.get(dest, offset, length);
		mAvailable -=  length;
		if (mAvailable == 0)
			mApplicationDataToRead.clear();
//		for (int i = 0, e = dest.length; i < e; i++)
//	        System.err.println( "'" + dest[i] + "'" );

	}

	public int readByte() throws IOException {
		if (mApplicationDataToRead == null)
			throw new  IOException("Connection was closed");
		while (mAvailable == 0) {
			mAvailable = read(mApplicationDataToRead);
			if (mAvailable > 0)
				mApplicationDataToRead.flip();
		}
		byte b = mApplicationDataToRead.get();
//	    System.err.println( "'" + b + "'" );
		mAvailable--;
		if (mAvailable == 0)
			mApplicationDataToRead.clear();
		return b;
	}

	public void writeBytes(byte [] src) throws IOException {
		if (mApplicationDataToSend == null) throw new IOException("Connection was closed");

		int length = src.length;
		if (mOutputTransportLog != null && length > 0) {
			mOutputTransportLog.write(src, 0, length);
		}
		if ((length+mApplicationDataSize) > mApplicationBufferSize) { // use some gather call?
			if (mApplicationDataSize > 0) { 
				write(mApplicationDataToSend);
				mApplicationDataSize = 0;
			}
			ByteBuffer buf = ByteBuffer.wrap(src, 0, length);
			buf.position(buf.limit());
			write(buf);
			return;
		}
		mApplicationDataToSend.put(src, 0, length);
		mApplicationDataSize += length; // getPosition shall be the same...
	}

	public void writeBytesAndFlush(byte [] src) throws IOException {
		writeBytes(src);
		if (mApplicationDataSize > 0) {
			write(mApplicationDataToSend);
			mApplicationDataSize = 0;
		}
	}

	public void write(ByteBuffer src) throws IOException {
		src.flip();
		SSLEngineResult result;
		do {
			try {
				result = mEngine.wrap(src, mPacketToSend);
				if (result.getStatus() != SSLEngineResult.Status.OK) throw new IOException("error when wrapping");
			} catch (SSLException e) {
				throw new IOException("error when wrapping " + e.getMessage());
			}
			mOutput.write(mPacketToSend.array(), 0, result.bytesProduced());
			mPacketToSend.clear();
		} while (src.hasRemaining());
		src.clear();
	}

	public void writeByte(int value) throws IOException {
		if (mApplicationDataToSend == null) throw new IOException("Connection was closed");
		if (mApplicationDataSize == mApplicationBufferSize) {
			write(mApplicationDataToSend);
			mApplicationDataSize = 0;
		}
		mApplicationDataToSend.put((byte) value);
		if (mOutputTransportLog != null) {
			mOutputTransportLog.write((byte) value);
		}
		mApplicationDataSize++; // getPosition shall be the same...
	}

	public void writeByteAndFlush(int value) throws IOException {
		if (mApplicationDataToSend == null) throw new IOException("Connection was closed");
		if (mApplicationDataSize == mApplicationBufferSize) {
			write(mApplicationDataToSend);
		}
		mApplicationDataToSend.put((byte) value);
		write(mApplicationDataToSend); // hopefully works with 1 byte... else one may fill with useless 'zzz'
		if (mOutputTransportLog != null) {
			mOutputTransportLog.write((byte) value);
		}
 		mApplicationDataSize = 0;
	}

	private SSLEngine          mEngine;
	private int                mApplicationDataSize;
	private int                mPacketBufferSize;
	private int                mApplicationBufferSize;
	private int                mAvailable;
	private ByteBuffer         mPacketToSend;
	private ByteBuffer         mApplicationDataToSend;
	private OutputStream       mOutput;
	private FileOutputStream   mOutputTransportLog;
	private ByteBuffer         mPacketToRead;
	private ByteBuffer         mApplicationDataToRead;
	private InputStream        mInput;
	private FileOutputStream   mInputTransportLog;

	public void init(ClientConfiguration cfg) throws IOException
	{
		try {
			context = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			throw new IOException("TLSServerTransport:IOException:" + e.getMessage() );
		}

	        dummyTrustManager trustManager = new dummyTrustManager();
	        dummyKeyManager   keyManager   = new dummyKeyManager(cfg.getX509Certificate(),cfg.getPrivateKey());

		KeyManager[] km =   { keyManager   };
		TrustManager[] tm = { trustManager };

		try {
			context.init(km, tm, null);
		} catch (KeyManagementException e) {
			throw new IOException("TLSServerTransport:KeyManagementException:" + e.getMessage() );
		}

		SSLEngine engine = context.createSSLEngine();
		engine.setUseClientMode(false);
		engine.setNeedClientAuth(false);
		engine.setWantClientAuth(false);

		mEngine                 = engine;
		SSLSession s            = engine.getSession();
		mApplicationBufferSize  = s.getApplicationBufferSize();
		mPacketBufferSize       = s.getPacketBufferSize();
		mPacketToSend           = ByteBuffer.allocate(mPacketBufferSize);
		mPacketToRead           = ByteBuffer.allocate(mPacketBufferSize);
		mApplicationDataToSend  = ByteBuffer.allocate(mApplicationBufferSize); // may rather use the configuration one
		mApplicationDataToRead  = ByteBuffer.allocate(mApplicationBufferSize);

		try {
			@SuppressWarnings("rawtypes")
			Class c = Class.forName("javax.net.ssl.SSLEngine");
			for (Method m : c.getDeclaredMethods() ) {
				if (m.getName() == "getSSLParameters") { //this is not supported on Android < 2.3
					SSLParameters params = engine.getSSLParameters();
					params.setCipherSuites(new String [] {
						"TLS_RSA_WITH_AES_128_CBC_SHA"
					});
					engine.setSSLParameters(params);
					}
				}
			}
		catch (ClassNotFoundException e) { //shouldnt ever happen
		    throw new IOException("TLSServerTransport:init_failed:" + e.getMessage(), e);
		}

		try {
			do_handshake();
		}
		catch (SSLException e) {
		    throw new IOException("TLSServerTransport:handshake_failed:" + e.getMessage(), e);
		}
		catch (IOException e) {
		    throw new IOException("TLSServerTransport:handshake_failed:" + e.getMessage(), e);
		}
	}

	void do_handshake() throws SSLException, IOException
	{

		SSLEngine engine = mEngine;
	        HandshakeStatus result =  HandshakeStatus.NEED_UNWRAP;
	        while(true) {
	            switch(result) {
	            case FINISHED:
	                return;
	            case NEED_TASK:
	                Runnable task;
	                while((task=engine.getDelegatedTask()) != null) {
	                    task.run();
	                }
	                break;
	            case NEED_UNWRAP: {
			mAvailable = read(mApplicationDataToRead); // shall be 0...
	                break;
	            }
	            case NEED_WRAP: {
			write(mApplicationDataToSend); // shall be empty...
	                break;
	            }
	            case NOT_HANDSHAKING:
	                return;
	            }
	            result = engine.getHandshakeStatus();

        }
	}

	public void close() throws IOException {
		mPacketToSend           = null;
		mPacketToRead           = null;
		mApplicationDataToSend  = null; // may rather use the configuration one
		mApplicationDataToRead  = null;
		try {
			if (mInputTransportLog != null)
				mInputTransportLog.close();
		} catch (IOException e) {
		}
		mInputTransportLog = null;
		try {
			if (mOutputTransportLog != null)
				mOutputTransportLog.close();
		} catch (IOException e) {
		}
		mOutputTransportLog = null;
		mInput.close();
		mOutput.close();
	}

	private SSLEngine engine = null;
	private SSLContext context = null;

	public class dummyKeyManager extends X509ExtendedKeyManager {
		X509Certificate cert;
		PrivateKey privKey;

		public dummyKeyManager(X509Certificate _cert,PrivateKey _privKey) {
			privKey = _privKey;
			cert = _cert;
		}

		public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
			return null;
		}

		public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
			if (keyType == "RSA") {
				return "dummyServer";
			}
			return null;
		}

		public String chooseServerAlias(String keyType, Principal[] issuers, Socket arg2) {
			return null;
		}

		public X509Certificate[] getCertificateChain(String arg0) {
			return new X509Certificate[] { cert };
		}

		public String[] getClientAliases(String arg0, Principal[] arg1) {
			return null;
		}

		public PrivateKey getPrivateKey(String arg0) {
			return privKey;
		}

		public String[] getServerAliases(String arg0, Principal[] arg1) {
			return null;
		}
	}

	/*
	 * This class should never be called
	 * */
	public class dummyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
		throws CertificateException {
			throw new CertificateException("dummyTrustManager:checkClientTrusted called");
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
		throws CertificateException {
			throw new CertificateException("dummyTrustManagercheckServerTrusted called");
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
