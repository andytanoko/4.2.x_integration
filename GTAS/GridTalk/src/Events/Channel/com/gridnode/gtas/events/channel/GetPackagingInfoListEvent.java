/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPackagingInfoListEvent.java
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



public class GetPackagingInfoListEvent extends GetEntityListEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2859226109597319199L;

	public GetPackagingInfoListEvent()
  {
    super();
  }

  public GetPackagingInfoListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetPackagingInfoListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetPackagingInfoListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetPackagingInfoListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPackagingInfoListEvent";
  }


}


