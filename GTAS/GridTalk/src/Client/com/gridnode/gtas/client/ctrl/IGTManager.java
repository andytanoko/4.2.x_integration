/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-05-29     Daniel D'Cotta      Added managers for GridForm
 * 2003-01-15     Andrew Hill         Exposed 2 getByUid() variants
 * 2003-03-20     Andrew Hill         getListPager();
 * 2003-05-29     Andrew Hill         getUidFieldId()
 * 2003-07-08     Andrew Hill         Remove GF manager type defs
 * 2003-10-19     Daniel D'Cotta      Added sorting
 * 2004-02-11     Neo Sok Lay         Added manager for ScheduledTask.
 * 2005-09-16     Sumedh Chalermkanjana   Added field: MANAGER_ESTORE_SEARCH
 * 2005-12-30			SC									Add MANAGER_JMS_DESTINATION field.
 * 2006-01-03			SC									Add MANAGER_VIRTUAL_LOOKUP_PROPERTIES field.
 * 2006-03-12     Tam Wei Xiang       Added manager for ImportSchema, SchemaMapping
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream.
 * 2006-11-02     Regina Zeng         Added MANAGER_GDOC_DETAIL
 * 2008-11-12     Wong Yee Wah        Added MANAGER_AS2_DOC_TYPE_MAPPING
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Collection;

/**
 * A GTManager is used to access the entities. Each entity type will generally have a corresponding
 * GTManager. To obtain a manager use gtSession.getManager(IGTManager.XXXXXXX);
 * In addition to providing crud (create, read, update, delete) functionality the manager will
 * act as a factory for new GTEntity objects via its newEntity() method. Such new entities will
 * not exist in gtas until the create() method is used.
 */
public interface IGTManager
{
  public static final int MANAGER_USER = 1;

  //20030708AH - co: public static final int MANAGER_GF_DEFINITION = 2;

  //20030708AH - co: public static final int MANAGER_GF_TEMPLATE = 3;

  //20030708AH - co: public static final int MANAGER_GF_FORM = 4;

  public static final int MANAGER_ROLE = 5;

  public static final int MANAGER_FEATURE = 6;

  public static final int MANAGER_ACCESS_RIGHT = 7;

  public static final int MANAGER_PARTNER_TYPE  = 8;

  public static final int MANAGER_PARTNER_GROUP = 9;

  public static final int MANAGER_PARTNER = 10;

  public static final int MANAGER_DOCUMENT_TYPE = 11;

  public static final int MANAGER_GRIDTALK_MAPPING_RULE = 12;

  public static final int MANAGER_MAPPING_FILE = 13;

  public static final int MANAGER_CHANNEL_INFO = 14;

  public static final int MANAGER_COMM_INFO = 15;

  public static final int MANAGER_BUSINESS_ENTITY = 16;

  public static final int MANAGER_FILE_TYPE = 17;

  public static final int MANAGER_GRID_DOCUMENT = 18;

  public static final int MANAGER_COMPANY_PROFILE = 19;

  public static final int MANAGER_GN_CATEGORY = 20;

  public static final int MANAGER_COUNTRY_CODE = 21;

  public static final int MANAGER_LANGUAGE_CODE = 22;

  public static final int MANAGER_SECURITY_INFO = 23;

  public static final int MANAGER_PACKAGING_INFO = 24;

  public static final int MANAGER_CERTIFICATE = 25;

  public static final int MANAGER_PARTNER_FUNCTION = 26;

  public static final int MANAGER_REGISTRATION_INFO = 27;

  public static final int MANAGER_TRIGGER = 28;

  public static final int MANAGER_PROCEDURE_DEF_FILE = 29;

  public static final int MANAGER_USER_PROCEDURE = 30;

  public static final int MANAGER_SEARCH_GRIDNODE_QUERY = 31;

  public static final int MANAGER_PROCESS_DEF = 32;

  public static final int MANAGER_PROCESS_INSTANCE = 33;

  public static final int MANAGER_ALERT = 34;

  public static final int MANAGER_ACTION = 35;

  public static final int MANAGER_MESSAGE_TEMPLATE = 36;

  public static final int MANAGER_RESPONSE_TRACK_RECORD = 37;

  public static final int MANAGER_ALERT_TYPE = 38;

  public static final int MANAGER_ALERT_CATEGORY = 39;

  public static final int MANAGER_NETWORK_SETTING = 40;

  public static final int MANAGER_CONNECTION_SETUP_RESULT = 41;

  public static final int MANAGER_JMS_ROUTER = 42;

  public static final int MANAGER_GRIDNODE = 43;

  public static final int MANAGER_ACTIVATION_RECORD = 44;

  public static final int MANAGER_PROCESS_MAPPING = 50; //gap to give room for unmerged I6 stuff

  public static final int MANAGER_ATTACHMENT = 51;

  public static final int MANAGER_RFC = 52;

  public static final int MANAGER_PORT = 53;

  public static final int MANAGER_BIZ_CERT_MAPPING = 54; //20030114AH
  
  public static final int MANAGER_ALERT_TRIGGER = 55; //20030506AH
  
  public static final int MANAGER_ARCHIVE_DOCUMENT = 56; //20030517AH
  
  public static final int MANAGER_EXPORT_CONFIG = 57; //20030528AH

  public static final int MANAGER_SCHEDULED_TASK = 60; //20040211NSL 
  
  public static final int MANAGER_ARCHIVE = 61; 
  
  public static final int MANAGER_SCHEDULED_ARCHIVE = 62;

  public static final int MANAGER_REGISTRY_CONNECT_INFO = 101;
  
  public static final int MANAGER_SEARCH_REGISTRY_QUERY = 102; // 20030915 DDJ

  public static final int MANAGER_SEARCH_QUERY = 103; // 20031027 DDJ
  
  public static final int MANAGER_HOUSE_KEEPING = 201; // 20040114 DDJ

  public static final int MANAGER_WEBSERVICE = 301; //20040209 Mahesh
  
  public static final int MANAGER_SERVICE_ASSIGNMENT = 302; //20040209 Mahesh

  public static final int MANAGER_EMAIL_CONFIG = 401; //20040701 Mahesh
  public static final int MANAGER_JMS_DESTINATION = 402; 
  public static final int MANAGER_VIRTUAL_LOOKUP_PROPERTIES = 403;
  
  public static final int MANAGER_IMPORT_SCHEMA = 404; //20060312 TWX
  public static final int MANAGER_SCHEMA_MAPPING_FILE = 405; //20060314 TWX
  
  public static final int MANAGER_DO_NOTHING = 500;
  
  public static final int MANAGER_ES_PI = 501;
  
  public static final int MANAGER_ES_DOC = 502;
  
  public static final int MANAGER_AUDIT_FILE = 504;		//20051012 Sumedh
  
  public static final int MANAGER_FIELD_VALUE_COLLECTION = 503; //20051020 Sumedh
  
  public static final int MANAGER_SEARCH_ES_PI_DOCUMENT = 505; //TWX 21092006
  
  public static final int MANAGER_GDOC_DETAIL = 507; //Regina Zeng 02112006
  
  public static final int MANAGER_AS2_DOC_TYPE_MAPPING = 508; // Yee Wah 17102008
  
  /**
   * Return the entity type id for the entities this managers controls.
   */
  public int getType();

  /**
   * Create the entity in the GridTalk server.
   * @param entity an appropriately initialised new entity
   * @throws GTClientException if there was an error
   */
  public void create(IGTEntity entity) throws GTClientException;

  /**
   * Update the values in GridTalk of an existing entity.
   * (GridTalk identifies the entity to be updated by the UID field in the IGTEntity passed).
   * @param entity modified entity
   * @throws GTClientException
   */
  public void update(IGTEntity entity) throws GTClientException;

  /**
   * Delete the entity in GridTalk server.
   * If the entity does not exist in GridTalk it will not throw an exception.
   * @param entity entity to kill
   * @throws GTClientException
   */
  public void delete(IGTEntity entity) throws GTClientException;

  /**
   * Deletes a batch of entities identified by their UID numbers.
   * If the entities do not exist in GridTalk behaviour is undefined (NB: this may be different to
   * behaviour of delete(IGTEntity) method in this situation!.
   * @param long[] of uid to kill
   * @throws GTClientException
   */
  public void delete(long[] uids) throws GTClientException;

  /**
   * Get all entities of this type in GridTalk.
   * Nb: This could return a LOT of data for certain entity types that have
   * numerous instances. If possible consider using an alternative method to find the subset
   * of entities you want, such as getByKey or getListPager
   * @return Collection of all the entities of this managers entity type.
   */
  public Collection getAll() throws GTClientException;

  /**
   * Retrieve the entity specified by the uid (must be of this managers type)
   * @param long uID
   * @return entity or null if not found
   * @deprecated use getByUid() variant instead
   */
  public IGTEntity getByUID(long uID) throws GTClientException;

  //These should have been exposed a loooong time ago? Guess I forgot!
  public IGTEntity getByUid(long uid) throws GTClientException; //20030115AH
  public IGTEntity getByUid(Long uid) throws GTClientException; //20030115AH


  /**
   * Factory method to create a new IGTEntity implementing object with default values.
   * To create a new entity in GridTalk, use this method, then use the setXXX() methods of the
   * returned entity (cast it to appropriate IGTEntity subinterface) and then call the managers
   * create() method to write it to the server.
   * @return new object that implements IGTEntity and the subinterface appropriate for this entity type.
   * @throws GTClientException
   */
  public IGTEntity newEntity() throws GTClientException;

  public Number getFieldId(String entityType, String fieldName)
    throws GTClientException;

  public IGTFieldMetaInfo getSharedFieldMetaInfo(String entityType, Number fieldId)
    throws GTClientException;

  public IGTFieldMetaInfo getSharedFieldMetaInfo(String entityType, String fieldName)
    throws GTClientException;

  public IGTFieldMetaInfo[] getSharedFieldMetaInfo(String entityType)
    throws GTClientException;

  public Collection getByKey(Object key, Number keyFieldId)
    throws GTClientException;
 
  public IGTListPager getListPager() throws GTClientException; //20030320AH
  public IGTListPager getListPager(Object key, Number keyFieldId) throws GTClientException; //20030321AH
  public IGTListPager getListPager(Object key, Number keyFieldId, boolean forceHeavy) throws GTClientException; //20030321AH
  public IGTListPager getListPager(Object key, Number keyFieldId, boolean forceHeavy, Number[] sortField, boolean[] sortAscending) throws GTClientException;
  public Number getUidFieldId(String entityType) throws GTClientException; //20030529AH
}