/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFTemplate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.pdip.app.gridform.model;

/**
 * Interface that defines the properties and FieldIds for accessing fields
 * in the GFTemplate entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public interface IGFTemplate
{
  /**
   * Name for GFTemplate entity.
   */
  public static final String ENTITY_NAME  = "GFTemplate";

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
}