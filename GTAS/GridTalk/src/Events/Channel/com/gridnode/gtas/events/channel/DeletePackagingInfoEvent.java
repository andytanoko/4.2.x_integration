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
 * Aug 27 2002    Jagadeesh               Created
 * Jul 15 2003    Neo Sok Lay             Extend from DeleteEntityListEvent
 */

package com.gridnode.gtas.events.channel;

import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for creation of a PackagingInfo.
 *
 * @author Jagadeesh
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeletePackagingInfoEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7161156676950131103L;

	/**
   * Constructor for DeletePackagingInfoEvent.
   * @param uids Collection of UIDs of the PackagingInfo entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeletePackagingInfoEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeletePackagingInfoEvent.
   * @param uId UID of the PackagingInfo entity to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeletePackagingInfoEvent(Long uId) throws EventException
  {
    super(new Long[] { uId });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeletePackagingInfoEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IPackagingInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IPackagingInfo.UID;
  }
}
