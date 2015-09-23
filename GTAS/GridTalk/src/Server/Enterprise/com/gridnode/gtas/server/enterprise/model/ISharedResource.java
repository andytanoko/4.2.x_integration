/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISharedResource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

/**
 * This interface defines the the properties and FieldIds for accessing fields
 * in SharedResource entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface ISharedResource
{
  /**
   * Name of the SharedResource entity.
   */
  public static final String ENTITY_NAME = "SharedResource";

  /**
   * FieldId for UID of a SharedResource entity instance. A Long.
   */
  public static final Number UID         = new Integer(0);  //Long

  /**
   * FieldId for ID of the Enterprise that the resource is shared to.
   * A String.
   */
  public static final Number TO_ENTERPRISE_ID = new Integer(1);  //String(20)

  /**
   * FieldId for UID of the resource that is shared.
   * A Long.
   */
  public static final Number RESOURCE_UID  = new Integer(2);  //Long

  /**
   * FieldId for ResourceType of resource that is shared.
   * A String(50).
   */
  public static final Number RESOURCE_TYPE   = new Integer(3);  //String(50)

  /**
   * FieldId for the State of the SharedResource.
   * A Short.
   */
  public static final Number STATE     = new Integer(4);  //Short.

  /**
   * FieldId for SyncCheckSum of the shared resource.
   * A Long.
   */
  public static final Number SYNC_CHECKSUM  = new Integer(5);  //Long

  /**
   * FieldId for CanDelete flag of a SharedResource instance. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(6);  //Boolean

  /**
   * FieldId for Version of a SharedResource instance, A Double.
   */
  public static final Number VERSION     = new Integer(7);  //Double


  // Values for STATE
  /**
   * Unsync state: The shared resource is not synchronized with destination.
   */
  public static final short STATE_UNSYNC  = 0;
  /**
   * Sync state: The shared resource is sychronized with destination.
   */
  public static final short STATE_SYNC    = 1;
  /**
   * Deleted state: The shared resource has been "unshared".
   */
  public static final short STATE_DELETED = 2;
}
