/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpCommInfo.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Nov 22 2002     Jagadeesh                Created
 * Dec 10 2002     Qingsong                 Add some fields
 * Oct 17 2003     Neo Sok Lay              Removed Get/Set TptImplVersion.
 *                                          Override parseAndSetURL() for Http specific
 *                                          parsing.
 * Dec 04 2003     Neo Sok Lay              Remove constructor HttpCommInfo(URL).
 *                                          Replace by setURL(URL).
 * Oct 11 2004	   Jagadeesh		    Fixed Defect:GNDB00025310,to allow URL
 * 					    without port and uri.
 * Mar 21 2006     Neo Sok Lay              GNDB00026766: fix the same above.
 *                                          the fix by JD is not working (code lost during CC delivery).
 * May 18 2010     Tam Wei Xiang            #1470 : Support HTTP Basic Authentication
 */

package com.gridnode.pdip.base.transport.comminfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


public class HttpCommInfo extends AbstractCommInfo
             implements IHttpCommInfo
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1289462943384706063L;
  private static final String BASIC_AUTH_USER = "username=";
  private static final String BASIC_AUTH_PW = "password=";
  private static final String BASIC_AUTH_HTTP_PROTOCOL = "http://["+BASIC_AUTH_USER;
  private static final String BASIC_AUTH_HTTPS_PROTOCOL = "https://["+BASIC_AUTH_USER;
  
  
	private String _requestMethod  = IHttpCommInfo.HTTP_METHOD_POST;
  //private String _tptImplVersion = null;
  private String _uri            = null;
  

  protected String _gateWayURL = "http://localhost:8081/HttpReceiver/sender";
  protected int    _timeout = 3000;

  protected boolean _authServer = false;
  protected boolean _authClient = false;
  protected boolean _verifyHostname = false;
  protected String _keystoreFile = null;
  protected String _keystorePassword = "changeit";
  protected String _truststoreFile = null;
  protected String _truststorePassword = "changeit";
  
  protected String _urlWithoutUserInfo = "";
  protected String _httpAuthUser;
  protected String _httpAuthPassword;
  protected boolean _isHttpBasicAuth;

  public HttpCommInfo()
  {
  }
  
  /*031204NSL
  public HttpCommInfo(String url)
  {
    super(url);
    parseAndSetURI(url);
  }
  */
  /*031017NSL
  public void setTptImplVersion(String tptimplversion)
  {
    _tptImplVersion = tptimplversion;
  }
  */
  
  /**
   * This getter method returns the protocol type. e.g. JMS, HTTP
   *
   * @return  the protocol
   *
   * @since 1.0
   */
  public String getProtocolType()
  {
    return IHttpCommInfo.HTTP;
  }

  /**
   * This getter returns the version of the CommInfo
   *
   * @return  the version of the CommInfo
   *
   * @since 2.0
   */
  /*031017NSL
  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }
  */
  
  /**
   * This getter returns the version of the protocol used
   *
   * @return  the protocol version
   *
   * @since 2.0
   */
  public String getProtocolVersion()
  {
    return IHttpCommInfo.HTTP_VERSION;
  }

  /**
   * This getter returns URL used.
   *
   * @return  the protocol version
   *
   * @since 2.0
   */
  /*031017NSL
  public String getURL()
  {
    return _url;
  }
  */
  
  public void setRequestMethod(String requestMethod)
  {
    _requestMethod = requestMethod;
  }

  public String getRequestMethod()
  {
    return _requestMethod;
  }

  public boolean isAuthServer()
  {
    return _authServer;
  }

  public boolean isAuthClient()
  {
    return _authClient;
  }

  public boolean isVerifyHostname()
  {
    return _verifyHostname;
  }

  public String getKeystoreFile()
  {
    return _keystoreFile;
  }

  public String getKeystorePassword()
  {
    return _keystorePassword;
  }

  public String getTruststoreFile()
  {
    return _truststoreFile;
  }

  public String getTruststorePassword()
  {
    return _truststorePassword;
  }

  public String getGatewayURL()
  {
    return _gateWayURL;
  }

  public int    getTimeout()
  {
    return _timeout;
  }

  public void setAuthClient(boolean authClient)
  {
    _authClient = authClient;
  }
  public void setAuthServer(boolean authServer)
  {
    _authServer = authServer;
  }
  public void setGatewayURL(String gatewayURL)
  {
    _gateWayURL = gatewayURL;
  }
  public void setKeystoreFile(String keystoreFile)
  {
    _keystoreFile = keystoreFile;
  }
  public void setKeystorePassword(String keystorePassword)
  {
    _keystorePassword = keystorePassword;
  }
  public void setTimeout(int timeout)
  {
    _timeout = timeout;
  }
  public void setTruststorePassword(String truststorePassword)
  {
    _truststorePassword = truststorePassword;
  }
  public void setTruststoreFile(String truststoreFile)
  {
    _truststoreFile = truststoreFile;
  }
  public void setVerifyHostname(boolean verifyHostname)
  {
    _verifyHostname = verifyHostname;
  }

  public String getURI()
  {
    return _uri;
  }

  /*NSL20060321
  private void parseAndSetURI(String url)
  {
    _uri = url.substring(url.lastIndexOf(":")+5);




  }*/

  public String toString()
  {
     String str = "[{ User="+getUser()+","+"Password="+getPassword()+","+
     "Destination="+getURL()+","+"}]";
     return str;
  }

  /*031021NSL
  public static void main(String args[])
  {
    HttpCommInfo info = new HttpCommInfo("http://www.localhost.com:8080/musthave/whatisthis.html");



    info.setTptImplVersion("abc");

    System.out.println("Protocol :"+info.getProtocol());
    System.out.println("Host :"+info.getHost());
    System.out.println("Port :"+info.getPort());
    System.out.println("Destination  :"+info.getURL());
    System.out.println("URI  :"+info.getURI());
    System.out.println("Protocol Version  :"+info.getProtocolVersion());
    System.out.println("Type :"+info.getProtocolType());
  }
  */
  /**
   * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseAndSetURL(java.lang.String)
   */
  protected void parseAndSetURL(String url1)
  {
    //super.parseAndSetURL(url1);
    //parseAndSetURI(url1);
    setUrlWithoutUserInfo(url1);
    
  	try
  	{
  		URL parsedURL = new URL(_urlWithoutUserInfo);
  		
      setProtocol(parsedURL.getProtocol());
      setHost(parsedURL.getHost());
      setPort(parsedURL.getPort());
      String userInfo = parsedURL.getUserInfo();
      if (userInfo != null)
      {
      	String userName = "";
      	String password = "";
      	int pos = userInfo.indexOf(':');
      	if (pos>-1)
      	{
          userName = userInfo.substring(0, pos);
          password = userInfo.substring(pos+1);
      	}
      	else
      	{
      		userName = userInfo;
      	}
      	setUserName(userName);
      	setPassword(password);
      }
      _uri = parsedURL.getPath();
      
      
  	}
  	catch (Exception ex)
  	{
  		ex.printStackTrace();
  	}
  }

  private boolean isConfiguredHttpBasicAuth(String url)
  {
    return isHttpAuth(url) || isHttpsAuth(url);
  }
  
  private boolean isHttpAuth(String url)
  {
    return url.indexOf(BASIC_AUTH_HTTP_PROTOCOL) >= 0;
  }
  
  private boolean isHttpsAuth(String url)
  {
    return url.indexOf(BASIC_AUTH_HTTPS_PROTOCOL) >= 0;
  }
  
  private String getAuthInfo(String url)
  {
    String firstBoundary = "[";
    String lastBoundary = "]";
    
    int firstBoundaryIndex = url.indexOf(firstBoundary);
    int lastBoundaryIndex = url.indexOf(lastBoundary);
    
    return url.substring(firstBoundaryIndex+1, lastBoundaryIndex);
  }
  
  /**
   * 
   * @param authInfo with pattern  username=proxyuser:password=partner1
   */
  private void setAuthInfo(String authInfo)
  {
    int usrIndex = authInfo.indexOf(BASIC_AUTH_USER);
    int pwIndex = authInfo.indexOf(BASIC_AUTH_PW);
    
    //username=proxyuser:password=partner1
    String username = authInfo.substring(usrIndex + BASIC_AUTH_USER.length(), pwIndex-1); 
    String password = authInfo.substring(pwIndex+BASIC_AUTH_PW.length(), authInfo.length());
    
    setHttpAuthUser(username);
    setHttpAuthPassword(password);
  }
  
  /**
   * TWX 20100518 #1470 - the given url will contain the HTTP basic auth user and auth password
   * @param url
   */
  private void setUrlWithoutUserInfo(String url)
  {
    
    
    //  TWX obtain the URL without the userinfo
    //   http://[username=admin:password=admin]localhost:8080/gridtalk/
    if(! isConfiguredHttpBasicAuth(url))
    {
      _urlWithoutUserInfo = url;
    }
    else
    {
      setConfiguredHttpBasicAuth(true);
      
      //initialize the http basic auth user and password
      String authInfo = getAuthInfo(url);
      setAuthInfo(authInfo);
      _urlWithoutUserInfo = parseHttpBasicAuth(url);
    }
    
  }
  
  //return the URL without the HTTP Basic Auth info
  private String parseHttpBasicAuth(String url)
  {
    if(! isConfiguredHttpBasicAuth(url))
    {
      return url;
    }
    else
    {
      String authInfo = getAuthInfo(url);
      String authInfoWithBoundary = "["+authInfo+"]";
      int authInfoFirstIndex = url.indexOf(authInfoWithBoundary);
      
      String urlProtocol = url.substring(0, authInfoFirstIndex);
      String urlDomainAndPath = url.substring(authInfoFirstIndex+authInfoWithBoundary.length(), url.length());
      
      return urlProtocol + urlDomainAndPath;
    }
  }
  
	/**
	 * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseURL(java.lang.String)
	 */
	@Override
	public void parseURL(String url1) throws MalformedURLException
	{
    //TWX20100518 check is there any basic auth info embedded in the url, if yes, we will remove it
    url1 = parseHttpBasicAuth(url1);
    
		//NSL20060321 use URL to validate
		URL url = new URL(url1);
		String protocol = url.getProtocol();
		if (!protocol.equals("http") && !protocol.equals("https"))
		{
			throw new MalformedURLException("Incorrect protocol: "+protocol);
		}
	}
  
  public String getUrlWithoutUserInfo()
  {
    return _urlWithoutUserInfo;
  }
  
  private void setHttpAuthPassword(String password)
  {
    _httpAuthPassword = password;
  }
  
  public String getHttpAuthPassword()
  {
    
    return _httpAuthPassword;
  }

  private void setHttpAuthUser(String user)
  {
    _httpAuthUser = user;
  }
  
  public String getHttpAuthUser()
  {
    
    return _httpAuthUser;
  }

  public boolean isConfiguredHttpBasicAuth()
  {
    return _isHttpBasicAuth;
  }
  
  private void setConfiguredHttpBasicAuth(boolean isConfiguredHttpBasicAuth)
  {
    _isHttpBasicAuth = isConfiguredHttpBasicAuth;
  }
  
  public static void main(String[] args) throws MalformedURLException
  {
    String url = "https://[username=proxyuser:password=partner1]localhost:8080/gridtalk/";
    HttpCommInfo commInfo = new HttpCommInfo();
    commInfo.setURL(url);
    
    System.out.println(commInfo.getURI()+" usernmae="+commInfo.getHttpAuthUser()+" pw="+commInfo.getHttpAuthPassword());
    System.out.println("Url="+commInfo.getURL());
    System.out.println("Url without user info="+commInfo.getUrlWithoutUserInfo());
    System.out.println("is http basic auth="+commInfo.isConfiguredHttpBasicAuth());
    
//      String s = "http://username=testgridtalk";
//      StringTokenizer st = new StringTokenizer(s, "http");
//      String s1 = "";
//      while(st.hasMoreTokens())
//      {
//        s1 = s1 + st.nextToken();
//      }
//      
//      System.out.println("S1= "+s1);
  }





  
}




