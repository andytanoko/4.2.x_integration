/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: ReceiveMessageListenerMDBean.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Aug 20 2002    Jagadeesh         Created
 * Nov 28 2002    Neo Sok Lay       Check isUnzip() instead of isZip() for
 *                                  backward compatible.
 * Dec 06 2002    Jagadeesh         Modified: Included to add Additional Header
 *                                  -To be notified to BL Listeners.
 *
 * Feb 28 2003    Jagadeesh         Modified - To Pass RNIF2 envelope type(As
 *                                  suggested by Qingsong/Adrian.
 *
 *                                  Added additional log to validate.
 *
 * Jan 30 2003    Kan Mun           Modified - refactored
 *                                           - Added method to handle splt and ack.
 * Mar 21 2003    Kan Mun           Modified - To make the file splitting process works.
 *                                           - To handle cleaning up of files after file
 *                                             splitting process.
 * Apr 03 2003    Kan Mun           Modified - To retrieve the eventId for Split and split
 *                                             Ack.
 *                                           - To pass in the correct channel for sending
 *                                             the next 10 packets.
 * Jul 21 2003    Guo Jianyu        Added RNIF1.1 support
 * Oct 20 2006    Tam Wei Xiang     Added tracingID into payload's header.
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception.
 * Oct 05, 2010   Tam Wei xiang     #1897 - Modified - processReceiveMessage() --> set file payload
 */

package com.gridnode.pdip.app.channel.listener.ejb;

import com.gridnode.pdip.app.channel.IMessageContext;
import com.gridnode.pdip.app.channel.IMessageReceiver;
import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.MessageHandlerFactory;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.UUIDUtil;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.io.File;
import java.util.Map;

public class ReceiveMessageListenerMDBean
  implements MessageDrivenBean, MessageListener
{
 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -8816982322731646775L;
  private static final String CLASS_NAME = "ReceiveMessageListenerMDBean";
  private MessageDrivenContext _ctx;

  /**
   * Standard EJB-MDB Method called by the container, and set's the
   * MessageDrivenContext to this instance.
   *
   * @param ctx - MessageDrivenContex - environment variable used as a
   * gateway to the Container environment.
   */

  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    ChannelLogger.infoLog(
      "ReceiveMessageListenerMDBean",
      "setMessageDrivenContext()",
      "Context Initilized ");
    _ctx = ctx;
  }

  /**
   * Standard EJB-MDB Method to create instance of MessageDrivenBean. And sets
   * this instance in the instance pool.
   */
  public void ejbCreate()
  {
    ChannelLogger.infoLog(
      "ReceiveMessageListenerMDBean",
      "ejbCreate()",
      "In create - MDB Bean");
  }

  /**
   * ReceiveMessageListenerMDBean - is essentially responsible to "Pre-Process"
   * messages received, either from
   *  a . GT-AS,
   *  b . GT-1.x,
   *  c . GT-HTTP - (Please Read : Note-I).
   *  d . ......
   *
   * All Messages are of Type GNTransportPayload, as similar to any Standard
   * Protocol, the Message header is used by the Pre-Processing logic, which
   * include the following processes.
   *
   *  1.  To validate the Message.
   *  2.  To retrieve proper Channel.
   *  3.  To Decrypt the Message.
   *  4.  To Unpackage the Message.
   *  5.  To invoke the BL listener.
   *
   * @param message - Message Object complying to JMS Message Object.
   *
   * Note-I: To Process GT-HTTP, the Http Transport Module needs to set a
   * distinct Constant, in order to process this message uniquely.
   * (This constant is not yet finalized).
   *
   */

  public void onMessage(javax.jms.Message message)
  {
	String jmsMessageID = "";  
    try
    {
      if(message.getJMSRedelivered())
      {
        infoLog("onMessage", "Redelivered msg found. Message: "+message);
      }
      
      jmsMessageID = message.getJMSMessageID();
      processReceiveMessage(message);
    }
    catch (Throwable t)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(t)) //#69 25072008 TWX
      {
        ChannelLogger.warnLog("ReceiveMessageListenerMDBean", "onMessage", "Encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
        _ctx.setRollbackOnly();
      }
    	
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_RECEIVE_MESSAGE_LISTENER,
        "ReceiveMessageListenerMDBean",
        "onMessage",
        "Could not perform onMessage. Error: "+t.getMessage(),
        t);
      
  	  
    }

  }

  private void processReceiveMessage(javax.jms.Message message)
    throws Throwable
  {
    GNTransportPayload payload = null;
    MessageContext messageContext = null;
    //MessageContext for received Message.
    infoLog("processReceiveMessage", "In processReceiveMessage() Begin");
    if (message instanceof ObjectMessage)
    {
      ObjectMessage objectMessage = (ObjectMessage) message;
      payload = (GNTransportPayload) objectMessage.getObject();
      
      addTracingID(payload.getHeader());
      
      Map commonHeaders =
        ChannelReceiveHeader.getCommonHeaders(payload.getHeader());
      Map messageHeaders =
        ChannelReceiveHeader.getMessageHeaders(payload.getHeader());
      com.gridnode.pdip.framework.messaging.Message inboundMessage =
        new com.gridnode.pdip.framework.messaging.Message();
      inboundMessage.setCommonHeaders(commonHeaders);
      inboundMessage.setMessageHeaders(messageHeaders);
      inboundMessage.setData(payload.getData());
      inboundMessage.setPayLoad(payload.getFileContent());
      
      //TWX #1897 20101005 set file payload as well if any
      inboundMessage.setPayLoad(new File[]{payload.getPayloadFile()});

      messageContext = new MessageContext();
      messageContext.setAttribute(
        IMessageContext.INBOUND_MESSAGE,
        inboundMessage);
    }
    else
      throw new Exception("Invalid Message Type. Expecting a valid Message Type");
    //@todo To create a UserDefined exception.
    logReceivedMessage(messageContext);
    IMessageReceiver messageReceiver =
      MessageHandlerFactory.getMessageReceiver(messageContext);
    if (messageReceiver == null)
      infoLog(
        "processReceiveMessage",
        "Message Left UnProcessd As no Message Receiver Exists\n"
          + messageContext);
    else
      messageReceiver.receive();
    infoLog("processReceiveMessage", "In processReceiveMessage() End");
  }
  
  //TWX Create TracingID for OnlineTracking
  private void addTracingID(Map commonHeaders)
  {
    if(commonHeaders.size() > 0)
    {
      String tracingID = UUIDUtil.getRandomUUIDInStr();
      infoLog("addTracingID","[ReceiveMessageListnenerMDBean.addTracingID] tracingID generated for OTC is "+tracingID);
      commonHeaders.put(ICommonHeaders.TRACING_ID, tracingID);
    }
  }
  
  private void logReceivedMessage(MessageContext messageContext)
  {
    Message payLoad =
      (Message) messageContext.getAttribute(IMessageContext.INBOUND_MESSAGE);
    logHeader(payLoad.getCommonHeaders());
    logHeader(payLoad.getMessageHeaders());
    if (null != payLoad.getData())
      ChannelLogger.debugLog(
        CLASS_NAME,
        "logReceivedMessage()",
        "[MetaInfo Length=" + payLoad.getData().length + "]");
    if (null != payLoad.getPayLoadData())
      ChannelLogger.debugLog(
        CLASS_NAME,
        "logReceivedMessage()",
        "[PayLoad Length=" + payLoad.getPayLoadData().length + "]");
    //String[] dataContent = payLoad.getData();
    //byte[] filePayload = payLoad.getFileContent();
  }

  private void logHeader(Map header)
  {
    String[] headerNames = (String[]) header.keySet().toArray(new String[] {
    });
    Object[] headerValues = header.entrySet().toArray(new Object[] {
    });
    for (int i = 0; i < headerNames.length; i++)
      infoLog(
        "logHeader()",
        "HeaderName=["
          + headerNames[i]
          + "]HeaderValue["
          + headerValues[i]
          + "]");
  }

  private void infoLog(String methodName, String message)
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "[" + methodName + "]",
      "[" + message + "]");
  }

  public void ejbRemove()
  {
    infoLog("ejbRemove", "In ejbRemove");
  }

  /*  private Vector performChannelReceive(ChannelInfo info,String[] dataContent,
            byte[] fileContent,String actualFileName,Hashtable header)
            throws Exception
    {
      ChannelLogger.infoLog(CLASS_NAME, "performChannelReceive",
              "From Normal Channel Receive");
      try
      {
        Vector ovec = new Vector();
        File[] actualPayLoad = {};
        byte[] decryptedContent={};
        String[] unpackagedHeader={};
        Hashtable additionalHeader=null;
  
        PackagingInfo packagingInfo = info.getPackagingProfile(); // Get Packaging Profile.
        SecurityInfo  securityInfo = info.getSecurityProfile();   // Get Security Profile.
        com.gridnode.pdip.base.packaging.helper.PackagingInfo baseInfo=null;
  
        SecurityServiceDelegate handler = new SecurityServiceDelegate(securityInfo);
        dataContent = handler.dataToDecrypt(dataContent,false);
        PackagingServiceDelegate packHandler = new PackagingServiceDelegate(packagingInfo);
        if (fileContent!= null)
        {
          decryptedContent = handler.dataToDecrypt(fileContent,false);
          baseInfo = packHandler.unPackage(
                      actualFileName,
                      decryptedContent,
                      dataContent,
                      header
                      );
          actualPayLoad = baseInfo.getUnPackagedPayLoad();
        }
        else
        {
          ChannelLogger.infoLog(CLASS_NAME, "performChannelReceive",
                  "fileContent is Null");
         baseInfo = packHandler.unPackage(null, null, dataContent, header);
        }
        unpackagedHeader =  baseInfo.getDefaultUnPackagedHeader();
        additionalHeader = baseInfo.getAdditionalHeader();
        ovec.add(dataContent);
        ovec.add(actualPayLoad);
        ovec.add(unpackagedHeader);
        ovec.add(additionalHeader);
        ChannelLogger.infoLog(CLASS_NAME, "performChannelReceive",
                "From Normal Channel Receive");
        return ovec;
      }
      catch(Exception ex)
      {
        ChannelLogger.errorLog(CLASS_NAME, "performChannelReceive",
                "Cannot Perform Channel Receive from ChannelInfo", ex);
        throw new Exception("Cannot Perform Channel Receive from ChannelInfo");
      }
    }
  
    private Vector performBackwardCompatibleReceive(String eventID, ChannelInfo info,
            String[] dataContent, byte[] fileContent, String actualFileName,
            Hashtable header)
            throws Exception
    {
      try
      {
        File[] actualPayLoad = {};
        String[] unpackagedHeader = {};
        Hashtable additionalHeader = null;
  
        Vector infoReturn = new Vector();
        com.gridnode.pdip.base.packaging.helper.PackagingInfo baseInfo=null;
  
        PackagingInfo packagingInfo = info.getPackagingProfile();
        SecurityInfo securityInfo   = info.getSecurityProfile();
        CommInfo commInfo           = info.getTptCommInfo();
        //Now Set the Profiles Accordingly for PackagingInfo And SecurityInfo
  
        GNCompatibleEventRegistry registry = GNCompatibleEventRegistry.getInstance();
  
        boolean toDecryptNone = registry.isEncryptNone(eventID);
        boolean toDecryptNoVerify  = registry.isEncryptNoSign(eventID);
  
        boolean toUnZip   = registry.isUnzip(eventID);
        boolean toexcludeFirstTow = registry.isDecryptExcludeFirstTow(eventID);
        boolean toexcludeFirst = registry.isDecryptExcludeFirst(eventID);
  
        if (toDecryptNone)  //Set Decrypt Status
        {
          securityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_NONE);
          securityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
        }
  
        if (toDecryptNoVerify)  //Set Verify Status
        {
          securityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
          securityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
        }
  
        packagingInfo.setZip(toUnZip); //Set UnZip Status
        SecurityServiceDelegate handler = new SecurityServiceDelegate(securityInfo);
  
        ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                "Security Info RefId=" + securityInfo.getReferenceId());
        ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                "Security Info EncryCert=" + securityInfo.getEncryptionCertificateID());
        ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                "Security Info SignCert=" + securityInfo.getSignatureEncryptionCertificateID());
  
        if (toexcludeFirst)
        {
          ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                  "B4 Copying to Exclude First");
          Boolean returnValue = new Boolean(dataContent[0]);
          if (returnValue.booleanValue() == true)
          {
            String[] decryptArray = new String[dataContent.length - 1];
            System.arraycopy(dataContent, 1, decryptArray, 0, (dataContent.length - 1));
            decryptArray = handler.dataToDecrypt(decryptArray, true);
            ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                    "After Copying to Exclude First");
            System.arraycopy(decryptArray, 0, dataContent, 1, decryptArray.length);
            for (int i = 0; i < decryptArray.length; i++)
            ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                    "Data Content " + dataContent[i]);
          }
        }
        else if (toexcludeFirstTow)
        {
          Boolean returnValue = new Boolean(dataContent[1]);
          if (returnValue.booleanValue() == true)
          {
            String[] decryptArray = new String[19];
            System.arraycopy(dataContent, 2, decryptArray, 0, 19);
            decryptArray = handler.dataToDecrypt(decryptArray, true);
            System.arraycopy(decryptArray, 0, dataContent, 2, 19);
            for (int i = 0; i < dataContent.length; i++)
            ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                    "Data Content " + dataContent[i]);
          }
        }
        else
        {
          dataContent = handler.dataToDecrypt(dataContent, true);
        }
  
        PackagingServiceDelegate packHandler = new PackagingServiceDelegate(packagingInfo);
        if (fileContent != null)
        {
          byte[] decryptedContent = {};
          decryptedContent = handler.dataToDecrypt(fileContent, true);
          baseInfo = packHandler.unPackage(
                        actualFileName,
                        decryptedContent,
                        dataContent,
                        header
                        );
          actualPayLoad = baseInfo.getUnPackagedPayLoad();
        }
        else
        {
          ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                  "fileContent is Null");
          baseInfo = packHandler.unPackage(null, null, dataContent, header);
        }
        unpackagedHeader = baseInfo.getDefaultUnPackagedHeader();
        additionalHeader = baseInfo.getAdditionalHeader();
        ChannelLogger.infoLog(CLASS_NAME, "performBackwardCompatibleReceive",
                "B4 Setting Values for backward compatible");
        infoReturn.add(dataContent);
        infoReturn.add(actualPayLoad);
        infoReturn.add(unpackagedHeader);
        infoReturn.add(additionalHeader);
        return infoReturn;
      }
      catch(Exception ex)
      {
        ChannelLogger.errorLog(CLASS_NAME, "performBackwardCompatibleReceive",
                "Cannot Perform BackWard Compatible Channel Handling ", ex);
        throw new Exception("Cannot Perform BackWard Compatible Channel Handling");
      }
  
    }//End of Method
  
    private void handleDefaultReceive(Hashtable header, byte[] fileContent, String[] dataContent)
            throws Exception
    {
      String myrefId      = (String)header.get(ITransportConstants.RECEIPENT_NODE_ID);
      String senderNodeId = (String)header.get(ITransportConstants.SENDER_NODE_ID);
      String actualFileName = (String)header.get(ITransportConstants.FILE_ID);
      String channelName = (String)header.get(IPackagingConstants.CHANNEL_NAME);
  
      ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
              "Channel Reference ID " + myrefId);
      ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
              "Channel File ID " + actualFileName);
      ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
              "Channel Name " + channelName);
      ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
              "SenderNode ID" + senderNodeId);
  
      File[] actualPayLoad = {};
      ChannelInfo info = null;
      CommInfo commInfo = null;
      byte[] decryptedContent = {};
  
      ChannelServiceDelegate cutil = ChannelServiceDelegate.getInstance(); //Utility Class for Channel.
      String eventID = (String)header.get(ITransportConstants.EVENT_ID);
      com.gridnode.pdip.base.packaging.helper.PackagingInfo infoToUnPack = null;
      String[] defaultHeader=null;
      Hashtable additionalHeader=null;
  
      if (channelName != null && myrefId != null)
      {
        ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
                "From ChannelName And RefId");
        info = cutil.getChannelInfoByNameAndRefId(channelName,myrefId);
      }
      else if (GNCompatibleEventRegistry.getInstance().isRelay(eventID))
      {
        ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
                "From Relay Channel");
        info = cutil.getRelayChannelInfo();
      }
      else if (senderNodeId != null)//Receiving from BackwardCompatible....
      {
        ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
                "From BackwardCompatible  ");
        info = cutil.getChannelInfoByRefId(senderNodeId);
      }
      else //Other Messages.
      {
        ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive",
                "From Non-GTAS Receive");
        GNTransportHeader tptheader = new GNTransportHeader(header);
  
        if(tptheader.isRNMessage())
        {
  //        IRNPackagingHandler rnHandler = (IRNPackagingHandler)Class.forName("com.gridnode.gtas.server.rnif.helpers.RNUnpackagingHandler").newInstance();
          if ((tptheader.getRNVersion() != null) && (tptheader.getRNVersion().length()>0))
          { // RNIF2.0
            infoToUnPack =
             new com.gridnode.pdip.base.packaging.helper.PackagingInfo(
             //Default to set RNIF2 PackagingType.(As suggested.)280203
             com.gridnode.pdip.base.packaging.helper.IPackagingInfo.RNIF2_ENVELOPE_TYPE,
             0,false,null,header, false, false);
          }
          else
          { // RNIF1.1
            infoToUnPack =
             new com.gridnode.pdip.base.packaging.helper.PackagingInfo(
             com.gridnode.pdip.base.packaging.helper.IPackagingInfo.RNIF1_ENVELOPE_TYPE,
             0,false,null,header, false, false);
          }
          infoToUnPack.setPayLoadToUnPackage(fileContent);
             PackagingServiceDelegate packagingServiceHandler = new PackagingServiceDelegate(null);
             com.gridnode.pdip.base.packaging.helper.PackagingInfo unPackInfo =
             packagingServiceHandler.unPackage(infoToUnPack); //infoToUnPack is set with Values.Xiaohua.
  //rnHandler.unPackage(infoToUnPack);
        }
        else
        {
  
        String packagingType = PackagingServiceDelegate.getEnvelopeType(header);
        ChannelLogger.infoLog(CLASS_NAME,"handleDefaultReceive()","["+packagingType+"]");
        if (packagingType != null)
        {
          infoToUnPack = new com.gridnode.pdip.base.packaging.helper.PackagingInfo(
                  packagingType,0,true,actualFileName,
                  header, false, false);
          infoToUnPack.setPayLoadToUnPackage(fileContent);
          PackagingServiceDelegate packagingServiceHandler = new PackagingServiceDelegate(null);
          com.gridnode.pdip.base.packaging.helper.PackagingInfo unPackInfo =
                packagingServiceHandler.unPackage(infoToUnPack);
          infoToUnPack = unPackInfo;
        }
        }
      }
  
      if (info != null)
      {
        commInfo = info.getTptCommInfo();
        Vector resultVect=null;
        if (commInfo.getTptImplVersion().startsWith("02"))
        {  //Perform BackwardCompatible..
          resultVect = performBackwardCompatibleReceive(eventID,
                                                        info,
                                                        dataContent,
                                                        fileContent,
                                                        actualFileName,
                                                        header
                                                        );
        }
        else //Perform Normal Channel Receive
        {
          resultVect = performChannelReceive(info,
                                             dataContent,
                                             fileContent,
                                             actualFileName,
                                             header
                                             );
        }
        dataContent = (String[]) resultVect.get(0);
        actualPayLoad = (File[]) resultVect.get(1);
        defaultHeader = (String[]) resultVect.get(2);
        additionalHeader = (Hashtable) resultVect.get(3);
        eventID = defaultHeader[0];
      }
      else if(infoToUnPack != null)
      {
        defaultHeader = infoToUnPack.getDefaultUnPackagedHeader();
        additionalHeader = infoToUnPack.getAdditionalHeader();
        actualPayLoad = infoToUnPack.getUnPackagedPayLoad();
        eventID = defaultHeader[0];
        ChannelLogger.debugLog(CLASS_NAME, "handleDefaultReceive",
                          "Default Header After UnPackaging"+defaultHeader);
        ChannelLogger.debugLog(CLASS_NAME, "handleDefaultReceive",
                          "Additional Header After UnPackaging"+defaultHeader);
        ChannelLogger.debugLog(CLASS_NAME, "handleDefaultReceive",
                          "EventID After UnPackaging"+eventID);
  
      }
      ChannelLogger.infoLog(CLASS_NAME, "handleDefaultReceive", "After Decrypt And Set");
  
      if (header != null)
      {
        IReceiveMessageHandler listener = MessageHandlerRegistry.getHandlerRegistry(
                ).getReceiveMessageHandler(eventID);
        if(listener != null)
        {
          listener.handlerMessage(defaultHeader,
                                  dataContent,
                                  actualPayLoad,
                                  additionalHeader
                                  );
        }
        else
        {
          ChannelLogger.infoLog("ReceiveMessageListenerMDBean", "handleDefaultReceive",
                  "No Handler Configured For This ID " + eventID);
        }
      }
  
      ChannelLogger.infoLog("ReceiveMessageListenerMDBean", "handleDefaultReceive",
              "After Calling Handler ");
      ChannelLogger.infoLog("ReceiveMessageListenerMDBean", "handleDefaultReceive",
              "Message Handled Successfully ... ");
    }
  
    private void handleReceiveSplit(Hashtable header, byte[] fileContent,
            String[] dataContent, boolean isReceiveAck)
            throws Exception
    {
      @todo Current Version not implemented yet. For backward compatible version only.
      ChannelLogger.infoLog(CLASS_NAME, "handleReceiveSplit", "isReceiveAck = " + isReceiveAck);
      String finalRecipientNodeId = (String) header.get(ITransportConstants.RECEIPENT_NODE_ID);
      String originalSenderNodeId = (String) header.get(ITransportConstants.SENDER_NODE_ID);
      String currentSenderNodeId = (String) header.get(ITransportConstants.PROCESS_ID);
      String fileId = (String) header.get(ITransportConstants.FILE_ID);
      String eventId = (String) header.get(ITransportConstants.EVENT_ID);
      String myNodeId = ChannelServiceDelegate.getInstance().getMyNodeId();
      boolean forMe = (myNodeId.equals(finalRecipientNodeId));
  
      String[] data = null;
      byte[] payload = null;
  
      ChannelInfo currentSender = getChannelInfo(currentSenderNodeId);
  
      if (currentSenderNodeId != null && !currentSenderNodeId.equals("0")) // relay
        data = decrypt(currentSenderNodeId, dataContent, eventId);
      else
        data = decrypt(originalSenderNodeId, dataContent, eventId);
  
      boolean isFromDelay = !originalSenderNodeId.equals(currentSenderNodeId);
  
  
      PackagingInfo packageInfo = null;
      if (forMe) // Decrypt and join
      {
  //ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplit", "decrypt using " + originalSenderNodeId + "'s security");
  //      payload = decrypt(originalSenderNodeId, fileContent, eventId);
  ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplit", "decrypt using " + finalRecipientNodeId + "'s security");
        payload = decrypt(finalRecipientNodeId, fileContent, eventId);
        ChannelInfo channel = getChannelInfo(originalSenderNodeId);
        packageInfo = channel.getPackagingProfile();
  //      if (isBackwardCompatible(channel.getTptCommInfo()))
          packageInfo = getBackwardCompatiblePackagingInfo(packageInfo, eventId, isFromDelay);
        packageInfo.setIsRelay(false); // To Trigger the Join
        ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplit", "finalRecipientisMe = true");
      }
      else
      {
        payload = fileContent;
        packageInfo = currentSender.getPackagingProfile();
  //      if (isBackwardCompatible(currentSender.getTptCommInfo()))
          packageInfo = getBackwardCompatiblePackagingInfo(packageInfo, eventId, isFromDelay);
        packageInfo.setIsRelay(true); // Don't join
        ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplit", "finalRecipientisMe = false");
      }
  
      PackagingServiceDelegate packHandler = new PackagingServiceDelegate(packageInfo);
      com.gridnode.pdip.base.packaging.helper.PackagingInfo unPackInfo =
                      packHandler.unPackage(fileId, payload, data, header);
  
      String[] dataReceived = unPackInfo.getDataContent();
      File[] payloadReceived = unPackInfo.getUnPackagedPayLoad();
      String[] headerReceived = unPackInfo.getDefaultUnPackagedHeader();
      Hashtable additionalHeader = unPackInfo.getAdditionalHeader();
  
      if (unPackInfo.isAllPacketsProcessed())
      {
        invokeListener(headerReceived[0], headerReceived, dataReceived, payloadReceived, additionalHeader);
        if (isReceiveAck)
        {
          new CleanupFilePartMessageHandler(
                  currentSender,
                  headerReceived[6],
                  myNodeId
                  );
        }
      }
      else
      {
        if (isReceiveAck)
        {
          if (unPackInfo.moreBlocksToProcess())
            new SendFilePartMessageHandler(
                    //original sender of the ack event is the final recipient of the send file split event.
                    getChannelInfo(originalSenderNodeId),
                    dataReceived,
                    payloadReceived,
                    headerReceived,
                    // final recipient of the ack event is the original sender of the send file split event.
                    finalRecipientNodeId
                    );
          else
            ChannelLogger.infoLog(CLASS_NAME, "handleReceiveSplit", "No more block to process!");
        }
        else
        {
          if (!unPackInfo.moreBlocksToProcess())
          {
            new SendFilePartAckMessageHandler(
                          currentSender,
                          unPackInfo.getDataContent(),
                          unPackInfo.getUnPackagedPayLoad(),
                          unPackInfo.getDefaultUnPackagedHeader(),
                          myNodeId
                        );
          }
          else
            ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplit", "Waiting for next packet...");
        }
      }
    }
  
    private String[] decrypt(String nodeId, String[] dataContent, String eventId)
            throws Exception
    {
      ChannelLogger.debugLog(CLASS_NAME, "decrypt", "with eventId = " + eventId +
              "and nodeId = " + nodeId);
      ChannelInfo channel = getChannelInfo(nodeId);
      SecurityInfo securityInfo = channel.getSecurityProfile();
      boolean backwardCompatible = isBackwardCompatible(channel.getTptCommInfo());
      SecurityServiceDelegate handler = null;
      if (backwardCompatible)
      {
        ChannelLogger.debugLog(CLASS_NAME, "decrypt", "Backward compatible decryption");
        handler = getBackwardCompatibleSecurityServiceHandler(securityInfo, eventId);
        return getBackwardCompatibleDecryptedData(dataContent, eventId, handler);
      }
      else
      {
        ChannelLogger.debugLog(CLASS_NAME, "decrypt", "Normal decryption");
        handler = new SecurityServiceDelegate(securityInfo);
        return handler.dataToDecrypt(dataContent, false);
      }
    }
  
    private byte[] decrypt(String nodeId, byte[] fileContent, String eventId)
            throws Exception
    {
      ChannelLogger.debugLog(CLASS_NAME, "decrypt", "with eventId = " + eventId +
              "and nodeId = " + nodeId);
      ChannelInfo channel = getChannelInfo(nodeId);
      SecurityInfo securityInfo = channel.getSecurityProfile();
      boolean backwardCompatible = isBackwardCompatible(channel.getTptCommInfo());
      SecurityServiceDelegate handler = null;
      if (backwardCompatible)
      {
        ChannelLogger.debugLog(CLASS_NAME, "decrypt", "Backward compatible decryption");
        handler = getBackwardCompatibleSecurityServiceHandler(securityInfo, eventId);
      }
      else
      {
        ChannelLogger.debugLog(CLASS_NAME, "decrypt", "Normal decryption");
        handler = new SecurityServiceDelegate(securityInfo);
      }
      return handler.dataToDecrypt(fileContent, backwardCompatible);
    }
  
    private ChannelInfo getChannelInfo(String refId) throws Exception
    {
      return ChannelServiceDelegate.getInstance().getChannelInfoByRefId(refId);
    }
  
    private PackagingInfo getBackwardCompatiblePackagingInfo(PackagingInfo packagingInfo,
            String eventIdGtAs, boolean isFromDelay)
    {
      ChannelLogger.infoLog(CLASS_NAME, "getBackwardCompatiblePackagingInfo",
              "Get Info for " + eventIdGtAs + ", isFromDelay = " + isFromDelay);
      GNCompatibleEventRegistry eventRegistry = GNCompatibleEventRegistry.getInstance();
      if (isFromDelay)
        packagingInfo.setZip(true);
      else
        packagingInfo.setZip(eventRegistry.isZip(eventIdGtAs));
      packagingInfo.setSplit(eventRegistry.isSplitEvent(eventIdGtAs));
      packagingInfo.setSplitAck(eventRegistry.isSplitAckEvent(eventIdGtAs));
      packagingInfo.setEnvelope(packagingInfo.FILE_SPLIT_ENVELOPE_TYPE);
      ChannelLogger.infoLog(CLASS_NAME, "getBackwardCompatiblePackagingInfo",
              "isZip = " + packagingInfo.isZip() +
              ", isSplit = " + packagingInfo.isSplit() +
              ", isSplitAck = " + packagingInfo.isSplitAck()
              );
      return packagingInfo;
    }
  
    private boolean isBackwardCompatible(CommInfo commInfo)
    {
      return commInfo.getTptImplVersion().startsWith("02");
    }
  
    private void invokeListener(String eventId, String[] header, String[] dataContent,
            File[] payload, Hashtable additionalHeader)
    {
      ChannelLogger.infoLog("ReceiveMessageListenerMDBean", "invokeListener",
          "eventId = " + eventId + " and (header == null) = " + (header == null));
      if (header != null)
      {
        IReceiveMessageHandler listener = MessageHandlerRegistry.getHandlerRegistry(
            ).getReceiveMessageHandler(eventId);
        if (listener != null)
        {
          listener.handlerMessage(header,
                                  dataContent,
                                  payload,
                                  additionalHeader
                                 );
        }
        else
        {
          ChannelLogger.infoLog("ReceiveMessageListenerMDBean", "invokeListener",
              "No Handler Configured For This ID " + eventId);
        }
      }
    }
  
    private SecurityServiceDelegate getBackwardCompatibleSecurityServiceHandler
            (SecurityInfo securityInfo, String eventId)
    {
      ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleSecurityServiceHandler",
              "eventId is " + eventId);
      GNCompatibleEventRegistry registry = GNCompatibleEventRegistry.getInstance();
      if (registry.isEncryptNone(eventId))  //Set Decrypt Status
      {
        securityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_NONE);
        securityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
      }
      if (registry.isEncryptNoSign(eventId))  //Set Verify Status
      {
        securityInfo.setEncryptionType(ISecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
        securityInfo.setSignatureType(ISecurityInfo.SIGNATURE_TYPE_NONE);
      }
      return new SecurityServiceDelegate(securityInfo);
    }
  
    private String[] getBackwardCompatibleDecryptedData(String[] encryptedData,
            String eventId, SecurityServiceDelegate handler) throws Exception
    {
      ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
              "Decrypt with eventId = " + eventId);
      GNCompatibleEventRegistry registry = GNCompatibleEventRegistry.getInstance();
      String[] decryptedData = new String[encryptedData.length];
      System.arraycopy(encryptedData, 0, decryptedData, 0, encryptedData.length);
      if (registry.isDecryptExcludeFirst(eventId))
      {
        ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
                "Event is decrypt exclude first");
        Boolean returnValue = new Boolean(encryptedData[0]);
        if (returnValue.booleanValue())
        {
          ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
                  "Decrypt data exclude first");
          String[] tmpArray = new String[encryptedData.length - 1];
          System.arraycopy(encryptedData, 1, tmpArray, 0, (encryptedData.length - 1));
          tmpArray = handler.dataToDecrypt(tmpArray, true);
          System.arraycopy(tmpArray, 0, decryptedData, 1, tmpArray.length);
        }
      }
      else if (registry.isDecryptExcludeFirstTow(eventId))
      {
        ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
                "Event is decrypt exclude first two");
        Boolean returnValue = new Boolean(encryptedData[1]);
        if (returnValue.booleanValue())
        {
          ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
                  "Decrypt data exclude first two");
          String[] tmpArray = new String[19];
          System.arraycopy(encryptedData, 2, tmpArray, 0, 19);
          tmpArray = handler.dataToDecrypt(tmpArray, true);
          System.arraycopy(tmpArray, 0, decryptedData, 2, 19);
        }
      }
      else
      {
        ChannelLogger.debugLog(CLASS_NAME, "getBackwardCompatibleDecryptedData",
                "Decrypt data normal");
        decryptedData = handler.dataToDecrypt(encryptedData, true);
      }
      return decryptedData;
    }
  
    private void infoLog(String methodName,String message)
    {
      ChannelLogger.infoLog(CLASS_NAME,"["+ methodName + "]","["+ message +"]");
    }
  
   */
}