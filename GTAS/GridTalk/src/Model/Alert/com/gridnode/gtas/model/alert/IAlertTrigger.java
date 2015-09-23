/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertTrigger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in AlertTrigger entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IAlertTrigger
{
  /**
   * Name of AlertTrigger entity.
   */
  public static final String  ENTITY_NAME = "AlertTrigger";


  /**
   * FieldId for the UID for a AlertTrigger entity instance. A Number.
   */
  public static final Number UID = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-AlertTrigger-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION = new Integer(2); //Double

  /**
   * FieldId for Level. A Number.
   *
   * @see #LEVEL_0
   * @see #LEVEL_1
   * @see #LEVEL_2
   * @see #LEVEL_3
   * @see #LEVEL_4
   */
  public static final Number LEVEL = new Integer(3);  //Integer

  /**
   * FieldId for Alert. A String.
   */
  public static final Number ALERT_UID = new Integer(4);  //String

  /**
   * FieldId for AlertType. A String.
   */
  public static final Number ALERT_TYPE = new Integer(5);  //String

  /**
   * FieldId for DocType. A String.
   */
  public static final Number DOC_TYPE = new Integer(6);  //String(12)

  /**
   * FieldId for PartnerType. A String.
   */
  public static final Number PARTNER_TYPE = new Integer(7);  //String(3)

  /**
   * FieldId for PartnerGroup. A String.
   */
  public static final Number PARTNER_GROUP = new Integer(8);  //String(3)

  /**
   * FieldId for PartnerId. A String.
   */
  public static final Number PARTNER_ID = new Integer(9);  //String(20)

  /**
   * FieldId for Enabled. A Number.
   */
  public static final Number IS_ENABLED = new Integer(10);  //Boolean

  /**
   * FieldId for AttachDoc. A Boolean.
   */
  public static final Number IS_ATTACH_DOC = new Integer(11);  //Boolean

  /**
   * FieldId for Recipients. List of String
   */
  public static final Number RECIPIENTS    = new Integer(12); //List of String


  //Levels
  /**
   * Level 0 trigger.
   */
  public static final Integer LEVEL_0 = new Integer(0);

  /**
   * Level 1 trigger.
   */
  public static final Integer LEVEL_1 = new Integer(1);

  /**
   * Level 2 trigger.
   */
  public static final Integer LEVEL_2 = new Integer(2);

  /**
   * Level 3 trigger.
   */
  public static final Integer LEVEL_3 = new Integer(3);

  /**
   * Level 4 trigger.
   */
  public static final Integer LEVEL_4 = new Integer(4);

  //Types of Recipients. To be appended with value
  /**
   * Recipient: Email Address<p>
   * Usage: RECPT_EMAIL_ADDRESS + <email_address_val>
   */
  public static final String RECPT_EMAIL_ADDRESS      = "EMAIL_ADDRESS:";

  /**
   * Recipient: Email Code<p>
   * Usage: RECPT_EMAIL_CODE + <email_code_val>
   */
  public static final String RECPT_EMAIL_CODE         = "EMAIL_CODE:";

  /**
   * Recipient: Email Code XPath<p>
   * Usage: RECPT_EMAIL_CODE_XPATH + <email_code_xpath_val>
   */
  public static final String RECPT_EMAIL_CODE_XPATH   = "EMAIL_CODE_XPATH:";

  /**
   * Recipient: Email Address XPath<p>
   * Usage: RECPT_EMAIL_ADDRESS_XPATH + <email_address_xpath_val>
   */
  public static final String RECPT_EMAIL_ADDRESS_XPATH= "EMAIL_ADDRESS_XPATH:";

  /**
   * Recipient: User<p>
   * Usage: RECPT_USER + <user_id>
   */
  public static final String RECPT_USER               = "USER:";

  /**
   * Recipient: Role<p>
   * Usage: RECPT_ROLE + <role_name>
   */
  public static final String RECPT_ROLE               = "ROLE:";

}