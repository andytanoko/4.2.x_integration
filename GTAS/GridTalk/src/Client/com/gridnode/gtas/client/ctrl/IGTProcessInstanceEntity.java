/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcessInstanceEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-08-20     Andrew Hill         Added USER_TRACKING_IDENTIFIER field
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.rnif.IProcessInstance;

public interface IGTProcessInstanceEntity extends IGTEntity
{
  // Fail Reason constants
  public static final int FR_RETRY_OUT            = IProcessInstance.FR_RETRY_OUT;
  public static final int FR_ACT_OUT              = IProcessInstance.FR_ACT_OUT;
  public static final int FR_VALIDATE_EX          = IProcessInstance.FR_VALIDATE_EX;
  public static final int FR_RECEIVE_EX           = IProcessInstance.FR_RECEIVE_EX;
  public static final int FR_USER_CANCEL          = IProcessInstance.FR_USER_CANCEL;

  // Entity fields
  public static final Number UID                  = IProcessInstance.UID;
  public static final Number PROCESS_INSTANCE_ID  = IProcessInstance.PROCESS_INSTANCE_ID;
  public static final Number PARTNER              = IProcessInstance.PARTNER;
  public static final Number STATE                = IProcessInstance.STATE;
  public static final Number START_TIME           = IProcessInstance.START_TIME;
  public static final Number END_TIME             = IProcessInstance.END_TIME;
  public static final Number RETRY_NUM            = IProcessInstance.RETRY_NUM;
  public static final Number IS_FAILED            = IProcessInstance.IS_FAILED;
  public static final Number FAIL_REASON          = IProcessInstance.FAIL_REASON;
  public static final Number DETAIL_REASON        = IProcessInstance.DETAIL_REASON;
  public static final Number ASSOC_DOCS           = IProcessInstance.ASSOC_DOCS;
  public static final Number PROCESS_DEF_NAME     = IProcessInstance.PROCESS_DEF_NAME;
  public static final Number ROLE_TYPE            = IProcessInstance.ROLE_TYPE;
  public static final Number USER_TRACKING_IDENTIFIER = IProcessInstance.USER_TRACKING_ID; //20030821AH
}