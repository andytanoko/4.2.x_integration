/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridTalkMappingRuleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 02 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the retrieving of a
 * GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridTalkMappingRuleEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2160348075812074166L;
	public static final String MAPPING_RULE_UID = "MappingRule UID";

  public GetGridTalkMappingRuleEvent(Long mappingRuleUID)
  {
    setEventData(MAPPING_RULE_UID, mappingRuleUID);
  }

  public Long getGridTalkMappingRuleUID()
  {
    return (Long)getEventData(MAPPING_RULE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGridTalkMappingRuleEvent";
  }

}