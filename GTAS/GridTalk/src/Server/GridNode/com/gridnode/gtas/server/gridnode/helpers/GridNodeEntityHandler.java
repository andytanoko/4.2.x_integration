/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.gridnode.helpers;

import com.gridnode.gtas.server.gridnode.entities.ejb.IGridNodeLocalHome;
import com.gridnode.gtas.server.gridnode.entities.ejb.IGridNodeLocalObj;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the GridNode.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class GridNodeEntityHandler
  extends          LocalEntityHandler
{
  private GridNodeEntityHandler()
  {
    super(GridNode.ENTITY_NAME);
  }

  /**
   * Get an instance of a GridNodeEntityHandler.
   */
  public static GridNodeEntityHandler getInstance()
  {
    GridNodeEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GridNode.ENTITY_NAME, true))
    {
      handler = (GridNodeEntityHandler)EntityHandlerFactory.getHandlerFor(
                  GridNode.ENTITY_NAME, true);
    }
    else
    {
      handler = new GridNodeEntityHandler();
      EntityHandlerFactory.putEntityHandler(GridNode.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGridNodeLocalHome.class.getName(),
      IGridNodeLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IGridNodeLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IGridNodeLocalObj.class;
  }

  // ********************** Own methods ******************************
}