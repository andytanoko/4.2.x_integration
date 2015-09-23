/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Filter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-18     Andrew Hill         Created
 */
package com.gridnode.gtas.client.utils;

import com.gridnode.gtas.client.GTClientException;

public abstract class Filter implements IFilter
{
  
  public boolean allows(Object object, Object context) throws GTClientException
  {
    return true;
  }

}