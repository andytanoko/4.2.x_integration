/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidateRegistrationInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 09 2003    Koh Han Sing        Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.registration;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for upgrading a GTAS license
 *
 * @author Koh Han Sing
 *
 * @version GT 2.1.19
 * @since 2.0 I8
 */

public class UpgradeLicenseEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4606875722881941055L;
	static final String GUARDED_FEATURE = "SYSTEM";
  static final String GUARDED_ACTION  = "UpgradeLicense";

  static final String LICENSE_FILE    = "License File";

  public UpgradeLicenseEvent(String licenseFile)
    throws EventException
  {
    checkSetString(LICENSE_FILE, licenseFile);
  }

  public String getLicenseFile()
  {
    return (String)getEventData(LICENSE_FILE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpgradeLicenseEvent";
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