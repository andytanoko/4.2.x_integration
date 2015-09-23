/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IErrorCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2002    Neo Sok Lay         Created
 * May 24 2002    Jared Low           Added codes for the GridForm module.
 * Aug 06 2002    Neo Sok Lay         Added codes for the Enterprise module.
 * Aug 26 2002    Neo Sok Lay         Added codes for Sending Entity.
 * Sep 06 2002    Neo Sok Lay         Added codes for the GridNode module.
 * Oct 28 2002    Neo Sok Lay         Added codes for the Connection module.
 * Dec 27 2002    Neo Sok Lay         Added code for getting Utc Time.
 * Jan 13 2002    Neo Sok Lay         Added code for filtering helpers.
 * Jan 30 2002    Jagadeesh           Added code for Certificate module.
 * Feb 06 2003    Neo Sok Lay         Added codes for Alert module.
 * Apr 15 2003    Koh Han Sing        Added code for nodelock exception.
 * Apr 28 2003    Qingsong            Added codes for Certificate module.
 * Jul 14 2003    Neo Sok Lay         Added codes for Deleting entities.
 * Aug 12 2003    Neo Sok Lay         Added codes for publish/query Registry.
 * Mar 06 2006    Neo Sok Lay         Added code for batch import schemas error.
 * Mar 16 2006    Tam Wei Xiang       Added code for get schema zip file entry error
 * Mar 28 2006    Neo Sok Lay         Added code for max active partners reached
 * Jun 22 2006    Tam Wei Xiang       Added code for license expired
 * Jul 31 2006    Tam Wei Xiang       Added code for invalid CA certificate
 */
package com.gridnode.gtas.exceptions;

public interface IErrorCode
{
  static final short NO_ERROR                 = -1;

  static final short GENERAL_ERROR            = 1;

  static final short CREATE_ENTITY_ERROR      = 1001;
  static final short UPDATE_ENTITY_ERROR      = 1002;
  static final short DELETE_ENTITY_ERROR      = 1003;

  static final short FIND_ENTITY_BY_KEY_ERROR = 1004;
  static final short FIND_ENTITY_LIST_ERROR   = 1005;
  
  static final short DELETE_ENTITY_LIST_ERROR = 1006;

  static final short SEND_ENTITY_ERROR        = 1010;

  static final short CONNECTION_REQUIRED_ERROR= 1020;
  static final short GET_UTC_ERROR            = 1021;

  static final short FILTER_HELPER_ERROR      = 1030;

  //User
  static final short CHANGE_PASSWORD_ERROR    = 1101;
  static final short LOGIN_ERROR              = 1102;
  static final short LOGOUT_ERROR             = 1103;

  //Enterprise
  static final short UPDATE_ASSOCIATION_ERROR = 1201;
  static final short FIND_ASSOCIATION_ERROR   = 1202;
  static final short SERVER_WATCHLIST_ERROR   = 1203;

  //GridNode
  static final short SAVE_COY_PROFILE_ERROR   = 1301;

  //Activation
  static final short SEARCH_GRIDNODE_ERROR    = 1401;
  static final short ACTIVATE_GRIDNODE_ERROR  = 1402;

  //Connection
  static final short NETWORK_SETTING_ERROR    = 1501;
  static final short CONNECTION_SETUP_ERROR   = 1502;
  static final short CONNECTION_ERROR         = 1505;
  static final short DISCONNECTION_ERROR      = 1506;
  static final short INVALID_SEC_PWD_ERROR    = 1507;

  //Certificate
  static final short INVALID_FILETYPE_ERROR   =1601;
  static final short INVALID_PASSWORD_OR_FILETYPE_ERROR=1602;
  static final short DUPLICATE_CERTIFICATE_IMPORT_ERROR = 1603;
  
  static final short INCORRECT_PRIVATE_CERT_PASSWORD = 1604;
  static final short CHANGE_CERTIFICATE_NAME_ERROR = 1605;
  static final short EXPORT_CERTIFICATE_KEYSTORE_ERROR = 1606;
  static final short REMOVE_CERTIFICATE_ERROR = 1607;
  static final short EXPORT_CERTIFICATE_TRUSTSTORE_ERROR = 1608;
  static final short CHANGE_PRIVATE_CERT_PASSWORD_ERROR = 1609;
  static final short SET_PRIVATE_CERT_PASSWORD_ERROR = 1610;
  static final short GET_PRIVATE_CERT_PASSWORD_STATE_ERROR = 1611;
  static final short INVALID_CA_CERTIFICATE_ERROR = 1612;
  
  //Alert
  static final short SUBSTITUTION_LIST_ERROR  = 1701;

  //Mapping
  static final short BATCH_IMPORT_SCHEMA_ERROR = 1801;
  static final short GET_SCHEMA_ZIP_FILE_ENTRY_ERROR = 1802;
  
  // GridForm.
  static final short LOAD_FORM_ERROR          = 4001;
  static final short SAVE_FORM_ERROR          = 4002;
  static final short POPULATE_FORM_ERROR      = 4003;
  static final short OUTPUT_FORM_ERROR        = 4004;

  //Registration
  static final short INVALID_REGISTRATION_ERROR = 7001;
  static final short NODELOCK_ERROR             = 7002;
  static final short INVALID_LICENSE_FILE_ERROR = 7003;
  static final short CREATE_PARTNER_MAX_ACTIVE_REACHED= 7004;
  static final short UPDATE_PARTNER_MAX_ACTIVE_REACHED= 7005;
  static final short LICENSE_EXPIRED_ERROR = 7006;
  
  //Detail Delete Error codes
  static final short DELETE_RECORD_NOT_EXISTS_ERROR  = 10060;
  static final short DELETE_NOT_ENABLED_ERROR = 10061;
  static final short DEPENDENCIES_EXIST_ERROR = 10062;
  static final short UNEXPECTED_ERROR         = 10063;
  
  //Public registry
  static final short REGISTRY_PUBLISH_ERROR = 8001;
  static final short REGISTRY_INQUIRE_ERROR = 8002;
  static final short REGISTRY_SYNC_UP_ERROR = 8003;
  static final short REGISTRY_SYNC_DONW_ERROR = 8004;
  static final short GET_MSG_STDS_ERROR = 8005;
  static final short CONFIGURE_BE_ERROR = 8006;
  
}