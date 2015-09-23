/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INotification.java
 * Refactored from com.gridnode.gtas.server.notify.INotification.java
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.notification;

/**
 * Interface for a Notification message.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public interface INotification
  extends        java.io.Serializable
{

  /**
   * Get the identifier for this Notification message. This identifier
   * should be unique as it will be used for message selection,
   * e.g. id='Connection' would be to select ConnectionNotification messages.
   */
  String getNotificationID();

  /**
   * Get the keys to all header properties (specific to the notification, excluding
   * notificationID).
   */
  String[] getPropertyKeys();

  /**
   * Get the value of a property.
   *
   * @param key Key to the property to get value.
   * @return Value for the property, or <b>null<b> if no such property key.
   */
  String getProperty(String key);
}