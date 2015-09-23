/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRegistrationInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-26     Andrew Hill         Created
 * 2003-04-16     Andrew Hill         Nodelock changes
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.registration.IRegistrationInfo;

public interface IGTRegistrationInfoEntity extends IGTEntity
{
  // REG_STATE Constants
  public static final Short REG_STATE_NOT_REG     = new Short(IRegistrationInfo.STATE_NOT_REGISTERED);
  public static final Short REG_STATE_REG         = new Short(IRegistrationInfo.STATE_REGISTERED);
  public static final Short REG_STATE_IN_PROGRESS = new Short(IRegistrationInfo.STATE_REG_IN_PROGRESS);
  public static final Short REG_STATE_EXPIRED     = new Short(IRegistrationInfo.STATE_EXPIRED); //20030416AH

  // LICENSE_STATE Constants
  /*
  public static final Short LICENSE_STATE_VALID       = new Short(IRegistrationInfo.STATE_LICENSE_VALID); //20030416AH
  public static final Short LICENSE_STATE_UNCOMMENCED = new Short(IRegistrationInfo.STATE_LICENSE_NOT_COMMENCED); //20030416AH
  public static final Short LICENSE_STATE_EXPIRED     = new Short(IRegistrationInfo.STATE_LICENSE_EXPIRED); //20030416AH
  public static final Short LICENSE_STATE_REVOKED     = new Short(IRegistrationInfo.STATE_LICENSE_REVOKED); //20030416AH
  */

  //Field ids
  public static final Number GRIDNODE_ID          = IRegistrationInfo.GRIDNODE_ID;
  public static final Number GRIDNODE_NAME        = IRegistrationInfo.GRIDNODE_NAME;
  public static final Number CATEGORY             = IRegistrationInfo.CATEGORY;
  public static final Number BIZ_CONNECTIONS      = IRegistrationInfo.BIZ_CONNECTIONS;
  public static final Number COMPANY_PROFILE      = IRegistrationInfo.COMPANY_PROFILE;
  public static final Number PRODUCT_KEY_F1       = IRegistrationInfo.PRODUCT_KEY_F1;
  public static final Number PRODUCT_KEY_F2       = IRegistrationInfo.PRODUCT_KEY_F2;
  public static final Number PRODUCT_KEY_F3       = IRegistrationInfo.PRODUCT_KEY_F3;
  public static final Number PRODUCT_KEY_F4       = IRegistrationInfo.PRODUCT_KEY_F4;
  public static final Number LIC_START_DATE       = IRegistrationInfo.LIC_START_DATE;
  public static final Number LIC_END_DATE         = IRegistrationInfo.LIC_END_DATE;
  public static final Number REG_STATE            = IRegistrationInfo.REGISTRATION_STATE;
  public static final Number LICENSE_FILE         = IRegistrationInfo.LICENSE_FILE; //20030416AH
  public static final Number OS_NAME              = IRegistrationInfo.OS_NAME; //20030416AH
  public static final Number OS_VERSION           = IRegistrationInfo.OS_VERSION; //20030416AH
  public static final Number MACHINE_NAME         = IRegistrationInfo.MACHINE_NAME; //20030416HA
  /*public static final Number LICENSE_STATE        = IRegistrationInfo.LICENSE_STATE;*/ //20030416AH

  //vfield ids
  public static final Number SECURITY_PASSWORD    = new Integer(-10);
  public static final Number CONFIRM_PASSWORD     = new Integer(-20);

  //constant
  public static final int PROD_KEY_SIZE = 22; //length of product key in chars - for validation
}