/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidActivationRecordStateException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.exceptions;

 
/**
 * This exception indicates an invalid state of an Activation record for
 * proceeding with Activate/Approve/Abort/Deny/Deactivate.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class InvalidActivationRecordStateException
  extends    GridNodeActivationException
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1822898634725338108L;
	private static final String ILLEGAL_DEACTIVATION = "Illegal Deactivation Attempt! ";
  private static final String ILLEGAL_ACTIVATION   = "Illegal Activation Attempt! ";
  private static final String ILLEGAL_CANCELLATION = "Illegal Cancellation Attempt! ";
  private static final String ILLEGAL_REPLY        = "Illegal Approval/Denial Attempt!";

  public InvalidActivationRecordStateException(String msg)
  {
    super(msg);
  }

  public InvalidActivationRecordStateException(String msg, Throwable t)
  {
    super(msg, t);
  }

  public InvalidActivationRecordStateException(Throwable t)
  {
    super(t);
  }


  public static InvalidActivationRecordStateException illegalDeactivation(String reason)
  {
    return new InvalidActivationRecordStateException(
                ILLEGAL_DEACTIVATION + reason);
  }

  public static InvalidActivationRecordStateException illegalActivation(String reason)
  {
    return new InvalidActivationRecordStateException(
                ILLEGAL_ACTIVATION + reason);
  }

  public static InvalidActivationRecordStateException illegalCancellation(String reason)
  {
    return new InvalidActivationRecordStateException(
                ILLEGAL_CANCELLATION + reason);
  }

  public static InvalidActivationRecordStateException illegalReply(String reason)
  {
    return new InvalidActivationRecordStateException(
                ILLEGAL_REPLY + reason);
  }

}