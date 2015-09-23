/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReturnDefDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-06-04     Andrew Hill         Remove deprecated getNavgroup() method
 * 2003-06-13     Daniel D'Cotta      Added value1 & value2 when operator is 'between'
 */
package com.gridnode.gtas.client.web.bp;

import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ReturnDefDispatchAction extends EntityDispatchAction2
{
  private static final String INDEX_KEY = "returnDef index";

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_RETURN_DEF;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ReturnDefRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.RETURN_DEF_UPDATE : IDocumentKeys.RETURN_DEF_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTReturnDefEntity returnDef = (IGTReturnDefEntity)entity;

    ReturnDefAForm form = (ReturnDefAForm)actionContext.getActionForm();
    form.setOperator(   returnDef.getFieldString(IGTReturnDefEntity.RETURN_LIST_OPERATOR));
    form.setValue(      returnDef.getFieldString(IGTReturnDefEntity.RETURN_LIST_VALUE));
    form.setActionType( returnDef.getFieldString(IGTReturnDefEntity.RETURN_LIST_ACTION));
    form.setAlert(      returnDef.getFieldString(IGTReturnDefEntity.RETURN_LIST_ALERT));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ReturnDefAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_USER_PROCEDURE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTReturnDefEntity returnDef = (IGTReturnDefEntity)entity;

    basicValidateString(errors, IGTReturnDefEntity.RETURN_LIST_OPERATOR, form, entity);
    basicValidateString(errors, IGTReturnDefEntity.RETURN_LIST_VALUE,    form, entity);
    basicValidateString(errors, IGTReturnDefEntity.RETURN_LIST_ACTION,   form, entity);
    basicValidateString(errors, IGTReturnDefEntity.RETURN_LIST_ALERT,    form, entity);

    // 20030613 DDJ: Display value1 & value2 when operator is 'between'
    // However, still store it into 1 field for B-Tier
    ReturnDefAForm returnDefForm = (ReturnDefAForm) form;
    if(IGTReturnDefEntity.OPERATOR_BETWEEN.equals(returnDefForm.getOperatorInteger()))
    {
      if(StaticUtils.stringEmpty(returnDefForm.getValue1()) ||
         StaticUtils.stringEmpty(returnDefForm.getValue2()))
      {
        ActionError partial = new ActionError("returnDef.error.value.partial");
        errors.add(returnDef.getFieldName(IGTReturnDefEntity.RETURN_LIST_VALUE), partial);
      }
    }


  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTReturnDefEntity returnDef = (IGTReturnDefEntity)entity;
    ReturnDefAForm form = (ReturnDefAForm)actionContext.getActionForm();

    returnDef.setFieldValue(IGTReturnDefEntity.RETURN_LIST_OPERATOR, form.getOperatorInteger());
    returnDef.setFieldValue(IGTReturnDefEntity.RETURN_LIST_VALUE,    form.getValue());
    returnDef.setFieldValue(IGTReturnDefEntity.RETURN_LIST_ACTION,   form.getActionTypeInteger());
    returnDef.setFieldValue(IGTReturnDefEntity.RETURN_LIST_ALERT,    StaticUtils.longValue( form.getAlert() ) ); //20030604AH
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
      IGTReturnDefEntity returnDef = (IGTReturnDefEntity)entity;

      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      String indexString = (String)opContext.getAttribute(INDEX_KEY);
      if("new".equals(indexString))
      {
        procReturnList.add(returnDef);
      }
      else
      {
        int index = Integer.parseInt(indexString);
        procReturnList.remove(index);
        procReturnList.add(index, returnDef);
      }

      // update parent action form odering
      OperationContext pOpContext = opContext.getPreviousContext();
      UserProcedureAForm userProcedureForm = (UserProcedureAForm)pOpContext.getActionForm();
      userProcedureForm.initProcReturnListOrder(procReturnList == null ? 0 : procReturnList.size());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving returnDef entity in userProcedure:" + entity, t);
    }
  }

  protected IGTUserProcedureEntity getUserProcedure(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      OperationContext pOpContext = opContext.getPreviousContext();
      if(pOpContext == null)
      {
        throw new java.lang.NullPointerException("null parent OperationContext reference");
      }

      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(userProcedure == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(userProcedure instanceof IGTUserProcedureEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is not a userProcedure entity. Entity=" + userProcedure);
      }
      return userProcedure;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving userProcedure entity from parent OperationContext",t);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTReturnDefEntity entity = null;
      String indexString = actionContext.getRequest().getParameter("index");
      opContext.setAttribute(INDEX_KEY, indexString);
      if(indexString != null)
      {
        if(indexString.equals("new"))
        {
          try
          {
            IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
            IGTUserProcedureManager manager = (IGTUserProcedureManager)gtasSession.getManager(IGTManager.MANAGER_USER_PROCEDURE);
            entity = manager.newReturnDef();
            initialiseNewEntity(actionContext, entity);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error instantiating new returnDef entity object",t);
          }
        }
        else
        {
          try
          {
            IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
            List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
            if(procReturnList == null)
            {
              throw new java.lang.NullPointerException("Null value for userProcedure.returnDef in userProcedure entity=" + userProcedure);
            }

            int index = Integer.parseInt(indexString);
            try
            {
              entity = (IGTReturnDefEntity)procReturnList.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving returnDef from list at index " + index, t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of returnDef list retrieved from userProcedure entity "
                        + userProcedure);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving returnDef entity object from userProcedure entity in parent OperationContext", t);
          }
        }
      }
      else
      {
        throw new GTClientException("No index specified");
      }
      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining returnDef entity object",t);
    }
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    // 20030213 DDJ: Currently only the 'continue' action is supported
    entity.setFieldValue(IGTReturnDefEntity.RETURN_LIST_ACTION, IGTReturnDefEntity.ACTION_CONTINUE);
  }
}