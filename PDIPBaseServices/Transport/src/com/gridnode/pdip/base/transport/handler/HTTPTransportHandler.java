/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HTTPTransportHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 * Jan 12 2007    Neo Sok Lay             Revamp proxy connection method.
 * Mar 01 2007    Tam Wei Xiang           Added HTTP Response code that is
 *                                        needed in TM event 'Document Delivery'
 * Oct 06 2010    Tam Wei Xiang           #1889
 */

package com.gridnode.pdip.base.transport.handler;

import java.io.IOException;
import java.util.Hashtable;

import com.gridnode.pdip.base.transport.comminfo.HttpCommInfo;
import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.helpers.*;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.net.GNProxyInitializer;

public class HTTPTransportHandler implements ITransportHandler
{
  private static String http_sender_url =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_SENDER_URL);

  private static String https_keystore_export_handler =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_KEYSTORE_EXPORT_HANDLER);
  private static String https_truststore_export_handler =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_TRUSTSTORE_EXPORT_HANDLER);

  private static boolean authenticateServer =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getBoolean(ITransportConfig.TRANSPORT_HTTPS_AUTHOR_SERVER);
  private static boolean authenticateClient=  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getBoolean(ITransportConfig.TRANSPORT_HTTPS_AUTHOR_CLIENT);
  private static boolean verifyServerHostname=  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getBoolean(ITransportConfig.TRANSPORT_HTTPS_VERIFY_SERVER);
  private static String keyStoreFile=  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_KEYSTORE_NAME);
  private static String keyStorePassword=  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_KEYSTORE_PASSWORD);
  private static String trustStoreFile=  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_TRUSTSTORE_NAME);
  private static String trustStorePassword =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTPS_TRUSTSTORE_PASSWORD);
  /*NSL20070112
  private static String http_proxy_url =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_URL);
  private static String http_proxy_port =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PORT);
  private static String http_proxy_pac =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PAC);
  private static String http_proxy_username =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_USERNAME);
  private static String http_proxy_password =  ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME).getString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PASSWORD);
  */
  
  static
  {
      //GNHttpConnection.setHttp_proxy(http_proxy_pac, http_proxy_url, http_proxy_port, http_proxy_username, http_proxy_password);
    
    //#1889 TWX 20101006 the following causing the base transport log log to gtas log intead of http receiver log
    //HttpMessageContext.getInstance().setLogType(HttpMessageContext.LOG_GTAS);
    
    //NSL20070112 Init the proxy setting
    GNProxyInitializer.init();
    
    if(https_keystore_export_handler == null || https_keystore_export_handler.length() <= 0)
      https_keystore_export_handler = http_sender_url;
    if(https_truststore_export_handler == null || https_truststore_export_handler.length() <= 0)
      https_truststore_export_handler = http_sender_url;
  }

  private static final String CLASS_NAME = "HTTPTransportHandler";
  public HTTPTransportHandler()
  {
  }

  public void connect(ICommInfo commInfo, String[] header)
  throws        InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
      //throw new GNTransportException("HTTPTransportHandler Http cannot connection", null);
  }

  public void connectAndListen(ICommInfo commInfo, String[] header)
   throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
        connect(commInfo, header);
  }

  public void disconnect(ICommInfo commInfo)
   throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTptPersistenceConnectionException,
                GNTptWrongConfigException,
                GNTransportException
  {
      //throw new InvalidProtocolException("HTTPTransportHandler cannot disconnect");
  }

  static public void sendCMD_SetTrustStore(byte[] trustStorePackage) throws GNTransportException
  {
    sendCMDPackage(https_truststore_export_handler, ITransportConstants.SENDER_CMD_SET_TRUSTSTORE, trustStorePackage);
  }

  static public void sendCMD_SetKeyStore(byte[] keyStorePackage) throws GNTransportException
  {
    sendCMDPackage(https_keystore_export_handler, ITransportConstants.SENDER_CMD_SET_KEYSTORE, keyStorePackage);
  }

  static public void sendCMDPackage(String url, String cmd, byte[] cmdPackage) throws GNTransportException
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "sendCMDPackage", "sendCMDPackage");
      TptLogger.debugLog(CLASS_NAME, "     GateWayURL:", http_sender_url);
      TptLogger.debugLog(CLASS_NAME, "     Timeout:", "" + 3000);
      GNHttpConnection httpconnection = new GNHttpConnection(url ,3000);
      GNTransportHeader theader = new GNTransportHeader();
      theader.setSenderCMD(cmd);
      httpconnection.doPost(theader.getProperties(), cmdPackage);
      TptLogger.debugLog(CLASS_NAME, "sendCMDPackage", "Sent");
    }
    catch (Exception ex)
    {
      TptLogger.debugLog(CLASS_NAME, "sendCMDPackage", "error");
      throw new GNTransportException("HTTPTransportHandler cannot sendCMDPackage", ex);
    }
  }

  public void send(ICommInfo commInfo, Hashtable header, String[] dataToSend, byte[] fileData)
  throws InvalidCommInfoException,
                InvalidProtocolException,
                GNTransportException
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "send", "Send");
      HttpCommInfo httpcomminfo = (HttpCommInfo)commInfo;
      httpcomminfo.setGatewayURL(http_sender_url);
      httpcomminfo.setAuthClient(authenticateClient);
      httpcomminfo.setAuthServer(authenticateServer);
      httpcomminfo.setVerifyHostname(verifyServerHostname);
      httpcomminfo.setKeystoreFile(keyStoreFile);
      httpcomminfo.setKeystorePassword(keyStorePassword);
      httpcomminfo.setTruststoreFile(trustStoreFile);
      httpcomminfo.setTruststorePassword(trustStorePassword);
      TptLogger.debugLog(CLASS_NAME, "     GateWayURL:", httpcomminfo.getGatewayURL());
      TptLogger.debugLog(CLASS_NAME, "     Timeout:", ""+httpcomminfo.getTimeout());
      GNHttpConnection httpconnection = new GNHttpConnection(httpcomminfo.getGatewayURL(),httpcomminfo.getTimeout());
      GNTransportHeader theader = new GNTransportHeader(header);

      if(!header.containsKey(IAS2Headers.AS2_VERSION) && !theader.isGTASMessage() && !theader.isRNMessage())
      {
        if(theader.isNativeRNMessage())
          theader.setRNMessage();
        else
          theader.setGTASMessage();
      }
      GNTransportPayload payload = new GNTransportPayload(theader.getProperties(), dataToSend, fileData);
      GNTransportInfo stuff = new GNTransportInfo(commInfo, payload);
      byte[] contend = stuff.save();
      TptLogger.debugLog(CLASS_NAME, "send", "Sending...." + contend.length);
      theader.removeAllProperty();
      theader.setSenderPayloadMessage();
      httpconnection.doPost(theader.getProperties(), contend);
      int responseCode = httpconnection.getResponseCode();
      if (responseCode == HTTPServletTransportHandler.EXCEPTION)
      {
        String exceptionMsg = new String(httpconnection.getResponseMessage());
        throw new IOException(exceptionMsg);
      }
      
      header.put(HTTP_RESPONSE_CODE, responseCode); //TWX 01032007 Added in the HTTP response code
      TptLogger.debugLog(CLASS_NAME, "send", "Sent");
    }
    catch (Exception ex)
    {
      TptLogger.debugLog(CLASS_NAME, "send", "error");
      throw new GNTransportException("HTTPTransportHandler cannot send", ex);
    }
  }

}