/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsRouterEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.connection.helpers;

import com.gridnode.gtas.server.connection.entities.ejb.IJmsRouterLocalHome;
import com.gridnode.gtas.server.connection.entities.ejb.IJmsRouterLocalObj;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the JmsRouter.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public final class JmsRouterEntityHandler
  extends          LocalEntityHandler
{
  private JmsRouterEntityHandler()
  {
    super(JmsRouter.ENTITY_NAME);
  }

  /**
   * Get an instance of a JmsRouterEntityHandler.
   */
  public static JmsRouterEntityHandler getInstance()
  {
    JmsRouterEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(JmsRouter.ENTITY_NAME, true))
    {
      handler = (JmsRouterEntityHandler)EntityHandlerFactory.getHandlerFor(
                  JmsRouter.ENTITY_NAME, true);
    }
    else
    {
      handler = new JmsRouterEntityHandler();
      EntityHandlerFactory.putEntityHandler(JmsRouter.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IJmsRouterLocalHome.class.getName(),
      IJmsRouterLocalHome.class);
  }*/

  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IJmsRouterLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IJmsRouterLocalObj.class;
  }

  // ********************** Own methods ******************************

}