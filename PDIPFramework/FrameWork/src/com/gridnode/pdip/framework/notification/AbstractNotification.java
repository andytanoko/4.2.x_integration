/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractNotification.java
 * Refactored from com.gridnode.gtas.server.notify.AbstractNotification.java
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.notification;

import java.util.Properties;

/**
 * Abstraction for a Notification message.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public abstract class AbstractNotification
  implements INotification
{
  private Properties _propertyMap = new Properties();

  public AbstractNotification()
  {
  }

  /**
   * Set header property of the message. This property can be used to select
   * type of notification message to receive.
   *
   * @param key Key of the property
   * @param value Value of the property.
   */
  protected void putProperty(String key, String value)
  {
    _propertyMap.setProperty(key, value);
  }

  /**
   * Get the keys to all header properties set.
   *
   * @return keys of all header properties set.
   */
  public String[] getPropertyKeys()
  {
    return (String[])_propertyMap.keySet().toArray(new String[0]);
  }

  /**
   * Get the value of the property with the specified key.
   * @param key The key to the property
   * @param Value set for the property, or <b>null</b> if no such property.
   */
  public String getProperty(String key)
  {
    return _propertyMap.getProperty(key);
  }
}