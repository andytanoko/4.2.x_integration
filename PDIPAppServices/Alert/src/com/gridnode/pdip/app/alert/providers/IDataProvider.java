/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDataProvider.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 16 May 2003    Neo Sok Lay         Get field with a pattern for formatting.
 */

package com.gridnode.pdip.app.alert.providers;


public interface IDataProvider
{
  public static final String TYPE_ENTITY   = "Entity";
  public static final String TYPE_NORMAL   = "Normal";
  //public static final String TYPE_PATTERN  = "Pattern";

  /**
   * Get the value for a field
   *
   * @param            fieldName name of the field
   * @return           the value
   */
  public String get(String fieldName);

  /**
   * Get the value for a field, formatted with a pattern.
   *
   * @param fieldName The name of the field
   * @param pattern The pattern to format the field.
   */
  public String get(String fieldName, String pattern);

  /**
   * The name of this <code>IDataProvider</code> class
   *
   * @return           name of this provider
   * @since            1.2
   */
  public String getName();

  public String getType();

}