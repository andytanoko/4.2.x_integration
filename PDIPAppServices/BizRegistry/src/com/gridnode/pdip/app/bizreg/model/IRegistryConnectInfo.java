/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryConnectInfo.java
 * Moved from GTAS/GridTalk/BizRegistry
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.model;

/**
 * This interface defines the fields of the RegistryConnectInfo
 * entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryConnectInfo
{
  /**
   * The name of the RegistryConnectInfo entity.
   */
  static final String ENTITY_NAME = "RegistryConnectInfo";
  
  /**
   * FieldId for the Name field. A String.
   */
  static final Number NAME = new Integer(0);
  
  /**
   * FieldId for the QueryUrl field. A String.
   */
  static final Number QUERY_URL = new Integer(1);
  
  /**
   * FieldId for the PublishUrl field. A String.
   */
  static final Number PUBLISH_URL = new Integer(2);
  
  /**
   * FieldId for the PublishUser field. A String.
   */
  static final Number PUBLISH_USER = new Integer(3);
  
  /**
   * FieldId for the PublishPassword field. A String.
   */
  static final Number PUBLISH_PASSWORD = new Integer(4);
 
  /**
   * FieldId for the uId field. A Long.
   */
  static final Number UID = new Integer(5);  
  
  /**
   * FieldId for the CanDelete field. A Boolean.
   */
  static final Number CAN_DELETE = new Integer(6);
  
  /**
   * FieldId for the Version field. A Double.
   */
  static final Number VERSION = new Integer(7);
}
