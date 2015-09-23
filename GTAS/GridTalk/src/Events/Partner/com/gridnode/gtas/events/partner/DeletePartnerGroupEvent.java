/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeletePartnerGroupEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.gtas.model.partner.IPartnerGroup;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a Partner Group based on
 * its UID.
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2  I1
 * @since 2.0.2
 */
public class DeletePartnerGroupEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3848096713961828312L;

	/**
   * Constructor for DeletePartnerGroupEvent.
   * @param uID UID of the PartnerGroup entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeletePartnerGroupEvent(Long uID) throws EventException
  {
    super(new Long[] { uID });
  }

  /**
   * Constructor for DeletePartnerGroupEvent.
   * @param uids Collection of UIDs of the PartnerGroup entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeletePartnerGroupEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeletePartnerGroupEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IPartnerGroup.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IPartnerGroup.UID;
  }
}