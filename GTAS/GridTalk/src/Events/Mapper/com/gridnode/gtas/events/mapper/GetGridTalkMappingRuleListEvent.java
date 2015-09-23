/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridTalkMappingRuleListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 03 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of GridTalkMappingRule,
 * optionally based on a filtering condition.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridTalkMappingRuleListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2359166262905237143L;

	public GetGridTalkMappingRuleListEvent()
  {
    super();
  }

  public GetGridTalkMappingRuleListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetGridTalkMappingRuleListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetGridTalkMappingRuleListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetGridTalkMappingRuleListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGridTalkMappingRuleListEvent";
  }

}