/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPublishBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchRegistryQueryManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PublishBusinessEntityDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PublishBusinessEntityRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(!edit) throw new UnsupportedOperationException("PublishBusinessEntity does not supprt viewing");
    return IDocumentKeys.PUBLISH_BUSINESS_ENTITY;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    PublishBusinessEntityAForm form = (PublishBusinessEntityAForm)actionContext.getActionForm();

    //form.setBe                  (entity.getFieldStringArray (IGTPublishBusinessEntityEntity.BE));
    //form.setRegistryConnectInfo (entity.getFieldString      (IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO));
    
    // initialise it from 
    String[] beUids = actionContext.getRequest().getParameterValues("uid");
    form.setBe(beUids);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PublishBusinessEntityAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SEARCH_REGISTRY_QUERY;
  }

  protected IGTEntity getNewEntityInstance( ActionContext actionContext,
                                            IGTManager manager)
    throws GTClientException
  {
    return ((IGTSearchRegistryQueryManager)manager).newPublishBusinessEntity();
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    basicValidateStringArray(actionErrors, IGTPublishBusinessEntityEntity.BE,                    form, entity);
    basicValidateString     (actionErrors, IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO, form, entity);
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    PublishBusinessEntityAForm form = (PublishBusinessEntityAForm)actionContext.getActionForm();

    entity.setFieldValue(IGTPublishBusinessEntityEntity.BE,                     form.getBe());
    entity.setFieldValue(IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO,  form.getRegistryConnectInfo());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTPublishBusinessEntityEntity publishBusinessEntity = (IGTPublishBusinessEntityEntity)entity;

      // Create a Collection of Long(s)
      String[] beUidsArray = publishBusinessEntity.getFieldStringArray(IGTPublishBusinessEntityEntity.BE);
      Collection beUids = new Vector(beUidsArray.length);
      for(int i = 0; i < beUidsArray.length; i++)
      {
        beUids.add(StaticUtils.longValue(beUidsArray[i]));
      }

      Collection businessEntitiyUids = beUids;
      String registryConnectInfoName = publishBusinessEntity.getFieldString(IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO);
      
      ((IGTSearchRegistryQueryManager)manager).publishBusinessEntity(businessEntitiyUids, registryConnectInfoName);      
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error publishing business entity: " + entity, t);
    }
  }}