/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedureDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTSoapProcedureEntity;
import com.gridnode.gtas.client.ctrl.IGTUserProcedureEntity;
import com.gridnode.gtas.client.ctrl.IGTUserProcedureManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SoapProcedureDispatchAction extends EntityDispatchAction2
{
  public static final String PROC_DEF_FILE_UID_KEY = JavaProcedureDispatchAction.PROC_DEF_FILE_UID_KEY;
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SOAP_PROCEDURE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new SoapProcedureRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SOAP_PROCEDURE_UPDATE : IDocumentKeys.SOAP_PROCEDURE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTSoapProcedureEntity soapProcedure = (IGTSoapProcedureEntity)entity;

    SoapProcedureAForm form = (SoapProcedureAForm)actionContext.getActionForm();  
    form.setMethodName      (soapProcedure.getFieldString(IGTSoapProcedureEntity.SOAP_METHOD_NAME));
    form.setUserName        (soapProcedure.getFieldString(IGTSoapProcedureEntity.SOAP_USER_NAME)); // 20031205 DDJ
    form.setPassword        (soapProcedure.getFieldString(IGTSoapProcedureEntity.SOAP_PASSWORD));  // 20031205 DDJ
    form.setConfirmPassword (soapProcedure.getFieldString(IGTSoapProcedureEntity.SOAP_PASSWORD));  // 20031205 DDJ
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new SoapProcedureAForm();
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
    //IGTSoapProcedureEntity soapProcedure = (IGTSoapProcedureEntity)entity;
    basicValidateString(errors, IGTSoapProcedureEntity.SOAP_METHOD_NAME, form, entity);
    basicValidateString(errors, IGTSoapProcedureEntity.SOAP_USER_NAME, form, entity);
    basicValidateString(errors, IGTSoapProcedureEntity.SOAP_PASSWORD, form, entity);

    // 20031205 DDJ: Validate ConfirmPassword
    String entityType = entity.getType();
    String password         = ((SoapProcedureAForm)form).getPassword();
    String confirmPassword  = ((SoapProcedureAForm)form).getConfirmPassword();
    if(StaticUtils.stringEmpty(confirmPassword))
    {
      IGTFieldMetaInfo passwordMetaInfo = entity.getFieldMetaInfo(IGTSoapProcedureEntity.SOAP_PASSWORD);
      if(passwordMetaInfo.isMandatory(entity.isNewEntity()))
      {
        EntityFieldValidator.addFieldError(errors, "confirmPassword", entityType, EntityFieldValidator.REQUIRED, null);
      }
      else if(StaticUtils.stringNotEmpty(password))
      {
        EntityFieldValidator.addFieldError(errors, "confirmPassword", entityType, EntityFieldValidator.MISMATCH, null);
      }
    }
    else
    {
      if(!confirmPassword.equals(password))
      {
        EntityFieldValidator.addFieldError(errors, "confirmPassword", entityType, EntityFieldValidator.MISMATCH, null);
      }
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTSoapProcedureEntity soapProcedure = (IGTSoapProcedureEntity)entity;
    SoapProcedureAForm form = (SoapProcedureAForm)actionContext.getActionForm();

    soapProcedure.setFieldValue(IGTSoapProcedureEntity.SOAP_METHOD_NAME, form.getMethodName());
    soapProcedure.setFieldValue(IGTSoapProcedureEntity.SOAP_USER_NAME,   form.getUserName());  // 20031205 DDJ
    soapProcedure.setFieldValue(IGTSoapProcedureEntity.SOAP_PASSWORD,    form.getPassword());  // 20031205 DDJ
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTSoapProcedureEntity soapProcedure = (IGTSoapProcedureEntity)entity;
      userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_DEF, soapProcedure);

      // Clear embeded entity "NotValid" flag
      setEmbededEntityAsValid(actionContext, IGTUserProcedureEntity.PROC_DEF, userProcedure);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving soapProcedure entity in userProcedure:" + entity, t);
    }
  }

  /** @todo see if this can be made generic */
  protected void setEmbededEntityAsValid( ActionContext actionContext,
                                        Number fieldId,
                                        IGTEntity entity)
    throws GTClientException
  { //@todo: factor out as this is duplicated in JavaProcedureDispatchAction
    // Set a flag to represent embeded entity is valid (eg. "[fieldName]NotValid")
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    OperationContext pOpContext = opContext.getPreviousContext();
    pOpContext.setAttribute(fieldName + "NotValid", Boolean.FALSE);
  }

  protected IGTUserProcedureEntity getUserProcedure(ActionContext actionContext)
    throws GTClientException
  { //@todo: factor out as this is duplicated in JavaProcedureDispatchAction
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
  { //@todo: a lot (not all) here can be factored out as its common with JavaProcedureDispatchAction
    try
    {
      IGTSoapProcedureEntity entity = null;

      IGTUserProcedureEntity userProcedure = getUserProcedure(actionContext);
      IGTEntity procDef = (IGTEntity)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF);
      if(procDef != null && procDef instanceof IGTSoapProcedureEntity)
      {
        entity = (IGTSoapProcedureEntity)procDef;
      }
      else
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTUserProcedureManager manager = (IGTUserProcedureManager)gtasSession.getManager(IGTManager.MANAGER_USER_PROCEDURE);
        entity = manager.newSoapProcedure();
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