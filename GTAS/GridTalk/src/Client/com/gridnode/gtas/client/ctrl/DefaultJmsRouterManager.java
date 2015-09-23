/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultJmsRouterManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.connection.GetConnectionSetupResultEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultJmsRouterManager extends DefaultAbstractManager
  implements IGTJmsRouterManager
{

  DefaultJmsRouterManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_JMS_ROUTER, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("JmsRouter entities may not be updated");
  }


  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new NotApplicableException("Create is not applicable to JmsRouter");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_JMS_ROUTER;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_JMS_ROUTER;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(uid != null)
    {
      throw new java.lang.IllegalArgumentException("JmsRouter may not be retrieved by uid");
    }
    return new GetConnectionSetupResultEvent();
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new NotApplicableException("JmsRouter does not have associated list events");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("JmsRouter entities may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_JMS_ROUTER.equals(entityType))
    {
      return new DefaultJmsRouterEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }
}