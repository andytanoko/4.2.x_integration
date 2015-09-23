/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerTypeListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
/**
 * This Event class contains the data for retrieve a list of Partner Type,
 * optionally based on a filtering condition.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetPartnerTypeListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8055654435708900327L;

	public GetPartnerTypeListEvent()
  {
    super();
  }

  public GetPartnerTypeListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetPartnerTypeListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetPartnerTypeListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetPartnerTypeListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerTypeListEvent";
  }
}