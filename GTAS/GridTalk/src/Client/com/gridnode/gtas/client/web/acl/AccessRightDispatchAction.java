/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-09-23     Andrew Hill         Renamed action to actionName to avoid conflict in JS
 * 2003-07-22     Andrew Hill         Remove deprecated getNavgroup()
 */
package com.gridnode.gtas.client.web.acl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class AccessRightDispatchAction extends EntityDispatchAction
{

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ACCESS_RIGHT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    Collection dataTypes = null;
    Collection actions = null;

    AccessRightAForm form = (AccessRightAForm)actionContext.getActionForm();

    IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
    IGTFeatureManager featureMgr = (IGTFeatureManager)gtasSession.getManager(IGTManager.MANAGER_FEATURE);

    if(edit)
    { //If we are editing then lookup the choices for the listboxes
      try
      {
        String selectedFeature = form.getFeature();
        if( (selectedFeature != null) && !"".equals(selectedFeature))
        {
          IGTFeatureEntity feature = featureMgr.getFeatureByFeature(selectedFeature);
          if(feature != null)
          {
            actions = (Collection)feature.getFieldValue(IGTFeatureEntity.ACTIONS);
            dataTypes = (Collection)feature.getFieldValue(IGTFeatureEntity.DATA_TYPES);
          }
        }
      }
      catch(GTClientException gtce)
      { // Catch problem getting specified feature
        throw new GTClientException("Error preparing list of datatypes and actions",gtce);
      }
      if(actions == null)
      {
        actions = new ArrayList();
      }
      if(dataTypes == null)
      {
        dataTypes = new ArrayList();
      }
    }
    return new AccessRightRenderer(rContext, edit, actions, dataTypes);
  }

  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ACCESS_RIGHT_UPDATE : IDocumentKeys.ACCESS_RIGHT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
    }
    else
    {
      AccessRightAForm form = (AccessRightAForm)actionContext.getActionForm();
      form.setRole(entity.getFieldString(IGTAccessRightEntity.ROLE));
      form.setDescription((String)entity.getFieldValue(IGTAccessRightEntity.DESCRIPTION));
      form.setFeature((String)entity.getFieldValue(IGTAccessRightEntity.FEATURE));
      form.setDataType((String)entity.getFieldValue(IGTAccessRightEntity.DATA_TYPE));
      form.setActionName((String)entity.getFieldValue(IGTAccessRightEntity.ACTION));
      //form.setCriteria((String)entity.getFieldValue(IGTAccessRightEntity.CRITERIA));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new AccessRightAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ACCESS_RIGHT;
  }

  protected boolean doSave(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    //ISimpleResourceLookup rLookup = 
	createResourceLookup(actionContext);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());

    if(opContext == null)
      throw new java.lang.NullPointerException("No OperationContext found");

    IGTEntity entity = (IGTEntity)opContext.getAttribute(IOperationContextKeys.ENTITY);
    if(entity == null)
      throw new java.lang.NullPointerException("No entity found in Operation Context");

    AccessRightAForm form = (AccessRightAForm)actionContext.getActionForm();


    // Validate the request parameters specified by the user
    boolean failed = false;
    basicValidateString(errors,"feature",form.getFeature(),IGTAccessRightEntity.FEATURE,entity);
    basicValidateString(errors,"description",form.getDescription(),IGTAccessRightEntity.DESCRIPTION,entity);
    basicValidateString(errors,"dataType",form.getDataType(),IGTAccessRightEntity.DATA_TYPE,entity);
    basicValidateString(errors,"action",form.getActionName(),IGTAccessRightEntity.ACTION,entity);
    basicValidateString(errors,"role",form.getRole(),IGTAccessRightEntity.ROLE,entity);
    failed = (errors.size() != 0);
    if(!failed)
    {
      entity.setFieldValue(IGTAccessRightEntity.FEATURE, form.getFeature());
      entity.setFieldValue(IGTAccessRightEntity.DESCRIPTION, form.getDescription());
      entity.setFieldValue(IGTAccessRightEntity.DATA_TYPE, form.getDataType());
      entity.setFieldValue(IGTAccessRightEntity.ACTION, form.getActionName());
      entity.setFieldValue(IGTAccessRightEntity.ROLE, new Long(form.getRole()));

      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTManager manager = gtasSession.getManager(IGTManager.MANAGER_ACCESS_RIGHT);
      try
      {
        if(entity.isNewEntity())
        {
          manager.create(entity);
        }
        else
        {
          manager.update(entity);
        }
      }
      catch(Exception e)
      {
        throw new GTClientException("Error saving AccessRight",e);
      }
    }
    return failed;
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    // If diverted to by the Role screen we may have been passed a role uid
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    AccessRightAForm form = (AccessRightAForm)actionContext.getActionForm();
    String role = (String)opCon.getAttribute("role.uid");
    if(role != null)
    {
      form.setRole(role);
    }
  }  
}