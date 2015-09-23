/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */
package com.gridnode.pdip.base.time.entities.value;

import java.util.Date;

public class iCalPeriodV extends iCalValueV
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7552864092411285673L;
public static final int	CAL_COMPONENT_PERIOD_DATETIME = 0;
 public	static final int	CAL_COMPONENT_PERIOD_DURATION =1;

/* Period of time, can have explicit start/end times or start/duration instead */
  protected  int  _type;
  protected  Date _start;
  protected  Date _endTime;
  protected  int _duration;


  public iCalPeriodV(int kind)
  {
    if(kind != iCalValueKind.ICAL_PERIOD_VALUE)
      throw new IllegalArgumentException("Wrong Kind="+kind);
    _kind = (short)kind;
  }

  public iCalPeriodV()
  {
    super(iCalValueKind.ICAL_PERIOD_VALUE);
  }

  public int getType()
  {
    return _type;
  }
  public Date getStart()
  {
    return _start;
  }
  public Date getEndTime()
  {
    return _endTime;
  }
  public int getDuration()
  {
     return _duration;
  }

  static final int ICAL_INT_VALUE = 1;
  static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[]
  {
    new ValueFieldInfo("_type", "int", 1),
    new ValueFieldInfo("_start", "Date", 2),
    new ValueFieldInfo("_endTime", "Date", 3),
    new ValueFieldInfo("_duration", "int", 4)
  };

 public  ValueFieldInfo[] getFieldInfos()
 {
   return FieldInfo;
 }

}
