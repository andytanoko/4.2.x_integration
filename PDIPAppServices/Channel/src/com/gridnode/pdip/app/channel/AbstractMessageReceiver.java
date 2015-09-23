/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: AbstractMessageReceiver.java
*
****************************************************************************
* Date           Author                  Changes
****************************************************************************
* Aug 17 2003    Jagadeesh             Created.
* Mar 22 2006    Neo Sok Lay             Add SENDER_BE_ID, SENDER_BE_DUNS, 
*                                        RECIPIENT_BE_ID, RECIPIENT_BE_DUNS, 
*                                        and SENDER_ID to CommonHeaders Map.
* Oct 23 2006    Tam Wei Xiang           Added TRACING_ID into CommonHeaders[]                                       
* Dec 26 2006    Tam Wei Xiang           Change the docFlowType from String to
*                                        enum EDocumentFlow.
* Sep 30 2010    Tam Wei Xiang           #1897 - set incomingConnectionType to 
*                                                ChannelReceiveHeader.
*/

package com.gridnode.pdip.app.channel;

import java.util.Date;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.exceptions.FlowControlException;
import com.gridnode.pdip.app.channel.exceptions.PackagingException;
import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.handler.MessageHandlerRegistry;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.PackagingServiceDelegate;

import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.FlowControlInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.notification.IDocumentFlow;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.notification.Notifier;

import com.gridnode.pdip.base.transport.helpers.TptMapping;
import com.gridnode.pdip.base.transport.helpers.TptMappingRegistry;
import com.gridnode.pdip.base.transport.helpers.TptMappingFactory;

import java.util.Hashtable;

public abstract class AbstractMessageReceiver implements IMessageReceiver
{
  private static final String CLASS_NAME = "AbstractMessageReceiver";
  protected MessageContext messageContext = null;

  public AbstractMessageReceiver()
  {
  }

  public void init(MessageContext messageContext)
  {
    this.messageContext = messageContext;
  }

  public void receive() throws ChannelException, SystemException
  {
    boolean isUnpackSuccess = true;
    byte[] payloadData = null;
    Message message = null;
    Date receivedDate = new Date();
    Date unpackDate = null;
    Exception unpackErr = null;
    try
    {
      message =
        (Message) messageContext.getAttribute(IMessageContext.INBOUND_MESSAGE);
      payloadData = message.getPayLoadData();
      
      ChannelInfo channelInfo =
        (ChannelInfo) messageContext.getAttribute(IMessageContext.CHANNEL_INFO);

      SecurityInfo securityInfo = channelInfo.getSecurityProfile();
      PackagingInfo packagingInfo = channelInfo.getPackagingProfile();
      FlowControlInfo flowControlInfo = channelInfo.getFlowControlInfo();
      CommInfo commInfo = channelInfo.getTptCommInfo();

      Map messageHeaders =
        getTransformedMessageHeaders(
          message,
          commInfo.getProtocolType(),
          packagingInfo.getEnvelope());
      message.setMessageHeaders(messageHeaders);
      message = decrypt(securityInfo, message);
      message = join(flowControlInfo, message);
      
      message = unpack(packagingInfo, message);
      unpackDate = new Date();
      
      invokeListener(message);
      return;
    }
    catch (Exception ex)
    { 
      isUnpackSuccess = false;
      unpackErr = ex;
      ChannelLogger.warnLog(
        CLASS_NAME,
        "receiveMessage",
        "Unable to Process Receive Message",
        ex);
      throw new ChannelException("Unable to Process Receive Message: "+ex.getMessage(), ex);
    }
    finally
    {
      String tracingID = (String)message.getCommonHeaders().get(ICommonHeaders.TRACING_ID);
      String receiverBeID = (String)message.getCommonHeaders().get(ICommonHeaders.RECIPENT_BE_ID);
      
      DocumentFlowNotifyHandler.triggerNotification(EDocumentFlowType.DOCUMENT_RECEIVED, 
                                                   receivedDate, true, "", 
                                                   tracingID,payloadData, "", false, receiverBeID, null);
      
      //    TWX 27102006    broadcast the Document Unpack status
      DocumentFlowNotifyHandler.triggerNotification(EDocumentFlowType.UNPACK_PAYLOAD,
                                                    unpackDate== null? new Date(): unpackDate, isUnpackSuccess,
                                                   (unpackErr != null? unpackErr.getMessage(): ""), tracingID,null, "", false, receiverBeID,  unpackErr);
      
    }
  }

  public abstract Message decrypt(SecurityInfo info, Message message)
    throws SecurityException;

  public abstract Message join(FlowControlInfo info, Message message)
    throws FlowControlException;

  public abstract Message unpack(PackagingInfo packagingInfo, Message message)
    throws PackagingException;

  public void invokeListener(Message message) throws Exception
  {
    if (message != null)
    {
      Map header = message.getCommonHeaders();
      Map messageHeaders = null;
      
      IReceiveMessageHandler listener =
        MessageHandlerRegistry.getHandlerRegistry().getReceiveMessageHandler(
          (String) header.get(ICommonHeaders.MSG_EVENT_ID));
      String[] headers = getCommonHeaders(message.getCommonHeaders());

      if (message.getMessageHeaders() != null)
      {
        messageHeaders = getMessageHeaders(message.getMessageHeaders());
      }
      listener.handlerMessage(
        headers,
        message.getData(),
        message.getPayLoad(),
        new Hashtable(messageHeaders));
      return;
      /* @todo invoke BL to process the received message */
    }
    else
      ChannelLogger.infoLog(
        CLASS_NAME,
        "invokeListener",
        "Message is Null");
  }

  public String[] getCommonHeaders(Map header)
  {
    if (header != null)
    {
      ChannelReceiveHeader chlReceiveHeader =
        new ChannelReceiveHeader(
          (String) header.get(ICommonHeaders.MSG_EVENT_ID),
          (String) header.get(ICommonHeaders.MSG_TRANSACTION_ID),
          (String) header.get(ICommonHeaders.MSG_EVENT_SUB_ID),
          (String) header.get(ICommonHeaders.PAYLOAD_ID),
          (String) header.get(ICommonHeaders.MSG_PROCESS_ID),
          (String) header.get(ICommonHeaders.COMM_CHANNEL));

      chlReceiveHeader.setGridnodeHeaderInfo(
        (String) header.get(ICommonHeaders.SENDER_BE_GNID),
        (String) header.get(ICommonHeaders.RECIPENT_BE_GNID));

      chlReceiveHeader.setMessageHeaderInfo(
        (String) header.get(ICommonHeaders.PAYLOAD_GROUP),
        (String) header.get(ICommonHeaders.PAYLOAD_TYPE));

      //NSL20060322 Add SENDER_BE_ID, SENDER_BE_DUNS, RECIPIENT_BE_ID, RECIPIENT_BE_DUNS, SENDER_ID
      chlReceiveHeader.setRegistryHeaderInfo(
        (String) header.get(ICommonHeaders.SENDER_BE_ID),
        (String) header.get(ICommonHeaders.SENDER_BE_DUNS),
        (String) header.get(ICommonHeaders.SENDER_BE_UUID),
        (String) header.get(ICommonHeaders.SENDER_BE_UDDI_URL),
        (String) header.get(ICommonHeaders.RECIPENT_BE_ID),
        (String) header.get(ICommonHeaders.RECIPENT_BE_DUNS),
        (String) header.get(ICommonHeaders.RECIPENT_BE_UUID),
        (String) header.get(ICommonHeaders.RECIPENT_BE_UDDI_URL));
      
      chlReceiveHeader.setPartnerHeaderInfo(
        (String) header.get(ICommonHeaders.SENDER_ID));
      
      //TWX 23102006 Added in tracingID
      chlReceiveHeader.setOnlineTrackingHeaderInfo(
        (String) header.get(ICommonHeaders.TRACING_ID));
      
      //TWX 30092010 #1897 Added incoming connection type
      chlReceiveHeader.setIncomingConnectionType((String) header.get(ICommonHeaders.COMM_CONNECTION_TYPE));
      
      return chlReceiveHeader.getHeaderArray();
    }
    else
      return new String[] {
    };
  }

  /** @todo This method can be moved later either to transport or can remain as is. */
  /**
   * This method transforms the message-standard headers to message-framework(channel) headers.
   * There is exists inconsistency, transforming the headers, since we do not have
   * a framework for Transport-Receive.
   * In a "ideal scenario" Transport should handle these transformation (i.e both
   * InBound, and OutBound).
   * As of 2.3, only OutBound Messages are transformed at Transport while Send (Transport Facade).
   *
   * Another inconsistency exists at ICommonHeaders.PAYLOAD_TYPE which is used both by
   * Transport and Channel in totally different context.
   * To Transport the ICommonHeaders.PAYLOAD_TYPE indicates the recipient (i.e GT or NonGT)
   * To Channel the ICommonHeaders.PAYLOAD_TYPE indicates the payload type(RNIF1,RNIF2,NONE)..
   *
   * The above mentioned inconsistency's will be addressed shortly ....
   * 
   * @param message
   * @param protocol
   * @return
   */
  private Map getTransformedMessageHeaders(
    Message message,
    String protocol,
    String envelopeType)
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "getTransformedMessageHeaders",
      "In Transformation Begin");
    TptMappingRegistry registry =
      TptMappingFactory.getInstance().getMappingRegistry(protocol);
    if (null == registry)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getTransformedMessageHeaders",
        "No Transformation Done For Protocol=" + protocol);
      return message.getMessageHeaders();
      //No transformation Done since no mapping exists.
    }
    else
    {
      //String messageType = (String)message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_TYPE);
      //Commented as mentioned in above description....
      TptMapping mapping =
        registry.getTptMapping(
          PackagingServiceDelegate.getPackagedPayLoadType(envelopeType));
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getTransformedMessageHeaders",
        "In Transformation");
      if (null == mapping)
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getTransformedMessageHeaders",
          "No Mapping Exists From MessageType" + envelopeType);
        return message.getMessageHeaders();
      }
      else
        return mapping.getInBoundMappedHeader(message.getMessageHeaders());
    }
  }
  /*
  private boolean checkNullMessageHeaders(Map messageHeaders)
  {
    Iterator ite = messageHeaders.keySet().iterator();
    while (ite.hasNext())
    {
      Object key = ite.next();
      if (key == null || messageHeaders.get(key) == null)
        return true;
    }
    return false;
  }*/

  private Map getMessageHeaders(Map messageHeaders)
  {
    Map headers = new HashMap();
    Iterator ite = messageHeaders.keySet().iterator();
    while (ite.hasNext())
    {
      Object key = ite.next();
      if (key != null && messageHeaders.get(key) != null)
        headers.put(key, messageHeaders.get(key));
    }
    return headers;
  }
}