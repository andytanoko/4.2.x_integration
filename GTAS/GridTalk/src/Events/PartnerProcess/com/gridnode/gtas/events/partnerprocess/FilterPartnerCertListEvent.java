/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:FilterPartnerCertListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 13 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of Certificate(s)
 * that belong to the Partner's enterprise, or do not belong to any enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class FilterPartnerCertListEvent
       extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2841687867114717229L;
	public static final String PARTNER_UID = "Partner UID";
  public static final String PARTNER_ID  = "Partner ID";

  public FilterPartnerCertListEvent(Long partnerUID)
    throws EventException
  {
    checkSetLong(PARTNER_UID, partnerUID);
  }

  public FilterPartnerCertListEvent(String partnerID)
    throws EventException
  {
    checkSetString(PARTNER_ID, partnerID);
  }

  public Long getPartnerUID()
  {
    return (Long)getEventData(PARTNER_UID);
  }

  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/FilterPartnerCertListEvent";
  }

}