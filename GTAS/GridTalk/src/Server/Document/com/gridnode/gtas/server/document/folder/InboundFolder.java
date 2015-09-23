/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InboundFolder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Koh Han Sing        Created
 * Jan 15 2002    Neo Sok Lay         Make Send acknowledgement data payload
 *                                    backward compatible.
 *                                    Send acknowledgement even when duplicate
 *                                    (use the existing inbound doc to return
 *                                     acknowledgement).
 *                                    Send download acknowledgement.
 * Mar 25 2003    Goh Kan Mun         To send string value of timestamp instead
 *                                    of date for sending the ack.
 * May 21 2003    Neo Sok Lay         Throw PartnerFunctionFailure after saving
 *                                    the document if partner not found or not enabled.
 * Oct 02 2003    Neo Sok Lay         Ask ChannelDelegate to prepare the header array
 *                                    for sending acknowledgement.
 * Nov 16 2005    Tam Wei Xiang       modified method doEnter(gdoc)                                    
 */
package com.gridnode.gtas.server.document.folder;

import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.exceptions.PartnerNotEnabledException;
import com.gridnode.gtas.server.document.exceptions.PartnerNotFoundException;
import com.gridnode.gtas.server.document.helpers.*;
import com.gridnode.gtas.server.document.model.*;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.util.List;

public class InboundFolder extends SystemFolder
{
  public static final String FOLDER_NAME = "Inbound";
  private static final Long DEF_OB_IB_HEADER_MR_UID = new Long(2);

  private PartnerFunctionFailure _failure = null;

  public InboundFolder()
  {
  }

  public GridDocument doEnter(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[InboundFolder.doEnter] Start");

    String ackEventId = getAcknowledgementEvent(gdoc);
    String ackTrxId = gdoc.getReceiveTrxId();
    GridDocument received = findReceived(gdoc);

    //if (!doDuplicateCheck(gdoc))
    if (received == null)
    {
      gdoc.setFolder(getFolderName());
      //already set in ReceiveDocumentHelper
      //gdoc.setDateTimeReceiveEnd(new Date(TimeUtil.localToUtc()));

      try
      {
        PartnerInfo pInfo = BizRegDelegate.getPartnerInfo(gdoc.getSenderNodeId(),
                                                      gdoc.getSenderBizEntityId());
        gdoc.setSenderPartnerName(pInfo.getPartnerName());
        gdoc.setSenderPartnerGroup(pInfo.getPartnerGroup());
        gdoc.setSenderPartnerType(pInfo.getPartnerType());
        gdoc.setSenderPartnerId(pInfo.getPartnerID());
      }
      catch (PartnerNotFoundException ex)
      {
        _failure = new PartnerFunctionFailure(
                       PartnerFunctionFailure.TYPE_RECEIVE_DOC_FAILURE,
                       PartnerFunctionFailure.REASON_UNKNOWN_PARTNER,
                       ex);
      }
      catch (PartnerNotEnabledException ex)
      {
        _failure = new PartnerFunctionFailure(
                       PartnerFunctionFailure.TYPE_RECEIVE_DOC_FAILURE,
                       PartnerFunctionFailure.REASON_PARTNER_NOT_ENABLED,
                       ex);
      }

      gdoc = doDefaultHeaderTransform(gdoc);
      
      Logger.log("[Inbound] doEnter gdoc filename "+gdoc.getUdocFilename());
      //TWX: 16 NOV 2005 extract out the values of the elements from udoc and put inside gdoc
      super.extractUdocElementInfo(gdoc, FileHelper.getUdocFile(gdoc));
      
      Activity newActivity = new Activity();
      newActivity.setActivityType(EXIT_TO_FOLDER);
      newActivity.setDescription(EXIT_TO_FOLDER+" "+getFolderName());
      //newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
      newActivity.setDateTime(TimeUtil.localToUtcTimestamp());
      gdoc.addActivity(newActivity);

//      gdoc = createHeader(gdoc);
      gdoc = saveInFolder(gdoc);

      //doSendAck(gdoc);
      /*020120NSL Decide base on sender and eventId
      if (gdoc.getRnProfileUid() == null)
      {
        doSendAck(gdoc, getAcknowledgementEvent(gdoc), ackTrxId);
      }
      */
      doSendAck(gdoc, ackEventId, ackTrxId);

      // throw failure, if any
      if (_failure != null)
      {
        _failure.setDocument(gdoc);
        throw _failure;
      }
    }
    else
    {
      // the document has been received before
      // do not process, just send the acknowledgement
      doSendAck(received, ackEventId, ackTrxId);
    }
    Logger.debug("[InboundFolder.doEnter] End");
    return gdoc;
  }

  public Long getDefaultHeaderMappingRuleUID(GridDocument gdoc)
  {
    return DEF_OB_IB_HEADER_MR_UID;
  }

  public String getFolderName()
  {
    return FOLDER_NAME;
  }

  public String getGdocFilename(GridDocument gdoc)
    throws Exception
  {
    return gdoc.getSenderNodeId()+"-"+
           gdoc.getRecipientNodeId()+"-IB-"+
           gdoc.getGdocId()+".xml";
  }

  protected GridDocument checkAttachmentStatus(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[InboundFolder.checkAttachmentStatus] Start");
    if (gdoc.hasAttachment().booleanValue())
    {
      if (!gdoc.isAttachmentLinkUpdated().booleanValue())
      {
        Logger.debug("[InboundFolder.checkAttachmentStatus] Attachments not updated");
        List newUids = AttachmentHelper.findIncomingAttachments(gdoc, gdoc.getSenderPartnerId());
        if (!newUids.isEmpty())
        {
          Logger.debug("[InboundFolder.checkAttachmentStatus] Updating attachments");
          gdoc.setAttachments(newUids);
          gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
        }
      }
      else
      {
        // Update all griddocuments
        AttachmentHelper.updateAttachmentLinks(gdoc.getSenderPartnerId());
      }
    }
    Logger.debug("[InboundFolder.checkAttachmentStatus] End");
    return gdoc;
  }

  /*
  private boolean doDuplicateCheck(GridDocument gdoc)
  {
    try
    {
      return getGridDocumentEntityHandler().findDuplicate(gdoc);
    }
    catch (Throwable ex)
    {
      Logger.err("[InboundFolder.doDuplicateCheck] Exception", ex);
    }
    return false;
  }
  */

  private GridDocument findReceived(GridDocument gdoc)
  {
    try
    {
      return getGridDocumentEntityHandler().findInboundDocFor(gdoc);
    }
    catch (Throwable ex)
    {
      Logger.warn("[InboundFolder.findReceived] Error", ex);
    }
    return null;
  }

  private String getAcknowledgementEvent(GridDocument gdoc)
  {
    return GridDocument.ROUTE_DIRECT.equals(gdoc.getSenderRoute()) ?
           IDocumentEvents.SEND_GRIDDOC_ACK :
           IDocumentEvents.DOWNLOAD_GRIDDOC_ACK;
  }

  private void doSendAck(GridDocument gdoc,
                         String ackEventId,
                         String ackTrxId)
    throws Exception
  {
    Logger.debug("[InboundFolder.doSendAck] Start");

    if (ackTrxId == null || gdoc.getSenderNodeId() == null)
    {
      Logger.log("[InboundFolder.doSendAck] Document was not received via GridTalk protocol, not sending acknowledgement");
      return;
    }

    /*020115NSL Make the data payload backward compatible
    String[] dataToSend = new String[] {gdoc.getRefGdocId().toString(), gdoc.getGdocId().toString()};
    */
    String[] dataToSend = {
                            Boolean.TRUE.toString(),
                            gdoc.getGdocId().toString(),
                            GridDocument.convertToTimestamp(gdoc.getDateTimeReceiveEnd()).toString(),
                          };

    if (IDocumentEvents.DOWNLOAD_GRIDDOC_ACK.equals(ackEventId))
    {
      // recipient is GridMaster
      ChannelDelegate.sendToGridMaster(dataToSend, ackEventId, ackTrxId);
    }
    else
    {
      // recipient is partner
      DocChannelInfo channelInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gdoc.getSenderPartnerId());
      String senderNodeId = gdoc.getSenderNodeId().toString();
      String recipientNodeId = gdoc.getRecipientNodeId().toString();  // me

      ChannelDelegate.send(
        ChannelDelegate.getChannelInfo(channelInfo.getChannelUid()),
        dataToSend,
        null,
        ChannelDelegate.prepareSendHeader(ackEventId, ackTrxId, null, recipientNodeId, senderNodeId));
      /*031002NSL  
        ackEventId,
        ackTrxId,
        recipientNodeId,  //sender of this ack
        senderNodeId);    //recipient of this ack
      */  
    }
    Logger.debug("[InboundFolder.doSendAck] End");
  }

  protected TriggerInfo getTriggerInfo(GridDocument gdoc)
  {
    TriggerInfo triggerInfo = new TriggerInfo();
    triggerInfo.setDocType(gdoc.getUdocDocType());
    triggerInfo.setPartnerGroup(gdoc.getSenderPartnerGroup());
    triggerInfo.setPartnerId(gdoc.getSenderPartnerId());
    triggerInfo.setPartnerType(gdoc.getSenderPartnerType());
    triggerInfo.setTriggerType(TriggerInfo.TRIGGER_RECEIVE);
    return triggerInfo;
  }
}