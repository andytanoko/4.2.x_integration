/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.entities.ejb.ITriggerLocalHome;
import com.gridnode.gtas.server.partnerprocess.entities.ejb.ITriggerLocalObj;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the TriggerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class TriggerEntityHandler
  extends          LocalEntityHandler
{
  private TriggerEntityHandler()
  {
    super(Trigger.ENTITY_NAME);
  }

  /**
   * Get an instance of a TriggerEntityHandler.
   */
  public static TriggerEntityHandler getInstance()
  {
    TriggerEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(Trigger.ENTITY_NAME, true))
    {
      handler = (TriggerEntityHandler)EntityHandlerFactory.getHandlerFor(
                  Trigger.ENTITY_NAME, true);
    }
    else
    {
      handler = new TriggerEntityHandler();
      EntityHandlerFactory.putEntityHandler(Trigger.ENTITY_NAME,
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
      ITriggerLocalHome.class.getName(),
      ITriggerLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ITriggerLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return ITriggerLocalObj.class;
  }
}