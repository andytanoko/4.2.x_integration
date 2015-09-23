/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTriggerListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of PartnerFunctions,
 * optionally based on a filtering condition.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetTriggerListEvent
       extends GetEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3088274349285610958L;

	public GetTriggerListEvent()
  {
    super();
  }

  public GetTriggerListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetTriggerListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetTriggerListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetTriggerListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetTriggerListEvent";
  }

}