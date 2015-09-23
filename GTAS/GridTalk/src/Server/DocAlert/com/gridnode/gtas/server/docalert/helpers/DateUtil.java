/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DateUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Apr 02 2003    Neo Sok Lay         GNDB00013324: rollDate() does not roll
 *                                    across month.
 */
package com.gridnode.gtas.server.docalert.helpers;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

/**
 * This class provides the services for manipulating dates.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DateUtil
{

  /**
   * Roll a date up a number of days.
   *
   * @param date The date to roll.
   * @param daysToRollUp The number of days to roll up. Note that if this
   * number is negative, the date will actually be rolled down.
   * @return The modified date.
   */
  public static Date rollDate(Date date, int daysToRollUp)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    /*030402NSL: cal.roll() only roll the DD but not MM. Should use cal.add()
    cal.roll(Calendar.DATE, daysToRollUp);
    */
    cal.add(Calendar.DATE, daysToRollUp);
    return cal.getTime();
  }

  /**
   * Convert a date string to Date using current timezone.
   * Currently the only supported date format is "yyyyMMdd" e.g. "20021024" for
   * 24th Oct 2002. Any time portion will be discarded.
   *
   * @return The converted Date.
   */
  public static Date convertToDate(String dateStr)
  {
    Date date = null;

    try
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      date = dateFormat.parse(dateStr.substring(0, 8));
    }
    catch (Exception ex)
    {
      Logger.warn("[DocExtractionHelper.convertToDate] Error", ex);
    }

    return date;
  }

}