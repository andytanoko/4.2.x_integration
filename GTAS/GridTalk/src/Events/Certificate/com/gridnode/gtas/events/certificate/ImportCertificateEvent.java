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
 * Mar 26 2004    Guo Jianyu             Added relatedCertUid
 * 28 Jul 2006    Tam Wei Xiang          Added isCA
 */


package com.gridnode.gtas.events.certificate;


import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;


public class ImportCertificateEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6014401605895377527L;

	/**
   * FieldId for CertName.
   */
  public static final String CERT_NAME = "Cert Name";

  /**
   * FieldId for Partner Id.
   */
  public static final String PARTNERID = "Partner Id";

  /**
   * FieldId for Cert File.
   */
  public static final String CERT_FILE = "Cert File";

  /**
   * FieldId for Password of P12 file.
   */
  public static final String PASSWORD = "Password";

  /**
   * FieldId for Status.
   */

  public static final String STATUS = "Status";

  public static final String RELATED_CERT_UID = "Related Cert UID";
  
  public static final String IS_CA = "Is CA";

/*  public ImportCertificateEvent( String  certName,
                                 Integer partnerId,
                                 String  certFile
                                ) throws EventException
  {
    checkSetString(CERT_NAME,certName);
    checkSetInteger(PARTNERID,partnerId);
    checkSetString(CERT_FILE,certFile);

  }

  public ImportCertificateEvent( String  certName,
                                 Integer partnerId,
                                 String  certFile,
                                 String  password
                               ) throws EventException

  {
    checkSetString(CERT_NAME,certName);
    checkSetInteger(PARTNERID,partnerId);
    checkSetString(CERT_FILE,certFile);
    checkSetString(PASSWORD,password);
  }
*/


  public ImportCertificateEvent( String  certName,
                                 String  certFile,
                                 Long relatedCertUid,
                                 Boolean isCA
                               ) throws EventException

  {
    checkSetString(CERT_NAME,certName);
    checkSetString(CERT_FILE,certFile);
    if (relatedCertUid != null)
      checkSetLong(RELATED_CERT_UID, relatedCertUid);
    
    setEventData(IS_CA, isCA);
  }


  public ImportCertificateEvent( String  certName,
                                 String  certFile,
                                 String  password,
                                 Long relatedCertUid
                               ) throws EventException

  {
    checkSetString(CERT_NAME,certName);
    checkSetString(CERT_FILE,certFile);
    checkSetString(PASSWORD,password);
    if (relatedCertUid != null)
      checkSetLong(RELATED_CERT_UID, relatedCertUid);
    
    setEventData(IS_CA, new Boolean(false));
  }


  public String getCertName()
  {
    return (String)getEventData(CERT_NAME);
  }

  public Integer getPartnerId()
  {
    return (Integer)getEventData(PARTNERID);
  }

  public String getCertFile()
  {
    return (String)getEventData(CERT_FILE);
  }

  public String getPassword()
  {
    return (String)getEventData(PASSWORD);
  }

  public Long getRelatedCertUid()
  {
    return (Long)getEventData(RELATED_CERT_UID);
  }
  
  public Boolean isCA()
  {
  	return (Boolean)getEventData(IS_CA);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/ImportCertificateEvent";
  }

}


