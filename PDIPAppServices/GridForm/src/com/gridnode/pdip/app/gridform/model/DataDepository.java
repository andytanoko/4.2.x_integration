/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataDepository.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Implements Serializable.
 */
package com.gridnode.pdip.app.gridform.model;

import com.gridnode.pdip.app.gridform.exceptions.InvalidAccessException;

import java.io.Serializable;

/**
 * Abstract depository that relates to or mirrors a data source.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public abstract class DataDepository implements Serializable
{
  /**
   * Get the identification name of this depository.
   *
   * @return The identification name of this depository.
   * @since 2.0
   */
  public abstract Object getID();

  /**
   * Generic setter method.
   *
   * @param identifier The key where the value is mapped to.
   * @param value The value object.
   * @since 2.0
   */
  public abstract void setValue(Object identifier, Object value) throws InvalidAccessException;

  /**
   * Generic getter method.
   *
   * @param identifier The key where the value is mapped to.
   * @return The value indicated by the identifier.
   * @since 2.0
   */
  public abstract Object getValue(Object identifier) throws InvalidAccessException;
}