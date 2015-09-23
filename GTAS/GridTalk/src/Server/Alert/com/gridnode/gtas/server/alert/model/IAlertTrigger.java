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
 * Apr 17 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.alert.model;

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


  public static final Number[] LEVEL_4_NULL_FIELDS = { PARTNER_TYPE,
                                                       PARTNER_GROUP,
                                                     };
  public static final Number[] LEVEL_4_COND_FIELDS = { PARTNER_ID,
                                                       DOC_TYPE,
                                                       ALERT_TYPE,
                                                     };

  public static final Number[] LEVEL_3_NULL_FIELDS = { PARTNER_ID,
                                                     };
  public static final Number[] LEVEL_3_COND_FIELDS = { PARTNER_GROUP,
                                                       PARTNER_TYPE,
                                                       DOC_TYPE,
                                                       ALERT_TYPE,
                                                     };

  public static final Number[] LEVEL_2_NULL_FIELDS = { PARTNER_GROUP,
                                                       PARTNER_ID,
                                                     };
  public static final Number[] LEVEL_2_COND_FIELDS = { PARTNER_TYPE,
                                                       DOC_TYPE,
                                                       ALERT_TYPE,
                                                     };

  public static final Number[] LEVEL_1_NULL_FIELDS = { PARTNER_TYPE,
                                                       PARTNER_GROUP,
                                                       PARTNER_ID,
                                                     };
  public static final Number[] LEVEL_1_COND_FIELDS = { DOC_TYPE,
                                                       ALERT_TYPE,
                                                     };

  public static final Number[] LEVEL_0_NULL_FIELDS = { PARTNER_TYPE,
                                                       PARTNER_GROUP,
                                                       PARTNER_ID,
                                                       DOC_TYPE,
                                                     };
  public static final Number[] LEVEL_0_COND_FIELDS = { ALERT_TYPE,
                                                     };

}