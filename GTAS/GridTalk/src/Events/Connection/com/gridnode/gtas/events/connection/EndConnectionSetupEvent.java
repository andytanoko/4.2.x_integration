/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EndConnectionSetupEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.connection;

import java.util.Collection;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.gtas.events.IGuardedEvent;

/**
 * This Event class contains the data for ending the Connection setup process
 * with a probably re-ordered list of available GridMasters and JmsRouters.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class EndConnectionSetupEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4700463147604262939L;
	private static final String GRIDMASTERS = "Available GridMasters";
  private static final String ROUTERS     = "Available Routers";

  public static final String GUARDED_ACTION   = "EndConnectionSetup";
  public static final String GUARDED_FEATURE  = "SYSTEM";

  public EndConnectionSetupEvent(
    Collection availableGridMasters, Collection availableRouters)
    throws EventException
  {
    checkSetCollection(GRIDMASTERS, availableGridMasters, Long.class);
    checkSetCollection(ROUTERS, availableRouters, Long.class);
  }

  public Collection getAvailableGridMasters()
  {
    return (Collection)getEventData(GRIDMASTERS);
  }

  public Collection getAvailableRouters()
  {
    return (Collection)getEventData(ROUTERS);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/EndConnectionSetupEvent";
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