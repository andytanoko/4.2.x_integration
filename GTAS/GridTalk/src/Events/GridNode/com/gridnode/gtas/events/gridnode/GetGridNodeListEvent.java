/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridNodeListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28 2002    Neo Sok Lay         Created
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
public class GetGridNodeListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5859307397130083359L;

	public GetGridNodeListEvent()
  {
    super();
  }

  public GetGridNodeListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGridNodeListEvent";
  }

}