/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria;
import com.gridnode.gtas.server.activation.model.SearchGridNodeQuery;
import com.gridnode.gtas.server.activation.model.SearchedGridNode;
import com.gridnode.pdip.framework.db.DataFilterHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.FilterOperator;

/**
 * This class is a helper class that handles the searching of GridNodes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchGridNodeHandler
  extends    AbstractActivationProcessHandler
  implements ISearchGridNodeKeys
{
  private static final Object _lock = new Object();
  private static SearchGridNodeHandler _self = null;

  private final Collection GN_EQMATCH_KEYS = new ArrayList();
  private final Collection WP_EQMATCH_KEYS = new ArrayList();
  private final Hashtable  QUERIES         = new Hashtable();

  private SearchGridNodeHandler() throws Exception
  {
    GN_EQMATCH_KEYS.add(SearchGridNodeCriteria.GRIDNODE_ID);
    GN_EQMATCH_KEYS.add(SearchGridNodeCriteria.CATEGORY);
    WP_EQMATCH_KEYS.add(SearchGridNodeCriteria.COUNTRY);
  }

  public static final SearchGridNodeHandler getInstance()
    throws Exception
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
        {
          _self = new SearchGridNodeHandler();
        }
      }
    }

    return _self;
  }

  /**
   * Submit a search request for GridNodes to the GridMaster.
   *
   * @param searchID The SearchID identifying the search request instance.
   * @param criteria The search criteria.
   */
  public void submitSearchQuery(Long searchID, SearchGridNodeCriteria criteria)
    throws Exception
  {
    SearchGridNodeQuery searchQuery = new SearchGridNodeQuery();
    searchQuery.setSearchID(searchID);
    searchQuery.setSearchCriteria(criteria);
    searchQuery.setDTSubmitted(getCurrentTimestamp());

    File queryFile = createQueryFile(criteria, searchID);
    QUERIES.put(searchID, searchQuery);

    post(null, _eventID, searchID.toString(), new File[]{queryFile}, null);
  }

  /**
   * Retrieve a Search query submitted previously.
   * @param searchID the SearchID for the search.
   * @return The Search query object.
   * @throws Exception No such SearchID.
   */
  public SearchGridNodeQuery retrieveSearchQuery(Long searchID)
    throws Exception
  {
    SearchGridNodeQuery searchQuery = (SearchGridNodeQuery)QUERIES.get(searchID);

    if (searchQuery == null)
      throw new Exception("Search Query not found for Search ID: "+searchID);

    return searchQuery;
  }

  /**
   * Invoked when acknowledgement for a previously submitted search request has
   * been received. This will update the Search query object for the relevant
   * search.
   *
   * @param searchID The SearchID identifying the search request.
   * @param searchedGns The GridNodes returned from the search.
   */
  public void onSearchResultsReturned(
    Long searchID, Collection searchedGns)
    throws Exception
  {
    SearchGridNodeQuery searchQuery = retrieveSearchQuery(searchID);

    searchQuery.setSearchResults(searchedGns);
    searchQuery.setDTResponded(getCurrentTimestamp());
  }

  /**
   * Determine the current states of the SearchedGridNode(s) by checking against
   * the ActivationRecords.
   *
   * @param searchResults The SearchedGridNode(s)
   */
  public void determineGridNodeStates(Collection searchResults)
    throws Throwable
  {
    Collection activeNodes = ActivationRecordEntityHandler.getInstance().getActiveGridNodeIDs();
    //Collection inactiveNodes = ActivationRecordEntityHandler.getInstance().getInactiveGridNodeIDs();
    Collection pendNodes = ActivationRecordEntityHandler.getInstance().getPendingGridNodeIDs();

    Object[] nodes = searchResults.toArray();
    String myNodeID = getMyGridNode().getID();
    for (int i=0; i<nodes.length; i++)
    {
      SearchedGridNode node = (SearchedGridNode)nodes[i];
      if (activeNodes.contains(node.getGridNodeID()))
        node.setState(new Short(node.STATE_ACTIVE));
      else if (pendNodes.contains(node.getGridNodeID()))
        node.setState(new Short(node.STATE_PENDING));
      else if (myNodeID.equals(node.getGridNodeID().toString()))
        node.setState(new Short(node.STATE_ME));
      else //if (inactiveNodes.contains(node.getGridNodeID()))
        node.setState(new Short(node.STATE_INACTIVE));
    }
  }

  // ************************ Private Methods *********************************

  /**
   * Convert the search criteria to a search query file.
   *
   * @param criteria the search criteria
   * @param searchID the SearchID for the search request.
   */
  private File createQueryFile(SearchGridNodeCriteria criteria, Long searchID)
  {
    File queryFile = null;
    DataFilterImpl query = null;
    String type = TYPE_GRIDNODE;
    String filename = new StringBuffer(_filePath).append(
                        SEARCH_FILE_PREFIX).append(searchID).append(
                        SEARCH_FILE_EXT).toString();

    switch (criteria.getCriteriaType())
    {
      case SearchGridNodeCriteria.CRITERIA_BY_GRIDNODE :
           query = createSearchFilter(criteria, GRIDNODE_KEYS, GN_EQMATCH_KEYS);
           break;
      case SearchGridNodeCriteria.CRITERIA_BY_WHITEPAGE :
           query  = createSearchFilter(criteria, WHITEPAGE_KEYS, WP_EQMATCH_KEYS);
           type = TYPE_WHITEPAGE;
           break;
    }

    queryFile = writeQueryToFile(query, type, filename);
    return queryFile;
  }

  /**
   * Convert a data filter to file.
   *
   * @param filter The data filter to convert.
   * @param type The type of search: by GridNode or Whitepage
   * @param filename The name of the file.
   */
  private File writeQueryToFile(DataFilterImpl filter, String type, String filename)
  {
    return DataFilterHandler.serializeToFile(filter, type, filename);
  }

  /**
   * Create a data filter based on a specified search criteria.
   * @param criteria The search criteria
   * @param searchKeys The search keys supported
   * @param equalMatchKeys The search keys that require equality match.
   */
  private DataFilterImpl createSearchFilter(
    SearchGridNodeCriteria criteria,
    Number[] searchKeys,
    Collection equalMatchKeys)
  {
    boolean empty = true;
    DataFilterImpl filter = new DataFilterImpl();
    FilterConnector connector = null;
    for (int i=0; i<searchKeys.length; i++)
    {
      Object val = criteria.getFieldValue(searchKeys[i]);
      if (isSet(val))
      {
        empty = false;
        filter.addSingleFilter(connector, getSearchKey(searchKeys[i]),
            getOperator(searchKeys[i], equalMatchKeys, filter), val, false);
      }

      if (connector == null)
      {
        connector = getConnector(criteria.getMatchType(), filter);
      }
    }

    return empty ? null : filter;
  }

  /**
   * retrieve a search key for a field.
   * @param fieldID the FieldID of the field.
   * @return the Search key value, or <b>null</b> if none found.
   */
  private String getSearchKey(Number fieldID)
  {
    return _config.getString(SEARCH_KEY_PREFIX+fieldID, null);
  }

  protected String readEventID() throws Exception
  {
    String eventID = _config.getString(SEARCH_EVENT_ID, null);
    if (eventID == null)
      throw new Exception("No Event ID for Search GridNode configured!!");
    return eventID;
  }

  protected String readAckEventID() throws Exception
  {
    return null;
  }

  private boolean isSet(Object val)
  {
    boolean set = true;
    if (val != null)
    {
      if (val instanceof String)
        set = (((String)val).trim().length() > 0);
    }
    else
      set = false;

    return set;
  }

  /**
   * Determine the connector based on the match type.
   *
   * @param matchType The matching type: All or Any
   * @param filter The data filter.
   * @return the connector for the matching type.
   *
   */
  private FilterConnector getConnector(
    short matchType, DataFilterImpl filter)
  {
    return (matchType == SearchGridNodeCriteria.MATCH_ALL)?
           filter.getAndConnector() :
           filter.getOrConnector();
  }

  /**
   * Determine the operator for a search key.
   *
   * @param key The key
   * @param equalMatchKeys List of keys that require equality match.
   * @param filter The data filter.
   */
  private FilterOperator getOperator(
    Number key, Collection equalMatchKeys, DataFilterImpl filter)
  {
    return (equalMatchKeys.contains(key) ?
           filter.getEqualOperator() :
           filter.getLocateOperator());
  }
}