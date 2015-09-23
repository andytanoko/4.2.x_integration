/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteSecurityInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh               Created
 * Jul 15 2003    Neo Sok Lay             Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.channel;

import com.gridnode.gtas.model.channel.ISecurityInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for creation of a SecurityInfo.
 *
 * @author Jagadeesh.
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteSecurityInfoEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8264442267283005293L;

	/**
   * Constructor for DeleteSecurityInfoEvent.
   * @param uids Collection of UIDs of the SecurityInfo entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteSecurityInfoEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteSecurityInfoEvent.
   * @param uId UID of the SecurityInfo entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteSecurityInfoEvent(Long uId) throws EventException
  {
    super(new Long[] { uId });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteSecurityInfoEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return ISecurityInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return ISecurityInfo.UID;
  }
}
