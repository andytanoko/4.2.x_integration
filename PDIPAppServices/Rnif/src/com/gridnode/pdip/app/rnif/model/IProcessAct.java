/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessAct.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Unknown					Unknown									Created
 * 19 Jan 06				SC											comment G_DIGEST_ALG_CODE.
 */
package com.gridnode.pdip.app.rnif.model;

public interface IProcessAct
{
  public static final String ENTITY_NAME = "ProcessAct";
  public static final Number UID = new Integer(0); //Long 
  public static final Number PROCESS_DEF_UID = new Integer(1); // Long 
  public static final Number PROCESS_DEF_ACT = new Integer(2); // Integer 
  public static final Number MSG_TYPE = new Integer(3); // String 
  public static final Number RETRIES = new Integer(4); // Integer 
  public static final Number TIME_TO_ACKNOWLEDGE = new Integer(5); // Integer 
  public static final Number IS_AUTHORIZATION_REQUIRED = new Integer(6); // Boolean 
  public static final Number IS_NON_REPUDIATION_REQUIRED = new Integer(7); // Boolean 
  public static final Number IS_SECURE_TRANSPORT_REQUIRED = new Integer(8); // Boolean 
  public static final Number BIZ_ACTIVITY_IDENTIFIER = new Integer(9); // String 
  public static final Number G_BIZ_ACTION_CODE = new Integer(10); // String 
  public static final Number DICT_FILE = new Integer(11); // String 
  public static final Number XML_SCHEMA = new Integer(12); // String 
//  public static final Number USER_PROCEDURE_U_ID = new Integer(13); // Integer 


  public static final Number DISABLE_DTD = new Integer(14); // Boolean 
  public static final Number DISABLE_SCHEMA = new Integer(15); // Boolean
  
  /* 19 Jan 06 [SC] use DIGEST_ALGORITHM instead */
//  public static final Number G_DIGEST_ALG_CODE = new Integer(16); // String
  
  public static final Number VALIDATE_AT_SENDER = new Integer(17); // Boolean 

  public static final Number DISABLE_ENCRYPTION = new Integer(18); // Boolean 
  public static final Number DISABLE_SIGNATURE = new Integer(19); // Boolean 
  public static final Number ONLY_ENCRYPT_PAYLOAD = new Integer(20); // Boolean 

  /**
   * FieldId for DIGEST_ALGORITHM. A String.
   */
  public static final Number DIGEST_ALGORITHM = new Integer(21);


  /**
   * FieldId for ENCRYPTION_ALGORITHM. A String.
   */
  public static final Number ENCRYPTION_ALGORITHM = new Integer(22);

  /**
   * FieldId for ENCRYPTION_ALGORITHM_LENGTH. An Integer.
   */
  public static final Number ENCRYPTION_ALGORITHM_LENGTH = new Integer(23);
  
  /**
   * FieldID for IS_COMPRESS_REQUIRED. A Boolean
   */
  public static final Number IS_COMPRESS_REQUIRED = new Integer(24);

  public static final Integer REQUEST_ACT = new Integer(1);
  public static final Integer RESPONSE_ACT = new Integer(2);
  
  public static final String SHA_ALG ="1.3.14.3.2.26";
  public static final String MD5_ALG = "1.2.840.113549.2.5";

  public static final String RC5_ALG ="RC5-0x10-32-16/CBC/PKCS5Padding";
  public static final String TDES_ALG ="1.2.840.113549.3.7";
  public static final String RC2_ALG = "1.2.840.113549.3.2";
  

}
