/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteJmsDestinationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.alert.IJmsDestination;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

public class DeleteJmsDestinationEvent
  extends DeleteEntityListEvent
  implements IGuardedEvent
{
	private static final long serialVersionUID = -8726102418072691512L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION = "DeleteJmsDestination";

  public DeleteJmsDestinationEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  public DeleteJmsDestinationEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteJmsDestinationEvent";
  }

  public String getEntityType()
  {
    return IJmsDestination.ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return IJmsDestination.UID;
  }

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }
}