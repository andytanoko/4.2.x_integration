/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of Partner,
 * optionally based on a filtering condition.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetPartnerListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2634410822248146842L;

	public GetPartnerListEvent()
  {
    super();
  }

  public GetPartnerListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetPartnerListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetPartnerListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetPartnerListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerListEvent";
  }
}