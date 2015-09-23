/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimeConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2004-02-17     Neo Sok Lay         Add Pattern property for display
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class TimeConstraint extends AbstractConstraint implements ITimeConstraint
{
  private static final String ADJ_STR_NONE  = "none"; //20030109AH
  private static final String ADJ_STR_GTS   = "gts";  //20030109AH
  private static final String ADJ_STR_GTAS  = "gtas"; //20030109AH

  protected boolean _hasTime;
  protected boolean _hasDate;
  private int _adjustment; //20030109AH
  private String _pattern; //20040217NSL

  TimeConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_TIME, detail);
  }

  protected void initialise(int type, Properties detail)
    throws GTClientException
  {
    try
    {
      _hasDate = DefaultSharedFMI.makeBoolean( detail.getProperty("datetime.date","true") );
      _hasTime = DefaultSharedFMI.makeBoolean( detail.getProperty("datetime.time","true") );
      _pattern = detail.getProperty("datetime.pattern", null);

      //20030109AH - Process adjustment format information
      String adjustment = detail.getProperty("datetime.adjustment","none");
      if(ADJ_STR_NONE.equals(adjustment))
      {
        _adjustment = ADJ_NONE;
      }
      else if(ADJ_STR_GTS.equals(adjustment))
      {
        _adjustment = ADJ_GTS;
      }
      else if(ADJ_STR_GTAS.equals(adjustment))
      {
        _adjustment = ADJ_GTAS;
        throw new java.lang.UnsupportedOperationException(
          "TimeConstraint support for GTAS adjustment not implemented yet");
      }
      else
      {
        throw new java.lang.UnsupportedOperationException(
          "Unknown adjustment for TimeConstraint:"
          + adjustment);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to initialise metainfo for TimeConstraint",t);
    }
  }

  public int getAdjustment()
  { //20030109AH
    return _adjustment;
  }

  public boolean hasTime()
  {
    return _hasTime;
  }

  public boolean hasDate()
  {
    return _hasDate;
  }
  
  public String getPattern()
  {
    return _pattern;
  }
}