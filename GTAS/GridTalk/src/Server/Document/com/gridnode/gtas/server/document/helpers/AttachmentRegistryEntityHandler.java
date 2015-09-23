/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentRegistryEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 25 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.entities.ejb.IAttachmentRegistryLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IAttachmentRegistryLocalObj;
import com.gridnode.gtas.server.document.model.AttachmentRegistry;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AttachmentRegistryBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class AttachmentRegistryEntityHandler
  extends          LocalEntityHandler
{
  private AttachmentRegistryEntityHandler()
  {
    super(AttachmentRegistry.ENTITY_NAME);
  }

  /**
   * Get an instance of a AttachmentRegistryEntityHandler.
   */
  public static AttachmentRegistryEntityHandler getInstance()
  {
    Logger.debug("[AttachmentRegistryEntityHandler.getInstance]");
    AttachmentRegistryEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(AttachmentRegistry.ENTITY_NAME, true))
    {
      handler = (AttachmentRegistryEntityHandler)EntityHandlerFactory.getHandlerFor(
                  AttachmentRegistry.ENTITY_NAME, true);
    }
    else
    {
      handler = new AttachmentRegistryEntityHandler();
      EntityHandlerFactory.putEntityHandler(AttachmentRegistry.ENTITY_NAME,
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
      IAttachmentRegistryLocalHome.class.getName(),
      IAttachmentRegistryLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAttachmentRegistryLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IAttachmentRegistryLocalObj.class;
  }
}