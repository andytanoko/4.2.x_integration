/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBusinessEntityListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Remove implementation for IGuardedEvent
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

/**
 * This Event class contains the data for retrieve a list of BusinessEntities,
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class GetBusinessEntityListEvent
  extends    GetEntityListEvent
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "ENTERPRISE";
//  public static final String GUARDED_ACTION  = "GetBusinessEntityList";

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7740688333440468194L;

	public GetBusinessEntityListEvent()
  {
    super();
  }

  public GetBusinessEntityListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetBusinessEntityListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetBusinessEntityListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetBusinessEntityListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetBusinessEntityListEvent";
  }

  // ************* From IGuardedEvent *************************

//  public String getGuardedFeature()
//  {
//    return GUARDED_FEATURE;
//  }
//
//  public String getGuardedAction()
//  {
//    return GUARDED_ACTION;
//  }

}