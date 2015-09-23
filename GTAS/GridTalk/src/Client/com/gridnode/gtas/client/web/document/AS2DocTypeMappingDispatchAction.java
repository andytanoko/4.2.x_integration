/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAS2DocTypeMappingEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class AS2DocTypeMappingDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
  throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";
  }
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_AS2_DOC_TYPE_MAPPING;
  }
  
  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new AS2DocTypeMappingRenderer(rContext, edit);
  }
  
  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
  throws GTClientException
  {
    return null;
  }
  
  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
  throws GTClientException
  {
    return (edit ? IDocumentKeys.AS2_DOC_TYPE_MAPPING_UPDATE : IDocumentKeys.AS2_DOC_TYPE_MAPPING_VIEW);
  }
  
  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
  throws GTClientException
  {
    if(entity.isNewEntity())
    {
      
    }
    else
    {
      AS2DocTypeMappingAForm form = (AS2DocTypeMappingAForm)actionContext.getActionForm();
      form.setAs2DocType((String)entity.getFieldValue(IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE));
      form.setDocType((String)entity.getFieldValue(IGTAS2DocTypeMappingEntity.DOC_TYPE));
      form.setPartnerId((String)entity.getFieldValue(IGTAS2DocTypeMappingEntity.PARTNER_ID));
    }
  }
  
  protected ActionForm createActionForm(ActionContext actionContext)
  throws GTClientException
  {
    return new AS2DocTypeMappingAForm();
  }
  
  protected int getIGTManagerType(ActionContext actionContext)
  throws GTClientException
  {
    return IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING;
  }
  
  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    basicValidateString(errors, IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE, form, entity);
    basicValidateString(errors, IGTAS2DocTypeMappingEntity.DOC_TYPE, form, entity);
    basicValidateString(errors, IGTAS2DocTypeMappingEntity.PARTNER_ID, form, entity);
  }
  
  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
  throws GTClientException
  {
    AS2DocTypeMappingAForm as2DocTypeMappingAForm = (AS2DocTypeMappingAForm)actionContext.getActionForm();
    entity.setFieldValue(IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE, as2DocTypeMappingAForm.getAs2DocType());
    entity.setFieldValue(IGTAS2DocTypeMappingEntity.DOC_TYPE, as2DocTypeMappingAForm.getDocType());
    entity.setFieldValue(IGTAS2DocTypeMappingEntity.PARTNER_ID, as2DocTypeMappingAForm.getPartnerId());
  }
}
