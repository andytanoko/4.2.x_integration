/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This entity keeps record of a submitted GridNode search query. This entity
 * is not persistent in database.
 *
 * The Model:<BR><PRE>
 *   SearchID        - The identification for a particular submitted search.
 *   DTSubmitted     - Timestamp when the search is submitted.
 *   DTResponded     - Timestamp when the search results returned.
 *   SearchCriteria  - The search criteria.
 *   SearchResults   - The search results.
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchGridNodeQuery
  extends    AbstractEntity
  implements ISearchGridNodeQuery
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2648217694958549359L;
	private Long      _searchID;
  private Timestamp _dtSubmitted;
  private Timestamp _dtResponded;
  private SearchGridNodeCriteria _criteria;
  private Collection _results = new ArrayList();

  public SearchGridNodeQuery()
  {
  }

  public String getEntityDescr()
  {
    return getSearchID() + "- submitted at: "+ getDTSubmitted();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return SEARCH_ID;
  }

  // ************************* Getters & Setters *****************************

  public void setSearchID(Long searchID)
  {
    _searchID = searchID;
  }

  public Long getSearchID()
  {
    return _searchID;
  }

  public void setDTSubmitted(Timestamp dtSubmitted)
  {
    _dtSubmitted = dtSubmitted;
  }

  public Timestamp getDTSubmitted()
  {
    return _dtSubmitted;
  }

  public void setDTResponded(Timestamp dtResponded)
  {
    _dtResponded = dtResponded;
  }

  public Timestamp getDTResponded()
  {
    return _dtResponded;
  }

  public void setSearchCriteria(SearchGridNodeCriteria criteria)
  {
    _criteria = criteria;
  }

  public SearchGridNodeCriteria getSearchCriteria()
  {
    return _criteria;
  }

  public void setSearchResults(Collection results)
  {
    _results = results;
  }

  public Collection getSearchResults()
  {
    return _results;
  }
}