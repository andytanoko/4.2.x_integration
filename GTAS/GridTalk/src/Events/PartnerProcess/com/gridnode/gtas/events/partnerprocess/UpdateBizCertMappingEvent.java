/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateBizCertMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Updated
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the update of an existing BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UpdateBizCertMappingEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1000652028909899615L;
	public static final String OWN_CERT_UID      = "Own Cert UID";
  public static final String PARTNER_CERT_UID  = "Partner Cert ID";
  public static final String UID               = "BizCertMapping UID";

  public UpdateBizCertMappingEvent(
    Long uid,
    Long partnerCertUID,
    Long ownCertUID) throws EventException
  {
    checkSetLong(UID, uid);
    checkSetLong(PARTNER_CERT_UID, partnerCertUID);
    checkSetLong(OWN_CERT_UID, ownCertUID);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
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
    return "java:comp/env/param/event/UpdateBizCertMappingEvent";
  }

}