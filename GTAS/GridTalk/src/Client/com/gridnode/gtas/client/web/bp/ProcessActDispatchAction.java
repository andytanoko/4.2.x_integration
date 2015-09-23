/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessActDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2007-11-07     Tam Wei Xiang       Added option "isCompressRequired"
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessActEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessDefManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ProcessActDispatchAction extends EntityDispatchAction2
{
  private static final String PROCESS_ACT_KEY = "processAct processActType";

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PROCESS_ACT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ProcessActRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PROCESS_ACT_UPDATE : IDocumentKeys.PROCESS_ACT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessActEntity processAct = (IGTProcessActEntity)entity;

    ProcessActAForm form = (ProcessActAForm)actionContext.getActionForm();
    form.setMsgType                   (processAct.getFieldString(IGTProcessActEntity.MSG_TYPE));
    form.setDictFile                  (processAct.getFieldString(IGTProcessActEntity.DICT_FILE));
    form.setXmlSchema                 (processAct.getFieldString(IGTProcessActEntity.XML_SCHEMA));

    form.setBizActivityIdentifier     (processAct.getFieldString(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER));
    form.setBizActionCode             (processAct.getFieldString(IGTProcessActEntity.BIZ_ACTION_CODE));
    form.setRetries                   (processAct.getFieldString(IGTProcessActEntity.RETRIES));
    form.setTimeToAcknowledge         (processAct.getFieldString(IGTProcessActEntity.TIME_TO_ACKNOWLEDGE));

    form.setIsAuthorizationRequired   (processAct.getFieldString(IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED));
    form.setIsNonRepudiationRequired  (processAct.getFieldString(IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED));
    form.setIsSecureTransportRequired (processAct.getFieldString(IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED));
    form.setDisableDtd                (processAct.getFieldString(IGTProcessActEntity.DISABLE_DTD));
    form.setDisableSchema             (processAct.getFieldString(IGTProcessActEntity.DISABLE_SCHEMA));
    form.setValidateAtSender          (processAct.getFieldString(IGTProcessActEntity.VALIDATE_AT_SENDER));

    form.setDisableEncryption         (processAct.getFieldString(IGTProcessActEntity.DISABLE_ENCRYPTION));
    form.setDisableSignature          (processAct.getFieldString(IGTProcessActEntity.DISABLE_SIGNATURE));
    form.setOnlyEncryptPayload        (processAct.getFieldString(IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD));
    form.setDigestAlgorithm           (processAct.getFieldString(IGTProcessActEntity.DIGEST_ALGORITHM));
    form.setEncryptionAlgorithm       (processAct.getFieldString(IGTProcessActEntity.ENCRYPTION_ALGORITHM));
    form.setEncryptionAlgorithmLength (processAct.getFieldString(IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH));
    
    form.setIsCompressRequired        (processAct.getFieldString(processAct.IS_COMPRESS_REQUIRED));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ProcessActAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PROCESS_DEF;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTProcessActEntity processAct = (IGTProcessActEntity)entity;
    //ProcessActAForm tform = (ProcessActAForm)form;

    basicValidateString(errors, IGTProcessActEntity.MSG_TYPE, form, entity);
    basicValidateString(errors, IGTProcessActEntity.DICT_FILE, form, entity);
    basicValidateString(errors, IGTProcessActEntity.XML_SCHEMA, form, entity);

    basicValidateString(errors, IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER, form, entity);
    basicValidateString(errors, IGTProcessActEntity.BIZ_ACTION_CODE, form, entity);
    basicValidateString(errors, IGTProcessActEntity.RETRIES, form, entity);
    basicValidateString(errors, IGTProcessActEntity.TIME_TO_ACKNOWLEDGE, form, entity);

    basicValidateString(errors, IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED, form, entity);
    basicValidateString(errors, IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED, form, entity);
    basicValidateString(errors, IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED, form, entity);
    basicValidateString(errors, IGTProcessActEntity.DISABLE_DTD, form, entity);
    basicValidateString(errors, IGTProcessActEntity.DISABLE_SCHEMA, form, entity);
    basicValidateString(errors, IGTProcessActEntity.VALIDATE_AT_SENDER, form, entity);

    basicValidateString(errors, IGTProcessActEntity.DISABLE_ENCRYPTION, form, entity);
    basicValidateString(errors, IGTProcessActEntity.DISABLE_SIGNATURE, form, entity);
    basicValidateString(errors, IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD, form, entity);
    basicValidateString(errors, IGTProcessActEntity.DIGEST_ALGORITHM, form, entity);
    basicValidateString(errors, IGTProcessActEntity.ENCRYPTION_ALGORITHM, form, entity);
    basicValidateString(errors, IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH, form, entity);
    
    basicValidateString(errors, IGTProcessActEntity.IS_COMPRESS_REQUIRED, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessActEntity processAct = (IGTProcessActEntity)entity;
    ProcessActAForm form = (ProcessActAForm)actionContext.getActionForm();

    processAct.setFieldValue(IGTProcessActEntity.MSG_TYPE,                     form.getMsgTypeLong());
    processAct.setFieldValue(IGTProcessActEntity.DICT_FILE,                    form.getDictFileLong());
    processAct.setFieldValue(IGTProcessActEntity.XML_SCHEMA,                   form.getXmlSchemaLong());

    processAct.setFieldValue(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER,      form.getBizActivityIdentifier());
    processAct.setFieldValue(IGTProcessActEntity.BIZ_ACTION_CODE,              form.getBizActionCode());
    processAct.setFieldValue(IGTProcessActEntity.RETRIES,                      form.getRetriesInteger());
    processAct.setFieldValue(IGTProcessActEntity.TIME_TO_ACKNOWLEDGE,          form.getTimeToAcknowledgeInteger());

    processAct.setFieldValue(IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED,    form.getIsAuthorizationRequiredBoolean());
    processAct.setFieldValue(IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED,  form.getIsNonRepudiationRequiredBoolean());
    processAct.setFieldValue(IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED, form.getIsSecureTransportRequiredBoolean());
    processAct.setFieldValue(IGTProcessActEntity.DISABLE_DTD,                  form.getDisableDtdBoolean());
    processAct.setFieldValue(IGTProcessActEntity.DISABLE_SCHEMA,               form.getDisableSchemaBoolean());
    processAct.setFieldValue(IGTProcessActEntity.VALIDATE_AT_SENDER,           form.getValidateAtSenderBoolean());

    processAct.setFieldValue(IGTProcessActEntity.DISABLE_ENCRYPTION,           form.getDisableEncryptionBoolean());
    processAct.setFieldValue(IGTProcessActEntity.DISABLE_SIGNATURE,            form.getDisableSignatureBoolean());
    processAct.setFieldValue(IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD,         form.getOnlyEncryptPayloadBoolean());
    processAct.setFieldValue(IGTProcessActEntity.DIGEST_ALGORITHM,             form.getDigestAlgorithm());
    processAct.setFieldValue(IGTProcessActEntity.ENCRYPTION_ALGORITHM,         form.getEncryptionAlgorithm());
    processAct.setFieldValue(IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH,  form.getEncryptionAlgorithmLengthInteger());
    processAct.setFieldValue(processAct.IS_COMPRESS_REQUIRED,         form.getIsCompressRequiredBoolean());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTProcessDefEntity processDef = getProcessDef(actionContext);
      IGTProcessActEntity processAct = (IGTProcessActEntity)entity;

      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      Number processActKey = (Number)opContext.getAttribute(PROCESS_ACT_KEY);
      processDef.setFieldValue(processActKey, processAct);

      // Clear embeded entity "NotValid" flag
      setEmbededEntityAsValid(actionContext, processActKey, processDef);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving processAct entity in processDef:" + entity, t);
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

  protected IGTProcessDefEntity getProcessDef(ActionContext actionContext)
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

      IGTProcessDefEntity processDef = (IGTProcessDefEntity)pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(processDef == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(processDef instanceof IGTProcessDefEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is not a processDef entity. Entity=" + processDef);
      }
      return processDef;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving processDef entity from parent OperationContext",t);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTProcessActEntity entity = null;

      IGTProcessDefEntity processDef = getProcessDef(actionContext);

      String processTypeString = actionContext.getRequest().getParameter("processType");  // "processType" is defined in struts-config
      Number processActKey;
      if("request".equals(processTypeString))
        processActKey = IGTProcessActEntity.REQUEST_ACT;
      else if("response".equals(processTypeString))
        processActKey = IGTProcessActEntity.RESPONSE_ACT;
      else
        throw new java.lang.IllegalStateException("Invalid processType of " + processTypeString);
      opContext.setAttribute(PROCESS_ACT_KEY, processActKey);

      IGTEntity processAct = (IGTEntity)processDef.getFieldValue(processActKey);
      if(processAct != null && processAct instanceof IGTProcessActEntity)
      {
        entity = (IGTProcessActEntity)processAct;
      }
      else
      {
        IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        IGTProcessDefManager manager = (IGTProcessDefManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_DEF);
        entity = manager.newProcessAct();
        initialiseNewEntity(actionContext, entity);
      }

      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining processAct entity object",t);
    }
  }
}