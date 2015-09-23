/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGdocDetailManager.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 2  Nov 2006       Regina Zeng             Created
 * 13 Dec 2006       Tam Wei Xiang           Modified loadField(...). For some case,
 *                                           the gdoc may not contain the senderCertUID or receiverCertUID.
 *                                           DocumentMetaInfo related field will also be loaded together 
 *                                           with DOC_META_INFO_UID.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.web.archive.helpers.Logger;

import com.gridnode.gtas.events.dbarchive.doc.GetEsDocEvent;
import com.gridnode.gtas.events.dbarchive.doc.temp.GetGdocDetailEvent;
import com.gridnode.gtas.events.document.GetGridDocumentListEvent;
import com.gridnode.gtas.events.certificate.GetCertificateEvent;
import com.gridnode.gtas.model.dbarchive.docforpi.IDocumentMetaInfo;
import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.gtas.model.certificate.ICertificate;

import com.gridnode.pdip.framework.db.filter.DataFilterFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class DefaultGdocDetailManager extends DefaultAbstractManager implements IGTGdocDetailManager
{
  //private static final String ATTACHMENT_PATH_KEY = "document.path.attachment"; //commented the actual path key 
  
  private static final String FILENAME_PATH_KEY = "dbarchive.path.estore.temp.tempContent";
      
  DefaultGdocDetailManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_GDOC_DETAIL, session);
  }
  
  protected IEvent getGetEvent(Long uid) throws EventException
  {
    return new GetGdocDetailEvent(uid);
  }

  protected IEvent getGetListEvent(IDataFilter filter) throws EventException
  {
    return new GetGridDocumentListEvent(filter);
  }

  protected IEvent getDeleteEvent(Collection uids) throws EventException
  {
    throw new UnsupportedOperationException();
  }
  
  protected AbstractGTEntity createEntityObject(String entityType)
  throws GTClientException
  {
    if(IGTEntity.ENTITY_GDOC_DETAIL.equals(entityType))
    {
      return new DefaultGdocDetailEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:"+this+" cannot create entity object of type "+entityType);
    }
  }
  
  protected void doUpdate(IGTEntity entity) throws GTClientException
  {
    throw new UnsupportedOperationException();
  }

  protected void doCreate(IGTEntity entity) throws GTClientException
  {
    throw new UnsupportedOperationException();
  }  
  
  protected int getManagerType()
  {
    return IGTManager.MANAGER_GDOC_DETAIL;
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_GDOC_DETAIL;
  }
  
  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType) throws GTClientException
  {
    if(IGTEntity.ENTITY_GDOC_DETAIL.equals(entityType))
    { 
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[20];
      
      sharedVfmi[0] = new VirtualSharedFMI("documentMetaInfo.remark", IGTGdocDetailEntity.DOC_REMARKS);
      sharedVfmi[0].setMandatoryCreate(false);
      sharedVfmi[0].setDisplayableCreate(true);
      sharedVfmi[0].setCollection(false);
      sharedVfmi[0].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type", "text");
      constraint = new TextConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);
     
      //@Override the property value of udoc filename
      sharedVfmi[1] = new VirtualSharedFMI("gridDocument.udocFilename", IGTGdocDetailEntity.U_DOC_FILENAME);
      sharedVfmi[1].setMandatoryCreate(true);
      sharedVfmi[1].setDisplayableCreate(true);
      sharedVfmi[1].setCollection(true);
      sharedVfmi[1].setValueClass("java.util.Collection");
      sharedVfmi[1].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type", "file");
      detail.setProperty("file.downloadable", "true");
      detail.setProperty("file.fixedKey",FILENAME_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[1].setConstraint(constraint);
            
      //@Override the property value of Sender Partner ID
      sharedVfmi[2] = new VirtualSharedFMI("gridDocument.senderPartnerId", IGTGdocDetailEntity.S_PARTNER_ID);
      sharedVfmi[2].setMandatoryCreate(true);
      sharedVfmi[2].setDisplayableCreate(true);
      sharedVfmi[2].setCollection(false);
      sharedVfmi[2].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[2].setConstraint(constraint);

      //@Override the property value of docType
      sharedVfmi[3] = new VirtualSharedFMI("gridDocument.udocDocType", IGTGdocDetailEntity.U_DOC_DOC_TYPE);
      sharedVfmi[3].setMandatoryCreate(true);
      sharedVfmi[3].setDisplayableCreate(true);
      sharedVfmi[3].setCollection(false);
      sharedVfmi[3].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[3].setConstraint(constraint);

      //@Override the property value of recipients
      sharedVfmi[4] = new VirtualSharedFMI("gridDocument.recipientPartnerId", IGTGdocDetailEntity.R_PARTNER_ID);
      sharedVfmi[4].setMandatoryCreate(true);
      sharedVfmi[4].setDisplayableCreate(true);
      sharedVfmi[4].setCollection(false);
      sharedVfmi[4].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[4].setConstraint(constraint);

      //@Override the property value of attachments
      sharedVfmi[5] = new VirtualSharedFMI("gridDocument.attachments", IGTGdocDetailEntity.ATTACHMENTS);
      sharedVfmi[5].setMandatoryCreate(false);
      sharedVfmi[5].setDisplayableCreate(true);
      sharedVfmi[5].setCollection(true);
      sharedVfmi[5].setValueClass("java.util.Collection");
      sharedVfmi[5].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type", "file");
      detail.setProperty("file.downloadable", "true");
      detail.setProperty("file.fixedKey",FILENAME_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[5].setConstraint(constraint);
      
      //@Override the property value of RecipientChannelName
      sharedVfmi[6] = new VirtualSharedFMI("gridDocument.recipientChannelName", IGTGdocDetailEntity.R_CHANNEL_NAME);
      sharedVfmi[6].setMandatoryCreate(true);
      sharedVfmi[6].setDisplayableCreate(true);
      sharedVfmi[6].setCollection(false);
      sharedVfmi[6].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[6].setConstraint(constraint);
      
      //@Override the property value of Port Name
      sharedVfmi[7] = new VirtualSharedFMI("gridDocument.portName", IGTGdocDetailEntity.PORT_NAME);
      sharedVfmi[7].setMandatoryCreate(true);
      sharedVfmi[7].setDisplayableCreate(true);
      sharedVfmi[7].setCollection(false);
      sharedVfmi[7].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[7].setConstraint(constraint);
      
      //@Override the property value of Attachment Filename 
      sharedVfmi[8] = new VirtualSharedFMI("gridDocument.attachmentFilenames", IGTGdocDetailEntity.ATTACHMENT_FILENAMES);
      sharedVfmi[8].setMandatoryCreate(false);
      sharedVfmi[8].setDisplayableCreate(true);
      sharedVfmi[8].setCollection(true);
      sharedVfmi[8].setValueClass("java.util.Collection");
      sharedVfmi[8].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","file");
      detail.setProperty("file.downloadable","true");
      detail.setProperty("file.fixedKey",FILENAME_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[8].setConstraint(constraint);
      
      //Audit Filename
      sharedVfmi[9] = new VirtualSharedFMI("gridDocument.auditFileName", IGTGdocDetailEntity.AUDIT_FILENAME);
      sharedVfmi[9].setMandatoryCreate(true);
      sharedVfmi[9].setDisplayableCreate(true);
      sharedVfmi[9].setCollection(true);
      sharedVfmi[9].setValueClass("java.util.Collection");
      sharedVfmi[9].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type", "file");
      detail.setProperty("file.downloadable", "true");
      detail.setProperty("file.fixedKey",FILENAME_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[9].setConstraint(constraint);
      
      //@Override the property value of Recipient Audit Filename
      sharedVfmi[10] = new VirtualSharedFMI("gridDocument.receiptAuditFile", IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME);
      sharedVfmi[10].setMandatoryCreate(true);
      sharedVfmi[10].setDisplayableCreate(true);
      sharedVfmi[10].setCollection(false);
      sharedVfmi[10].setValueClass("java.lang.String");
      //sharedVfmi[10].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type", "file");
      detail.setProperty("file.downloadable", "true");
      detail.setProperty("file.fixedKey",FILENAME_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[10].setConstraint(constraint);
      
      //@Override the property value of Sender Certificate
      sharedVfmi[11] = new VirtualSharedFMI("gridDocument.senderCert", IGTGdocDetailEntity.SENDER_CERT);
      sharedVfmi[11].setMandatoryCreate(false);
      sharedVfmi[11].setDisplayableCreate(true);
      sharedVfmi[11].setCollection(false);
      sharedVfmi[11].setValueClass("java.lang.Long");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","certificate.uid");
      detail.setProperty("foreign.display","certificate.name");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[11].setConstraint(constraint);
      
      //@Override the property value of Receiver Certificate
      sharedVfmi[12] = new VirtualSharedFMI("gridDocument.receiverCert", IGTGdocDetailEntity.RECEIVER_CERT);
      sharedVfmi[12].setMandatoryCreate(false);
      sharedVfmi[12].setDisplayableCreate(true);
      sharedVfmi[12].setCollection(false);
      sharedVfmi[12].setValueClass("java.lang.Long");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","certificate.uid");
      detail.setProperty("foreign.display","certificate.name");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[12].setConstraint(constraint);
      
      //Doc Meta Info UID
      sharedVfmi[13] = new VirtualSharedFMI("gridDocument.receiptAuditFileName", IGTGdocDetailEntity.DOC_META_INFO_UID);
      sharedVfmi[13].setMandatoryCreate(true);
      sharedVfmi[13].setDisplayableCreate(false);
      sharedVfmi[13].setCollection(false);
      sharedVfmi[13].setValueClass("java.lang.Long");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","documentMetaInfo.UID");
      detail.setProperty("foreign.display","gridDocument.auditFileName");
      detail.setProperty("foreign.additionalDisplay", "gridDocument.receiptAuditFileName");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[13].setConstraint(constraint);
      
      //Sender Certificate Name
      sharedVfmi[14] = new VirtualSharedFMI("gridDocument.senderCert", IGTGdocDetailEntity.SENDER_CERT_NAME);
      sharedVfmi[14].setMandatoryCreate(false);
      sharedVfmi[14].setDisplayableCreate(true);
      sharedVfmi[14].setCollection(false);
      sharedVfmi[14].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[14].setConstraint(constraint);
      
      //Doc Meta Info UID
      sharedVfmi[15] = new VirtualSharedFMI("gridDocument.receiverCert", IGTGdocDetailEntity.RECEIVER_CERT_NAME);
      sharedVfmi[15].setMandatoryCreate(false);
      sharedVfmi[15].setDisplayableCreate(true);
      sharedVfmi[15].setCollection(false);
      sharedVfmi[15].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","text");
      constraint = new TextConstraint(detail);
      sharedVfmi[15].setConstraint(constraint);
      
      //TWX 13122006 Added vfield for hasAttachment
      sharedVfmi[16] = new VirtualSharedFMI("gridDocument.hasAttachment", IGTGdocDetailEntity.HAS_ATTACHMENT);
      sharedVfmi[16].setMandatoryCreate(false);
      sharedVfmi[16].setMandatoryUpdate(false);
      sharedVfmi[16].setEditableCreate(false);
      sharedVfmi[16].setEditableUpdate(false);
      sharedVfmi[16].setDisplayableCreate(false);
      sharedVfmi[16].setDisplayableUpdate(false);
      sharedVfmi[16].setCollection(false);
      sharedVfmi[16].setValueClass("java.lang.Boolean");
      detail = new Properties();
      detail.setProperty("type",          "enum");
      detail.setProperty("generic.yes",   "true");
      detail.setProperty("generic.no",    "false");
      constraint = new EnumeratedConstraint(detail);
      sharedVfmi[16].setConstraint(constraint);
      
      //TWX add user tracking ID & udoc number
      sharedVfmi[17] = new VirtualSharedFMI("gridDocument.userTrackingId", IGTGdocDetailEntity.USER_TRACKING_ID);
      sharedVfmi[17].setMandatoryCreate(false);
      sharedVfmi[17].setMandatoryUpdate(false);
      sharedVfmi[17].setEditableCreate(false);
      sharedVfmi[17].setEditableUpdate(false);
      sharedVfmi[17].setDisplayableCreate(true);
      sharedVfmi[17].setDisplayableUpdate(true);
      sharedVfmi[17].setCollection(false);
      sharedVfmi[17].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type",            "text");
      detail.setProperty("text.length.max", "80");
      constraint = new TextConstraint(detail);
      sharedVfmi[17].setConstraint(constraint);
      
      sharedVfmi[18] = new VirtualSharedFMI("gridDocument.udocNum", IGTGdocDetailEntity.U_DOC_NUM);
      sharedVfmi[18].setMandatoryCreate(false);
      sharedVfmi[18].setMandatoryUpdate(false);
      sharedVfmi[18].setEditableCreate(false);
      sharedVfmi[18].setEditableUpdate(false);
      sharedVfmi[18].setDisplayableCreate(true);
      sharedVfmi[18].setDisplayableUpdate(true);
      sharedVfmi[18].setCollection(false);
      sharedVfmi[18].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type",            "text");
      detail.setProperty("text.length.max", "20");
      constraint = new TextConstraint(detail);
      sharedVfmi[18].setConstraint(constraint);
      
      sharedVfmi[19] = new VirtualSharedFMI("gridDocument.docTransStatus", IGTGdocDetailEntity.DOC_TRANS_STATUS);
      sharedVfmi[19].setMandatoryCreate(false);
      sharedVfmi[19].setMandatoryUpdate(false);
      sharedVfmi[19].setEditableCreate(false);
      sharedVfmi[19].setEditableUpdate(false);
      sharedVfmi[19].setDisplayableCreate(true);
      sharedVfmi[19].setDisplayableUpdate(true);
      sharedVfmi[19].setCollection(false);
      sharedVfmi[19].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type",            "text");
      detail.setProperty("text.length.max", "255");
      constraint = new TextConstraint(detail);
      sharedVfmi[19].setConstraint(constraint);
      
      return sharedVfmi;
    }
    else
    {
      return new IGTFieldMetaInfo[0];
    }
  }
  
  void initVirtualEntityFields(String entityType, AbstractGTEntity entity, Map fieldMap)
    throws GTClientException
  {
    IGTGdocDetailEntity instance = (IGTGdocDetailEntity)entity;
    if(IGTEntity.ENTITY_GDOC_DETAIL.equals(entityType))
    { 
      Long senderCertUID = (Long)fieldMap.get(IGridDocument.SENDER_CERT);
      Long receiverCertUID = (Long)fieldMap.get(IGridDocument.RECEIVER_CERT);
      
      entity.setNewFieldValue(instance.DOC_META_INFO_UID, new UnloadedFieldToken());
      
      /*
      entity.setNewFieldValue(instance.ATTACHMENT_FILENAMES, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.DOC_REMARKS, new UnloadedFieldToken());
      
      //TWX 13122006 Change to Load on demand field
      entity.setNewFieldValue(instance.SENDER_CERT, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.RECEIVER_CERT, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.AUDIT_FILENAME, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.RECEIPT_AUDIT_FILENAME, new UnloadedFieldToken());
      
      entity.setNewFieldValue(instance.SENDER_CERT_NAME, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.RECEIVER_CERT_NAME, new UnloadedFieldToken());
      entity.setNewFieldValue(instance.HAS_ATTACHMENT, new UnloadedFieldToken());
      */
    }
  }
  
  protected void loadField(Number fieldId, AbstractGTEntity entity) throws GTClientException
  {  
    if(entity instanceof IGTGdocDetailEntity)
    {
      //if((IGTGdocDetailEntity.DOC_REMARKS.equals(fieldId))||(IGTGdocDetailEntity.DOC_META_INFO_UID.equals(fieldId)))
      
      //TWX 13122006 Instead of everytime we fire an event to backend while loading a particular field,
      //    we take this apportunity to initialize all the field that related to the DocumentMetaInfo
      if(IGTGdocDetailEntity.DOC_META_INFO_UID.equals(fieldId))
      {
        if(entity.isNewEntity())
        {
          entity.setNewFieldValue(IGTGdocDetailEntity.DOC_REMARKS, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.DOC_META_INFO_UID, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.HAS_ATTACHMENT, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.ATTACHMENT_FILENAMES, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.SENDER_CERT, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.SENDER_CERT_NAME, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.RECEIVER_CERT, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.RECEIVER_CERT_NAME, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.AUDIT_FILENAME, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.USER_TRACKING_ID,null);
          entity.setNewFieldValue(IGTGdocDetailEntity.U_DOC_NUM, null);
          entity.setNewFieldValue(IGTGdocDetailEntity.DOC_TRANS_STATUS, null);
        }
        else{
          try
          {
            GetEsDocEvent event = new GetEsDocEvent(entity.getUidLong());
            Map docMap = (Map)handleEvent(event);
            if(docMap==null)
            {
              throw new java.lang.NullPointerException("null response collection for GetEsDocEvent");
            }
            
            String remarks = (String)docMap.get(IDocumentMetaInfo.REMARK);
            Long docUID = (Long)docMap.get(IDocumentMetaInfo.UID);
            boolean isContainAttachment = (Boolean)docMap.get(IDocumentMetaInfo.IS_CONTAIN_ATTACHMENT);
            Collection<String> attachmentFilenames = convertConcatenatedAttachmentFilenames((String)docMap.get(IDocumentMetaInfo.ATTACHMENT_FILENAMES));
            String senderCert = (String)docMap.get(IDocumentMetaInfo.SENDER_CERT);
            String receiverCert = (String)docMap.get(IDocumentMetaInfo.RECEIVER_CERT);
            String auditFilename = (String)docMap.get(IDocumentMetaInfo.Filename);
            String receiptAuditFilename = (String)docMap.get(IDocumentMetaInfo.RECEIPT_AUDIT_FILENAME);
            String userTrackingID = (String)docMap.get(IDocumentMetaInfo.USER_TRACKING_ID);
            String udocNum = (String)docMap.get(IDocumentMetaInfo.Doc_Number);
            String docTransStatus = (String)docMap.get(IDocumentMetaInfo.DOC_TRANS_STATUS);
            
            auditFilename = getFilename(auditFilename);
            System.out.println("Receipt Audit is "+receiptAuditFilename);
            entity.setNewFieldValue(IGTGdocDetailEntity.DOC_REMARKS, remarks);
            entity.setNewFieldValue(IGTGdocDetailEntity.DOC_META_INFO_UID, String.valueOf(docUID));
            entity.setNewFieldValue(IGTGdocDetailEntity.ATTACHMENT_FILENAMES, attachmentFilenames);
            entity.setNewFieldValue(IGTGdocDetailEntity.HAS_ATTACHMENT, new Boolean(isContainAttachment).toString());
            entity.setNewFieldValue(IGTGdocDetailEntity.AUDIT_FILENAME, auditFilename);
            entity.setNewFieldValue(IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME, getFilename(receiptAuditFilename));
            entity.setNewFieldValue(IGTGdocDetailEntity.USER_TRACKING_ID, userTrackingID);
            entity.setNewFieldValue(IGTGdocDetailEntity.U_DOC_NUM, udocNum);
            entity.setNewFieldValue(IGTGdocDetailEntity.DOC_TRANS_STATUS, docTransStatus);
            
            if(senderCert != null && senderCert.length() > 0)
            {
              Long senderCertUID = new Long(senderCert);
              String certName = getCertNameByCertUID(senderCertUID);
              entity.setNewFieldValue(IGTGdocDetailEntity.SENDER_CERT, senderCertUID);
              entity.setNewFieldValue(IGTGdocDetailEntity.SENDER_CERT_NAME, getCertNameByCertUID(senderCertUID));
              
            }
            
            if(receiverCert != null && receiverCert.length() > 0)
            {
              Long receiverCertUID = new Long(receiverCert);
              String certName = getCertNameByCertUID(receiverCertUID);
              entity.setNewFieldValue(IGTGdocDetailEntity.RECEIVER_CERT, receiverCertUID);
              entity.setNewFieldValue(IGTGdocDetailEntity.RECEIVER_CERT_NAME, getCertNameByCertUID(receiverCertUID));
            }
            
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
          }
        }        
      }
      /* TWX 13122006 attachmentFilenames will be loaded together with the field DocMetaInfoUID which initialized
       * through GetEsDocEvent
      else if(IGTGdocDetailEntity.ATTACHMENT_FILENAMES.equals(fieldId))
      {
        try
        {
          GetGdocDetailEvent event = new GetGdocDetailEvent(entity.getUidLong());
          Map docMap = (Map)handleEvent(event);
          if(docMap==null)
          {
            throw new java.lang.NullPointerException("null response collection for GetGdocDetailEvent");
          }
          Collection attachments = (Collection)docMap.get(IGridDocument.ATTACHMENTS);
          ArrayList filenames = null;
          if(attachments != null)
          {
            if(attachments.size() > 0)
            {
              filenames = new ArrayList(attachments.size());
              Iterator i = attachments.iterator();
              while(i.hasNext())
              {
                IGTAttachmentEntity attachment = (IGTAttachmentEntity)i.next();
                String filename = attachment.getFieldString(IGTAttachmentEntity.FILENAME);
                filenames.add(filename);
              }
            }
          }
          entity.setNewFieldValue(fieldId, filenames);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
        }
      } 
      else if(IGTGdocDetailEntity.SENDER_CERT_NAME.equals(fieldId))
      {        
        try
        {
          Long senderCertUid = (Long)entity.getFieldValue(IGTGdocDetailEntity.SENDER_CERT);
          String senderCertName = "";
          if(senderCertUid != null)
          {
            GetCertificateEvent event = new GetCertificateEvent(senderCertUid);
            Map certMap = (Map)handleEvent(event);
            if(certMap==null)
            {
              throw new java.lang.NullPointerException("null response collection for GetCertificateEvent while retriving senderCertUID");
            }
            senderCertName = (String)certMap.get(ICertificate.NAME);
          }
          entity.setNewFieldValue(IGTGdocDetailEntity.SENDER_CERT_NAME, senderCertName);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
        }
      }
      else if(IGTGdocDetailEntity.RECEIVER_CERT_NAME.equals(fieldId))
      {
        try
        {
          Long receiverCertUid = (Long)entity.getFieldValue(IGTGdocDetailEntity.RECEIVER_CERT);   
          String receiverCertName = "";
          if(receiverCertUid != null)
          {
            GetCertificateEvent event = new GetCertificateEvent(receiverCertUid);
            Map certMap = (Map)handleEvent(event);
            if(certMap==null)
            {
              throw new java.lang.NullPointerException("null response collection for GetCertificateEvent while retriving receiverCertUID");
            }
            receiverCertName = (String)certMap.get(ICertificate.NAME);
            
          }
          entity.setNewFieldValue(IGTGdocDetailEntity.RECEIVER_CERT_NAME, receiverCertName);
          
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
        }
      } */
      else
      {
        throw new java.lang.IllegalStateException("Field " + fieldId + " of entity " + entity + " is not load-on-demand");
      }
    }      
    else
      super.loadField(fieldId, entity);
  }
  
  protected DataFilterImpl getAttachmentLinkFilter() throws GTClientException
  {
    DataFilterFactory ff = new DataFilterFactory();

    DataFilterImpl hua = new DataFilterImpl(); //has updated attachment filter
    hua.addSingleFilter(null,
                        IGridDocument.HAS_ATTACHMENT,
                        ff.getEqualOperator(),
                        Boolean.TRUE,
                        false);
    hua.addSingleFilter(hua.getAndConnector(),
                        IGridDocument.IS_ATTACHMENT_LINK_UPDATED,
                        ff.getEqualOperator(),
                        Boolean.TRUE,
                        false);

    DataFilterImpl noa = new DataFilterImpl(); //no attachment filter
    noa.addSingleFilter(null,
                        IGridDocument.HAS_ATTACHMENT,
                        ff.getEqualOperator(),
                        Boolean.FALSE,
                        false);

    DataFilterImpl attachmentLinkFilter = new DataFilterImpl(hua, ff.getOrConnector(), noa);

    return attachmentLinkFilter;
  }
  
  /**
   * Retrive all GridDocs.
   *
   * Jared: Overrided to return only GridDocs that has the attachment link
   * updated.
   * Andrew: Refactor to do attachment link filtering stuff with a DataFilterImpl
   * nb: at this stage getByKey doesnt do this filtering!
   *
   * @returns A collection of IGridDocumentEntity.
   * @throws GTClientException
   */
   public Collection getAll() throws GTClientException
  {
    try
    {
      DataFilterImpl attachmentLinkFilter = getAttachmentLinkFilter();
      IEvent event = getGetListEvent(attachmentLinkFilter);
      return handleGetListEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing getAll() request", t);
    }
  }
   
   private Collection<String> convertConcatenatedAttachmentFilenames(String concatAttachmentFilename)
   {
     ArrayList<String> attachmentFilenames = new ArrayList<String>();
     if(concatAttachmentFilename != null && concatAttachmentFilename.length() > 0)
     {
       StringTokenizer tokenizer = new StringTokenizer(concatAttachmentFilename, ":");
       while(tokenizer.hasMoreTokens())
       {
         attachmentFilenames.add(tokenizer.nextToken());
       }
     }
     return attachmentFilenames;
   }
   
   private String getCertNameByCertUID(Long certUID) throws Exception
   {
     String senderCertName = "";
     if(certUID != null)
     {
       GetCertificateEvent event = new GetCertificateEvent(certUID);
       Map certMap = (Map)handleEvent(event);
       if(certMap==null)
       {
         throw new java.lang.NullPointerException("null response collection for GetCertificateEvent while retriving senderCertUID");
       }
       senderCertName = (String)certMap.get(ICertificate.NAME);
     }
     return senderCertName;
   }
   
   private String getFilename(String filePath)
   {
     if(filePath == null || "".equals(filePath))
     {
       return filePath;
     }
     return FileUtil.extractFilename(filePath);
   }
}