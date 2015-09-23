/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionReceiverDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import java.io.File;

/**
 * This interface defines the methods to be implemented by a Delegate to handle
 * a particular message. Each subclass that implement this interface should
 * handle one and only one type of message.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IConnectionReceiverDelegate
{
  /**
   * Invoked on receive of a message that the delegate should handle.
   *
   * @param eventSubID Event Sub ID of the message. To be used for decide
   * sub-category of the same type of message received (same Event ID but still
   * needs to differentiate due to process may be different).
   * @param refTransID Reference TransID of the received message.
   * @param dataPayload The data payload received.
   * @param filePayload The file payload received.
   */
  void receive(
    String eventSubID, String refTransID, String[] dataPayload, File[] filePayload)
    throws Throwable;

}