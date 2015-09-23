/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FakeSingleRangeConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.ISingleRangeConstraint;

public class FakeSingleRangeConstraint implements ISingleRangeConstraint
{
  Number _min;
  Number _max;

  public FakeSingleRangeConstraint(Number min, Number max)
  {
    setMin(min);
    setMax(max);
  }

  public int getType()
  {
    return IConstraint.TYPE_RANGE;
  }

  public int getSize()
  {
    return 1;
  }

  public Number getMax()
  {
    return _max;
  }
  
  public Number getMax(int i)
  {
    return _max;
  }

  public Number getMin()
  {
    return _min;
  }
  
  public Number getMin(int i)
  {
    return _min;
  }

  public void setMax(Number max)
  {
    if (max == null)
      throw new NullPointerException("max is null");
    _max = max;
  }

  public void setMin(Number min)
  {
    if (min == null)
      throw new NullPointerException("min is null");
    _min = min;
  }

}