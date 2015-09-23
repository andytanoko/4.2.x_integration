/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteBizCertMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.gtas.model.partnerprocess.IBizCertMapping;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class DeleteBizCertMappingEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7792899067924759268L;

	/**
   * Constructor for DeleteBizCertMappingEvent.
   * @param uids Collection of UIDs of the BizCertMapping entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteBizCertMappingEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteBizCertMappingEvent.
   * @param uid UID of the BizCertMapping entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteBizCertMappingEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteBizCertMappingEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IBizCertMapping.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IBizCertMapping.UID;
  }
}