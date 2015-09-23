/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Util.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107             Created
 */
package com.gridnode.gridtalk.testkit.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author i00107
 * Utility helper class
 */
public class Util
{

  /**
   * Generate current timestamp string in UTC
   * @return
   */
  public static String genTS()
  {
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd'T'HHmmss'.'SSS'Z'");
    Date   currentTime_1 = new Date();
    int offset = TimeZone.getDefault().getOffset(currentTime_1.getTime());
    Calendar cal = GregorianCalendar.getInstance();
    cal.setTimeInMillis(currentTime_1.getTime() - offset);
    return formatter.format(cal.getTime());
  }
}
