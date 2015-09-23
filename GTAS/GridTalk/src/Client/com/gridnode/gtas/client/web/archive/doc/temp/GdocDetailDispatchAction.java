/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GdocDetailDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 27 Oct 2006    Regina Zeng         Created
 */
package com.gridnode.gtas.client.web.archive.doc.temp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.document.GridDocumentAForm;
import com.gridnode.gtas.client.web.archive.doc.temp.GdocDetailRenderer;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class GdocDetailDispatchAction extends EntityDispatchAction2
{
  protected static final Number[] _fields = new Number[]{
    IGTGdocDetailEntity.G_DOC_ID,
    IGTGdocDetailEntity.G_DOC_FILENAME,
    IGTGdocDetailEntity.FOLDER,
    IGTGdocDetailEntity.U_DOC_NUM,
    IGTGdocDetailEntity.U_DOC_VERSION,
    IGTGdocDetailEntity.U_DOC_DOC_TYPE,
    IGTGdocDetailEntity.U_DOC_FILENAME,
    IGTGdocDetailEntity.U_DOC_FILE_TYPE,
    IGTGdocDetailEntity.U_DOC_FILESIZE,
    IGTGdocDetailEntity.S_G_DOC_ID,
    IGTGdocDetailEntity.R_G_DOC_ID,
    IGTGdocDetailEntity.REF_G_DOC_ID,
    IGTGdocDetailEntity.REF_U_DOC_NUM,
    IGTGdocDetailEntity.REF_U_DOC_FILENAME,
    IGTGdocDetailEntity.SOURCE_FOLDER,
    IGTGdocDetailEntity.PARTNER_FUNCTION,
    IGTGdocDetailEntity.S_NODE_ID,
    IGTGdocDetailEntity.S_BIZ_ENTITY_ID,
    IGTGdocDetailEntity.S_PARTNER_ID,
    IGTGdocDetailEntity.S_PARTNER_NAME,
    IGTGdocDetailEntity.S_PARTNER_TYPE,
    IGTGdocDetailEntity.S_PARTNER_GROUP,
    IGTGdocDetailEntity.S_USER_ID,
    IGTGdocDetailEntity.S_USER_NAME,
    IGTGdocDetailEntity.R_NODE_ID,
    IGTGdocDetailEntity.R_BIZ_ENTITY_ID,
    IGTGdocDetailEntity.R_PARTNER_ID,
    IGTGdocDetailEntity.R_PARTNER_NAME,
    IGTGdocDetailEntity.R_PARTNER_TYPE,
    IGTGdocDetailEntity.R_PARTNER_GROUP,
    IGTGdocDetailEntity.DT_IMPORT,
    IGTGdocDetailEntity.DT_SEND_END,
    IGTGdocDetailEntity.DT_TRANSACTION_COMPLETE,
    IGTGdocDetailEntity.DT_RECEIVE_END,
    IGTGdocDetailEntity.DT_EXPORT,
    IGTGdocDetailEntity.DT_VIEW,
    IGTGdocDetailEntity.R_CHANNEL_NAME,
    IGTGdocDetailEntity.ENCRYPTION_LEVEL,
    IGTGdocDetailEntity.S_ROUTE,
    IGTGdocDetailEntity.PORT_NAME,
    IGTGdocDetailEntity.PROCESS_INSTANCE_ID, 
    IGTGdocDetailEntity.USER_TRACKING_ID, 
    IGTGdocDetailEntity.PROCESS_INSTANCE_UID,
    IGTGdocDetailEntity.AUDIT_FILENAME,
    //IGTGdocDetailEntity.RECEIPT_AUDIT_FILENAME,
    IGTGdocDetailEntity.DOC_TRANS_STATUS,
    IGTGdocDetailEntity.SENDER_CERT,
    IGTGdocDetailEntity.RECEIVER_CERT,
    IGTGdocDetailEntity.DOC_META_INFO_UID,
    IGTGdocDetailEntity.SENDER_CERT_NAME,
    IGTGdocDetailEntity.RECEIVER_CERT_NAME,
  };
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_GDOC_DETAIL;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new GdocDetailRenderer(rContext,edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if (edit) 
    { 
      throw new UnsupportedOperationException(); 
    }
    return IDocumentKeys.GDOC_DETAIL_PAGE;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    GridDocumentAForm form = (GridDocumentAForm)actionContext.getActionForm();
    IGTGdocDetailEntity instance = (IGTGdocDetailEntity)entity;
      
    Object piUid = instance.getFieldValue(IGTGdocDetailEntity.UID);  
        
    initFormFields(actionContext, _fields);
    form.setAttachmentFilenames(instance.getFieldStringArray(instance.ATTACHMENT_FILENAMES));
    form.setDocRemarks(instance.getFieldString(instance.DOC_REMARKS));
    form.setDocMetaInfoUID(instance.getFieldString(instance.DOC_META_INFO_UID));
    form.setAuditFileName(instance.getFieldString(instance.AUDIT_FILENAME));
    form.setReceiptAuditFile(instance.getFieldString(instance.RECEIPT_AUDIT_FILENAME));
    form.setSenderCert(instance.getFieldString(instance.SENDER_CERT));
    form.setReceiverCert(instance.getFieldString(instance.RECEIVER_CERT));
    form.setSenderCertName(instance.getFieldString(instance.SENDER_CERT_NAME));
    form.setReceiverCertName(instance.getFieldString(instance.RECEIVER_CERT_NAME));
    form.setHasAttachment(instance.getFieldString(instance.HAS_ATTACHMENT));
    //form.setAttachmentFilenames(instance.getFieldStringArray(instance.ATTACHMENT_FILENAMES));
    form.setUserTrackingId(instance.getFieldString(instance.USER_TRACKING_ID));
    form.setUdocNum(instance.getFieldString(instance.U_DOC_NUM));
    form.setDocTransStatus(instance.getFieldString(instance.DOC_TRANS_STATUS));
  }

  protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
  {
    return new GridDocumentAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_GDOC_DETAIL;
  }

  protected void validateActionForm(ActionContext actionContext, IGTEntity entity,
                                    ActionForm form, ActionErrors errors) throws GTClientException
  {
    throw new UnsupportedOperationException();
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity) throws GTClientException
  {
    throw new UnsupportedOperationException();
  }
  
  protected ActionForward getDivertForward(ActionContext actionContext, OperationContext opCon,
                                           ActionMapping mapping, String divertMapping) throws GTClientException
  { 
    ActionForward forward = mapping.findForward(divertMapping);
    if ("divertToViewAuditFile".equals(divertMapping))
    {
      try
      {
        GridDocumentAForm form = (GridDocumentAForm) actionContext.getActionForm();
        String docMetaInfoUID = form.getDocMetaInfoUID(); 
        if (StaticUtils.stringEmpty(docMetaInfoUID)) 
        { 
          throw new IllegalStateException("No docMetaInfoUid defined in action form"); 
        }

        IGTSession gtasSession = getGridTalkSession(actionContext);
        IGTGdocDetailManager mgr = (IGTGdocDetailManager) gtasSession.getManager(IGTManager.MANAGER_GDOC_DETAIL);
        IGTFieldMetaInfo fmi = mgr.getSharedFieldMetaInfo(IGTEntity.ENTITY_GDOC_DETAIL, IGTGdocDetailEntity.DOC_META_INFO_UID);
        IForeignEntityConstraint constraint = (IForeignEntityConstraint) fmi.getConstraint();
        
        String path = forward.getPath();
        path = StaticWebUtils.addParameterToURL(path, "keyField", (constraint.getKeyFieldName()).toLowerCase());
        path = StaticWebUtils.addParameterToURL(path, (constraint.getKeyFieldName()).toLowerCase(), docMetaInfoUID);
        forward = new ActionForward(path, forward.getRedirect());

        return processSOCForward( forward, opCon );
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error preparing forward path for docMetaInfo diversion", t);
      }
    }
    else if ("divertToViewSCert".equals(divertMapping))
    {
      try
      {
          GridDocumentAForm form = (GridDocumentAForm) actionContext.getActionForm();
          String senderCert = form.getSenderCert(); 
          if (StaticUtils.stringEmpty(senderCert)) 
          { 
            throw new IllegalStateException("No senderUid defined in action form"); 
          }
  
          IGTSession gtasSession = getGridTalkSession(actionContext);
          IGTGdocDetailManager mgr = (IGTGdocDetailManager) gtasSession.getManager(IGTManager.MANAGER_GDOC_DETAIL);
          IGTFieldMetaInfo fmi = mgr.getSharedFieldMetaInfo(IGTEntity.ENTITY_GDOC_DETAIL, IGTGdocDetailEntity.SENDER_CERT);
          IForeignEntityConstraint constraint = (IForeignEntityConstraint) fmi.getConstraint();
  
          String path = forward.getPath();
          path = StaticWebUtils.addParameterToURL(path, "keyField", (constraint.getKeyFieldName()).toLowerCase());
          path = StaticWebUtils.addParameterToURL(path, (constraint.getKeyFieldName()).toLowerCase(), senderCert);
          forward = new ActionForward(path, forward.getRedirect());

          return processSOCForward( forward, opCon );
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error preparing forward path for docMetaInfo diversion", t);
      }
    }
    else if ("divertToViewRCert".equals(divertMapping))
    {
      try
      {
          GridDocumentAForm form = (GridDocumentAForm) actionContext.getActionForm();
          String receiverCert = form.getReceiverCert(); 
          if (StaticUtils.stringEmpty(receiverCert)) 
          { 
            throw new IllegalStateException("No receiverUid defined in action form"); 
          }
  
          IGTSession gtasSession = getGridTalkSession(actionContext);
          IGTGdocDetailManager mgr = (IGTGdocDetailManager) gtasSession.getManager(IGTManager.MANAGER_GDOC_DETAIL);
          IGTFieldMetaInfo fmi = mgr.getSharedFieldMetaInfo(IGTEntity.ENTITY_GDOC_DETAIL, IGTGdocDetailEntity.RECEIVER_CERT);
          IForeignEntityConstraint constraint = (IForeignEntityConstraint) fmi.getConstraint();
  
          String path = forward.getPath();
          path = StaticWebUtils.addParameterToURL(path, "keyField", (constraint.getKeyFieldName()).toLowerCase());
          path = StaticWebUtils.addParameterToURL(path, (constraint.getKeyFieldName()).toLowerCase(), receiverCert);
          forward = new ActionForward(path, forward.getRedirect());

          return processSOCForward( forward, opCon );
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error preparing forward path for docMetaInfo diversion", t);
      }
    }
    else
    {
      return super.getDivertForward(actionContext, opCon, mapping, divertMapping);
    }
  }
}
