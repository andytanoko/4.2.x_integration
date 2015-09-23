/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 * Jun 21 2002    Goh Kan Mun             Modified - Change to get generic CommInfo
 *                                                   and passing the protocol type.
 */
package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This event class contains the data for retrieval of a CommInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetCommInfoListEvent extends GetEntityListEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8889615858855980137L;

	public GetCommInfoListEvent()
  {
    super();
  }

  public GetCommInfoListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetCommInfoListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetCommInfoListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetCommInfoListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetCommInfoListEvent";
  }


}