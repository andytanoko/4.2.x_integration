/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.mapper.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.mapper.entities.ejb.IGridTalkMappingRuleLocalHome;
import com.gridnode.gtas.server.mapper.entities.ejb.IGridTalkMappingRuleLocalObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the GridTalkMappingRuleBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class GridTalkMappingRuleEntityHandler
  extends          LocalEntityHandler
{
  private GridTalkMappingRuleEntityHandler()
  {
    super(GridTalkMappingRule.ENTITY_NAME);
  }

  /**
   * Get an instance of a GridTalkMappingRuleEntityHandler.
   */
  public static GridTalkMappingRuleEntityHandler getInstance()
  {
    GridTalkMappingRuleEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GridTalkMappingRule.ENTITY_NAME, true))
    {
      handler = (GridTalkMappingRuleEntityHandler)EntityHandlerFactory.getHandlerFor(
                  GridTalkMappingRule.ENTITY_NAME, true);
    }
    else
    {
      handler = new GridTalkMappingRuleEntityHandler();
      EntityHandlerFactory.putEntityHandler(GridTalkMappingRule.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the GridTalkMappingRule whose name is the specified.
   *
   * @param gridTalkMappingRuleName The name of the GridTalkMappingRule.
   * @return the GridTalkMappingRule having the specified name.
   */
  public GridTalkMappingRule findByGridTalkMappingRuleName(String gridTalkMappingRuleName)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           GridTalkMappingRule.NAME,
                           filter.getEqualOperator(),
                           gridTalkMappingRuleName,
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (GridTalkMappingRule)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGridTalkMappingRuleLocalHome.class.getName(),
      IGridTalkMappingRuleLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IGridTalkMappingRuleLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IGridTalkMappingRuleLocalObj.class;
  }
}