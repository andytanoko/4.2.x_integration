/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Activity entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IActivity
{
  /**
   * Name for Activity entity.
   */
  public static final String ENTITY_NAME = "Activity";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID         = new Integer(0);  //Integer

  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(2); //Double

  /**
   * FieldId for ActivityType. A String.
   */
  public static final Number ACTIVITY_TYPE    = new Integer(3);  //String

  /**
   * FieldId for Description. A String.
   */
  public static final Number DESCRIPTION  = new Integer(4);  //String

  /**
   * FieldId for DateTime. A Date.
   */
  public static final Number DATE_TIME = new Integer(5);  //Date

}