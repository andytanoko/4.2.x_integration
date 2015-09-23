/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteCommInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 * Jul 15 2003    Neo Sok Lay             Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.channel;

import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for creation of a CommInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteCommInfoEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6879915241706547981L;

	/**
   * Constructor for DeleteCommInfoEvent.
   * @param uids Collection of UIDs of the CommInfo entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteCommInfoEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteCommInfoEvent.
   * @param uId UID of the CommInfo entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteCommInfoEvent(Long uId) throws EventException
  {
    super(new Long[] { uId });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteCommInfoEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return ICommInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return ICommInfo.UID;
  }
}