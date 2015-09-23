// %1023788048059:com.gridnode.pdip.base.time.value%
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

public class DayEntry
  implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2084430954701629L;
	private int _weekno;
  private int _day;

  /**
   * Creates a new DayEntry object.
   * 
   * @param day DOCUMENT ME!
   */
  public DayEntry(int day)
  {
    this._weekno = 0;
    this._day = day;
  }

  /**
   * Creates a new DayEntry object.
   * 
   * @param weekno DOCUMENT ME!
   * @param day DOCUMENT ME!
   */
  public DayEntry(int weekno, int day)
  {
    this._weekno = weekno;
    this._day = day;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public int getWeekNo()
  {
    return _weekno;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public int getDay()
  {
    return _day;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public int dayEntryToInt()
  {
    int weekNo = getWeekNo();
    int day = getDay();
    if (weekNo == 0)
      return day;
    int sign = weekNo > 0 ? 1 : -1;
    return sign * (Math.abs(weekNo) * 8 + day);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param day DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public static int dayOfWeek(int day)
  {
    return Math.abs(day) % 8;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param day DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public static int weekNo(int day)
  {
    int wd;
    int pos;
    wd = dayOfWeek(day);
    pos = (Math.abs(day) - wd) / 8 * ((day < 0) ? -1 : 1);
    return pos;
  }
}