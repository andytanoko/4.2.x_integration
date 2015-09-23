/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultTimeServer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.util;

import java.util.Calendar;
import java.util.Properties;

import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.config.PropertyConfigReader;
import com.gridnode.pdip.framework.config.PropertyConfigWriter;

/**
 * Default implementation for TimeServer.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DefaultTimeServer
  implements ITimeServer,
             IFrameworkConfig
{
  //private static final String UTC_OFFSET_FILE = "time.file";
  private static final String UTC_OFFSET      = "utc.offset";
  private PropertyConfigReader _reader        = new PropertyConfigReader();
  private PropertyConfigWriter _writer        = new PropertyConfigWriter();
  private String     _path                    = "utc-time.properties";

  public DefaultTimeServer()
  {
  }

  /**
   * Get the Utc Offset - offset to add to local time to get Utc time.
   *
   * @return Utc offset stored in "time" properties or default Utc offset based
   * on default timezone offset.
   */
  public long getUtcOffset()
  {
    Properties props = loadProperties();  // refresh the properties
    String offsetStr = props.getProperty(UTC_OFFSET, "0");
    long offset = 0;
    try
    {
      offset = Long.parseLong(offsetStr);
    }
    catch (NumberFormatException ex)
    {
    }

    // calculate from default TimeZone offset if the UTC offset has not
    // been available.
    if (offset == 0)
    {
      long defOffset = getDefaultTimeZoneOffset();
      offset -= defOffset;
    }
    return offset;
  }

  /**
   * Get the default Time Zone offset for the machine this code is running.
   *
   * @return Default Time Zone offset. Offset to add to Utc to get local time.
   */
  public long getDefaultTimeZoneOffset()
  {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
  }

  /**
   * Set the current Utc offset in milliseconds. This offset will be saved in
   * the "time" properties.
   *
   * @param currentUtcTime Current Utc time.
   */
  protected void setUtcOffset(long utcOffset)
  {
    Properties props = loadProperties();
    props.setProperty(UTC_OFFSET, String.valueOf(utcOffset));
    _writer.write(_path, props);
  }

  /**
   * Set the path to the Time properties.
   *
   * @param path File path to the Time properties
   */
  protected void setPropertyPath(String path)
  {
    _path = path;
  }

  /**
   * Get the path to the Time properties.
   *
   * @return File path to the Time properties.
   */
  protected String getPropertyPath()
  {
    return _path;
  }

  /**
   * Initialize the Time configuration.
   *
   * @return The Properties for Time.
   */
  private Properties loadProperties()
  {
    Properties timeProps = _reader.read(_path);

//    if (timeProps == null)
//    {
//      Configuration conf = ConfigurationManager.getInstance().getConfig(
//                             FRAMEWORK_TIME_CONFIG);
//
//      if (conf != null)
//      {
//        String file = conf.getString(UTC_OFFSET_FILE, null);
//        if (file != null)
//        {
//          timeProps = _reader.read(file);
//          if (timeProps != null)
//            _path = file;
//        }
//      }

      if (timeProps == null)
      {
        timeProps = new Properties();
      }
//    }

    return timeProps;
  }

}