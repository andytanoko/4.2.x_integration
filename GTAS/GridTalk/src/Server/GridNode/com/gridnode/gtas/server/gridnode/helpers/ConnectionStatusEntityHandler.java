/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionStatusEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.gridnode.helpers;

import com.gridnode.gtas.server.gridnode.entities.ejb.IConnectionStatusLocalHome;
import com.gridnode.gtas.server.gridnode.entities.ejb.IConnectionStatusLocalObj;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ConnectionStatus.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class ConnectionStatusEntityHandler
  extends          LocalEntityHandler
{
  private ConnectionStatusEntityHandler()
  {
    super(ConnectionStatus.ENTITY_NAME);
  }

  /**
   * Get an instance of a ConnectionStatusEntityHandler.
   */
  public static ConnectionStatusEntityHandler getInstance()
  {
    ConnectionStatusEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ConnectionStatus.ENTITY_NAME, true))
    {
      handler = (ConnectionStatusEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ConnectionStatus.ENTITY_NAME, true);
    }
    else
    {
      handler = new ConnectionStatusEntityHandler();
      EntityHandlerFactory.putEntityHandler(ConnectionStatus.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IConnectionStatusLocalHome.class.getName(),
      IConnectionStatusLocalHome.class);
  }*/

  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IConnectionStatusLocalObj.class;
  }

	protected Class getHomeInterfaceClass() throws Exception
	{
		return IConnectionStatusLocalHome.class;
	}

  // ********************** Own methods ******************************
}