/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGnCategory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.gridnode;

/**
 * This interface defines the field IDs and constants for GnCategory entity.
 * <p>This is a subset of the IGnCategory interface defines under the
 * <code>com.gridnode.gtas.server.gridnode.model</code> package.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IGnCategory
{
  /**
   * Name of the GnCategory entity.
   */
  static final String ENTITY_NAME = "GnCategory";

  /**
   * FieldID for CategoryCode. A String.
   */
  static final Number CATEGORY_CODE = new Integer(0); //String(3)

  /**
   * FieldID for CategoryName. A String.
   */
  static final Number CATEGORY_NAME = new Integer(1); //String(50)

}