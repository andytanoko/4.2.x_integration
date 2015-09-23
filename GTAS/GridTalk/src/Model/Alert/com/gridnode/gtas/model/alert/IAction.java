/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Action entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAction
{
  /**
   * Name for Action entity.
   */
  public static final String  ENTITY_NAME = "Action";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // String 30

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(2); // string 50

  /**
   * FieldId for Message UId.
   */
  public static final Number MSG_UID = new Integer(3); // tinyint
}