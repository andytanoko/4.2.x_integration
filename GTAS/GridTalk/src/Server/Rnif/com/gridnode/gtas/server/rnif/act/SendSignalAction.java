/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendSignalAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 02 2003    Neo Sok Lay         Set Sender/Recipient BizEntityUuid and
 *                                    RegistryQueryUrl into the rGDoc.
 * Oct 31 2003    Guo Jianyu          Modified sendDocument() to include a subpath "Outbound"
 * Jan 29 2004    Neo Sok Lay         Modified setUdocInfo() to set UdocFileType to "xml".
 *                                    Modified createGDoc2Send() to setProcessDefId() to
 *                                    GridDocument. 
 * Nov 17 2005    Tam Wei Xiang       Modified createGDoc2Send() to setOrignalDoc into
 *                                    GDOC.         
 * Oct 26 2006    Tam Wei Xiang       Modified method createGDoc2Send(...) to add in
 *                                    tracingID into GDOC.   
 * Nov 13 2006    Tam Wei Xiang       Trigger the creation of document (the signal doc) to
 *                                    OTC plug-in                                           
 *                                                                                      
 */
package com.gridnode.gtas.server.rnif.act;

import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.notification.DocumentTransactionHandler;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.EnterpriseUtil;
import com.gridnode.gtas.server.rnif.helpers.BpssHandler;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RNDocSender;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

import java.io.File;

abstract class SendSignalAction extends SendDocumentAction
{
  protected boolean       _isInitiator = false;


  void setExtraParam(Object param, boolean isRequest) throws RnifException
  {
    _isInitiator = ! isRequest;
  }

  GridDocument createGDoc2Send(GridDocument originalGDoc) throws RnifException
  {
    Logger.debug("In createGDoc2send, original GDoc is " + originalGDoc);
    Logger.debug("OriginalGDoc.gDocid is " + originalGDoc.getGdocId() );
    Logger.debug("OriginalGDoc.processinstanceid is " + originalGDoc.getProcessInstanceID() );
    Logger.debug("OriginalGDoc.processinstanceUid is " + originalGDoc.getProcessInstanceUid() );
    Logger.debug("OriginalGDoc.userTrackingId is " + originalGDoc.getUserTrackingID() );
    GridDocument gDoc= new GridDocument();
    setRecipientInfo(gDoc, originalGDoc);
    setSenderInfo(gDoc, originalGDoc);
    setUdocInfo(gDoc, originalGDoc);

    //TWX: 17 NOV 2005 set signal msg correspond gdoc's orignalDoc = true
    gDoc.setOriginalDoc(Boolean.TRUE);
    
    //TWX: 26102006 add the tracingID
    addTracingID(gDoc);
    
    ProfileUtil profileUtil = new ProfileUtil();
    RNProfile oldProfile= profileUtil.getProfileMustExist(originalGDoc);
    
    gDoc.setProcessDefId(oldProfile.getProcessDefName());
//    gDoc.setProcessInstanceID(oldProfile.getProcessInstanceId());
    try
    {
      BpssHandler.setProcessInstanceIds(gDoc, oldProfile.getProcessInstanceId() + "/"
        + oldProfile.getProcessOriginatorId());
    }
    catch(Exception e)
    {
      Logger.warn("Failed to set ProcessInstanceIds ", e);
    }
//Logger.debug("Setting processInstanceid to " + oldProfile.getProcessInstanceId());
    //gDoc.setUserTrackingID(originalGDoc.getUserTrackingID());
    try
    {
      BpssHandler.setUserTrackingId(null, gDoc, oldProfile, false);
    }
    catch(Exception e)
    {
      Logger.warn("Failed to set userTrackingId ", e);
    }

    try
    {
      RNProfile profile=
        profileUtil.createSignalRNProfileFields(
          oldProfile,
          getGlobalSignalCode(),
          getGlobalSignalVersion());
      gDoc.setRnProfileUid((Long) profile.getKey());
    }
    catch (Exception ex)
    {
      throw RnifException.profileCreateEx("in createSignalRNProfileFields ", ex);
    }
    return gDoc;
  }

  void setUdocInfo(GridDocument gDoc, GridDocument originalGDoc) throws RnifException
  {
    gDoc.setFolder(GridDocument.FOLDER_OUTBOUND);
    gDoc.setUdocDocType(getUDocDocType());
    gDoc.setUdocFileType("xml");
  }

  void setSenderInfo(GridDocument rGDoc, GridDocument originalGDoc) throws RnifException
  {
    BusinessEntity businessEntity= _recipientBE;

    rGDoc.setCreateBy("System");
    rGDoc.setSenderBizEntityId(businessEntity.getBusEntId());
    rGDoc.setSenderNodeId(
      businessEntity.getEnterpriseId() == null ? null : new Long(businessEntity.getEnterpriseId()));
    rGDoc.setSenderBizEntityUuid(originalGDoc.getRecipientBizEntityUuid());
    rGDoc.setSenderRegistryQueryUrl(originalGDoc.getRecipientRegistryQueryUrl());
  }

  /**
   * Set a responding griddocument's recipient information based on its original sender info
   *
   * @exception RNIFException if the partner cannot be found in the database
   * @since 1.1
   */
  void setRecipientInfo(GridDocument rGDoc, GridDocument originalGDoc) throws RnifException
  {
    BusinessEntity businessEntity= _senderBE;
    Long gnodeId= originalGDoc.getSenderNodeId();
    String beId= originalGDoc.getSenderBizEntityId();

    Partner partner= null;
    try
    {
      partner= EnterpriseUtil.get1stPartner4BE(businessEntity);
    }
    catch (Exception ex)
    {
      Logger.warn("Error invoking get1stPartner4BE in setRecipientInfo!", ex);
      partner= null;
    }

    // update sender info
    if (partner == null)
    {
      throw RnifException.entityNotFoundEx(
        "When sending an process acknowledge/exception Message, the partner is not found!",
        new Exception());
    }

    rGDoc.setRecipientNodeId(gnodeId);
    rGDoc.setRecipientBizEntityId(beId);
    rGDoc.setRecipientBizEntityUuid(originalGDoc.getSenderBizEntityUuid());
    rGDoc.setRecipientRegistryQueryUrl(originalGDoc.getSenderRegistryQueryUrl());
    rGDoc.setRecipientPartnerId(partner.getPartnerID());
    rGDoc.setRecipientPartnerName(partner.getName());
    rGDoc.setRecipientPartnerGroup(
      partner.getPartnerGroup() == null ? null : partner.getPartnerGroup().getName());
    rGDoc.setRecipientPartnerType(
      partner.getPartnerType() == null ? null : partner.getPartnerType().getName());
  } // end updateRecipientInfo()

  abstract String getGlobalSignalCode();

  String getGlobalSignalVersion()
  {
    return "V02.00";
  }

  void sendDocument(File uDoc, String udocName, GridDocument originalGDoc) throws RnifException
  {

    RNDocSender sender= new RNDocSender();
    GridDocument gDoc= createGDoc2Send(originalGDoc);

    try
    {
      udocName= FileUtil.create(IDocumentPathConfig.PATH_UDOC, "Outbound"+File.separator, udocName, uDoc);
      gDoc.setUdocFilename(udocName);
      String fullPath = DocumentUtil.getFullUDocPath(gDoc);
      gDoc.setUdocFullPath(fullPath);
    }
    catch (Throwable ex)
    {
      throw RnifException.fileProcessErr(
        "When moving the signal document to uDoc folder! uDocName is " + udocName,
        ex);
    }

    try
    {
      gDoc= DocumentUtil.addGridDocument(gDoc);
      //gDoc = DocumentUtil.addGDocWithNewTrans(gDoc); //TWX 11 DEC 2007 To ensure ChannelDelegate can retrieve the OB Signal
      
      //TWX 13112006 trigger the creation of document transaction
      DocumentTransactionHandler.triggerDocumentTransaction(gDoc, false, false); //10072007 new flag isRetry
      gDoc = sender.setSendingInfo(gDoc, _defName, _isInitiator? Boolean.TRUE:Boolean.FALSE);
      sender.send(gDoc);
     
    }
    catch (Throwable ex)
    {
      throw RnifException.documentSendEx("When sending an signal document!", ex);
    }
  }
  
  /**
   * TWX 26102006 Add tracingID into the gdoc
   * @param gdoc
   */
  private void addTracingID(GridDocument gdoc)
  {
    gdoc.setTracingID(UUIDUtil.getRandomUUIDInStr());
  }
  
}
