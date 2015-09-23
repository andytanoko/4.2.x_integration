/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchQueryLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.searchquery.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for SearchQueryBean
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public interface ISearchQueryLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new SearchQuery
   *
   * @param searchQuery The SearchQuery entity.
   * @return EJBLocalObject as a proxy to SearchQueryBean for the created
   *         SearchQuery.
   */
  public ISearchQueryLocalObj create(IEntity searchQuery)
    throws CreateException;

  /**
   * Load a SearchQueryBean
   *
   * @param primaryKey The primary key to the SearchQuery record
   * @return EJBLocalObject as a proxy to the loaded SearchQueryBean.
   */
  public ISearchQueryLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find SearchQuery records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IUserLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}