/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifOptions.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 16, 2007   i00107           Created
 * Oct 05, 2009   Tam Wei Xiang    #1053: Update to load the RNIF option based
 *                                        on ownBusinessEntityID, TP BE ID
 */
package com.gridnode.gtas.server.rnif.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * @author i00107
 * This class is used to load options files for a process definition. The option files contain properties
 * that can be used to configure whether to output some optional tags in the service header, specifically, the messageStandard
 * and VersionIdentifier at this moment.
 */
public class RnifOptions
{
  private static final String OPTIONS_PATH_KEY = "rnif.path.options";
  
  // options
  private static final String SET_ACTION_IDENTITY_MESSAGE_STANDARD = "set.ActionIdentityMessageStandard";
  private static final String SET_ACTION_IDENTITY_VERSION_IDENTIFIER = "set.ActionIdentityVersionIdentifier";
  private static final String SET_REQUEST_MSG_RECEIVER_ID_DOMAIN = "set.Request.MessageReceiverIDDomain";
  private static final String SET_REQUEST_MSG_SENDER_ID_DOMAIN = "set.Request.MessageSenderIDDomain";
  private static final String SET_RESPONSE_MSG_RECEIVER_ID_DOMAIN = "set.Response.MessageReceiverIDDomain";
  private static final String SET_RESPONSE_MSG_SENDER_ID_DOMAIN = "set.Response.MessageSenderIDDomain";
  
  private static final String SET_REQUEST_SENDER_LOCATION_ID = "set.Request.SenderLocationID";
  private static final String SET_REQUEST_RECEIVER_LOCATION_ID = "set.Request.ReceiverLocationID";
  private static final String SET_RESPONSE_SENDER_LOCATION_ID = "set.Response.SenderLocationID";
  private static final String SET_RESPONSE_RECEIVER_LOCATION_ID = "set.Response.ReceiverLocationID";
  
  private String _pipVersion;
  private Properties _options = new Properties();
  
  public void loadOptions(String rnifKey)
  {
    try
    {
      Logger.log("RNIF key: "+rnifKey);
      File f = FileUtil.getFile(OPTIONS_PATH_KEY, rnifKey+".opts");
      if (f != null)
      {
        _options.load(new FileInputStream(f));
      }
    }
    catch (Exception ex)
    {
      Logger.warn("RNIFOptions given rnifKey="+rnifKey+" failed to be loaded", ex);
    }
  }
  
  /**
   * Construct a RnifOptions for the specified process definition.
   * @param def The process definition
   */
  public RnifOptions(ProcessDef def)
  {
    _pipVersion = def.getVersionIdentifier();
    loadOptions(def.getDefName());
  }
  
  /**
   * Take in own Business Entity ID, Receiver Biz entity ID and Process Def
   * @param def
   * @param ownBizEntityID
   * @param receiverBizEntityID
   */
  public RnifOptions(ProcessDef def, String ownBizEntityID, String receiverBizEntityID)
  {
    _pipVersion = def.getVersionIdentifier();
    loadOptions(ownBizEntityID+"."+receiverBizEntityID+"."+def.getDefName());
  }
  
  /**
   * Check whether any options file is specified for the process definition
   * @return <b>true</b> if some options have been loaded for the process definition, <B>false</b> otherwise.
   */
  public boolean isNoOptions()
  {
    return _options.isEmpty();
  }
  
  /**
   * Get the MessageStandard value set in the options file.
   * @return The value set in 'set.ActionIdentityMessageStandard' property in the options file, if found. <b>null</b> if not set.
   */
  public String getActionIdentityMessageStandard()
  {
    String messageStandard = _options.getProperty(SET_ACTION_IDENTITY_MESSAGE_STANDARD, null);
    if (messageStandard != null && messageStandard.trim().length()==0)
    {
      messageStandard = null;
    }
    return messageStandard;
  }
  
  /**
   * Get the VersionIdentifier value set in the options file.
   * 
   * @return The value set in 'set.ActionIdentityVersionIdentifier' property in the options file, if found. <b>null</b> if not set.
   */
  public String getActionIdentityVersionIdentifier()
  {
    String versionId = _options.getProperty(SET_ACTION_IDENTITY_VERSION_IDENTIFIER, null);
    if (versionId != null)
    {
      if (versionId.trim().length() == 0)
      {
        versionId = null;
      }
      else if (versionId.equalsIgnoreCase("default"))
      {
        versionId = _pipVersion;
      }
    }
    return versionId;
  }
  
  /**
   * Get the value for request receiver domian that set in options file.
   * @return the value set in 'set.Request.MessageReceiverIDDomain' property files or null if not set.
   */
  public String getRequestMsgReceiverIDDomain()
  {
    String receiverDomain = _options.getProperty(SET_REQUEST_MSG_RECEIVER_ID_DOMAIN, null);
    if(receiverDomain != null && receiverDomain.trim().length() == 0)
    {
      receiverDomain = null;
    }
    return receiverDomain;
  }
  
  /**
   * Get the value for requeset receiver domian that set in options file.
   * @return the value set in 'set.Request.MessageSenderIDDomain' property files or null if not set.
   */
  public String getRequestMsgSenderIDDomain()
  {
    String senderDomain = _options.getProperty(SET_REQUEST_MSG_SENDER_ID_DOMAIN, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
 
  /**
   * Get the value for response receiver domian that set in options file.
   * @return the value set in 'set.Response.MessageReceiverIDDomain' property files or null if not set.
   */
  public String getResponseMsgReceiverIDDomain()
  {
    String receiverDomain = _options.getProperty(SET_RESPONSE_MSG_RECEIVER_ID_DOMAIN, null);
    if(receiverDomain != null && receiverDomain.trim().length() == 0)
    {
      receiverDomain = null;
    }
    return receiverDomain;
  }
  
  /**
   * Get the value for response receiver domian that set in options file.
   * @return the value set in 'set.Response.MessageSenderIDDomain' property files or null if not set.
   */
  public String getResponseMsgSenderIDDomain()
  {
    String senderDomain = _options.getProperty(SET_RESPONSE_MSG_SENDER_ID_DOMAIN, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
  
  /**
   * Get the value for request doc sender location id that set in options file.
   * @return the value set in 'set.Request.SenderLocationID' property files or null if not set.
   */
  public String getRequestSenderLocationID()
  {
    String senderDomain = _options.getProperty(SET_REQUEST_SENDER_LOCATION_ID, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
  
  /**
   * Get the value for request doc receiver location id that set in options file.
   * @return the value set in 'set.Request.ReceiverLocationID' property files or null if not set.
   */
  public String getRequestReceiverLocationID()
  {
    String senderDomain = _options.getProperty(SET_REQUEST_RECEIVER_LOCATION_ID, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
  
  /**
   * Get the value for response doc sender location id that set in options file.
   * @return the value set in 'set.Response.SenderLocationID' property files or null if not set.
   */
  public String getResponseSenderLocationID()
  {
    String senderDomain = _options.getProperty(SET_RESPONSE_SENDER_LOCATION_ID, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
  
  /**
   * Get the value for response doc receiver location id that set in options file.
   * @return the value set in 'set.Response.ReceiverLocationID' property files or null if not set.
   */
  public String getResponseReceiverLocationID()
  {
    String senderDomain = _options.getProperty(SET_RESPONSE_RECEIVER_LOCATION_ID, null);
    if(senderDomain != null && senderDomain.trim().length() == 0)
    {
      senderDomain = null;
    }
    return senderDomain;
  }
}
