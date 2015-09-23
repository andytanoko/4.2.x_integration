/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetLanguageCodeListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.locale;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of LanguageCode(s),
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetLanguageCodeListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6324135684600414449L;

	public GetLanguageCodeListEvent()
  {
    super();
  }

  public GetLanguageCodeListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetLanguageCodeListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetLanguageCodeListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetLanguageCodeListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetLanguageCodeListEvent";
  }

}