/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeletePartnerFunctionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.partnerfunction;

import com.gridnode.gtas.model.partnerfunction.IPartnerFunction;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a PartnerFunction based on
 * Uid.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeletePartnerFunctionEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6595831351808650237L;

	/**
   * Constructor for DeletePartnerFunctionEvent.
   * @param uids Collection of UIDs of the PartnerFunction entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeletePartnerFunctionEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeletePartnerFunctionEvent.
   * @param partnerFunctionUid UID of the PartnerFunction entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeletePartnerFunctionEvent(Long partnerFunctionUid)
    throws EventException
  {
    super(new Long[]{partnerFunctionUid});
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeletePartnerFunctionEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IPartnerFunction.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IPartnerFunction.UID;
  }
}