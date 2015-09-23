/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2006-05-08     Neo Sok Lay         To hide Alerts of P2P alert types.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.CreateAlertEvent;
import com.gridnode.gtas.events.alert.DeleteAlertEvent;
import com.gridnode.gtas.events.alert.GetAlertEvent;
import com.gridnode.gtas.events.alert.GetAlertListEvent;
import com.gridnode.gtas.events.alert.UpdateAlertEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultAlertManager extends DefaultAbstractManager
  implements IGTAlertManager
{
	private Long[] _excludeAlertTypes = {7L, 8L, 9L, 10L, 11L}; //p2p alert types to be hidden
	
  DefaultAlertManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ALERT, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTAlertEntity alert = (IGTAlertEntity)entity;

      Long uid = (Long)alert.getFieldValue(IGTAlertEntity.UID);
      String name = alert.getFieldString(IGTAlertEntity.NAME);
      String description = alert.getFieldString(IGTAlertEntity.DESCRIPTION);
      Long alertType = (Long)alert.getFieldValue(IGTAlertEntity.TYPE);
      Collection actions = (Collection)alert.getFieldValue(IGTAlertEntity.ACTIONS);

      UpdateAlertEvent event = new UpdateAlertEvent(uid,
                                                    name,
                                                    description,
                                                    alertType,
                                                    actions);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update alert", e); //20030718AH
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTAlertEntity alert = (IGTAlertEntity)entity;

      String name = alert.getFieldString(IGTAlertEntity.NAME);
      String description = alert.getFieldString(IGTAlertEntity.DESCRIPTION);
      Long alertType = (Long)alert.getFieldValue(IGTAlertEntity.TYPE);
      Collection actions = (Collection)alert.getFieldValue(IGTAlertEntity.ACTIONS);

      CreateAlertEvent event = new CreateAlertEvent(name,
                                                    description,
                                                    alertType,
                                                    actions);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update alert", e); //20030718AH
    }
  }

  public void updateActions(IGTAlertEntity entity, Collection actions)
    throws GTClientException
  {
    if(entity == null)
    {
      throw new java.lang.NullPointerException("null entity reference");
    }
    if(actions == null)
    {
      actions = new Vector(0);
    }
    try
    {
      /** @todo create action */
//      SetActionListForAlertEvent event = new SetActionListForAlertEvent(entity.getUidLong(), actions);
//      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating actions list for Alert entity", t);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ALERT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ALERT;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAlertEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
  	if (_session.isNoP2P())
  	{
  		//NSL20060508 hide P2P alerts
  		FilterConnector conn = null;
  		if (filter == null)
  		{
  			filter = new DataFilterImpl();
  		}
  		else
  		{
  			conn = filter.getAndConnector();
  		}
  		
  		filter.addDomainFilter(conn, IGTAlertEntity.TYPE, Arrays.asList(_excludeAlertTypes), true);
  	}
 		return new GetAlertListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteAlertEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ALERT.equals(entityType))
    {
      return new DefaultAlertEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}