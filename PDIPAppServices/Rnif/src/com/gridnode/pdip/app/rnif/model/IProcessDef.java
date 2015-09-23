/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessDef.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 16 2003    Neo Sok Lay         Add CAN_DELETE fieldid.
 * Aug 20 2003    Guo Jianyu          Added USER_TRACKING_IDENTIFIER
 */
package com.gridnode.pdip.app.rnif.model;

public interface IProcessDef
{
  public static final String ENTITY_NAME = "ProcessDef";
  public static final Number UID = new Integer(0); //Integer
  public static final Number REQUEST_ACT = new Integer(1); // ProcessAct
  public static final Number RESPONSE_ACT = new Integer(2); // ProcessAct
  public static final Number DEF_NAME = new Integer(3); // String
  public static final Number ACTION_TIME_OUT = new Integer(4); // Integer
  public static final Number PROCESS_TYPE = new Integer(5); // String
  public static final Number RNIF_VERSION = new Integer(6); // String
  public static final Number FROM_PARTNER_ROLE_CLASS_CODE = new Integer(7); // String
  public static final Number FROM_BIZ_SERVICE_CODE = new Integer(8); // String
  public static final Number FROM_PARTNER_CLASS_CODE = new Integer(9); // String
  public static final Number G_TO_PARTNER_ROLE_CLASS_CODE = new Integer(10); // String
  public static final Number G_TO_BIZ_SERVICE_CODE = new Integer(11); // String
  public static final Number G_TO_PARTNER_CLASS_CODE = new Integer(12); // String
  public static final Number G_PROCESS_INDICATOR_CODE = new Integer(13); // String
  public static final Number VERSION_IDENTIFIER = new Integer(14); // String
  public static final Number G_USAGE_CODE = new Integer(15); // String

  public static final Number REQUEST_DOC_THIS_DOC_IDENTIFIER = new  Integer(16); // String
  public static final Number RESPONSE_DOC_THIS_DOC_IDENTIFIER = new  Integer(17); // String
  public static final Number RESPONSE_DOC_REQUEST_DOC_IDENTIFIER = new  Integer(18); // String

 /**
   * FieldId for Synchronous. An Boolean.
   */
  public static final Number  IS_SYNCHRONOUS    = new Integer(19); //Boolean

  /**
   * FieldID for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(20);
  public static final Number USER_TRACKING_IDENTIFIER = new Integer(21); //String

  public static final String TYPE_SINGLE_ACTION = "SingleActionProcess";
  public static final String TYPE_TWO_ACTION = "TwoActionProcess";

  public static final String RNIF_1_1 = "RNIF1.1";
  public static final String RNIF_2_0 = "RNIF2.0";

//  public static final String SHA_ALG = "SHA1";
//  public static final String MD5_ALG = "MD5";

}
