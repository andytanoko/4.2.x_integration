/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionStatus.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.model;

/**
 * This interface defines the field IDs and constants for ConnectionStatus
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IConnectionStatus
{
  /**
   * Name of the ConnectionStatus entity.
   */
  static final String ENTITY_NAME     = "ConnectionStatus";

  /**
   * FieldID for UID. A Long.
   */
  static final Number UID             = new Integer(0);

  /**
   * FieldID for GridNode ID. A String.
   */
  static final Number GRIDNODE_ID     = new Integer(1); // String(10)

  /**
   * FieldID for StatusFlag. A Short.
   */
  static final Number STATUS_FLAG     = new Integer(2);

  /**
   * FieldID for DTLastOnline. A Timestamp.
   */
  static final Number DT_LAST_ONLINE  = new Integer(3);

  /**
   * FieldID for DTLastOffline. A Timestamp.
   */
  static final Number DT_LAST_OFFLINE = new Integer(4);

  /**
   * FieldID for ReconnectionKey. A String
   */
  static final Number RECONNECTION_KEY= new Integer(5);

  /**
   * FieldID for ConnectedServerNode. A String.
   */
  static final Number CONNECTED_SERVER_NODE = new Integer(6);


  // values for STATUS_FLAG

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode is
   * currently online.
   */
  static final short STATUS_ONLINE        = 0;

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode is
   * currently offline.
   */
  static final short STATUS_OFFLINE       = 1;

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode is
   * currently making an attempt to connect.
   */
  static final short STATUS_CONNECTING    = 2;

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode is
   * currently making an attempt to re-establish a connection.
   */
  static final short STATUS_RECONNECTING  = 3;

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode is
   * currently making an attempt to disconnect.
   */
  static final short STATUS_DISCONNECTING = 4;

  /**
   * Possible value for STATUS_FLAG. This indicates that the GridNode's
   * connection has been broken.
   */
  static final short STATUS_EXPIRED       = 5;

}