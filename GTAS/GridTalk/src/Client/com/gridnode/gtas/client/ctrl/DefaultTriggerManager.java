/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultTriggerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-08-08     Andrew Hill         Support the new isLocalPending field for triggers
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.partnerprocess.CreateTriggerEvent;
import com.gridnode.gtas.events.partnerprocess.DeleteTriggerEvent;
import com.gridnode.gtas.events.partnerprocess.GetTriggerEvent;
import com.gridnode.gtas.events.partnerprocess.GetTriggerListEvent;
import com.gridnode.gtas.events.partnerprocess.UpdateTriggerEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultTriggerManager extends DefaultAbstractManager
  implements IGTTriggerManager
{
  DefaultTriggerManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_TRIGGER, session);
  }

  private Boolean getIsLocalPendingValueForEvent(IGTTriggerEntity trigger, Integer triggerType)
    throws GTClientException
  { //20030808AH
    try
    {
      if (trigger == null)
        throw new NullPointerException("trigger is null");
      if (triggerType == null)
        throw new NullPointerException("triggerType is null");

      if( triggerType.equals(IGTTriggerEntity.TRIGGER_IMPORT)
          || triggerType.equals(IGTTriggerEntity.TRIGGER_MANUAL_SEND))
      {
        return (Boolean)trigger.getFieldValue(IGTTriggerEntity.IS_LOCAL_PENDING);
      }
      else
      {
        return Boolean.TRUE;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining value of isLocalPending for use in event",t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTTriggerEntity trigger = (IGTTriggerEntity)entity;

      Long uid = (Long)trigger.getFieldValue(IGTTriggerEntity.UID);
      Integer triggerLevel = (Integer)trigger.getFieldValue(IGTTriggerEntity.TRIGGER_LEVEL);
      String pfId = trigger.getFieldString(IGTTriggerEntity.PARTNER_FUNCTION_ID);
      String processId = trigger.getFieldString(IGTTriggerEntity.PROCESS_ID);
      String docType = trigger.getFieldString(IGTTriggerEntity.DOC_TYPE);
      String partnerType = trigger.getFieldString(IGTTriggerEntity.PARTNER_TYPE);
      String partnerGroup = trigger.getFieldString(IGTTriggerEntity.PARTNER_GROUP);
      String partnerId = trigger.getFieldString(IGTTriggerEntity.PARTNER_ID);
      Integer triggerType = (Integer)trigger.getFieldValue(IGTTriggerEntity.TRIGGER_TYPE);
      Boolean isRequest = (Boolean)trigger.getFieldValue(IGTTriggerEntity.IS_REQUEST);
      Boolean isLocalPending = getIsLocalPendingValueForEvent(trigger, triggerType); //20030808AH
      Integer numOfRetries = (Integer)trigger.getFieldValue(IGTTriggerEntity.NUM_OF_RETRIES);  // 20031120 DDJ
      Integer retryInterval = (Integer)trigger.getFieldValue(IGTTriggerEntity.RETRY_INTERVAL); // 20031120 DDJ
      Long channelUid = (Long)trigger.getFieldValue(IGTTriggerEntity.CHANNEL_UID);             // 20031120 DDJ

      UpdateTriggerEvent event = new UpdateTriggerEvent(
        uid,
        triggerLevel,
        pfId,
        processId,
        docType,
        partnerType,
        partnerGroup,
        partnerId,
        triggerType,
        isRequest,
        isLocalPending, //20030808AH - isLocalPending
        numOfRetries,  // 20031120 DDJ
        retryInterval, // 20031120 DDJ
        channelUid     // 20031120 DDJ
      );

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Throwable t) //20030808AH - Catch throwable not exception
    {
      throw new GTClientException("Error attempting to update trigger", t); //20030808AH - Corrected msg
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTTriggerEntity trigger = (IGTTriggerEntity)entity;

      Integer triggerLevel = (Integer)trigger.getFieldValue(IGTTriggerEntity.TRIGGER_LEVEL);
      String pfId = trigger.getFieldString(IGTTriggerEntity.PARTNER_FUNCTION_ID);
      String processId = trigger.getFieldString(IGTTriggerEntity.PROCESS_ID);
      String docType = trigger.getFieldString(IGTTriggerEntity.DOC_TYPE);
      String partnerType = trigger.getFieldString(IGTTriggerEntity.PARTNER_TYPE);
      String partnerGroup = trigger.getFieldString(IGTTriggerEntity.PARTNER_GROUP);
      String partnerId = trigger.getFieldString(IGTTriggerEntity.PARTNER_ID);
      Integer triggerType = (Integer)trigger.getFieldValue(IGTTriggerEntity.TRIGGER_TYPE);
      Boolean isRequest = (Boolean)trigger.getFieldValue(IGTTriggerEntity.IS_REQUEST);
      Boolean isLocalPending = getIsLocalPendingValueForEvent(trigger, triggerType); //20030808AH
      Integer numOfRetries = (Integer)trigger.getFieldValue(IGTTriggerEntity.NUM_OF_RETRIES);  // 20031120 DDJ
      Integer retryInterval = (Integer)trigger.getFieldValue(IGTTriggerEntity.RETRY_INTERVAL); // 20031120 DDJ
      Long channelUid = (Long)trigger.getFieldValue(IGTTriggerEntity.CHANNEL_UID);             // 20031120 DDJ

      CreateTriggerEvent event = new CreateTriggerEvent(
        triggerLevel,
        pfId,
        processId,
        docType,
        partnerType,
        partnerGroup,
        partnerId,
        triggerType,
        isRequest,
        isLocalPending, //20030808AH - isLocalPending
        numOfRetries,  // 20031120 DDJ
        retryInterval, // 20031120 DDJ
        channelUid     // 20031120 DDJ
      );

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Throwable t) //20030808AH - Catch throwable not exception
    {
      throw new GTClientException("Error attempting to create trigger", t); //20030808AH - Corrected message
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_TRIGGER;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_TRIGGER;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetTriggerEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetTriggerListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteTriggerEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_TRIGGER.equals(entityType))
    {
      AbstractGTEntity trigger = new DefaultTriggerEntity(); //20030808AH
      trigger.setNewFieldValue(IGTTriggerEntity.IS_LOCAL_PENDING, Boolean.TRUE); //20030808AH
      return trigger; //20030808AH
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}