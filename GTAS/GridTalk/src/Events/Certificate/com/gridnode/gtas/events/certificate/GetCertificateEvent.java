/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh          Created
 */


package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
/**
 * This Event class contains the data for retrieve a Certificate based on
 * UID.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public class GetCertificateEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4909299310570407729L;
	public static final String CERTIFICATE_UID  = "Certificate UID";

  public GetCertificateEvent(Long certUID)
  {
    setEventData(CERTIFICATE_UID, certUID);
  }

  public Long getCertificateUID()
  {
    return (Long)getEventData(CERTIFICATE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetCertificateEvent";
  }
}


