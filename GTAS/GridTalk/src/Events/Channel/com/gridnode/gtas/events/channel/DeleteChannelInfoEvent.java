/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteChannelInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 * Jul 15 2003    Neo Sok Lay             Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.channel;

import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for creation of a ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteChannelInfoEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7147187175656425481L;

	/**
   * Constructor for DeleteChannelInfoEvent.
   * @param uids Collection of UIDs of the ChannelInfo entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteChannelInfoEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteChannelInfoEvent.
   * @param uId UID of the ChannelInfo entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteChannelInfoEvent(Long uId) throws EventException
  {
    super(new Long[] { uId });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteChannelInfoEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IChannelInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IChannelInfo.UID;
  }

}