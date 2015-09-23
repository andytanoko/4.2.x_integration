package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetPrivateCertificatePasswordEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class SetPrivateCertificatePasswordEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4862662552078359382L;
	public static final String PRIVATE_PASSEWORD = "Password";

  public SetPrivateCertificatePasswordEvent(String password) throws EventException
  {
    checkSetString(PRIVATE_PASSEWORD,password);
  }

  public String getPassword()
  {
    return (String)getEventData(PRIVATE_PASSEWORD);
  }
  public String getEventName()
  {
    return "java:comp/env/param/event/SetPrivateCertificatePasswordEvent";
  }
}