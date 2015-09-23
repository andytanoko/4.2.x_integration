/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAlertTypeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-02-04     Daniel D'Cotta      Created
 * 2003-05-06     Andrew Hill         AlertType name constants
 * 2003-06-23     Neo Sok Lay         Add NAME_DOCUMENT_STATUS_UPDATE.
 * 2007-10-03     Tam Wei Xiang       Add NAME_DOCUMENT_RESEND_EXHAUSTED.
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IAlertType;
import com.gridnode.gtas.model.alert.IAlertTypes;

public interface IGTAlertTypeEntity extends IGTEntity
{
  // Fields
  public static final Number UID          = IAlertType.UID;
  public static final Number NAME         = IAlertType.NAME;
  public static final Number DESCRIPTION  = IAlertType.DESCRIPTION;
  
  
  // 20030506AH - Constants for predefined AlertType names:
  // Connection
  static final String NAME_GRIDMASTER_CONNECTION_ACTIVITY = IAlertTypes.GRIDMASTER_CONNECTION_ACTIVITY;
  static final String NAME_GRIDMASTER_CONNECTION_LOST     = IAlertTypes.GRIDMASTER_CONNECTION_LOST;
  static final String NAME_GRIDMASTER_RECONNECTION        = IAlertTypes.GRIDMASTER_RECONNECTION;
  static final String NAME_PARTNER_UNCONTACTABLE          = IAlertTypes.PARTNER_UNCONTACTABLE;

  // Application/general
  static final String NAME_APPLICATION_STATE              = IAlertTypes.APPLICATION_STATE;

  // Activation
  static final String NAME_PARTNER_ACTIVATION             = IAlertTypes.PARTNER_ACTIVATION;

  // License
  static final String NAME_LICENSE_EXPIRED                = IAlertTypes.LICENSE_EXPIRED;
  static final String NAME_LICENSE_EXPIRE_SOON            = IAlertTypes.LICENSE_EXPIRE_SOON;

  // Document
  static final String NAME_DOCUMENT_RECEIVED              = IAlertTypes.DOCUMENT_RECEIVED;
  static final String NAME_DOCUMENT_RECEIVED_BY_PARTNER   = IAlertTypes.DOCUMENT_RECEIVED_BY_PARTNER;
  static final String NAME_DOCUMENT_EXPORTED              = IAlertTypes.DOCUMENT_EXPORTED;
  static final String NAME_DOCUMENT_STATUS_UPDATE         = IAlertTypes.DOCUMENT_STATUS_UPDATE;
  static final String NAME_DOCUMENT_RESEND_EXHAUSTED      = IAlertTypes.DOCUMENT_RESEND_EXHAUSTED;
  
  //failures
  static final String NAME_PARTNER_FUNCTION_FAILURE       = IAlertTypes.PARTNER_FUNCTION_FAILURE;
  static final String NAME_PROCESS_INSTANCE_FAILURE       = IAlertTypes.PROCESS_INSTANCE_FAILURE;
  static final String NAME_DOCUMENT_MAPPING_FAILURE       = IAlertTypes.DOCUMENT_MAPPING_FAILURE;
  static final String NAME_USER_PROCEDURE_FAILURE         = IAlertTypes.USER_PROCEDURE_FAILURE;

  //Not used in AlertTrigger configuration
  static final String NAME_DOCUMENT_REMINDER              = IAlertTypes.DOCUMENT_REMINDER;
  static final String NAME_DOCUMENT_RESPONSE_RECEIVED     = IAlertTypes.DOCUMENT_RESPONSE_RECEIVED;
  // User defined, used in Workflow
  static final String NAME_USER_DEFINED                   = IAlertTypes.USER_DEFINED;
  //.....
  
  
}
