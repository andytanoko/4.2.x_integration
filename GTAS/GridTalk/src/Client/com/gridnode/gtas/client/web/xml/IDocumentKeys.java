/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2002-06-06     Daniel D'Cotta      Added GridForm pages
 * 2002-10-24     Andrew Hill         Change keys to new style
 * 2004-02-11     Neo Sok Lay         Added ScheduledTask pages
 * 2005-03-16     Andrew Hill         Added updateUserUpdate (for user to change own PW)
 * 2005-09-20     Sumedh C.           Added ES_SEARCH_PAGE field for estore search page.
 * 2005-12-28			SC									Add JMS_DESTINATION_VIEW field.
 * 2006-03-10     Tam Wei Xiang       Added IMPORT_SCHEMA_MAPPING_UPDATE
 *                                          SCHEMA_MAPPING_FILES_UPDATE
 *                                          SCHEMA_MAPPING_FILE_VIEW
 * 2006-03-17     Tam Wei Xiang       Added EMBEDDED_LIST_VIEW_LIGHT     
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream.  
 * 2006-10-27     Regina Zeng         Added GdocDetailPage  
 * 2008-10-17     Wong Yee Wah        Added AS2DocTypeMapping                           
 */
package com.gridnode.gtas.client.web.xml;

/**
 * Constant keys for documents managed by the Document Manager
 */
public interface IDocumentKeys
{
  public static final String TEMPLATE                     = "template";
  public static final String DETAIL_VIEW_TEMPLATE         = "detailViewTemplate";
  public static final String BLANK                        = "blank";
  public static final String LOGIN                        = "login";
  public static final String GMT_OFFSET                   = "gmtOffset"; //20030113AH
  public static final String GET_CERT_PW                  = "getCertPw"; //20030423AH
  public static final String USER_UPDATE                  = "userUpdate";
  public static final String UPDATE_USER_UPDATE           = "updateUserUpdate"; //20050316AH
  public static final String USER_VIEW                    = "userView";
  public static final String TEMPLATE_LISTVIEW            = "templateListview";
  public static final String PARTNER_TYPE_UPDATE          = "partnerTypeUpdate";
  public static final String PARTNER_TYPE_VIEW            = "partnerTypeView";
  public static final String PARTNER_GROUP_UPDATE         = "partnerGroupUpdate";
  public static final String PARTNER_GROUP_VIEW           = "partnerGroupView";
  public static final String PARTNER_UPDATE               = "partnerUpdate";
  public static final String PARTNER_VIEW                 = "partnerView";
  public static final String ACCESS_RIGHT_UPDATE          = "accessRightUpdate";
  public static final String ACCESS_RIGHT_VIEW            = "accessRightView";
  public static final String ROLE_UPDATE                  = "roleUpdate";
  public static final String ROLE_VIEW                    = "roleView";
  public static final String DOCUMENT_TYPE_UPDATE         = "documentTypeUpdate";
  public static final String DOCUMENT_TYPE_VIEW           = "documentTypeView";
  public static final String MAPPING_FILE_UPDATE          = "mappingFileUpdate";
  public static final String MAPPING_FILE_VIEW            = "mappingFileView";
  public static final String GRIDTALK_MAPPING_RULE_UPDATE = "gridtalkMappingRuleUpdate";
  public static final String GRIDTALK_MAPPING_RULE_VIEW   = "gridtalkMappingRuleView";
  public static final String CHANNEL_INFO_UPDATE          = "channelInfoUpdate";
  public static final String CHANNEL_INFO_VIEW            = "channelInfoView";
  public static final String COMM_INFO_UPDATE             = "commInfoUpdate";
  public static final String COMM_INFO_VIEW               = "commInfoView";
  public static final String FILE_TYPE_VIEW               = "fileTypeView";
  public static final String FILE_TYPE_UPDATE             = "fileTypeUpdate";
  public static final String BUSINESS_ENTITY_VIEW         = "businessEntityView";
  public static final String BUSINESS_ENTITY_UPDATE       = "businessEntityUpdate";
  public static final String GRID_DOCUMENT_UPDATE         = "gridDocumentUpdate";
  public static final String GRID_DOCUMENT_VIEW           = "gridDocumentView";
  public static final String GRID_DOCUMENT_IMPORT         = "gridDocumentImport";
  public static final String COMPANY_PROFILE_VIEW         = "companyProfileView";
  public static final String COMPANY_PROFILE_UPDATE       = "companyProfileUpdate";
  public static final String SECURITY_INFO_UPDATE         = "securityInfoUpdate";
  public static final String SECURITY_INFO_VIEW           = "securityInfoView";
  public static final String PACKAGING_INFO_UPDATE        = "packagingInfoUpdate";
  public static final String PACKAGING_INFO_VIEW          = "packagingInfoView";
  public static final String PARTNER_FUNCTION_UPDATE      = "partnerFunctionUpdate";
  public static final String PARTNER_FUNCTION_VIEW        = "partnerFunctionView";
  public static final String WORKFLOW_ACTIVITY_UPDATE     = "workflowActivityUpdate";
  public static final String WORKFLOW_ACTIVITY_VIEW       = "workflowActivityView";
  public static final String REGISTRATION_INFO_UPDATE     = "registrationInfoUpdate";
  public static final String REGISTRATION_INFO_VIEW       = "registrationInfoView"; //20030417AH
  public static final String TRIGGER_UPDATE               = "triggerUpdate";
  public static final String TRIGGER_VIEW                 = "triggerView";
  public static final String SEARCH_GRIDNODE_QUERY_UPDATE = "searchGridnodeQueryUpdate";
  public static final String SEARCH_GRIDNODE_QUERY_VIEW   = "searchGridnodeQueryView";
  public static final String NETWORK_SETTING_UPDATE       = "networkSettingUpdate";
  public static final String NETWORK_SETTING_VIEW         = "networkSettingView";
  public static final String CONNECTION_SETUP_RESULT_UPDATE = "connectionSetupResultUpdate";
  public static final String CONNECTION_SETUP_RESULT_VIEW   = "connectionSetupResultView";
  public static final String ACTIVATION_RECORD_UPDATE     = "activationRecordUpdate";
  public static final String ACTIVATION_RECORD_VIEW       = "activationRecordView";
  public static final String GRIDNODE_VIEW                = "gridNodeView";

  public static final String EMBED_LISTVIEW               = "embedListview";
  public static final String MULTISELECTOR                = "multiselector";
  public static final String MULTIFILES                   = "multifiles";
  public static final String EXCEPTION                    = "exception";

  public static final String SEND_BE_UPDATE               = "sendBusinessEntityUpdate";
  public static final String SERVER                       = "server";

  public static final String PROCEDURE_DEF_FILE_UPDATE    = "procedureDefFileUpdate";
  public static final String PROCEDURE_DEF_FILE_VIEW      = "procedureDefFileView";
  public static final String USER_PROCEDURE_UPDATE        = "userProcedureUpdate";
  public static final String USER_PROCEDURE_VIEW          = "userProcedureView";
  public static final String SHELL_EXECUTABLE_UPDATE      = "shellExecutableUpdate";
  public static final String SHELL_EXECUTABLE_VIEW        = "shellExecutableView";
  public static final String JAVA_PROCEDURE_UPDATE        = "javaProcedureUpdate";
  public static final String JAVA_PROCEDURE_VIEW          = "javaProcedureView";
  public static final String PARAM_DEF_UPDATE             = "paramDefUpdate";
  public static final String PARAM_DEF_VIEW               = "paramDefView";
  public static final String RETURN_DEF_UPDATE            = "returnDefUpdate";
  public static final String RETURN_DEF_VIEW              = "returnDefView";
  public static final String PARTNER_WATCH_LIST           = "partnerWatchList";

  public static final String PROCESS_MAPPING_UPDATE       = "processMappingUpdate";
  public static final String PROCESS_MAPPING_VIEW         = "processMappingView";
  public static final String PROCESS_DEF_UPDATE           = "processDefUpdate";
  public static final String PROCESS_DEF_VIEW             = "processDefView";
  public static final String PROCESS_ACT_UPDATE           = "processActUpdate";
  public static final String PROCESS_ACT_VIEW             = "processActView";
  public static final String PROCESS_INSTANCE_VIEW        = "processInstanceView";

  public static final String RFC_UPDATE                   = "rfcUpdate";
  public static final String RFC_VIEW                     = "rfcView";
  public static final String PORT_UPDATE                  = "portUpdate";
  public static final String PORT_VIEW                    = "portView";

  public static final String BIZ_CERT_MAPPING_UPDATE      = "bizCertMappingUpdate"; //20030114AH
  public static final String BIZ_CERT_MAPPING_VIEW        = "bizCertMappingView"; //20030114AH

  public static final String CERTIFICATE_UPDATE           = "certificateUpdate"; //200301020AH
  public static final String CERTIFICATE_VIEW             = "certificateView"; //20030120AH

  public static final String ALERT_UPDATE                   = "alertUpdate";
  public static final String ALERT_VIEW                     = "alertView";
  public static final String ACTION_UPDATE                  = "actionUpdate";
  public static final String ACTION_VIEW                    = "actionView";
  public static final String MESSAGE_TEMPLATE_UPDATE        = "messageTemplateUpdate";
  public static final String MESSAGE_TEMPLATE_VIEW          = "messageTemplateView";
  public static final String RESPONSE_TRACK_RECORD_UPDATE   = "responseTrackRecordUpdate";
  public static final String RESPONSE_TRACK_RECORD_VIEW     = "responseTrackRecordView";
  public static final String REMINDER_ALERT_UPDATE          = "reminderAlertUpdate";
  public static final String REMINDER_ALERT_VIEW            = "reminderAlertView";
  public static final String ALERT_TRIGGER_UPDATE           = "alertTriggerUpdate"; //20030507AH
  public static final String ALERT_TRIGGER_VIEW             = "alertTriggerView"; //20030507AH

  public static final String VARIOUS                        = "various"; //20030325AH

  public static final String CERTIFICATE_PASSWORD           = "certPassword"; //20030414AH
  
  public static final String ARCHIVE_DOCUMENT_UPDATE        = "archiveDocumentUpdate"; //20030517AH
  public static final String RESTORE_DOCUMENT_UPDATE        = "restoreDocumentUpdate"; //20030522AH

  public static final String ARCHIVE_UPDATE                 = "archiveUpdate"; 
  public static final String ARCHIVE_VIEW                   = "archiveView";
  
  public static final String SCHEDULED_ARCHIVE_UPDATE       = "scheduledArchiveUpdate"; 
  public static final String SCHEDULED_ARCHIVE_VIEW         = "scheduledArchiveView"; 
  
  public static final String EXPORT_CONFIG_UPDATE           = "exportConfigUpdate"; //20030528AH
  public static final String IMPORT_CONFIG_UPDATE           = "importConfigUpdate"; //20030528AH

  public static final String SOAP_PROCEDURE_UPDATE          = "soapProcedureUpdate"; //20030730AH
  public static final String SOAP_PROCEDURE_VIEW            = "soapProcedureView"; //20030730AH
  
  public static final String REGISTRY_CONNECT_INFO_UPDATE   = "registryConnectInfoUpdate";    
  public static final String REGISTRY_CONNECT_INFO_VIEW     = "registryConnectInfoView";      
  public static final String PUBLISH_BUSINESS_ENTITY        = "publishBusinessEntity";        
  public static final String SEARCH_REGISTRY_QUERY_UPDATE   = "searchRegistryQueryUpdate";    
  public static final String SEARCH_REGISTRY_QUERY_VIEW     = "searchRegistryQueryView";      
  public static final String SEARCHED_BUSINESS_ENTITY_VIEW  = "searchedBusinessEntityView"; // 20030915 DDJ
  
  public static final String SEARCH_QUERY_UPDATE            = "searchQueryUpdate";    
  public static final String SEARCH_QUERY_VIEW              = "searchQueryView";      // 20031027 DDJ     

  public static final String EMBEDDED_CHANNEL_INFO_VIEW     = "embeddedChannelInfoView";  // 20030915 DDJ
  
  public static final String HOUSE_KEEPING_UPDATE            = "houseKeepingUpdate";    
  public static final String HOUSE_KEEPING_VIEW              = "houseKeepingView";    // 20040116 DDJ
  
  public static final String WEBSERVICE_UPDATE            = "webServiceUpdate"; // 20040209 Mahesh
  public static final String WEBSERVICE_VIEW              = "webServiceView";   // 20040209 Mahesh

  public static final String SERVICE_ASSIGNMENT_UPDATE            = "serviceAssignmentUpdate"; // 20040209 Mahesh
  public static final String SERVICE_ASSIGNMENT_VIEW              = "serviceAssignmentView";   // 20040209 Mahesh
       
  
  public static final String SCHEDULED_TASK_UPDATE          = "scheduledTaskUpdate"; //20040211NSL
  public static final String SCHEDULED_TASK_VIEW            = "scheduledTaskView";   //20040211NSL

  public static final String EMAIL_CONFIG_UPDATE          = "emailConfigUpdate"; //20040701 Mahesh
  public static final String EMAIL_CONFIG_VIEW            = "emailConfigView";   //20040701 Mahesh
  
  public static final String ES_PI_SEARCH_PAGE = "EsPiSearchPage";   //20051128 Sumedh
  public static final String ES_DOC_SEARCH_PAGE = "EsDocSearchPage";   //20051128 Sumedh
  
//public static final String ES_PI_VIEW = "EsPiView";   //20051006 Sumedh

  public static final String ES_PI_DETAIL = "EsPiDetail"; //20051011 Sumedh
  public static final String AUDIT_FILE_VIEW = "AuditFileView"; //20051012 Sumedh
  public static final String GDOC_DETAIL_PAGE = "GdocDetailPage"; //20061027 Regina Zeng

//public static final String ES_SEARCH_QUERY = "EsSearchQuery";		//20051010 Sumedh
  public static final String ES_PI_SEARCH_QUERY = "EsPi-searchQuery";		//20051020 Sumedh
  public static final String ES_DOC_SEARCH_QUERY = "EsDoc-searchQuery";		//20051020 Sumedh
  public static final String JMS_DESTINATION_UPDATE           = "jmsDestinationUpdate";
  public static final String JMS_DESTINATION_VIEW            = "jmsDestinationView";
  
  public static final String IMPORT_SCHEMA_MAPPING_UPDATE         = "importSchemaUpdate"; //20060310 TWX
  
  public static final String SCHEMA_MAPPING_FILE_UPDATE      = "schemaMappingFileUpdate"; //20060314 TWX
  public static final String SCHEMA_MAPPING_FILE_VIEW        = "schemaMappingFileView";   //20060314 TWX
  public static final String EMBEDDED_LIST_VIEW_LIGHT        = "embeddedListViewLight";   //20060317 TWX
  
  public static final String AS2_DOC_TYPE_MAPPING_UPDATE         = "as2DocTypeMappingUpdate";     //20081017 WYW
  public static final String AS2_DOC_TYPE_MAPPING_VIEW           = "as2DocTypeMappingView";       //20081017 WYW
}