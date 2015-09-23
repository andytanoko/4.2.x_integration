/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventSupport.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Ang Meng Hua        Created
 * May 02 2002    Neo Sok Lay         Remove getEventData() and setEventData()
 *                                    methods.
 * May 31 2002    Neo Sok Lay         Add data validation methods.
 * 2003-01-31     Daniel D'Cotta      Corrected input parameter class of
 *                                    checkSetShort(), checkSetDouble() and
 *                                    checkSetFloat() methods.
 * Oct 31 2003    Neo Sok Lay         Fix defect GNDB00015128:-
 *                                      Trim string event data
 */
package com.gridnode.pdip.framework.rpf.event;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;

/**
 * This is the base class for all events used by the application.
 * Currently this class only implements Serializable to ensure that
 * all events may be sent the the EJB container via RMI-IIOP.
 */
public class EventSupport
  implements IEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2446736485407993191L;
	private Map _eventData = Collections.synchronizedMap(new HashMap());

public String getEventName()
  {
    return null;
  }

  /**
   * Get the event data
   *
   * @return The data associated with this event
   *
   * @since 2.0 release 2.0.1
   */
//  public Map getEventData()
//  {
//    return _eventData;
//  }

  /**
   * Set the event data
   *
   * @param The data associated with this event
   *
   * @since 2.0 release 2.0.1
   */
//  public void setEventData(HashMap eventData)
//  {
//    _eventData = eventData;
//  }

  /**
   * Get the specific event data based on unique key
   *
   * @return The data associated with this unique key
   *
   * @since 2.0 release 2.0.1
   */
  public Object getEventData(String key)
  {
    return _eventData.get(key);
  }

  /**
   * Set the specific event data based on unique key
   *
   * @param key with which the specified value is to be associated.
   * @param value to be associated with the specified key
   *
   * @since 2.0 release 2.0.1
   */
  public void setEventData(String key, Object value)
  {
    //031031NSL: trim the value if it is a string
    if (value != null && value instanceof String)
    {
      value = ((String)value).trim();
    }
    _eventData.put(key, value);
  }

  public void checkSetString(String key, String value)
    throws EventException
  {
    checkString(key, value);
    setEventData(key, value);
  }

  public void checkSetLong(String key, Long value)
    throws EventException
  {
    checkLong(key, value);
    setEventData(key, value);
  }

  public void checkSetInteger(String key, Integer value)
    throws EventException
  {
    checkInteger(key, value);
    setEventData(key, value);
  }

  public void checkSetShort(String key, Short value)
    throws EventException
  {
    checkShort(key, value);
    setEventData(key, value);
  }

  public void checkSetDouble(String key, Double value)
    throws EventException
  {
    checkDouble(key, value);
    setEventData(key, value);
  }

  public void checkSetFloat(String key, Float value)
    throws EventException
  {
    checkFloat(key, value);
    setEventData(key, value);
  }

  public void checkSetObject(String key, Object value, Class expectedClass)
    throws EventException
  {
    checkObject(key, value, expectedClass);
    setEventData(key, value);
  }

  public void checkSetCollection(String key, Collection value, Class elementClass)
    throws EventException
  {
    checkCollection(key, value, elementClass);
    setEventData(key, value);
  }

  public void checkCollection(String dataKey, Collection value, Class elementClass)
    throws EventException
  {
    if (value == null)
      throw new EventException(dataKey + " is missing!");

    Object[] objects = value.toArray();
    for (int i=0; i<objects.length; i++)
    {
      if (!elementClass.isInstance(objects[i]))
        throw new EventException("Invalid Element Data Type in Event Data "+dataKey);
    }
  }

  public void checkObject(String dataKey, Object dataObj, Class expectedClass)
    throws EventException
  {
    if (dataObj == null)
      throw new EventException(dataKey + " is missing!");

    if (!expectedClass.isInstance(dataObj))
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  /**
   * Check a String type attribute in the Event.
   *
   * @param attrKey The Key to obtain the String attribute.
   * @exception InvalidEventException Attribute value is null, or empty, or
   * is not of String type.
   * @since 2.0
   */
  public void checkString(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof String)
    {
      String dataStr = (String)dataObj;
      if (dataStr == null || dataStr.length() == 0)
        throw new EventException(dataKey +" is missing!");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkLong(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof Long)
    {
      if (dataObj == null)
        throw new EventException(dataKey+" is missing");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkShort(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof Short)
    {
      if (dataObj == null)
        throw new EventException(dataKey+" is missing");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkInteger(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof Integer)
    {
      if (dataObj == null)
        throw new EventException(dataKey+" is missing");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkDouble(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof Double)
    {
      if (dataObj == null)
        throw new EventException(dataKey+" is missing");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkFloat(String dataKey, Object dataObj)
    throws EventException
  {
    if (dataObj instanceof Float)
    {
      if (dataObj == null)
        throw new EventException(dataKey+" is missing");
    }
    else
      throw new EventException("Invalid Event Data Type for "+dataKey);
  }

  public void checkValidURL(String dataKey, String url)
    throws EventException
  {
    try
    {
      new URL(url);
    }
    catch (MalformedURLException e)
    {
      throw new EventException(url+" is not a valid URL for "+dataKey);
    }
  }
  
}

