/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now under navgroup_server
 * 2003-07-16     Andrew Hill         Kill/modify deprecated overrides
 * 2003-07-22     Andrew Hill         Remove deprecated getNavgroup()
 */
package com.gridnode.gtas.client.web.acl;

import java.util.Collection;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class RoleDispatchAction extends EntityDispatchAction
{
  protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  {
    String fuid = actionContext.getRequest().getParameter("singleFuid");
    ActionForward divertForward = mapping.findForward(divertTo);
    if(divertForward == null)
    {
      throw new GTClientException("No mapping found for " + divertTo);
    }
    if( ("divertUpdateAccessRight".equals(divertTo))
        || ("divertViewAccessRight".equals(divertTo))
        || ("divertDeleteAccessRight".equals(divertTo)) )
    {

      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"uid",fuid),
                          divertForward.getRedirect());

    }
    return processSOCForward( divertForward, opCon );
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ROLE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTAccessRightManager arMgr = (IGTAccessRightManager)gtasSession.getManager(IGTManager.MANAGER_ACCESS_RIGHT);
    Collection accessRights = arMgr.getAccessRightsForRole((IGTRoleEntity)getEntity(actionContext));
    return new RoleRenderer(rContext, edit, accessRights);
  }

  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ROLE_UPDATE : IDocumentKeys.ROLE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
    }
    else
    {
      RoleAForm form = (RoleAForm)actionContext.getActionForm();
      form.setRole((String)entity.getFieldValue(IGTRoleEntity.ROLE));
      form.setDescription((String)entity.getFieldValue(IGTRoleEntity.DESCRIPTION));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new RoleAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ROLE;
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

    RoleAForm form = (RoleAForm)actionContext.getActionForm();


    // Validate the request parameters specified by the user
    boolean failed = false;
    basicValidateString(errors,"description",form.getDescription(),IGTRoleEntity.DESCRIPTION,entity);
    basicValidateString(errors,"role",form.getRole(),IGTRoleEntity.ROLE,entity);
    failed = (errors.size() != 0);
    if(!failed)
    {
      entity.setFieldValue(IGTRoleEntity.DESCRIPTION, form.getDescription());
      entity.setFieldValue(IGTRoleEntity.ROLE, form.getRole());

      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTManager manager = gtasSession.getManager(getIGTManagerType(actionContext));
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
        throw new GTClientException("Error saving " + getEntityName(),e);
      }
    }
    return failed;
  }

  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext thisOperation,
                                  OperationContext childOperation,
                                  String mapping) //20030722AH - Add mapping param to support non deprecated signature
    throws GTClientException
  {
    IGTRoleEntity entity = (IGTRoleEntity)getEntity(actionContext);
    //String divertTo = (String)actionContext.getRequest().getParameter("divertTo");
    childOperation.setAttribute("role.uid", entity.getFieldString(IGTRoleEntity.UID));
  }


}