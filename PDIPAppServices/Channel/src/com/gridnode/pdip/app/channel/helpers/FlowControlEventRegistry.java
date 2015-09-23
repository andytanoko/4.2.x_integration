package com.gridnode.pdip.app.channel.helpers;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

public class FlowControlEventRegistry
{
  private static final String CLASS_NAME = "FlowControlEventRegistry";
  private static final String SEPARATOR = ",";

  private static FlowControlEventRegistry _self = null;

  private HashSet _disableSplitSet = null;
  private HashSet _disableZipSet = null;

  static {
    _self = new FlowControlEventRegistry();
  }

  private FlowControlEventRegistry()
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "FlowControlEventRegistry()",
      "[In FlowControlEventRegistry Begin]");
    _disableSplitSet = new HashSet();
    _disableZipSet = new HashSet();
    Configuration config =
      ConfigurationManager.getInstance().getConfig(
        IChannelConfig.FLOWCONTROL_CONFIG_NAME);
    Properties flowEventProps = config.getProperties();
    boolean isInitlilize = initilizeEventList(flowEventProps);
    if (isInitlilize)
      ChannelLogger.debugLog(
        CLASS_NAME,
        "FlowControlEventRegistry()",
        "[FlowControlEventRegistry Initilized]");
    else
      ChannelLogger.debugLog(
        CLASS_NAME,
        "FlowControlEventRegistry()",
        "[FlowControlEventRegistry is Not Initilized...Check properties file]");
  }

  public static FlowControlEventRegistry getInstance()
  {
    return _self;
  }

  private boolean initilizeEventList(Properties flowEventProps)
  {
    try
    {
      if (flowEventProps != null)
      {
        ChannelLogger.infoLog(
          CLASS_NAME,
          "initilizeEventList()",
          "[Initilize  Begin]");

        String disableSplit =
          flowEventProps.getProperty(IChannelConfig.FLOWCONTROL_DISABLE_SPLIT);
        parseAndSet(_disableSplitSet, disableSplit);
        String disableZip =
          flowEventProps.getProperty(IChannelConfig.FLOWCONTROL_DISABLE_ZIP);
        parseAndSet(_disableZipSet, disableZip);
        return true;
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "initilizeEventList",
        "Cannot Initilize FlowControl EventList",
        ex);
    }
    return false;
  }

  private void parseAndSet(HashSet eventSet, String eventList)
  {
    if (eventList != null)
    {
      StringTokenizer st = new StringTokenizer(eventList, SEPARATOR);
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        ChannelLogger.debugLog(
          CLASS_NAME,
          "parseAndSet()",
          "EventID Set--> " + token);
        eventSet.add(token);
      }
    }
    else
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "parseAndSet()",
        "[EventList is Null]");
    }
  }

  public boolean isSplitDisable(String eventId)
  {
    return _disableSplitSet.contains(eventId);
  }

  public boolean isZipDisable(String eventId)
  {
    return _disableZipSet.contains(eventId);
  }

}