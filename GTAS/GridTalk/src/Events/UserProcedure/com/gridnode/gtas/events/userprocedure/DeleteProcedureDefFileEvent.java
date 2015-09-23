/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcedureDefFileEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created
 * Jul 15 2003    Neo Sok Lay             Extend from DeleteEntityListEvent.
 */
package com.gridnode.gtas.events.userprocedure;

import com.gridnode.gtas.model.userprocedure.IProcedureDefFile;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for deletion of a ProcedureDefinition File.
 *
 * @author Jagadeesh.
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteProcedureDefFileEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -475224303395558837L;

	/**
   * Constructor for DeleteProcedureDefFileEvent.
   * @param uids Collection of UIDs of the ProcedureDefFile entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteProcedureDefFileEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteProcedureDefFileEvent.
   * @param uID UID of the ProcedureDefFile entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteProcedureDefFileEvent(Long uID) throws EventException
  {
    super(new Long[] { uID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteProcedureDefFileEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IProcedureDefFile.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IProcedureDefFile.UID;
  }
}
