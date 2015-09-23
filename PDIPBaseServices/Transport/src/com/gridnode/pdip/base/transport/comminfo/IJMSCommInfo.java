/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSCommInfo.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * June 12 2002     Guo Jianyu              Created
 * Jun 21 2002      Kan Mun                 Modified - added in constant for versions.
 * Oct 21 2003      Neo Sok Lay             Moved getHost() and getPort()
 *                                          from ICommInfo.
 */
package com.gridnode.pdip.base.transport.comminfo;

/**
 * Interface for JMS communication information
 *
 * @author Guo Jianyu
 *
 * @version GT 2.3 I1
 * @since 1.0
 */
public interface IJMSCommInfo extends ICommInfo
{
  public static final int TOPIC = 1;
  public static final int QUEUE = 2;

  // Version of the JMS supported.
  public static final String JMS_VERSION = "1.0.2";
  // Version of the implementation.
  public static final String IMPL_VERSION = "000001";

  /**
   * This getter method returns the Hostname or IP address.
   *
   * @return  the DNS-name of the host or its IP address
   *
   * @since 1.0
   */
  public String getHost();

  /**
   * This getter method returns the port number
   *
   * @return  the port number
   *
   * @since 1.0
  */
  public int getPort();

  /**
   * This getter returns the user
   *
   * @return the user ID
   *
   * @since 1.0
   */
  public String getUser();

  /**
   * This getter returns the password
   *
   * @return the password
   *
   * @since 1.0
   */
  public String getPassword();

  /**
   * This getter returns the destination(topic or queue) name.
   *
   * @return the topic/queue name
   *
   * @since 1.0
   */
  public String getDestination();

  /**
   * This getter returns the destination type, i.e. topic or queue.
   * The return value can be either TOPIC or QUEUE, as defined in this interface
   *
   * @return  the destination type
   *
   * @since 1.0
   */
  public int getDestType();

  /**
   * This getter returns the version of the protocol used
   *
   * @return  the protocol version
   *
   * @since 1.0
   */

  public String getProtocolVersion();


  /**
   * To return this CommInfo as URL.
   *
   * @return  the URL
   *
   * @since 1.0
   */

  public String toURL();

}