/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-04-10     Andrew Hill         Upgraded abort and delete to support non elv diverted opCon'ed requests
 * 2003-08-20     Andrew Hill         Added support for userTrackingId field
 **/
package com.gridnode.gtas.client.web.bp;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessInstanceEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessInstanceManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ProcessInstanceDispatchAction extends EntityDispatchAction2
{
  private static final String MANUAL_ABORT_REASON = "User Cancelled"; //20030410AH - Made a const - but its still not good..

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PROCESS_INSTANCE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ProcessInstanceRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(edit) throw new java.lang.UnsupportedOperationException("ProcessInstance entity does not support editing");
    return IDocumentKeys.PROCESS_INSTANCE_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessInstanceEntity instance = (IGTProcessInstanceEntity)entity;

    ProcessInstanceAForm form = (ProcessInstanceAForm)actionContext.getActionForm();

    form.setProcessInstanceId(instance.getFieldString(IGTProcessInstanceEntity.PROCESS_INSTANCE_ID) );
    form.setPartner(          instance.getFieldString(IGTProcessInstanceEntity.PARTNER) );
    form.setState(            instance.getFieldString(IGTProcessInstanceEntity.STATE) );
    form.setStartTime(        instance.getFieldString(IGTProcessInstanceEntity.START_TIME) );
    form.setEndTime(          instance.getFieldString(IGTProcessInstanceEntity.END_TIME) );
    form.setRetryNum(         instance.getFieldString(IGTProcessInstanceEntity.RETRY_NUM) );
    form.setIsFailed(         instance.getFieldString(IGTProcessInstanceEntity.IS_FAILED) );
    form.setFailReason(       instance.getFieldString(IGTProcessInstanceEntity.FAIL_REASON) );
    form.setDetailReason(     instance.getFieldString(IGTProcessInstanceEntity.DETAIL_REASON) );
    form.setProcessDefName(   instance.getFieldString(IGTProcessInstanceEntity.PROCESS_DEF_NAME) );
    form.setRoleType(         instance.getFieldString(IGTProcessInstanceEntity.ROLE_TYPE) );
    form.setUserTrackingId(   instance.getFieldString(IGTProcessInstanceEntity.USER_TRACKING_IDENTIFIER) ); //20030820AH

    List assocDocsList = (List)instance.getFieldValue(IGTProcessInstanceEntity.ASSOC_DOCS);
    form.initAssocDocsListOrder(assocDocsList == null ? 0 : assocDocsList.size());
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ProcessInstanceAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PROCESS_INSTANCE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("ProcessInstance entity does not support editing");
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("ProcessInstance entity does not support editing");
  }

  public ActionForward abort(ActionMapping mapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
    throws IOException, ServletException, GTClientException
  {
    try
    {
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response); //20030310AH
      String[] uids = getDeleteIds(actionContext); //20030310AH
      ActionForward forward = getDeleteReturnForward(actionContext); //20030410AH
      if( (uids == null) || (uids.length == 0) )
      {
        return forward;
      }
      IGTSession gtasSession = getGridTalkSession(actionContext); //20030410AH
      IGTProcessInstanceManager manager = (IGTProcessInstanceManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_INSTANCE);
      Collection processes = StaticUtils.getLongCollection(uids);
      manager.abort(processes, MANUAL_ABORT_REASON); //20030410AH
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error aborting ProcessInstances entitities",t);
    }
  }

  public void doDelete(ActionContext actionContext)
    throws GTClientException
  { //20030709AH - Modified to be doDelete() method
    //20030724AH - Corrected method signature to override correct method! (oops), remove try/catch
    String[] uids = getDeleteIds(actionContext); //20030310AH

    if( (uids == null) || (uids.length == 0) )
    {
      return;
    }
    IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
    IGTProcessInstanceManager manager = (IGTProcessInstanceManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_INSTANCE);
    Collection processes = StaticUtils.getLongCollection(uids);
    manager.delete(processes, Boolean.FALSE);
  }

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
    if( "divertViewGridDocument".equals(divertTo) )
    {

      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"uid",fuid),
                          divertForward.getRedirect());

    }
    return processSOCForward( divertForward, opCon );
  }
}