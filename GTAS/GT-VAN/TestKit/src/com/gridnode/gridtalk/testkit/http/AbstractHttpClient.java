/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractHttpClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17, 2007   i00107              Created
 */

package com.gridnode.gridtalk.testkit.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * @author i00107
 * This provides helpful methods for reuse in http clients.
 */
public abstract class AbstractHttpClient
{
  protected String _defaultHttpMtd = "POST";
  
  protected abstract Logger getLogger();
  protected abstract String getTargetDesc();
  
  protected void post(URL httpUrl, byte[] payload, Properties headers, String contentType)
  {
    HttpURLConnection httpCon = null;
    try
    {
      httpCon = (HttpURLConnection)httpUrl.openConnection();
      setNoHostVerification(httpCon);
      HttpURLConnection.setFollowRedirects(true);
      httpCon.setRequestMethod(_defaultHttpMtd);
      httpCon.setDoInput(true);
      httpCon.setDoOutput(true);
      httpCon.setUseCaches(false);
      httpCon.setAllowUserInteraction(true);
      
      payload = wrapContent(payload, contentType, httpCon);
      
      Enumeration props = headers.propertyNames();
      while (props.hasMoreElements())
      {
        String key = (String)props.nextElement();
        String value = headers.getProperty(key);
        httpCon.setRequestProperty(key, value);
      }
      httpCon.setRequestProperty("mime-version", "1.0");
      httpCon.setRequestProperty("Content-Length", payload.length+"");
      getLogger().info("payload length is "+payload.length);
      
      OutputStream os = httpCon.getOutputStream();
      os.write(payload);
      
      int respCode = httpCon.getResponseCode();
      String respMsg = httpCon.getResponseMessage();
      getLogger().info("Return code="+respCode + ", msg="+respMsg);
      String detailResp = readContent(httpCon);
      getLogger().info("Detail Response = "+detailResp);
    }
    catch (Exception ex)
    {
      getLogger().log(Level.SEVERE, "Unable to send to "+getTargetDesc(), ex);
    }
  }
  
  protected byte[] wrapContent(byte[] payload, String contentType, HttpURLConnection conn)
  {
    return payload;
  }
  
 
  protected String readContent(HttpURLConnection conn) throws IOException
  {
    int contentLength = conn.getContentLength();
    if (contentLength > 0)
    {
      char[] content = new char[contentLength];
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      reader.read(content);
      return String.valueOf(content);
    }
    else
      return "";
  }  
  protected void setNoHostVerification(HttpURLConnection conn)
  {
    if (conn instanceof HttpsURLConnection)
    {
      HttpsURLConnection httpsConn = (HttpsURLConnection)conn;
      httpsConn.setHostnameVerifier(new NoVerificationHostnameVerifier());
    }
  }
  
  private class NoVerificationHostnameVerifier implements HostnameVerifier
  {
    public boolean verify(String hostName, SSLSession session)
    {
      getLogger().log(Level.FINE, "Bypassing hostname verification for "+hostName);
      return true;
    }
  }
}
