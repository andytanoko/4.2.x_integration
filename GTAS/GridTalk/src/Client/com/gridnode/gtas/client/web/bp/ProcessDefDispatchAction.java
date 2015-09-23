/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2003-03-03     Daniel D'Cotta      Fixed DocumentIdentifiers
 * 2003-08-20     Andrew Hill         Removed deprecated getNavgroup() method, userTrackingIdentifier support
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ProcessDefDispatchAction extends EntityDispatchAction2
{


  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PROCESS_DEF;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ProcessDefRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PROCESS_DEF_UPDATE : IDocumentKeys.PROCESS_DEF_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessDefEntity processDef = (IGTProcessDefEntity)entity;

    ProcessDefAForm form = (ProcessDefAForm)actionContext.getActionForm();
    form.setDefName                         (processDef.getFieldString(IGTProcessDefEntity.DEF_NAME));
    form.setIsSynchronous                   (processDef.getFieldString(IGTProcessDefEntity.IS_SYNCHRONOUS));
    form.setProcessType                     (processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE));
    form.setActionTimeOut                   (processDef.getFieldString(IGTProcessDefEntity.ACTION_TIME_OUT));
    form.setRnifVersion                     (processDef.getFieldString(IGTProcessDefEntity.RNIF_VERSION));
    form.setProcessIndicatorCode            (processDef.getFieldString(IGTProcessDefEntity.PROCESS_INDICATOR_CODE));
    form.setVersionIdentifier               (processDef.getFieldString(IGTProcessDefEntity.VERSION_IDENTIFIER));
    form.setUsageCode                       (processDef.getFieldString(IGTProcessDefEntity.USAGE_CODE));
    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //form.setDigestAlgCode                   (processDef.getFieldString(processDef.DIGEST_ALG_CODE));

    form.setFromPartnerClassCode            (processDef.getFieldString(IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE));
    form.setFromPartnerRoleClassCode        (processDef.getFieldString(IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE));
    form.setFromBizServiceCode              (processDef.getFieldString(IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE));

    form.setToPartnerClassCode              (processDef.getFieldString(IGTProcessDefEntity.TO_PARTNER_CLASS_CODE));
    form.setToPartnerRoleClassCode          (processDef.getFieldString(IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE));
    form.setToBizServiceCode                (processDef.getFieldString(IGTProcessDefEntity.TO_BIZ_SERVICE_CODE));

    form.setRequestDocThisDocIdentifier     (processDef.getFieldString(IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER));
    form.setResponseDocThisDocIdentifier    (processDef.getFieldString(IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER));
    form.setResponseDocRequestDocIdentifier (processDef.getFieldString(IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER));

    form.setUserTrackingIdentifier( processDef.getFieldString(IGTProcessDefEntity.USER_TRACKING_IDENTIFIER) ); //20030820AH
System.out.println("userTrackingIdentifier=" + form.getUserTrackingIdentifier()); //@todo Remove before checkin
    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //form.setDisableDtd                      (processDef.getFieldString(processDef.DISABLE_DTD));
    //form.setDisableSchema                   (processDef.getFieldString(processDef.DISABLE_SCHEMA));
    //form.setDisableSignature                (processDef.getFieldString(processDef.DISABLE_SIGNATURE));
    //form.setDisableEncryption               (processDef.getFieldString(processDef.DISABLE_ENCRYPTION));
    //form.setEnableEncryptPayload            (processDef.getFieldString(processDef.ENABLE_ENCRYPT_PAYLOAD));
    //form.setValidateAtSender                (processDef.getFieldString(processDef.VALIDATE_AT_SENDER));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ProcessDefAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PROCESS_DEF;
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    IGTProcessDefEntity processDef = (IGTProcessDefEntity)getEntity(actionContext);
    if(processDef.isNewEntity())
    {
      initialiseEmbededEntity(actionContext, IGTProcessDefEntity.REQUEST_ACT);
      initialiseEmbededEntity(actionContext, IGTProcessDefEntity.RESPONSE_ACT);
    }
    else
    {
      if(IGTProcessDefEntity.TYPE_SINGLE_ACTION.equals(processDef.getFieldValue(IGTProcessDefEntity.PROCESS_TYPE)))
      {
        initialiseEmbededEntity(actionContext, IGTProcessDefEntity.RESPONSE_ACT);
      }
    }
  }

  /** @todo see if this can be made generic */
  protected void initialiseEmbededEntity(ActionContext actionContext, Number fieldId)
    throws GTClientException
  {
    // Set a flag to represent embeded entity is valid (eg. "[fieldName]NotValid")
    IGTEntity entity = (IGTEntity)getEntity(actionContext);
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    if(opContext.getAttribute(fieldName + "NotValid") == null)
      opContext.setAttribute(fieldName + "NotValid", Boolean.TRUE);
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTProcessDefEntity processDef = (IGTProcessDefEntity)entity;
    ProcessDefAForm pdForm = (ProcessDefAForm)form;

    basicValidateString(errors, IGTProcessDefEntity.DEF_NAME, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.IS_SYNCHRONOUS, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.PROCESS_TYPE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.RNIF_VERSION, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.PROCESS_INDICATOR_CODE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.VERSION_IDENTIFIER, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.USAGE_CODE, form, entity);
    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //basicValidateString(errors, IGTProcessDefEntity.DIGEST_ALG_CODE, form, entity);

    basicValidateString(errors, IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE, form, entity);

    basicValidateString(errors, IGTProcessDefEntity.TO_PARTNER_CLASS_CODE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.TO_BIZ_SERVICE_CODE, form, entity);

    basicValidateString(errors, IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER, form, entity);
    basicValidateString(errors, IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER, form, entity);
    
    basicValidateString(errors, IGTProcessDefEntity.USER_TRACKING_IDENTIFIER, form, entity); //20030821AH

    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //basicValidateString(errors, IGTProcessDefEntity.DISABLE_DTD, form, entity);
    //basicValidateString(errors, IGTProcessDefEntity.DISABLE_SCHEMA, form, entity);
    //basicValidateString(errors, IGTProcessDefEntity.DISABLE_SIGNATURE, form, entity);
    //basicValidateString(errors, IGTProcessDefEntity.DISABLE_ENCRYPTION, form, entity);
    //basicValidateString(errors, IGTProcessDefEntity.ENABLE_ENCRYPT_PAYLOAD, form, entity);
    //basicValidateString(errors, IGTProcessDefEntity.VALIDATE_AT_SENDER, form, entity);

    if(IGTProcessDefEntity.TYPE_SINGLE_ACTION.equals(pdForm.getProcessType()))
    {
      validateEmbededEntitiy(actionContext, errors, IGTProcessDefEntity.REQUEST_ACT, entity);
    }
    else if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(pdForm.getProcessType()))
    {
      basicValidateString(errors, IGTProcessDefEntity.ACTION_TIME_OUT, form, entity);

      validateEmbededEntitiy(actionContext, errors, IGTProcessDefEntity.REQUEST_ACT, entity);
      validateEmbededEntitiy(actionContext, errors, IGTProcessDefEntity.RESPONSE_ACT, entity);
    }
  }

  /** @todo see if this can be made generic */
  protected void validateEmbededEntitiy(ActionContext actionContext,
                                        ActionErrors actionErrors,
                                        Number fieldId,
                                        IGTEntity entity)
    throws GTClientException
  {
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    if(Boolean.TRUE.equals(opContext.getAttribute(fieldName + "NotValid")))
    {
      actionErrors.add(fieldName, new ActionError(entity.getType() + ".error." + fieldName + ".required"));
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcessDefEntity processDef = (IGTProcessDefEntity)entity;
    ProcessDefAForm form = (ProcessDefAForm)actionContext.getActionForm();

    processDef.setFieldValue(IGTProcessDefEntity.DEF_NAME,                     form.getDefName());
    processDef.setFieldValue(IGTProcessDefEntity.IS_SYNCHRONOUS,               form.getIsSynchronousBoolean());
    processDef.setFieldValue(IGTProcessDefEntity.PROCESS_TYPE,                 form.getProcessType());
    processDef.setFieldValue(IGTProcessDefEntity.ACTION_TIME_OUT,              form.getActionTimeOutInteger());
    processDef.setFieldValue(IGTProcessDefEntity.RNIF_VERSION,                 form.getRnifVersion());
    processDef.setFieldValue(IGTProcessDefEntity.PROCESS_INDICATOR_CODE,       form.getProcessIndicatorCode());
    processDef.setFieldValue(IGTProcessDefEntity.VERSION_IDENTIFIER,           form.getVersionIdentifier());
    processDef.setFieldValue(IGTProcessDefEntity.USAGE_CODE,                   form.getUsageCode());
    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //processDef.setFieldValue(IGTProcessDefEntity.DIGEST_ALG_CODE,              form.getDigestAlgCode());

    processDef.setFieldValue(IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE,      form.getFromPartnerClassCode());
    processDef.setFieldValue(IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE, form.getFromPartnerRoleClassCode());
    processDef.setFieldValue(IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE,        form.getFromBizServiceCode());

    processDef.setFieldValue(IGTProcessDefEntity.TO_PARTNER_CLASS_CODE,        form.getToPartnerClassCode());
    processDef.setFieldValue(IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE,   form.getToPartnerRoleClassCode());
    processDef.setFieldValue(IGTProcessDefEntity.TO_BIZ_SERVICE_CODE,          form.getToBizServiceCode());

    processDef.setFieldValue(IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER,      form.getRequestDocThisDocIdentifier());
    processDef.setFieldValue(IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER,     form.getResponseDocThisDocIdentifier());
    processDef.setFieldValue(IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER,  form.getResponseDocRequestDocIdentifier());

    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //processDef.setFieldValue(IGTProcessDefEntity.DISABLE_DTD,                  StaticUtils.booleanValue(form.getDisableDtd()));
    //processDef.setFieldValue(IGTProcessDefEntity.DISABLE_SCHEMA,               StaticUtils.booleanValue(form.getDisableSchema()));
    //processDef.setFieldValue(IGTProcessDefEntity.DISABLE_SIGNATURE,            StaticUtils.booleanValue(form.getDisableSignature()));
    //processDef.setFieldValue(IGTProcessDefEntity.DISABLE_ENCRYPTION,           StaticUtils.booleanValue(form.getDisableEncryption()));
    //processDef.setFieldValue(IGTProcessDefEntity.ENABLE_ENCRYPT_PAYLOAD,       StaticUtils.booleanValue(form.getEnableEncryptPayload()));
    //processDef.setFieldValue(IGTProcessDefEntity.VALIDATE_AT_SENDER,           StaticUtils.booleanValue(form.getValidateAtSender()));

    processDef.setFieldValue(IGTProcessDefEntity.USER_TRACKING_IDENTIFIER, form.getUserTrackingIdentifier() ); //20030829AH
  }
}