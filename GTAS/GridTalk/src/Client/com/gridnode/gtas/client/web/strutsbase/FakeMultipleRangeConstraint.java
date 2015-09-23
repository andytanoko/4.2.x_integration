/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FakeMultipleRangeConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.IRangeConstraint;
import com.gridnode.gtas.client.ctrl.ISingleRangeConstraint;
 
public class FakeMultipleRangeConstraint implements IRangeConstraint
{
  ISingleRangeConstraint[] _ranges;

  public FakeMultipleRangeConstraint(ISingleRangeConstraint[] ranges)
  {
    setRanges(ranges);
  }

  public int getType()
  {
    return IConstraint.TYPE_RANGE;
  }

  public int getSize()
  {
    return _ranges.length;
  }

  public Number getMin(int i)
  {
    return _ranges[i].getMin();
  }
  
  public Number getMax(int i)
  {
    return _ranges[i].getMax();
  }

  public ISingleRangeConstraint[] getRanges()
  {
    return _ranges;
  }

  public void setRanges(ISingleRangeConstraint[] ranges)
  {
    if (ranges == null)
      throw new NullPointerException("ranges is null");
    if(ranges.length < 1)
      throw new IllegalArgumentException("Must specify at least one range");
    _ranges = ranges;
  }

}