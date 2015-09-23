/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 03 2003    Jagadeesh               Created
 * Jan 08 2004    Neo Sok Lay             Use UID instead of Name to identify 
 *                                        the certificate to export.
 */

package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class ExportCertificateEvent extends EventSupport
{

  /**
   * FieldId for CertName.
   */
  //public static final String CERT_NAME = "Cert Name";

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7836487134377353633L;

	/**
    * FieldId for Cert File.
    */
  public static final String CERT_FILE = "Cert File";

  public static final String CERT_UID = "Cert UID";

  //public ExportCertificateEvent(String certName,String certFile) throws EventException
  public ExportCertificateEvent(Long certUid, String certFile)
    throws EventException
  {
    //checkSetString(CERT_NAME,certName);
    checkSetLong(CERT_UID, certUid);
    checkSetString(CERT_FILE, certFile);
  }
  /*
  public String getCertName()
  {
    return (String)getEventData(CERT_NAME);
  }
  */
  public Long getCertUid()
  {
    return (Long) getEventData(CERT_UID);
  }

  public String getCertFile()
  {
    return (String) getEventData(CERT_FILE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ExportCertificateEvent";
  }
}
