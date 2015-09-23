/**
 * This software is the proprietary information of Registry Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) Registry Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 */
package com.gridnode.pdip.app.bizreg.search.model;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This entity keeps record of a submitted Registry search query. This entity
 * is not persistent in database.
 *
 * The Model:<BR><PRE>
 *   SearchID        - The identification for a particular submitted search.
 *   DTSubmitted     - Timestamp when the search is submitted.
 *   DTResponded     - Timestamp when the search results returned.
 *   SearchCriteria  - The search criteria.
 *   SearchResults   - The search results, after massaging on the raw search results.
 *   RawSearchResults- The raw search results as returned from the search request server.
 *   IsException     - Indicates whether exception has occurred during the search.
 *   ExceptionStr    - A String representation of the exception that occurred.
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SearchRegistryQuery
  extends    AbstractEntity
  implements ISearchRegistryQuery
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2209035455356637434L;
	private Long _searchID;
  private Timestamp _dtSubmitted;
  private Timestamp _dtResponded;
  private SearchRegistryCriteria _criteria;
  private Collection _results = new ArrayList();
  private Collection _rawResults = new ArrayList();
  private boolean _isException;
  private String _exceptionStr;

  public SearchRegistryQuery()
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

  public void setSearchCriteria(SearchRegistryCriteria criteria)
  {
    _criteria = criteria;
  }

  public SearchRegistryCriteria getSearchCriteria()
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
  
  public void setRawSearchResults(Collection rawResults)
  {
    _rawResults = rawResults;
  }

  public Collection getRawSearchResults()
  {
    return _rawResults;
  }
  
  public void setExceptionStr(String exceptionStr)
  {
    _exceptionStr = exceptionStr;
  }
  
  public String getExceptionStr()
  {
    return _exceptionStr;
  }
  
  public void setIsException(boolean isException)
  {
    _isException = isException;
  }
  
  public boolean isException()
  {
    return _isException;
  }
}