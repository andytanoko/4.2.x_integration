/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetConnectionStatusListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
 
/**
 * This Event class contains the data for retrieve a list of GridNode(s),
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetConnectionStatusListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9215172879885715200L;

	public GetConnectionStatusListEvent()
  {
    super();
  }

  public GetConnectionStatusListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetConnectionStatusListEvent";
  }

}