/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGridNodeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 * 2002-11-19     Andrew Hill         deactivate()
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.activation.SubmitGridNodeDeactivationEvent;
import com.gridnode.gtas.events.gridnode.GetGridNodeEvent;
import com.gridnode.gtas.events.gridnode.GetGridNodeListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultGridNodeManager extends DefaultAbstractManager
  implements IGTGridNodeManager
{

  DefaultGridNodeManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_GRIDNODE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("GridNode entities may not be updated via the manager");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("GridNode entities may not be created directly");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_GRIDNODE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_GRIDNODE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridNodeEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridNodeListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new UnsupportedOperationException("DeleteGridNodeEvent not available yet");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_GRIDNODE.equals(entityType))
    {
      return new DefaultGridNodeEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  public final void deactivate(long[] uids) throws GTClientException
  {
    if(uids == null) throw new java.lang.NullPointerException("Null uids array");
    if(uids.length == 0) return;
    try
    {
      Collection uidCollection = new ArrayList(uids.length);
      for(int i=0; i < uids.length; i++)
      {
        Long uid = new Long(uids[i]);
        uidCollection.add( uid );
      }
      SubmitGridNodeDeactivationEvent event = new SubmitGridNodeDeactivationEvent(uidCollection);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error deactivating gridNodes",t);
    }
  }
}