/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 02 2002    Koh Han Sing        Created
 * Jan 14 2003    Neo Sok Lay         Upload doc logic
 * Mar 27 2003    Koh Han Sing        Delete temp files
 * Apr 03 2003    Goh Kan Mun         Update header when invoking upload process.
 * May 07 2003    Neo Sok Lay         Raise alert at points that forbids sending
 *                                    to proceed: unknown destination, unknown partner,
 *                                    partner not enabled, license invalid, other
 *                                    general errors.
 * Jun 20 2003    Neo Sok Lay         Use DocumentStatusBroadcaster when start end
 *                                    instead of direct using Notifier.
 * Oct 01 2003    Neo Sok Lay         Modify sendOrUpload(), prepareSendingInfo()
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Oct 22 2003    Guo Jianyu          Modified prepareSendingInfo() such that the
 *                                    griddocument's recipient channel, if not null,
 *                                    won't be overwritten with the default channel.
 * Oct 29 2003    Guo Jianyu          Added preSend() to invoke the message handler
 *                                    for preprocessing of outgoing messages.
 * Jan 08 2004    Jagadeesh           Added : Support for transformation of GDoc 2.x -> GDoc 1.x
 * Jan 29 2004    Neo Sok Lay         Added: Support for resume send.
 * Mar 17 2004    Jagadeesh           Modified : To support Upload of RNIF Messages to GM.
 * Apr 13 2004    Guo Jianyu          Added resend() to invoke transport level resends
 * Jun 07 2004    Neo Sok Lay         Modified: getAttachmentFiles() should not use casting
 *                                    to Long from attachmentUids list. During resume (attachments
 *                                    have gone thru for some docs sent but some suspended),
 *                                    the list items retrieved is not Long type (could be Integer type). 
 * Jun 30 2004    Neo Sok Lay         GNDB00025083: Do not resume send RN docs if no 
 *                                    RnProfileUid or ProcessInstanceUid present,
 *                                    instead treat it as new send.
 * Jul 30 2004    Neo Sok Lay         GNDB00025179: Should not set the partner default channel into Gdoc
 *                                    if processdef is set (checking rnprofileuid may not be accurate).
 * Nov 21 2005    Neo Sok Lay         Change getRouteLevel(): return ROUTE_DIRECT instead of -1 for other cases.       
 * Apr 03 2006    Neo Sok Lay         Change doSend(): raise alert and fail upfront if partner function failure already
 *                                    determined.        
 * Apr 17 2006    Neo Sok Lay         Change sendOrUpload(): update document resend timer for AS2        
 * Aug 30 2006    Neo Sok Lay         GNDB00027767: Set Payload filename in send headers.     
 * Jan 05 2007    Tam Wei Xiang       Trigger the event of CHANNEL_Connectivity for OTC.                                         
 * Jan 19 2007    Neo Sok Lay         Use common Notifier to broadcast notifications.
 * Jul 25 2008    Tam Wei Xiang       #69: Modified method sendDocumentFlowNotification(..)
 *                                         to indicate whether to fire the Notification under new 
 *                                         transaction.                                               
 */

package com.gridnode.gtas.server.document.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.exceptions.PartnerNotEnabledException;
import com.gridnode.gtas.server.document.exceptions.PartnerNotFoundException;
import com.gridnode.gtas.server.document.handlers.AS2MessageHandler;
import com.gridnode.gtas.server.document.model.*;
import com.gridnode.gtas.server.document.notification.DocumentTransactionHandler;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.SendRNDocNotification;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.ObjectXmlSerializer;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * This class provides sending services.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class SendDocumentHelper implements IDocProcessingErrorConstants
{
  public static final String EXIT_TO_CHANNEL = "Exit to Channel";

  protected transient PartnerFunctionFailure _failure = null;

  public GridDocument prepareSendingInfo(GridDocument gdoc)
  //    throws Exception
  {
    try
    {
      Logger.debug("[SendDocumentHelper.prepareSendingInfo] Enter");
      if (gdoc.getRecipientPartnerId() != null
        && gdoc.getRecipientPartnerId().trim().length() > 0)
      {
        PartnerInfo pInfo =
          BizRegDelegate.getPartnerInfo(gdoc.getRecipientPartnerId(), true);
        gdoc.setRecipientPartnerName(pInfo.getPartnerName());
        gdoc.setRecipientPartnerGroup(pInfo.getPartnerGroup());
        gdoc.setRecipientPartnerType(pInfo.getPartnerType());
        gdoc.setRecipientBizEntityId(pInfo.getBizEntityID());
        gdoc.setRecipientNodeId(pInfo.getNodeID());
        gdoc.setRecipientBizEntityUuid(pInfo.getBizEntityUuid());
        gdoc.setRecipientRegistryQueryUrl(pInfo.getRegistryQueryUrl());

        //if (gdoc.getRnProfileUid() == null)
        if (gdoc.getProcessDefId() == null || gdoc.getProcessDefId().length()==0) //this is much more accurate
        {
          if (gdoc.getRecipientChannelUid() == null)
          {
            DocChannelInfo cInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gdoc.getRecipientPartnerId());
            if (cInfo != null)
            {
              gdoc.setRecipientChannelUid(cInfo.getChannelUid());
              gdoc.setRecipientChannelName(cInfo.getChannelName());
              gdoc.setRecipientChannelProtocol(cInfo.getChannelProtocol());
            }
          }
        }
      }
      else
      {
        _failure =
          new PartnerFunctionFailure(
            TYPE_SEND_DOC_FAILURE,
            REASON_UNKNOWN_DESTINATION,
            NO_RECIPIENT_PARTNER);
      }
    }
    catch (PartnerNotFoundException ex)
    {
      _failure =
        new PartnerFunctionFailure(
          TYPE_SEND_DOC_FAILURE,
          REASON_UNKNOWN_PARTNER,
          ex);
    }
    catch (PartnerNotEnabledException ex)
    {
      _failure =
        new PartnerFunctionFailure(
          TYPE_SEND_DOC_FAILURE,
          REASON_PARTNER_NOT_ENABLED,
          ex);
    }
    catch (Throwable t)
    {
      _failure =
        new PartnerFunctionFailure(
          TYPE_SEND_DOC_FAILURE,
          REASON_GENERAL_ERROR,
          t);
    }
    finally
    {
      Logger.debug("[SendDocumentHelper.prepareSendingInfo] Exit");
    }
    return gdoc;
  }

  public void doSend(GridDocument gdoc) throws Throwable
  {
    doSend(gdoc, false);
  }

  public void doSend(GridDocument gdoc, boolean isResumeSend) throws Throwable
  {
  	//NSL20060403 Fail upfront if failure already determined
  	if (_failure != null)
  	{
      ////TWX 08012007 Indicate the Channel Connectivity Event
      sendDocumentFlowNotification(gdoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, false, _failure.getException().getMessage(), new Date(), null);
  		_failure.raiseAlert(gdoc);
  		throw _failure.getException();
  	}

    try
    {
      Logger.log("[SendDocumentHelper.doSend] Start, isResumeSend="+isResumeSend);
      gdoc = updateUdocFilename(gdoc);
      Logger.debug("[SendDocumentHelper.doSend] send gdoc, processDefId="+gdoc.getProcessDefId());
      if ((gdoc.getProcessDefId() != null)
        && (!gdoc.getProcessDefId().equals("")))
      {
        
        boolean isSentSuccess = true;
        String errMsg = "";
        try
        {
          //040630NSL: Don't resume send if no rnprofileuid or processinstanceuid
          //RN_ACK / RN_EXCEPTION will have both rnprofileuid and processinstanceuid?
          if (isResumeSend)
          {
            if (gdoc.getProcessInstanceUid() == null)
            {
              if (gdoc.getRnProfileUid() == null)
                isResumeSend = false;
              else
              {
                //should be old docs created before v2.3, ignore
                Logger.log("[SendDocumentHelper.doSend] Old RNdoc, ignore resend");
                return;
              }
            }
            else
              isResumeSend = gdoc.getRnProfileUid() != null;
          }
          if (!isResumeSend)
            Logger.log("[SendDocumentHelper.doSend] "+
              "RosettaNet document but no RnProfileUid/ProcessInstanceUid, not re-send but new send.");
          Logger.log(
            "[SendDocumentHelper.doSend] RosettaNet document, notifying RosettaNet");
          Object[] gdocs = { gdoc };
          SendRNDocNotification notification =
            new SendRNDocNotification(
                                      gdocs,
                                      gdoc.getProcessDefId(),
                                      gdoc.isRequest(),
                                      isResumeSend);
          //SendRNDocNotifier.getInstance().broadcast(notification);
          
          if(! JMSRetrySender.isSendViaDefMode()) //TWX 23 Nov 2007 Enable jms retry if encounter error*
          {
            JMSRetrySender.broadcast(notification);
          }
          else
          {
            Notifier.getInstance().broadcast(notification); //NSL20070119
          }
        }
        catch(Exception ex) //TWX 08012006
        {
          isSentSuccess = false;
          errMsg = ex.getMessage();
          sendDocumentFlowNotification(gdoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, isSentSuccess, errMsg, new Date(), null); //TWX 08012007 Indicate the Channel Connectivity Event
          throw ex;
        }
      }
      else
      {
        Logger.log("[SendDocumentHelper.doSend] Normal send");
        
        DocumentTransactionHandler.triggerDocumentTransaction(gdoc, false, false); //10012007 TWX
        send(gdoc);
      }
    }
    catch (Throwable t)
    { 
      Logger.warn(
        "[SendDocumentHelper.doSend] Unable to send " + t.getMessage());

      throw t.fillInStackTrace();
    }
  }

  public void resend(GridDocument gDoc, String channelUID) throws Throwable
  {
    String exMsg = "";
    boolean isChannelConnectivitySuccess = true;
    try
    {
      if (channelUID == null)
      {
        DocChannelInfo cInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gDoc.getRecipientPartnerId());
        if (cInfo != null)
          channelUID = cInfo.getChannelUid().toString();
        else
          throw new Exception("No default channel found for gDoc with UID="
                              + gDoc.getKey());
      }

      //It should be determined here the type of the gDoc, e.g. AS2 or RosettaNet
      //But gDoc lacks a field to indicate that information. For now, simply invoke
      //AS2 resend().
      (new AS2MessageHandler()).resend(gDoc, channelUID);
    }
    catch(Throwable th)
    {
      isChannelConnectivitySuccess = false;
      exMsg = th.getMessage();
      throw th;
    }
    finally
    {
      //TWX 08012007 Indicate the Channel Connectivity Event
      sendDocumentFlowNotification(gDoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, isChannelConnectivitySuccess, exMsg, new Date(), null);
    }
  }
  
  public void send(GridDocument gdoc) throws Throwable
  {
    try
    {
      if (_failure == null)
      {
        if (gdoc.getRecipientChannelUid() != null)
        {
          sendViaChannel(gdoc);
        }
        else
        {
          // raise alert
          _failure =
            new PartnerFunctionFailure(
              TYPE_SEND_DOC_FAILURE,
              REASON_UNKNOWN_DESTINATION,
              NO_RECIPIENT_CHANNEL);
        }
      }
    }
    catch (FindEntityException ex)
    {
      _failure =
        new PartnerFunctionFailure(
          TYPE_SEND_DOC_FAILURE,
          REASON_UNKNOWN_DESTINATION,
          ex);
    }
    catch (PartnerFunctionFailure fail)
    {
      _failure = fail;
    }
    catch (Throwable t)
    {
      _failure =
        new PartnerFunctionFailure(
          TYPE_SEND_DOC_FAILURE,
          REASON_GENERAL_ERROR,
          t);
    }

    String exMsg = "";
    boolean isChannelConnectivitySuccess = true;
    if (_failure != null)
    {
      exMsg = _failure.getException().getMessage();
      isChannelConnectivitySuccess = false;
      _failure.raiseAlert(gdoc);
      throw _failure.getException();
    }
    //  TWX 08012007 Indicate the Channel Connectivity Event
    sendDocumentFlowNotification(gdoc, EDocumentFlowType.CHANNEL_CONNECTIVITY, isChannelConnectivitySuccess, exMsg, new Date(), null);
  }

  private void sendViaChannel(GridDocument gdoc) throws Throwable
  {
    Logger.debug("[SendDocumentHelper.sendViaChannel] Start");

    boolean sendDirect = true;
    boolean upload = false;

    String recipient = null, sender = null;
    if (gdoc.getRecipientNodeId() != null)
      recipient = gdoc.getRecipientNodeId().toString();
    sender = gdoc.getSenderNodeId().toString();

    ChannelInfo channel =
      ChannelDelegate.getChannelInfo(gdoc.getRecipientChannelUid());

    Activity newActivity = new Activity();
    newActivity.setActivityType(EXIT_TO_CHANNEL);
    newActivity.setDescription(EXIT_TO_CHANNEL + " " + channel.getName());
    newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
    gdoc.addActivity(newActivity);

    if (recipient != null) // GridTalk partner
    {
      //isDefaultTpt is to check for upload of RNIF Messages., snice we use Master Channel Comm Profile.
      if (channel.isMaster() || channel.getTptCommInfo().isDefaultTpt())
      {
        if (ConnectionDelegate.isOnline())
        {
          if (!ConnectionDelegate.isPartnerOnline(recipient))
          {
            Logger.debug(
              "[SendDocumentHelper.sendViaChannel][Uploading to GridMaster]"
                + gdoc.getRecipientPartnerId());
            sendDirect = false;
            upload = !isLocalPending(gdoc);
          }
          // else send direct
        }
        else
        {
          sendDirect = false;
          upload = false;
        }
      }
      // else send direct
    }
    // else send direct

    // check license, send only if valid
    if (RegistrationDelegate.hasValidLicense())
    {
      if (sendDirect)
      {
        Logger.debug(
          "[SendDocumentHelper.sendViaChannel] Sending directly to Partner "
            + gdoc.getRecipientPartnerId());

        sendOrUpload(
          gdoc,
          GridDocument.ROUTE_DIRECT,
          false,
          channel,
          sender,
          recipient);
      }
      else if (upload)
      {
        Logger.debug(
          "[SendDocumentHelper.sendViaChannel] Uploading to GridMaster for Partner "
            + gdoc.getRecipientPartnerId());
        sendOrUpload(
          gdoc,
          GridDocument.ROUTE_GRIDMASTER,
          true,
          channel,
          sender,
          recipient);
      }
      //else local pend
      else
      {
        Logger.debug(
          "[SendDocumentHelper.sendViaChannel] GridTalk is either not online or the document must be local pend.");
        updateHeader(gdoc);
      }
    }
    else
    {
      Logger.debug(
        "[SendDocumentHelper.sendViaChannel] License not valid, document not send");
      updateHeader(gdoc);

      throw new PartnerFunctionFailure(
        TYPE_SEND_DOC_FAILURE,
        REASON_LICENSE_INVALID,
        SEND_DISALLOWED);
    }

    Logger.debug("[SendDocumentHelper.sendViaChannel] End");
  }

  /**
   *
   * @param gdoc
   * @param route
   * @param isUpload
   * @param channel
   * @param sender
   * @param recipient
   * @throws java.lang.Throwable
   */


  private void sendOrUpload(
    GridDocument gdoc,
    String route,
    boolean isUpload,
    ChannelInfo channel,
    String sender,
    String recipient)
    throws Throwable
  {
    String[] moreHeaders = null;

    gdoc.setSenderRoute(route);
    gdoc.setDateTimeSendStart(TimeUtil.localToUtcTimestamp());
    if (isUpload == false)
      moreHeaders = preSend(gdoc, channel);

    updateHeader(gdoc);
    File[] filesToSend = prepareFilesToSend(gdoc,getRouteLevel(channel,isUpload));
//    File[] filesToSend = prepareFilesToSend(gdoc,isBackwardCompatible(channel));
    String[] dataToSend = prepareDataToSend(gdoc, isUpload);

    /*031001NSL
    if (isUpload)
      ChannelDelegate.upload(channel, dataToSend, filesToSend,
          IDocumentEvents.UPLOAD_GRIDDOC, gdoc.getGdocId().toString(),
          sender, recipient);
    else
    {
      ChannelDelegate.send(channel, dataToSend, filesToSend,
          IDocumentEvents.SEND_GRIDDOC, gdoc.getGdocId().toString(),
          sender, recipient, moreHeaders);
    }
    */
    
    //NSL20060417 update the retry timer with OB gdoc info
    DocumentResendTimerHelper.updateTimer(gdoc, channel);
    
    if (isUpload)
    {
      ChannelDelegate.upload(
        channel,
        dataToSend,
        filesToSend,
        ChannelDelegate.prepareUploadHeader(
          IDocumentEvents.UPLOAD_GRIDDOC,
          gdoc.getGdocId().toString(),
          gdoc,
          sender,
          recipient));
    }
    else
    {
      ChannelSendHeader sendHeaders = ChannelDelegate.prepareSendHeader(
          IDocumentEvents.SEND_GRIDDOC,
          gdoc.getGdocId().toString(),
          gdoc,
          sender,
          recipient);
      PackagingInfo packagingInfo = channel.getPackagingProfile();
      if (packagingInfo != null)
      {
        String envelope = packagingInfo.getEnvelope();
        if (envelope != null)
        {
          if (envelope.equals(PackagingInfo.AS2_ENVELOPE_TYPE))
          {
            sendHeaders.setAS2Headers(moreHeaders[0], moreHeaders[1],moreHeaders[2],
              moreHeaders[3],moreHeaders[4],moreHeaders[5],moreHeaders[6],
              moreHeaders[7], moreHeaders[8], null, null, moreHeaders[9]); //NSL20060830
          }
        }
      }
      ChannelDelegate.send(
        channel,
        dataToSend,
        filesToSend,
        sendHeaders);
    }
    DocumentStatusBroadcaster.documentSendStart(gdoc);

    /*030620NSL
    Notifier.getInstance().broadcast(
      new DocumentActivityNotification(
          DocumentActivityNotification.TYPE_START_SEND,
          gdoc));
    */
  }

  protected int getRouteLevel(ChannelInfo channelInfo,boolean isUpload)
  {
    Logger.log("[## isUpload]["+isUpload+"]");
    if (channelInfo.getTptCommInfo().getTptImplVersion().startsWith("02"))
      return IDocumentConstants.ROUTE_GT1_GM;
    if (isUpload) //If only on upload we check if we need to send to GM,applys only for RNIF
    {
      if (channelInfo.getTptCommInfo().isDefaultTpt())
        return IDocumentConstants.ROUTE_GM;
    }
    //return -1;
    return IDocumentConstants.ROUTE_DIRECT;
  }
  /*
  private boolean isBackwardCompatible(ChannelInfo channel)
  {
    if (channel == null)
      return false;
    else
    {
      return channel.getTptCommInfo().getTptImplVersion().startsWith("02");
    }
  }*/

  protected boolean isLocalPending(GridDocument gdoc)
  {
    return gdoc.isLocalPending().booleanValue();
  }

  protected String[] prepareDataToSend(GridDocument gdoc, boolean sendGdocId)
  {
    String[] data =
      {
        sendGdocId ? gdoc.getGdocId().toString() : null,
        null // for backward compatible
    };

    return data;
  }

  /**
   * Since transformation of GridDocument is required only for GridTalk Standard Messages,
   * other Message Standards ex: RNIF dose not need to Send a GridDoc across.
   *
   * The current implementation for RNIF Send, dose not send GridDoc, in both
   * GridTalk 1.x series and GridTalk2.0 series.
   *
   * This method is overridden by RNDocSender of RNIF Module.
   *
   * @changed since 2.3
   *
   * @param gdoc
   * @return
   * @throws Exception
   */
//  protected File[] prepareFilesToSend(GridDocument gdoc,boolean isTransformationRequired)
  protected File[] prepareFilesToSend(GridDocument gdoc,int route_lvl)
      throws Exception
  {
  //protected File[] prepareFilesToSend(GridDocument gdoc) throws Exception
  //{
    ArrayList<File> filesList = new ArrayList<File>();
    String gdocFilename = gdoc.getGdocFilename();
    String udocFilename = gdoc.getUdocFilename();

    File udocFile = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC,
                                     gdoc.getFolder()+File.separator,
                                     udocFilename);
    filesList.add(udocFile);

    File gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC,
                                     gdoc.getFolder()+File.separator,
                                     gdocFilename);
    if (route_lvl == IDocumentConstants.ROUTE_GT1_GM)
    {
      gdocFile = TransformationHelper.transformDocument(gdocFile,TransformationHelper.OUTBOUND_TRANSFORMATION);
      //performTransformation(gdocFile);
      filesList.add(gdocFile);
      filesList.add(null);  //14/01/04 Jagadeesh.
    }
    else
      filesList.add(gdocFile);


    filesList = getAttachmentFiles(gdoc, filesList);

    File[] filesToSend = convertToFileArray(filesList);
    return filesToSend;
  }
  /*
  private File performTransformation(File gdocFile)
    throws Exception
  {
    try
    {
      File tGdoc = getTransformedGDocFile(getOutBoundMappingFileName(),gdocFile);
      return (tGdoc == null ? gdocFile : tGdoc);
    }
    catch(XMLException ex)
    {
      Logger.err("[SendDocumentHelper][performTransformation()][Could Not perform BackwardCompatible Transformation]",ex);
      throw new Exception("[SendDocumentHelper][prepareFilesToSend][Could Not perform BackwardCompatible Transformation]\n"+ex.getMessage());
    }
    catch(FileAccessException fileEx)
    {
      Logger.err("[SendDocumentHelper][performTransformation()]"+
      "[Could Not Transform BackwardCompatible GridDocument with FileName]"+getOutBoundMappingFileName(),fileEx);
    }
    catch(ServiceLookupException slEx)
    {
      Logger.err("[SendDocumentHelper][performTransformation()][Could Not Lookup XMLServiceFacade]",slEx);
      throw new Exception("[Could Not Lookup XMLServiceFacade]"+
        slEx.getMessage());
    }
    return null;

  }*/
  /*
  private File getTransformedGDocFile(String mappingFileName,File gdocFile)
    throws XMLException,FileAccessException,ServiceLookupException
  {

      File xslFile = FileUtil.getFile(IMapperPathConfig.PATH_XSL,mappingFileName);
      File transformedGdoc = XMLDelegate.getManager().transform(
              xslFile.getAbsolutePath(),
              gdocFile.getAbsolutePath()
              );
      if (transformedGdoc == null)
        throw new XMLException("[BackwardCompatible Transformation Unsuccessful]");
      else
        return transformedGdoc;
  }

  private String getOutBoundMappingFileName()
  {
    return getDocumentConfig().getString(
      IDocumentConfig.BACKWARDCOMPATIBLE_OUTBOUND_MAPPING);
  }

  private Configuration getDocumentConfig()
  {

     return ConfigurationManager.getInstance().getConfig(
                          IDocumentConfig.CONFIG_NAME);
  }*/


  protected ArrayList<File> getAttachmentFiles(
    GridDocument gdoc,
    ArrayList<File> filesList)
    throws Exception
  {
    if (gdoc.hasAttachment().booleanValue())
    {
      List attachmentUids = gdoc.getAttachments();
      if (!attachmentUids.isEmpty())
      {
        File attInfoFile = getInfoFile(gdoc);
        filesList.add(attInfoFile);
        boolean isSendInitiated =
          AttachmentHelper.isAttachmentSent(
            new Long(attachmentUids.get(0).toString()), //use casting may not work during resume
            gdoc.getRecipientPartnerId());
        if (!isSendInitiated)
        {
          AttachmentHelper.register(
            new Long(attachmentUids.get(0).toString()), //use casting may not work during resume
            gdoc.getRecipientPartnerId());

          for (Iterator i = attachmentUids.iterator(); i.hasNext();)
          {
            Long attachmentUid = new Long(i.next().toString());
            Attachment attachment = null;
            try
            {
              attachment =
                (Attachment) AttachmentEntityHandler
                  .getInstance()
                  .getEntityByKey(
                  attachmentUid);
            }
            catch (Throwable ex)
            {
              throw new ApplicationException(
                "Unable to retrieve attachment "
                  + attachmentUid
                  + " from database",
                ex);
            }

            try
            {
              File attFile =
                FileUtil.getFile(
                  IDocumentPathConfig.PATH_ATTACHMENT,
                  attachment.getFilename());
              filesList.add(attFile);
            }
            catch (Exception ex)
            {
              throw new ApplicationException(
                "Unable to find attachment file "
                  + attachment.getFilename()
                  + " in attachment directory",
                ex);
            }
          }
        }
        else if (gdoc.getTriggerType() == GridDocument.TRIGGER_MANUAL_SEND)
        {
          for (Iterator i = attachmentUids.iterator(); i.hasNext();)
          {
            Long attachmentUid = new Long(i.next().toString());
            Attachment attachment =
              (Attachment) AttachmentEntityHandler
                .getInstance()
                .getEntityByKeyForReadOnly(
                attachmentUid);
            File attFile =
              FileUtil.getFile(
                IDocumentPathConfig.PATH_ATTACHMENT,
                attachment.getFilename());
            filesList.add(attFile);
          }
        }
      }
    }
    return filesList;
  }

  protected File getInfoFile(GridDocument gdoc) throws Exception
  {
    try
    {
      AttachmentInfo attInfo = new AttachmentInfo();
      List attachmentUids = gdoc.getAttachments();
      for (Iterator i = attachmentUids.iterator(); i.hasNext();)
      {
        Long uid = new Long(i.next().toString());
        Attachment attachment =
          (Attachment) AttachmentEntityHandler
            .getInstance()
            .getEntityByKeyForReadOnly(
            uid);
        attInfo.add(attachment);
      }
      ObjectXmlSerializer serializer = new ObjectXmlSerializer();
      File attInfoFile = File.createTempFile(generateRandomFileName(), ".info");
      Logger.debug(
        "[SendDocumentHelper.getInfoFile] InfoFilename = "
          + attInfoFile.getAbsolutePath());
      serializer.serialize(attInfo, attInfoFile.getAbsolutePath());
      return attInfoFile;
    }
    catch (IOException ioEx)
    {
      throw new ApplicationException(
        "Unable to create attachment info file",
        ioEx);
    }
  }

  protected File[] convertToFileArray(ArrayList filesList)
  {
    File[] filesToSend = new File[filesList.size()];
    for (int i = 0; i < filesList.size(); i++)
    {
      filesToSend[i] = (File) filesList.get(i);
    }
    return filesToSend;
  }

  protected void updateHeader(GridDocument gdoc) throws Exception
  {
    try
    {
      Logger.debug("[SendDocumentHelper.updateHeader] Start");

      //String udocFilename = gdoc.getUdocFilename();
      String tempUdocFilename = gdoc.getTempUdocFilename();

      if (!tempUdocFilename.equals(""))
      {
        gdoc.setTempUdocFilename("");
        File udoc = FileHelper.getUdocFile(gdoc);
        gdoc.setUdocFullPath(udoc.getAbsolutePath());
      }

      GridDocumentEntityHandler.getInstance().update(gdoc);
      File tempGdoc = File.createTempFile(generateRandomFileName(), ".xml");
      String gdocFullPath = tempGdoc.getAbsolutePath();
      gdoc.serialize(gdocFullPath);
      FileUtil.delete(IDocumentPathConfig.PATH_GDOC,
                      gdoc.getFolder()+File.separator,
                      gdoc.getGdocFilename());
      FileUtil.create(IDocumentPathConfig.PATH_GDOC,
                      gdoc.getFolder()+File.separator,
                      gdoc.getGdocFilename(),
                      new FileInputStream(tempGdoc));

      if (!tempUdocFilename.equals(""))
      {
        gdoc.setTempUdocFilename(tempUdocFilename);
        File udoc = FileHelper.getUdocFile(gdoc);
        gdoc.setUdocFullPath(udoc.getAbsolutePath());
      }

      tempGdoc.delete(); //KHS20030327
      Logger.debug("[SendDocumentHelper.updateHeader] End");
    }
    catch (Throwable t)
    {
      Logger.warn("Unable to update GridDocument", t);
      throw new Exception(t.getMessage());
    }
  }

  protected GridDocument updateUdocFilename(GridDocument gdoc) throws Exception
  {
    String tempUdocFilename = gdoc.getTempUdocFilename();
    if (!tempUdocFilename.equals(""))
    {
      gdoc.setTempUdocFilename("");
      File udoc = FileHelper.getUdocFile(gdoc);
      gdoc.setUdocFullPath(udoc.getAbsolutePath());
    }
    return gdoc;
  }

  protected String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt());
    //NSL20070312 Ensure uniqueness
    return UUIDUtil.getRandomUUIDInStr();
  }

  /**
   * This method invokes the appropriate message handler based on the envelope type
   * of the packaging profile which is embedded in the channelInfo.
   */
  public String[] preSend(GridDocument gDoc, ChannelInfo channelInfo)
  {
    PackagingInfo packagingInfo = channelInfo.getPackagingProfile();
    if (packagingInfo == null) return null;

    String envelope = packagingInfo.getEnvelope();
    if (envelope == null) return null;

    if (envelope.equals(PackagingInfo.AS2_ENVELOPE_TYPE))
    { // invoke AS2 handler
      return new AS2MessageHandler().preSend(gDoc, channelInfo);
    }
    else
    { //expand this part if message handlers are implemented for other envelope types
    }
    return null;
  }
  
  private void sendDocumentFlowNotification(GridDocument gdoc, EDocumentFlowType docFlowType, boolean isDocumentFlowSucess, String errReason,
                                            Date eventOccurTime, Throwable th) throws SystemException
  {
    DocumentFlowNotifyHandler.triggerNotification(docFlowType, eventOccurTime, gdoc.getFolder(), gdoc.getGdocId(), isDocumentFlowSucess, errReason,
                                                  gdoc.getTracingID(), gdoc.getUdocDocType(), gdoc.getSenderBizEntityId(), gdoc.getRecipientBizEntityId(),
                                                  "", (Long)gdoc.getKey(), gdoc.getSrcFolder(), th);
  }
}
