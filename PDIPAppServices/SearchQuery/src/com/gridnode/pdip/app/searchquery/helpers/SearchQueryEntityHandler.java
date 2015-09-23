/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.searchquery.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.searchquery.entities.ejb.ISearchQueryLocalHome;
import com.gridnode.pdip.app.searchquery.entities.ejb.ISearchQueryLocalObj;
import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the SearchQueryBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public final class SearchQueryEntityHandler
  extends          LocalEntityHandler
{
  private SearchQueryEntityHandler()
  {
    super(SearchQuery.ENTITY_NAME);
  }

  /**
   * Get an instance of a SearchQueryEntityHandler.
   */
  public static SearchQueryEntityHandler getInstance()
  {
    SearchQueryEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(SearchQuery.ENTITY_NAME, true))
    {
      handler = (SearchQueryEntityHandler)EntityHandlerFactory.getHandlerFor(
                  SearchQuery.ENTITY_NAME, true);
    }
    else
    {
      handler = new SearchQueryEntityHandler();
      EntityHandlerFactory.putEntityHandler(SearchQuery.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the SearchQuery whose name is the specified.
   *
   * @param searchQueryName The name of the SearchQuery.
   * @return the SearchQuery having the specified name.
   */
  public SearchQuery findBySearchQueryName(String searchQueryName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SearchQuery.NAME, filter.getEqualOperator(),
      searchQueryName, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (SearchQuery)result.iterator().next();
  }


  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ISearchQueryLocalHome.class.getName(),
      ISearchQueryLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ISearchQueryLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return ISearchQueryLocalObj.class;
  }
}