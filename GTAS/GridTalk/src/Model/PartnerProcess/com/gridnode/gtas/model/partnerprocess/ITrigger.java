/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITrigger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Dec 12 2002    Daniel D'Cotta      Added triggerType and isRequest
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Oct 20 2003    Guo Jianyu          Add NumOfRetries, RetryInterval, ChannelUID
 */
package com.gridnode.gtas.model.partnerprocess;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Trigger entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public interface ITrigger
{
  /**
   * Name of Trigger entity.
   */
  public static final String  ENTITY_NAME = "Trigger";

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

  //Trigger Types
  /**
   * Trigger on import.
   */
  public static final Integer TRIGGER_IMPORT = new Integer(0);

  /**
   * Trigger on receive.
   */
  public static final Integer TRIGGER_RECEIVE = new Integer(1);

  /**
   * Trigger on manual send.
   */
  public static final Integer TRIGGER_MANUAL_SEND = new Integer(2);

  /**
   * Trigger on manual export.
   */
  public static final Integer TRIGGER_MANUAL_EXPORT = new Integer(3);

  /**
   * FieldId for the UID for a Trigger entity instance. A Number.
   */
  public static final Number UID = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-Trigger-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1); //Boolean

  /**
   * FieldId for Level. A Number.
   *
   * @see #LEVEL_0
   * @see #LEVEL_1
   * @see #LEVEL_2
   * @see #LEVEL_3
   * @see #LEVEL_4
   */
  public static final Number TRIGGER_LEVEL = new Integer(3);  //Integer

  /**
   * FieldId for PartnerFunctionId. A String.
   */
  public static final Number PARTNER_FUNCTION_ID = new Integer(4);  //String(4)

  /**
   * FieldId for ProcessId. A String.
   */
  public static final Number PROCESS_ID = new Integer(5);  //String(80)

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
   * FieldId for TriggerType. A Number.
   */
  public static final Number TRIGGER_TYPE = new Integer(10);  //Integer

  /**
   * FieldId for IsRequest. A Boolean.
   */
  public static final Number IS_REQUEST = new Integer(11);  //Boolean

  /**
   * FieldId for IsLocalPending. A Boolean.
   */
  public static final Number IS_LOCAL_PENDING = new Integer(12);  //Boolean

  /**
   * FieldId for NumOfRetries. A Number.
   */
  public static final Number NUM_OF_RETRIES = new Integer(13); //Integer

  /**
   * FieldId for RetryInterval. A Number.
   */
  public static final Number RETRY_INTERVAL = new Integer(14); //Integer

  /**
   * FieldId for ChannelUID. A Long.
   */
  public static final Number CHANNEL_UID = new Integer(15); //Integer

}