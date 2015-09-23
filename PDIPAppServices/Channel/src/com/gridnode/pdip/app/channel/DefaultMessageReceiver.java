/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: DefaultMessageReceiver.java
*
****************************************************************************
* Date           Author                  Changes
****************************************************************************
* July 17 2003    Jagadeesh             Created.
*/
package com.gridnode.pdip.app.channel;

import com.gridnode.pdip.app.channel.exceptions.FlowControlException;
import com.gridnode.pdip.app.channel.exceptions.PackagingException;
import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.helpers.FlowControlEventRegistry;
import com.gridnode.pdip.app.channel.helpers.FlowControlServiceHandler;
import com.gridnode.pdip.app.channel.helpers.PackagingServiceDelegate;
import com.gridnode.pdip.app.channel.helpers.SecurityServiceDelegate;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

public class DefaultMessageReceiver extends AbstractMessageReceiver
{
  //private static final String CLASS_NAME = "DefaultMessageReceiver";

  public DefaultMessageReceiver()
  {
  }

  public Message decrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {
    /* @todo to set security level 1 in security info */
    if (ISecurityInfo
      .ENCRYPTION_TYPE_NONE
      .equals(securityInfo.getEncryptionType())
      && ISecurityInfo.SIGNATURE_TYPE_NONE.equals(
        securityInfo.getSignatureType()))
    {
      return message; //Return Original Message if no decrypt and Verify
    }

    if (securityInfo.getSecuritylevel() == 0)
      securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_1);
    return SecurityServiceDelegate.decrypt(securityInfo, message);
  }

  public Message join(FlowControlInfo flowControlInfo, Message message)
    throws FlowControlException
  {
    /* @todo join should return either a null message if not all message
     * blocks are received or the pass in message if join is not required
     */
    if (IFlowControlInfo.STATUS_FLOWCONTROL_UNSET
      == flowControlInfo.getFlowControlStatus())
    {
      flowControlInfo = getFlowControlInfo(flowControlInfo, message);
    }
    return FlowControlServiceHandler.join(flowControlInfo, message);
  }

  public Message unpack(PackagingInfo packagingInfo, Message message)
    throws PackagingException
  {
    return PackagingServiceDelegate.unpack(packagingInfo, message);
  }

  private FlowControlInfo getFlowControlInfo(
    FlowControlInfo info,
    Message message)
  {
    String eventId =
      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
    if (eventId != null)
    {
      if (FlowControlEventRegistry.getInstance().isSplitDisable(eventId))
      {
        info.setIsSplit(false);
      }
      if (FlowControlEventRegistry.getInstance().isZipDisable(eventId))
      {
        info.setIsZip(false);
      }
    }
    return info;
  }

}
