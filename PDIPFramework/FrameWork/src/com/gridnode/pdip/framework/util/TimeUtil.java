/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimeUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 */
package com.gridnode.pdip.framework.util;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;

import java.util.Date;
import java.sql.Timestamp;

/**
 * This class provides services for conversion between local time and utc time.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I7
 */
public class TimeUtil
{
  private static ITimeServer _timeServer;
  private static final String TIME_SERVER = "time.server";

  /**
   * Set the TimeServer that provides the time service.
   *
   * @param timeServer The TimeServer
   */
  public static void setTimeServer(ITimeServer timeServer)
  {
    _timeServer = timeServer;
  }

  /**
   * Get the local current time in milliseconds.
   *
   * @return Local Current time in milliseconds.
   */
  public static long getCurrentLocalTimeMillis()
  {
    return System.currentTimeMillis();
  }

  /**
   * Get the local current time.
   *
   * @return Local current time.
   */
  public static Timestamp getCurrentLocalTimestamp()
  {
    return new Timestamp(getCurrentLocalTimeMillis());
  }

  /**
   * Get the Offset of utc time to local time (in milliseconds).
   */
  private static synchronized long getUtcOffset()
  {
    long offset = 0;

    if (_timeServer == null)
      _timeServer = getTimeServer();

    offset = _timeServer.getUtcOffset();

    return offset;
  }

  /**
   * Convert current local time to utc time.
   *
   * @return Current Utc time in milliseconds.
   */
  public static long localToUtc()
  {
    return getCurrentLocalTimeMillis() + getUtcOffset();
  }

  /**
   * Convert current local time to uitc time.
   *
   * @return Current Utc time.
   */
  public static Timestamp localToUtcTimestamp()
  {
    return new Timestamp(localToUtc());
  }

  /**
   * Convert specific local time to Utc time.
   *
   * @param localTime the specific local time in milliseconds to convert.
   * @return Utc time in milliseconds converted from <code>localTime</code>
   */
  public static long localToUtc(long localTime)
  {
    return localTime + getUtcOffset();
  }

  /**
   * Convert specific local time to Utc time.
   *
   * @param localTime the specific local time to convert.
   * @return Utc time converted from <code>localTime</code>
   */
  public static Timestamp localToUtcTimestamp(Date localTime)
  {
    return new Timestamp(localToUtc(localTime.getTime()));
  }

  /**
   * Convert specific Utc time to local time.
   *
   * @param utcTime the specific Utc time in milliseconds to convert.
   * @return Local time in milliseconds converted from <code>utcTime</code>
   */
  public static long utcToLocal(long utcTime)
  {
    return utcTime - getUtcOffset();
  }

  /**
   * Convert specific Utc time to local time.
   *
   * @param utcTime the specific Utc time to convert.
   * @return Local time converted from <code>utcTime</code>
   */
  public static Timestamp utcToLocalTimestamp(Date utcTime)
  {
    return new Timestamp(utcToLocal(utcTime.getTime()));
  }

  /**
   * Get the TimeServer implementation class for this TimeUtil
   *
   * @return The TimeServer implementation instance for this TimeUtil.
   */
  private static ITimeServer getTimeServer()
  {
    ITimeServer timeServer = null;
    Configuration conf = ConfigurationManager.getInstance().getConfig(
                           IFrameworkConfig.FRAMEWORK_TIME_CONFIG);

    if (conf != null)
    {
      String timeServerClass = conf.getString(TIME_SERVER);

      try
      {
        timeServer = (ITimeServer)Class.forName(timeServerClass).newInstance();
      }
      catch (Exception ex)
      {
        Log.error(ILogErrorCodes.TIME_GENERIC, Log.FRAMEWORK, "[TimeUtil.getTimeServer] Unable to instantiate TimeServer. Error: "+ex.getMessage(), ex);
      }

    }
    if (timeServer == null)
      timeServer = new DefaultTimeServer();

    return timeServer;
  }

}