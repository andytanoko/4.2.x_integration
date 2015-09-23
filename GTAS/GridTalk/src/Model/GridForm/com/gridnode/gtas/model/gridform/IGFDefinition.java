/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFDefinition.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Daniel D'Cotta      Created.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.gtas.model.gridform;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in GFDefinition entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public interface IGFDefinition
{
  /**
   * Name for GFDefinition entity.
   */
  public static final String ENTITY_NAME  = "GFDefinition";

  /**
   * FieldId for UID (Number).
   */
  public static final Number UID          = new Integer(0);

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION      = new Integer(1);

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE   = new Integer(2);
  /**
   * FieldId for Name (String, len = 50).
   */
  public static final Number NAME         = new Integer(3);

  /**
   * FieldId for filename (String, len = 80).
   */
  public static final Number FILENAME     = new Integer(4);

  /**
   * FieldId for UID of a GFTemplate entity ({@link com.gridnode.pdip.app.gridform.model.GFTemplate GFTemplate}).
   */
  public static final Number TEMPLATE     = new Integer(5);
}