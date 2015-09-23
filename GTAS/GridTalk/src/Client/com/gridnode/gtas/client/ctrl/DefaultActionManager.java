/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultActionManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2006-05-08     Neo Sok Lay         To hide Actions of P2P alert types.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Arrays;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.CreateActionEvent;
import com.gridnode.gtas.events.alert.DeleteActionEvent;
import com.gridnode.gtas.events.alert.GetActionEvent;
import com.gridnode.gtas.events.alert.GetActionListEvent;
import com.gridnode.gtas.events.alert.UpdateActionEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultActionManager extends DefaultAbstractManager
  implements IGTActionManager
{
	private Long[] _excludeUids = {-16L, -17L, -18L, -19L, -20L, -21L}; //NSL20060508 exclude actions for p2p
	
  DefaultActionManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ACTION, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try 
    {
      IGTActionEntity action = (IGTActionEntity)entity;

      Long uid = (Long)action.getFieldValue(IGTActionEntity.UID);
      String name = action.getFieldString(IGTActionEntity.NAME);
      String description = action.getFieldString(IGTActionEntity.DESCRIPTION);
      Long messageId = (Long)action.getFieldValue(IGTActionEntity.MSG_ID);

//Hashtable params = new Hashtable();
//if(uid != null) params.put("uid", uid);
//if(name != null) params.put("name", name);
//if(description != null) params.put("description", description);
//if(messageId != null) params.put("messageId", messageId);
//StaticUtils.dumpMap(params);

      UpdateActionEvent event = new UpdateActionEvent(uid,
                                                    name,
                                                    description,
                                                    messageId);

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
      IGTActionEntity action = (IGTActionEntity)entity;

      String name = action.getFieldString(IGTActionEntity.NAME);
      String description = action.getFieldString(IGTActionEntity.DESCRIPTION);
      Long messageId = (Long)action.getFieldValue(IGTActionEntity.MSG_ID);

      CreateActionEvent event = new CreateActionEvent(name,
                                                      description,
                                                      messageId);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ACTION;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ACTION;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws EventException
  {
    return new GetActionEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
  	if (_session.isNoP2P())
  	{
  		//NSL20060508 hide actions of p2p alert types
  		FilterConnector conn = null;
  		if (filter == null)
  		{
  			filter = new DataFilterImpl();
  		}
  		else
  		{
  			conn = filter.getAndConnector();
  		}
  		filter.addDomainFilter(conn, IGTActionEntity.UID, Arrays.asList(_excludeUids), true);
  	}
 		return new GetActionListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  { //20030718AH
    return new DeleteActionEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACTION.equals(entityType))
    {
      return new DefaultActionEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}