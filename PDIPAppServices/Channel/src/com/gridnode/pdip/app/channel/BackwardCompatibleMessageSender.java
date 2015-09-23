/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackwardCompatibleMessageSender.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel;

import java.util.Map;

import com.gridnode.pdip.app.channel.exceptions.FlowControlException;
import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.helpers.GNCompatibleEventRegistry;
import com.gridnode.pdip.app.channel.model.FlowControlInfo;
import com.gridnode.pdip.app.channel.model.IFlowControlInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

public class BackwardCompatibleMessageSender extends DefaultMessageSender
{
  //private static final String CLASS_NAME = "BackwardCompatibleMessageSender";

  public BackwardCompatibleMessageSender()
  {
  }

  public Message[] split(FlowControlInfo info, Message message)
    throws FlowControlException
  {
    Map headers = message.getCommonHeaders();
    String eventId = (String) headers.get(ICommonHeaders.MSG_EVENT_ID);
    info = getFlowControlInfo(info, eventId);
    return super.split(info, message);
  }

  private FlowControlInfo getFlowControlInfo(
    FlowControlInfo flowControlInfo,
    String eventId)
  {
    GNCompatibleEventRegistry eventRegistry =
      GNCompatibleEventRegistry.getInstance();
    /** @todo This is done here only for testing .... */
    if (flowControlInfo == null)
    {
      flowControlInfo = new FlowControlInfo();
      flowControlInfo.setSplitSize(IFlowControlInfo.DEFAULT_SPLIT_SIZE);
      flowControlInfo.setSplitThreshold(
        IFlowControlInfo.DEFAULT_SPLIT_THRESHOLD);
    }
    flowControlInfo.setIsZip(eventRegistry.isZip(eventId));
    flowControlInfo.setIsSplit(eventRegistry.isSplit(eventId));
    flowControlInfo.setFlowControlStatus(
      IFlowControlInfo.STATUS_FLOWCONTROL_SET);
    return flowControlInfo;
  }

  public Message encrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {
    Map headers = message.getCommonHeaders();
    String eventId = (String) headers.get(ICommonHeaders.MSG_EVENT_ID);
    securityInfo = getSecurityInfo(securityInfo, eventId);
    return super.encrypt(securityInfo, message);
  }

  /**
   * Get the BackwardCompatible SecurityInfo
   *
   * EventId is a special discriminator used as a function id to make the
   * profiles configurable "externally". This is to in support for
   * backward compatibility with GT 1.x.
   *
   * @param securityInfo - SecurityProfile set for this channel.
   * @param eventId  - EventId passed by the BL.
   * @return - SecurityInfo - whose attribute values are adjusted according to
   *                          EventId.
   */
  protected SecurityInfo getSecurityInfo(
    SecurityInfo securityInfo,
    String eventId)
  {
    SecurityInfo cloneSecurityInfo = (SecurityInfo) securityInfo.clone();
    cloneSecurityInfo.setSecuritylevel(securityInfo.getSecuritylevel());

    GNCompatibleEventRegistry eventRegistry =
      GNCompatibleEventRegistry.getInstance();
    boolean toEncryptNone = eventRegistry.isEncryptNone(eventId);
    boolean toEncryptNoSign = eventRegistry.isEncryptNoSign(eventId);

    if (toEncryptNone)
    {
      cloneSecurityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_NONE);
      cloneSecurityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
    }
    if (toEncryptNoSign)
    {
      cloneSecurityInfo.setEncryptionType(
        ISecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
      cloneSecurityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
    }
    return cloneSecurityInfo;
  }
}