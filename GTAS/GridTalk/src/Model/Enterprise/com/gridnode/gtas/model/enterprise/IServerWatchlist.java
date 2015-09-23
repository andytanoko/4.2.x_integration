/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServerWatchlist.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Add more status for the server.
 */
package com.gridnode.gtas.model.enterprise;

/**
 * This interface keeps all constants required to access the Server watchlist.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IServerWatchlist
{
  static final int MAIN_INDEX_STATUS      = 0;
  static final int MAIN_INDEX_PARTNER     = 1;
  static final int MAIN_INDEX_GRIDMASTER  = 2;

  static final int PARTNER_INDEX_ID       = 0;
  static final int PARTNER_INDEX_NAME     = 1;
  static final int PARTNER_INDEX_UID      = 2;
  static final int PARTNER_INDEX_STATUS   = 3;

  static final int GRIDMASTER_INDEX_NODEID  = 0;
  static final int GRIDMASTER_INDEX_NAME    = 1;
  static final int GRIDMASTER_INDEX_UID     = 2;

  /**
   * Possible value for STATUS. This indicates that the Partner or this GridTalk is
   * currently online.
   */
  static final Short STATUS_ONLINE        = new Short("0");

  /**
   * Possible value for STATUS. This indicates that the Partner or this GridTalk is
   * currently offline.
   */
  static final Short STATUS_OFFLINE       = new Short("1");

  /**
   * Possible value for STATUS_FLAG. This indicates that the Partner's
   * connection has been broken.
   */
  static final Short STATUS_EXPIRED       = new Short("2");

  /**
   * Possible value for STATUS. This indicates that the system is in the process
   * of determining the state of the Partner's connection
   */
  static final Short STATUS_DETERMINING   = new Short("3");

  /**
   * Possible value for STATUS. This indicates that the state of the Partner
   * cannot be determined. Could be the partner is contactable only through
   * Http.
   */
  static final Short STATUS_UNKNOWN       = new Short("4");

  /**
   * Possible value for STATUS_FLAG. This indicates that this GridTalk is
   * currently making an attempt to connect.
   */
  static final Short STATUS_CONNECTING    = new Short("5");

  /**
   * Possible value for STATUS_FLAG. This indicates that this GridTalk is
   * currently making an attempt to re-establish a connection.
   */
  static final Short STATUS_RECONNECTING  = new Short("6");

  /**
   * Possible value for STATUS_FLAG. This indicates that this GridTalk is
   * currently making an attempt to disconnect.
   */
  static final Short STATUS_DISCONNECTING = new Short("7");


}