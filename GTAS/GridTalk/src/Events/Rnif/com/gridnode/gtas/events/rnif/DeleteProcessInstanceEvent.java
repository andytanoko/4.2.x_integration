/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcessInstanceEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.rnif;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.rnif.IProcessInstance;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting one or more ProcessInstance
 * based on UID.
 *
 * @author Liu Xiaohua
 *
 * @version GT 2.2.10
 * @since 2.0 I7
 */
public class DeleteProcessInstanceEvent
  extends DeleteEntityListEvent
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 904461297203433544L;
	public static final String GUARDED_FEATURE = "PROCESS";
  public static final String GUARDED_ACTION = "DeleteProcessInstance";

  public static final String DELETE_GRID_DOC = "DeleteGridDoc";

  /**
   * Constructor for DeleteProcessInstanceEvent.
   * @param instUID UID of the ProcessInstance entity to delete.
   * @param deleteGridDoc Whether to delete the GridDocuments of the process instances.
   * @throws EventException Invalid UID specified.
   */
  public DeleteProcessInstanceEvent(Long instUID, Boolean deleteGridDoc)
    throws EventException
  {
    super(new Long[] { instUID });
    setEventData(DELETE_GRID_DOC, deleteGridDoc);
  }

  /**
   * Constructor for DeleteProcessInstanceEvent.
   * @param uids Collection of UIDs of the ProcessInstance entities to delete.
   * @param deleteGridDoc Whether to delete the GridDocuments of the process instances.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteProcessInstanceEvent(Collection uids, Boolean deleteGridDoc)
    throws EventException
  {
    super(uids);
    setEventData(DELETE_GRID_DOC, deleteGridDoc);
  }

  public Boolean getDeleteGridDoc()
  {
    return (Boolean) getEventData(DELETE_GRID_DOC);
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IProcessInstance.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IProcessInstance.UID;
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteProcessInstanceEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

}