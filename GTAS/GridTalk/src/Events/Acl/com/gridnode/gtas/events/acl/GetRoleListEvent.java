/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListEvent.java
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
 * This Event class contains the data for retrieve a list of Roles,
 * optionally based on a filtering condition.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetRoleListEvent extends GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2654930451866051484L;
	public static final String GET_ROLE_NAME = "Get Role Name";

  public GetRoleListEvent()
  {
    super();
  }

  public GetRoleListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetRoleListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetRoleListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetRoleListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetRoleListEvent";
  }

}