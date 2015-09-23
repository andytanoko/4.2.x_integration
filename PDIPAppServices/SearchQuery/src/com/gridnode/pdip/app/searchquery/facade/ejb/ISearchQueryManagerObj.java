/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchQueryManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 */
package com.gridnode.pdip.app.searchquery.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for SearchQueryManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public interface ISearchQueryManagerObj
  extends        EJBObject
{
  /**
   * Create a new SearchQuery.
   *
   * @param searchQuery The SearchQuery entity.
   * @return the uid of the SearchQuery created.
   */
  public Long createSearchQuery(SearchQuery searchQuery)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a SearchQuery
   *
   * @param searchQuery The SearchQuery entity with changes.
   */
  public void updateSearchQuery(SearchQuery searchQuery)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a SearchQuery.
   *
   * @param searchQueryUId The UID of the SearchQuery to delete.
   */
  public void deleteSearchQuery(Long searchQueryUId)
    throws DeleteEntityException, SystemException, RemoteException;

  
  public void deleteSearchQuery(Long searchQueryUId, String userID, boolean isAdmin)
    throws DeleteEntityException, SystemException, RemoteException;
  
  /**
   * Find a SearchQuery using the SearchQuery UID.
   *
   * @param searchQueryUId The UID of the SearchQuery to find.
   * @return The SearchQuery found, or <B>null</B> if none exists with that
   * UID.
   */
  public SearchQuery findSearchQuery(Long searchQueryUId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a SearchQuery using the SearchQuery Name.
   *
   * @param searchQueryName The Name of the SearchQuery to find.
   * @return The SearchQuery found, or <B>null</B> if none exists.
   */
  public SearchQuery findSearchQuery(String searchQueryName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of SearchQuery that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of SearchQuery found, or empty collection if none
   * exists.
   */
  public Collection findSearchQuerys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of SearchQuery that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of SearchQuery found, or empty collection if
   * none exists.
   */
  public Collection findSearchQuerysKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;
}