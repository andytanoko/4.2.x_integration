/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapCommInfo.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Sep 25 2003     Jagadeesh               Created
 * Oct 17 2003      Neo Sok Lay             Removed Get/SetTptImplVersion
 * Mar 21 2006      Neo Sok Lay             GNDB00026766: Make port optional in url.
 *                                          Use java.net.URL to parse and validate the url.
 */

package com.gridnode.pdip.base.transport.comminfo;

import java.net.MalformedURLException;
import java.net.URL;

public class SoapCommInfo extends AbstractCommInfo implements ISoapCommInfo
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6892441619638809752L;
	private String _tptImplVersion = TPT_IMPL_VERSION;

  public SoapCommInfo()
  {
  }

  public SoapCommInfo(String url)
  {
    super(url);
  }

  public String getProtocolVersion()
  {
    return PROTOCOL_IMPL_VERSION;
  }

  /*031017NSL
  public void setTptImplVersion(String tptImplVersion)
  {
    _tptImplVersion = tptImplVersion;
  }

  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }
  */
  
  public String getProtocolType()
  {
    return ISoapCommInfo.SOAP;
  }

  /**
	 * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseURL(java.lang.String)
	 */
	@Override
	public void parseURL(String url1) throws MalformedURLException
	{
		//NSL20060321 Use URL to validate
		URL url = new URL(url1);
		String protocol = url.getProtocol();
		if (!protocol.equals("http")) //only accept http
		{
			throw new MalformedURLException("Incorrect protocol: "+protocol);
		}
	}

	
	/**
	 * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseAndSetURL(java.lang.String)
	 */
	@Override
	protected void parseAndSetURL(String url1)
	{
  	try
  	{
  		URL parsedURL = new URL(url1);
  		
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
  	}
  	catch (Exception ex)
  	{
  		ex.printStackTrace();
  	}
	}

	public static void main(String[] args)
  {
    SoapCommInfo commInfo =
      new SoapCommInfo("http://192.168.213.167:8080/gtas/webservices/SoapB2BWebServices");
    System.out.println("URL-->" + commInfo.getURL());
  }

}