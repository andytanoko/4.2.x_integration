package com.gridnode.pdip.app.servicemgmt.router;

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: GNHttpConnection.java
 * 
 * ***************************************************************************
 * Date         Author             Changes
 * ***************************************************************************
 * Nov 20 2002  Qingsong           Created
 * Jan 12 2007  Neo Sok Lay        Use ProxySelector for proxy connections
 * May 07 2007  Neo Sok Lay        GNDB00028341: Construct own SSL Context instead
 *                                 of using default.
 */

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.*;

import javax.net.ssl.*;

import com.gridnode.pdip.app.servicemgmt.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.servicemgmt.helpers.Logger;
import com.gridnode.pdip.framework.net.GNProxyInitializer;
import com.gridnode.pdip.framework.util.StringUtil;

class TimedURLConnection
{
	public TimedURLConnection()
	{
	}

	public static OutputStream getOutputStream(	URLConnection urlconnection,
																							int timeout) throws IOException
	{
		Object event = new Object();
		OutputStreamThread outputstreamthread = new OutputStreamThread(urlconnection,
																																	 event);
		outputstreamthread.start();

		try
		{
			if (!outputstreamthread.isConnected())
			{
				synchronized (event)
				{
					event.wait(timeout);
				}
			}
		}
		catch (InterruptedException ex)
		{
		}
		if (outputstreamthread.isConnected())
			return outputstreamthread.getOutputStream();
		else
		{
			throw outputstreamthread.getException();
		}
	}

	public static InputStream getInputStream(	URLConnection urlconnection,
																						int timeout) throws IOException
	{

		Object event = new Object();
		InputStreamThread inputstreamthread = new InputStreamThread(urlconnection,
																																event);
		inputstreamthread.start();

		try
		{
			if (!inputstreamthread.isConnected())
			{
				synchronized (event)
				{
					event.wait(timeout);
				}
			}
		}
		catch (InterruptedException ex)
		{
		}

		if (inputstreamthread.isConnected())
			return inputstreamthread.getInputStream();
		else
		{
			throw inputstreamthread.getException();
		}
	}

}

class OutputStreamThread extends Thread
{
	private volatile OutputStream a;

	private IOException b;

	private URLConnection c;

	private Object event;

	public void run()
	{
		OutputStream outputstream = null;
		try
		{
			outputstream = c.getOutputStream();
		}
		catch (IOException ioexception)
		{
			b = ioexception;
		}
		a = outputstream;
		synchronized (event)
		{
			event.notifyAll();
		}
	}

	public boolean isConnected()
	{
		return a != null;
	}

	public boolean isError()
	{
		return b != null;
	}

	public OutputStream getOutputStream()
	{
		return a;
	}

	public IOException getException()
	{
		return b;
	}

	public OutputStreamThread(URLConnection urlconnection, Object event)
	{
		a = null;
		b = null;
		c = null;
		c = urlconnection;
		this.event = event;
	}
}

class InputStreamThread extends Thread
{

	private volatile InputStream a;

	private IOException b;

	private URLConnection c;

	private Object event;

	public void run()
	{
		InputStream inputstream = null;
		try
		{
			inputstream = c.getInputStream();
		}
		catch (IOException ioexception)
		{
			b = ioexception;
		}
		a = inputstream;

		synchronized (event)
		{
			event.notifyAll();
		}
	}

	public boolean isConnected()
	{
		return a != null;
	}

	public boolean isError()
	{
		return b != null;
	}

	public InputStream getInputStream()
	{
		return a;
	}

	public IOException getException()
	{
		return b;
	}

	public InputStreamThread(URLConnection urlconnection, Object event)
	{
		a = null;
		b = null;
		c = null;
		c = urlconnection;
		this.event = event;
	}
}

class NoVerificationHostnameVerifier implements HostnameVerifier
{
	public boolean verify(String hostName, SSLSession session)
	{
		return true;
	}
}

class NoAuthSSLSocketFactory extends SSLSocketFactory
{
	private SSLSocketFactory factory;

	private String[] getNoAuthCiperSuites(String ciperSuites[])
	{
		// Count the number of ciper suites.
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 0; i < ciperSuites.length; i++)
		{
			if (ciperSuites[i].indexOf("anon") != -1)
      {
        v.add(new Integer(i));
      }
		}

		String ret[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++)
		{
			int tmp = v.get(i).intValue();
			ret[i] = ciperSuites[tmp];
		}

		return ret;
	}

	public NoAuthSSLSocketFactory(SSLSocketFactory factory)
	{
		this.factory = factory;
	}

	public String[] getSupportedCipherSuites()
	{
		return factory.getSupportedCipherSuites();
	}

	public String[] getDefaultCipherSuites()
	{
		return factory.getDefaultCipherSuites();
	}

	public Socket createSocket(	Socket socket,
															String host,
															int port,
															boolean autoClose) throws IOException
	{
		Socket s = this.factory.createSocket(socket, host, port, autoClose);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setEnabledCipherSuites(this.getNoAuthCiperSuites(ssl
					.getSupportedCipherSuites()));
		}
		return s;
	}

	public Socket createSocket(InetAddress host, int port) throws IOException
	{
		Socket s = this.factory.createSocket(host, port);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setEnabledCipherSuites(this.getNoAuthCiperSuites(ssl
					.getSupportedCipherSuites()));
		}
		return s;
	}

	public Socket createSocket(	InetAddress address,
															int port,
															InetAddress clientAddress,
															int clientPort) throws IOException
	{
		Socket s = this.factory.createSocket(address, port, clientAddress,
																					clientPort);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setEnabledCipherSuites(this.getNoAuthCiperSuites(ssl
					.getSupportedCipherSuites()));
		}
		return s;
	}

	public Socket createSocket(String host, int port) throws IOException
	{
		Socket s = this.factory.createSocket(host, port);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setEnabledCipherSuites(this.getNoAuthCiperSuites(ssl
					.getSupportedCipherSuites()));
		}
		return s;
	}

	public Socket createSocket(	String host,
															int port,
															InetAddress clientHost,
															int clientPort) throws IOException
	{
		Socket s = this.factory.createSocket(host, port, clientHost, clientPort);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setEnabledCipherSuites(this.getNoAuthCiperSuites(ssl
					.getEnabledCipherSuites()));
		}
		return s;
	}
}

class ClientAuthSSLSocketFactory extends SSLSocketFactory
{
	private SSLSocketFactory factory;

	public ClientAuthSSLSocketFactory(SSLSocketFactory factory)
	{
		this.factory = factory;
	}

	public String[] getSupportedCipherSuites()
	{
		return factory.getSupportedCipherSuites();
	}

	public String[] getDefaultCipherSuites()
	{
		return factory.getDefaultCipherSuites();
	}

	public Socket createSocket(	Socket socket,
															String host,
															int port,
															boolean autoClose) throws IOException
	{
		Socket s = this.factory.createSocket(socket, host, port, autoClose);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setUseClientMode(false); // client offers to authenticate itself
			ssl.setNeedClientAuth(true);
		}
		return s;
	}

	public Socket createSocket(InetAddress host, int port) throws IOException
	{
		Socket s = this.factory.createSocket(host, port);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setUseClientMode(false); // client offers to authenticate itself
			ssl.setNeedClientAuth(true);
		}
		return s;
	}

	public Socket createSocket(	InetAddress address,
															int port,
															InetAddress clientAddress,
															int clientPort) throws IOException
	{
		Socket s = this.factory.createSocket(address, port, clientAddress,
																					clientPort);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setUseClientMode(false); // client offers to authenticate itself
			ssl.setNeedClientAuth(true);
		}
		return s;
	}

	public Socket createSocket(String host, int port) throws IOException
	{
		Socket s = this.factory.createSocket(host, port);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setUseClientMode(false); // client offers to authenticate itself
			ssl.setNeedClientAuth(true);
		}
		return s;
	}

	public Socket createSocket(	String host,
															int port,
															InetAddress clientHost,
															int clientPort) throws IOException
	{
		Socket s = this.factory.createSocket(host, port, clientHost, clientPort);
		if (s instanceof SSLSocket)
		{
			SSLSocket ssl = (SSLSocket) s;
			ssl.setUseClientMode(false); // client offers to authenticate itself
			ssl.setNeedClientAuth(true);
		}
		return s;
	}
}

/*NSL20070112
class ProxyServerThread extends Thread
{
	private ServerSocket server;

	private int portno = -1;

	private boolean connected = false;

	public int getPortNo()
	{
		return portno;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public void run()
	{
		try
		{
			server.setSoTimeout(10000);
			Socket client = server.accept();
			try
			{
				InputStream in = client.getInputStream();
				OutputStream out = client.getOutputStream();
				in.close();
				out.close();
				client.close();
			}
			catch (Exception ex)
			{

			}
			server.close();
			connected = true;
		}
		catch (IOException ex)
		{
			connected = false;
		}
	}

	public ProxyServerThread() throws IOException
	{
		server = new ServerSocket(0);
		portno = server.getLocalPort();
	}
}
*/
class ILogIn
{
	public void Info(String msg)
	{
		Logger.log("wsrouter:" + msg);
	}

	public void Debug(String msg)
	{
		Logger.debug("wsrouter:" + msg);
	}

	/**
	 * @param msg
	 * @param e
	 */
	public void Warn(String msg, Throwable e)
	{
		Logger.warn("wsrouter:" + msg, e);
	}
	
	/**
	 * @deprecated Use error(String, String, Object)
	 * @see Logger#error(String, String, Object)
	 */
	public void Error(String msg, Throwable e)
	{
		Logger.err("wsrouter:" + msg, e);
	}
	
	/**
	 * @param errorCode
	 * @param msg
	 * @param e
	 */
	public void Error(String errorCode, String msg, Throwable e)
	{
		Logger.error(errorCode, "wsrouter:" + msg, e);
	}

	private boolean logheader = true;

	static ILogIn httpMessageContext = new ILogIn();

	static public byte[] getMessage(InputStream is) throws IOException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int numBytesRead = 0;
		byte[] subbuffer = new byte[1000];
		while (numBytesRead != -1)
		{
			numBytesRead = is.read(subbuffer, 0, 1000);
			if (numBytesRead > 0) buffer.write(subbuffer, 0, numBytesRead);
		}
		return buffer.toByteArray();
	}

	static public ILogIn getInstance()
	{
		return httpMessageContext;
	}

	public boolean getLogheader()
	{
		return logheader;
	}

	static public String getJava_home()
	{
		String javaHome = System.getProperty("java.home");
		return javaHome;
	}

	static public String getDefaultTrustedStore()
	{
		Properties prop = System.getProperties();
		String javaHome = getJava_home();
		javaHome += "/lib/security/cacerts";
		return prop.getProperty("javax.net.ssl.trustStore", javaHome);
	}

	static public String getTructStoreName(String filename)
	{
		if (emptyString(filename))
			return getDefaultTrustedStore();
		else
			return filename;
	}

	static public String getTructStorePassword(String password)
	{
		if (emptyString(password))
		{
			return System.getProperties()
					.getProperty("javax.net.ssl.trustStorePassword", "changeit");
		}
		else
			return password;
	}

	static public boolean emptyString(String str)
	{
		if (str == null || str.length() <= 0)
			return true;
		else
			return false;
	}

	public String Exception2String(Throwable e)
	{
		ByteArrayOutputStream detailedError = new ByteArrayOutputStream();
		PrintStream outS = new PrintStream(detailedError);
		if (e != null) e.printStackTrace(outS);
		outS.flush();
		outS.close();
		return new String(detailedError.toByteArray());
	}

}

public class GNHttpConnection
{
	static ILogIn context = ILogIn.getInstance();
	/*NSL20070112
	private Object newProxySetting(	boolean success,
																	boolean usingProxy,
																	String ip,
																	String port)
	{
		Object[] setting = new Object[4];
		if (success)
			setting[0] = Boolean.TRUE;
		else
			setting[0] = Boolean.FALSE;

		if (usingProxy)
			setting[1] = Boolean.TRUE;
		else
			setting[1] = Boolean.FALSE;
		if (ip == null) ip = "";
		if (port == null) port = "";
		setting[2] = ip;
		setting[3] = port;
		return setting;
	}
  */
	protected String url;

	protected URL myURLobject;

	protected String CRLF; // carrige return, line-feed

	protected boolean authenticateServer;

	protected boolean authenticateClient;

	protected boolean verifyServerHostname;

	protected String keyStoreFile, keyStorePassword;

	protected String trustStoreFile, trustStorePassword;

	protected int responseCode;

	protected byte[] responseMessage; // carrige return, line-feed

	protected HttpURLConnection connection;

	protected Hashtable<String,String> responseheaders;

	protected int timeout = 1;

	public static final String REQUEST_METHOD_GET = "GET";

	public static final String REQUEST_METHOD_POST = "POST";

	// private static final String CLASS_NAME = "GNHttpConnection";

  /*NSL20070112
	static protected String auth_username = "";

	static protected String auth_password = "";

	static private String http_proxy_pac = "";

	static private String http_proxy_url = "";

	static private String http_proxy_port = "";

	private String config_http_proxy_url = "";

	private String config_http_proxy_port = "";

	static private boolean vm_proxy_support = false;

	static private boolean vm_proxy_support_tested = false;

	private boolean usingproxy = false;

	static private Vector pacProxyList = new Vector();

	static private boolean retrievedPac = false;

	static private Hashtable proxysettingList = new Hashtable();
  */
  private static Properties _proxySetting;
  
	private String requestMethod = REQUEST_METHOD_GET;

  /*NSL20070112
	static public void clearProxySettings()
	{
		pacProxyList = new Vector();
		proxysettingList = new Hashtable();
		retrievedPac = false;
		vm_proxy_support_tested = false;
		vm_proxy_support = false;
	}

	public boolean testingVMProxy()
	{
		context
				.Info("HttpMessageContext[testingVMProxy] Testing VM Proxy Support ");
		if (vm_proxy_support_tested)
		{
			context
					.Info("HttpMessageContext[testingVMProxy] VM Proxy Support Tested Already "
								+ vm_proxy_support);
			return vm_proxy_support;
		}
		ProxyServerThread proxyserverthread = null;
		try
		{
			proxyserverthread = new ProxyServerThread();
			proxyserverthread.start();
			GNHttpConnection connection = new GNHttpConnection(
																													"http://www.gridnode.com/",
																													10);
			connection.setUsingProxy(true);
			connection.setConfig_http_proxy_url("localhost");
			connection.setConfig_http_proxy_port("" + proxyserverthread.getPortNo());
			connection.connect();
			connection.doNativeGet(null);
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
		}
		vm_proxy_support_tested = true;
		vm_proxy_support = proxyserverthread.isConnected();
		if (vm_proxy_support)
			context.Info("HttpMessageContext[testingVMProxy] VM Proxy Support OK");
		else
			context.Info("HttpMessageContext[testingVMProxy] VM Proxy Support Fail");
		return vm_proxy_support;
	}

	static public String getHttp_proxy_pac()
	{
		return http_proxy_pac;
	}

	static public void setHttp_proxy_pac(String http_proxy_pac1)
	{
		http_proxy_pac = http_proxy_pac1;
	}

	public String getConfig_http_proxy_port()
	{
		return config_http_proxy_port;
	}

	public String getConfig_http_proxy_url()
	{
		return config_http_proxy_url;
	}

	public void setConfig_http_proxy_port(String config_http_proxy_port1)
	{
		config_http_proxy_port = config_http_proxy_port1;
	}

	public void setConfig_http_proxy_url(String config_http_proxy_url1)
	{
		config_http_proxy_url = config_http_proxy_url1;
	}

	static public String getAuth_password()
	{
		return auth_password;
	}

	static public String getAuth_username()
	{
		return auth_username;
	}

	static public void setAuth_password(String auth_password1)
	{
		auth_password = auth_password1;
	}

	static public void setAuth_username(String auth_username1)
	{
		auth_username = auth_username1;
	}

	static public String getHttp_proxy_port()
	{
		return http_proxy_port;
	}

	static public String getHttp_proxy_url()
	{
		return http_proxy_url;
	}

	static public void setHttp_proxy(	String http_proxy_pac,
																		String http_proxy_url,
																		String http_proxy_port,
																		String http_proxy_user,
																		String http_proxy_passwd)
	{
		clearProxySettings();
		setHttp_proxy_pac(http_proxy_pac);
		setHttp_proxy_url(http_proxy_url);
		setHttp_proxy_port(http_proxy_port);
		setAuth_username(http_proxy_user);
		setAuth_password(http_proxy_passwd);
	}

	static public void setHttp_proxy_port(String http_proxy_port1)
	{
		http_proxy_port = http_proxy_port1;
	}

	static public void setHttp_proxy_url(String http_proxy_url1)
	{
		http_proxy_url = http_proxy_url1;
	}

	public boolean isUsingproxy()
	{
		return usingproxy;
	}

	public void setUsingproxy(boolean usingproxy)
	{
		this.usingproxy = usingproxy;
	}

	public Vector parseProxyList(String content)
	{
		Vector proxyList = new Vector();
		int index = 0;
		do
		{
			index = content.indexOf("return ");
			if (index < 0) index = content.indexOf("RETURN ");
			if (index >= 0)
			{
				content = content.substring(index);
				content = content.substring(content.indexOf("\"") + 1);
				String pacproxy = content.substring(0, content.indexOf("\""));
				if (pacproxy.indexOf("DIRECT") == -1)
				{
					pacproxy = pacproxy.substring(content.indexOf(" ") + 1);
					if (proxyList.indexOf(pacproxy) < 0) proxyList.add(pacproxy);
				}
			}
		}
		while (index >= 0);
		return proxyList;
	}

	public int prepareProxy()
	// 0 OK
	// -1 error
	// 1 not config
	{
		synchronized (context)
		{
			if (getHttp_proxy_pac().length() <= 0
					&& (getHttp_proxy_url().length() <= 0 || getHttp_proxy_port()
							.length() <= 0))
			{
				context.Debug("GNHttpConnection[prepareProxy] no Proxy configured");
				return 1;
			}
			if (!testingVMProxy())
			{
				context
						.Debug("GNHttpConnection[prepareProxy] Cannot Support Proxy for this JVM: "
										+ ILogIn.getJava_home());
				return -1;
			}
			try
			{
				if (getHttp_proxy_pac().length() > 0)
				{
					context.Debug("GNHttpConnection[prepareProxy] has configured pac");
					if (!retrievedPac)
					{
						context
								.Debug("GNHttpConnection[prepareProxy] retrieving pac script from "
												+ getHttp_proxy_pac());
						GNHttpConnection connection = new GNHttpConnection(
																																getHttp_proxy_pac(),
																																5);
						connection.setUsingProxy(false);
						connection.connect();
						String pacsetting = new String(connection.doNativeGet(null));
						pacProxyList = parseProxyList(pacsetting);
						retrievedPac = true;
					}
				}
			}
			catch (Exception ex)
			{
				context
						.Debug("GNHttpConnection[prepareProxy] retrieve pac script error+\r\n"
										+ context.Exception2String(ex));
			}
			if (getHttp_proxy_url().length() > 0 && getHttp_proxy_port().length() > 0)
			{
				String aproxy = getHttp_proxy_url() + ":" + getHttp_proxy_port();
				if (pacProxyList.indexOf(aproxy) < 0) pacProxyList.add(aproxy);
			}
			if (pacProxyList.size() > 0)
				return 0;
			else
				return 1;
		}
	}

	class AuthImpl extends Authenticator
	{
		protected PasswordAuthentication getPasswordAuthentication()
		{
			if (auth_username != null && auth_username.length() > 0)
				return new PasswordAuthentication(auth_username, auth_password
						.toCharArray());
			else
				return null;
		}
	}
  */
  
	public GNHttpConnection(String url)
	{
		this(url, 0);
	}

	public GNHttpConnection(String url, int timeout)
	{
		this(url, false, false, false, "", "", "", "", timeout);
	}

	public GNHttpConnection(String url, boolean authenticateServer,
													boolean authenticateClient,
													boolean verifyServerHostname, String keyStoreFile,
													String keyStorePassword, String trustStoreFile,
													String trustStorePassword)
	{
		this(url, authenticateServer, authenticateClient, verifyServerHostname,
					keyStoreFile, keyStorePassword, trustStoreFile, trustStorePassword, 0);
	}

	public GNHttpConnection(String url, boolean authenticateServer,
													boolean authenticateClient,
													boolean verifyServerHostname, String keyStoreFile,
													String keyStorePassword, String trustStoreFile,
													String trustStorePassword, int timeout)
	{
		init(url, authenticateServer, authenticateClient, verifyServerHostname,
					keyStoreFile, keyStorePassword, trustStoreFile, trustStorePassword,
					timeout);
	}

	private void init(String url,
										boolean authenticateServer,
										boolean authenticateClient,
										boolean verifyServerHostname,
										String keyStoreFile,
										String keyStorePassword,
										String trustStoreFile,
										String trustStorePassword,
										int timeout)
	{
		this.authenticateServer = authenticateServer;
		this.authenticateClient = authenticateClient;
		this.verifyServerHostname = verifyServerHostname;
		this.keyStoreFile = keyStoreFile;
		this.keyStorePassword = keyStorePassword;
		this.trustStoreFile = trustStoreFile;
		this.trustStorePassword = trustStorePassword;
		this.url = url;
		this.timeout = timeout;

		char newLineArray[] = new char[2];
		newLineArray[0] = (char) 13;
		newLineArray[1] = (char) 10;
		this.CRLF = new String(newLineArray);
	}

	public String logMessageSetting()
	{
		String messageConfig = "GNHTTPConnection Settings:\r\n";
		messageConfig += "   URL[" + url + "]\r\n" 
                      //+ "   Using Proxy?"
											//+ isUsingProxy() + "   Proxy Setting[" + auth_username
											//+ ":" + auth_password + "@" + getConfig_http_proxy_url()
											//+ ":" + getConfig_http_proxy_port() + "]\r\n"
                      + "   Proxy Setting["+_proxySetting+"]\r\n"
											+ "   Timeout[" + timeout + "]\r\n";
		messageConfig += "   auth server[" + authenticateServer + "]\r\n"
											+ "   verify hostname[" + verifyServerHostname
											+ "]\r\n\r\n" + "   keystorefile[" + keyStoreFile
											+ "]\r\n" + "   keystorepass[" + keyStorePassword
											+ "]\r\n";

    messageConfig += "   truststorefile["
											+ ILogIn.getTructStoreName(trustStoreFile) + "]\r\n"
											+ "   truststorepass["
											+ ILogIn.getTructStorePassword(trustStorePassword)
											+ "]\r\n";

		messageConfig += "   java.protocol.handler.pkgs["
											+ System.getProperty("java.protocol.handler.pkgs")
											+ "]\r\n" + "   javax.net.ssl.trustStore = "
											+ System.getProperty("javax.net.ssl.trustStore")
											+ "]\r\n" + "   javax.net.ssl.trustStorePassword = "
											+ System.getProperty("javax.net.ssl.trustStorePassword")
											+ "]\r\n" + "   javax.net.ssl.keyStore = "
											+ System.getProperty("javax.net.ssl.keyStore") + "]\r\n"
											+ "   javax.net.ssl.keyStorePassword = "
											+ System.getProperty("javax.net.ssl.keyStorePassword")
											+ "]\r\n";
		return messageConfig;
	}

	public void disconnect() throws IOException
	{
		connection.disconnect();
	}

	protected HttpURLConnection connect(String url) throws Exception
	{
		context.Debug("GNHttpConnection [connect] connect");
		URLConnection uc = null;
		/*synchronized (context)
		{
			Properties prop = System.getProperties();*/
      /*NSL20070112
			if (isUsingProxy())
			{
				prop.setProperty("proxySet", "true");
				prop.setProperty("proxyHost", getConfig_http_proxy_url());
				prop.setProperty("proxyPort", getConfig_http_proxy_port());

				prop.setProperty("http.proxySet", "true");
				prop.setProperty("http.proxyHost", getConfig_http_proxy_url());
				prop.setProperty("http.proxyPort", getConfig_http_proxy_port());
				prop.setProperty("https.proxyHost", getConfig_http_proxy_url());
				prop.setProperty("https.proxyPort", getConfig_http_proxy_port());
			}
			else
			{
				prop.remove("proxySet");
				prop.remove("proxyHost");
				prop.remove("proxyPort");

				prop.remove("http.proxySet");
				prop.remove("http.proxyHost");
				prop.remove("http.proxyPort");
				prop.remove("https.proxyHost");
				prop.remove("https.proxyPort");
			}
			Authenticator.setDefault(new AuthImpl());
      */
			// prop.put("java.protocol.handler.pkgs",
			// "com.sun.net.ssl.internal.www.protocol");
			// java.security.Security.addProvider(new
			// com.sun.net.ssl.internal.ssl.Provider());
			/*if (!ILogIn.emptyString(keyStoreFile))
			{
				prop.put("javax.net.ssl.keyStore", keyStoreFile);
				prop.put("javax.net.ssl.keyStorePassword", ILogIn
						.getTructStorePassword(keyStorePassword));
			}
			if (!ILogIn.emptyString(trustStoreFile))
			{
				prop.put("javax.net.ssl.trustStore", trustStoreFile);
				prop.put("javax.net.ssl.trustStorePassword", trustStorePassword);
			}*/
			// prop.put("sun.net.client.defaultConnectTimeout",""+(timeout*1000));
			// prop.put("sun.net.client.defaultReadTimeout", ""+(timeout*1000));
      /*NSL20070112
			context.Debug("GNHttpConnection [connect]" + "Connecting URL(" + url
										+ ") timeout[" + timeout + " s]");
			myURLobject = new URL(url);
			uc = myURLobject.openConnection();
      */
      GNProxyInitializer.init();
		/*}*/
    
    context.Debug("GNHttpConnection [connect]" + "Connecting URL(" + url + ") timeout[" + timeout +" s]");
    try
    {
      myURLobject = new URL(url);
      List<Proxy> proxyList = findProxy(myURLobject.toURI());
      if (proxyList != null && !proxyList.isEmpty())
      {
        for (Proxy proxy : proxyList)
        {
          try
          {
            uc = myURLobject.openConnection(proxy);
            break;
          }
          catch (Exception ex)
          {
          }
        }
      }
      if (uc == null)
      {
        uc = myURLobject.openConnection();
      }
    }
    catch (MalformedURLException ex)
    {
      context.Error(ILogErrorCodes.SERVICEMGMT_HTTP_CONNECT,
                    "[GNHttpConnection.connect] Malformed URL: "+url, ex);
    }
    catch (URISyntaxException ex)
    {
      context.Error(ILogErrorCodes.SERVICEMGMT_HTTP_CONNECT,
                    "[GNHttpConnection.connect] Invalid URI: "+myURLobject, ex);
    }

		connection = (HttpURLConnection) uc;
		context.Debug("GNHttpConnection [connect] Connection created");
		if (connection instanceof HttpsURLConnection)
		{
			context.Debug("GNHttpConnection [connect] Https Connection");
			HttpsURLConnection https = (HttpsURLConnection) connection;
			//SSLSocketFactory factory = null;
      SSLSocketFactory factory = getSSLSocketFactory(); //NSL20070507
			if (!authenticateServer)
			{
				//factory = new NoAuthSSLSocketFactory(https.getSSLSocketFactory());
        factory = new NoAuthSSLSocketFactory(factory); //NSL20070507
			}

			if (factory != null)
			{
				https.setSSLSocketFactory(factory);
			}

			if (!verifyServerHostname)
			{
				https.setHostnameVerifier(new NoVerificationHostnameVerifier());
			}
		}
		return connection;
	}

  /**
   * Get the SSLSocketFactory for the current keystore and truststore specified in the connection
   * @return The SSLSocketFactory constructed
   * @throws Exception
   */
  protected SSLSocketFactory getSSLSocketFactory() throws Exception
  {
    KeyManager[] km = getKeyManagers(keyStoreFile, keyStorePassword);
    TrustManager[] tm = getTrustManagers(trustStoreFile, trustStorePassword);
    
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(km, tm, null);
    
    return sslContext.getSocketFactory();
  }

  /**
   * Get the KeyManagers for the the specified keystore
   * @param ksFile The keystore to manager
   * @param ksPass The keystore password
   * @return KeyManagers that can manage the keystore
   * @throws Exception
   */
  protected KeyManager[] getKeyManagers(String ksFile, String ksPass) throws Exception
  {
    if (StringUtil.isEmpty(ksFile))
    {
      ksFile = System.getProperty("javax.net.ssl.keyStore");
    }
    if (StringUtil.isEmpty(ksPass))
    {
      ksPass = System.getProperty("javax.net.ssl.keyStorePassword", "changeit");
    }
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(ksFile), ksPass.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, ksPass.toCharArray());
    
    return kmf.getKeyManagers();
  }

  /**
   * Get the TrustManagers for the specified trust store.
   * @param tsFile The trust store file
   * @param tsPass The trust store password
   * @return The TrustManagers that can manager the specified trust store.
   * @throws Exception
   */
  protected TrustManager[] getTrustManagers(String tsFile, String tsPass) throws Exception
  {
    if (StringUtil.isEmpty(tsFile))
    {
      tsFile = System.getProperty("javax.net.ssl.trustStore");
    }
    if (StringUtil.isEmpty(tsPass))
    {
      tsPass = System.getProperty("javax.net.ssl.trustStorePassword", "changeit");
    }
    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(new FileInputStream(tsFile), tsPass.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(ts);
    return tmf.getTrustManagers();
  }

  /**
   * Find the proxy to connect to the URL
   * @param uri The URL to connect to
   * @return List of proxies that can connect to the URL
   */
  private List<Proxy> findProxy(URI uri)
  {
    try 
    { 
      return GNProxyInitializer.selectProxies(uri);
    }
    catch (IllegalArgumentException e)
    {
    }
    return null;
  }

	protected HttpURLConnection connect() throws Exception
	{
		return connect(url);
	}

	private byte[] sendHttpMessage(byte[] body) throws IOException
	{
		context.Debug("GNHttpConnection [sendHttpMessage] starts");
		if (context.getLogheader())
		{
			context.Info(logMessageSetting());
		}
		connection.setDoInput(true);
		if (body != null)
		{
			connection.setDoOutput(true);
			OutputStream os = TimedURLConnection.getOutputStream(connection,
																														timeout * 1000);
			context.Debug("GNHttpConnection [sendHttpMessage] sending message ...");
			os.write(body);
			context.Debug("GNHttpConnection [sendHttpMessage] message sent");
		}
		context
				.Debug("GNHttpConnection [sendHttpMessage] TimedURLConnection.getInputStream timeout["
								+ timeout + " S]");
		InputStream is = TimedURLConnection.getInputStream(connection,
																												timeout * 1000);
		responseCode = connection.getResponseCode();
		context.Debug("GNHttpConnection [sendHttpMessage] responseCode["
									+ responseCode + "]");
		responseMessage = ILogIn.getMessage(is);
		responseheaders = new Hashtable<String,String>();
		int no = 0;
		while (true)
		{
			String headerName = connection.getHeaderFieldKey(no);
			String headerValue = connection.getHeaderField(no);
			if (headerName != null && headerName.length() > 0)
			{
				responseheaders.put(headerName, headerValue);
			}
			else
			{
				if (headerValue == null || headerValue.length() <= 0) break;
			}
			no++;
		}
		if (context.getLogheader())
		{
			GTConfigFile head = new GTConfigFile(responseheaders);
			context.Debug("GNHttpConnection [sendHttpMessage] responseHeader\r\n"
										+ head);
			context.Debug("GNHttpConnection [sendHttpMessage] responseMessage\r\n"
										+ new String(getResponseMessage()));
			context.Info("GNHttpConnection [sendHttpMessage] success for " + url);
		}
		else
			context.Info("GNHttpConnection [sendHttpMessage] success for " + url);
		return responseMessage;
	}

	protected byte[] sendHttpMessage(Hashtable headers, byte[] body) throws IOException
	{
		setVerb(getRequestMethod());
		context.Debug("GNHttpConnection [sendHttpMessage] Method["
									+ getRequestMethod() + "]");
		addRequestHeaders(headers);
		if (context.getLogheader() && headers != null)
		{
			GTConfigFile head = new GTConfigFile(headers);
			context.Debug("GNHttpConnection [sendHttpMessage] requestHeader\r\n"
										+ head);
		}
		return sendHttpMessage(body);
	}

	protected void setVerb(String verb) throws ProtocolException
	{
		connection.setRequestMethod(verb);
	}

	protected void setDoPost() throws ProtocolException
	{
		setRequestMethod(REQUEST_METHOD_POST);
	}

	protected void setDoGet() throws ProtocolException
	{
		setRequestMethod(REQUEST_METHOD_GET);
	}

  public byte[] doGetPOST(Hashtable headers, byte[] content) throws IOException
  {
    try
    {
      connect();
      sendHttpMessage(headers, content);
      return getResponseMessage();
    }
    catch (Exception ex)
    {
      context.Debug("GNHttpConnection[doGetPOST] Connecton fail for "
                    + url + "\r\n" + context.Exception2String(ex));
      throw new IOException("GNHttpConnection cannot connect to" + url);
    }
    
  }
  
  /*NSL20070112 Replaced by above method
	public byte[] doGetPOST(Hashtable headers, byte[] content) throws IOException
	{
		try
		{
			Object cachesetting = null;
			synchronized (context)
			{
				cachesetting = proxysettingList.get(url);
			}
			if (cachesetting != null)
			{
				context
						.Debug("GNHttpConnection[doGetPOST] use cached proxy setting for "
										+ url);
				Object[] proxysetting = (Object[]) cachesetting;
				// 0 boolean success
				// 1 use proxy or not
				// 2 ip
				// 3 port
				if (((Boolean) proxysetting[0]).booleanValue())
				{
					setUsingproxy(((Boolean) proxysetting[1]).booleanValue());
					setConfig_http_proxy_url((String) proxysetting[2]);
					setConfig_http_proxy_port((String) proxysetting[3]);
					context.Debug("GNHttpConnection[doGetPOST] success for " + url);
				}
			}
			else
				setUsingProxy(false);// direct
			connect();
			sendHttpMessage(headers, content);
			synchronized (context)
			{
				proxysettingList.put(url, newProxySetting(true, isUsingProxy(),
																									getConfig_http_proxy_url(),
																									getConfig_http_proxy_port()));
			}
			return getResponseMessage();
		}
		catch (Exception ex)
		{
			if (isUsingProxy())
			{
				context
						.Debug("GNHttpConnection[doGetPOST] Cached Proxy connecton fail for "
										+ url + "\r\n" + context.Exception2String(ex));
			}
			else
				context.Debug("GNHttpConnection[doGetPOST] Direct connecton fail for "
											+ url + "\r\n" + context.Exception2String(ex));
		}

		if (isUsingProxy())// try direct
		{
			context.Debug("GNHttpConnection[doGetPOST] try Direct connection for "
										+ url);
			try
			{
				setUsingProxy(false);// direct
				connect();
				sendHttpMessage(headers, content);
				synchronized (context)
				{
					proxysettingList.put(url,
																newProxySetting(true, isUsingProxy(),
																								getConfig_http_proxy_url(),
																								getConfig_http_proxy_port()));
				}
				return getResponseMessage();
			}
			catch (Exception ex)
			{
				context.Debug("GNHttpConnection[doGetPOST] Direct connecton fail for "
											+ url + "\r\n" + context.Exception2String(ex));
			}
		}
		context.Debug("GNHttpConnection[doGetPOST] try proxy for " + url);
		setUsingProxy(true);
		int pc = prepareProxy();
		if (pc == -1)
			throw new IOException("GNHttpConnection[doGetPOST] prepare proxy error");
		else if (pc == 1)
			throw new IOException("GNHttpConnection[doGetPOST] cannot send");
		for (int i = 0; i < pacProxyList.size(); i++)
		{
			String onepacproxy = (String) pacProxyList.get(i);
			String proxy_IP = onepacproxy.substring(0, onepacproxy.indexOf(":"));
			String proxy_Port = onepacproxy.substring(onepacproxy.indexOf(":") + 1);
			setConfig_http_proxy_url(proxy_IP);
			setConfig_http_proxy_port(proxy_Port);
			context.Debug("GNHttpConnection[doGetPOST] try one proxy from list["
										+ getAuth_username() + ":" + getAuth_password() + "@"
										+ proxy_IP + ":" + proxy_Port + "]");
			try
			{
				connect();
				sendHttpMessage(headers, content);
				synchronized (context)
				{
					proxysettingList.put(url,
																newProxySetting(true, isUsingProxy(),
																								getConfig_http_proxy_url(),
																								getConfig_http_proxy_port()));
				}
				return getResponseMessage();
			}
			catch (Exception ex)
			{
				context
						.Debug("GNHttpConnection[doGetPOST] try proxy connecton fail for "
										+ url + "\r\n" + context.Exception2String(ex));
			}
		}
		throw new IOException("GNHttpConnection cannot connect to" + url);
	}
  */

	public byte[] doGet(Hashtable headers) throws IOException
	{
		setDoGet();
		return doGetPOST(headers, null);
		//return getResponseMessage();
	}

	public byte[] doPost(Hashtable headers, byte[] body) throws IOException
	{
		setDoPost();
		return doGetPOST(headers, body);
		//return getResponseMessage();
	}

	public byte[] doNativeGet(Hashtable headers) throws IOException
	{
		setDoGet();
		sendHttpMessage(headers, null);
		return getResponseMessage();
	}

	public byte[] doNativePost(Hashtable headers, byte[] body) throws IOException
	{
		setDoPost();
		sendHttpMessage(headers, body);
		return getResponseMessage();
	}

	protected void addRequestHeaders(Hashtable headers)
	{
		if (connection == null || headers == null) return;
		Enumeration keys = headers.keys();
		while (keys.hasMoreElements())
		{
			String keyValue = (String) keys.nextElement();
			String headerValue = (String) headers.get(keyValue);
			addRequestHeaders(keyValue, headerValue);
		}
	}

	protected void addRequestHeaders(String name, String value)
	{
		connection.setRequestProperty(name, value);
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public byte[] getResponseMessage()
	{
		return responseMessage;
	}

	public Hashtable getResponseHeaders()
	{
		return responseheaders;
	}

  /*NSL20070112
	public void setUsingProxy(boolean proxy)
	{
		this.usingproxy = proxy;
	}

	public boolean isUsingProxy()
	{
		return usingproxy;
	}
  */
  
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getRequestMethod()
	{
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod)
	{
		this.requestMethod = requestMethod;
	}

  /*NSL20070112
	public static Vector getPacProxyList()
	{
		return pacProxyList;
	}

	public static Hashtable getProxysettingList()
	{
		return proxysettingList;
	}
  */
}
