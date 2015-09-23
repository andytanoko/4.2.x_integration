/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateBizCertMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class CreateBizCertMappingEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3121138936289273501L;
	public static final String PARTNER_CERT_UID   = "Partner Cert UID";
  public static final String OWN_CERT_UID       = "Own Cert UID";
  public static final String PARTNER_ID         = "Partner ID";

  public CreateBizCertMappingEvent(
    String partnerID,
    Long partnerCertUID,
    Long ownCertUID) throws EventException
  {
    checkSetString(PARTNER_ID, partnerID);
    checkSetLong(PARTNER_CERT_UID, partnerCertUID);
    checkSetLong(OWN_CERT_UID, ownCertUID);
  }

  public String getPartnerID()
  {
    return (String)getEventData(PARTNER_ID);
  }

  public Long getPartnerCertUID()
  {
    return (Long)getEventData(PARTNER_CERT_UID);
  }

  public Long getOwnCertUID()
  {
    return (Long)getEventData(OWN_CERT_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateBizCertMappingEvent";
  }

}