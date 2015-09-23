package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChangePrivateCertificatePasswordEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class ChangePrivateCertificatePasswordEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1458501346468930390L;
	public static final String PRIVATE_PASSEWORD_OLD = "Old Password";
   public static final String PRIVATE_PASSEWORD_NEW = "New Password";

  public ChangePrivateCertificatePasswordEvent(String oldPassword,String newPassword) throws EventException
  {
    checkSetString(PRIVATE_PASSEWORD_OLD,oldPassword);
    checkSetString(PRIVATE_PASSEWORD_NEW,newPassword);
  }

  public String getOldPassword()
  {
    return (String)getEventData(PRIVATE_PASSEWORD_OLD);
  }

  public String getNewPassword()
  {
    return (String)getEventData(PRIVATE_PASSEWORD_NEW);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ChangePrivateCertificatePasswordEvent";
  }
}