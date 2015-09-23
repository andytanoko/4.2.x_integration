/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for updating a Partner Type.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdatePartnerTypeEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8781001357461265758L;
	public static final String UID          = "UID";
  public static final String DESCRIPTION  = "Description";

  public UpdatePartnerTypeEvent(Long uID, String description)
    throws EventException
  {
    checkSetLong(UID, uID);
    checkSetString(DESCRIPTION, description);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdatePartnerTypeEvent";
  }

}