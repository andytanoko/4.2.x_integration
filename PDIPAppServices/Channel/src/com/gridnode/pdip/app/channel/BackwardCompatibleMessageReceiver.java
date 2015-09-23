/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: BackwardCompatibleMessageReceiver.java
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

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;

public class BackwardCompatibleMessageReceiver extends DefaultMessageReceiver
{
  private static final String CLASS_NAME = "BackwardCompatibleMessageReceiver";

  public BackwardCompatibleMessageReceiver()
  {
  }

  public Message decrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {
    ChannelLogger.debugLog(CLASS_NAME, "decrypt()", "[Begin Decrypt...]");
    Map headers = message.getCommonHeaders();
    String eventId = (String) headers.get(ICommonHeaders.MSG_EVENT_ID);
    String[] data = message.getData();

    ChannelLogger.debugLog(
      CLASS_NAME,
      "decrypt()",
      "[B4 getting backwardcompatile info--]");
    debugLog(securityInfo);
    SecurityInfo cloneSecurityInfo = getSecurityInfo(securityInfo, eventId);
    cloneSecurityInfo.setSecuritylevel(securityInfo.getSecuritylevel());

    ChannelLogger.debugLog(
      CLASS_NAME,
      "decrypt()",
      "[After getting backwardcompatile info--]");
    debugLog(cloneSecurityInfo);
    // @todo to pre-process message data prior to data decryption
    if (cloneSecurityInfo.getSecuritylevel() != ISecurityInfo.SECURITY_LEVEL_2)
      message.setData(preprocessDataForDecryption(data, eventId));
    message = super.decrypt(cloneSecurityInfo, message);
    if (cloneSecurityInfo.getSecuritylevel() != ISecurityInfo.SECURITY_LEVEL_2)
      message.setData(
        postProcessDataAfterDecryption(data, message.getData(), eventId));
    return message;
  }

  public Message join(FlowControlInfo flowControlInfo, Message message)
    throws FlowControlException
  {
    Map headers = message.getCommonHeaders();
    String eventId = (String) headers.get(ICommonHeaders.MSG_EVENT_ID);
    flowControlInfo = getFlowControlInfo(flowControlInfo, eventId);
    return super.join(flowControlInfo, message);
  }

  protected FlowControlInfo getFlowControlInfo(
    FlowControlInfo flowControlInfo,
    String eventId)
  {
    GNCompatibleEventRegistry gnRegistry =
      GNCompatibleEventRegistry.getInstance();
    boolean isZip = gnRegistry.isUnzip(eventId);

    /** @todo this is done only for testing .... */
    if (flowControlInfo == null)
    {
      flowControlInfo = new FlowControlInfo();
    }
    flowControlInfo.setIsZip(isZip);
    flowControlInfo.setFlowControlStatus(
      IFlowControlInfo.STATUS_FLOWCONTROL_SET);
    return flowControlInfo;
  }

  protected SecurityInfo getSecurityInfo(
    SecurityInfo securityInfo,
    String eventId)
  {
    GNCompatibleEventRegistry registry =
      GNCompatibleEventRegistry.getInstance();
    SecurityInfo clnSecurityInfo = (SecurityInfo) securityInfo.clone();
    boolean toDecryptNone = registry.isEncryptNone(eventId);
    if (toDecryptNone)
    {
      clnSecurityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_NONE);
      clnSecurityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
    }

    boolean toDecryptNoVerify = registry.isEncryptNoSign(eventId);
    if (toDecryptNoVerify)
    {
      clnSecurityInfo.setEncryptionType(
        ISecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
      clnSecurityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
    }

    Long encryptionCertificate = clnSecurityInfo.getEncryptionCertificateID();
    Long verificationCertificate =
      clnSecurityInfo.getSignatureEncryptionCertificateID();

    // swap the certificates
    clnSecurityInfo.setEncryptionCertificateID(verificationCertificate);
    clnSecurityInfo.setSignatureEncryptionCertificateID(encryptionCertificate);
    return clnSecurityInfo;
  }

  protected String[] preprocessDataForDecryption(String[] data, String eventId)
  {
    String[] xdata = null; // extracted data from pass in data
    GNCompatibleEventRegistry registry =
      GNCompatibleEventRegistry.getInstance();
    boolean toExcludeFirstTow = registry.isDecryptExcludeFirstTow(eventId);
    boolean toExcludeFirst = registry.isDecryptExcludeFirst(eventId);
    if (toExcludeFirst)
    {
      Boolean returnValue = new Boolean(data[0]);
      if (returnValue.booleanValue() == true)
      {
        xdata = new String[data.length - 1];
        System.arraycopy(data, 1, xdata, 0, (data.length - 1));
      }
    }
    else if (toExcludeFirstTow)
    {
      Boolean returnValue = new Boolean(data[1]);
      if (returnValue.booleanValue() == true)
      {
        xdata = new String[19];
        System.arraycopy(data, 2, xdata, 0, 19);
      }
    }
    else
    {
      // return the pass in data
      xdata = data;
    }
    return xdata;
  }

  protected String[] postProcessDataAfterDecryption(
    String[] data,
    String[] xdata,
    String eventId)
  {
    GNCompatibleEventRegistry registry =
      GNCompatibleEventRegistry.getInstance();
    boolean toExcludeFirstTow = registry.isDecryptExcludeFirstTow(eventId);
    boolean toExcludeFirst = registry.isDecryptExcludeFirst(eventId);
    if (toExcludeFirst)
    {
      Boolean returnValue = new Boolean(data[0]);
      if (returnValue.booleanValue() == true)
      {
        System.arraycopy(xdata, 0, data, 1, xdata.length);
      }
    }
    else if (toExcludeFirstTow)
    {
      Boolean returnValue = new Boolean(data[1]);
      if (returnValue.booleanValue() == true)
      {
        System.arraycopy(xdata, 0, data, 2, 19);
      }
    }
    else
    {
      return xdata; //If none pls return xdata.
    }
    return data;
  }

  private void debugLog(SecurityInfo info)
  {
    ChannelLogger.debugLog(CLASS_NAME, "debugLog()", info.getEncryptionType());
    ChannelLogger.debugLog(CLASS_NAME, "debugLog()", info.getSignatureType());
    ChannelLogger.debugLog(
      CLASS_NAME,
      "debugLog()",
      String.valueOf(info.getEncryptionCertificateID()));
    ChannelLogger.debugLog(
      CLASS_NAME,
      "debugLog()",
      String.valueOf(info.getSignatureEncryptionCertificateID()));
    ChannelLogger.debugLog(CLASS_NAME, "debugLog()", info.getDigestAlgorithm());
    ChannelLogger.debugLog(
      CLASS_NAME,
      "debugLog()",
      String.valueOf(info.getEncryptionLevel()));
    ChannelLogger.debugLog(
      CLASS_NAME,
      "debugLog()",
      String.valueOf(info.getSecuritylevel()));
  }
}
