/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivityEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 03 2003    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.partnerfunction.helpers;

import com.gridnode.gtas.server.partnerfunction.entities.ejb.IWorkflowActivityLocalHome;
import com.gridnode.gtas.server.partnerfunction.entities.ejb.IWorkflowActivityLocalObj;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the WorkflowActivityBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class WorkflowActivityEntityHandler
  extends          LocalEntityHandler
{
  private WorkflowActivityEntityHandler()
  {
    super(WorkflowActivity.ENTITY_NAME);
  }

  /**
   * Get an instance of a WorkflowActivityEntityHandler.
   */
  public static WorkflowActivityEntityHandler getInstance()
  {
    WorkflowActivityEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(WorkflowActivity.ENTITY_NAME, true))
    {
      handler = (WorkflowActivityEntityHandler)EntityHandlerFactory.getHandlerFor(
                  WorkflowActivity.ENTITY_NAME, true);
    }
    else
    {
      handler = new WorkflowActivityEntityHandler();
      EntityHandlerFactory.putEntityHandler(WorkflowActivity.ENTITY_NAME,
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
      IWorkflowActivityLocalHome.class.getName(),
      IWorkflowActivityLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IWorkflowActivityLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IWorkflowActivityLocalObj.class;
  }
}