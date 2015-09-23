/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.searchquery.facade.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.searchquery.helpers.Logger;
import com.gridnode.pdip.app.searchquery.helpers.SearchQueryEntityHandler;
import com.gridnode.pdip.app.searchquery.model.ISearchQuery;
import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * This bean provides the app service layer implementation of the
 * mapper module. It serves as the facade to the business methods of
 * this module.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchQueryManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6289749616822561608L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in ISearchQueryManagerObj

  // ********************* Methods for SearchQuery

  /**
   * Create a new SearchQuery.
   *
   * @param searchQuery The SearchQuery entity.
   * @return the uid of the SearchQuery created.
   */
  public Long createSearchQuery(SearchQuery searchQuery)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[SearchQueryManagerBean.createSearchQuery] Enter");
    Long key = null;

    try
    {
      key = (Long)getSearchQueryEntityHandler().createEntity(searchQuery).getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[SearchQueryManagerBean.createSearchQuery] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[SearchQueryManagerBean.createSearchQuery] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.createSearchQuery] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.createSearchQuery(SearchQuery) Error ",
        ex);
    }

    Logger.log("[SearchQueryManagerBean.createSearchQuery] Exit");
    return key;
  }

  /**
   * Update a SearchQuery
   *
   * @param searchQuery The SearchQuery entity with changes.
   */
  public void updateSearchQuery(SearchQuery searchQuery)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[SearchQueryManagerBean.updateSearchQuery] Enter");

    try
    {
      getSearchQueryEntityHandler().update(searchQuery);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[SearchQueryManagerBean.updateSearchQuery] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.updateSearchQuery] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.updateSearchQuery(SearchQuery) Error ",
        ex);
    }

    Logger.log("[SearchQueryManagerBean.updateSearchQuery] Exit");
  }

  /**
   * Delete a SearchQuery.
   *
   * @param searchQueryUId The UID of the SearchQuery to delete.
   */
  public void deleteSearchQuery(Long searchQueryUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[SearchQueryManagerBean.deleteSearchQuery] Enter");

    try
    {
      getSearchQueryEntityHandler().remove(searchQueryUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.deleteSearchQuery(searchQueryUId) Error ",
        ex);
    }

    Logger.log("[SearchQueryManagerBean.deleteSearchQuery] Exit");
  }

  public void deleteSearchQuery(Long searchQueryUId, String userID, boolean isAdmin)
  throws DeleteEntityException, SystemException
  {
    Logger.log("[SearchQueryManagerBean.deleteSearchQuery] Enter");

    try
    {
      if(isAdmin)
      {
        getSearchQueryEntityHandler().remove(searchQueryUId);
      }
      else
      {
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, ISearchQuery.CREATED_BY, filter.getEqualOperator(), userID, false);
        filter.addSingleFilter(filter.getAndConnector(), ISearchQuery.UID, filter.getEqualOperator(), searchQueryUId, false);
        
        Collection searchQuerys = getSearchQueryEntityHandler().findByFilter(filter);
        if(searchQuerys != null && searchQuerys.size() > 0)
        {
          getSearchQueryEntityHandler().remove(searchQueryUId);
        }
        else
        {
          throw new DeleteEntityException("User role is not allowed to delete the search query that is not belong to his/her !");
        }
      }
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.deleteSearchQuery] Error ", ex);
      throw new SystemException(
                                "SearchQueryManagerBean.deleteSearchQuery(searchQueryUId) Error ",
                                ex);
    }

    Logger.log("[SearchQueryManagerBean.deleteSearchQuery] Exit");
  }
  
  /**
   * Find a SearchQuery using the SearchQuery UID.
   *
   * @param searchQueryUId The UID of the SearchQuery to find.
   * @return The SearchQuery found, or <B>null</B> if none exists with that
   * UID.
   */
  public SearchQuery findSearchQuery(Long searchQueryUId)
    throws FindEntityException, SystemException
  {
    Logger.log("[SearchQueryManagerBean.findSearchQuery] UID: "+searchQueryUId);

    SearchQuery searchQuery = null;

    try
    {
      searchQuery = (SearchQuery)getSearchQueryEntityHandler().getEntityByKeyForReadOnly(searchQueryUId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.findSearchQuery(searchQueryUId) Error ",
        ex);
    }

    return searchQuery;
  }

  /**
   * Find a SearchQuery using the SearchQuery Name.
   *
   * @param searchQueryName The Name of the SearchQuery to find.
   * @return The SearchQuery found, or <B>null</B> if none exists.
   */
  public SearchQuery findSearchQuery(String searchQueryName)
    throws FindEntityException, SystemException
  {
    Logger.log("[SearchQueryManagerBean.findSearchQuery] Document Type Name: "
      +searchQueryName);

    SearchQuery searchQuery = null;

    try
    {
      searchQuery = (SearchQuery)getSearchQueryEntityHandler().findBySearchQueryName(searchQueryName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuery] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.findSearchQuery(searchQueryName) Error ",
        ex);
    }

    return searchQuery;
  }

  /**
   * Find a number of SearchQuery that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of SearchQuery found, or empty collection if none
   * exists.
   */
  public Collection findSearchQuerys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[SearchQueryManagerBean.findSearchQuerys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection searchQuerys = null;
    try
    {
      searchQuerys = getSearchQueryEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerys] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.findSearchQuerys(filter) Error ",
        ex);
    }

    return searchQuerys;
  }

  /**
   * Find a number of SearchQuery that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of SearchQuery found, or empty collection if
   * none exists.
   */
  public Collection findSearchQuerysKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[SearchQueryManagerBean.findSearchQuerysKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection searchQuerysKeys = null;
    try
    {
      searchQuerysKeys = getSearchQueryEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerysKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerysKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchQueryManagerBean.findSearchQuerysKeys] Error ", ex);
      throw new SystemException(
        "SearchQueryManagerBean.findSearchQuerysKeys(filter) Error ",
        ex);
    }

    return searchQuerysKeys;
  }

 // ********************* Methods for EntityHandler

  private SearchQueryEntityHandler getSearchQueryEntityHandler()
  {
     return SearchQueryEntityHandler.getInstance();
  }
}