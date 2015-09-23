/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.helpers;

import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.model.searchquery.SearchQueryEntityFieldID;
import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerHome;
import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerObj;
import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class provides common services used by the action classes.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class ActionHelper
{
  /**
   * Obtain the EJBObject for the SearchQueryManagerBean.
   *
   * @return The EJBObject to the SearchQueryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.3
   */
  public static ISearchQueryManagerObj getManager()
         throws ServiceLookupException
  {
    return (ISearchQueryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISearchQueryManagerHome.class.getName(),
      ISearchQueryManagerHome.class,
      new Object[0]);
  }

  /**
   * Convert an SearchQuery to Map object.
   *
   * @param searchQuery The SearchQuery to convert.
   * @return A Map object converted from the specified SearchQuery.
   *
   * @since 2.3
   */
  public static Map convertSearchQueryToMap(SearchQuery searchQuery)
  {
    return SearchQuery.convertToMap(
             searchQuery,
             SearchQueryEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of SearchQuery to Map objects.
   *
   * @param searchQueryList The collection of SearchQuery to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of SearchQuerys.
   *
   * @since 2.3
   */
  public static Collection convertSearchQueryToMapObjects(Collection searchQueryList)
  {
    return SearchQuery.convertEntitiesToMap(
             (SearchQuery[])searchQueryList.toArray(
             new SearchQuery[searchQueryList.size()]),
             SearchQueryEntityFieldID.getEntityFieldID(),
             null);
  }

}