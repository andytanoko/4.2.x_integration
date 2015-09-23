/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTTriggerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2003-08-08     Andrew Hill         isLocalPending field
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partnerprocess.ITrigger;

public interface IGTTriggerEntity extends IGTEntity
{
  //Trigger types
  public static final Integer TRIGGER_IMPORT        = ITrigger.TRIGGER_IMPORT;
  public static final Integer TRIGGER_RECEIVE       = ITrigger.TRIGGER_RECEIVE;
  public static final Integer TRIGGER_MANUAL_SEND   = ITrigger.TRIGGER_MANUAL_SEND;
  public static final Integer TRIGGER_MANUAL_EXPORT = ITrigger.TRIGGER_MANUAL_EXPORT;

  //Trigger levels
  public static final Integer TRIGGER_LEVEL_0       = ITrigger.LEVEL_0;
  public static final Integer TRIGGER_LEVEL_1       = ITrigger.LEVEL_1;
  public static final Integer TRIGGER_LEVEL_2       = ITrigger.LEVEL_2;
  public static final Integer TRIGGER_LEVEL_3       = ITrigger.LEVEL_3;
  public static final Integer TRIGGER_LEVEL_4       = ITrigger.LEVEL_4;

  //Fields
  public static final Number UID                    = ITrigger.UID;
  public static final Number TRIGGER_TYPE           = ITrigger.TRIGGER_TYPE;
  public static final Number TRIGGER_LEVEL          = ITrigger.TRIGGER_LEVEL;
  public static final Number PARTNER_FUNCTION_ID    = ITrigger.PARTNER_FUNCTION_ID;
  public static final Number DOC_TYPE               = ITrigger.DOC_TYPE;
  public static final Number PARTNER_TYPE           = ITrigger.PARTNER_TYPE;
  public static final Number PARTNER_GROUP          = ITrigger.PARTNER_GROUP;
  public static final Number PARTNER_ID             = ITrigger.PARTNER_ID;
  public static final Number CHANNEL_UID            = ITrigger.CHANNEL_UID; // 20031120 DDJ
  public static final Number CAN_DELETE             = ITrigger.CAN_DELETE;
  public static final Number IS_LOCAL_PENDING       = ITrigger.IS_LOCAL_PENDING; //20030808AH

  public static final Number PROCESS_ID             = ITrigger.PROCESS_ID;
  public static final Number IS_REQUEST             = ITrigger.IS_REQUEST;
  public static final Number NUM_OF_RETRIES         = ITrigger.NUM_OF_RETRIES; // 20031120 DDJ
  public static final Number RETRY_INTERVAL         = ITrigger.RETRY_INTERVAL; // 20031120 DDJ
}
