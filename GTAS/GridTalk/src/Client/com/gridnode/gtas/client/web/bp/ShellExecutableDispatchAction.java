/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ShellExecutableDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

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

public class ShellExecutableDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SHELL_EXECUTABLE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ShellExecutableRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SHELL_EXECUTABLE_UPDATE : IDocumentKeys.SHELL_EXECUTABLE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTShellExecutableEntity shellExecutable = (IGTShellExecutableEntity)entity;

    ShellExecutableAForm form = (ShellExecutableAForm)actionContext.getActionForm();
    form.setArguments(shellExecutable.getFieldString(IGTShellExecutableEntity.EXEC_ARGUMENTS));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ShellExecutableAForm();
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
    //IGTShellExecutableEntity shellExecutable = (IGTShellExecutableEntity)entity;

    basicValidateString(errors, IGTShellExecutableEntity.EXEC_ARGUMENTS, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTShellExecutableEntity shellExecutable = (IGTShellExecutableEntity)entity;
    ShellExecutableAForm form = (ShellExecutableAForm)actionContext.getActionForm();

    shellExecutable.setFieldValue(IGTShellExecutableEntity.EXEC_ARGUMENTS, form.getArguments());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTShellExecutableEntity shellExecutable = (IGTShellExecutableEntity)entity;
      userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_DEF, shellExecutable);

      // Clear embeded entity "NotValid" flag
      setEmbededEntityAsValid(actionContext, IGTUserProcedureEntity.PROC_DEF, userProcedure);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving paramDef entity in userProcedure:" + entity, t);
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
      IGTShellExecutableEntity entity = null;

      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTEntity procDef = (IGTEntity)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF);
      if(procDef != null && procDef instanceof IGTShellExecutableEntity)
      {
        entity = (IGTShellExecutableEntity)procDef;
      }
      else
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTUserProcedureManager manager = (IGTUserProcedureManager)gtasSession.getManager(IGTManager.MANAGER_USER_PROCEDURE);
        entity = manager.newShellExecutable();
        initialiseNewEntity(actionContext, entity);
      }

      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining shellExecutable entity object",t);
    }
  }
}