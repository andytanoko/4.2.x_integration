/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICertificate
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 13-Sept-2002    Jagadeesh          Created.
 *
 * 27-JAN-2003     Jagadeesh           Added: Key for Exported Certificate.

 * 15-April-2003   Qingsong            Added: Fields for Keystore, Truststore.
 * 26-March-2004   Guo Jianyu          Added field: RELATED_CERT_UID
 * 26-July-2006    Tam Wei Xiang       Added field: START_DATE, END_DATE, IS_CA
 * 28-Aug-2006     Tam Wei Xiang       Added field: REPLACEMENT_CERT_UID
 * 01-Aug-2008		Wong Yee Wah		#38  Added field: CERTIFICATE_SWAPPING
 */


package com.gridnode.gtas.model.certificate;


public interface ICertificate
{
  /**
   * Name for UserAccount entity.
   */
  public static final String ENTITY_NAME = "Certificate";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID        = new Integer(0);  //integer

  /**
   * FieldId for unique Id. A Number.
   */
  public static final Number ID         = new Integer(1);  //string(15)

  /**
   * FieldId for Name. A String.
   *
  */
  public static final Number NAME       = new Integer(2);  //string(50)

  /**
   * FieldId for SerialNumber. A String.
   */
  public static final Number SERIALNUM   = new Integer(3);  //string(12)

  /**
   * FieldId for IssuerName. A String.
   */
  public static final Number ISSUERNAME      = new Integer(4);  //string(16)

  /**
   * FieldId for CERTIFICATE. A String.
   */
  public static final Number CERTIFICATE      = new Integer(5);  //string(50)

  /**
   * FieldId for PUBLICKEY. A String.
   */
  public static final Number PUBLICKEY   = new Integer(6); //string(120)

  /**
   * FieldId for PRIVATEKEY. A String.
   */
  public static final Number PRIVATEKEY = new Integer(7); //string(120)

  /**
   * FieldId for Version. A Integer.
   */
  public static final Number REVOKEID       = new Integer(8); //Double


  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_MASTER       = new Integer(9); //Boolean


  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_PARTNER       = new Integer(10); //Boolean


  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number CATEGORY       = new Integer(11); //String


    /**
   * FieldId for isKS. A Booelan.
   */
  public static final Number IS_IN_KS          =   new Integer(12); //Boolean

  public static final Number IS_IN_TS       =   new Integer(13); //Boolean

  public static final Number RELATED_CERT_UID = new Integer(14); //Integer

  public static final Number START_DATE = new Integer(15); //DATE
  
  public static final Number END_DATE = new Integer(16); //DATE
  
  public static final Number IS_CA = new Integer(17); //Boolean
  
  public static final Number REPLACEMENT_CERT_UID = new Integer(18); //Integer
  
  public static final String IMPORT_STATUS_SAVE = "Import Status Save";

  public static final String IMPORT_STATUS_OK = "Import Status OK";

  public static final String EXPORT_CERTIFICATE_FILENAME = "Exported Certificate FileName";
  
  //26072006 TWX to act as a index while we insert into the map in constructing the response.
  //    see ViewCertificateAction
  public static final String SERIAL_NUM_FIELD = "SerialNum";
  
  public static final String START_DATE_FIELD = "StartDate";
  
  public static final String END_DATE_FIELD = "EndDate";
  
  public static final Number CERTIFICATE_SWAPPING = new Integer(19); //CertificateSwapping
 }