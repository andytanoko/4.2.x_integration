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

package com.gridnode.gridtalk.testkit.http.backend;

import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import com.gridnode.gridtalk.testkit.http.AbstractHttpClient;


/**
 * @author i00107
 * HTTP client to send to HTTP backend connector
 */
public class HttpBackendClient extends AbstractHttpClient
{
  private Logger logger = Logger.getLogger("HttpBackendClient");

  /**
   * Sends payload to target URL
   * @param httpUrl target URL
   * @param payload Payload to send
   * @param docProps Properties for HTTP headers
   * @param contentType Content type to use for request.
   */
  public HttpBackendClient(URL httpUrl, byte[] payload, Properties docProps, String contentType)
  {
    docProps.setProperty("Content-Type", contentType);
    post(httpUrl, payload, docProps, contentType);
  }
  
  
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
    return "Backend Connector";
  }


}
