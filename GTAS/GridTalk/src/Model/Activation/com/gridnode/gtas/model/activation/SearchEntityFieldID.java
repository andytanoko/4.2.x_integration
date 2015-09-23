/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created.
 */
package com.gridnode.gtas.model.activation;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Activation module for
 * GridNode Search function.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchEntityFieldID
{
  private Hashtable _table;
  private static SearchEntityFieldID _self = null;

  private SearchEntityFieldID()
  {
    _table = new Hashtable();

    //SearchGridNodeCriteria
    _table.put(ISearchGridNodeCriteria.ENTITY_NAME,
      new Number[]
      {
        ISearchGridNodeCriteria.BUSINESS_DESC,
        ISearchGridNodeCriteria.CATEGORY,
        ISearchGridNodeCriteria.CONTACT_PERSON,
        ISearchGridNodeCriteria.COUNTRY,
        ISearchGridNodeCriteria.CRITERIA,
        ISearchGridNodeCriteria.DUNS,
        ISearchGridNodeCriteria.EMAIL,
        ISearchGridNodeCriteria.FAX,
        ISearchGridNodeCriteria.GRIDNODE_ID,
        ISearchGridNodeCriteria.GRIDNODE_NAME,
        ISearchGridNodeCriteria.MATCH,
        ISearchGridNodeCriteria.TEL,
        ISearchGridNodeCriteria.WEBSITE,
      });

    //SearchGridNodeQuery
    _table.put(ISearchGridNodeQuery.ENTITY_NAME,
      new Number[]
      {
        ISearchGridNodeQuery.CRITERIA,
        ISearchGridNodeQuery.DT_RESPONDED,
        ISearchGridNodeQuery.DT_SUBMITTED,
        ISearchGridNodeQuery.RESULTS,
        ISearchGridNodeQuery.SEARCH_ID,
      });

    //SearchedGridNode
    _table.put(ISearchedGridNode.ENTITY_NAME,
      new Number[]
      {
        ISearchedGridNode.GRIDNODE_ID,
        ISearchedGridNode.GRIDNODE_NAME,
        ISearchedGridNode.STATE,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new SearchEntityFieldID();
    }
    return _self._table;
  }
}