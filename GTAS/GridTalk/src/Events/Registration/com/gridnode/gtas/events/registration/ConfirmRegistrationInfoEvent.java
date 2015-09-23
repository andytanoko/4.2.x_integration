/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfirmRegistrationInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.registration;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data to confirm the Product RegistrationInfo
 * validated earlier on.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I5
 */
public class ConfirmRegistrationInfoEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7957384716186592832L;
	static final String GUARDED_FEATURE = "SYSTEM";
  static final String GUARDED_ACTION  = "ConfirmRegistrationInfo";
 
  static final String SECURITY_PASSWORD = "Security Password";

  public ConfirmRegistrationInfoEvent(String secPassword)
    throws EventException
  {
    checkSetString(SECURITY_PASSWORD, secPassword);
  }

  public String getSecurityPassword()
  {
    return (String)getEventData(SECURITY_PASSWORD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ConfirmRegistrationInfoEvent";
  }

  // ************ Methods from IGuardedEvent ***********************
  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }


}