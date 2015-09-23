/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelInfoListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 */
package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This event class contains the data for retrieval of a ChannelInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetChannelInfoListEvent extends GetEntityListEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6289938557056739372L;

	public GetChannelInfoListEvent()
  {
    super();
  }

  public GetChannelInfoListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetChannelInfoListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetChannelInfoListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetChannelInfoListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetChannelInfoListEvent";
  }


}