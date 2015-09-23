/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUpcCommInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 17 2003    Neo Sok Lay         Created
 * Nov 14 2003    Neo Sok Lay         Add send options: sendAll, skipGdoc, sendUdocOnly.
 */
package com.gridnode.gtas.server.userprocedure.model;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;

/**
 * This interface defines the UPC Communication Profile.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public interface IUpcCommInfo extends ICommInfo
{
  /**
   * UPC Protocol Type
   */
  static final String UPC = "UPC";
  
  /**
   * UPC Protocol Version
   */
  static final String UPC_VERSION = "1.0";

  //future may support more commands
  //static final String URL_PATTERN = 
  // "upc://send=<send_up>&connect=<connect_up>&disconnect=<disconnect_up>&listen=<listen_up>";
  //now only send is supported
  /**
   * Pattern for specifying the URL for UPC
   */
  static final String URL_PATTERN = "upc://send=<user_procedure_name>";

  /**
   * Formatting pattern for UPC URL.
   */
  static final String URL_FORMAT_PATTERN = "upc://{0}";
  
  /**
   * Formatting pattern for the Command portion of the URL
   */
  static final String COMMAND_FORMAT_PATTERN = "{0}={1}"; //command=userprocedure
  
  /**
   * Protocol prefix of the URL
   */
  static final String PROTOCOL = "upc://";
  
  /**
   * Separator between a Command and UserProcedure parameters.
   */
  static final String COMMAND_UP_SEPARATOR = "=";
  
  /**
   * Separator between an UserProcedure (or options) and Command parameters.
   */
  static final String UP_COMMAND_SEPARATOR = "/";
  
  /**
   * Separator between an UserProcedure and options parameters
   */
  static final String UP_OPTION_SEPARATOR = "?";
  
  /**
   * Separator between options parameters
   */
  static final String OPTIONS_SEPARATOR = "&";
  
  /**
   * Supported Command: Send
   */
  static final String COMMAND_SEND = "send";
  
  /**
   * Option for send: to skip sending Gdoc
   */
  static final String OPTION_SKIP_GDOC = "skipGdoc";
  
  /**
   * Option for send: to send Udoc only
   */
  static final String OPTION_UDOC_ONLY = "udocOnly";
  
  /**
   * Option for send: to send all files
   */
  static final String OPTION_SEND_ALL = "sendAll";
  
  /**
   * User Procedure to use for handling the Send command.
   */   
  String getSendUserProcedure();

  /**
   * Whether to skip send GridDoc file payload. 
   * 
   * @return <b>true</b> if the GridDoc file payload will be skipped for sending. This
   * is only applicable if the file payloads are not Zipped. <b>false</b> otherwise.
   */
  boolean isSkipSendGdoc();
  
  /**
   * Whether to send Udoc file payload only.
   * @return <b>true</b> if only the UserDoc file payload will be sent. This is only
   * applicable if the file payloads are not Zipped. <b>false</b> otherwise.
   */
  boolean isSendUdocOnly();
 
  /**
   * Whether to send all file payloads. This option is exclusive of the 
   * <code>skipGdoc</code> and <code>udocOnly<code> options and take precedence to both
   * options.
   * 
   * @return <b>true</b> if all file payloads will be sent, <b>false</b> otherwise.
   */
  boolean isSendAll();
   
}
