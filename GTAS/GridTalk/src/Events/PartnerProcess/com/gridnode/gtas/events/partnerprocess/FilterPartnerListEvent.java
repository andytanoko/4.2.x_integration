/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:FilterPartnerListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of Partner(s),
 * based on a condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class FilterPartnerListEvent
       extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 128828064364726222L;

	public static final String CONDITION  = "Condition";

  public static final Integer HAVE_CERT_MAPPING = new Integer(1);
  public static final Integer NO_CERT_MAPPING   = new Integer(2);

  public FilterPartnerListEvent(Integer condition)
    throws EventException
  {
    super();

    checkInteger(CONDITION, condition);
    if (condition.intValue() != HAVE_CERT_MAPPING.intValue() &&
        condition.intValue() != NO_CERT_MAPPING.intValue())
      throw new EventException("Invalid filter condition specified: "+condition);

    setEventData(CONDITION, condition);
  }

  public Integer getCondition()
  {
    return (Integer)getEventData(CONDITION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/FilterPartnerListEvent";
  }

}