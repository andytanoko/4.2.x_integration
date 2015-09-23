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

package com.gridnode.gridtalk.testkit.http.rnif;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;

import com.gridnode.gridtalk.testkit.http.AbstractHttpClient;

/**
 * @author i00107
 * HTTP client to send message to RN receiver
 */
public class RnifHttpClient extends AbstractHttpClient
{
  
  private Logger logger = Logger.getLogger("RnifServlet");

  /**
   * @see com.gridnode.gridtalk.testkit.http.AbstractHttpClient#getLogger()
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }

  /**
   * @see com.gridnode.gridtalk.testkit.http.AbstractHttpClient#getTargetDesc()
   */
  @Override
  protected String getTargetDesc()
  {
    return "RN Gateway";
  }

  /**
   * @param httpUrl URL for RN receiver
   * @param payload Payload to send
   * @param type Content type to use for payload
   * @param rnifVersion Version of RNIF
   * @param respType Response type: sync or async
   */
  public RnifHttpClient(URL httpUrl, byte[] payload, String type, String rnifVersion, String respType)
  {
    Properties props = new Properties();
    props.setProperty("x-RN-Version", rnifVersion);
    props.setProperty("x-RN-Response-Type", respType);
    post(httpUrl, payload, props, type);
  }

  protected byte[] wrapContent(byte[] content, String type, HttpURLConnection conn)
  {
    String boundary = "----" + createNewMsgId();
    String startboundary = "--" + boundary;
    String endboundary = startboundary + "--";
    content = (startboundary + "\r\n" + new String(content) + "\r\n" + endboundary + "\r\n").getBytes();
    String contentType = "multipart/related; type=\"" + type + "\"; boundary=\"" + boundary + "\"";
    conn.setRequestProperty("Content-Type", contentType);
    
    return content;

  }
  private String createNewMsgId()
  {
    Calendar cal = new GregorianCalendar();
    return "GNRNUID" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH)
            + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
  }

  
}
