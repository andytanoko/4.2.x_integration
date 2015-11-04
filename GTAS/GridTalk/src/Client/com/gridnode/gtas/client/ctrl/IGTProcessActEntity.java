/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcessActEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2007-11-07     Tam Wei Xiang       Add new field "is_compress_required"
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.rnif.IProcessAct;

public interface IGTProcessActEntity extends IGTEntity
{
  // Process Definition Action type
  public static final Integer REQUEST_ACT                 = IProcessAct.REQUEST_ACT;
  public static final Integer RESPONSE_ACT                = IProcessAct.RESPONSE_ACT;

  // Digest algorithm code
  public static final String SHA_ALG                      = IProcessAct.SHA_ALG;
  public static final String MD5_ALG                      = IProcessAct.MD5_ALG;
  //added by Nazir on 09282015
  public static final String SHA224_ALG 				  = IProcessAct.SHA224_ALG;
  public static final String SHA256_ALG   				  = IProcessAct.SHA256_ALG;
  public static final String SHA384_ALG					  = IProcessAct.SHA384_ALG;
  public static final String SHA512_ALG 				  = IProcessAct.SHA512_ALG;

  // Encryption algorithm
  public static final String RC2_ALG                      = IProcessAct.RC2_ALG;
  public static final String RC5_ALG                      = IProcessAct.RC5_ALG;
  public static final String TDES_ALG                     = IProcessAct.TDES_ALG;

  // Fields
  public static final Number UID                          = IProcessAct.UID;
  public static final Number PROCESS_DEF_UID              = IProcessAct.PROCESS_DEF_UID;
  public static final Number PROCESS_DEF_ACT              = IProcessAct.PROCESS_DEF_ACT;

  public static final Number MSG_TYPE                     = IProcessAct.MSG_TYPE;
  public static final Number DICT_FILE                    = IProcessAct.DICT_FILE;
  public static final Number XML_SCHEMA                   = IProcessAct.XML_SCHEMA;

  public static final Number RETRIES                      = IProcessAct.RETRIES;
  public static final Number TIME_TO_ACKNOWLEDGE          = IProcessAct.TIME_TO_ACKNOWLEDGE;
  public static final Number BIZ_ACTIVITY_IDENTIFIER      = IProcessAct.BIZ_ACTIVITY_IDENTIFIER;
  public static final Number BIZ_ACTION_CODE              = IProcessAct.G_BIZ_ACTION_CODE;

  public static final Number IS_AUTHORIZATION_REQUIRED    = IProcessAct.IS_AUTHORIZATION_REQUIRED;
  public static final Number IS_NON_REPUDIATION_REQUIRED  = IProcessAct.IS_NON_REPUDIATION_REQUIRED;
  public static final Number IS_SECURE_TRANSPORT_REQUIRED = IProcessAct.IS_SECURE_TRANSPORT_REQUIRED;
  public static final Number DISABLE_DTD                  = IProcessAct.DISABLE_DTD;
  public static final Number DISABLE_SCHEMA               = IProcessAct.DISABLE_SCHEMA;
  public static final Number VALIDATE_AT_SENDER           = IProcessAct.VALIDATE_AT_SENDER;

  public static final Number DISABLE_ENCRYPTION           = IProcessAct.DISABLE_ENCRYPTION;
  public static final Number DISABLE_SIGNATURE            = IProcessAct.DISABLE_SIGNATURE;
  public static final Number ONLY_ENCRYPT_PAYLOAD         = IProcessAct.ONLY_ENCRYPT_PAYLOAD;
  public static final Number DIGEST_ALGORITHM             = IProcessAct.DIGEST_ALGORITHM;
  public static final Number ENCRYPTION_ALGORITHM         = IProcessAct.ENCRYPTION_ALGORITHM;
  public static final Number ENCRYPTION_ALGORITHM_LENGTH  = IProcessAct.ENCRYPTION_ALGORITHM_LENGTH;
  
  public static final Number IS_COMPRESS_REQUIRED         = IProcessAct.IS_COMPRESS_REQUIRED;
}