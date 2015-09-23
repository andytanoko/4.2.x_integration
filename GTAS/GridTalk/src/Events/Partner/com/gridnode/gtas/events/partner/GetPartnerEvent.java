/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Deprecated GetPartnerEvent(String)
 *                                    and getPartnerID().
 */
package com.gridnode.gtas.events.partner;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving Partner based on
 * UID
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class GetPartnerEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5130508143848973749L;
	public static final String UID        = "UID";
	
	/**
	 * @deprecated
	 */
  public static final String PARTNER_ID = "Partner ID";

  public GetPartnerEvent(Long uID)
    throws EventException
  {
    checkSetLong(UID, uID);
  }

  /**
   * @deprecated use only GetPartnerEvent(Long)
   * @param partnerID
   * @throws EventException
   */
  public GetPartnerEvent(String partnerID)
    throws EventException
  {
    checkSetString(PARTNER_ID, partnerID);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  /**
   * @deprecated
   * @return
   */
  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }


  public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerEvent";
  }
}