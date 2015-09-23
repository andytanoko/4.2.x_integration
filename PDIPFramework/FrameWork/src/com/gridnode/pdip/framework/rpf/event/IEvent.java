/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Ang Meng Hua        Created
 * May 02 2002    Neo Sok Lay         Remove exposure of getEventData() and
 *                                    setEventData() methods.
 */
package com.gridnode.pdip.framework.rpf.event;

//import java.util.HashMap;

/**
 * This interface determines the required methods for an estore event
 */
public interface IEvent extends java.io.Serializable
{
  /**
  *   Specifiy a logical name that is mapped to the event
  *   in the Universal Remote Controller.
  */
  public String getEventName();

  /**
   * Get the event data
   *
   * @return The data associated with this event
   *
   * @since 2.0 release 2.0.1
   */
//  public HashMap getEventData();

  /**
   * Set the event data
   *
   * @param The data associated with this event
   *
   * @since 2.0 release 2.0.1
   */
//  public void setEventData(HashMap eventData);

  /**
   * Get the specific event data based on unique key
   *
   * @return The data associated with this unique key
   *
   * @since 2.0 release 2.0.1
   */
  public Object getEventData(String key);

  /**
   * Set the specific event data based on unique key
   *
   * @param key with which the specified value is to be associated.
   * @param value to be associated with the specified key
   *
   * @since 2.0 release 2.0.1
   */
//  public void setEventData(String key, Object value);
}

