/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTDocumentTypeEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class DocumentTypeDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_DOCUMENT_TYPE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new DocumentTypeRenderer(rContext, edit);
  }

  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.DOCUMENT_TYPE_UPDATE : IDocumentKeys.DOCUMENT_TYPE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
      
    }
    else
    {
      DocumentTypeAForm form = (DocumentTypeAForm)actionContext.getActionForm();
      form.setDocType((String)entity.getFieldValue(IGTDocumentTypeEntity.DOC_TYPE));
      form.setDescription((String)entity.getFieldValue(IGTDocumentTypeEntity.DESCRIPTION));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new DocumentTypeAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_DOCUMENT_TYPE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    basicValidateString(errors, IGTDocumentTypeEntity.DOC_TYPE, form, entity);
    basicValidateString(errors, IGTDocumentTypeEntity.DESCRIPTION, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    DocumentTypeAForm documentTypeAForm = (DocumentTypeAForm)actionContext.getActionForm();
    entity.setFieldValue(IGTDocumentTypeEntity.DOC_TYPE, documentTypeAForm.getDocType());
    entity.setFieldValue(IGTDocumentTypeEntity.DESCRIPTION, documentTypeAForm.getDescription());
  }
}