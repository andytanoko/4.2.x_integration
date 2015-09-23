/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TextConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class TextConstraint extends AbstractConstraint implements ITextConstraint
{
  protected int _max;
  protected int _min;

  /**
   * Explicit constructor for use with virtual fields
   */
  TextConstraint(int min, int max)
  {
    try
    {
      init(IConstraint.TYPE_TEXT, null);
      _min = min;
      _max = max;
    }
    catch(Throwable t)
    {
      throw new java.lang.RuntimeException("Caught " + t + " while initialising TextConstraint");
    }
  }

  /**
   * Standard constructor for use when initialising based on gtas supplied properties
   */
  TextConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_TEXT, detail);
  }

  public int getMinLength()
  {
    return _min;
  }

  public int getMaxLength()
  {
    return _max;
  }

  protected void initialise(int type, Properties detail)
  {
    String minStr = detail.getProperty("text.length.min","0");
    String maxStr = detail.getProperty("text.length.max","0");
    try
    {
      _min = Integer.parseInt(minStr);
      _max = Integer.parseInt(maxStr);
    }
    catch(Exception e)
    {
      throw new java.lang.IllegalArgumentException("Couldnt convert property to integer value");
    }
  }
}