/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICommInfo.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * June 11 2002     Guo Jianyu              Created
 * June 21 2002     Kan Mun                 Modified - added constant value for protocol type.
 * Nov  22 2002     Jagadeesh               Added - URL getter method.
 * Oct 17 2003      Neo Sok Lay             Added additional methods 
 *                                          to aid generic access from
 *                                          Channel.
 * Oct 21 2003      Neo Sok Lay             Removed getHost() and getPort()
 *                                          to specific comminfo interface.
 *                                          ICommInfo should retain the bare
 *                                          minimum.
 */

package com.gridnode.pdip.base.transport.comminfo;

import java.net.MalformedURLException;

/**
 * Interface that contains communication information.
 * This interface should be extended by interfaces that represent various
 * types of connections, such as JMS, HTTP, etc. All of these interfaces
 * should provide getter methods for all of their connection-specific parameters
 * such as IP, port, protocol, etc.
 *
 * @author Guo Jianyu
 *
 * @version GT 2.3 I1
 * @since 1.0
 */
public interface ICommInfo extends java.io.Serializable
{

  /**
   * This getter method returns the Hostname or IP address.
   *
   * @return  the DNS-name of the host or its IP address
   *
   * @since 1.0
   */
//  public String getHost();

  /**
   * This getter method returns the port number
   *
   * @return  the port number
   *
   * @since 1.0
  */
//  public int getPort();

  /**
   * This getter method returns the protocol type. e.g. JMS, HTTP
   *
   * @return  the protocol
   *
   * @since 1.0
   */
  public String getProtocolType();

  /**
   * This getter returns the version of the CommInfo
   *
   * @return  the version of the CommInfo
   *
   * @since 1.0
   */
  public String getTptImplVersion();

  /**
   * This getter returns the version of the protocol used
   *
   * @return  the protocol version
   *
   * @since 1.0
   */
  //  public String getProtocolVersion();

  /**
   * This getter returns the URL used.
   *
   * @return  the URL
   *
   * @since 1.0
   */

  public String getURL();

  /**
   * Set the Transport implementation version
   * 
   * @param tptImplVersion The implementation version to set.
   */
  public void setTptImplVersion(String tptImplVersion);

  /**
   * Parse the specified url for use for this transport protocol.
   * 
   * @param url The URL to be parse.
   * @throws MalformedURLException Invalid URL.
   */
  public void parseURL(String url) throws MalformedURLException;

  /**
   * Sets the specified URL for use for this transport protocol.
   * The URL will be validated before setting.
   * 
   * @param url The URL to be set.
   * @throws MalformedURLException Invalid URL.
   */
  public void setURL(String url) throws MalformedURLException;

  /**
   * Get the URL format for the concrete protocol implementation 
   * @return The URL pattern.
   */
  public String getURLPattern();

  //------------------------------------------------------------------------------------
  // Static value for protocol type
  //------------------------------------------------------------------------------------
  public static final String DEFAULT_SOAP_PROTOCOL = "SOAP-HTTP";

  public static final String JMS = "JMS";
  public static final String HTTP = "HTTP";
  public static final String SOAP = DEFAULT_SOAP_PROTOCOL;

  public static final String URL_FORMAT =
    "protocol://[username:password@]host:port/URI";

}