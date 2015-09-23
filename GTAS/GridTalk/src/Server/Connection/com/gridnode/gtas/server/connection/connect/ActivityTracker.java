/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivityTracker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 28 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.notify.AlertRequestNotification;

/**
 * Tracks connection related actvities, and at the end of the activity, raise
 * alert notification.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class ActivityTracker
{
  public static final String TYPE_CONNECT           = "1";
  public static final String TYPE_DISCONNECT        = "2";
  public static final String TYPE_RECONNECT         = "3";

  public static final String TYPE_LOST_CONNECTION   = "4";
  public static final String TYPE_LOST_PARTNER      = "5";

  public static final String STATUS_SUCCESS         = "0";
  public static final String STATUS_FAILURE         = "1";

  private static final Object _lock = new Object();
  private static ActivityTracker _self;
  private Hashtable _activities;

  private ActivityTracker()
  {
    _activities = new Hashtable();
  }

  public static ActivityTracker getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new ActivityTracker();
      }
    }
    return _self;
  }

  private Activity removeActivity(String nodeID)
  {
    Activity activity = (Activity)_activities.remove(nodeID);
    if (activity != null)
    {
      Logger.log("[ActivityTracker.removeActivity] Removed "+ activity);
    }

    return activity;
  }

  public void startActivity(String nodeID, String type)
  {
    removeActivity(nodeID);
    Activity activity = new Activity(type);
    _activities.put(nodeID, activity);
  }

  public void endActivity(String nodeID, String type, String status)
  {
    Activity activity = removeActivity(nodeID);
    if (activity == null)
    {
      Logger.log("[ActivityTracker.endActivity] No activity tracked for GridNode "+ nodeID);
    }
    else
    {
      if (activity.getType().equals(type))
      {
        activity.end(status);

        raiseAlert(nodeID, activity);
      }
      else
      {
        Logger.log("[ActivityTracker.endActivity] Incompatible activity type: "+type +
          ", expected: "+activity.getType());
      }
    }
  }

  public void logActivity(String nodeID, String type)
  {
    removeActivity(nodeID);
    Activity activity = new Activity(type);

    raiseAlert(nodeID, activity);
  }

  private void raiseAlert(String nodeID, Activity activity)
  {
    ConnectionData provider;
    String alertType;

    String nodeName = getGridNodeName(nodeID);
    Timestamp end   = activity.getEnd();

    if (end != null)
    {
      provider = new ConnectionData(activity.getType(),
                                    activity.getStart(),
                                    end,
                                    nodeID,
                                    nodeName,
                                    activity.getStatus());

      alertType = TYPE_RECONNECT.equals(activity.getType())         ?
                  AlertRequestNotification.GRIDMASTER_RECONNECTION  :
                  AlertRequestNotification.GRIDMASTER_CONNECTION_ACTIVITY;
    }
    else
    {
      provider = new ConnectionData(activity.getType(),
                                    activity.getStart(),
                                    nodeID,
                                    nodeName);

      alertType = TYPE_LOST_CONNECTION.equals(activity.getType())  ?
                  AlertRequestNotification.GRIDMASTER_CONNECTION_LOST :
                  AlertRequestNotification.PARTNER_UNCONTACTABLE;
    }

    ArrayList providerList = new ArrayList();
    providerList.add(provider);

    AlertRequestNotification notification = new AlertRequestNotification(
                                                alertType,
                                                providerList);
    DelegateHelper.broadcast(notification);
  }

  private String getGridNodeName(String nodeID)
  {
    String name = null;
    try
    {
      GridNode gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(nodeID);
      if (gn != null)
        name = gn.getName();
    }
    catch (Exception ex)
    {
      Logger.warn("[ActivityTracker.getGridNodeName] Error: "+ex.getMessage());
    }
    return name;
  }

  class Activity
  {
    private String _type;
    private String _status;
    private long _start;
    private long _end;

    Activity(String type)
    {
      _type = type;
      _start = System.currentTimeMillis();
    }

    void end(String status)
    {
      _end = System.currentTimeMillis();
      _status = status;
    }

    Timestamp getStart()
    {
      return new Timestamp(_start);
    }

    Timestamp getEnd()
    {
      if (_end != 0)
        return new Timestamp(_end);
      else
        return null;
    }

    String getType()
    {
      return _type;
    }

    String getStatus()
    {
      return _status;
    }

    public String toString()
    {
      StringBuffer buff = new StringBuffer("Activity: ").append(getType());
      buff.append(", Start: ").append(getStart());
      buff.append(", End: ").append(getEnd());

      return buff.toString();
    }
  }
}
