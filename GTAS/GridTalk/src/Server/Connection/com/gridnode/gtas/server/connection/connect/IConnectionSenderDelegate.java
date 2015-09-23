/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionSenderDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import java.io.File;

/**
 * This interface defines the methods to be implemented by a Delegate for
 * handling one step of a Connection process. Each step will handle
 * sending one particular message.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IConnectionSenderDelegate
{
  /**
   * Start executing the step.
   * A message will be sent during the execution, and if acknowledgement is required
   * the Delegate should wait for the reply before continuing with the rest of
   * the processing.
   */
  void execute() throws Throwable;

  /**
   * Invoked on receive of the acknowledgement for the message it sent earlier.
   * If no acknowledgement is required, this method should throw a
   * UnsupportedOperationException.
   */
  void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable;

  /**
   * Invoked on receive of the feedback for the message it sent earlier.
   *
   * @param success Whether the message was sent successfully.
   * @param message Feedback message.
   */
  void receiveFeedback(boolean success, String message) throws Throwable;

}