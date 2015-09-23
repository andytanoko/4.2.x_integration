/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IHttpCommInfo.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Nov 22 2002     Jagadeesh                Created
 * Dec 5  2002     Qingsong                 Modified, add more fields.
 * Oct 11 2004	   Jagadeesh		    Modified: Fixed Defect GNDB00025310
 * 					    Added: Default Ports for Http,Https
 */

package com.gridnode.pdip.base.transport.comminfo;

public interface IHttpCommInfo extends ICommInfo
{

  public static final String HTTP_METHOD_GET = "Get";
  public static final String HTTP_METHOD_POST = "Post";
  public static final String HTTP_VERSION = "1.1";
  public static final String IMPL_VERSION = "000001";



  /**
   * This getter returns the user
   *
   * @return the user ID
   *
   * @since 2.0
   */
  public String getUser();

  /**
   * This getter returns the password
   *
   * @return the password
   *
   * @since 2.0
   */
  public String getPassword();

  /**
   * This getter returns the HTTP Basic Authentication username
   * @return
   */
  public String getHttpAuthUser();
  
  /**
   * This getter returns the HTTP Basic Authentication password
   * @return
   */
  public String getHttpAuthPassword();
  
  /**
   * This getter returns the Request Method
   *
   * @return the Request Method
   *
   * @since 2.0
   */

  public String getRequestMethod();


  /**
   * This getter returns Gateway URL
   *
   * @returns Gateway URL
   *
   * @since 2.0
   */
  public String getGatewayURL();


  /**
   * This getter returns Timeout
   *
   * @returns Timeout
   *
   * @since 2.0
   */
   public int    getTimeout();

//for https
  /**
   * This getter returns if Auth Server
   *
   * @returns if Auth Server
   *
   * @since 2.0
   */
  public boolean isAuthServer();

  /**
   * This getter returns if Auth Client
   *
   * @returns if Auth Client
   *
   * @since 2.0
   */
  public boolean isAuthClient();


  /**
   * This getter returns if Verify Hostname
   *
   * @returns if Verify Hostname
   *
   * @since 2.0
   */

  public boolean isVerifyHostname();

  /**
   * This getter returns keystore filename
   *
   * @returns keystore filename
   *
   * @since 2.0
   */
  public String getKeystoreFile();

  /**
   * This getter returns keystore password
   *
   * @returns keystore password
   *
   * @since 2.0
   */
  public String getKeystorePassword();

  /**
   * This getter returns truststore filename
   *
   * @returns truststore filename
   *
   * @since 2.0
   */
  public String getTruststoreFile();

  /**
   * This getter returns truststore password
   *
   * @returns truststore password
   *
   * @since 2.0
   */
  public String getTruststorePassword();
  
  /**
   * This getter returns the URL without the userinfo <user>:<password>
   * @return
   */
  public String getUrlWithoutUserInfo();
  
  public boolean isConfiguredHttpBasicAuth();

}