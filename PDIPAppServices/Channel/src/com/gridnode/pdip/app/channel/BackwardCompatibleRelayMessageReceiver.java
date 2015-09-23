/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: BackwardCompatibleRelayMessageReceiver.java
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
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

import java.util.Map;

/**
 * This Class handles the RelayMessages received by GTAS. This class is specialized,
 * to perform Decryption and Verification uniquely.
 *
 * The DataContent is Decrypted with Signature Certificate, and Verified with
 * Encryption Certificate.
 *
 * The FileContent is Decrypted with Signature Certificate, and Verified with
 * Original Sender's(SENDER_NODE_ID) Master Channel Encryption Certificate.
 *
 */
public class BackwardCompatibleRelayMessageReceiver
  extends BackwardCompatibleMessageReceiver
{
  private static final String CLASS_NAME =
    "BackwardCompatibleRelayMessageReceiver";

  public Message decrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {
    try
    {
      /*todo to set security level 3 in security info so that only
       *data gets decrypted*/
      String eventId =
        (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);

      if (GNCompatibleEventRegistry
        .getInstance()
        .isRelayChannelDecrypt(eventId))
        return super.decrypt(securityInfo, message);
      else
        securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_3);

      message = super.decrypt(securityInfo, message);

      Map header = message.getCommonHeaders();

      ChannelInfo channelInfo =
        ChannelServiceDelegate.getMasterChannelInfoByRefId(
          (String) header.get(ICommonHeaders.SENDER_BE_GNID));
      if (channelInfo != null)
      {
        //todo set with encryption certificate to master channel encryption certificate
        //securityInfo.setSignatureEncryptionCertificateID(
        //  channelInfo.getSecurityProfile().getEncryptionCertificateID());
        securityInfo.setEncryptionCertificateID(
          channelInfo.getSecurityProfile().getEncryptionCertificateID());

      }
      /*todo to set security level 2 in security info so that only
       *payload gets decrypted*/
      securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_2);
      message = super.decrypt(securityInfo, message);
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "decrypt()",
        "Unable to decrypt message");

      throw new SecurityException("Unable to decrypt message", ex);
    }
    return message;
  }
}
