/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventResponseSupport.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2002    Neo Sok Lay         Change Implementation similar to
 *                                    EventSupport.
 * May 02 2002    Neo Sok Lay         Remove getResponseData() and setResponseData()
 *                                    methods.
 */
package com.gridnode.pdip.framework.rpf.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Convience class to extend for EventResponses
 */
public abstract class EventResponseSupport
  implements          IEventResponse
{
  private Map _responseData = Collections.synchronizedMap(new HashMap());

  public String getEventName()
  {
    return null;
  }

  public EventResponseSupport()
  {
  }

  /**
   * Get the event response data
   *
   * @return The data associated with this event response
   *
   * @since 2.0 release 2.0.1
   */
//  protected Map getResponseData()
//  {
//    return _responseData;
//  }

  /**
   * Set the event response data
   *
   * @param The data associated with this event response
   *
   * @since 2.0 release 2.0.1
   */
//  protected void setResponseData(HashMap responseData)
//  {
//    _responseData = responseData;
//  }

  /**
   * Get the specific event response data based on unique key
   *
   * @return The data associated with this unique key
   *
   * @since 2.0 release 2.0.1
   */
  public Object getResponseData(String key)
  {
    return _responseData.get(key);
  }

  /**
   * Set the specific event response data based on unique key
   *
   * @param key with which the specified value is to be associated.
   * @param value to be associated with the specified key
   *
   * @since 2.0 release 2.0.1
   */
  protected void setResponseData(String key, Object value)
  {
    _responseData.put(key, value);
  }
}

