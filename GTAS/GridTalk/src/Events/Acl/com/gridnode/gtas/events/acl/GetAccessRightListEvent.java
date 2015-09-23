/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAccessRightListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 14 2002    Goh Kan Mun             Created
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Event class contains the data for retrieve a list of AccessRights,
 * optionally based on a filtering condition.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetAccessRightListEvent extends GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5250739603797397092L;

	public GetAccessRightListEvent()
  {
    super();
  }

  public GetAccessRightListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetAccessRightListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetAccessRightListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetAccessRightListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAccessRightListEvent";
  }

}