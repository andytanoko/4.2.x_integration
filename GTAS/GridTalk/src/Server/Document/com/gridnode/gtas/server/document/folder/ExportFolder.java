/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportFolder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 08 2002    Koh Han Sing        Created
 * Jan 09 2002    Koh Han Sing        Remove doExport
 * Oct 10 2006    Neo Sok Lay         GNDB00027902: To construct TriggerInfo
 *                                    based on source folder type.
 */
package com.gridnode.gtas.server.document.folder;

import java.util.List;

import com.gridnode.gtas.server.document.helpers.AttachmentHelper;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.TriggerInfo;

public class ExportFolder extends SystemFolder
{
  public static final String FOLDER_NAME = "Export";
  private static final Long DEF_IB_EP_HEADER_MR_UID = new Long(1);
  private static final Long DEF_IP_EP_HEADER_MR_UID = new Long(4);

  public ExportFolder()
  {
  }

//  public GridDocument doExit(GridDocument gdoc)
//    throws Throwable
//  {
//    Logger.debug("[ExportFolder.doExit] Start");
//
//    gdoc = super.doExit(gdoc);
//
//    //gdoc = doExport(gdoc);
//    if (gdoc.getGdocId() != null)
//    {
//      gdoc = updateHeader(gdoc);
//    }
//
//    Logger.debug("[ExportFolder.doExit] End");
//    return gdoc;
//  }

//  public GridDocument doActivityEnd(GridDocument gdoc)
//    throws Throwable
//  {
//    Logger.debug("[ExportFolder.doActivityEnd] Start");
//    gdoc = super.doActivityEnd(gdoc);
//
//    String udocFilename = gdoc.getUdocFilename();
//    udocFilename = udocFilename.substring(0, udocFilename.indexOf(":"));
//    gdoc.setUdocFilename(udocFilename);
//
//    gdoc = this.doExit(gdoc);
//    Logger.debug("[ExportFolder.doActivityEnd] End");
//    return gdoc;
//  }

  public Long getDefaultHeaderMappingRuleUID(GridDocument gdoc)
  {
    if (gdoc.getSrcFolder().equals(GridDocument.FOLDER_INBOUND))
    {
      return DEF_IB_EP_HEADER_MR_UID;
    }
    else
    {
      return DEF_IP_EP_HEADER_MR_UID;
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
           gdoc.getRecipientNodeId()+"-EP-"+
           gdoc.getGdocId()+".xml";
  }

//  private GridDocument doExport(GridDocument gdoc)
//    throws Exception
//  {
//    Logger.debug("[ExportFolder.doExport] Start");
//
//    if (gdoc.getPortUid() != null)
//    {
//      ExportDocumentHelper helper = new ExportDocumentHelper();
//      helper.doExport(gdoc);
//    }
//
//    Logger.debug("[ExportFolder.doExport] End");
//    return gdoc;
//  }

  protected TriggerInfo getTriggerInfo(GridDocument gdoc)
  {
    TriggerInfo triggerInfo = new TriggerInfo();
    triggerInfo.setDocType(gdoc.getUdocDocType());
    if (GridDocument.FOLDER_INBOUND.equals(gdoc.getSrcFolder()))
    {
      //IB->EP
      triggerInfo.setPartnerGroup(gdoc.getSenderPartnerGroup());
      triggerInfo.setPartnerId(gdoc.getSenderPartnerId());
      triggerInfo.setPartnerType(gdoc.getSenderPartnerType());
    }
    else //IP->EP
    {
      triggerInfo.setPartnerGroup(gdoc.getRecipientPartnerGroup());
      triggerInfo.setPartnerId(gdoc.getRecipientPartnerId());
      triggerInfo.setPartnerType(gdoc.getRecipientPartnerType());
    }
    triggerInfo.setTriggerType(TriggerInfo.TRIGGER_MANUAL_EXPORT);
    return triggerInfo;

  }

  protected GridDocument checkAttachmentStatus(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[ExportFolder.checkAttachmentStatus] Start");
    if (gdoc.hasAttachment().booleanValue() && !gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      List newUids = AttachmentHelper.findIncomingAttachments(gdoc, gdoc.getSenderPartnerId());
      if (!newUids.isEmpty())
      {
        gdoc.setAttachments(newUids);
        gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
      }
    }
    Logger.debug("[ExportFolder.checkAttachmentStatus] End");
    return gdoc;
  }
}