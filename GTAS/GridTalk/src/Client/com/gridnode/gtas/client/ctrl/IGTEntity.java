/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-05-29     Daniel D'Cotta      Added entities for GridForm
 * 2002-08-22     Andrew Hill         Modified get & set methods for fields
 * 2002-11-13     Andrew Hill         getFieldContainer()
 * 2003-07-08     Andrew Hill         Remove GF entity type defs
 * 2003-10-10     Neo Sok Lay         Add searchedChannelInfo entity type.
 * 2004-02-11     Neo Sok Lay         Add scheduledTask entity type.
 * 2005-09-16     Sumedh Chalermkanjana     Added field: ENTITY_ESTORE_SEARCH
 * 2005-12-28			SC									Add ENTITY_JMS_DESTINATION field.
 * 2006-01-03			SC									Add ENTITY_VIRTUAL_LOOKUP_PROPERTIES field.
 * 2006-03-10     Tam Wei Xiang       Added entity for ImportSchema, SchemaMapping
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream.
 * 2006-09-27     Tam Wei Xiang       Added entity for SearchEsPI, SearchEsDoc
 * 2006-11-14     Regina Zeng         Added entity for gdocDetail
 * 2008-06-26	Yee Wah, Wong	      #38  Added entity for CertificateSwapping
 * 2008-10-17     Wong Yee Wah        #80  Added entity for AS2DocTypeMapping
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

/**
 * A GTEntity is a data acces object that holds a local copy of the information for a GridTalk entity.
 * GTEntity updates are transmitted/received from gtas via the associated manager.
 * These values may be modified, but for the change to take effect in GridTalk a GTManager must be
 * used to update gridtalk with the new data.
 * As this is essentially just a value object, changes made to one GTEntity instance related
 * to a particular object in GridTalk will not be reflected in other GTEntity objects that also
 * relate to that particular object.
 */
public interface IGTEntity
{
  //@todo: factor out all the types into a seperate interface. Not technically problematic
  //but will take forever due to the number of references! 20030509AH

  /**
   * Type identifier for a User entity.
   * Subinterface: IGTUserEntity
   */
  public static final String ENTITY_USER = "user";

  /**
   * Type identifier for a User Profile entity.
   * Subinterface: IGTUserProfileEntity
   */
  //public static final String ENTITY_USER_PROFILE = "userProfile";
  public static final String ENTITY_ACCOUNT_STATE = "accountState"; //20020624AH

  /**
   * Type identifier for a GF Definition entity.
   * Subinterface: IGFDefinitionEntity
   */
  public static final String ENTITY_GF_DEFINITION = "definition";

  /**
   * Type identifier for a GF Template entity.
   * Subinterface: IGFTemplateEntity
   */
  //20030708AH - co: public static final String ENTITY_GF_TEMPLATE = "template";

  public static final String ENTITY_ROLE = "role";

  public static final String ENTITY_ACCESS_RIGHT = "accessRight"; //20020726AH corrected spelling

  public static final String ENTITY_FEATURE = "feature";

  public static final String ENTITY_PARTNER_TYPE = "partnerType";

  public static final String ENTITY_PARTNER_GROUP = "partnerGroup";

  public static final String ENTITY_PARTNER = "partner";

  public static final String ENTITY_DOCUMENT_TYPE = "documentType";
  
  public static final String ENTITY_AS2_DOC_TYPE_MAPPING = "as2DocTypeMapping";

  public static final String ENTITY_GRIDTALK_MAPPING_RULE = "gridTalkMappingRule";

  public static final String ENTITY_MAPPING_RULE = "mappingRule"; // these are embedded in GridTalkMappingRules

  public static final String ENTITY_MAPPING_FILE = "mappingFile";
  
  //added by ming qian
  public static final String ENTITY_MAPPING_CLASS = "mappingClass";
  //end of added by ming qian

  public static final String ENTITY_CHANNEL_INFO = "channelInfo";

  public static final String ENTITY_COMM_INFO = "commInfo";

  public static final String ENTITY_BUSINESS_ENTITY = "businessEntity";

  public static final String ENTITY_WHITE_PAGE  = "whitePage";

  public static final String ENTITY_FILE_TYPE = "fileType";

  public static final String ENTITY_GRID_DOCUMENT = "gridDocument";

  public static final String ENTITY_IMPORT_DOCUMENTS = "importDocuments"; // first of the virtual entities

  public static final String ENTITY_COMPANY_PROFILE = "coyProfile";

  public static final String ENTITY_GN_CATEGORY = "gnCategory";

  public static final String ENTITY_COUNTRY_CODE = "countryCode";

  public static final String ENTITY_LANGUAGE_CODE = "languageCode";

  public static final String ENTITY_SECURITY_INFO = "securityInfo";

  public static final String ENTITY_PACKAGING_INFO = "packagingInfo";

  public static final String ENTITY_CERTIFICATE = "certificate";
  
  public static final String ENTITY_CERTIFICATE_SWAPPING = "certificateSwapping";

  public static final String ENTITY_PARTNER_FUNCTION = "partnerFunction";

  public static final String ENTITY_WORKFLOW_ACTIVITY = "workflowActivity";

  public static final String ENTITY_REGISTRATION_INFO = "registrationInfo";

  public static final String ENTITY_ACTIVATION_RECORD = "activationRecord";

  public static final String ENTITY_GRIDNODE_ACTIVATION = "gridNodeActivation";

  public static final String ENTITY_SEARCHED_GRIDNODE = "searchedGridNode";

  public static final String ENTITY_SEARCH_GRIDNODE_CRITERIA = "searchGridNodeCriteria";

  public static final String ENTITY_SEARCH_GRIDNODE_QUERY = "searchGridNodeQuery";

  public static final String ENTITY_TRIGGER = "trigger";

  public static final String ENTITY_PROCEDURE_DEF_FILE = "procedureDefFile";

  public static final String ENTITY_CONNECTION_SETUP_PARAM = "connectionSetupParam";

  public static final String ENTITY_CONNECTION_SETUP_RESULT = "connectionSetupResult";

  public static final String ENTITY_NETWORK_SETTING = "networkSetting";

  public static final String ENTITY_JMS_ROUTER = "jmsRouter";

  public static final String ENTITY_USER_PROCEDURE = "userProcedure";

  public static final String ENTITY_SHELL_EXECUTABLE = "shellExecutable";

  public static final String ENTITY_JAVA_PROCEDURE = "javaProcedure";

  public static final String ENTITY_PARAM_DEF = "paramDef";

  public static final String ENTITY_RETURN_DEF = "returnDef";

  public static final String ENTITY_GRIDNODE = "gridNode";

  public static final String ENTITY_PROCESS_MAPPING = "processMapping";

  public static final String ENTITY_PROCESS_DEF = "processDef";

  public static final String ENTITY_PROCESS_ACT = "processAct";

  public static final String ENTITY_ATTACHMENT = "attachment";

  public static final String ENTITY_RFC = "rfc";

  public static final String ENTITY_PORT = "port";

  public static final String ENTITY_BIZ_CERT_MAPPING = "bizCertMapping"; //20030114AH

  public static final String ENTITY_X500NAME = "x500Name"; //20030117AH

  public static final String ENTITY_PROCESS_INSTANCE = "processInstance";

  public static final String ENTITY_ALERT = "alert";

  public static final String ENTITY_ACTION = "action";

  public static final String ENTITY_MESSAGE_TEMPLATE = "messageTemplate";

  public static final String ENTITY_RESPONSE_TRACK_RECORD = "responseTrackRecord";

  public static final String ENTITY_REMINDER_ALERT = "reminderAlert";

  public static final String ENTITY_ALERT_TYPE = "alertType";

  public static final String ENTITY_ALERT_CATEGORY = "alertCategory";

  public static final String ENTITY_ALERT_TRIGGER = "alertTrigger"; //20030506AH

  public static final String ENTITY_ARCHIVE_DOCUMENT = "archiveDocument"; //20030517AH
  
  public static final String ENTITY_SCHEDULED_ARCHIVE = "scheduledArchive"; //
  
  public static final String ENTITY_ARCHIVE = "archiveMetaInfo";  //

  public static final String ENTITY_RESTORE_DOCUMENT = "restoreDocument"; //20030522AH

  public static final String ENTITY_EXPORT_CONFIG = "exportConfig"; //20030528AH

  public static final String ENTITY_IMPORT_CONFIG = "importConfig"; //20030528AH

  public static final String ENTITY_SOAP_PROCEDURE = "soapProcedure"; //20030730AH

  public static final String ENTITY_REGISTRY_CONNECT_INFO = "registryConnectInfo";

  public static final String ENTITY_PUBLISH_BUSINESS_ENTITY = "publishBusinessEntity";

  public static final String ENTITY_SEARCHED_BUSINESS_ENTITY = "searchedBusinessEntity";

  public static final String ENTITY_SEARCH_REGISTRY_CRITERIA = "searchRegistryCriteria";

  public static final String ENTITY_SEARCH_REGISTRY_QUERY = "searchRegistryQuery"; // 20030915 DDJ

  public static final String ENTITY_AS2_PACKAGING_INFO_EXTENSION = "as2PackagingInfoExtension"; // 20031120 DDJ

  public static final String ENTITY_SEARCHED_CHANNEL_INFO = "searchedChannelInfo"; // 20031010NSL

  public static final String ENTITY_CONDITION = "condition";

  public static final String ENTITY_SEARCH_QUERY = "searchQuery"; // 20031027 DDJ

  public static final String ENTITY_FLOW_CONTROL_INFO = "flowControlInfo"; // 20031010 DDJ

  public static final String ENTITY_HOUSE_KEEPING = "houseKeeping"; // 20040114 DDJ

  public static final String ENTITY_DOMAIN_IDENTIFIER = "domainIdentifier"; // 20040102 DDJ

  public static final String ENTITY_SCHEDULED_TASK = "scheduledTask"; //20040211NSL
  
  public static final String ENTITY_WEBSERVICE = "webService"; //20040209 Mahesh
  
  public static final String ENTITY_SERVICE_ASSIGNMENT = "serviceAssignment"; //20040209 Mahesh
  
  public static final String ENTITY_EMAIL_CONFIG = "emailConfig"; //20040701 Mahesh
  
  public static final String ENTITY_ES_PI = "EsPi";

  public static final String ENTITY_ES_DOC = "EsDoc";
  
  public static final String ENTITY_AUDIT_FILE = "AuditFileInfo";	//20051012 Sumedh
  public static final String ENTITY_FIELD_VALUE_COLLECTION = "FieldValueCollection"; //20051020 Sumedh
  
  public static final String ENTITY_JMS_DESTINATION = "jmsDestination";
  
  public static final String ENTITY_VIRTUAL_LOOKUP_PROPERTIES = "virtualLookupProperties";
  
  public static final String ENTITY_MESSAGE_PROPERTY = "messageProperty";
  
  public static final String ENTITY_IMPORT_SCHEMA = "importSchema";
  
  public static final String ENTITY_SCHEMA_MAPPING_FILE = "schemaMapping";
  
  public static final String ENTITY_SEARCH_ES_PI_DOCUMENT = "searchEsPi"; //old estorePiDocument 21092006 TWX
  
  public static final String ENTITY_SEARCH_ES_DOC_DOCUMENT = "searchEsDoc"; //27092006 TWX
  
  public static final String ENTITY_GDOC_DETAIL = "gdocDetail"; //14112006 RZ
  
  /**
   * Return the type of this entity
   * @return entity type
   */
  public String getType();

  /**
   * Sets the value in this object for the specified field.
   * (To update gridtalk server with any changes, use IGTManager.update() method)
   * @param field id as defined in IGTEntity subinterface
   * @param value the new value for the field
   */
  public void setFieldValue(Number field, Object value) throws GTClientException;
  public void setFieldValue(String fieldName, Object value) throws GTClientException;

  /**
   * Retrieve the value for the specified field.
   * Objects implemting IGTEntity will *mostly* return null for non-existent fields however
   * some may return other values or throw exceptions. Check documentation for specific entity
   * type.
   * @param fieldId as defined in IGTEntity subinterface
   */
  public Object getFieldValue(Number field) throws GTClientException;
  public Object getFieldValue(String fieldName) throws GTClientException;

  /**
   * Returns a string representation of the field value using Object.toString()
   * nb: null fields return an empty string and _not_ "null".
   * @param fieldId
   */
  public String getFieldString(Number field) throws GTClientException;
  public String getFieldString(String fieldName) throws GTClientException;

  public String[] getFieldStringArray(Number field) throws GTClientException;
  public String[] getFieldStringArray(String fieldName) throws GTClientException;

  /**
   * Return an array of IGTFieldMetaInfo objects for all fields supported by this entity.
   * Note that metaInfo for a different entity of the sdame type may not be identical as it
   * may be partially determined by object state and the implementing subclass.
   * The position of a fields metainfo in the array is NOT its field number.
   * @return array of all fields meta info for this particular instance of this entity type.
   */
  public IGTFieldMetaInfo[] getFieldMetaInfo() throws GTClientException;

  /**
   * Returns the meta info for the specified field of this instance of this entity type.
   * @param fieldId
   * @return fieldMetaInfo object for the specified field
   */
  public IGTFieldMetaInfo getFieldMetaInfo(Number field) throws GTClientException;
  public IGTFieldMetaInfo getFieldMetaInfo(String fieldName) throws GTClientException;

  /**
   * Convienience method to retrieve the UID of this entity as a primitive long.
   * (Usually) Equivalent to ((Long)getFieldValue(IGTXXEntity.UID)).longValue()
   * @return uid
   */
  public long getUid();

  /**
   * @deprecated use getUid() instead
   */
  public long getUID();

  /**
   * Convienience method to retrieve the UID of this entity.
   * @return uid
   */
  public Long getUidLong();

  /**
   * For entities that were not obtained from or written to GridTalk yet this will return true;
   * @return true if newly created entity
   */
  public boolean isNewEntity();

  /**
   * Given the fieldId returns a human-readable fieldName that may be used
   * to identify getter and setter methods in related beans and forms etc...
   */
  public String getFieldName(Number fieldId) throws GTClientException;

  public Number getFieldId(String fieldName) throws GTClientException;

  public IGTSession getSession();

  public boolean isFromCache();

  public Collection getFieldEntities(Number fieldId) throws GTClientException;

  public boolean isFieldDirty(Number fieldId);

  public boolean isEntityDirty();

  public boolean isVirtualEntity();

  public boolean canDelete() throws GTClientException;

  public boolean canEdit() throws GTClientException;

  /**
   * Returns the entity that contains the specified field - taking into account embedded entities.
   */
  public IGTEntity getFieldContainer(String fieldName)
    throws GTClientException;

  public IGTEntity getFieldContainer(Number fieldId)
    throws GTClientException;
}
