/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetupConnectionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.connection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.gtas.events.IGuardedEvent;

/**
 * This Event class contains the data for triggering a Connectio setup.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class SetupConnectionEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3171467437218062834L;
	private static final String CURRENT_LOCATION = "Current Location";
  private static final String SERVICING_ROUTER = "Servicing Router";
  private static final String SECURITY_PWD     = "Security Password";

  public static final String GUARDED_ACTION   = "SetupConnection";
  public static final String GUARDED_FEATURE  = "SYSTEM";

  public SetupConnectionEvent(
    String currentLocation, String servicingRouter, String securityPassword)
    throws EventException
  {
    checkSetString(CURRENT_LOCATION, currentLocation);
    checkSetString(SERVICING_ROUTER, servicingRouter);
    checkSetString(SECURITY_PWD, securityPassword);
  }

  public String getCurrentLocation()
  {
    return (String)getEventData(CURRENT_LOCATION);
  }

  public String getServicingRouter()
  {
    return (String)getEventData(SERVICING_ROUTER);
  }

  public String getSecurityPassword()
  {
    return (String)getEventData(SECURITY_PWD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SetupConnectionEvent";
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