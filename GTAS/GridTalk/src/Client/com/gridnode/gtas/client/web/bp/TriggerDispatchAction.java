/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2003-08-08     Andrew Hill         isLocalPending support
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTTriggerEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class TriggerDispatchAction extends EntityDispatchAction2
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
    return new TriggerRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.TRIGGER_UPDATE : IDocumentKeys.TRIGGER_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTTriggerEntity trigger = (IGTTriggerEntity)entity;

    TriggerAForm form = (TriggerAForm)actionContext.getActionForm();
    form.setTriggerType(trigger.getFieldString(IGTTriggerEntity.TRIGGER_TYPE));
    form.setTriggerLevel(trigger.getFieldString(IGTTriggerEntity.TRIGGER_LEVEL));
    form.setPartnerFunctionId(trigger.getFieldString(IGTTriggerEntity.PARTNER_FUNCTION_ID));
    form.setDocType(trigger.getFieldString(IGTTriggerEntity.DOC_TYPE));
    form.setPartnerType(trigger.getFieldString(IGTTriggerEntity.PARTNER_TYPE));
    form.setPartnerGroup(trigger.getFieldString(IGTTriggerEntity.PARTNER_GROUP));
    form.setPartnerId(trigger.getFieldString(IGTTriggerEntity.PARTNER_ID));
    form.setChannelUid(trigger.getFieldString(IGTTriggerEntity.CHANNEL_UID)); // 20031120 DDJ
    form.setIsLocalPending(trigger.getFieldString(IGTTriggerEntity.IS_LOCAL_PENDING)); //20030808AH

    form.setProcessId(trigger.getFieldString(IGTTriggerEntity.PROCESS_ID));
    form.setIsRequest(trigger.getFieldString(IGTTriggerEntity.IS_REQUEST));
    form.setNumOfRetries(trigger.getFieldString(IGTTriggerEntity.NUM_OF_RETRIES));  // 20031120 DDJ
    form.setRetryInterval(trigger.getFieldString(IGTTriggerEntity.RETRY_INTERVAL)); // 20031120 DDJ
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new TriggerAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_TRIGGER;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTTriggerEntity trigger = (IGTTriggerEntity)entity;
    TriggerAForm tform = (TriggerAForm)form;

    basicValidateString(errors, IGTTriggerEntity.TRIGGER_TYPE, form, entity);
    basicValidateString(errors, IGTTriggerEntity.TRIGGER_LEVEL, form, entity);
    basicValidateString(errors, IGTTriggerEntity.PARTNER_FUNCTION_ID, form, entity);

    Integer triggerType = tform.getTriggerTypeInteger();
    if(IGTTriggerEntity.TRIGGER_IMPORT.equals(triggerType) || IGTTriggerEntity.TRIGGER_MANUAL_SEND.equals(triggerType))
    {
      basicValidateString(errors, IGTTriggerEntity.PROCESS_ID, form, entity);
      basicValidateString(errors, IGTTriggerEntity.IS_LOCAL_PENDING, form, entity); //20030808AH
      
      if(StaticUtils.stringNotEmpty(tform.getProcessId()))
      {
        basicValidateString(errors, IGTTriggerEntity.IS_REQUEST, form, entity); // 20031120 DDJ
      }
      else
      {
        basicValidateString(errors, IGTTriggerEntity.NUM_OF_RETRIES, form, entity); // 20031120 DDJ
        basicValidateString(errors, IGTTriggerEntity.RETRY_INTERVAL, form, entity); // 20031120 DDJ
      }
    }

    Integer triggerLevel = tform.getTriggerLevelInteger();
    if(IGTTriggerEntity.TRIGGER_LEVEL_0.equals(triggerLevel))
    {
      throw new java.lang.UnsupportedOperationException("Editing of level O triggers is unsupported");
    }
    else if(IGTTriggerEntity.TRIGGER_LEVEL_1.equals(triggerLevel))
    {
      basicValidateString(errors, IGTTriggerEntity.DOC_TYPE, form, entity);
    }
    else if(IGTTriggerEntity.TRIGGER_LEVEL_2.equals(triggerLevel))
    {
      basicValidateString(errors, IGTTriggerEntity.DOC_TYPE, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_TYPE, form, entity);
    }
    else if(IGTTriggerEntity.TRIGGER_LEVEL_3.equals(triggerLevel))
    {
      basicValidateString(errors, IGTTriggerEntity.DOC_TYPE, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_TYPE, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_GROUP, form, entity);
    }
    else if(IGTTriggerEntity.TRIGGER_LEVEL_4.equals(triggerLevel))
    {
      basicValidateString(errors, IGTTriggerEntity.DOC_TYPE, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_TYPE, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_GROUP, form, entity);
      basicValidateString(errors, IGTTriggerEntity.PARTNER_ID, form, entity);
      basicValidateString(errors, IGTTriggerEntity.CHANNEL_UID, form, entity); // 20031120 DDJ
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTTriggerEntity trigger = (IGTTriggerEntity)entity;
    TriggerAForm form = (TriggerAForm)actionContext.getActionForm();

    trigger.setFieldValue(IGTTriggerEntity.TRIGGER_TYPE, form.getTriggerTypeInteger());
    trigger.setFieldValue(IGTTriggerEntity.TRIGGER_LEVEL, form.getTriggerLevelInteger());
    trigger.setFieldValue(IGTTriggerEntity.PARTNER_FUNCTION_ID, form.getPartnerFunctionId());
    trigger.setFieldValue(IGTTriggerEntity.DOC_TYPE, form.getDocType());
    trigger.setFieldValue(IGTTriggerEntity.PARTNER_TYPE, form.getPartnerType());
    trigger.setFieldValue(IGTTriggerEntity.PARTNER_GROUP, form.getPartnerGroup());
    trigger.setFieldValue(IGTTriggerEntity.PARTNER_ID, form.getPartnerId());
    trigger.setFieldValue(IGTTriggerEntity.CHANNEL_UID, form.getChannelUidAsLong()); // 20031120 DDJ
    trigger.setFieldValue(IGTTriggerEntity.IS_LOCAL_PENDING, StaticUtils.booleanValue(form.getIsLocalPending())); //20030808AH

    trigger.setFieldValue(IGTTriggerEntity.PROCESS_ID, form.getProcessId());
    trigger.setFieldValue(IGTTriggerEntity.IS_REQUEST, StaticUtils.booleanValue(form.getIsRequest()));
    trigger.setFieldValue(IGTTriggerEntity.NUM_OF_RETRIES, form.getNumOfRetriesAsInteger());  // 20031120 DDJ
    trigger.setFieldValue(IGTTriggerEntity.RETRY_INTERVAL, form.getRetryIntervalAsInteger()); // 20031120 DDJ
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTTriggerEntity trigger = (IGTTriggerEntity)entity;

    Integer triggerLevel = StaticUtils.integerValue(actionContext.getRequest().getParameter("triggerLevel")  );
    trigger.setFieldValue(IGTTriggerEntity.TRIGGER_LEVEL, triggerLevel);
    //trigger.setFieldValue(IGTTriggerEntity.IS_LOCAL_PENDING, Boolean.TRUE); //20030808AH
  }
}