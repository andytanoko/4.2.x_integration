/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of AlertTriggers,
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5109945676682103510L;

	public GetAlertTriggerListEvent()
  {
    super();
  }

  public GetAlertTriggerListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetAlertTriggerListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetAlertTriggerListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetAlertTriggerListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAlertTriggerListEvent";
  }

}