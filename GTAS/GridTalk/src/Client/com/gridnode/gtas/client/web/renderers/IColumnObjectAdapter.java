/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IColumnObjectAdapter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created (IColumnEntityAdapter)
 * 2002-09-04     Andrew Hill         Made generic as IColumnObjectAdapter
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;
 
public interface IColumnObjectAdapter
{
  /**
   * Returns the number of columns
   */
  public int getSize();

  /**
   * Returns the value for the specified column in specified entity
   * @param object
   * @param column
   */
  public Object getColumnValue(Object object, int column) throws GTClientException;

  /**
   * Returns the value (in String) for the specified column in specified entity
   * @param object
   * @param column
   */
  /*public String getColumnString(Object object, int column) throws GTClientException;*/

  /**
   * Return the field label for specified column
   * @param column
   */
  //public String getColumnLabel(Object object, int column);
  public String getColumnLabel(int column) throws GTClientException;
}