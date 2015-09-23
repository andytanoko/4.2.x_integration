/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OutboundFolder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 08 2002    Koh Han Sing        Created
 * Sep 26 2002    Koh Han Sing        Add in send/receive functions
 * Sep 08 2003    Koh Han Sing        Add in Inbound to Outbound feature
 */
package com.gridnode.gtas.server.document.folder;

import java.util.List;

import com.gridnode.gtas.server.document.helpers.AttachmentHelper;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.TriggerInfo;

public class OutboundFolder extends SystemFolder
{
  public static final String FOLDER_NAME = "Outbound";
  private static final Long DEF_IP_OB_HEADER_MR_UID = new Long(3);
  private static final Long DEF_IB_OB_HEADER_MR_UID = new Long(-1);

  public OutboundFolder()
  {
  }

//  public GridDocument doExit(GridDocument gdoc)
//    throws Throwable
//  {
//    Logger.debug("[OutboundFolder.doExit] Start");

//    if (gdoc.getRecipientPartnerId() != null)
//    {
//      PartnerInfo pInfo = BizRegDelegate.getPartnerInfo(gdoc.getRecipientPartnerId());
//      if (pInfo != null)
//      {
//        gdoc.setRecipientPartnerName(pInfo.getPartnerName());
//        gdoc.setRecipientPartnerGroup(pInfo.getPartnerGroup());
//        gdoc.setRecipientPartnerType(pInfo.getPartnerType());
//        gdoc.setRecipientBizEntityId(pInfo.getBizEntityID());
//        gdoc.setRecipientNodeId(pInfo.getNodeID());
//      }
//      else
//      {
//        Logger.err("[OutboundFolder.doExit] Unable to retrieve ParterInfo for partner "
//          + gdoc.getRecipientPartnerId());
//      }
//    }

//    if (gdoc.getRnProfileUid() == null)
//    {
//      DocChannelInfo channelInfo = BizRegDelegate.getPartnerDefaultChannelInfo(gdoc.getRecipientPartnerId());
//      if (channelInfo != null)
//      {
//        gdoc.setRecipientChannelUid(channelInfo.getChannelUid());
//        gdoc.setRecipientChannelName(channelInfo.getChannelName());
//        gdoc.setRecipientChannelProtocol(channelInfo.getChannelProtocol());
//      }
//    }

//    super.doExit(gdoc);
//
//    if (gdoc.getGdocId() != null)
//    {
//      gdoc = updateHeader(gdoc);
//    }

//    Logger.debug("[OutboundFolder.doExit] End");
//    return gdoc;
//  }

//  public GridDocument doActivityEnd(GridDocument gdoc)
//    throws Throwable
//  {
//    Logger.debug("[OutboundFolder.doActivityEnd] Start");
//    gdoc = super.doActivityEnd(gdoc);
//    String udocFilename = gdoc.getUdocFilename();
//    udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
//    gdoc.setUdocFilename(udocFilename);
//    return doExit(gdoc);
//  }

  public Long getDefaultHeaderMappingRuleUID(GridDocument gdoc)
  {
    if (gdoc.getSrcFolder().equals(GridDocument.FOLDER_INBOUND))
    {
      return DEF_IB_OB_HEADER_MR_UID;
    }
    else
    {
      return DEF_IP_OB_HEADER_MR_UID;
    }
  }

  public String getFolderName()
  {
    return FOLDER_NAME;
  }

  public String getGdocFilename(GridDocument gdoc)
    throws Exception
  {
    return gdoc.getSenderNodeId()+"-"+
           gdoc.getRecipientNodeId()+"-OB-"+
           gdoc.getGdocId()+".xml";
  }

//  private void doSend(GridDocument gdoc)
//    throws Exception
//  {
//    Logger.debug("[OutboundFolder.doSend] Start");
//    SendDocumentHelper helper = new SendDocumentHelper();
//    helper.doSend(gdoc);
//    Logger.debug("[OutboundFolder.doSend] End");
//  }


  protected TriggerInfo getTriggerInfo(GridDocument gdoc)
  {
    TriggerInfo triggerInfo = new TriggerInfo();
    triggerInfo.setDocType(gdoc.getUdocDocType());
    triggerInfo.setPartnerGroup(gdoc.getRecipientPartnerGroup());
    triggerInfo.setPartnerId(gdoc.getRecipientPartnerId());
    triggerInfo.setPartnerType(gdoc.getRecipientPartnerType());
    triggerInfo.setTriggerType(TriggerInfo.TRIGGER_MANUAL_SEND);
    return triggerInfo;

  }

  protected GridDocument checkAttachmentStatus(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[OutboundFolder.checkAttachmentStatus] Start");
    if (gdoc.hasAttachment().booleanValue() && !gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      List newUids = AttachmentHelper.findIncomingAttachments(gdoc, gdoc.getRecipientPartnerId());
      if (!newUids.isEmpty())
      {
        gdoc.setAttachments(newUids);
        gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
      }
    }
    Logger.debug("[OutboundFolder.checkAttachmentStatus] End");
    return gdoc;
  }
}