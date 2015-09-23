/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 21 2003    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add updateEntity(entity)                                   
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.server.alert.entities.ejb.IAlertTriggerLocalHome;
import com.gridnode.gtas.server.alert.entities.ejb.IAlertTriggerLocalObj;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertTriggerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public final class AlertTriggerEntityHandler
  extends          LocalEntityHandler
{
  private AlertTriggerEntityHandler()
  {
    super(AlertTrigger.ENTITY_NAME);
  }

  /**
   * Get an instance of a AlertTriggerEntityHandler.
   */
  public static AlertTriggerEntityHandler getInstance()
  {
    AlertTriggerEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(AlertTrigger.ENTITY_NAME, true))
    {
      handler = (AlertTriggerEntityHandler)EntityHandlerFactory.getHandlerFor(
                  AlertTrigger.ENTITY_NAME, true);
    }
    else
    {
      handler = new AlertTriggerEntityHandler();
      EntityHandlerFactory.putEntityHandler(AlertTrigger.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertTriggerLocalHome.class.getName(),
      IAlertTriggerLocalHome.class);
  }*/

  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertTriggerLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IAlertTriggerLocalObj.class;
  }

  public void update(IEntity entity) throws java.lang.Throwable
  {
    AlertTriggerDAOHelper.getInstance().checkDuplicate(
      (AlertTrigger)entity, true);

    super.update(entity);
  }
  
  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    AlertTriggerDAOHelper.getInstance().checkDuplicate((AlertTrigger)entity, true);
		return super.updateEntity(entity);
	}

	public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = (Long[])getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (int i=0; i<affectedUIDs.length; i++)
      remove(affectedUIDs[i]);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    AlertTriggerDAOHelper.getInstance().checkCanDelete(
      (AlertTrigger)getEntityByKeyForReadOnly(uId));

    super.remove( uId);
  }


}