/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented ISearchQuery.CAN_DELETE
 */
package com.gridnode.gtas.model.searchquery;

import java.util.Hashtable;

import com.gridnode.pdip.app.searchquery.model.ISearchQuery;

/**
 * This class provides the fieldIDs of the entities in the SearchQuery module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchQueryEntityFieldID
{
  private Hashtable _table;
  private static SearchQueryEntityFieldID _self = null;

  private SearchQueryEntityFieldID()
  {
    _table = new Hashtable();

    //SearchQuery
    _table.put(ISearchQuery.ENTITY_NAME,
      new Number[]
      {
//        ISearchQuery.CAN_DELETE,
        ISearchQuery.CONDITIONS,
        ISearchQuery.CREATED_BY,
        ISearchQuery.DESCRIPTION,
        ISearchQuery.NAME,
        ISearchQuery.UID,
        ISearchQuery.IS_PUBLIC
      });

    //Condition
    _table.put(ICondition.ENTITY_NAME,
      new Number[]
      {
        ICondition.FIELD,
        ICondition.OPERATOR,
        ICondition.TYPE,
        ICondition.VALUES,
        ICondition.XPATH
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new SearchQueryEntityFieldID();
    }
    return _self._table;
  }
}