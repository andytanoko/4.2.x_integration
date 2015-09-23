/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedureDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         Remove deprecated getNavgroup() method
 */
package com.gridnode.gtas.client.web.bp;

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

public class JavaProcedureDispatchAction extends EntityDispatchAction2
{
  public static final String PROC_DEF_FILE_UID_KEY = "userProcedure.procDefFile"; //20030716AH
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_JAVA_PROCEDURE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new JavaProcedureRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.JAVA_PROCEDURE_UPDATE : IDocumentKeys.JAVA_PROCEDURE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTJavaProcedureEntity javaProcedure = (IGTJavaProcedureEntity)entity;

    JavaProcedureAForm form = (JavaProcedureAForm)actionContext.getActionForm();
    form.setClassName(  javaProcedure.getFieldString(IGTJavaProcedureEntity.JAVA_CLASS_NAME));
    form.setMethodName( javaProcedure.getFieldString(IGTJavaProcedureEntity.JAVA_METHOD_NAME));
    form.setIsLocal(    javaProcedure.getFieldString(IGTJavaProcedureEntity.JAVA_IS_LOCAL));
    form.setJvmOptions( javaProcedure.getFieldString(IGTJavaProcedureEntity.JAVA_JVM_OPTIONS));
    form.setArguments(  javaProcedure.getFieldString(IGTJavaProcedureEntity.JAVA_ARGUMENTS));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new JavaProcedureAForm();
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
    //IGTJavaProcedureEntity javaProcedure = (IGTJavaProcedureEntity)entity;

    basicValidateString(errors, IGTJavaProcedureEntity.JAVA_CLASS_NAME,  form, entity);
    basicValidateString(errors, IGTJavaProcedureEntity.JAVA_IS_LOCAL,    form, entity);
    if(StaticUtils.primitiveBooleanValue(((JavaProcedureAForm)form).getIsLocal()))
    {
      basicValidateString(errors, IGTJavaProcedureEntity.JAVA_METHOD_NAME, form, entity);
    }
    else
    {
      basicValidateString(errors, IGTJavaProcedureEntity.JAVA_JVM_OPTIONS, form, entity);
      basicValidateString(errors, IGTJavaProcedureEntity.JAVA_ARGUMENTS,   form, entity);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTJavaProcedureEntity javaProcedure = (IGTJavaProcedureEntity)entity;
    JavaProcedureAForm form = (JavaProcedureAForm)actionContext.getActionForm();

    javaProcedure.setFieldValue(IGTJavaProcedureEntity.JAVA_CLASS_NAME,  form.getClassName());
    javaProcedure.setFieldValue(IGTJavaProcedureEntity.JAVA_METHOD_NAME, form.getMethodName());
    javaProcedure.setFieldValue(IGTJavaProcedureEntity.JAVA_IS_LOCAL,    StaticUtils.booleanValue(form.getIsLocal()));
    javaProcedure.setFieldValue(IGTJavaProcedureEntity.JAVA_JVM_OPTIONS, form.getJvmOptions());
    javaProcedure.setFieldValue(IGTJavaProcedureEntity.JAVA_ARGUMENTS,   form.getArguments());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTJavaProcedureEntity javaProcedure = (IGTJavaProcedureEntity)entity;
      userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_DEF, javaProcedure);

      // Clear embeded entity "NotValid" flag
      setEmbededEntityAsValid(actionContext, IGTUserProcedureEntity.PROC_DEF, userProcedure);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving javaProcedure entity in userProcedure:" + entity, t);
    }
  }

  /** @todo see if this can be made generic */
  protected void setEmbededEntityAsValid( ActionContext actionContext,
                                        Number fieldId,
                                        IGTEntity entity)
    throws GTClientException
  {
    // Set a flag to represent embeded entity is valid (eg. "[fieldName]NotValid")
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    OperationContext pOpContext = opContext.getPreviousContext();
    pOpContext.setAttribute(fieldName + "NotValid", Boolean.FALSE);
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
      IGTJavaProcedureEntity entity = null;

      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTEntity procDef = (IGTEntity)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF);
      if(procDef != null && procDef instanceof IGTJavaProcedureEntity)
      {
        entity = (IGTJavaProcedureEntity)procDef;
      }
      else
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTUserProcedureManager manager = (IGTUserProcedureManager)gtasSession.getManager(IGTManager.MANAGER_USER_PROCEDURE);
        entity = manager.newJavaProcedure();
        initialiseNewEntity(actionContext, entity);
      }

      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining javaProcedure entity object",t);
    }
  }
}