/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSecurityInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-07     Andrew Hill         Partner_Cat
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.ISecurityInfo;

public interface IGTSecurityInfoEntity extends IGTProfileEntity
{
  //Constants for ENC_TYPE
  public static final String ENC_TYPE_NONE          = ISecurityInfo.ENCRYPTION_TYPE_NONE;
  public static final String ENC_TYPE_ASYMMETRIC    = ISecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC;
  public static final String ENC_TYPE_SMIME         = ISecurityInfo.ENCRYPTION_TYPE_SMIME;

  // Constants for ENC_LEVEL
  public static final Integer ENC_LEVEL_40          = new Integer("40");//ISecurityInfo.;   // 20031204 DDJ
  public static final Integer ENC_LEVEL_64          = new Integer("64");//ISecurityInfo.;   // 20031204 DDJ
  public static final Integer ENC_LEVEL_128         = new Integer("128");//ISecurityInfo.;  // 20031204 DDJ
  public static final Integer ENC_LEVEL_168         = new Integer("168");//ISecurityInfo.;  // 20031204 DDJ
  public static final Integer ENC_LEVEL_256         = new Integer("256");//ISecurityInfo.;  // 20031204 DDJ
  public static final Integer ENC_LEVEL_512         = new Integer("512");//ISecurityInfo.;  // 20031204 DDJ
  public static final Integer ENC_LEVEL_1024        = new Integer("1024");//ISecurityInfo.; // 20031204 DDJ

  //Constants for SIG_TYPE
  public static final String SIG_TYPE_NONE          = ISecurityInfo.SIGNATURE_TYPE_NONE;
  public static final String SIG_TYPE_SMIME         = ISecurityInfo.SIGNATURE_TYPE_SMIME;

  //Constants for DIGEST_ALGORITHM
  public static final String DIGEST_ALGORITHM_MD5   = ISecurityInfo.DIGEST_ALGORITHM_MD5;
  public static final String DIGEST_ALGORITHM_SHA1  = ISecurityInfo.DIGEST_ALGORITHM_SHA1;
  public static final String DIGEST_ALGORITHM_SHA224  = ISecurityInfo.DIGEST_ALGORITHM_SHA224;
  public static final String DIGEST_ALGORITHM_SHA256  = ISecurityInfo.DIGEST_ALGORITHM_SHA256;
  public static final String DIGEST_ALGORITHM_SHA384  = ISecurityInfo.DIGEST_ALGORITHM_SHA384;
  public static final String DIGEST_ALGORITHM_SHA512  = ISecurityInfo.DIGEST_ALGORITHM_SHA512;
  

  // Constants for SEQUENCE
  public static final String SEQUENCE_S_C           = "S;C";//ISecurityInfo.;                       // 20031126 DDJ
  public static final String SEQUENCE_C_S           = "C;S";//ISecurityInfo.;                       // 20031126 DDJ
  public static final String SEQUENCE_S_E           = "S;E";//ISecurityInfo.;                       // 20031126 DDJ
  public static final String SEQUENCE_E_S           = "E;S";//ISecurityInfo.;                       // 20031126 DDJ
  public static final String SEQUENCE_C_E           = "C;E";//ISecurityInfo.;                       // 20031126 DDJ
  public static final String SEQUENCE_S_C_E         = "S;C;E";//ISecurityInfo.;                     // 20031126 DDJ
  public static final String SEQUENCE_C_S_E         = "C;S;E";//ISecurityInfo.;                     // 20031126 DDJ
  public static final String SEQUENCE_C_E_S         = "C;E;S";//ISecurityInfo.;                     // 20031126 DDJ

  // Constants for COMPRESSION_TYPE
  public static final String COMPRESSION_TYPE_NONE  = ISecurityInfo.COMPRESSION_TYPE_NONE;   // 20031126 DDJ
  public static final String COMPRESSION_TYPE_SMIME = ISecurityInfo.COMPRESSION_TYPE_SMIME;  // 20031126 DDJ

  // Constants for COMPRESSION_METHOD
  public static final String COMPRESSION_METHOD_ZLIB= ISecurityInfo.COMPRESSION_METHOD_ZLIB; // 20031126 DDJ

  //Field ids
  public static final Number UID                    = ISecurityInfo.UID;
  public static final Number NAME                   = ISecurityInfo.NAME;
  public static final Number DESCRIPTION            = ISecurityInfo.DESCRIPTION;
  public static final Number ENC_TYPE               = ISecurityInfo.ENCRYPTION_TYPE;
  public static final Number ENC_LEVEL              = ISecurityInfo.ENCRYPTION_LEVEL;
  public static final Number ENC_CERT               = ISecurityInfo.ENCRYPTION_CERTIFICATE_ID;
  public static final Number SIG_TYPE               = ISecurityInfo.SIGNATURE_TYPE;
  public static final Number DIGEST_ALGORITHM       = ISecurityInfo.DIGEST_ALGORITHM;
  public static final Number SIG_ENC_CERT           = ISecurityInfo.SIGNATURE_ENCRYPTION_CERTIFICATE_ID;
  public static final Number IS_DISABLED            = ISecurityInfo.IS_DISABLE;
  public static final Number IS_PARTNER             = ISecurityInfo.IS_PARTNER;
  public static final Number PARTNER_CAT            = ISecurityInfo.PARTNER_CAT;
  public static final Number REF_ID                 = ISecurityInfo.REF_ID;
  public static final Number ENCRYPTION_ALGORITHM   = ISecurityInfo.ENCRYPTION_ALGORITHM; // 20031126 DDJ
  public static final Number SEQUENCE               = ISecurityInfo.SEQUENCE;             // 20031126 DDJ
  public static final Number COMPRESSION_TYPE       = ISecurityInfo.COMPRESSION_TYPE;     // 20031126 DDJ
  public static final Number COMPRESSION_METHOD     = ISecurityInfo.COMPRESSION_METHOD;   // 20031126 DDJ
  public static final Number COMPRESSION_LEVEL      = ISecurityInfo.COMPRESSION_LEVEL;    // 20031126 DDJ
}