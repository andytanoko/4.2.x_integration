/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivationProcessKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

/**
 * This interface contains the contants required by the Activation process
 * handlers.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IActivationProcessKeys
  extends        IPathConfig
{
  /**
   * Name for the Activation configuration file.
   */
  public static final String CONFIG_NAME = "activation";

  /**
   * Event ID for sending/receiving Search GridNode messages,
   */
  public static final String SEARCH_EVENT_ID          = "search.event.id";

  /**
   * Event ID for sending/receiving Activation request messages.
   */
  public static final String REQUEST_EVENT_ID         = "request.event.id";

  /**
   * Event ID for sending/receiving Activation request acknowledgment messages.
   */
  public static final String REQUEST_ACK_EVENT_ID     = "request.ack.event.id";

  /**
   * Event ID for sending/receiving Activation reply messages.
   */
  public static final String REPLY_EVENT_ID           = "reply.event.id";

  /**
   * Event ID for sending/receiving Activation reply acknowledgement messages.
   */
  public static final String REPLY_ACK_EVENT_ID       = "reply.ack.event.id";

  /**
   * Event ID for sending/receiving Activation Abort messages.
   */
  public static final String ABORT_EVENT_ID           = "abort.event.id";

  /**
   * Event ID for sending/receiving Activation Abort acknowledgement messages.
   */
  public static final String ABORT_ACK_EVENT_ID       = "abort.ack.event.id";

  /**
   * Event ID for sending/receiving Deactivation request messages.
   */
  public static final String DEACT_EVENT_ID           = "deact.event.id";

  /**
   * Event ID for sending/receiving Deactivation request acknowledgment messages.
   */
  public static final String DEACT_ACK_EVENT_ID       = "deact.ack.event.id";

  /**
   * File extension for Search file.
   */
  public static final String SEARCH_FILE_EXT          = ".filter";

  /**
   * File prefix for Search file.
   */
  public static final String SEARCH_FILE_PREFIX       = "search";

  /**
   * File extension for Activation file.
   */
  public static final String ACTIVATION_FILE_EXT      = ".act";

  /**
   * File prefix for Activation request file.
   */
  public static final String REQUEST_FILE_PREFIX      = "reqAct_";

  /**
   * File prefix for Activation approval file.
   */
  public static final String APPROVE_FILE_PREFIX      = "approveReq_";

  /**
   * File prefix for Activation abort file.
   */
  public static final String ABORT_FILE_PREFIX        = "abortReq_";

  /**
   * File prefix for Deactivation request file.
   */
  public static final String DEACT_FILE_PREFIX        = "reqDeact_";

  /**
   * File prefix for Activation denial file.
   */
  public static final String DENY_FILE_PREFIX         = "denyReq_";


}