/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CancelRegistrationInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 18 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.registration;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data to cancel the Product RegistrationInfo
 * validated earlier on.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I5
 */
public class CancelRegistrationInfoEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6383213057446431079L;
	static final String GUARDED_FEATURE = "SYSTEM";
  static final String GUARDED_ACTION  = "CancelRegistrationInfo";

  public CancelRegistrationInfoEvent()
    //throws EventException
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CancelRegistrationInfoEvent";
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