/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerGroupEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Deprecate method GetPartnerGroupEvent(String)
 *                                    and getName().
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving a Partner Group based on
 * UID or Name
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class GetPartnerGroupEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7320631265089748376L;
	public static final String UID  = "UID";
	/**
	 * @deprecated
	 */
  public static final String NAME = "Name";

  public GetPartnerGroupEvent(Long uID)
    throws EventException
  {
    checkSetLong(UID, uID);
  }

  /**
   * @deprecated Use only GetPartnerGroupEvent(Long)
   * @param name
   * @throws EventException
   */
  public GetPartnerGroupEvent(String name)
    throws EventException
  {
    checkSetString(NAME, name);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  /**
   * @deprecated
   * @return
   */
  public String getName()
  {
    return (String)getEventData(NAME);
  }


  public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerGroupEvent";
  }
}