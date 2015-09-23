/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistryConnectInfoListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         Extend from GetEntityListEvent 
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

/**
 * This event is used for obtaining a collection of RegistryConnectInfo objects. 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class GetRegistryConnectInfoListEvent extends GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -983097540902154980L;


	/**
   * Constructs a  GetRegistryConnectInfoListEvent for returning all
   * rows.
   * 
   * @param filter Filtering condition on RegistryConnectInfo to retrieve.
   */
  public GetRegistryConnectInfoListEvent(IDataFilter filter)
  {
    super(filter);
  }

  /**
   * Constructs a  GetRegistryConnectInfoListEvent for returning a maximum
   * number of rows.
   * 
   * @param filter  Filtering condition on RegistryConnectInfo to retrieve.
   * @param maxRows Maximum number of rows to return
   */
  public GetRegistryConnectInfoListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  /**
   * Constructs a  GetRegistryConnectInfoListEvent for returning a maximum
   * number of rows starting from a particular row.
   * 
   * @param filter  Filtering condition on RegistryConnectInfo to retrieve.
   * @param maxRows Maximum number of rows to return
   * @param startRow The starting row to return.
   */
  public GetRegistryConnectInfoListEvent(
    IDataFilter filter,
    int maxRows,
    int startRow)
  {
    super(filter, maxRows, startRow);
  }

  /**
   * Constructs a  GetRegistryConnectInfoListEvent for returning a maximum
   * number of rows starting from a particular row of a previously retrieved
   * list.
   * 
   * @param listID The ListID of the list.
   * @param maxRows Maximum number of rows to return
   * @param startRow The starting row to return.
   * @throws EventException Invalid input parameters.
   */
  public GetRegistryConnectInfoListEvent(
    String listID,
    int maxRows,
    int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  /**
   * Constructs a  GetRegistryConnectInfoListEvent for returning all
   * rows.
   */ 
  public GetRegistryConnectInfoListEvent()
  { 
  }
  
  
  /**
   * @see com.gridnode.pdip.framework.rpf.event.IEvent#getEventName()
   */
  public String getEventName()
  {
    return "java:comp/env/param/event/GetRegistryConnectInfoListEvent";
  }

}
