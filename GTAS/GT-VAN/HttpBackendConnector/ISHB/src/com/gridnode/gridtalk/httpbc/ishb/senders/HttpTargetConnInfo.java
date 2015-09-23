/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpTargetConnInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.util.Properties;

/**
 * @author i00107
 * This class contains the properties for http connection
 */
public class HttpTargetConnInfo extends Properties
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -7409116822993348268L;
  public static final String HTTP_URL = "target.http.url";
  public static final String HTTP_USER = "target.http.user";
  public static final String HTTP_PASS = "target.http.pass";
  public static final String HTTP_TARGETID = "target.http.targetid";
  public static final String AUTH_VERIFY_HOST_ENABLED = "auth.verifyhost.enabled";

  public HttpTargetConnInfo(Properties props)
  {
    super(props);
  }
  
  public String getHttpUrl()
  {
    return getProperty(HTTP_URL, null);
  }
  
  public String getUser()
  {
    return getProperty(HTTP_USER, null);
  }
  
  public String getPassword()
  {
    return getProperty(HTTP_PASS, null);
  }
  
  public String getTargetId()
  {
    return getProperty(HTTP_TARGETID, null);
  }
  
  public boolean isVerifyHostName()
  {
    String prop = getProperty(AUTH_VERIFY_HOST_ENABLED, "true");
    return Boolean.parseBoolean(prop); 
  }

}

