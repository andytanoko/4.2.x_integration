/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcessDefEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-12     Daniel D'Cotta      Replaced "DOC_PATH" fields with
 *                                    "DOC_IDENTIFIER" fields
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2003-08-20     Andrew Hill         Added USER_TRACKING_IDENTIFIER
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.rnif.IProcessDef;
 
public interface IGTProcessDefEntity extends IGTEntity
{
  // Process Types
  public static final String TYPE_SINGLE_ACTION                   = IProcessDef.TYPE_SINGLE_ACTION;
  public static final String TYPE_TWO_ACTION                      = IProcessDef.TYPE_TWO_ACTION;

  // RNIF Versions
  public static final String RNIF_1_1                             = IProcessDef.RNIF_1_1;
  public static final String RNIF_2_0                             = IProcessDef.RNIF_2_0;

  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  // Digest algorithm Codes
  //public static final String SHA_ALG                              = IProcessDef.SHA_ALG;
  //public static final String MD5_ALG                              = IProcessDef.MD5_ALG;

  // Fields
  public static final Number UID                                  = IProcessDef.UID;
  public static final Number REQUEST_ACT                          = IProcessDef.REQUEST_ACT;
  public static final Number RESPONSE_ACT                         = IProcessDef.RESPONSE_ACT;
  public static final Number DEF_NAME                             = IProcessDef.DEF_NAME;
  public static final Number ACTION_TIME_OUT                      = IProcessDef.ACTION_TIME_OUT;
  public static final Number PROCESS_TYPE                         = IProcessDef.PROCESS_TYPE;
  public static final Number RNIF_VERSION                         = IProcessDef.RNIF_VERSION;
  public static final Number PROCESS_INDICATOR_CODE               = IProcessDef.G_PROCESS_INDICATOR_CODE;
  public static final Number VERSION_IDENTIFIER                   = IProcessDef.VERSION_IDENTIFIER;
  public static final Number USAGE_CODE                           = IProcessDef.G_USAGE_CODE;
  public static final Number IS_SYNCHRONOUS                       = IProcessDef.IS_SYNCHRONOUS;

  public static final Number FROM_PARTNER_CLASS_CODE              = IProcessDef.FROM_PARTNER_CLASS_CODE;
  public static final Number FROM_PARTNER_ROLE_CLASS_CODE         = IProcessDef.FROM_PARTNER_ROLE_CLASS_CODE;
  public static final Number FROM_BIZ_SERVICE_CODE                = IProcessDef.FROM_BIZ_SERVICE_CODE;

  public static final Number TO_PARTNER_CLASS_CODE                = IProcessDef.G_TO_PARTNER_CLASS_CODE;
  public static final Number TO_PARTNER_ROLE_CLASS_CODE           = IProcessDef.G_TO_PARTNER_ROLE_CLASS_CODE;
  public static final Number TO_BIZ_SERVICE_CODE                  = IProcessDef.G_TO_BIZ_SERVICE_CODE;

  public static final Number REQUEST_DOC_THIS_DOC_IDENTIFIER      = IProcessDef.REQUEST_DOC_THIS_DOC_IDENTIFIER;
  public static final Number RESPONSE_DOC_THIS_DOC_IDENTIFIER     = IProcessDef.RESPONSE_DOC_THIS_DOC_IDENTIFIER;
  public static final Number RESPONSE_DOC_REQUEST_DOC_IDENTIFIER  = IProcessDef.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER;

  //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
  //public static final Number DIGEST_ALG_CODE                      = IProcessDef.G_DIGEST_ALG_CODE;
  //public static final Number DISABLE_DTD                          = IProcessDef.DISABLE_DTD;
  //public static final Number DISABLE_SCHEMA                       = IProcessDef.DISABLE_SCHEMA;
  //public static final Number DISABLE_ENCRYPTION                   = IProcessDef.DISABLE_ENCRYPTION;
  //public static final Number DISABLE_SIGNATURE                    = IProcessDef.DISABLE_SIGNATURE;
  //public static final Number VALIDATE_AT_SENDER                   = IProcessDef.VALIDATE_AT_SENDER;
  //public static final Number ENABLE_ENCRYPT_PAYLOAD               = IProcessDef.ENABLE_ENCRYPT_PAYLOAD;

  public static final Number USER_TRACKING_IDENTIFIER             = IProcessDef.USER_TRACKING_IDENTIFIER; //20030821AH
}
