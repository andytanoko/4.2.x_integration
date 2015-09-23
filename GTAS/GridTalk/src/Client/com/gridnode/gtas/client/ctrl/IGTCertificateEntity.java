/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCertificateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-13     Andrew Hill         Created
 * 2003-01-17     Andrew Hill         Import/Export support
 * 2003-01-22     Andrew Hill         Seperate field for x500Names for issuer & subject
 * 2003-01-28     Andrew Hill         Renamed IMPORT_FILE to CERT_FILE
 * 2003-04-15     Andrew Hill         IS_IN_KS & IS_IN_TS, exportKeyStore(), exportTrustStore()
 * 2004-03-26     Daniel D'Cotta      Added RELATED_CERT_UID
 * 2006-07-26     Tam Wei Xiang       Added START_DATE, END_DATE
 * 2006-07-28     Tam Wei Xiang       Added isCA
 * 2006-08-28     Tam Wei Xiang       Added REPLACEMENT_CERT_UID
 * 2008-08-01	  Wong Yee Wah		 #38   Added CERTIFICATE_SWAPPING		
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.certificate.ICertificate;

import com.gridnode.gtas.client.GTClientException;

public interface IGTCertificateEntity extends IGTEntity
{
  public static final Number UID              = ICertificate.UID;
  public static final Number ID               = ICertificate.ID;
  public static final Number NAME             = ICertificate.NAME;
  public static final Number SERIAL_NUM       = ICertificate.SERIALNUM;
  public static final Number ISSUER_NAME      = ICertificate.ISSUERNAME;
  public static final Number CERTIFICATE      = ICertificate.CERTIFICATE;
  public static final Number PUBLIC_KEY       = ICertificate.PUBLICKEY;
  public static final Number PRIVATE_KEY      = ICertificate.PRIVATEKEY;
  public static final Number REVOKE_ID        = ICertificate.REVOKEID;
  public static final Number IS_MASTER        = ICertificate.IS_MASTER;
  public static final Number IS_PARTNER       = ICertificate.IS_PARTNER;
  public static final Number IS_IN_KS         = ICertificate.IS_IN_KS; //20030415AH
  public static final Number IS_IN_TS         = ICertificate.IS_IN_TS; //20030415AH
  public static final Number RELATED_CERT_UID = ICertificate.RELATED_CERT_UID; // 20040323 DDJ
  public static final Number START_DATE       = ICertificate.START_DATE; //20060726 TWX
  public static final Number END_DATE         = ICertificate.END_DATE;   //20060726 TWX
  public static final Number IS_CA            = ICertificate.IS_CA; //20060728 TWX
  public static final Number REPLACEMENT_CERT_UID = ICertificate.REPLACEMENT_CERT_UID; //20060828 TWX
  public static final Number CERTIFICATE_SWAPPING = ICertificate.CERTIFICATE_SWAPPING; //20080628 WYW
  
  //vfields
  public static final Number CERT_FILE        = new Integer(-10); //20030128AH
  public static final Number PASSWORD         = new Integer(-20); //20030117AH
  public static final Number ISSUER_DETAILS   = new Integer(-30); //20030122AH
  public static final Number SUBJECT_DETAILS  = new Integer(-40); //20030122AH
  
  public void exportKeyStore() throws GTClientException; //20030415AH
  public void exportTrustStore() throws GTClientException; //20030415AH
}