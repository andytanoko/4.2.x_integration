/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistrationInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 * Apr 11 2003    Koh Han Sing        Add in license state
 */
package com.gridnode.gtas.model.registration;

/**
 * This interface defines the field IDs and constants for the RegistrationInfo
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IRegistrationInfo
{
  /**
   * Entity name for RegistrationInfo entity.
   */
  static final String ENTITY_NAME     = "RegistrationInfo";

  /**
   * FieldID for GridNodeID. An Integer.
   */
  static final Number GRIDNODE_ID     = new Integer(0);

  /**
   * FieldID for GridNode Name. A String.
   */
  static final Number GRIDNODE_NAME   = new Integer(1);

  /**
   * FieldID for GridNode Category (Code). A String.
   */
  static final Number CATEGORY        = new Integer(2); // String(3)

  /**
   * FieldID for Business Connections. A Integer.
   */
  static final Number BIZ_CONNECTIONS = new Integer(3);

  /**
   * FieldID for Company Profile. A CompanyProfile.
   */
  static final Number COMPANY_PROFILE = new Integer(4);

  /**
   * FieldID for Product Key Field 1. A String.
   */
  static final Number PRODUCT_KEY_F1  = new Integer(5); // String(5)

  /**
   * FieldID for Product Key Field 2. A String.
   */
  static final Number PRODUCT_KEY_F2  = new Integer(6); // String(6)

  /**
   * FieldID for Product Key Field 3. A String.
   */
  static final Number PRODUCT_KEY_F3  = new Integer(7); // String(5)

  /**
   * FieldID for Product Key Field 4. A String.
   */
  static final Number PRODUCT_KEY_F4  = new Integer(8); // String(6)

  /**
   * FieldID for License Start Date. A Date.
   */
  static final Number LIC_START_DATE  = new Integer(9);

  /**
   * FieldID for License End Date. A Date.
   */
  static final Number LIC_END_DATE    = new Integer(10);

  /**
   * FieldID for RegistrationState. A Short
   */
  static final Number REGISTRATION_STATE = new Integer(11);

  /**
   * FieldID for LicenseState. A Short
   */
  static final Number LICENSE_FILE = new Integer(12);

  /**
   * FieldID for OsName. A String
   */
  static final Number OS_NAME = new Integer(13);

  /**
   * FieldID for OsVersion. A String
   */
  static final Number OS_VERSION = new Integer(14);

  /**
   * FieldID for MachineName. A String
   */
  static final Number MACHINE_NAME = new Integer(15);

  /**
   * FieldID for LicenseState. A Short
   */
  static final Number LICENSE_STATE = new Integer(16);


  // values for REGISTRATION_STATE
  /**
   * Possible value for REGISTRATION_STATE.
   * This indicates that registration has not been done or undetermined.
   */
  static final short STATE_NOT_REGISTERED = 0;

  /**
   * Possible value for REGISTRATION_STATE.
   * This indicates that registration has been completed successfully.
   */
  static final short STATE_REGISTERED     = 1;

  /**
   * Possible value for REGISTRATION_STATE.
   * This indicates that registration is currently in progress.
   */
  static final short STATE_REG_IN_PROGRESS= 2;

  /**
   * Possible value for REGISTRATION_STATE.
   * This indicates that the license has expired.
   */
  static final short STATE_EXPIRED= 3;


  // Values for LICENSE_STATE
  /**
   * Possible Value for STATE field. This indicates that the License is
   * currently valid.
   */
  static final short STATE_LICENSE_VALID         = 0;

  /**
   * Possible value for STATE field. This indicates that the License will not
   * be valid until the START_DATE.
   */
  static final short STATE_LICENSE_NOT_COMMENCED = 1;

  /**
   * Possible value for STATE field. This indicates that the License has expired.
   */
  static final short STATE_LICENSE_EXPIRED       = 2;

  /**
   * Possible value for STATE field. This indicates that the License is revoked
   * when it has not expired.
   */
  static final short STATE_LICENSE_REVOKED       = 3;

}