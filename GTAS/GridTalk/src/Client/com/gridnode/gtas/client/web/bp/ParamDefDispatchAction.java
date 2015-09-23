/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParamDefDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2002-09-09     Daniel D'Cotta      Modified validation for DataValue
 * 2002-11-28     Koh Han Sing        Modified validation for DataValue with
 *                                    new data type byte[], byte[][]
 */
package com.gridnode.gtas.client.web.bp;

import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ParamDefDispatchAction extends EntityDispatchAction2
{
  private static final String INDEX_KEY = "paramDef index";

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PARAM_DEF;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ParamDefRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PARAM_DEF_UPDATE : IDocumentKeys.PARAM_DEF_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTParamDefEntity paramDef = (IGTParamDefEntity)entity;

    ParamDefAForm form = (ParamDefAForm)actionContext.getActionForm();
    form.setName(       paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_NAME));
    form.setDescription(paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_DESCRIPTION));
    form.setSource(     paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_SOURCE));
    form.setType(       paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_TYPE));
    form.setDateFormat( paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_DATE_FORMAT));
    form.setValue(      paramDef.getFieldString(IGTParamDefEntity.PARAM_LIST_VALUE));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ParamDefAForm();
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
    //IGTParamDefEntity paramDef = (IGTParamDefEntity)entity;

    basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_NAME,           form, entity);
    basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_DESCRIPTION,    form, entity);
    basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_SOURCE,         form, entity);
    basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_TYPE,           form, entity);

    if(IGTParamDefEntity.DATA_TYPE_DATE.equals(((ParamDefAForm)form).getTypeInteger()))
    {
      basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_DATE_FORMAT,  form, entity);
    }

    Integer source = ((ParamDefAForm)form).getSourceInteger();
    Integer type = ((ParamDefAForm)form).getTypeInteger();
    if(!(IGTParamDefEntity.DATA_TYPE_DATAHANDLER.equals(type) ||
         IGTParamDefEntity.DATA_TYPE_DATAHANDLER_ARRAY.equals(type) ||
         IGTParamDefEntity.DATA_TYPE_BYTE_ARRAY.equals(type) ||
         IGTParamDefEntity.DATA_TYPE_BYTE_ARRAY_ARRAY.equals(type) ||
         (IGTParamDefEntity.SOURCE_GDOC.equals(source) && IGTParamDefEntity.DATA_TYPE_OBJECT.equals(type))))
    {
      basicValidateString(errors, IGTParamDefEntity.PARAM_LIST_VALUE,        form, entity);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTParamDefEntity paramDef = (IGTParamDefEntity)entity;
    ParamDefAForm form = (ParamDefAForm)actionContext.getActionForm();

    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_NAME,        form.getName());
    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_DESCRIPTION, form.getDescription());
    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_SOURCE,      form.getSourceInteger());
    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_TYPE,        form.getTypeInteger());
    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_DATE_FORMAT, form.getDateFormat());
    paramDef.setFieldValue(IGTParamDefEntity.PARAM_LIST_VALUE,       form.getValue());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
      IGTParamDefEntity paramDef = (IGTParamDefEntity)entity;

      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      String indexString = (String)opContext.getAttribute(INDEX_KEY);
      if("new".equals(indexString))
      {
        procParamList.add(paramDef);
      }
      else
      {
        int index = Integer.parseInt(indexString);
        procParamList.remove(index);
        procParamList.add(index, paramDef);
      }

      // update parent action form odering
      OperationContext pOpContext = opContext.getPreviousContext();
      UserProcedureAForm userProcedureForm = (UserProcedureAForm)pOpContext.getActionForm();
      userProcedureForm.initProcParamListOrder(procParamList == null ? 0 : procParamList.size());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving paramDef entity in userProcedure:" + entity, t);
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
      IGTParamDefEntity entity = null;
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
            entity = manager.newParamDef();
            initialiseNewEntity(actionContext, entity);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error instantiating new paramDef entity object",t);
          }
        }
        else
        {
          try
          {
            IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
            List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
            if(procParamList == null)
            {
              throw new java.lang.NullPointerException("Null value for userProcedure.paramDef in userProcedure entity=" + userProcedure);
            }

            int index = Integer.parseInt(indexString);
            try
            {
              entity = (IGTParamDefEntity)procParamList.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving paramDef from list at index " + index, t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of paramDef list retrieved from userProcedure entity "
                        + userProcedure);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving paramDef entity object from userProcedure entity in parent OperationContext", t);
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
      throw new GTClientException("Error obtaining paramDef entity object",t);
    }
  }
}