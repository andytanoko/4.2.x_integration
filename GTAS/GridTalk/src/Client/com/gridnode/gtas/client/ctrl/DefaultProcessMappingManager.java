/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProcessMappingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 * 2003-01-23     Andrew Hill         Cleaned up some leftover printlns
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.partnerprocess.CreateProcessMappingEvent;
import com.gridnode.gtas.events.partnerprocess.DeleteProcessMappingEvent;
import com.gridnode.gtas.events.partnerprocess.GetProcessMappingEvent;
import com.gridnode.gtas.events.partnerprocess.GetProcessMappingListEvent;
import com.gridnode.gtas.events.partnerprocess.UpdateProcessMappingEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultProcessMappingManager extends DefaultAbstractManager
  implements IGTProcessMappingManager
{
  DefaultProcessMappingManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PROCESS_MAPPING, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;
      Long uid = mapping.getUidLong();
      String processDef = mapping.getFieldString(IGTProcessMappingEntity.PROCESS_DEF);
      String partnerId = mapping.getFieldString(IGTProcessMappingEntity.PARTNER_ID);
      Boolean isInitiatingRole = (Boolean)mapping.getFieldValue(IGTProcessMappingEntity.IS_INITIATING_ROLE);
      String docType = mapping.getFieldString(IGTProcessMappingEntity.DOC_TYPE);
      Long sendChannelUid = (Long)mapping.getFieldValue(IGTProcessMappingEntity.SEND_CHANNEL_UID);

      UpdateProcessMappingEvent event = new UpdateProcessMappingEvent(uid,
                                                                      processDef,
                                                                      partnerId,
                                                                      isInitiatingRole,
                                                                      docType,
                                                                      sendChannelUid);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTProcessMappingEntity mapping = (IGTProcessMappingEntity)entity;

      String processDef = mapping.getFieldString(IGTProcessMappingEntity.PROCESS_DEF);
      String partnerId = mapping.getFieldString(IGTProcessMappingEntity.PARTNER_ID);
      Boolean isInitiatingRole = (Boolean)mapping.getFieldValue(IGTProcessMappingEntity.IS_INITIATING_ROLE);
      String docType = mapping.getFieldString(IGTProcessMappingEntity.DOC_TYPE);
      Long sendChannelUid = (Long)mapping.getFieldValue(IGTProcessMappingEntity.SEND_CHANNEL_UID);

      CreateProcessMappingEvent event = new CreateProcessMappingEvent(processDef,
                                                                      partnerId,
                                                                      isInitiatingRole,
                                                                      docType,
                                                                      sendChannelUid);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PROCESS_MAPPING;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PROCESS_MAPPING;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcessMappingEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcessMappingListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteProcessMappingEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PROCESS_MAPPING.equals(entityType))
    {
      return new DefaultProcessMappingEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}