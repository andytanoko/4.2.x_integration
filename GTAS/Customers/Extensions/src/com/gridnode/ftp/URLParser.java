/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: URLParser.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 12 2003    Jagadeesh               Created
 */

package com.gridnode.ftp;

import java.util.StringTokenizer;

/**
 * This class is a Utility Class to parse a given url, and construct back
 * each element of the URL.
 *
 * A typical URL Format is of the following form:
 *
 *  protocol://<<username>>:<<password>>@host:<<port>>/<<URI>> .(RFC Standard).
 *
 * URL Passed to this class is expected to follow the above standard.
 *
 * The URI Parsing is not mandatory, hence it is not considered for parsing
 * and setting the value. In later versions this will be supported.
 *
 */

public class URLParser
{

  String _protocol    = null;
  String _host        = null;
  int    _port        = -1;
  String _user        = null;
  String _password    = null;
  String _requestURI  = null;
  boolean debug       = true;

  /**
   * URLParser Constructor to parse and set individual values of a
   * URL.
   * @param url URL to parse
   * @throws Exception throws Exception, if the given URL cannot be parsed.
   */

  public URLParser(String url)
    throws Exception
  {
    parseAndSetURL(url);
  }

  /**
   * This method is a "Command Pattern Method", to delegate to perform the
   * desired operation.
   * @param url Givne URL
   * @throws Exception throws Exception, if the given URL cannot be parsed.
   */


  private void parseAndSetURL(String url)
    throws Exception
  {
    try
    {
      String protocol = getURLProtocol(url);
      String userInfo = getURLUserInfo(url);
      String hostPort = getURLHost(url);
      setProtocol(protocol);        //Sets protocol.
      parseAndSetUser(userInfo);    //Parse userInfo in user:password format.
      parseAndSetHost(hostPort);    //Parse hostPort in host:port/[URI] format.
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new Exception("[URLParser][parseAndSetURL()] Could Not Parse URL"+
          "Please Check URL Format port:[username@password]host:port/[URI]");
    }
  }


  /*****************   Getter Methods for the Given URL ************/

  public String getProtocol()
  {
    return _protocol;
  }

  public String getHost()
  {
    return _host;
  }

  public int getPort()
  {
    return _port;
  }

  public String getUser()
  {
    return _user;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getRequestURI()
  {
    return _requestURI;
  }

  /**
   * Parse and Get the Portocol from the URL. (EX: FTP/HTTP/JMS/...)
   * @param url URL to parse
   * @return Protocol.
   */

  private String getURLProtocol(String url)
  {
    return url.substring(0,url.indexOf("/")-1);
  }

  /**
   * Parse and Get UserInfo in <<username>:<<password>> format.
   * (EX: administrator:password)
   * @param url URL to Parse.
   * @return UserInfo, in the above specified format.
   */

  private String getURLUserInfo(String url)
  {
    int index = url.indexOf("@");
    if( index>0 )
    {
      return url.substring(url.indexOf("/")+2,index);
    }
    return null;
  }

  /**
   * Parse and Get URLHost for the given URL (host:<<port>>)
   * @param url URL to Parse
   * @return URLHost.
   */

  private String getURLHost(String url)
  {
    String purl=null;
    int i =  url.indexOf("@");
    if (i>0)
    {
      String passurl = url.substring(i+1);
      int ppos = passurl.indexOf("/");
      if (ppos>0)
        purl = passurl.substring(0,ppos);
      else
        purl = passurl;
    }
    else
    {
      purl = url.substring(url.indexOf("/")+2);
    }
    return purl;
  }

  private void setProtocol(String protocol)
  {
    _protocol = protocol;
  }

  public void parseAndSetUser(String userInfo)
  {
    //username:[password]
    if (userInfo!= null)
    {
      StringTokenizer stk = new StringTokenizer(userInfo,":");
      _user = stk.nextToken();
      if(stk.hasMoreTokens())
        _password = stk.nextToken();
    }
  }

  public void parseAndSetHost(String hostPort)
  {
    //host:[port/URI]
    String hosturl = null;
    int uriIndex = hostPort.indexOf("/");
    if (uriIndex > 0)
    {
      hosturl = hostPort.substring(0,uriIndex);
    }
    else
      hosturl = hostPort;
    StringTokenizer stk = new StringTokenizer(hosturl,":");
    _host = stk.nextToken();
    if(stk.hasMoreTokens())
      _port = Integer.parseInt(stk.nextToken());
  }

  public static void log(String logMessage)
  {
    System.out.println(logMessage);
  }

  public String toString()
  {
    String url = getProtocol()+"://"+getUser()+":"+getPassword()+"@"+
        getHost()+":"+getPort();
    log(url);
    return url;
  }

  /**
   *
   */
  public static void main(String[] args)
  {
    try
    {
      URLParser parser = new URLParser("http://localhost");
      log("Protocol---->"+parser.getProtocol());
      log("Host---->"+parser.getHost());
      log("Port---->"+parser.getPort());
      log("userName--->"+parser.getUser());
      log("password--->"+parser.getPassword());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

}