/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SysFoldersDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-26     Andrew Hill         Created
 * 2003-02-13     Jared Low           Added code to transfer attachments.
 * 2003-08-04     Andrew Hill         Eliminate deprecated getNavgroup() method
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTImportDocuments;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SysFoldersDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_GRID_DOCUMENT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ImportDocumentsRenderer(rContext,edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(edit)
    {
      return IDocumentKeys.GRID_DOCUMENT_IMPORT;
    }
    else
    {
      throw new GTClientException("ImportDocuments does not support a view mode");
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ImportDocumentsAForm form = (ImportDocumentsAForm)actionContext.getActionForm();
    form.setIsManual("true");
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ImportDocumentsAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_GRID_DOCUMENT;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    ImportDocumentsAForm importDocumentsAForm = (ImportDocumentsAForm)actionContext.getActionForm();
    IGTImportDocuments importDocs = (IGTImportDocuments)entity;

    Boolean isManual = StaticUtils.booleanValue( importDocumentsAForm.getIsManual() );
    importDocs.setFieldValue( importDocs.IS_MANUAL, isManual );

    if(isManual.booleanValue())
    {
      basicValidateString(errors, importDocs.SENDER_ID, importDocumentsAForm, importDocs);
      basicValidateString(errors, importDocs.DOC_TYPE, importDocumentsAForm, importDocs);

      String[] recipients = importDocumentsAForm.getRecipients();
      if( (recipients == null) || (recipients.length == 0) )
      {
        errors.add("recipients",new ActionError("importDocuments.error.recipients.required"));
      }
      basicValidateFiles(errors,importDocs.FILENAMES,importDocumentsAForm,importDocs);
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Not implemented - inbound import");
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ImportDocumentsAForm form = (ImportDocumentsAForm)actionContext.getActionForm();
    IGTImportDocuments importDocs = (IGTImportDocuments)entity;

    Boolean isManual = StaticUtils.booleanValue( form.getIsManual() );
    importDocs.setFieldValue( importDocs.IS_MANUAL, isManual );

    if(isManual.booleanValue())
    {
      importDocs.setFieldValue( importDocs.SENDER_ID, form.getSenderId() );
      importDocs.setFieldValue( importDocs.DOC_TYPE, form.getDocType() );
      importDocs.setFieldValue( importDocs.RECIPIENTS, StaticUtils.collectionValue(form.getRecipients()) );
      transferFieldFiles(actionContext, importDocs, importDocs.FILENAMES, false );
      transferFieldFiles(actionContext, importDocs, importDocs.ATTACHMENTS, false); // Jared: Added.
    }
    else
    {
      importDocs.setFieldValue( importDocs.G_DOC_UIDS, StaticUtils.collectionValue(form.getGdocUids()) );
    }
  }
}