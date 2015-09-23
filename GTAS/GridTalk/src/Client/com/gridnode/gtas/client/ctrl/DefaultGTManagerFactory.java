/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultManagerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-20     Neo Sok Lay         Created
 * 2002-05-21     Andrew Hill         Made constructor package protected
 * 2002-05-29     Daniel D'Cotta      Added managers for GridForm
 * 2002-06-19     Andrew Hill         Added managers for roles, accessRights, & features
 * 2002-??-??     Andrew Hill         Added lots of managers, and manager responsibilty lookup
 * 2002-09-10     Andrew Hill         CompanyProfile, GnCategory, CountryCode, LanguageCode mgrs
 * 2002-09-13     Andrew Hill         Certificate
 * 2002-09-16     Andrew Hill         PartnerFunction
 * 2002-10-??     Andrew Hill         RegistrationInfo
 * 2002-10-14     Daniel D'Cotta      Trigger
 * 2002-10-17     Andrew Hill         SearchGridNodeQuery
 * 2002-11-01     Andrew Hill         NetworkSetting, ConnectionSetupResult
 * 2002-11-15     Andrew Hill         ActivationRecord
 * 2002-11-27     Andrew Hill         ProcessMapping
 * 2002-12-09     Andrew Hill         Attachment
 * 2002-12-19     Andrew Hill         Rfc & Port
 * 2003-01-14     Andrew Hill         BizCertMapping
 * 2003-05-06     Andrew Hill         AlertTrigger, AlertRecipient
 * 2003-05-17     Andrew Hill         ArchiveDocument
 * 2003-05-28     Andrew Hill         ExportConfig
 * 2003-07-08     Andrew Hill         GridForm has been protected. It has gone down the stairs.
 * 2003-07-31     Andrew Hill         SoapProcedure
 * 2003-08-25     Andrew Hill         Added missing entry in table for processInstance
 * 2003-09-15     Daniel D'Cotta      RegistryConnectInfo, RegistryConnectInfoList, SearchRegistryQuery, SearchRegistryCriteria
ISearchedBusinessEntity
 * 2003-10-10     Neo Sok Lay         SearchedChannelInfo
 * 2004-02-12     Neo Sok Lay         Add manager for ScheduledTask
 * 2005-10-08	    Sumedh C.				    Addded 2 lines in initTypeLookup() for mapping for EsPi and EsDoc.
 * 2005-01-05			SC									In method createManager return appropriate manager for type MANAGER_JMS_DESTINATION.
 * 2005-01-05			SC									add mapping in initTypeLookup method for entity: virtual lookup properties
 * 2005-01-11			SC									add mapping in method: initTypeLookup for jms destination entity
 * 2006-03-08     Tam Wei Xiang       add manager BatchImportSchema
 * 2006-03-10     Tam Wei Xiang       add manager ImportSchema
 * 2006-03-14     Tam Wei Xiang       add manager SchemaMappingFile
 * 2006-09-21     Tam Wei Xiang       Merge from estore stream.
 * 2006-09-25     Tam Wei Xiang       added MANAGER_SEARCH_ES_PI_DOCUMENT
 * 2008-08-01	  Wong Yee Wah		  #38   added ENTITY_CERTIFICATE_SWAPPING
 * 2008-11-12     Wong Yee Wah        #80   added AS2DocTypeMapping
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Hashtable;

class DefaultGTManagerFactory extends AbstractManagerFactory
{
  private DefaultGTSession _session;
  private Hashtable    _lookupType = new Hashtable();

  DefaultGTManagerFactory(DefaultGTSession session)
  {
    _session = session;
    initTypeLookup();
  }

  public int getManagerType(String entityType) throws GTClientException
  {
    Integer managerType = (Integer)_lookupType.get(entityType);
    if(managerType == null)
    {
      throw new GTClientException("Unable to determine manager type for entities of type:"
                                  + entityType);
    }
    else
    {
      return managerType.intValue();
    }
  }

  protected IGTManager createManager(int type) throws GTClientException
  {
    IGTManager manager = null;
    switch(type)
    {
      // GridTalk Managers

      case IGTManager.MANAGER_USER:
        manager = new DefaultUserManager(_session);
        break;

      case IGTManager.MANAGER_ROLE:
        manager = new DefaultRoleManager(_session);
        break;

      case IGTManager.MANAGER_FEATURE:
        manager = new DefaultFeatureManager(_session);
        break;

      case IGTManager.MANAGER_ACCESS_RIGHT:
        manager = new DefaultAccessRightManager(_session);
        break;

      case IGTManager.MANAGER_PARTNER_TYPE:
        manager = new DefaultPartnerTypeManager(_session);
        break;

      case IGTManager.MANAGER_PARTNER_GROUP:
        manager = new DefaultPartnerGroupManager(_session);
        break;

      case IGTManager.MANAGER_PARTNER:
        manager = new DefaultPartnerManager(_session);
        break;

      case IGTManager.MANAGER_DOCUMENT_TYPE:
        manager = new DefaultDocumentTypeManager(_session);
        break;
        
      case IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING:
        manager = new DefaultAS2DocTypeMappingManager(_session);
        break;

      case IGTManager.MANAGER_GRIDTALK_MAPPING_RULE:
        manager = new DefaultGridTalkMappingRuleManager(_session);
        break;

      case IGTManager.MANAGER_MAPPING_FILE:
        manager = new DefaultMappingFileManager(_session);
        break;

      case IGTManager.MANAGER_CHANNEL_INFO:
        manager = new DefaultChannelInfoManager(_session);
        break;

      case IGTManager.MANAGER_COMM_INFO:
        manager = new DefaultCommInfoManager(_session);
        break;

      case IGTManager.MANAGER_BUSINESS_ENTITY:
        manager = new DefaultBusinessEntityManager(_session);
        break;

      case IGTManager.MANAGER_FILE_TYPE:
        manager = new DefaultFileTypeManager(_session);
        break;

      case IGTManager.MANAGER_GRID_DOCUMENT:
        manager = new DefaultGridDocumentManager(_session);
        break;

      case IGTManager.MANAGER_COMPANY_PROFILE:
        manager = new DefaultCompanyProfileManager(_session);
        break;

      case IGTManager.MANAGER_GN_CATEGORY:
        manager = new DefaultGnCategoryManager(_session);
        break;

      case IGTManager.MANAGER_COUNTRY_CODE:
        manager = new DefaultCountryCodeManager(_session);
        break;

      case IGTManager.MANAGER_LANGUAGE_CODE:
        manager = new DefaultLanguageCodeManager(_session);
        break;

      case IGTManager.MANAGER_SECURITY_INFO:
        manager = new DefaultSecurityInfoManager(_session);
        break;

      case IGTManager.MANAGER_PACKAGING_INFO:
        manager = new DefaultPackagingInfoManager(_session);
        break;

      case IGTManager.MANAGER_CERTIFICATE:
        manager = new DefaultCertificateManager(_session);
        break;

      case IGTManager.MANAGER_PARTNER_FUNCTION:
        manager = new DefaultPartnerFunctionManager(_session);
        break;

      case IGTManager.MANAGER_REGISTRATION_INFO:
        manager = new DefaultRegistrationInfoManager(_session);
        break;

      case IGTManager.MANAGER_TRIGGER:
        manager = new DefaultTriggerManager(_session);
        break;

       case IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY:
        manager = new DefaultSearchGridNodeQueryManager(_session);
        break;

      case IGTManager.MANAGER_PROCEDURE_DEF_FILE:
        manager = new DefaultProcedureDefFileManager(_session);
        break;

      case IGTManager.MANAGER_NETWORK_SETTING:
        manager = new DefaultNetworkSettingManager(_session);
        break;

      case IGTManager.MANAGER_USER_PROCEDURE:
        manager = new DefaultUserProcedureManager(_session);
        break;

      case IGTManager.MANAGER_CONNECTION_SETUP_RESULT:
        manager = new DefaultConnectionSetupResultManager(_session);
        break;

      case IGTManager.MANAGER_JMS_ROUTER:
        manager = new DefaultJmsRouterManager(_session);
        break;

      case IGTManager.MANAGER_GRIDNODE:
        manager = new DefaultGridNodeManager(_session);
        break;

      case IGTManager.MANAGER_ACTIVATION_RECORD:
        manager = new DefaultActivationRecordManager(_session);
        break;

      case IGTManager.MANAGER_PROCESS_DEF:
        manager = new DefaultProcessDefManager(_session);
        break;

      case IGTManager.MANAGER_PROCESS_MAPPING:
        manager = new DefaultProcessMappingManager(_session);
        break;

      case IGTManager.MANAGER_ATTACHMENT:
        manager = new DefaultAttachmentManager(_session);
        break;

      case IGTManager.MANAGER_RFC:
        manager = new DefaultRfcManager(_session);
        break;

      case IGTManager.MANAGER_PORT:
        manager = new DefaultPortManager(_session);
        break;

      case IGTManager.MANAGER_BIZ_CERT_MAPPING:
        manager = new DefaultBizCertMappingManager(_session);
        break;

      case IGTManager.MANAGER_PROCESS_INSTANCE:
        manager = new DefaultProcessInstanceManager(_session);
        break;

      case IGTManager.MANAGER_ALERT:
        manager = new DefaultAlertManager(_session);
        break;

      case IGTManager.MANAGER_ACTION:
        manager = new DefaultActionManager(_session);
        break;

      case IGTManager.MANAGER_MESSAGE_TEMPLATE:
        manager = new DefaultMessageTemplateManager(_session);
        break;

      case IGTManager.MANAGER_RESPONSE_TRACK_RECORD:
        manager = new DefaultResponseTrackRecordManager(_session);
        break;

      case IGTManager.MANAGER_ALERT_TYPE:
        manager = new DefaultAlertTypeManager(_session);
        break;

      case IGTManager.MANAGER_ALERT_CATEGORY:
        manager = new DefaultAlertCategoryManager(_session);
        break;

      case IGTManager.MANAGER_ALERT_TRIGGER:
        manager = new DefaultAlertTriggerManager(_session); //20030506AH
        break;

      case IGTManager.MANAGER_ARCHIVE_DOCUMENT:
        manager = new DefaultArchiveDocumentManager(_session); //20030517AH
        break;

	    case IGTManager.MANAGER_ARCHIVE:
        manager = new DefaultArchiveManager(_session); 
        break;
        
      case IGTManager.MANAGER_EXPORT_CONFIG:
        manager = new DefaultExportConfigManager(_session);  //20030528AH
        break;

      case IGTManager.MANAGER_REGISTRY_CONNECT_INFO:
        manager = new DefaultRegistryConnectInfoManager(_session);
        break;

      case IGTManager.MANAGER_SEARCH_REGISTRY_QUERY:
        manager = new DefaultSearchRegistryQueryManager(_session); // 20030915 DDJ
        break;

      case IGTManager.MANAGER_SEARCH_QUERY:
        manager = new DefaultSearchQueryManager(_session); // 20031027 DDJ
        break;

      case IGTManager.MANAGER_HOUSE_KEEPING:
        manager = new DefaultHouseKeepingManager(_session); // 20031027 DDJ
        break;

      case IGTManager.MANAGER_SCHEDULED_TASK:
        manager = new DefaultScheduledTaskManager(_session); //20040212NSL
        break;

      case IGTManager.MANAGER_WEBSERVICE:
        manager = new DefaultWebServiceManager(_session); // 20040209 MAHESH
        break;

      case IGTManager.MANAGER_SERVICE_ASSIGNMENT:
        manager = new DefaultServiceAssignmentManager(_session); // 20040209 MAHESH
        break;
      case IGTManager.MANAGER_EMAIL_CONFIG:
        manager = new DefaultEmailConfigManager(_session); // 20040701 MAHESH
        break;
      case IGTManager.MANAGER_ES_PI:
        manager = new DefaultEsPiManager(_session);
        break;
      case IGTManager.MANAGER_ES_DOC:
        manager = new DefaultEsDocManager(_session);
        break;  
      case IGTManager.MANAGER_FIELD_VALUE_COLLECTION:
        manager = new DefaultFieldValueCollectionManager(_session);
        break;
      case IGTManager.MANAGER_AUDIT_FILE:
      	manager = new DefaultAuditFileManager(_session);
      	break;
      case IGTManager.MANAGER_JMS_DESTINATION:
      	manager = new DefaultJmsDestinationManager(_session);
      	break;
      case IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES:
      	manager = new DefaultLookupPropertiesManager(_session);
      	break;
      case IGTManager.MANAGER_IMPORT_SCHEMA:
      	manager = new DefaultImportSchemaManager(_session); 
      	break;
      case IGTManager.MANAGER_SCHEMA_MAPPING_FILE:
      	manager = new DefaultSchemaMappingFileManager(_session);
      	break;
      case IGTManager.MANAGER_SEARCH_ES_PI_DOCUMENT:
      	manager = new DefaultSearchEsPiPageManager(_session);
      	break;
      case IGTManager.MANAGER_GDOC_DETAIL:
        manager = new DefaultGdocDetailManager(_session);
        break;        
      default:
        throw new GTClientException("Unknown manager type:" + type);
    }
    return manager;
  }

  void initTypeLookup()
  {
    _lookupType.put(IGTEntity.ENTITY_ACCESS_RIGHT,                new Integer(IGTManager.MANAGER_ACCESS_RIGHT));
    _lookupType.put(IGTEntity.ENTITY_CHANNEL_INFO,                new Integer(IGTManager.MANAGER_CHANNEL_INFO));
    _lookupType.put(IGTEntity.ENTITY_COMM_INFO,                   new Integer(IGTManager.MANAGER_COMM_INFO));
    _lookupType.put(IGTEntity.ENTITY_DOCUMENT_TYPE,               new Integer(IGTManager.MANAGER_DOCUMENT_TYPE));
    _lookupType.put(IGTEntity.ENTITY_AS2_DOC_TYPE_MAPPING,        new Integer(IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING));
    _lookupType.put(IGTEntity.ENTITY_FEATURE,                     new Integer(IGTManager.MANAGER_FEATURE));
    _lookupType.put(IGTEntity.ENTITY_GRIDTALK_MAPPING_RULE,       new Integer(IGTManager.MANAGER_GRIDTALK_MAPPING_RULE));
    _lookupType.put(IGTEntity.ENTITY_MAPPING_RULE,                new Integer(IGTManager.MANAGER_GRIDTALK_MAPPING_RULE));
    _lookupType.put(IGTEntity.ENTITY_MAPPING_FILE,                new Integer(IGTManager.MANAGER_MAPPING_FILE));
    _lookupType.put(IGTEntity.ENTITY_PARTNER,                     new Integer(IGTManager.MANAGER_PARTNER));
    _lookupType.put(IGTEntity.ENTITY_PARTNER_GROUP,               new Integer(IGTManager.MANAGER_PARTNER_GROUP));
    _lookupType.put(IGTEntity.ENTITY_PARTNER_TYPE,                new Integer(IGTManager.MANAGER_PARTNER_TYPE));
    _lookupType.put(IGTEntity.ENTITY_ROLE,                        new Integer(IGTManager.MANAGER_ROLE));
    _lookupType.put(IGTEntity.ENTITY_USER,                        new Integer(IGTManager.MANAGER_USER));
    _lookupType.put(IGTEntity.ENTITY_ACCOUNT_STATE,               new Integer(IGTManager.MANAGER_USER));
    _lookupType.put(IGTEntity.ENTITY_BUSINESS_ENTITY,             new Integer(IGTManager.MANAGER_BUSINESS_ENTITY));
    _lookupType.put(IGTEntity.ENTITY_WHITE_PAGE,                  new Integer(IGTManager.MANAGER_BUSINESS_ENTITY));
    _lookupType.put(IGTEntity.ENTITY_GRID_DOCUMENT,               new Integer(IGTManager.MANAGER_GRID_DOCUMENT));
    _lookupType.put(IGTEntity.ENTITY_IMPORT_DOCUMENTS,            new Integer(IGTManager.MANAGER_GRID_DOCUMENT));
    _lookupType.put(IGTEntity.ENTITY_COMPANY_PROFILE,             new Integer(IGTManager.MANAGER_COMPANY_PROFILE));
    _lookupType.put(IGTEntity.ENTITY_GN_CATEGORY,                 new Integer(IGTManager.MANAGER_GN_CATEGORY));
    _lookupType.put(IGTEntity.ENTITY_COUNTRY_CODE,                new Integer(IGTManager.MANAGER_COUNTRY_CODE));
    _lookupType.put(IGTEntity.ENTITY_LANGUAGE_CODE,               new Integer(IGTManager.MANAGER_LANGUAGE_CODE));
    _lookupType.put(IGTEntity.ENTITY_SECURITY_INFO,               new Integer(IGTManager.MANAGER_SECURITY_INFO));
    _lookupType.put(IGTEntity.ENTITY_PACKAGING_INFO,              new Integer(IGTManager.MANAGER_PACKAGING_INFO));
    _lookupType.put(IGTEntity.ENTITY_CERTIFICATE,                 new Integer(IGTManager.MANAGER_CERTIFICATE));
    _lookupType.put(IGTEntity.ENTITY_CERTIFICATE_SWAPPING,        new Integer(IGTManager.MANAGER_CERTIFICATE));
    _lookupType.put(IGTEntity.ENTITY_PARTNER_FUNCTION,            new Integer(IGTManager.MANAGER_PARTNER_FUNCTION));
    _lookupType.put(IGTEntity.ENTITY_WORKFLOW_ACTIVITY,           new Integer(IGTManager.MANAGER_PARTNER_FUNCTION));
    _lookupType.put(IGTEntity.ENTITY_REGISTRATION_INFO,           new Integer(IGTManager.MANAGER_REGISTRATION_INFO));
    _lookupType.put(IGTEntity.ENTITY_TRIGGER,                     new Integer(IGTManager.MANAGER_TRIGGER));
    _lookupType.put(IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY,       new Integer(IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY));
    _lookupType.put(IGTEntity.ENTITY_SEARCH_GRIDNODE_CRITERIA,    new Integer(IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY));
    _lookupType.put(IGTEntity.ENTITY_SEARCHED_GRIDNODE,           new Integer(IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY));
    _lookupType.put(IGTEntity.ENTITY_NETWORK_SETTING,             new Integer(IGTManager.MANAGER_NETWORK_SETTING));
    _lookupType.put(IGTEntity.ENTITY_PROCEDURE_DEF_FILE,          new Integer(IGTManager.MANAGER_PROCEDURE_DEF_FILE));
    _lookupType.put(IGTEntity.ENTITY_USER_PROCEDURE,              new Integer(IGTManager.MANAGER_USER_PROCEDURE));
    _lookupType.put(IGTEntity.ENTITY_SHELL_EXECUTABLE,            new Integer(IGTManager.MANAGER_USER_PROCEDURE));
    _lookupType.put(IGTEntity.ENTITY_JAVA_PROCEDURE,              new Integer(IGTManager.MANAGER_USER_PROCEDURE));
    _lookupType.put(IGTEntity.ENTITY_PARAM_DEF,                   new Integer(IGTManager.MANAGER_USER_PROCEDURE));
    _lookupType.put(IGTEntity.ENTITY_RETURN_DEF,                  new Integer(IGTManager.MANAGER_USER_PROCEDURE));
    _lookupType.put(IGTEntity.ENTITY_CONNECTION_SETUP_RESULT,     new Integer(IGTManager.MANAGER_CONNECTION_SETUP_RESULT));
    _lookupType.put(IGTEntity.ENTITY_CONNECTION_SETUP_PARAM,      new Integer(IGTManager.MANAGER_CONNECTION_SETUP_RESULT));
    _lookupType.put(IGTEntity.ENTITY_GRIDNODE,                    new Integer(IGTManager.MANAGER_GRIDNODE));
    _lookupType.put(IGTEntity.ENTITY_JMS_ROUTER,                  new Integer(IGTManager.MANAGER_JMS_ROUTER));
    _lookupType.put(IGTEntity.ENTITY_FILE_TYPE,                   new Integer(IGTManager.MANAGER_FILE_TYPE));
    _lookupType.put(IGTEntity.ENTITY_ACTIVATION_RECORD,           new Integer(IGTManager.MANAGER_ACTIVATION_RECORD));
    _lookupType.put(IGTEntity.ENTITY_GRIDNODE_ACTIVATION,         new Integer(IGTManager.MANAGER_ACTIVATION_RECORD));
    _lookupType.put(IGTEntity.ENTITY_PROCESS_MAPPING,             new Integer(IGTManager.MANAGER_PROCESS_MAPPING));
    _lookupType.put(IGTEntity.ENTITY_PROCESS_DEF,                 new Integer(IGTManager.MANAGER_PROCESS_DEF));
    _lookupType.put(IGTEntity.ENTITY_PROCESS_ACT,                 new Integer(IGTManager.MANAGER_PROCESS_DEF));
    _lookupType.put(IGTEntity.ENTITY_ATTACHMENT,                  new Integer(IGTManager.MANAGER_ATTACHMENT));
    _lookupType.put(IGTEntity.ENTITY_RFC,                         new Integer(IGTManager.MANAGER_RFC));
    _lookupType.put(IGTEntity.ENTITY_PORT,                        new Integer(IGTManager.MANAGER_PORT));
    _lookupType.put(IGTEntity.ENTITY_BIZ_CERT_MAPPING,            new Integer(IGTManager.MANAGER_BIZ_CERT_MAPPING)); //20030114AH
    _lookupType.put(IGTEntity.ENTITY_ALERT,                       new Integer(IGTManager.MANAGER_ALERT));
    _lookupType.put(IGTEntity.ENTITY_ACTION,                      new Integer(IGTManager.MANAGER_ACTION));
    _lookupType.put(IGTEntity.ENTITY_MESSAGE_TEMPLATE,            new Integer(IGTManager.MANAGER_MESSAGE_TEMPLATE));
    _lookupType.put(IGTEntity.ENTITY_RESPONSE_TRACK_RECORD,       new Integer(IGTManager.MANAGER_RESPONSE_TRACK_RECORD));
    _lookupType.put(IGTEntity.ENTITY_REMINDER_ALERT,              new Integer(IGTManager.MANAGER_RESPONSE_TRACK_RECORD));
    _lookupType.put(IGTEntity.ENTITY_ALERT_TYPE,                  new Integer(IGTManager.MANAGER_ALERT_TYPE));
    _lookupType.put(IGTEntity.ENTITY_ALERT_CATEGORY,              new Integer(IGTManager.MANAGER_ALERT_CATEGORY));
    _lookupType.put(IGTEntity.ENTITY_ALERT_TRIGGER,               new Integer(IGTManager.MANAGER_ALERT_TRIGGER)); //20030506AH
    _lookupType.put(IGTEntity.ENTITY_ARCHIVE_DOCUMENT,            new Integer(IGTManager.MANAGER_ARCHIVE_DOCUMENT)); //20030517AH
    _lookupType.put(IGTEntity.ENTITY_RESTORE_DOCUMENT,            new Integer(IGTManager.MANAGER_ARCHIVE_DOCUMENT)); //20030522AH
    _lookupType.put(IGTEntity.ENTITY_EXPORT_CONFIG,               new Integer(IGTManager.MANAGER_EXPORT_CONFIG)); //20030528AH
    _lookupType.put(IGTEntity.ENTITY_IMPORT_CONFIG,               new Integer(IGTManager.MANAGER_EXPORT_CONFIG)); //20030528AH
    _lookupType.put(IGTEntity.ENTITY_SOAP_PROCEDURE,              new Integer(IGTManager.MANAGER_USER_PROCEDURE)); //20030731AH
    _lookupType.put(IGTEntity.ENTITY_PROCESS_INSTANCE,            new Integer(IGTManager.MANAGER_PROCESS_INSTANCE)); //20030825AH
    _lookupType.put(IGTEntity.ENTITY_REGISTRY_CONNECT_INFO,       new Integer(IGTManager.MANAGER_REGISTRY_CONNECT_INFO));
    _lookupType.put(IGTEntity.ENTITY_SEARCH_REGISTRY_CRITERIA,    new Integer(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY));
    _lookupType.put(IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY,       new Integer(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY));
    _lookupType.put(IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY,     new Integer(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY));
    _lookupType.put(IGTEntity.ENTITY_SEARCHED_BUSINESS_ENTITY,    new Integer(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY)); // 20030915 DDJ
    _lookupType.put(IGTEntity.ENTITY_SEARCHED_CHANNEL_INFO,       new Integer(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY)); // 20031010 NSL
    _lookupType.put(IGTEntity.ENTITY_CONDITION,                   new Integer(IGTManager.MANAGER_SEARCH_QUERY));
    _lookupType.put(IGTEntity.ENTITY_SEARCH_QUERY,                new Integer(IGTManager.MANAGER_SEARCH_QUERY)); // 20031027 DDJ
    _lookupType.put(IGTEntity.ENTITY_AS2_PACKAGING_INFO_EXTENSION,new Integer(IGTManager.MANAGER_PACKAGING_INFO)); // 20031121 DDJ
    _lookupType.put(IGTEntity.ENTITY_FLOW_CONTROL_INFO,           new Integer(IGTManager.MANAGER_CHANNEL_INFO)); // 20031222 DDJ
    _lookupType.put(IGTEntity.ENTITY_HOUSE_KEEPING,               new Integer(IGTManager.MANAGER_HOUSE_KEEPING)); // 20040114 DDJ
    _lookupType.put(IGTEntity.ENTITY_DOMAIN_IDENTIFIER,           new Integer(IGTManager.MANAGER_BUSINESS_ENTITY)); // 20040102 DDJ
    _lookupType.put(IGTEntity.ENTITY_SCHEDULED_TASK,              new Integer(IGTManager.MANAGER_SCHEDULED_TASK)); // 20040212 NSL
    _lookupType.put(IGTEntity.ENTITY_SCHEDULED_ARCHIVE,           new Integer(IGTManager.MANAGER_SCHEDULED_ARCHIVE));
    _lookupType.put(IGTEntity.ENTITY_ARCHIVE,                     new Integer(IGTManager.MANAGER_ARCHIVE)); 
    _lookupType.put(IGTEntity.ENTITY_WEBSERVICE,                  new Integer(IGTManager.MANAGER_WEBSERVICE)); // 20040209 MAHESH    
    _lookupType.put(IGTEntity.ENTITY_SERVICE_ASSIGNMENT,          new Integer(IGTManager.MANAGER_SERVICE_ASSIGNMENT)); // 20040209 MAHESH
    _lookupType.put(IGTEntity.ENTITY_EMAIL_CONFIG,                new Integer(IGTManager.MANAGER_EMAIL_CONFIG)); // 20040701 MAHESH
    _lookupType.put(IGTEntity.ENTITY_VIRTUAL_LOOKUP_PROPERTIES,   new Integer(IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES));
    _lookupType.put(IGTEntity.ENTITY_ES_DOC,                			new Integer(IGTManager.MANAGER_ES_DOC)); // 20051008 SUMEDH
    _lookupType.put(IGTEntity.ENTITY_ES_PI,                			  new Integer(IGTManager.MANAGER_ES_PI)); // 20051008 SUMEDH
    _lookupType.put(IGTEntity.ENTITY_FIELD_VALUE_COLLECTION,      new Integer(IGTManager.MANAGER_FIELD_VALUE_COLLECTION)); // 20051020 SUMEDH
    _lookupType.put(IGTEntity.ENTITY_AUDIT_FILE,                	new Integer(IGTManager.MANAGER_AUDIT_FILE)); // 20051129 SUMEDH
    _lookupType.put(IGTEntity.ENTITY_MESSAGE_PROPERTY,            new Integer(IGTManager.MANAGER_MESSAGE_TEMPLATE)); 
    _lookupType.put(IGTEntity.ENTITY_JMS_DESTINATION,             new Integer(IGTManager.MANAGER_JMS_DESTINATION)); 
    _lookupType.put(IGTEntity.ENTITY_IMPORT_SCHEMA,               new Integer(IGTManager.MANAGER_IMPORT_SCHEMA));
    _lookupType.put(IGTEntity.ENTITY_SCHEMA_MAPPING_FILE,         new Integer(IGTManager.MANAGER_SCHEMA_MAPPING_FILE));
    _lookupType.put(IGTEntity.ENTITY_GDOC_DETAIL,                 new Integer(IGTManager.MANAGER_GDOC_DETAIL)); //20061102 REGINA ZENG
  }
}
