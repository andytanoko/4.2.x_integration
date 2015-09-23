/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSecurityInfoListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh             Created.
 */


package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This event class contains the data for retrieval of a PackagingInfo.
 *
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */


public class GetSecurityInfoListEvent extends GetEntityListEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1069462176755970624L;

	public GetSecurityInfoListEvent()
  {
    super();
  }

  public GetSecurityInfoListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetSecurityInfoListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetSecurityInfoListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetSecurityInfoListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetSecurityInfoListEvent";
  }

}

