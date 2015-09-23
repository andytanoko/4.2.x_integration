/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAlertTriggerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 * 2003-06-23     Neo Sok Lay         Add NAME_DOCUMENT_STATUS_UPDATE alert type.
 * 2007-10-03     Tam Wei Xiang       Add NAME_DOCUMENT_RESEND_EXHAUSTED alert type
 *                                    to be showed in all level.
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IAlertTrigger;
 
public interface IGTAlertTriggerEntity extends IGTEntity
{
  //Recipient type constants. Note that we dont include the final character which we assume is
  //the delimiter ':'
  public static final String RECIPIENT_TYPE_USER
    = IAlertTrigger.RECPT_USER.substring(0,IAlertTrigger.RECPT_USER.length()-1);
    
  public static final String RECIPIENT_TYPE_ROLE
    = IAlertTrigger.RECPT_ROLE.substring(0,IAlertTrigger.RECPT_ROLE.length()-1);
    
  public static final String RECIPIENT_TYPE_EMAIL_ADDRESS
    = IAlertTrigger.RECPT_EMAIL_ADDRESS.substring(0,IAlertTrigger.RECPT_EMAIL_ADDRESS.length()-1);
    
  public static final String RECIPIENT_TYPE_EMAIL_CODE
    = IAlertTrigger.RECPT_EMAIL_CODE.substring(0,IAlertTrigger.RECPT_EMAIL_CODE.length()-1);
    
  public static final String RECIPIENT_TYPE_EMAIL_CODE_XPATH
    = IAlertTrigger.RECPT_EMAIL_CODE_XPATH.substring(0,IAlertTrigger.RECPT_EMAIL_CODE_XPATH.length()-1);
    
  public static final String RECIPIENT_TYPE_EMAIL_ADDRESS_XPATH
    = "EMAIL_ADDRESS_XPATH"; //@todo: refer to interface once its delivered
    
  //...
  
  //Constants for Level
  public static final Integer LEVEL_0 = IAlertTrigger.LEVEL_0;
  public static final Integer LEVEL_1 = IAlertTrigger.LEVEL_1;
  public static final Integer LEVEL_2 = IAlertTrigger.LEVEL_2;
  public static final Integer LEVEL_3 = IAlertTrigger.LEVEL_3;
  public static final Integer LEVEL_4 = IAlertTrigger.LEVEL_4;
  
  static final String[] LEVEL_0_ALERT_TYPE_NAMES = 
  {
    IGTAlertTypeEntity.NAME_GRIDMASTER_CONNECTION_ACTIVITY,
    IGTAlertTypeEntity.NAME_GRIDMASTER_CONNECTION_LOST,
    IGTAlertTypeEntity.NAME_GRIDMASTER_RECONNECTION,
    IGTAlertTypeEntity.NAME_APPLICATION_STATE,
    IGTAlertTypeEntity.NAME_PARTNER_ACTIVATION,
    IGTAlertTypeEntity.NAME_PARTNER_UNCONTACTABLE,
    IGTAlertTypeEntity.NAME_LICENSE_EXPIRED,
    IGTAlertTypeEntity.NAME_LICENSE_EXPIRE_SOON,
    IGTAlertTypeEntity.NAME_DOCUMENT_RECEIVED,
    IGTAlertTypeEntity.NAME_DOCUMENT_RECEIVED_BY_PARTNER,
    IGTAlertTypeEntity.NAME_DOCUMENT_STATUS_UPDATE,
    IGTAlertTypeEntity.NAME_PARTNER_FUNCTION_FAILURE,
    IGTAlertTypeEntity.NAME_PROCESS_INSTANCE_FAILURE,
    IGTAlertTypeEntity.NAME_DOCUMENT_EXPORTED,
    IGTAlertTypeEntity.NAME_DOCUMENT_MAPPING_FAILURE,
    IGTAlertTypeEntity.NAME_USER_PROCEDURE_FAILURE,
    IGTAlertTypeEntity.NAME_DOCUMENT_RESEND_EXHAUSTED
  };
  
  static final String[] LEVEL_1_ALERT_TYPE_NAMES = 
  {
    IGTAlertTypeEntity.NAME_DOCUMENT_RECEIVED,
    IGTAlertTypeEntity.NAME_DOCUMENT_RECEIVED_BY_PARTNER,
    IGTAlertTypeEntity.NAME_PARTNER_FUNCTION_FAILURE,
    IGTAlertTypeEntity.NAME_PROCESS_INSTANCE_FAILURE,
    IGTAlertTypeEntity.NAME_DOCUMENT_EXPORTED,
    IGTAlertTypeEntity.NAME_DOCUMENT_MAPPING_FAILURE,
    IGTAlertTypeEntity.NAME_DOCUMENT_STATUS_UPDATE,
    IGTAlertTypeEntity.NAME_USER_PROCEDURE_FAILURE,
    IGTAlertTypeEntity.NAME_DOCUMENT_RESEND_EXHAUSTED
  };
  
  /*
   * An array of String[] defining the AlertType names applicable at each level.
   * Use the level as an index to retrieve an array of String of alertType.name
   * values for the appropriate predefined AlertTypes at that level.
   */
  public static final String[][] ALERT_TYPE_NAMES =
  {
    LEVEL_0_ALERT_TYPE_NAMES,
    LEVEL_1_ALERT_TYPE_NAMES,
    LEVEL_1_ALERT_TYPE_NAMES,
    LEVEL_1_ALERT_TYPE_NAMES,
    LEVEL_1_ALERT_TYPE_NAMES,
  };
  
  //Fields
  public static final Number UID            = IAlertTrigger.UID;
  public static final Number CAN_DELETE     = IAlertTrigger.CAN_DELETE;
  public static final Number LEVEL          = IAlertTrigger.LEVEL;
  public static final Number ALERT_UID      = IAlertTrigger.ALERT_UID;
  public static final Number ALERT_TYPE     = IAlertTrigger.ALERT_TYPE;
  public static final Number DOC_TYPE       = IAlertTrigger.DOC_TYPE;
  public static final Number PARTNER_TYPE   = IAlertTrigger.PARTNER_TYPE;
  public static final Number PARTNER_GROUP  = IAlertTrigger.PARTNER_GROUP;
  public static final Number PARTNER_ID     = IAlertTrigger.PARTNER_ID;
  public static final Number IS_ENABLED     = IAlertTrigger.IS_ENABLED;
  public static final Number IS_ATTACH_DOC  = IAlertTrigger.IS_ATTACH_DOC;
  
  //Not for 'general' use (though still partially exposed. @todo: make sure its hidden outside ctrl)
  static final Number RECIPIENTS  = IAlertTrigger.RECIPIENTS;
  
  //vFields
  public static final Number ALERT_RECIPIENTS = new Integer(-10);
}
