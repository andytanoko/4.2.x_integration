/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryObjectMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of a RegistryObjectMapping
 * entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryObjectMapping
{
  /**
   * Name of the RegistryObjectMapping entity.
   */
  static final String ENTITY_NAME = "RegistryObjectMapping";
  
  /**
   * FieldId for the UID field. A Long.
   */
  static final Number UID = new Integer(0);
  
  /**
   * FieldId for the RegistryObjectKey. A String.
   */
  static final Number REGISTRY_OBJECT_KEY = new Integer(1);
  
  /**
   * FieldId for the RegistryObjectType. An Integer.
   */
  static final Number REGISTRY_OBJECT_TYPE = new Integer(2);
  
  /**
   * FieldId for the RegistryQueryUrl. A String.
   */
  static final Number REGISTRY_QUERY_URL = new Integer(3);
  
  /**
   * FieldId for the RegistryPublishUrl. A String.
   */
  static final Number REGISTRY_PUBLISH_URL = new Integer(4);
  
  /**
   * FieldId for the ProprietaryObjectType. A String.
   */
  static final Number PROPRIETARY_OBJECT_TYPE = new Integer(5);
  
  /**
   * FieldId for the ProprietaryObjectKey. A String.
   */
  static final Number PROPRIETARY_OBJECT_KEY = new Integer(6);
  
  /**
   * FieldId for the State field. A Short.
   */
  static final Number STATE = new Integer(7);
  
  /**
   * FieldId for the IsPublishedObject field. A Boolean.
   */
  static final Number IS_PUBLISHED_OBJECT = new Integer(8);
  
  /**
   * State field value: Synchronized.
   * This indicates that the registry information object
   * is synchronized in the public registry. This is
   * the default state value for a newly created RegistryObjectMapping
   * entity. 
   */
  static final short STATE_SYNCHRONIZED = 0;
  
  /**
   * State field value: DeletedFromRegistry.
   * This indicates that the registry information object
   * has been deleted from the public registry.
   */
  static final short STATE_DELETED_FROM_REGISTRY = 1;
  
  /**
   * State field value: PendingDelete.
   * This indicates that the registry information object
   * is pending deletion from the public registry.
   */
  static final short STATE_PENDING_DELETE = 2;
  
  /**
   * State field value: PendingUpdate.
   * This indicates that the registry information object
   * is pending update to the public registry.
   */
  static final short STATE_PENDING_UPDATE = 3;
  
}
