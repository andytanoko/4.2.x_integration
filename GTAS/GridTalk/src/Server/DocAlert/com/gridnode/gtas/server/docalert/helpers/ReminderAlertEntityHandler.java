/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)                                   
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.entities.ejb.IReminderAlertLocalHome;
import com.gridnode.gtas.server.docalert.entities.ejb.IReminderAlertLocalObj;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ReminderAlertBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public final class ReminderAlertEntityHandler
  extends          LocalEntityHandler
{
  private ReminderAlertEntityHandler()
  {
    super(ReminderAlert.ENTITY_NAME);
  }

  /**
   * Get an instance of a ReminderAlertEntityHandler.
   */
  public static ReminderAlertEntityHandler getInstance()
  {
    ReminderAlertEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ReminderAlert.ENTITY_NAME, true))
    {
      handler = (ReminderAlertEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ReminderAlert.ENTITY_NAME, true);
    }
    else
    {
      handler = new ReminderAlertEntityHandler();
      EntityHandlerFactory.putEntityHandler(ReminderAlert.ENTITY_NAME,
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
      IReminderAlertLocalHome.class.getName(),
      IReminderAlertLocalHome.class);
  }*/
  

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getHomeInterfaceClass()
	 */
	protected Class getHomeInterfaceClass() throws Exception
	{
		return IReminderAlertLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IReminderAlertLocalObj.class;
  }

  public void update(IEntity entity) throws java.lang.Throwable
  {
    ReminderAlertDAOHelper.getInstance().checkDuplicate(
      (ReminderAlert)entity, true);

    super.update(entity);
  }

	/**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    ReminderAlertDAOHelper.getInstance().checkDuplicate((ReminderAlert)entity, true);
		return super.updateEntity(entity);
	}

}