/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 27 2003    Jagadeesh           Added : To support generating sequential no.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.backend.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.backend.entities.ejb.IPortLocalHome;
import com.gridnode.gtas.server.backend.entities.ejb.IPortLocalObj;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the PortBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class PortEntityHandler
  extends          LocalEntityHandler
{
  private PortEntityHandler()
  {
    super(Port.ENTITY_NAME);
  }

  /**
   * Get an instance of a PortEntityHandler.
   */
  public static PortEntityHandler getInstance()
  {
    PortEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(Port.ENTITY_NAME, true))
    {
      handler = (PortEntityHandler)EntityHandlerFactory.getHandlerFor(
                  Port.ENTITY_NAME, true);
    }
    else
    {
      handler = new PortEntityHandler();
      EntityHandlerFactory.putEntityHandler(Port.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the Port whose name is the specified.
   *
   * @param portName The name of the Port.
   * @return the Port having the specified port name.
   */
  public Port findByPortName(String portName)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Port.NAME,
      filter.getEqualOperator(),
      portName,
      false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (Port)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPortLocalHome.class.getName(),
      IPortLocalHome.class);
  }*/

  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IPortLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPortLocalObj.class;
  }
  /**
   * Returns next sequential no, given the port id.
   * @param portUID
   * @return
   * @throws Throwable
   */
  public String getNextSequenceNo(Long portUID) throws Throwable
  {
    IPortLocalObj remote = (IPortLocalObj) findByPrimaryKey(portUID);
    return remote.getNextSeqRunningNo();
  }

}