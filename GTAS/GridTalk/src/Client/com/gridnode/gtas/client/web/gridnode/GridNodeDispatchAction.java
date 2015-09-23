/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 * 2003-04-25     Andrew Hill         Fixed deactivate() method (was using bad oldui style)
 */
package com.gridnode.gtas.client.web.gridnode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class GridNodeDispatchAction extends EntityDispatchAction2
{
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_TRIGGER;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new GridNodeRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return IDocumentKeys.GRIDNODE_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    GridNodeAForm form = (GridNodeAForm)actionContext.getActionForm();
    IGTGridNodeEntity gridNode = (IGTGridNodeEntity)entity;

    form.setId( gridNode.getFieldString(gridNode.ID) );
    form.setName( gridNode.getFieldString(gridNode.NAME) );
    form.setCategory( gridNode.getFieldString(gridNode.CATEGORY) );
    form.setState( gridNode.getFieldString(gridNode.STATE) );
    form.setActivationReason( gridNode.getFieldString(gridNode.ACTIVATION_REASON) );
    form.setDtCreated( gridNode.getFieldString(gridNode.DT_CREATED) );
    form.setDtReqActivate( gridNode.getFieldString(gridNode.DT_REQ_ACTIVATE) );
    form.setDtActivated( gridNode.getFieldString(gridNode.DT_ACTIVATED) );
    form.setDtDeactivated( gridNode.getFieldString(gridNode.DT_DEACTIVATED) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new GridNodeAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_GRIDNODE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    throw new NotApplicableException("validateActionForm() not applicable to GridNode");
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    throw new NotApplicableException("updateEntityFields() not applicable to GridNode");
  }

  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  {
    ActionForward listview = super.getCancelForward(actionContext);
    GridNodeAForm form = (GridNodeAForm)actionContext.getActionForm();
    String state = form.getState();
    if(state != null)
    {
      String url = StaticWebUtils.addParameterToURL(listview.getPath(),"state",state);
      listview = new ActionForward(url, listview.getRedirect());
    }
    return listview;
  }

  public ActionForward deactivate(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    try
    {
      //20030425AH - was using old code. Fixed now.
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
      String[] uids = getDeleteIds(actionContext);
      ActionForward forward = getDeleteReturnForward(actionContext);
      //..........

      if( (uids == null) || (uids.length == 0) )
      {
        return forward;
      }
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTGridNodeManager manager = (IGTGridNodeManager)gtasSession.getManager(IGTManager.MANAGER_GRIDNODE);
      long[] gridNodes = StaticUtils.primitiveLongArrayValue(uids);
      manager.deactivate(gridNodes);
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error deactivating gridNodes",t);
    }
  }
}