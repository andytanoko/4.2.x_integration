/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportFolder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 08 2002    Koh Han Sing        Created
 * May 21 2003    Neo Sok Lay         Gobble the PartnerNotFoundException when getting
 *                                    partnerInfo.
 * Oct 01 2003    Neo Sok Lay         Set RecipientBizEntityUuid and RecipientRegistryQueryUrl
 *                                    into GridDocument from PartnerInfo.
 * Oct 25 2006    Tam Wei Xiang       Set the tracingID into gridDocument. Modified method
 *                                    doEnter()                                   
 */
package com.gridnode.gtas.server.document.folder;

import com.gridnode.gtas.server.document.exceptions.PartnerNotFoundException;
import com.gridnode.gtas.server.document.helpers.AttachmentHelper;
import com.gridnode.gtas.server.document.helpers.BizRegDelegate;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.Activity;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.PartnerInfo;
import com.gridnode.gtas.server.document.model.TriggerInfo;
import com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

import java.util.Date;
import java.util.List;

public class ImportFolder extends SystemFolder
{
  public static final String FOLDER_NAME = "Import";
  private static final Long DEF_IB_IP_HEADER_MR_UID = new Long(0);

  public ImportFolder()
  {
  }

  public GridDocument doEnter(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[ImportFolder.doEnter] Start");
    if ((gdoc.getFolder() != null) && (gdoc.getFolder().equals(GridDocument.FOLDER_INBOUND)))
    {
      gdoc = doDefaultHeaderTransform(gdoc);
      gdoc.setSrcFolder(gdoc.getFolder());
      
//    TWX: 16 NOV 2005 extract out the values of the elements from udoc and put inside gdoc
      super.extractUdocElementInfo(gdoc, FileHelper.getUdocFile(gdoc));
    }

    setPartnerInfo(gdoc);
    
    setTracingID(gdoc); //25102006 TWX
    
    gdoc.setFolder(getFolderName());
    gdoc.setDateTimeImport(new Date(TimeUtil.localToUtc()));
    
    setRegistryInfo(gdoc);
	
    Activity newActivity = new Activity();
    newActivity.setActivityType(EXIT_TO_FOLDER);
    newActivity.setDescription(EXIT_TO_FOLDER+" "+getFolderName());
    newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
    gdoc.addActivity(newActivity);

    if (!gdoc.getSrcFolder().equals(GridDocument.FOLDER_INBOUND))
    {
//      gdoc = createHeader(gdoc);
      gdoc = saveInFolder(gdoc);
    }

    Logger.debug("[ImportFolder.doEnter] End");
    return gdoc;
  }

  public GridDocument doExit(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[ImportFolder.doExit] Start");
    setPartnerInfo(gdoc);
    gdoc = super.doExit(gdoc);
    Logger.debug("[ImportFolder.doExit] End");
    return gdoc;
  }

//  public GridDocument doActivityEnd(GridDocument gdoc)
//    throws Throwable
//  {
//    Logger.debug("[ImportFolder.doActivityEnd] Start");
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
//        Logger.err("[ImportFolder.doActivityEnd] Unable to retrieve ParterInfo for partner "
//          + gdoc.getRecipientPartnerId());
//      }
//    }
//    return super.doActivityEnd(gdoc);
//  }

  public GridDocument saveInFolder(GridDocument gdoc) throws Throwable
  {
    Logger.debug("[ImportFolder.saveInFolder] Start");
    setPartnerInfo(gdoc);
    return super.saveInFolder(gdoc);
  }

  protected void setPartnerInfo(GridDocument gdoc) throws Throwable
  {
    if (gdoc.getRecipientPartnerId() != null && gdoc.getRecipientPartnerId().trim().length()>0)
    {
      try
      {
        PartnerInfo pInfo = BizRegDelegate.getPartnerInfo(gdoc.getRecipientPartnerId(), false);
        gdoc.setRecipientPartnerName(pInfo.getPartnerName());
        gdoc.setRecipientPartnerGroup(pInfo.getPartnerGroup());
        gdoc.setRecipientPartnerType(pInfo.getPartnerType());
        gdoc.setRecipientBizEntityId(pInfo.getBizEntityID());
        gdoc.setRecipientNodeId(pInfo.getNodeID());
        gdoc.setRecipientBizEntityUuid(pInfo.getBizEntityUuid());
        gdoc.setRecipientRegistryQueryUrl(pInfo.getRegistryQueryUrl());
      }
      catch (PartnerNotFoundException ex)
      {
        Logger.warn("[ImportFolder.setPartnerInfo] Unable to retrieve ParterInfo for partner "
          + gdoc.getRecipientPartnerId());
      }
    }
  }
  
  protected void setRegistryInfo(GridDocument gdoc) throws Throwable
  {
	RegistryObjectMapping mapping = BizRegDelegate.getRegistryInfo(
	                                  String.valueOf(gdoc.getSenderNodeId()),
	                                  gdoc.getSenderBizEntityId());
	if (mapping != null)
	{
		gdoc.setSenderBizEntityUuid(mapping.getRegistryObjectKey());
		gdoc.setSenderRegistryQueryUrl(mapping.getRegistryQueryUrl());    
	}
  }

  public Long getDefaultHeaderMappingRuleUID(GridDocument gdoc)
  {
    return DEF_IB_IP_HEADER_MR_UID;
  }

  public String getFolderName()
  {
    return FOLDER_NAME;
  }

  public String getGdocFilename(GridDocument gdoc)
    throws Exception
  {
    return gdoc.getSenderNodeId()+"-"+
           gdoc.getRecipientNodeId()+"-IP-"+
           gdoc.getGdocId()+".xml";
  }

  protected TriggerInfo getTriggerInfo(GridDocument gdoc)
  {
    TriggerInfo triggerInfo = new TriggerInfo();
    triggerInfo.setDocType(gdoc.getUdocDocType());
    triggerInfo.setPartnerGroup(gdoc.getRecipientPartnerGroup());
    triggerInfo.setPartnerId(gdoc.getRecipientPartnerId());
    triggerInfo.setPartnerType(gdoc.getRecipientPartnerType());
    triggerInfo.setTriggerType(TriggerInfo.TRIGGER_IMPORT);
    return triggerInfo;
  }

  protected GridDocument checkAttachmentStatus(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[ImportFolder.checkAttachmentStatus] Start");
    if (gdoc.hasAttachment().booleanValue() && !gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      List newUids = AttachmentHelper.findIncomingAttachments(gdoc, gdoc.getRecipientPartnerId());
      if (!newUids.isEmpty())
      {
        gdoc.setAttachments(newUids);
        gdoc.setAttachmentLinkUpdated(Boolean.TRUE);
      }
    }
    Logger.debug("[ImportFolder.checkAttachmentStatus] End");
    return gdoc;
  }
  
  /**
   * TWX 25102006 Set the tracingID into gdoc if it hasn't been set.
   * @param gdoc
   */
  private void setTracingID(GridDocument gdoc)
  {
    String tracingID = gdoc.getTracingID();
    if(tracingID == null || "".equals(tracingID))
    {
      gdoc.setTracingID(UUIDUtil.getRandomUUIDInStr());
    }
  }
}