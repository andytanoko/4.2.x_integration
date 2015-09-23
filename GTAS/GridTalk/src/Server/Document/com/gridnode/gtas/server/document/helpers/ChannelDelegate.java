/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Koh Han Sing        Created
 * Nov 25 2002    Neo Sok Lay         Add RecipientNodeId to header during send.
 * May 20 2003    Neo Sok Lay         Handle send feedback. If send not sucessful,
 *                                    raise alert.
 * Jun 19 2003    Neo Sok  Lay        Call docMgr.handleDocSent() to update the
 *                                    DateTimeSendEnd (for documents not sent using
 *                                    default master channel).
 * Oct 01 2003    Neo Sok Lay         Change construction of channel header array in
 *                                    send()
 * Oct 29 2003    Guo Jianyu          Modified send() to include more headers
 * May 27 2004    Guo Jianyu          Modified handlerFeedback()
 * Mar 28 2005    Mahesh              Removed appending the new audit file names
 * Apr 18 2006    Neo Sok Lay         Only update gdoc with audit filename and message digest
 *                                    when send is successful.  
 * Sep 05 2006    Neo Sok Lay         GNDB00027784: 
 *                                    - Revert above change. Need to update audit file & message digest
 *                                      regardless whether send is successful. Need it for resend,
 *                                      and also need it for RNIF audit.
 *                                    - handlerFeedback(): Use DocumentManagerBean to update gdoc instead
 *                                      of the entity handler.                                
 * Oct 20 2006    Tam Wei Xiang       Include tracingID in the ChannelSendHeader.
 *                                    Modified prepareSendHeader(...), prepareUploadHeader(...),
 *                                    Added getChannelSendHeader(...)     
 * Mac 01 2007    Tam Wei Xiang       Include the httpResponse code into DocumentFlowNotification. 
 * Jul 25 2008    Tam Wei Xiang       #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.                                                       
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.ICommInfo;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.notification.IDocumentFlow;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * This class provides channel services.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class ChannelDelegate implements IReceiveFeedbackHandler
{

  /**
   * Obtain the EJBObject for the ChannelManagerBean.
   *
   * @return The EJBObject to the ChannelManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IChannelManagerObj getManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IChannelManagerHome.class.getName(),
      IChannelManagerHome.class,
      new Object[0]);
  }

  /**
   * This method will check whether the given partner is currently online.
   *
   * @param partnerID The ID of the partner to check.
   * @return the boolean value indicating whether the partner is online.
   *
   * @since 2.0
   */
  //public static boolean isPartnerOnline(String partnerID)
  //{
    /**@todo check whether partner is online */
  //  return true;
  //}

  /**
   * This method will retrieve the ChannelInfo base on its Uid.
   *
   * @param channelUid The Uid of the ChannelInfo to retrieve.
   * return the ChannelInfo of the Uid provided.
   *
   * @since 2.0
   */
  public static ChannelInfo getChannelInfo(Long channelUid)
    throws Exception
  {
    return getManager().getChannelInfo(channelUid);
  }

  public static void send(ChannelInfo channelInfo,
                          String[] dataToSend,
                          File[] filesToSend,
                          String eventId,
                          String senderGdocID,
                          String senderNodeID,
                          String recipientNodeID)
                          throws Exception
  {
    ChannelSendHeader sendHeader = prepareSendHeader(eventId, null, null, senderNodeID,
      recipientNodeID);
    send(channelInfo, dataToSend, filesToSend, sendHeader);
  }

  /**
   * This method will send the files given to another GridTalk base on the
   * ChannelInfo provided.
   *
   * @param channelInfo The ChannelInfo of the gridnode to sent the
   *                   files to.
   * @param dataToSend The String array containing the information to be sent.
   * @param filesToSend The file array containing the files to be sent.
   * @param header ChannelSendHeader containing the header information for sending
   * @param eventId The EventId of the upload event
   * @param senderGdocID GridDoc ID of Sender's document or Transaction Id of a
   * received document (for use when sending acknowledgement).
   * @param senderNodeID Node ID of this GridTalk's GridNode
   * @param recipientNodeID Node ID of the target recipient of the document.
   *
   * @since 2.0
   */
  public static void send(ChannelInfo channelInfo,
                          String[] dataToSend,
                          File[] filesToSend,
                          ChannelSendHeader header)
                          //String eventId,
                          //String senderGdocID,
                          //String senderNodeID,
                          //String recipientNodeID)
                          throws Exception
  {
    /*031001NSL
    String trxId = senderGdocID;
    String fileId = new StringBuffer("send_E").append(eventId).append("_T").append(
                      trxId).append("_S").append(senderNodeID).append("_R").append(
                      recipientNodeID).toString();
    String myGNCI = ConnectionDelegate.getPostOffice().getMyGridNodeCommInfo();

    String[] headers = null;

    if ((moreHeaders == null) || (moreHeaders.length == 0))
      headers = new String[] {
        eventId,                           //EventId
        trxId,                             //FunctionId
        IDocumentEvents.GRIDDOC_SUB_EVENT, //EventSubId
        fileId,                            //FileId
        senderNodeID,                      //SenderNodeId
        recipientNodeID,                   //RecipientNodeId
        myGNCI,                            //MyGNCI
        };
    else
    {
      headers = new String[7 + moreHeaders.length];
      headers[0] = eventId;
      headers[1] = trxId;
      headers[2] = IDocumentEvents.GRIDDOC_SUB_EVENT;
      headers[3] = fileId;
      headers[4] = senderNodeID;
      headers[5] = recipientNodeID;
      headers[6] = myGNCI;
      for (int i=0; i<moreHeaders.length; i++)
      {
        headers[i+7] = moreHeaders[i];
      }
    }

    getManager().send(channelInfo, dataToSend, filesToSend, headers);
    */
    getManager().send(channelInfo, dataToSend, filesToSend, header.getHeaderArray());
  }

  /**
   * Prepares the header for sending message via a partner channel.
   *
   * @param eventId The EventId of the upload event
   * @param trxId GridDoc ID of Sender's document or Transaction Id of a
   * received document (for use when sending acknowledgement).
   * @param gdoc
   * @param senderNodeId GridnodeId of this GridTalk's GridNode
   * @param recipientNodeId GridNodeId of the target recipient of the message. This may be <b>null</b>.
   * @return the constructed ChannelSendHeader
   * @throws Exception Error obtaining the proprietary GNCI for this GridTalk.
   */
  public static ChannelSendHeader prepareSendHeader(
    String eventId, String trxId, GridDocument gdoc, String senderNodeId, String recipientNodeId)
    throws Exception
  {
    String fileId = new StringBuffer("send_E").append(eventId).append("_T").append(
                      trxId).append("_S").append(senderNodeId).append("_R").append(
                      recipientNodeId).toString();
    String myGNCI = ConnectionDelegate.getPostOffice().getMyGridNodeCommInfo();

    ChannelSendHeader header = getChannelSendHeader(gdoc, eventId, trxId, IDocumentEvents.GRIDDOC_SUB_EVENT, fileId);
    header.setGridnodeHeaderInfo(senderNodeId, recipientNodeId, myGNCI);
    if (gdoc != null)
    {
      header.setRegistryHeaderInfo(
        gdoc.getSenderBizEntityUuid(),
        gdoc.getSenderRegistryQueryUrl(),
        gdoc.getRecipientBizEntityUuid(),
        gdoc.getRecipientRegistryQueryUrl());
      header.setMessageHeaderInfo(gdoc.getUdocDocType(), null);
    }
    return header;
  }

  /**
   * Prepares the header for uploading message via the GridMaster channel.
   *
   * @param eventId The EventId of the upload event
   * @param trxId GridDoc ID of Sender's document or Transaction Id of a
   * received document (for use when sending acknowledgement).
   * @param gdoc
   * @param senderNodeId GridnodeId of this GridTalk's GridNode
   * @param recipientNodeId GridNodeId of the target recipient of the message.
   * @return the constructed ChannelSendHeader
   * @throws Exception Error obtaining the proprietary GNCI for this GridTalk.
   */
  public static ChannelSendHeader prepareUploadHeader(
    String eventId, String trxId, GridDocument gdoc, String senderNodeId, String recipientNodeId) throws Exception
  {
    String fileId = new StringBuffer("upload_E").append(eventId).append("_T").append(
                      trxId).append("_S").append(senderNodeId).append("_R").append(
                      recipientNodeId).toString();
    String myGNCI = ConnectionDelegate.getPostOffice().getMyGridNodeCommInfo();
    ChannelSendHeader header = getChannelSendHeader(gdoc, eventId, trxId, IDocumentEvents.GRIDDOC_SUB_EVENT, fileId);
    header.setGridnodeHeaderInfo(senderNodeId, recipientNodeId,myGNCI);
    header.setMessageHeaderInfo(gdoc.getUdocDocType(), null);
    return header;
  }
  
  //TWX 15112006 To include the tracingID in the ChannelSendHeader
  private static ChannelSendHeader getChannelSendHeader(GridDocument gdoc, String eventId, String trxId, String subID, String fileId)
  {
    Logger.log("[ChannelDelegate.getChannelSendHeader]");
    ChannelSendHeader header = new ChannelSendHeader(eventId, trxId, subID, fileId);
    
    if(gdoc != null)
    {
      header.setOnlineTrackingHeaderInfo(gdoc.getTracingID());
    }
    return header;
  }
  
  /**
   * This method will upload the files for another GridTalk to the GridMaster.
   *
   * @param channelInfo The ChannelInfo of the gridnode to send the
   *                   files to.
   * @param dataToSend The String array containing the information to be sent.
   * @param filesToSend The file array containing the files to be sent.
   * @param header ChannelSendHeader containing the header information for sending
   * @param eventId The EventId of the upload event
   * @param sendGdocID GridDoc ID of Sender's document for use as transaction Id for
   * the event.
   * @param senderNodeID Node ID of this GridTalk's GridNode
   * @param recipientNodeID Node ID of the target recipient of the document.
   *
   * @since 2.0 I7
   */
  public static void upload(ChannelInfo channelInfo,
                          String[] dataToSend,
                          File[] filesToSend,
                          ChannelSendHeader header)
                          //String eventId,
                          //String senderGdocID,
                          //String senderNodeID,
                          //String recipientNodeID)
                          throws Exception
  {
    /*031001NSL
    String trxId = senderGdocID;

    String fileId = new StringBuffer("upload_E").append(eventId).append("_T").append(
                      trxId).append("_S").append(senderNodeID).append("_R").append(
                      recipientNodeID).toString();

    PostInstruction post = new PostInstruction();
    post.setDataPayload(dataToSend);
    post.setEventID(eventId);
    post.setFileID(fileId);
    post.setFilePayload(filesToSend);
    post.setRecipientChannel(channelInfo);
    post.setRecipientNodeID(recipientNodeID);
    post.setSenderNodeID(senderNodeID);
    post.setTransID(trxId);
    post.setEventSubID(IDocumentEvents.GRIDDOC_SUB_EVENT);
    */
    PostInstruction post = new PostInstruction();
    post.setDataPayload(dataToSend);
    post.setFilePayload(filesToSend);
    post.setRecipientChannel(channelInfo);
    post.setHeader(header);
    ConnectionDelegate.getPostOffice().dropToGridMasterPostOffice(post);
  }

  public static void sendToGridMaster(String[] dataToSend,
                                      String eventId,
                                      String trxId)
                                      throws Exception
  {
    PostInstruction post = new PostInstruction();
    post.setDataPayload(dataToSend);
    post.setEventID(eventId);
    post.setTransID(trxId);

    ConnectionDelegate.getPostOffice().dropToGridMasterPostOffice(post);
  }

  public void handlerFeedback(String[] header, boolean success, String message) throws Throwable
  {
    try
    {
      Logger.log("[ChannelDelegate.handlerFeedback] "+
        "Received feedback for sent message for Gdoc "+header[ChannelSendHeader.TRANSACTION_ID_POS]+", status: "+
        success + ", feedback message: "+message);

      Long gdocID = new Long(header[ChannelSendHeader.TRANSACTION_ID_POS]);

      IDocumentManagerObj mgr = getDocumentManager();

      GridDocument gdoc = mgr.findGridDocument(GridDocument.FOLDER_OUTBOUND,
                                               gdocID);
      
      if (gdoc != null)
      { 
        //NSL20060905 Get the complete gdoc at the start
        gdoc = mgr.getCompleteGridDocument(gdoc);
        //Update the message digest and audit filename, if present
        if (header.length > ChannelSendHeader.COMMON_HEADER_SIZE )
        {
          gdoc.setMessageDigest(header[ChannelSendHeader.MIC]);
          String auditFileName = header[ChannelSendHeader.AUDIT_FILE_NAME];
          if(auditFileName!=null && auditFileName.trim().length()>0)
          {
            /* 200503288 Mahesh : set the audit file insted of appending to existing one
            String fileName = gdoc.getAuditFileName();
            if ((fileName != null) && (fileName.length() > 0))
              gdoc.setAuditFileName(fileName + ";" + auditFileName);
            else
            */
            gdoc.setAuditFileName(auditFileName);
          }
          Logger.debug("Setting MessageDigest to " + gdoc.getMessageDigest()+", AuditFileName to "+gdoc.getAuditFileName());            
        }
        if (!success)
        {
          gdoc.setDocTransStatus("Failed - " + message);
          //GridDocumentEntityHandler.getInstance().update(gdoc);
          //String gDocFullPath = FileHelper.getGdocFile(gdoc).getAbsolutePath();
          //gdoc.serialize(gDocFullPath);
          mgr.updateGridDocument(gdoc); //NSL20060905 Use document manager to update
          new PartnerFunctionFailure(
                //mgr.getCompleteGridDocument(gdoc),
                gdoc,
                PartnerFunctionFailure.TYPE_SEND_DOC_FAILURE,
                PartnerFunctionFailure.REASON_GENERAL_ERROR,
                new Exception(message)).raiseAlert();
        }
        else
        {
        	//NSL20060418 Only update the audit filename & message digest into gdoc if successful send
//          if (header.length > ChannelSendHeader.COMMON_HEADER_SIZE )
//          {
//            gdoc.setMessageDigest(header[ChannelSendHeader.MIC]);
//            String auditFileName = header[ChannelSendHeader.AUDIT_FILE_NAME];
//            if(auditFileName!=null && auditFileName.trim().length()>0)
//            {
//              /* 200503288 Mahesh : set the audit file insted of appending to existing one
//              String fileName = gdoc.getAuditFileName();
//              if ((fileName != null) && (fileName.length() > 0))
//                gdoc.setAuditFileName(fileName + ";" + auditFileName);
//              else
//              */
//              gdoc.setAuditFileName(auditFileName);
//            }
//            Logger.debug("Setting MessageDigest to " + gdoc.getMessageDigest()+", AuditFileName to "+gdoc.getAuditFileName());            
//          }

          //GridDocumentEntityHandler.getInstance().update(gdoc);
          //String gDocFullPath = FileHelper.getGdocFile(gdoc).getAbsolutePath();
          //gdoc.serialize(gDocFullPath);
          mgr.updateGridDocument(gdoc); //NSL20060905 Use document manager bean to update
          
          // only check if direct send
          if (IDocumentEvents.SEND_GRIDDOC.equals(header[ChannelSendHeader.EVENT_ID_POS]))
          {
            boolean handleDocSent = true;

            if (gdoc.getRecipientNodeId() != null)
            {
              // check channel if GT recipient
              //gdoc = mgr.getCompleteGridDocument(gdoc);
              if (ICommInfo.JMS.equals(gdoc.getRecipientChannelProtocol()))
              {
                ChannelInfo channelInfo = getChannelInfo(gdoc.getRecipientChannelUid());
                // only if not sent using default master channel
                handleDocSent = !channelInfo.isMaster();
              }
              // else wait for ACK
            }
            // not GT recipient, handleDocSent

            if (handleDocSent)
              mgr.handleDocSent(gdoc);
          }
          // else upload --> do not handleDocSent here, wait for ACK.
        }
        sendDocumentDeliverStatus(gdoc, success, message, false);
      }
      else
        Logger.warn("[ChannelDelegate.handlerFeedback] No such Gdoc in Outbound: "+gdocID);
    }
    catch (Throwable ex)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  throw ex;
      }
      Logger.warn(
        "[ChannelDelegate.handlerFeedback] Error processing received feedback",
        ex);
    }
  }
  
  /**
   * TWX 16112006 Notify the OTC-plugin the deliver status of the doc. If the deliver status is successful, the file path
   * to the payload (the audit file) will be included.
   * @param gdoc 
   * @param isDeliveredSuccess Indicate whether the delivery of the doc is successful or not.
   * @param message The error msg. Not empty if the isDeliveredSuccess is true.
   * @param isRequiredPack To indicate the OTC-plug-in whether we need to package the doc
   */
  private void sendDocumentDeliverStatus(GridDocument gdoc, boolean isDeliveredSuccess, String message, boolean isRequiredPack) throws SystemException
  {
    String auditAbsPath = "";
    String docFlowAddInfo = "";
    File auditFile = null;
    
    if(isDeliveredSuccess)
    {
      try
      {
        docFlowAddInfo = message; //the message contain the http respnse code
        message = "";
        
        auditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, gdoc.getAuditFileName());
        if(auditFile != null)
        {
          auditAbsPath = auditFile.getAbsolutePath();
        }
      }
      catch(Exception ex)
      {
        Logger.warn("[ChannelDelegate.sendDocumentDeliverStatus] The auditFile "+gdoc.getAuditFileName()+" can't be located.", ex);
      }
    }
    
    if(!isDeliveredSuccess || (isDeliveredSuccess && auditFile != null))
    {
      //  TWX 16112006 Trigger the deliver status of the doc to OTC plug-in
      DocumentFlowNotifyHandler.triggerNotification(EDocumentFlowType.DOCUMENT_DELIVERED, new Date(),gdoc.getFolder(), gdoc.getGdocId(), isDeliveredSuccess,message,  
                                                  gdoc.getTracingID(), gdoc.getUdocDocType(), auditAbsPath, gdoc.getRecipientBizEntityId(), 
                                                  gdoc.getSenderBizEntityId(), null, isRequiredPack, docFlowAddInfo, (Long)gdoc.getKey(), gdoc.getSrcFolder(), null);
    }
    else
    {
      ApplicationException ex = new ApplicationException("The auditFile "+gdoc.getAuditFileName()+" can't be located. GDocId: "+gdoc.getGdocId()+", folder: "+gdoc.getFolder());
      Logger.error(ILogErrorCodes.GT_GENERIC_DELIVERING_NOTIFICATION_ERROR,"[ChannelDelegate.sendDocumentDeliverStatus] Error in sending the DocumentFlowNotification: "+ex.getMessage(), ex);
    }
  }
  
  public IDocumentManagerObj getDocumentManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }
}