/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteAlertTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 * Jul 14 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.model.alert.IAlertTrigger;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a AlertTrigger based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.1
 */
public class DeleteAlertTriggerEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2198604234590504677L;

	/**
   * Constructor for DeleteAlertTriggerEvent.
   * 
   * @param uids Collection of UIDs of the AlertTrigger entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteAlertTriggerEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteAlertTriggerEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IAlertTrigger.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IAlertTrigger.UID;
  }

}