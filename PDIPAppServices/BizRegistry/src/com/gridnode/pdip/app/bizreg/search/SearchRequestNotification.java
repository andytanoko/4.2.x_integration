/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRequestNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 */
package com.gridnode.pdip.app.bizreg.search;

import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * Notification message on the request for a search.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * Destination   - The destination type to be searched: 'PublicRegistry'
 *               - Can be used for message selection, e.g. destination='PublicRegistry'
 * SearchId      - Unique Identifier of the Search at the Destination.
 * Criteria      - SearchCriteria object representing the criteria for the search request.
 *                 Must be Serializable.
 * </pre>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SearchRequestNotification extends AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9133461555203080801L;

	public static final String DESTINATION_PUBLIC_REGISTRY = "PublicRegistry";

  private String _destination;
  private Long _searchId;
  private Object _searchCriteria;
  
  /**
   * Constructs a SearchRequestNotification instance
   */
  public SearchRequestNotification(
    String destination, 
    Long searchId,
    Object searchCriteria)
  {
    _destination = destination;
    _searchId = searchId;
    _searchCriteria = searchCriteria;
    putProperty("destination", _destination);
  }

  /**
   * @see com.gridnode.gtas.server.notify.INotification#getNotificationID()
   */
  public String getNotificationID()
  {
    return "SearchRequest";
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
   * Gets the Search Criteria
   * @return The SearchCriteria value.
   */
  public Object getSearchCriteria()
  {
    return _searchCriteria;
  }

  /**
   * Gets the Search identifier
   * @return The SearchId value.
   */
  public Long getSearchId()
  {
    return _searchId;
  }

}
