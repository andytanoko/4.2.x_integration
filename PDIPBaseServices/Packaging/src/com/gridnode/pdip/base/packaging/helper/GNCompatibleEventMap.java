/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNCompatibleEventMap.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 30 2003    Kan Mun                 Created
 */


package com.gridnode.pdip.base.packaging.helper;

import java.util.*;

import com.gridnode.pdip.base.packaging.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

public class GNCompatibleEventMap
{

  private static final String CLASS_NAME = "GNCompatibleEventMap";
  private static final String SEPARATOR = ",";
  private static final String SEPARATOR2 = "|";

  private static Object lock = new Object();

  private static GNCompatibleEventMap _self = null;

  private Hashtable _splitEventAndAckPair = new Hashtable();

  private GNCompatibleEventMap()
  {
    PackagingLogger.debugLog(CLASS_NAME, "constructor",
            "In GNCompatibleEventMap");
    Configuration config = ConfigurationManager.getInstance().getConfig(
            IPackagingConstants.TPTIMPL_02000_CONFIG_NAME);
    Properties gneventProps = config.getProperties();
    boolean isInitlilize = initilizeEventList(gneventProps);
    if (isInitlilize)
      PackagingLogger.infoLog(CLASS_NAME, "GNCompatibleEventMap",
              "GNCompatibleEventMap Initilized Successfully");
    else
      PackagingLogger.errorLog(ILogErrorCodes.PACKAGING_EVENTMAP_INITIALIZE, CLASS_NAME, 
              "GNCompatibleEventMap","Event list is not initilized");
  }

  public static GNCompatibleEventMap getInstance()
  {
    if(_self == null)
    {
      synchronized(lock)
      {
        if(_self == null)
          _self = new GNCompatibleEventMap();
      }
    }
    return _self;
  }

  private boolean initilizeEventList(Properties gnProperties)
  {
    try
    {
      if(gnProperties != null)
      {
        PackagingLogger.infoLog(CLASS_NAME, "initilizeEventList",
                "In gnProperties Begin");

        String splitEventAndAck = gnProperties.getProperty(
        IPackagingConstants.TPTIMPL_02000_SPLIT_EVENT_AND_ACK);
        parseAndSet(_splitEventAndAckPair, splitEventAndAck);

        return true;
      }
      else
      {
        PackagingLogger.warnLog(CLASS_NAME,"initilizeEventList()",
                "GNCompatible Properties is Null");
      }
    }
    catch(Exception ex)
    {
      PackagingLogger.warnLog(CLASS_NAME,"initilizeEventList()",
              "Cannot Initilize the EventList ",ex);
    }
    return false;
  }

  private void parseAndSet(Hashtable eventTable, String eventList)
  {
    if(eventList != null)
    {
      StringTokenizer st = new StringTokenizer(eventList, SEPARATOR);
      String value = null;
      String key = null;
      while(st.hasMoreTokens())
      {
        String token = st.nextToken();
        PackagingLogger.infoLog(CLASS_NAME, "parseAndSet()", "Event and Ack Pair list--> " + token);
        int index = token.indexOf(SEPARATOR2);
        key = token.substring(0, index);
        value = token.substring(index + 1);
        eventTable.put(key, value);
      }
    }
    else
    {
      PackagingLogger.warnLog(CLASS_NAME, "parseAndSet", "Event and Ack list is Null ");
    }
  }


  public String getSplitEventAckId(String eventId)
  {
    return (String) _splitEventAndAckPair.get(eventId);
  }

  public String getSplitEventId(String ackEventId)
  {
    if (_splitEventAndAckPair.containsValue(ackEventId))
    {
      synchronized (_splitEventAndAckPair)
      {
    	  for (Iterator i=_splitEventAndAckPair.keySet().iterator(); i.hasNext(); ) 
    	  {
    		Object o = i.next();
    		if (ackEventId.equals(_splitEventAndAckPair.get(o)))
    		  return (String) o;
    	  }
      }
    }
    return null;
  }

}