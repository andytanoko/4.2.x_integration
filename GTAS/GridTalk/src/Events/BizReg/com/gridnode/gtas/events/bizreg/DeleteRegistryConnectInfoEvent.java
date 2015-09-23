/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteRegistryConnectInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Delete by UIDs instead if names (p-tier
 *                                    cannot handle).
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.gtas.model.bizreg.IRegistryConnectInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event contains the data required for delete one or more
 * RegistryConnectInfo.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DeleteRegistryConnectInfoEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8103753491982617752L;

	/**
   * Constructs a DeleteRegistryConnectInfoEvent
   * 
   * @param uids The uids of the RegistryConnectInfo to delete.
   * @throws EventException
   */
  public DeleteRegistryConnectInfoEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.IEvent#getEventName()
   */
  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteRegistryConnectInfoEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IRegistryConnectInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IRegistryConnectInfo.UID;
  }

}
