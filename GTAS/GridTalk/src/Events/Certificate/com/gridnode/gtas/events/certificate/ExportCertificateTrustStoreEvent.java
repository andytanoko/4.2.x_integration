package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportCertificateTrustStoreEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class ExportCertificateTrustStoreEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2014106253337150256L;
	public static final String CERTIFICATE_UID  = "Certificate UID";

  public ExportCertificateTrustStoreEvent(Long certUID)
  {
    setEventData(CERTIFICATE_UID, certUID);
  }

  public Long getCertificateUID()
  {
    return (Long)getEventData(CERTIFICATE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ExportCertificateTrustStoreEvent";
  }
}