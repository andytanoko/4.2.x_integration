/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcessDefEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.rnif;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.rnif.IProcessDef;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting one or more BusinessEntitoes
 * based on UID.
 *
 * @author Liu Xiao Hua
 *
 * @version GT 2.2.10
 * @since 2.0 I4
 */
public class DeleteProcessDefEvent
  extends    DeleteEntityListEvent
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7044960756486166363L;
	public static final String GUARDED_FEATURE = "PROCESS";
  public static final String GUARDED_ACTION  = "DeleteProcessDef";

  /**
   * Constructor for DeleteProcessDefEvent.
   * 
   * @param defUID UID of the ProcessDef entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteProcessDefEvent(Long defUID)
    throws EventException
  {
    super(new Long[]{defUID});
  }

  /**
   * Constructor for DeleteProcessDefEvent.
   * 
   * @param defUIDs Collection UIDs of the ProcessDef entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteProcessDefEvent(Collection defUIDs)
    throws EventException
  {
    super(defUIDs);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteProcessDefEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IProcessDef.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IProcessDef.UID;
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