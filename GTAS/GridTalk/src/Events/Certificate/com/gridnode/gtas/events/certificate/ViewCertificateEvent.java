/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 03 2003    Jagadeesh              Created
 * Jan 08 2004    Neo Sok Lay             Use UID for viewing existing certs.
 */

package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class ViewCertificateEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7239742131577403611L;
	public static final String CERT_NAME = "Cert Name";
  public static final String IS_FILE = "Is File";
  public static final String  PASSWORD = "Password";
  public static final String UID = "UID";

  public ViewCertificateEvent(String certName,Boolean isFile,String password) throws EventException
  {
    checkSetString(CERT_NAME,certName);
    checkSetObject(IS_FILE,isFile,Boolean.class);
    setEventData(PASSWORD,password);
  }

  public ViewCertificateEvent(Long uid) throws EventException
  {
    checkSetLong(UID, uid);
    //checkSetString(CERT_NAME,certName);
//    checkSetString(PASSWORD,password);
  }

  public String getName()
  {
    return (String)getEventData(CERT_NAME);
  }

  public String getPassword()
  {
    return (String)getEventData(PASSWORD);
  }

  public Boolean isFile()
  {
    return (Boolean)getEventData(IS_FILE);
  }

  public Long getUId()
  {
    return (Long)getEventData(UID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/ViewCertificateEvent";
  }

}