/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: AbstractMessageSender.java
*
****************************************************************************
* Date           Author                  Changes
****************************************************************************
* July 17 2003    Jagadeesh             Created.
* Jun 07 2004    Neo Sok Lay             Modified: Check for null MessageHeaders
*                                        in audit().
* Sep 02 2004	Jagadeesh		Modified : Check to include Channel Name
* 					in common headers. Bug Fix GNDB00025265
* Nov 01 2006     Tam Wei Xiang          Modfied method send() to trigger the status
*                                        of packaging and document delivery.
* Dec 26 2006     Tam Wei Xiang          Modified method sendDocFlowNotification(...)
*                                        Change the docFlowType from String to enum EDocumentFlow                                       
* Jul 28 2008    Tam Wei Xiang           #69 Modified method sendFeedback(...) to throw up exception 
*                                        if it is a JMSRetryable exception.
*/
package com.gridnode.pdip.app.channel;

import com.gridnode.pdip.app.channel.exceptions.*;
import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelServiceDelegate;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.notification.IDocumentFlow;
import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public abstract class AbstractMessageSender implements IMessageSender
{
  private static final String CLASS_NAME = "AbstractMessageSender";
  protected MessageContext messageContext = null;

  public AbstractMessageSender()
  {
  }

  public void init(MessageContext messageContext)
  {
    this.messageContext = messageContext;
  }

  public void send() throws ChannelException, SystemException
  {
    //TWX 01112006
    Exception packExp = null;
    boolean isPackSuccess = true;
    boolean isSentSuccess = true;
    String sentErrReason = "";
    Map header = null;
    
    Message message =
      (Message) messageContext.getAttribute(IMessageContext.OUTBOUND_MESSAGE);
    
    ChannelInfo channelInfo =
      (ChannelInfo) messageContext.getAttribute(IMessageContext.CHANNEL_INFO);

    try
    {
      if (channelInfo == null)
      {
        throw new ChannelException("Channel to Send Cannot be Null");
      }
      
      header = message.getCommonHeaders();
      
      // set the channel name into the common header
      //Bug Fix GNDB00025265
      //String channelName = getChannelName(channelInfo);

      //Assign ChannelName only if not http,isPartner, and referenceId != null
      //if (channelName != null)
      header.put(ICommonHeaders.COMM_CHANNEL, channelInfo.getName());

       ChannelLogger.infoLog("AbstractMessageSender","send()",
      		"[ChannelName]"+header.get(ICommonHeaders.COMM_CHANNEL));

      message.setCommonHeaders(header);
      ChannelLogger.debugLog("AbstractMessageSender", "send()", "header is "+ header);
      ChannelLogger.debugLog("AbstractMessageSender", "send()", "message header is "+ message.getMessageHeaders());
      // get the channel profiles
      PackagingInfo packagingInfo = channelInfo.getPackagingProfile();
      SecurityInfo securityInfo = channelInfo.getSecurityProfile();
      FlowControlInfo flowControlInfo = channelInfo.getFlowControlInfo();
      CommInfo commInfo = channelInfo.getTptCommInfo();

      // pack the payload in accordance to the envelope type
      message = pack(packagingInfo, message);
        
      // split the pack payload in accordance to the flow control settings
      Message[] messages = null;

      messages = split(flowControlInfo, message);

      if (messages != null)
      {
        for (int i = 0; i < messages.length; i++)
        {
          message = messages[i];
          if (message != null)
          {
            // encrypt and sign the payload in the message.
            message = encrypt(securityInfo, message);
            // create the audit file
            audit(message);
            //Bug Fix GNDB00025265
            //Assign ChannelName only if not http,isPartner, and referenceId != null
            message = postProcessMessage(message,channelInfo);
            
            // send the encrypted message
            message = send(commInfo, message);
            
            String responseCode = getHttpResponseCode(message);
            // send feedback message
            sendFeedback(true, responseCode, message);
          }
        }
      }
      else
      {
        ChannelLogger.infoLog(CLASS_NAME, "send", "[No Message to send]");
      }
    }
    catch (Exception ex)
    { 
      if(! (ex instanceof TransportException) )
      {
        isPackSuccess = false;
        packExp = ex;
      }
      
      ChannelLogger.warnLog(
        CLASS_NAME,
        "sendMessage",
        "Cannot Perform Send ...",
        ex);
      sendFeedback(false, ex.getLocalizedMessage(), message);
      throw new ChannelException("Cannot Perform Send: "+ex.getMessage(),ex);
    }
    finally
    {
      //    TWX 01112006 send out the status of packaging of the payload (include PACK, ENCRYPT)
      sendDocFlowNotification(EDocumentFlowType.PACK_PAYLOAD, new Date(), isPackSuccess, (packExp != null ? packExp.getMessage(): ""),header, null, false, packExp);
    }
  }
  
  /**
   * 01112006 TWX Send out the DocumentFlowNotification for the status of a particular doc flow activity. 
   * @param docFlowType
   * @param eventOccuredDate
   * @param isActivitySuccess
   * @param errReason
   * @param commonHeader
   * @param payload
   * @param isRequiredPack
   * @throws ApplicationException
   */
  private void sendDocFlowNotification(EDocumentFlowType docFlowType, Date eventOccuredDate, boolean isActivitySuccess, String errReason,
                                       Map commonHeader, byte[] payload, boolean isRequiredPack, Exception ex) throws SystemException
  {
    if(commonHeader != null)
    {
      String tracingID = (String)commonHeader.get(ICommonHeaders.TRACING_ID);
      String msgTransID = (String)commonHeader.get(ICommonHeaders.MSG_TRANSACTION_ID);
      
      DocumentFlowNotifyHandler.triggerNotification(docFlowType, eventOccuredDate,isActivitySuccess, errReason,
                                                   tracingID, payload, "", isRequiredPack, "Outbound", msgTransID, ex);
    }
  }
  
  protected void audit(Message message)
  {
    // create the audit file
    Map messageHeaders = message.getMessageHeaders();
    if (messageHeaders != null && messageHeaders.containsKey(IAS2Headers.AS2_FROM))
    {
      String digest = (String)messageHeaders.get(IAS2Headers.MIC);
      messageHeaders.remove(IAS2Headers.MIC);
      String auditFileName = ChannelServiceDelegate.writeToAudit(
        messageHeaders, message.getPayLoadData(), "AS2/", "out");

      if(digest == null)
      {
        digest = "";
      }
      messageHeaders.put(IAS2Headers.MIC, digest);
      messageHeaders.put(IAS2Headers.AUDIT_FILE_NAME, auditFileName);
    }
  }

  public abstract Message pack(PackagingInfo info, Message message)
    throws PackagingException;

  public abstract Message[] split(FlowControlInfo info, Message message)
    throws FlowControlException;

  public abstract Message encrypt(SecurityInfo info, Message message)
    throws SecurityException;

  public abstract Message send(CommInfo info, Message message)
    throws TransportException;

  public abstract void sendFeedback(
    boolean isSent,
    String exception,
    Message message);

/**
 * This method returns ChannelName, if we are transacting with gridtalk activated partners,
 * and protocols other then HTTP.
 *
 * @param info ChannelInfo used as info for the selected channel.
 * @return ChannelName or Null if condition met.
 */
  protected String getChannelName(ChannelInfo info)
  {
    Map header = new HashMap();
    header.putAll(header);
    if ("HTTP".equals(info.getTptProtocolType()) &&
    		info.getReferenceId() == null && info.isPartner())
    {
     return null;
    }
   else
   	return info.getName();
  }

  protected Message postProcessMessage(Message message,ChannelInfo info)
  {
    Map mapheader = new HashMap();
    mapheader.putAll(message.getCommonHeaders());
    if ("HTTP".equals(info.getTptProtocolType()) &&
                    info.getReferenceId() == null && info.isPartner())
    {
      ChannelLogger.debugLog(CLASS_NAME,"[postProcessMessage()]",
      	"[Remove Channel & SenderNodeId]");
      mapheader.remove(ICommonHeaders.COMM_CHANNEL);
      mapheader.remove(ICommonHeaders.SENDER_BE_GNID);
      message.setCommonHeaders(mapheader);
      return message;
    }
    else
    	return message;
  }
  
  /**
   * Get the HTTP Response code.
   * @param msg
   * @return
   */
  private String getHttpResponseCode(Message msg)
  {
    Integer responseCode = (Integer)msg.getHttpResponseCode();
    return responseCode == null ? null : String.valueOf(responseCode);
  }
}
