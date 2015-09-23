/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SingleRangeonstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2003-05-20     Andrew Hill         Use MAX_VALUE, MIN_VALUE for unbounded ends of range
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class SingleRangeConstraint extends AbstractConstraint implements ISingleRangeConstraint
{
  private Number _max;
  private Number _min;

  SingleRangeConstraint(Properties detail, String valueClass)
    throws GTClientException
  {
    super(IConstraint.TYPE_RANGE, detail);
    if(valueClass == null)
    {
      throw new java.lang.NullPointerException("valueClass may not be null");
    }
    initialise(detail, valueClass);
  }

  protected void initialise(int type, Properties detail)
  {
  }

  protected void initialise(Properties detail, String valueClass)
    throws GTClientException
  {
    try
    {
      _min = getNumber(detail.getProperty("range.min"), valueClass, false);
      _max = getNumber(detail.getProperty("range.max"), valueClass, true);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error initialising SingleRangeConstraint",t);
    }
  }

  public Number getMax()
  {
    return _max;
  }

  public Number getMin()
  {
    return _min;
  }

  public int getSize()
  {
    return 1;
  }

  public Number getMin(int i)
  {
    if(i != 0) throw new java.lang.IllegalArgumentException("Invalid range:" + i);
    return _min;
  }

  public Number getMax(int i)
  {
    if(i != 0) throw new java.lang.IllegalArgumentException("Invalid range:" + i);
    return _max;
  }

  protected Number getNumber(String value, String valueClass, boolean max)
    throws GTClientException
  {
    try
    {
      if("java.lang.Byte".equals(valueClass) || "java.lang.Number".equals(valueClass)) // 20031027 DDJ: Added support for Number for "condition.type" 
      {
        return new Byte( (value == null) ? "" + (max ? Byte.MAX_VALUE : Byte.MIN_VALUE) : value);
      }
      if("java.lang.Short".equals(valueClass))
      {
        return new Short((value == null) ? "" + (max ? Short.MAX_VALUE : Short.MIN_VALUE) : value);
      }
      if("java.lang.Integer".equals(valueClass))
      {
        return new Integer((value == null) ? "" + (max ? Integer.MAX_VALUE : Integer.MIN_VALUE) : value);
      }
      if("java.lang.Long".equals(valueClass))
      {
        return new Long((value == null) ? "" + (max ? Long.MAX_VALUE : Long.MIN_VALUE) : value);
      }
      if("java.lang.Float".equals(valueClass))
      {
        return new Float((value == null) ? "" + (max ? Float.MAX_VALUE : Float.MIN_VALUE) : value);
      }
      if("java.lang.Double".equals(valueClass))
      {
        return new Double((value == null) ? "" + (max ? Double.MAX_VALUE : Double.MIN_VALUE) : value);
      }
      throw new java.lang.UnsupportedOperationException("Type " + valueClass + " not supported");
    }
    catch(Throwable t)
    {
      throw new GTClientException("Cannot create " + valueClass + " from " + value,t);
    }
  }
}