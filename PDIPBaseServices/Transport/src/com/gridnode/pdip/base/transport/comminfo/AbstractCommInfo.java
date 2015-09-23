/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 * File: AbstractCommInfo.java
 *
 * ********************************************************
 * Date            Author                Changes
 * ********************************************************
 * Nov 28, 2002    Jagadeesh            Created
 * Dec 10, 2002    Qingsong             Remove dependency for log
 * Oct 17 2003     Neo Sok Lay          Change parseAndSetURL()
 *                                      to protected.
 *                                      Add various method
 *                                      implementations required
 *                                      by interface.
 * Oct 11 2004	   Jagadeesh		Fixed Defect:GNDB00025310, parseURL to
 * 					be overwritten by subclasses.
 */

package com.gridnode.pdip.base.transport.comminfo;

/**
 * This class provides default implementations for parsing and setting
 * the URL.
 */

//import com.gridnode.pdip.base.transport.helpers.TptLogger;

import java.net.MalformedURLException;
import java.util.StringTokenizer;

public abstract class AbstractCommInfo implements ICommInfo
{
  //private static final String CLASS_NAME = "AbstractCommInfo"; 
  protected static final String DELIMETER = ":";
  protected static final String UNKNOWN_HOST = "UNKNOWNHOST";
  private String _protocol = null;
  private String _host = null;
  private int _port = -1;
  private String _userName = null;
  private String _password = null;
  protected String _url = null;
  protected String _tptImplVersion;

  public AbstractCommInfo()
  {
  }

  public AbstractCommInfo(String url)
  {
    this._url = url;
    parseAndSetURL(url);
  }

  public String getProtocol()
  {
    return _protocol;
  }

  /**
   * This getter method returns the Hostname or IP address.
   *
   * @return  the DNS-name of the host or its IP address
   *
   * @since 1.0
   */
  public String getHost()
  {
    return _host;
  }

  /**
   * This getter method returns the port number
   *
   * @return  the port number
   *
   * @since 1.0
  */
  public int getPort()
  {
    return _port;
  }

  /**
   * This getter method returns the userName
   *
   * @return  the userName
   *
   * @since 1.0
  */
  public String getUser()
  {
    return _userName;
  }

  /**
   * This getter method returns the password
   *
   * @return  the password
   *
   * @since 1.0
  */
  public String getPassword()
  {
    return _password;
  }

  /**
   * This getter method returns the protocol type. e.g. JMS, HTTP
   *
   * @return  the protocol
   *
   * @since 1.0
   */
  public abstract String getProtocolType();

  /**
   * This getter returns the version of the CommInfo
   *
   * @return  the version of the CommInfo
   *
   * @since 1.0
   */
  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }

  /**
   * This getter returns the version of the protocol used
   *
   * @return  the protocol version
   *
   * @since 1.0
   */
  public abstract String getProtocolVersion();

  /**
   * This getter returns the URL used.
   *
   * @return  the URL
   *
   * @since 1.0
   */
  public String getURL()
  {
    return _url;
    //return parseAndGetURL();
  }

  /**
   * Parse and set the URL.
   * URL:Format: <<host>>://username:password@host:port
   * @param url
   * jms://user@www.localhost.com:443/destinationtype=Topic?destination=testtopic
   */
  protected void parseAndSetURL(String url1)
  {
    try
    {
      String protocol = null;
      String username = null;
      String password = null;
      String passWordHost = null;
      String host = null;
      int port = -1;

      int i = url1.indexOf("@");
      String purl = url1.substring(url1.lastIndexOf(":"));
      int pos = purl.indexOf("/");
      String url = url1.substring(0, url1.lastIndexOf(":") + pos);




      System.out.println(url);
      StringTokenizer stk = new StringTokenizer(url, ":");

      if (i > 0) //UserName:
      {
        protocol = stk.nextToken();
        String token = stk.nextToken();

        int k = token.indexOf("@");
        if (k > 0)
        {
          username = token.substring(0, k);
          passWordHost = token.substring(k + 1);
        }
        else
        {
          username = token.substring(token.indexOf("/"));
          passWordHost = stk.nextToken();
        }
        port = Integer.parseInt(stk.nextToken());

      }
      else
      {
        protocol = stk.nextToken();
        host = stk.nextToken();
        port = Integer.parseInt(stk.nextToken());

        host = host.substring(2);
      }
      //Getting Password And Host
      if (passWordHost != null)
      {
        int x = passWordHost.indexOf('@');
        password = x < 0 ? "" : passWordHost.substring(0, x);
        host = passWordHost.substring(x + 1);

        //Processing for userName
        int p = username.indexOf('/');
        username = p < 0 ? null : username.substring(p + 2);
      }
      setProtocol(protocol);
      setHost(host);
      setPort(port);
      setUserName(checkNull(username));
      setPassword(checkNull(password));
    }
    catch (Exception ex)
    {
      //TptLogger.infoLog(CLASS_NAME,"parseAndSetURL()",
      //"Please Verify the URL Format"+URL_FORMAT);
      ex.printStackTrace();
    }
  }

  /**
   * Parse the specified URL for validity for the specific protocol.
   * 
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#parseURL(java.lang.String)
   */
  public void parseURL(String url1) throws MalformedURLException
  {
    try
    {
      String protocol = null;
      String username = null;
      String password = null;
      String passWordHost = null;
      String host = null;
      int port = -1;

      int i = url1.indexOf("@");
      String purl = url1.substring(url1.lastIndexOf(":"));
      int pos = purl.indexOf("/");
      String url = url1.substring(0, url1.lastIndexOf(":") + pos);
      System.out.println(url);
      StringTokenizer stk = new StringTokenizer(url, ":");

      if (i > 0) //UserName:
      {
        protocol = stk.nextToken();
        String token = stk.nextToken();

        int k = token.indexOf("@");
        if (k > 0)
        {
          username = token.substring(0, k);
          passWordHost = token.substring(k + 1);
        }
        else
        {
          username = token.substring(token.indexOf("/"));
          passWordHost = stk.nextToken();
        }
        port = Integer.parseInt(stk.nextToken());
      }
      else
      {
        protocol = stk.nextToken();
        host = stk.nextToken();
        port = Integer.parseInt(stk.nextToken());
        host = host.substring(2);
      }
      //Getting Password And Host
      if (passWordHost != null)
      {
        int x = passWordHost.indexOf('@');
        password = x < 0 ? "" : passWordHost.substring(0, x);
        host = passWordHost.substring(x + 1);

        //Processing for userName
        int p = username.indexOf('/');
        username = p < 0 ? null : username.substring(p + 2);
      }
    }
    catch (Exception ex)
    {
      throw new MalformedURLException("Invalid URL Format: " + ex.getMessage());
    }
  }

  protected String parseAndGetURL()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(getProtocol());
    sb.append("//");
    if (getUser() != null && getUser().length()>0) //check for empty
    {
      sb.append(getUser());
      if (getPassword() != null && getPassword().length()>0) //check for empty
      {
      	sb.append(DELIMETER);
      	sb.append((getPassword() == null ? "" : getPassword()));
      }
      sb.append("@");
    }
    sb.append(getHost() == null ? "" : getHost());
    sb.append(DELIMETER);
    sb.append(getPort());
    return sb.toString();
  }

  /**************** Setter Methods ***************************/

  public void setHost(String host)
  {
    _host = host;
  }

  public void setPort(int port)
  {
    _port = port;
  }

  public void setProtocol(String protocol)
  {
    _protocol = protocol;
  }

  public void setUserName(String username)
  {
    _userName = username;
  }

  public void setPassword(String password)
  {
    _password = password;
  }

  public void setTptImplVersion(String tptImplVersion)
  {
    _tptImplVersion = tptImplVersion;
  }

  public String checkNull(String value)
  {
    return (value == null ? "" : value);
  }

  /**
   * This Method checks for Valid URL.
   * @param url - URL in URL_FORMAT(http://usernmae:password@localhost:port/URI)
   * @return
   */
  /*031017NSL 
  public boolean isValidURL(String url)
  {
    url = url.substring(0,url.lastIndexOf(":")+5);
    return false;
  }
  */
  public boolean isValidURL(String url)
  {
    boolean valid = false;
    try
    {
      parseURL(url);
      valid = true;
    }
    catch (Exception e)
    {

    }

    return valid;
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#setURL(java.lang.String)
   */
  public void setURL(String url) throws MalformedURLException
  {
    try
    {
      parseAndSetURL(url);
    }
    catch (Exception e)
    {

      throw new MalformedURLException(e.getMessage());
    }

    this._url = url;
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#getURLPattern()
   */
  public String getURLPattern()
  {
    return URL_FORMAT;





  }

}