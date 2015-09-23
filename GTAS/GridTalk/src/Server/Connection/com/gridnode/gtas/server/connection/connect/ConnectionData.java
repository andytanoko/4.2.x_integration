/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 25 Apr 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.pdip.app.alert.providers.AbstractDetails;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * This Data provider provides the Connection related activity data.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class ConnectionData extends AbstractDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1842131245773319522L;

	public static final String NAME = "Connection";

  public static final String FIELD_ACTIVITY           = "ACTIVITY";
  public static final String FIELD_NODE_NAME          = "NODE_NAME";
  public static final String FIELD_NODE_ID            = "NODE_ID";
  public static final String FIELD_STATUS             = "STATUS";
  public static final String FIELD_DT_ACTIVITY_START  = "DT_ACTIVITY_START";
  public static final String FIELD_DT_ACTIVITY_END    = "DT_ACTIVITY_END";
  public static final String FIELD_DT_OCCUR           = "DT_OCCUR";

  /**
   * Constructs a Connection data provider for an initiated activity: e.g
   * Connect, Disconnect, Reconnect.
   *
   * @param activity The activity that was initiated
   * @param start The start date/time of the activity
   * @param end The end date/time of the activity
   * @param nodeID GridNodeID of the other party involved.
   * @param nodeName GridNode Name of the other party involved.
   * @param status Status of the activity when it ends.
   */
  public ConnectionData(String activity, Date start, Date end,
                        String nodeID, String nodeName,
                        String status)
  {
    set(FIELD_ACTIVITY, activity);
    set(FIELD_NODE_NAME, nodeName);
    set(FIELD_NODE_ID, nodeID);
    set(FIELD_STATUS, status);
    set(FIELD_DT_ACTIVITY_START, start);
    set(FIELD_DT_ACTIVITY_END, end);
  }

  /**
   * Constructs a Connection data for an unexpected activity: e.g.
   * Lost connection to GM, Partner uncontactable.
   *
   * @param activity The activity that happened
   * @param occur The occur date/time of the activity
   * @param nodeID GridNodeID of the other party involved.
   * @param nodeName GridNode Name of the other party involved.
   */
  public ConnectionData(String activity, Date occur,
                        String nodeID, String nodeName)
  {
    set(FIELD_NODE_NAME, nodeName);
    set(FIELD_NODE_ID, nodeID);
    set(FIELD_DT_OCCUR, occur);
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    ArrayList list = new ArrayList();
    list.add(FIELD_ACTIVITY);
    list.add(FIELD_NODE_NAME);
    list.add(FIELD_NODE_ID);
    list.add(FIELD_STATUS);
    list.add(FIELD_DT_ACTIVITY_START);
    list.add(FIELD_DT_ACTIVITY_END);
    list.add(FIELD_DT_OCCUR);

    return list;
  }
}