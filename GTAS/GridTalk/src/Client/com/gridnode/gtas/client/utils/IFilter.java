/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-01     Andrew Hill         Created
 */
package com.gridnode.gtas.client.utils;

import com.gridnode.gtas.client.GTClientException;

/**
 * Simple filter interface.
 */
public interface IFilter
{
  /**
   * Passed an object will return true if this object is allowed through the filter, false otherwise.
   * @param object the objetc to be filtered
   * @param context parameter for extra context sentitive data (its meaning is not defined by this interface)
   * @returns boolean
   * @throws GTClientException
   */
  public boolean allows(Object object, Object context) throws GTClientException;
}