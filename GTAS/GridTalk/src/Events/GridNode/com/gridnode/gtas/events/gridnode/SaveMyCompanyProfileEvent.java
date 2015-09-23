/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveMyCompanyProfileEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "GRIDNODE"
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.gtas.model.gridnode.ICompanyProfile;
import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Map;

/**
 * This Event class contains the data for update a My CompanyProfile.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I5
 */
public class SaveMyCompanyProfileEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -975741871742081579L;
	public static final String GUARDED_FEATURE = "GRIDNODE";
  public static final String GUARDED_ACTION  = "SaveMyCompanyProfile";

  public static final String UPD_PROFILE   = "Upd Profile";

  public SaveMyCompanyProfileEvent(Map profile) throws EventException
  {
    checkObject(UPD_PROFILE, profile, Map.class);
    checkString("CoyName", profile.get(ICompanyProfile.COY_NAME));
    checkString("Country", profile.get(ICompanyProfile.COUNTRY));
    checkString("Language", profile.get(ICompanyProfile.LANGUAGE));

    setEventData(UPD_PROFILE, profile);
  }

  public Map getUpdProfile()
  {
    return (Map)getEventData(UPD_PROFILE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SaveMyCompanyProfileEvent";
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