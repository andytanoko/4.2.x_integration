/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProxyAuthConnInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 15 2007    i00107              Remove proxy related properties. No longer
 *                                    need explicity reference.
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.util.Properties;

/**
 * @author i00107
 * This class contains the properties for proxy connection as well as SSL connection.
 * The property keys required for proxy connection is defined in 
 * {@link com.gridnode.util.net.GNProxySelector}.
 */
public class ProxyAuthConnInfo extends Properties
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 3495937853211577418L;
  
  public static final String KEYSTORE_LOC = "cert.keystore.loc";
  public static final String KEYSTORE_PASS = "cert.keystore.pass";
  public static final String TRUSTSTORE_LOC = "cert.truststore.loc";
  public static final String TRUSTSTORE_PASS = "cert.truststore.pass";
  
  public static final String AUTH_SERVER_ENABLED = "auth.server.enabled";
  public static final String AUTH_CLIENT_ENABLED = "auth.client.enabled";
  public static final String AUTH_VERIFY_HOST_ENABLED = "auth.verifyhost.enabled";
  
  public ProxyAuthConnInfo(Properties props)
  {
    super(props);
  }
  
  public String getTrustStore()
  {
    return getProperty(TRUSTSTORE_LOC, null);
  }
  
  public String getTrustStorePass()
  {
    return getProperty(TRUSTSTORE_PASS, "changeit");
  }
  
  public boolean isServerAuthEnabled()
  {
    String prop = getProperty(AUTH_SERVER_ENABLED, "false");
    return Boolean.parseBoolean(prop);
  }
  
  public boolean isVerifyHostName()
  {
    String prop = getProperty(AUTH_VERIFY_HOST_ENABLED, "false");
    return Boolean.parseBoolean(prop); 
  }
  
  public String getKeyStore()
  {
    return getProperty(KEYSTORE_LOC, null);
  }
  
  public String getKeyStorePass()
  {
    return getProperty(KEYSTORE_PASS, "changeit");
  }
  
  public boolean isClientAuthEnabled()
  {
    String prop = getProperty(AUTH_CLIENT_ENABLED, "false");
    return Boolean.parseBoolean(prop);
  }
}
