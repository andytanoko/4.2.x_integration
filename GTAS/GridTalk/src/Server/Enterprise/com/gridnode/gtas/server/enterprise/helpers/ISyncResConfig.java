/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISyncResConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.helpers;

/**
 * This interface defines the constants for the SyncResource configuration
 * properties.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface ISyncResConfig
{
  static final String CONFIG_NAME = "res.sync";

  /**
   * List of resource types that can be synchronized.
   * Defined in this format: resource.types=typeA,typeB,typeC,...
   */
  static final String RESOURCE_TYPES  = "resource.types";

  /**
   * The class name of the ISyncResourceHandler that can handle the
   * synchronization of a type of resource.
   * Defined in this format: <p>
   * sync.handler.typeA=typeASyncHandler<br>
   * sync.handler.typeB=typeBSyncHandler<br>
   * sync.handler.typeC=typeCSyncHandler<br>
   * :<br>
   * :<br>
   */
  static final String SYNC_HANDLER    = "sync.handler.";

  static final String SEND_OR_ROUTE_MSGID = "sendOrRoute.msgID.";

  static final String SEND_ONLY_MSGID     = "sendOnly.msgID.";

  static final String ROUTE_MSGID         = "route.msgID.";

  static final String RECEIVE_ONLY_MSGID  = "receiveOnly.msgID.";

  static final String IS_TEST             = "is.test";

  static final int CONTENT_MSGID_IDX     = 0;
  static final int ACK_MSGID_IDX      = 1;
  static final int DEST_TYPE_IDX      = 2;
  static final int REC_OPTION_IDX     = 3;

}