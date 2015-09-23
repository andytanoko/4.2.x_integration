/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNDocReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 20 2002    Neo Sok Lay         Modify receiveRNDoc() parameters.
 * Aug 31 2003    Guo Jianyu          Modify receiveRNDoc() to set processInstanceIds
 *                                    and userTrackingIds
 * Jan 29 2004    Neo Sok Lay         Add receiveRNDoc() with additional Gdoc 
 *                                    parameter to use the Gdoc as the base for
 *                                    the received Gdoc instance.
 * Mar 28 2005    Mahesh              Removed appending the new audit file names 
 * Sep 20 2006    Tam Wei Xiang       Change the gdoc ownCert, TpCert to senderCert,
 *                                    receiverCert
 * Feb 06 2007    Tam Wei Xiang       Emitted event Process Injection to OTC                                   
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.base.rnif.helper.IRNHeaderConstants;
import com.gridnode.pdip.base.rnif.helper.RNCertInfo;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.base.packaging.helper.IPackagingInfo;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;

import java.io.File;
import java.util.*;

public class RNDocReceiver  implements IReceiveMessageHandler
{

  public void handlerMessage(String[] header,
                             String[] dataReceived,
                             File[] filesReceived,
                             Hashtable additionalHeader
                             ) throws Exception
  {

    Logger.debug("[RNDocReceiver.handlerMessage] entered, additionalHeader="+additionalHeader);
    
    Exception handleMsgEx = null;
    boolean isSuccess = true;
    try
    { 
     receiveRNDoc(header, dataReceived, filesReceived,additionalHeader);
    }
    catch(Exception ex)
    {
      isSuccess = false;
      handleMsgEx = ex;
      Logger.warn("[RNDocReceiver.handlerMessage]",ex);
      throw ex;
    }
    finally
    {
      Logger.log("rndocreceiver tracingID is "+header[ChannelReceiveHeader.TRACING_ID]);
      DocumentFlowNotifyHandler.triggerNotification(EDocumentFlowType.PROCESS_INJECTION, 
                                                    new Date(), isSuccess, (handleMsgEx != null ? handleMsgEx.getMessage() : ""), 
                                                    header[ChannelReceiveHeader.TRACING_ID],null, "", false, "", handleMsgEx);
      Logger.debug("[RNDocReceiver.handlerMessage] exited");
    }
    

  }

  static public GridDocument receiveRNDoc(String[] header,
                                          String[] dataReceived,
                                          File[] filesReceived,Hashtable additionalHeader) throws Exception
  {
    return receiveRNDoc(header, dataReceived, filesReceived, null,additionalHeader);
  }
  
  static public GridDocument receiveRNDoc(String[] header,
                                          String[] dataReceived,
                                          File[] filesReceived,
                                          GridDocument receivedGdoc,Hashtable additionalHeader) throws Exception
  {
    File packinfoFile = filesReceived[0];
    Logger.debug("packinfoFile is = " + packinfoFile.getAbsolutePath());
    RNPackInfo packinfo = new RNPackInfo();
    packinfo = (RNPackInfo) packinfo.deserialize(packinfoFile.getAbsolutePath());
    try
    {
      packinfoFile.delete();
    }catch(Throwable t)
    {
      Logger.warn("[RNDocReceiver::receiveRNDoc()]Error in delete packinfoFile", t);
    }
    Logger.debug("[RNDocReceiver.receiveRNDoc()] packInfo.RNIFVersion is " +
      packinfo.getRnifVersion());
    RNProfile profile = new RNProfile();
    PackInfoConvertor convertor = new PackInfoConvertor();
    convertor.packInfoToProfile(profile, packinfo);

    String receiverDUNS = packinfo.getReceiverGlobalBusIdentifier();
    String senderDUNS = packinfo.getSenderGlobalBusIdentifier();
    String originatorDUNS = packinfo.getPartnerGlobalBusIdentifier();

    BusinessEntity receiverBE = EnterpriseUtil.getBE4DUNS(receiverDUNS);
    BusinessEntity senderBE = EnterpriseUtil.getBE4DUNS(senderDUNS);
    Partner senderPartner = EnterpriseUtil.get1stPartner4BE(senderBE);
    String partnerId = senderPartner.getPartnerID();

    GridDocument gDoc = receivedGdoc; 
    
    if (gDoc == null)
    {
      gDoc = new GridDocument();
    }
    
    //TWX: 15 NOV 2005
    RNCertInfo certInfo = getBizCertMappingForPartner(partnerId);
    
    //TWX: 19 Jan 2006
    if(certInfo.get_ownSignCertificate() != null)
    {
    	gDoc.setReceiverCert((Long)certInfo.get_ownSignCertificate().getKey());
    }
    if(certInfo.get_partnerEncryptCertificate() != null)
    {
    	gDoc.setSenderCert((Long)certInfo.get_partnerEncryptCertificate().getKey());
    }
    
    gDoc.setOriginalDoc(Boolean.TRUE);
    
    //24012005 Mahesh: This string will be used as a flag to send RnException to sender
    // in Bpss validateDocument 
    if(additionalHeader.get(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION)!=null)
      gDoc.setDocTransStatus((String)additionalHeader.get(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION));
      
    //senderInfo
    gDoc.setSenderBizEntityId(senderBE.getBusEntId());
    String enterpriseId = senderBE.getEnterpriseId();
    gDoc.setSenderNodeId(enterpriseId==null? null: new Long(enterpriseId));
    gDoc.setSenderPartnerGroup(senderPartner.getPartnerGroup()==null? null:senderPartner.getPartnerGroup().getEntityName());
    gDoc.setSenderPartnerId(senderPartner.getPartnerID());
    gDoc.setSenderPartnerType(senderPartner.getPartnerType()==null? null: senderPartner.getPartnerType().getEntityName());
    gDoc.setSenderPartnerName(senderPartner.getEntityName());
    //recipientInfo
    gDoc.setRecipientBizEntityId(receiverBE.getBusEntId());
    gDoc.setRecipientNodeId(new Long(receiverBE.getEnterpriseId()));
    
    String auditFileName=(String)additionalHeader.get(IRNHeaderConstants.AUDIT_FILE_NAME);
    if(auditFileName!=null && auditFileName.length()>0)
    {
      /* 200503288 Mahesh : set the audit file insted of appending to existing one
      String fileName = gDoc.getAuditFileName();
      if ((fileName != null) && (fileName.length() > 0))
        gDoc.setAuditFileName(fileName + ";" + auditFileName);
      else
      */
      gDoc.setAuditFileName(auditFileName);
      Logger.debug("[RNDocReceiver.receiveRNDoc()] auditFileName set to "+gDoc.getAuditFileName());        
    }

    String initiatorId = null;
    String instanceId = null;
    String responderId = null;
    //setProfileInfo
    Boolean isInitiator= null;

    if(receiverDUNS.equals(originatorDUNS) )
    {
      initiatorId = IBpssConstants.PARTNER_CONSTANT;
      responderId = partnerId;
      isInitiator = Boolean.TRUE;
    }
    else
    {
      initiatorId = partnerId;
      responderId = IBpssConstants.PARTNER_CONSTANT;
      isInitiator = Boolean.FALSE;
    }

    profile.setProcessOriginatorId(initiatorId);
    profile.setProcessResponderId(responderId);

    instanceId = profile.getPIPInstanceIdentifier();
    profile.setProcessInstanceId(instanceId);

    String versionId = profile.getPIPVersionIdentifier();
    String gProcessCode = profile.getPIPGlobalProcessCode();
    GridDocument docToSet = null;
    if(!packinfo.getIsSignalDoc())
      docToSet = gDoc;
    else
    {
      String udocType =  getSignalUDocType(profile);
      gDoc.setUdocDocType(udocType);
Logger.debug("## signal doc, uDocType is " + udocType);
      BpssHandler.setProcessInstanceIds(gDoc, profile.getProcessInstanceId() + "/" +
               profile.getProcessOriginatorId());
      BpssHandler.setUserTrackingId(null, gDoc, profile, false);
    }

    String defName =  ProcessUtil.getProcessDefName(gProcessCode, versionId, partnerId, isInitiator, docToSet);

    profile.setProcessDefName(defName);
    profile = new ProfileUtil().addProfile(profile);
    gDoc.setRnProfileUid((Long)profile.getKey());
    gDoc.setProcessDefId(defName);

    File[] udocs = new File[filesReceived.length - 1];
    for (int i = 0; i < udocs.length; i++)
    {
      udocs[i] = filesReceived[i + 1];
    }
    
    
//    /* set attachment fields in gDoc */
//    Logger.debug("RNDocReceiver::receiveRNDoc");
//    if (filesReceived.length > 2)
//    {
//    	Logger.debug("Have attachment");
//    	Logger.debug("Number of attachment = " + (filesReceived.length - 2));
//    	gDoc.setHasAttachment(Boolean.TRUE);
//    	ArrayList ar = new ArrayList();
//    	for (int i = 2; i < filesReceived.length; i++)
//    	{
//    		Logger.debug("Attachment filename: " + filesReceived[i].getPath());
//    		Long uid = addAttachmentInfoToDatabase(filesReceived[i].getName());
//    		ar.add(uid);
//    	}
//    	gDoc.setAttachments(ar);
//    } else
//    {
//    	Logger.debug("NO attachment");
//    }
    
//Logger.debug("Before DocumentUtil.processReceivedRnifDoc");
    gDoc = DocumentUtil.processReceivedRnifDoc(gDoc, header, dataReceived, udocs);
//Logger.debug("before BpssInvoker.insertDocReceived2BPSS");
    BpssInvoker.insertDocReceived2BPSS(gDoc, defName);
//Logger.debug("After BpssInvoker.insertDocReceived2BPSS");

//Logger.debug("process Instance is " + profile.getProcessInstanceId());
//Logger.debug("process initiator is " + profile.getProcessOriginatorId());
//		gDoc.setProcessInstanceID(profile.getProcessInstanceId());
//Logger.debug("IsRequest is " + profile.getIsRequestMsg());
 //   BpssHandler.setProcessInstanceIds(gDoc, profile.getProcessInstanceId() + "/" +
 //         profile.getProcessOriginatorId());
    
    
    return gDoc;
  }
  
//  /*
//   * For Attachment object, the programmer didn't set Partnerid or originalid.
//   * Returns uid for attachment.
//   */
//  private static Long addAttachmentInfoToDatabase(String filename)
//  {
//	  try
//	  {
//		  Logger.debug("[RNDocReceiver::addAttachmentInfoToDatabase] filename = " + filename);
//		  Attachment attachment = new Attachment();
//		  attachment.setFilename(filename);
//		  attachment.setOriginalFilename(filename);
//		  attachment.setOutgoing(Boolean.FALSE);
//		  IDocumentManagerObj docMgr = ArchiveHelper.getDocumentManager();
//		  return docMgr.createAttachment(attachment);
//	  } catch (Exception e)
//	  {
//		  Logger.err(e);
//	  }
//	  return null;
//  }

  public static String getSignalUDocType(RNProfile profile) throws Exception
  {
Logger.debug("## In getSignalUDocType");
    String globalSignalCode= profile.getSignalIdentityGlobalBusSignalCode();
    if ("Receipt Acknowledge".equals(globalSignalCode))
      return IRnifConstant.RN1_ACK;
    if ("Receipt Acknowledgement Exception".equals(globalSignalCode))
      return IRnifConstant.RN1_EXCEPTION;
    if ("Receipt Acknowledgment".equals(globalSignalCode))
      return IRnifConstant.RN2_ACK;
    if ("Exception".equals(globalSignalCode))
      return IRnifConstant.RN2_EXCEPTION;
    if ("General Exception".equals(globalSignalCode))
    {
      if (profile.getRNIFVersion().equals(IPackagingInfo.RNIF1_ENVELOPE_TYPE))
        return IRnifConstant.RN1_EXCEPTION;
      else
        return IRnifConstant.RN2_EXCEPTION;
    }
    throw new IllegalArgumentException("cannot determined Signal Doc Type from globalSignalCode=" + globalSignalCode);

  }

  //TWX: 14 NOV 2005
  private static RNCertInfo getBizCertMappingForPartner(String partnerID)
  	throws Exception
  {
  	return ProcessUtil.getBizCertMappingForPartner(partnerID);
  }
}
