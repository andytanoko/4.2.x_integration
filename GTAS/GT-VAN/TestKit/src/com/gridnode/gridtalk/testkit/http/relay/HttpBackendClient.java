/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifHttpClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 4, 2006    i00107             Created
 */

package com.gridnode.gridtalk.testkit.http.relay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author i00107
 *
 */
public class HttpBackendClient
{
  private String httpMtd = "POST";
  
  private Logger logger = Logger.getLogger("HttpBackendClient");
  
  public HttpBackendClient(URL httpUrl, byte[] payload, Properties docProps, String contentType) throws IOException
  {
    HttpURLConnection httpCon = null;
      httpCon = (HttpURLConnection)httpUrl.openConnection();
      
      HttpURLConnection.setFollowRedirects(true);
      httpCon.setRequestMethod(httpMtd);
      httpCon.setDoInput(true);
      httpCon.setDoOutput(true);
      httpCon.setUseCaches(false);
      httpCon.setAllowUserInteraction(true);
      
      //payload = wrapContent(payload, httpCon, contentType);
      Enumeration props = docProps.propertyNames();
      while (props.hasMoreElements())
      {
        String key = (String)props.nextElement();
        String value = docProps.getProperty(key);
        httpCon.setRequestProperty(key, value);
      }
      httpCon.setRequestProperty("mime-version", "1.0");
      httpCon.setRequestProperty("Content-Length", payload.length+"");
      httpCon.setRequestProperty("Content-Type", contentType);
      logger.info("payload length is "+payload.length);
      
      OutputStream os = httpCon.getOutputStream();
      os.write(payload);
      
      int respCode = httpCon.getResponseCode();
      String respMsg = httpCon.getResponseMessage();
      logger.info("Return code="+respCode + ", msg="+respMsg);
      String detailResp = readContent(httpCon);
      logger.info("Detail Response = "+detailResp);
  }
/*
  private byte[] wrapContent(byte[] content, HttpURLConnection conn, String type)
  {
    String boundary = "----" + createNewMsgId();
    String startboundary = "--" + boundary;
    String endboundary = startboundary + "--";
    content = (startboundary + "\r\n" + new String(content) + "\r\n" + endboundary + "\r\n").getBytes();
    String contentType = "multipart/related; type=\""+type+"\"; boundary=\"" + boundary + "\"";
    conn.setRequestProperty("Content-Type", contentType);
    
    return content;

  }*/
  private String createNewMsgId()
  {
    Calendar cal = new GregorianCalendar();
    return "GNRNUID" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH)
            + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
  }

  
  private static String readContent(HttpURLConnection conn) throws IOException
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
}
