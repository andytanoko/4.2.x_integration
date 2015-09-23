/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchResultsNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type. 
 */
package com.gridnode.pdip.app.bizreg.search;

import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.exceptions.NestingException;


/**
 * Notification message on the results of a search request submitted earlier.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * Destination   - The destination type being searched: 'PublicRegistry'
 *               - Can be used for message selection, e.g. destination='PublicRegistry'
 * SearchId      - Unique Identifier of the Search at the Destination.
 * SearchResults - SearchResults object representing the results from the processed 
 *                 search request. Must be Serializable.
 * </pre>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SearchResultsNotification extends AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2435865688751460816L;

	public static final String DESTINATION_PUBLIC_REGISTRY = "PublicRegistry";

  private String _destination;
  private Long _searchId;
  private Object _searchResults;
  private String _searchException;
  private boolean _isException;
  
  /**
   * Constructs a SearchResultsNotification instance
   */
  public SearchResultsNotification(
    String destination, 
    Long searchId,
    Object searchResults)
  {
    _destination = destination;
    _searchId = searchId;
    _searchResults = searchResults;
    putProperty("destination", _destination);
  }

  /**
   * Constructs a SearchResultsNotification instance
   */
  public SearchResultsNotification(
    String destination, 
    Long searchId,
    Throwable searchException)
  {
    _destination = destination;
    _searchId = searchId;
    _searchException = NestingException.generateStackTraceString(searchException);
    _isException = true;
    putProperty("destination", _destination);
  }

  /**
   * @see com.gridnode.gtas.server.notify.INotification#getNotificationID()
   */
  public String getNotificationID()
  {
    return "SearchResults";
  }

  /**
   * Gets the Destination.
   * @return The Destination value.
   */
  public String getDestination()
  {
    return _destination;
  }

  /**
   * Gets the Search Results
   * @return The SearchResults value.
   */
  public Object getSearchResults()
  {
    return _searchResults;
  }

  /**
   * Gets the Search identifier
   * @return The SearchId value.
   */
  public Long getSearchId()
  {
    return _searchId;
  }

  /**
   * Gets the string representation of the Search exception.
   * 
   * @return String representation of the Search exception, if one
   * has occurred.
   */
  public String getSearchException()
  {
    return _searchException;
  }
  
  /**
   * Checks whether exception has occurred during the search.
   * 
   * @return <b>true</b> if exception has occurred and
   * the SearchException indicates the exception that occurred, <b>false</b> otherwise.
   */
  public boolean isException()
  {
    return _isException;
  }
}
