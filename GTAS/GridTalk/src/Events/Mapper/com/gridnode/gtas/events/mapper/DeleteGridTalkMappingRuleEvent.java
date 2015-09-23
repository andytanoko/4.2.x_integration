/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteGridTalkMappingRuleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 02 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.gtas.model.mapper.IGridTalkMappingRule;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for the deletion of a GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteGridTalkMappingRuleEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3858585465550020638L;

	/**
   * Constructor for DeleteGridTalkMappingRuleEvent.
   * @param uids Collection of UIDs of the GridTalkMappingRule entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteGridTalkMappingRuleEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteGridTalkMappingRuleEvent.
   * @param gridTalkMappingRuleUID UID of the GridTalkMappingRule entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteGridTalkMappingRuleEvent(Long gridTalkMappingRuleUID)
    throws EventException
  {
    super(new Long[] { gridTalkMappingRuleUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteGridTalkMappingRuleEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IGridTalkMappingRule.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IGridTalkMappingRule.UID;
  }
}