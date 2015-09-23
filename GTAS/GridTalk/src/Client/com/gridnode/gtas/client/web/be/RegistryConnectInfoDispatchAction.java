/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRegistryConnectInfoEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class RegistryConnectInfoDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_REGISTRY_CONNECT_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new RegistryConnectInfoRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.REGISTRY_CONNECT_INFO_UPDATE : IDocumentKeys.REGISTRY_CONNECT_INFO_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    RegistryConnectInfoAForm form = (RegistryConnectInfoAForm)actionContext.getActionForm();

    form.setName            (entity.getFieldString(IGTRegistryConnectInfoEntity.NAME));
    form.setQueryUrl        (entity.getFieldString(IGTRegistryConnectInfoEntity.QUERY_URL));
    form.setPublishUrl      (entity.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_URL));
    form.setPublishUser     (entity.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_USER));
    form.setPublishPassword (entity.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new RegistryConnectInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_REGISTRY_CONNECT_INFO;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    RegistryConnectInfoAForm registryConnectInfoForm = (RegistryConnectInfoAForm)form; 
    
    basicValidateString(actionErrors, IGTRegistryConnectInfoEntity.NAME,              form, entity);
    basicValidateString(actionErrors, IGTRegistryConnectInfoEntity.QUERY_URL,         form, entity);
    basicValidateString(actionErrors, IGTRegistryConnectInfoEntity.PUBLISH_URL,       form, entity);
    if(StaticUtils.stringNotEmpty(registryConnectInfoForm.getPublishUrl()))
    {
      basicValidateString(actionErrors, IGTRegistryConnectInfoEntity.PUBLISH_USER,      form, entity);
      basicValidateString(actionErrors, IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD,  form, entity);
    }
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    RegistryConnectInfoAForm form = (RegistryConnectInfoAForm)actionContext.getActionForm();

    entity.setFieldValue(IGTRegistryConnectInfoEntity.NAME,             form.getName());
    entity.setFieldValue(IGTRegistryConnectInfoEntity.QUERY_URL,        form.getQueryUrl());
    entity.setFieldValue(IGTRegistryConnectInfoEntity.PUBLISH_URL,      form.getPublishUrl());
    entity.setFieldValue(IGTRegistryConnectInfoEntity.PUBLISH_USER,     form.getPublishUser());
    entity.setFieldValue(IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD, form.getPublishPassword());
  }
}