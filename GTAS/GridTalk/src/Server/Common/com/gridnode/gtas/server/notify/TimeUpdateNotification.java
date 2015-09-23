/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimeUpdateNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.notify;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Notification for Time Updates. Type of Time Update is defined in ActionCode.
 * Currently only UTC time update is supported.
 * The notification contains the following: <p>
 * <pre>
 * Notification Id    - "TimeUpdate": Unique Identifier for this type of notification.
 *                    - can be used for message selection e.g. id='TimeUpdate'
 * Action Code        - Action code indicating type of update.
 *                    - can be used for message selection e.g. action='1' means
 *                      update UTC time.
 * Update Properties  - The properties that are changed.
 * </pre>
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class TimeUpdateNotification extends AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8280971637585402572L;

	public static final int UPDATE_UTC_TIME = 1;

  private int _actionCode;
  private Properties _updateProps;

  /**
   * Construct a TimeUpdateNotification instance.
   *
   * @param actionCode Action code indicate type of update.
   * @param timeUpdateProps Update properties. The details of the properties
   * should be defined by the sender.
   */
  public TimeUpdateNotification(int actionCode, Properties timeUpdateProps)
  {
    _actionCode = actionCode;
    _updateProps = timeUpdateProps;
    putProperty("action", String.valueOf(_actionCode));
  }

  /**
   * Get the Time update properties
   *
   * @return Time update properties.
   */
  public Properties getTimeUpdateProperties()
  {
    return _updateProps;
  }

  /**
   * Get Action code of this notification.
   *
   * @return The action code for this time update.
   */
  public int getActionCode()
  {
    return _actionCode;
  }

  public String getNotificationID()
  {
    return "TimeUpdate";
  }

  public String toString()
  {
    StringWriter writer = new StringWriter();
    writer.write(getNotificationID());
    writer.write("- [");
    writer.write(String.valueOf(getActionCode()));
    writer.write(", ");
    _updateProps.list(new PrintWriter(writer));
    writer.write("]");
    writer.flush();

    String str = writer.toString();

    return str;
  }
}