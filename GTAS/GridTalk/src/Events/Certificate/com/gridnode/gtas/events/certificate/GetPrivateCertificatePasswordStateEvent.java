package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPrivateCertificatePasswordStateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class GetPrivateCertificatePasswordStateEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1591248007003442279L;
	public static final String PRIVATE_PASSEWORD_STATE = "Password State";
  public GetPrivateCertificatePasswordStateEvent() throws EventException
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPrivateCertificatePasswordStateEvent";
  }
}