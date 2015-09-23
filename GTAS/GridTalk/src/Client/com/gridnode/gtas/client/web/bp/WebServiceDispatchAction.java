/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WebServiceDispatchAction.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTWebServiceEntity;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class WebServiceDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_WEBSERVICE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new WebServiceRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.WEBSERVICE_UPDATE : IDocumentKeys.WEBSERVICE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTWebServiceEntity webService = (IGTWebServiceEntity)entity;

    WebServiceAForm form = (WebServiceAForm)actionContext.getActionForm();
    form.setServiceName(webService.getFieldString(IGTWebServiceEntity.SERVICE_NAME));
    form.setServiceGroup(IGTWebServiceEntity.INTERNAL_GROUP);
    form.setWsdlUrl(webService.getFieldString(IGTWebServiceEntity.WSDL_URL));
    form.setEndPoint(webService.getFieldString(IGTWebServiceEntity.END_POINT));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new WebServiceAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_WEBSERVICE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTWebServiceEntity webService = (IGTWebServiceEntity)entity;
    //WebServiceAForm tform = (WebServiceAForm)form;
    
    basicValidateString(errors, IGTWebServiceEntity.SERVICE_NAME, form, entity);
    basicValidateString(errors, IGTWebServiceEntity.SEVICE_GROUP, form, entity);
    if(basicValidateString(errors, IGTWebServiceEntity.WSDL_URL, form, entity))
    {
      basicValidateUrl(errors, IGTWebServiceEntity.WSDL_URL, form, entity);
    }
    if(basicValidateString(errors, IGTWebServiceEntity.END_POINT, form, entity))
    {
      basicValidateUrl(errors, IGTWebServiceEntity.END_POINT, form, entity);
    }    
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTWebServiceEntity webService = (IGTWebServiceEntity)entity;
    WebServiceAForm form = (WebServiceAForm)actionContext.getActionForm();
    webService.setFieldValue(IGTWebServiceEntity.SERVICE_NAME, form.getServiceName());
    webService.setFieldValue(IGTWebServiceEntity.SEVICE_GROUP, IGTWebServiceEntity.INTERNAL_GROUP);
    webService.setFieldValue(IGTWebServiceEntity.WSDL_URL, form.getWsdlUrl());
    webService.setFieldValue(IGTWebServiceEntity.END_POINT, form.getEndPoint());
  }

}