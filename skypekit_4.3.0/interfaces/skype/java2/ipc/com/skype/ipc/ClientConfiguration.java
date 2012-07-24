package com.skype.ipc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import com.skype.util.PemReader;
//import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

/***
 * Configuration of the ipc wrapper. All these parameters are checked once at the initialization phase, and any modifications
 * done afterwards will be ignored.
 */
public class ClientConfiguration {

	public ClientConfiguration() {
		dispatchAll                = false;
		numRetries                 = 3;
		setup                      = null;
		certificateContents        = null;
		privatekeyContents         = null;
		pemFileName                = null;
		initialLatency             = 600;
		transportInputBufferSize   = 512;
		transportOutputBufferSize  = 512;
		transportLogBaseName       = null;
		noTls                      = false;
		transportFactory           = new TransportFactory();
		setTcpTransport();
	}

        /***
         * use TCP socket for the connection to the runtime with the default settings, port 8963 on the local host
         */
	public void setTcpTransport() {
		useLocalTransport    = false;
		this.ip              = "127.0.0.1";
		this.port            = 8963;
	}

        /***
         * use TCP socket for the connection to the runtime with custom settings
         * @param ip the ip address of the runtime host
         * @param port the port used by the runtime
         */
	public void setTcpTransport(String ip, int port) {
		useLocalTransport    = false;
		this.ip              = ip;
		this.port            = port;
	}

        /***
         * tells if the configuration was set to use a TCP socket with runtime
         */
	public boolean useTcpTransport() {
		return !useLocalTransport;
	}

        /***
         * get the ip address to be used with a TCP connection
         */
	public String getIp() {
		return ip;
	}

        /***
         * get the port to be used with a TCP connection
         */
	public int getPort() {
		return port;
	}

        /***
         * when an event or a property change notification is recieved on an object that
         * doesn't exist yet, create the object and forward the notification. The default
         * behavior is to discard the notification.
         */
	public void setDispatchAll() {
		dispatchAll = true;
	}

        /***
         * tells if the event or a property change notification will always be dispatched (default if false)
         */
	public boolean isDispatchAll() {
		return dispatchAll;
	}

        /***
         * turn off Tls (use it only if the runtime support this)
         */
	public void dontUseTls() {
		noTls = true;
	}

        /***
         * check if Tls mode is turned off
         */
	public boolean isWithoutTls() {
		return noTls;
	}

        /***
         * check if Tls mode is turned on (default)
         */
	public boolean isWithTls() {
		return !noTls;
	}
	
        /***
         * specify the certificate file to be used for the Tls connection
         * @param certificateFilename <x>.pem file (ensure that <x>.der is present as well)
         * returns the input filename argument
         */
	public String setCertificate(String certificateFilename) {
		this.pemFileName = certificateFilename;
		return certificateFilename;
	}
	
        /***
         * Do not use, internal only
         */
	public void setCertificateContents(byte[] contents)
	{
		certificateContents = contents;
	}

	/***
	 * Do not use, internal only
	 */
	public void setPrivateKeyContents(byte[] contents)
	{
		privatekeyContents = contents;
	}

        /***
         * Do not use, internal only, loads the certicate
         */
	public String getCertificate() throws IOException
	{
		assert(!isWithoutTls());
		
		if (certificateContents != null && certificateContents.length > 0)
			return new String(certificateContents);
		
		if ( ! hasFilePath())
			throw new IOException("no PemFile");
		
		File tokenFile = new File(pemFileName);
		InputStream in = new FileInputStream(tokenFile);
		long fileSize = tokenFile.length();
		byte[] bytes = new byte[(int) fileSize];
		int offset = 0;
		int count = 0;
		while (offset < fileSize) {
			count = in.read(bytes, offset, (int) fileSize - offset);
			if (count >= 0)

				offset += count;
			else
				throw new IOException("Unable to read App Token file: " + tokenFile.getName());
		}
		if (in != null)
		in.close();

		String rawString = new String(bytes);
		return rawString.trim();
	}

        /***
         * Do not use, internal only, loads the certicate
         */
	public X509Certificate getX509Certificate() throws IOException
	{
		assert (isWithTls());

		try {
			if (hasFilePath()) {
				PemReader donkey = new PemReader(pemFileName);
				return donkey.getCertificate();
			}
			else if (certificateContents.length == 0 || privatekeyContents.length == 0) {
				throw new IOException("no certificate data.");
			}
			else {
				ByteArrayInputStream certStream = new ByteArrayInputStream(certificateContents);
				ByteArrayInputStream keyStream = new ByteArrayInputStream(privatekeyContents);
				PemReader donkey = new PemReader(certStream, keyStream);
				return donkey.getCertificate();
			}
		}
		catch (InvalidKeySpecException e) {
			throw new IOException("Invalid certificate data.");
		}
	}

	/***
	 * Do not use, internal only, loads the certicate
	 */
	public PrivateKey getPrivateKey() throws IOException
	{
		assert (isWithTls());
		
		if (hasFilePath()) {
			PemReader donkey = new PemReader(pemFileName);
			return donkey.getKey();
		}
		else if (certificateContents.length == 0 || privatekeyContents.length == 0) {
			throw new IOException("no PemFile");
		}
		else {
			ByteArrayInputStream certStream = new ByteArrayInputStream(certificateContents);
			ByteArrayInputStream keyStream = new ByteArrayInputStream(privatekeyContents);
			PemReader donkey = new PemReader(certStream, keyStream);
			return donkey.getKey();
		}
	}

        /***
         * Decide how many attempts at connecting to the runtime shall be done
         * @param num with num > 0 and num < 10
         * return num if valid or latest valid input or default, ie 3
         */
	public int setConnectionNumRetries(int num) {
		if (num > 0 && num < 10)
			numRetries = num;
		return numRetries;
	}

        /***
         * check how many attempts at connecting to the runtime will be done
         */
	public int getConnectionNumRetries() {
		return numRetries;
	}

        /***
         * Decide what initial latency in milliseconds will be used between each retry
         * The policy is to increase this latency by 50% after each failed retry.
         * The default is 600ms, so that the default pattern is 600 (+600ms) -> 900 (+1500ms) -> 1350 (+2850ms)
         * @param ms with ms > 100 and ms < 1000
         * return ms if valid or latest valid input or default, ie 600
         */
	public int setConnectionRetryInitialLatency(int ms) {
		if (ms > 100 && ms < 1000)
			initialLatency = ms;
		return initialLatency;
	}

        /***
         * check the initial latency used in case of retries
         */
	public int getConnectionRetryInitialLatency() {
		return initialLatency;
	}

        /***
         * Recommend the transport input buffer size (may or may not be used by the transport, indicative only)
         * for the connection to the runtime
         * @param bytes with bytes >= 256 and bytes < 8096 , bytes is rounded to the next power of 2
         * return bytes if valid or latest valid input or default, ie 512
         */
	public int setTransportInputBufferSize(int bytes) {       // return 2^n so that 2^n >= bytes
		if (bytes >= 256 && bytes <= 8096) {
			int n = 256;
			while (n < bytes) n += n;
			transportInputBufferSize = n;
		}
		return transportInputBufferSize;
	}

        /***
         * check the recommended transport input buffer size
         */
	public int getTransportInputBufferSize() {
		return transportInputBufferSize;
	}

        /***
         * Recommend the transport output buffer size (may or may not be used by the transport, indicative only)
         * for the connection to the runtime
         * @param bytes with bytes >= 256 and bytes < 8096 , bytes is rounded to the next power of 2
         * return bytes if valid or latest valid input or default, ie 512
         */
	public int setTransportOutputBufferSize(int bytes) {       // return 2^n so that 2^n >= bytes
		if (bytes >= 256 && bytes <= 8096) {
			int n = 256;
			while (n < bytes) n += n;
			transportOutputBufferSize = n;
		}
		return transportOutputBufferSize;
	}

        /***
         * check the recommended transport output buffer size
         */
	public int getTransportOutputBufferSize() {                // default 512
		return transportOutputBufferSize;
	}

        /***
         * turn on the recording of the data exchanged between the runtime and the java client
         * or turn it off off the file name is empty (default). This is configured once 
         * @param baseName is the base name for the transport log which consists in 2 files
         * - <baseName>_log_in.1, data from the runtime to the client
         * - <baseName>_log_out.1,  data from the client to the runtime
         */
	public void generateTransportLog(String baseName) {
		if (baseName == null || baseName.equals("")) return;
		transportLogBaseName = baseName;
	}

        /***
         * check if a transport log shall be produced
         */
	public boolean generateTransportLog() {
		return transportLogBaseName != null && !transportLogBaseName.equals("");
	}

        /***
         * get the input transport log file name
         */
	public String getInputTransportLogName() {
		return transportLogBaseName + "_log_in.1";
	}

        /***
         * get the output transport log file name
         */
	public String getOutputTransportLogName() {
		return transportLogBaseName + "_log_out.1";
	}

        /***
         * By default only a TCP transport is supported to communicate with the runtime,
         * but with setting a TransportFactory 1 further non portable transport could be used, 
         * provided that they are supported by the runtime as well. For example android or
         * linux runtime shall accept unix socket too.
         */
	public void setTransportFactory(TransportFactory factory) {
		transportFactory = factory;
	}

	public TransportFactory getTransportFactory() {
		return transportFactory;
	}

	/***
         * By default a runtime only forwards the int alike value in case of property changes
	 * this function allows forwarding the string values as well which can highly improve
	 * the performances if your application is always getting the strings on the change
	 * notification.
         */
	public void fowardStringChangedValue() {
		if (setup == null)
			setup = new String();
		setup += "SkypeKit/FowardStringChangedValue=1\n";
	}


        /***
         * internal, don't use unless implementing a TransportFactory
         */
	public String getHandshakeSetup() {
		return setup;
	}

	private boolean hasFilePath()
	{
		return pemFileName != null && ! pemFileName.isEmpty();
	}
	
	private int     numRetries;
	private int     initialLatency;
	private int     transportInputBufferSize;
	private int     transportOutputBufferSize;
	private String  transportLogBaseName;
	private String  pemFileName;
	private byte[]  certificateContents;
	private byte[]  privatekeyContents;
	private boolean noTls;
	private boolean dispatchAll;
	protected boolean useLocalTransport;
	private String  ip;
	private int     port;
	private TransportFactory transportFactory;
	private String  setup;
}

