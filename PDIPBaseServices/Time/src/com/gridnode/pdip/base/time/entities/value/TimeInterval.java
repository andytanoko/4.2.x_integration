// %1023788050012:com.gridnode.pdip.base.time.value%
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

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeInterval
{

  Date _start;
  Date _end;

  /**
   * Creates a new TimeInterval object.
   * 
   * @param start DOCUMENT ME!
   * @param end DOCUMENT ME!
   */
  public TimeInterval(Date start, Date end)
  {
    if (start.before(end))
    {
      _start = start;
      _end = end;
    }
    else
    {
      _end = start;
      _start = end;
    }
  }

  /**
   * DOCUMENT ME!
   * 
   * @param obj DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public boolean equals(Object obj)
  {
    if (obj == null || !(obj instanceof TimeInterval))
      return false;
    TimeInterval another = (TimeInterval) obj;
    return _start.equals(another._start) && _end.equals(another._end);
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String toString()
  {
    return "{" + _start + ", " + _end + "}";
  }

  /**
   * DOCUMENT ME!
   * 
   * @param time DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public boolean isInScope(Date time)
  {
    return !time.before(_start) && !time.after(_end);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param another DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public boolean hasIntersection(TimeInterval another)
  {
    return isInScope(another._start) || another.isInScope(_start);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param another DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public TimeInterval union(TimeInterval another)
  {
    if (!hasIntersection(another))
      return null;
    Date start = _start.before(another._start) ? _start : another._start;
    Date end = _end.after(another._end) ? _end : another._end;
    return new TimeInterval(start, end);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param intervalList DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public List addToList(List intervalList)
  {
    List res = intervalList;
    if (res == null)
    {
      res = new ArrayList();
    }
    if (res.isEmpty())
    {
      res.add(this);
      return res;
    }
    int size = intervalList.size();
    TimeInterval interval = new TimeInterval(_start, _end);
    int i = size - 1;
    boolean added = false;
    for (; i >= 0; i--)
    {
      TimeInterval curInterval = (TimeInterval) intervalList.get(i);
      if (interval._end.before(curInterval._start)) //before curInterval
      {
        continue;
      }
      if (interval._start.after(curInterval._end)) //after curInterval
      {
        intervalList.add(i + 1, interval);
        added = true;
        break;
      }
      interval = interval.union(curInterval);
      intervalList.remove(i);
    }
    if (!added)
      intervalList.add(0, interval);
    return intervalList;
  }

//  public static String timeIntervalList2Str(List timeIntervalList)
//  {
//    if (timeIntervalList == null || timeIntervalList.isEmpty())
//      return "";
//    int size = timeIntervalList.size();
//    StringBuffer strBuf = new StringBuffer();
//    strBuf.append(size + "[\n");
//    for (int i = 0; i < size; i++)
//    {
//      TimeInterval interval = (TimeInterval) timeIntervalList.get(i);
//      if (interval != null)
//        strBuf.append(interval.toString());
//      strBuf.append("\n");
//    }
//    strBuf.append("]");
//    return strBuf.toString();
//  }
//
//  public static List str2TimeIntervalList(String str)
//  {
//    if (str == null)
//      return new ArrayList();
//
//    List res = new ArrayList();
//    try
//    {
//      String subStr = str.trim();
//      StringTokenizer tokenizer = new StringTokenizer(subStr, "[]\n");
//      int size = Integer.parseInt(tokenizer.nextToken());
//
//      while (tokenizer.hasMoreTokens())
//      {
//        String intervalStr = tokenizer.nextToken();
//        TimeInterval interval = str2TimeInterval(intervalStr);
//        if (interval != null)
//          res.add(interval);
//      }
//    }
//    catch (Throwable ex)
//    {
//      Log.err("base.time", "error in read str2TimeIntervalList, str = " + str, ex);
//    }
//    return res;
//  }
//
//  public static TimeInterval str2TimeInterval(String str)
//  {
//    if (str == null)
//      return null;
//    Date start= null, end= null;
//    try
//    {
//      String subStr = str.trim();
//      StringTokenizer tokenizer = new StringTokenizer(subStr, "{},");
//      start = new Date(tokenizer.nextToken());
//      end = new Date(tokenizer.nextToken());
//    }
//    catch (Throwable ex)
//    {
//      Log.err("base.time", "error in read str2TimeInterval, str = " + str, ex);
//    }
//    if (start != null && end != null)
//      return new TimeInterval(start, end);
//    return null;
//  }

}