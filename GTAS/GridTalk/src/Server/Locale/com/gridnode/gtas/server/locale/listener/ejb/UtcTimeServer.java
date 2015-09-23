/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UtcTimeServer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.listener.ejb;

import com.gridnode.gtas.server.locale.helpers.Logger;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.*;

import java.io.File;

/**
 * Implementation for Utc Time service.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UtcTimeServer
  extends    DefaultTimeServer
{
  public static final String  TIME_PATH_KEY = "locale.path.time";

  private static UtcTimeServer _self = null;
  private static final Object  _lock = new Object();

  public UtcTimeServer()
  {
    setPath();
  }

  public static UtcTimeServer getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
        {
          _self = new UtcTimeServer();
        }
      }
    }

    return _self;
  }

  /**
   * Set the Utc Offset using references from Utc and Local times.
   *
   * @param utcTime Utc time in milliseconds
   * @param localTime Local time in milliseconds.
   * @return Utc offset between the two reference times.
   */
  void setUtcOffset(long utcTime, long localTime)
  {
    long offset = utcTime - localTime;
    super.setUtcOffset(offset);
  }

  /**
   * Set the path for utc time properties file.
   */
  private void setPath()
  {
    try
    {
      File file = FileUtil.getFile(TIME_PATH_KEY, getPropertyPath());
      setPropertyPath(file.getCanonicalPath());
    }
    catch (Exception ex)
    {
      Logger.debug("[UtcTimeServer.setPath] Error: "+ex.getMessage());
    }
  }

}