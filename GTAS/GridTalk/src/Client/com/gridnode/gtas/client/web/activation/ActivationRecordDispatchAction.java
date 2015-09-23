/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-15     Andrew Hill         Created
 * 2002-12-26     Andrew Hill         abortMultiple(), denyMultiple()
 * 2003-04-25     Andrew Hill         Update abortMultiple(),denyMultiple() to newui methodology
 */
package com.gridnode.gtas.client.web.activation;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ActivationRecordDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ACTIVATION_RECORD;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ActivationRecordRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ACTIVATION_RECORD_UPDATE : IDocumentKeys.ACTIVATION_RECORD_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ActivationRecordAForm form = (ActivationRecordAForm)actionContext.getActionForm();
    IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
    IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)entity.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);

    form.setDoAction( (Short)record.getFieldValue(IGTActivationRecordEntity.DO_ACTION) );
    form.setActDirection( record.getFieldString(IGTActivationRecordEntity.ACT_DIRECTION) );
    form.setDeactDirection( record.getFieldString(IGTActivationRecordEntity.DEACT_DIRECTION) );
    form.setGridNodeId( record.getFieldString(IGTActivationRecordEntity.GRIDNODE_ID) );
    form.setGridNodeName( record.getFieldString(IGTActivationRecordEntity.GRIDNODE_NAME) );
    form.setDtRequested( record.getFieldString(IGTActivationRecordEntity.DT_REQUESTED) );
    form.setDtApproved( record.getFieldString(IGTActivationRecordEntity.DT_APPROVED) );
    form.setDtAborted( record.getFieldString(IGTActivationRecordEntity.DT_ABORTED) );
    form.setDtDenied( record.getFieldString(IGTActivationRecordEntity.DT_DENIED) );
    form.setDtDeactivated( record.getFieldString(IGTActivationRecordEntity.DT_DEACTIVATED) );
    form.setIsLatest( record.getFieldString(IGTActivationRecordEntity.IS_LATEST) );
    form.setCurrentType( record.getFieldString(IGTActivationRecordEntity.CURRENT_TYPE) );
    form.setActivateReason( activation.getFieldString(IGTGridNodeActivationEntity.ACTIVATE_REASON) );
    form.setRequestorBeList(StaticUtils.getStringArray((Collection)
                            activation.getFieldValue(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST)));
    form.setApproverBeList(StaticUtils.getStringArray((Collection)
                            activation.getFieldValue(IGTGridNodeActivationEntity.APPROVER_BE_LIST)));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ActivationRecordAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ACTIVATION_RECORD;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    ActivationRecordAForm form = (ActivationRecordAForm)actionForm;
    IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
    IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)record.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);
    Short doAction = form.getDoAction();
    if(IGTActivationRecordEntity.DO_ACTION_REQUEST.equals(doAction))
    { // If its new then we are doing a request activation submission
      basicValidateString(errors,IGTActivationRecordEntity.GRIDNODE_ID,form,record);
      basicValidateString(errors,IGTActivationRecordEntity.GRIDNODE_NAME,form,record);
      basicValidateString(errors,IGTGridNodeActivationEntity.ACTIVATE_REASON,form,activation);
      basicValidateKeys(errors,IGTGridNodeActivationEntity.REQUESTOR_BE_LIST,form,activation);
    }
    else if(IGTActivationRecordEntity.DO_ACTION_APPROVE.equals(doAction))
    {
      basicValidateKeys(errors,IGTGridNodeActivationEntity.APPROVER_BE_LIST,form,activation);
    }
    else if(IGTActivationRecordEntity.DO_ACTION_ABORT.equals(doAction))
    {
    }
    else if(IGTActivationRecordEntity.DO_ACTION_DENY.equals(doAction))
    {
    }
    else
    {
      throw new IllegalStateException("Bad doAction value:" + doAction); //20030425AH
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ActivationRecordAForm form = (ActivationRecordAForm)actionContext.getActionForm();
    IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
    IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)record.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);
    Short doAction = form.getDoAction();
    record.setFieldValue(IGTActivationRecordEntity.DO_ACTION, doAction);
    if(IGTActivationRecordEntity.DO_ACTION_REQUEST.equals(doAction))
    {
      record.setFieldValue( IGTActivationRecordEntity.GRIDNODE_ID, StaticUtils.integerValue(form.getGridNodeId()) );
      record.setFieldValue( IGTActivationRecordEntity.GRIDNODE_NAME, form.getGridNodeName() );
      activation.setFieldValue( IGTGridNodeActivationEntity.ACTIVATE_REASON, form.getActivateReason() );
      activation.setFieldValue( IGTGridNodeActivationEntity.REQUESTOR_BE_LIST,
                                StaticUtils.getLongCollection(form.getRequestorBeList()) );
    }
    else if(IGTActivationRecordEntity.DO_ACTION_APPROVE.equals(doAction))
    {
      activation.setFieldValue( IGTGridNodeActivationEntity.APPROVER_BE_LIST,
                                StaticUtils.getLongCollection(form.getApproverBeList()) );
    }
    else if(IGTActivationRecordEntity.DO_ACTION_ABORT.equals(doAction))
    {
    }
    else if(IGTActivationRecordEntity.DO_ACTION_DENY.equals(doAction))
    {
    }
    else
    {
      throw new java.lang.IllegalStateException("Invalid doAction value:" + doAction);
    }
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTActivationRecordEntity record = (IGTActivationRecordEntity)entity;
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    Integer gridNodeId = (Integer)opCon.getAttribute(IGTActivationRecordEntity.GRIDNODE_ID);
    if(gridNodeId != null)
    {
      record.setFieldValue(IGTActivationRecordEntity.GRIDNODE_ID, gridNodeId);
    }
    String gridNodeName = (String)opCon.getAttribute(IGTActivationRecordEntity.GRIDNODE_NAME);
    if(gridNodeName != null)
    {
      record.setFieldValue(IGTActivationRecordEntity.GRIDNODE_NAME, gridNodeName);
    }
  }

  public ActionForward abortMultiple(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    try
    { //20030425AH - Refactor to use newui methodology
      ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
      ActionForward forward = getDeleteReturnForward(actionContext);
      long[] uids = StaticUtils.primitiveLongArrayValue( getDeleteIds(actionContext) );
      
      if(!((uids == null) || (uids.length == 0)) )
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTActivationRecordManager manager =  (IGTActivationRecordManager)
                                              gtasSession.getManager(IGTManager.MANAGER_ACTIVATION_RECORD);
        manager.abort(uids);
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error aborting activation requests",t);
    }
  }

  public ActionForward denyMultiple(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    try
    { //20030425AH - Refactor to use newui methodology
      ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
      ActionForward forward = getDeleteReturnForward(actionContext);
      long[] uids = StaticUtils.primitiveLongArrayValue( getDeleteIds(actionContext) );
      if(!((uids == null) || (uids.length == 0)) )
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTActivationRecordManager manager =  (IGTActivationRecordManager)
                                              gtasSession.getManager(IGTManager.MANAGER_ACTIVATION_RECORD);
        manager.deny(uids);
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error denying activation requests",t);
    }
  }


  public ActionForward approve(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    ((ActivationRecordAForm)actionForm).setDoAction(IGTActivationRecordEntity.DO_ACTION_APPROVE);
    return save(mapping, actionForm, request, response);
  }

  public ActionForward deny(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    ((ActivationRecordAForm)actionForm).setDoAction(IGTActivationRecordEntity.DO_ACTION_DENY);
    return save(mapping, actionForm, request, response);
  }

  public ActionForward abort(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws Exception
  {
    ((ActivationRecordAForm)actionForm).setDoAction(IGTActivationRecordEntity.DO_ACTION_ABORT);
    return save(mapping, actionForm, request, response);
  }
}