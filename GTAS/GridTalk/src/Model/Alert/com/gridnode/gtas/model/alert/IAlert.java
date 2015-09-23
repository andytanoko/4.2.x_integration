/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlert.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Add BIND_ACTIONS_UIDS.
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Alert entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAlert
{
  /**
   * Name for Alert entity.
   */
  public static final String  ENTITY_NAME = "Alert";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // String 80

  /**
   * FieldId for Type.
   */
  public static final Number TYPE = new Integer(2); // int 1

  /**
   * FieldId for Category Code.
   */
  public static final Number CATEGORY = new Integer(3); // string 20

  /**
   * FieldId for Trigger.
   */
  public static final Number TRIGGER = new Integer(4); // Long

  /**
   * FieldId for Description.
   */
  public static final Number DESCRIPTION = new Integer(5); // string 255

  /**
   * FieldId for BindActions. A Collection of Long (UIDs). Not persistent.
   */
  public static final Number BIND_ACTIONS_UIDS = new Integer(6); //Collection


}