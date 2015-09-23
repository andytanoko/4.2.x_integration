/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackwardCompatibleRelayMessageSender.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel;

import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.ChannelServiceDelegate;
import com.gridnode.pdip.app.channel.helpers.GNCompatibleEventRegistry;
import com.gridnode.pdip.app.channel.helpers.SecurityServiceDelegate;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

import java.util.Map;

/**
 * This class performs RelayMessageSend (BackwardCompatible). We choose to extend
 * from  BackwardCompatibleMessageSender as, the RelayMessageSender is dependent on
 * Backward Compatibility.
 *
 * In a ideal case of RelayMessageSend, the send logic may not be dependent on
 * backwardcompatible send.
 *
 * @author Jagadeesh.
 * @version 2.2
 * @since 2.2
 *
 */

public class BackwardCompatibleRelayMessageSender
  extends BackwardCompatibleMessageSender
{

  private static final String CLASS_NAME =
    "BackwardCompatibleRelayMessageSender";

  public BackwardCompatibleRelayMessageSender()
  {
  }

  /**
   * BackwardCompatibleRelayMessageSender overrides this method, inorder to use
   * different certificates to perform Encrypt and Sign.
   *
   * Special Handling of FileContent for Relay Receiver - As (GM - Relay) will not
   * (decrypt and verify) this Content.
   * ---------------------------------------------------------------------------
   *
   * We Encrypt the filecontent with Receipent MasterChannel Encryption Certificate.
   * And Sign with OwnMasterChannel Encryption Certificate.
   *
   * @param secInfo Security Profile used for Security specific settings
   * @param fileContent FileContent in byte[] to Encrypt and Sign.
   * @param header - Header for this Channel Send.
   * @return Encrypted byte[].
   * @throws Exception - throws Exception when cannot perform Encrypt And Sign.
   *
   * @version 2.2
   * @since 2.2
   */

  public Message encrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {
    try
    {
      Message aMessage = null;
      ChannelLogger.debugLog(
        CLASS_NAME,
        "encrypt()",
        "[Begin BackwardCompatibleRealySender Encrypt]");
      Map header = message.getCommonHeaders();
      String eventId = (String) header.get(ICommonHeaders.MSG_EVENT_ID);
      if (GNCompatibleEventRegistry
        .getInstance()
        .isRealyChannelEncrypt(eventId))
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "encrypt()",
          "[Relay Channel Encrypt]");
        securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_1);
        SecurityInfo sinfo = getSecurityInfo(securityInfo, eventId);
        sinfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_1);
        Message eMessage = SecurityServiceDelegate.encrypt(sinfo, message);
        //ChannelLogger.debugLog(CLASS_NAME,"encrypt()","[Encrypted Message]\n"+eMessage.toString());
        return eMessage;
      }

      String recipientMasterChannelRefId =
        (String) header.get(ICommonHeaders.RECIPENT_BE_GNID);
      String senderMasterChannelRefId =
        (String) header.get(ICommonHeaders.SENDER_BE_GNID);

      ChannelInfo channelInfo =
        (ChannelInfo) messageContext.getAttribute(IMessageContext.CHANNEL_INFO);

      //String eventId = (String)header.get(ICommonHeaders.MSG_EVENT_ID);
      if (recipientMasterChannelRefId.equals(channelInfo.getReferenceId()))
      {
        // if receipent is relay itself
        message =
          SecurityServiceDelegate.encrypt(
            getSecurityInfo(securityInfo, eventId),
            message);
      }
      else
      {

        /*@todo to set security level 3 in clone security info */
        securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_3);
        aMessage =
          SecurityServiceDelegate.encrypt(
            getSecurityInfo(securityInfo, eventId),
            message);

        message.setData(aMessage.getData());

        /*@todo to set security level 2 in clone security info */
        SecurityInfo clonedSecInfo =
          getSecurityInfo(
            securityInfo,
            recipientMasterChannelRefId,
            senderMasterChannelRefId);
        clonedSecInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_2);
        aMessage = SecurityServiceDelegate.encrypt(clonedSecInfo, message);
        message.setPayLoad(aMessage.getPayLoadData());
      }

    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "encrypt()",
        "Unable to encrypt message");

      throw new SecurityException("Unable to encrypt message", ex);
    }
    return message;
  }

  private SecurityInfo getSecurityInfo(
    SecurityInfo info,
    String recipientMasterChannelRefId,
    String senderMasterChannelRefId)
    throws Exception
  {
    SecurityInfo cloneInfo = (SecurityInfo) info.clone();
    ChannelInfo channelInfo = null;

    channelInfo =
      ChannelServiceDelegate.getMasterChannelInfoByRefId(
        recipientMasterChannelRefId);
    if (channelInfo != null)
    {
      cloneInfo.setEncryptionCertificateID(
        channelInfo.getSecurityProfile().getEncryptionCertificateID());
    }

    channelInfo =
      ChannelServiceDelegate.getMasterChannelInfoByRefId(
        senderMasterChannelRefId);
    if (channelInfo != null)
    {
      cloneInfo.setSignatureEncryptionCertificateID(
        channelInfo.getSecurityProfile().getEncryptionCertificateID());
    }
    return cloneInfo;
  }
}