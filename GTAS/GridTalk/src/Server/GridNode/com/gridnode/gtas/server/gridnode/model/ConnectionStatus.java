/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionStatus.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.sql.Timestamp;

/**
 * This entity keeps track of the current connection status of a GridNode.
 * The data model:<p>
 * <PRE>
 * UID            - UID of this ConnectionStatus entity instance.
 * GridNodeID     - ID of the GridNode that this ConnectionStatus belongs to.
 * StatusFlag     - Flag for the connection status.
 * DTLastOnline   - Timestamp for the last time the GridNode was online.
 * DTLastOffline  - Timestamp for the last time the GridNode was offline.
 * ReconnectionKey- Some key for reconnection purpose.
 * ConnectedServerNode- NodeID of the connection server (GridMaster) that the
 *                      GridNode is connected to.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ConnectionStatus
  extends    AbstractEntity
  implements IConnectionStatus
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 587494104151993133L;
	private String    _nodeID;
  private short     _statusFlag           = STATUS_OFFLINE;
  private Timestamp _dtLastOnline;
  private Timestamp _dtLastOffline;
  private String    _reconnectionKey;
  private String    _connectedServerNode;

  public ConnectionStatus()
  {
  }

  public String getEntityDescr()
  {
    return getGridNodeID() + ":" + getStatusFlag();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // **************** Getters & Setters ***************************

  public void setGridNodeID(String nodeID)
  {
    _nodeID = nodeID;
  }

  public String getGridNodeID()
  {
    return _nodeID;
  }

  public void setStatusFlag(short status)
  {
    _statusFlag = status;
  }

  public short getStatusFlag()
  {
    return _statusFlag;
  }

  public void setDTLastOnline(Timestamp dtLastOnline)
  {
    _dtLastOnline = dtLastOnline;
  }

  public Timestamp getDTLastOnline()
  {
    return _dtLastOnline;
  }

  public void setDTLastOffine(Timestamp dtLastOffline)
  {
    _dtLastOffline = dtLastOffline;
  }

  public Timestamp getDTLastOffline()
  {
    return _dtLastOffline;
  }

  public void setReconnectionKey(String reconnectionKey)
  {
    _reconnectionKey = reconnectionKey;
  }

  public String getReconnectionKey()
  {
    return _reconnectionKey;
  }

  public void setConnectedServerNode(String serverNodeID)
  {
    _connectedServerNode = serverNodeID;
  }

  public String getConnectedServerNode()
  {
    return _connectedServerNode;
  }
}