/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertTypeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-02-04     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2006-05-08     Neo Sok Lay         Hide P2P alert types
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Arrays;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.GetAlertTypeEvent;
import com.gridnode.gtas.events.alert.GetAlertTypeListEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultAlertTypeManager extends DefaultAbstractManager
  implements IGTAlertTypeManager
{
	private Long[] _excludeUids = {7L, 8L, 9L, 10L, 11L};
	
  DefaultAlertTypeManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ALERT_TYPE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Not implement yet!");
//    try
//    {
//      IGTAlertTypeEntity alertType = (IGTAlertTypeEntity)entity;
//
//      Long uid = (Long)alertType.getFieldValue(alertType.UID);
//      String name = alertType.getFieldString(alertType.NAME);
//      String description = alertType.getFieldString(alertType.DESCRIPTION);
//
//      UpdateAlertTypeEvent event = new UpdateAlertTypeEvent(uid,
//                                                    name,
//                                                    description);
//
//      handleUpdateEvent(event, (AbstractGTEntity)entity);
//    }
//    catch(Exception e)
//    {
//      throw new GTClientException("GTAS Error attempting to update", e);
//    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Not implement yet!");
//    try
//    {
//      IGTAlertTypeEntity alertType = (IGTAlertTypeEntity)entity;
//
//      String name = alertType.getFieldString(alertType.NAME);
//      String description = alertType.getFieldString(alertType.DESCRIPTION);
//      Long alertTypeType = (Long)alertType.getFieldValue(alertType.TYPE);
//
//      CreateAlertTypeEvent event = new CreateAlertTypeEvent(name,
//                                                    description);
//
//      handleCreateEvent(event, (AbstractGTEntity)entity);
//    }
//    catch(Exception e)
//    {
//      throw new GTClientException("GTAS Error attempting to update", e);
//    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ALERT_TYPE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ALERT_TYPE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAlertTypeEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws EventException
  {
  	if (_session.isNoP2P())
  	{
  		//NSL20060508 Hide p2p alert types
  		FilterConnector conn = null;
  		if (filter == null)
  		{
  			filter = new DataFilterImpl();
  		}
  		else
  		{
  			conn = filter.getAndConnector();
  		}
  		filter.addDomainFilter(conn, IGTAlertTypeEntity.UID, Arrays.asList(_excludeUids), true);
  	}
    return new GetAlertTypeListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("AlertTypes may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ALERT_TYPE.equals(entityType))
    {
      return new DefaultAlertTypeEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}