/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 * Nov 29 2002    Neo Sok Lay         Add reconnecting state.
 * Jan 23 2003    Neo Sok Lay         Add My GNCI string.
 */
package com.gridnode.gtas.server.notify;

/**
 * Notification of the Connection status of a GridNode.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * State    - (1) Online or (2) Offline (3) Reconnecting
 *          - Can be used for message selection, e.g. state='1'
 * NodeType - (1) GM - GridMaster (2) ME - This GridTalk (3) Partner
 *          - Can be used for message selection, e.g. nodeType='2'
 * Node     - GridNode ID of the GridNode in the context
 * MyGNCI   - GridNodeCommInfo string for this GridTalk.
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I6
 */
public class ConnectionNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7852254376720750458L;
	public static final short STATE_ONLINE        = 1;
  public static final short STATE_OFFLINE       = 2;
  public static final short STATE_CONNECTING    = 3;
  public static final short STATE_RECONNECTING  = 4;
  public static final short STATE_DISCONNECTING = 5;

  public static final short TYPE_GM       = 1;
  public static final short TYPE_ME       = 2;
  public static final short TYPE_PARTNER  = 3;

  private short _state;
  private short _nodeType;
  private String _node;
  private String _myGNCI;

  public ConnectionNotification(short state, short nodeType, String node)
  {
    _state = state;
    _nodeType = nodeType;
    _node = node;
    putProperty("state", String.valueOf(state));
    putProperty("nodeType", String.valueOf(nodeType));
  }

  /**
   * Construct a ConnectionNotification object for a GridNode.
   *
   * @param state The state of the GridNode
   * @param nodeType The type of the GridNode e.g TYPE_GM, TYPE_ME, TYPE_PARTNER
   * @param node The GridNode ID of the GridNode
   * @param myGNCI GridNodeCommInfo string for my GridNode. Applicable only if
   * nodeType=TYPE_GM and state=STATE_ONLINE. <b>null</b> if not applicable.
   */
  public ConnectionNotification(
    short state,
    short nodeType,
    String node,
    String myGNCI)
  {
    this(state, nodeType, node);
    _myGNCI = myGNCI;
  }

  public short getState()
  {
    return _state;
  }

  public short getNodeType()
  {
    return _nodeType;
  }

  public String getNode()
  {
    return _node;
  }

  public String getMyGNCI()
  {
    return _myGNCI;
  }

  public String getNotificationID()
  {
    return "Connection";
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer(getNotificationID());
    buff.append(" - Node[").append(getNode()).append("], Type[");
    buff.append(getNodeType()).append("], State[").append(getState()).append("]");

    return buff.toString();
  }
}