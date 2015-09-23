/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertTypes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 * Jun 20 2003    Neo Sok Lay         Add DOCUMENT_STATUS_UPDATE type.
 * Oct 03 2007    Tam Wei Xiang       Add DOCUMENT_RESEND_EXHAUSTED type.
 */
package com.gridnode.gtas.server.notify;

/**
 * This interface defines the AlertType(s) that GTAS currently support.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IAlertTypes
{
  // Connection
  static final String GRIDMASTER_CONNECTION_ACTIVITY = "GRIDMASTER_CONNECTION_ACTIVITY";
  static final String GRIDMASTER_CONNECTION_LOST     = "GRIDMASTER_CONNECTION_LOST";
  static final String GRIDMASTER_RECONNECTION        = "GRIDMASTER_RECONNECTION";
  static final String PARTNER_UNCONTACTABLE          = "PARTNER_UNCONTACTABLE";

  // Application/general
  static final String APPLICATION_STATE              = "APPLICATION_STATE";

  // Activation
  static final String PARTNER_ACTIVATION             = "PARTNER_ACTIVATION";

  // License
  static final String LICENSE_EXPIRED                = "LICENSE_EXPIRED";
  static final String LICENSE_EXPIRE_SOON            = "LICENSE_EXPIRE_SOON";

  // Document
  static final String DOCUMENT_RECEIVED              = "DOCUMENT_RECEIVED";
  static final String DOCUMENT_RECEIVED_BY_PARTNER   = "DOCUMENT_RECEIVED_BY_PARTNER";
  static final String DOCUMENT_EXPORTED              = "DOCUMENT_EXPORTED";
  static final String DOCUMENT_STATUS_UPDATE         = "DOCUMENT_STATUS_UPDATE";
  static final String DOCUMENT_RESEND_EXHAUSTED      = "DOCUMENT_RESEND_EXHAUSTED";
  
  //failures
  static final String PARTNER_FUNCTION_FAILURE       = "PARTNER_FUNCTION_FAILURE";
  static final String PROCESS_INSTANCE_FAILURE       = "PROCESS_INSTANCE_FAILURE";
  static final String DOCUMENT_MAPPING_FAILURE       = "DOCUMENT_MAPPING_FAILURE";
  static final String USER_PROCEDURE_FAILURE         = "USER_PROCEDURE_FAILURE";

  //Not used in AlertTrigger configuration
  static final String DOCUMENT_REMINDER              = "DOCUMENT_REMINDER";
  static final String DOCUMENT_RESPONSE_RECEIVED     = "DOCUMENT_RESPONSE_RECEIVED";
  // User defined, used in Workflow
  static final String USER_DEFINED                   = "USER_DEFINED";

}
