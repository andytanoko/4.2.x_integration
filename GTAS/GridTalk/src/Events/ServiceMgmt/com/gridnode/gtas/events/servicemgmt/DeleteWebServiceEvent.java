/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteWebServiceEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.gtas.model.servicemgmt.*;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;


public class DeleteWebServiceEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 999186652227509738L;

	/**
   * Constructor for DeleteWebServiceEvent.
   * @param uids Collection of Uids of the WebService entities to delete.
   * @throws EventException Invalid Uids specified.
   */
  public DeleteWebServiceEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteWebServiceEvent.
   * @param webServiceUid Uid of the WebService entity to delete.
   * @throws EventException Invalid Uid specified.
   */
  public DeleteWebServiceEvent(Long webServiceUid) throws EventException
  {
    super(new Long[] { webServiceUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteWebServiceEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IWebService.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IWebService.UID;
  }
}