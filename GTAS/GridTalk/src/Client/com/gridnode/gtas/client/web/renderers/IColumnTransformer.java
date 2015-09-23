/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IColumnTransformer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-09-06     Andrew Hill         Modified to take Object params
 */
package com.gridnode.gtas.client.web.renderers;

 
public interface IColumnTransformer
{
  public Object getTransformedValue(Object object, int column, Object field, Object value);
}