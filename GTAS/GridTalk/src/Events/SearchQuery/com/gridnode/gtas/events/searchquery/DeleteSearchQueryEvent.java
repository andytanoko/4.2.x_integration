/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteSearchQueryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.searchquery;

import com.gridnode.gtas.model.searchquery.ISearchQuery;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a SearchQuery based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class DeleteSearchQueryEvent extends DeleteEntityListEvent
{ 
  private static String IS_ADMIN = "IS Admin";
  private static String LOGIN_ID = "Login ID";
  
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2633126760657809269L;

	/**
   * Constructor for DeleteSearchQueryEvent.
   * @param uids Collection of Uids of the SearchQuery entities to delete.
   * @throws EventException Invalid Uids specified.
   */
  public DeleteSearchQueryEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getLoginID()
  {
    return (String)getEventData(LOGIN_ID);
  }
  
  public void setLoginID(String loginID)
  {
    setEventData(LOGIN_ID, loginID);
  }
  
  public Boolean isAdmin()
  {
    return (Boolean)getEventData(IS_ADMIN);
  }
  
  public void setIsAdmin(boolean isAdmin)
  {
    setEventData(IS_ADMIN, isAdmin);
  }
  
  /**
   * Constructor for DeleteSearchQueryEvent.
   * @param searchQueryUid Uid of the SearchQuery entity to delete.
   * @throws EventException Invalid Uid specified.
   */
  public DeleteSearchQueryEvent(Long searchQueryUid) throws EventException
  {
    super(new Long[] { searchQueryUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteSearchQueryEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return ISearchQuery.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return ISearchQuery.UID;
  }
}