/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-04     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in AlertType entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAlertType
{
  /**
   * Name for AlertType entity.
   */
  public static final String  ENTITY_NAME = "AlertType";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // String 80

  /**
   * FieldId for Description.
   */
  public static final Number DESCRIPTION = new Integer(2); // String 255
}