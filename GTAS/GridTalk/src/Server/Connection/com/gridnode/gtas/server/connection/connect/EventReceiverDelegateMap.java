/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventReceiverDelegateMap.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

/**
 * Maps the EventIDs to the ConnectionReceiverDelegates.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class EventReceiverDelegateMap
{
  /**
   * Obtain the receiver Delegate to handle the receive of messages of the
   * event ID.
   *
   * @param eventID The event ID.
   * @return the receiver delegate.
   * @throws Exception No receiver delegate suitable to handle the message.
   */
  public static IConnectionReceiverDelegate getReceiverDelegate(String eventID)
    throws Throwable
  {
    ConnectionProperties props = DelegateHelper.getProperties();
    if (eventID.equals(props.getOnlineNotificationEventId()))
      return new ReceiveOnlineNotificationDelegate(ConnectionContext.getInstance());
    else if (eventID.equals(props.getOfflineNotificationEventId()))
      return new ReceiveOfflineNotificationDelegate(ConnectionContext.getInstance());
    else if (eventID.equals(props.getConfirmAliveEventId()))
      return new ReceiveConfirmAliveDelegate(ConnectionContext.getInstance());
    else
      throw new Exception("No Delegate to handle the received message: "+eventID);
  }

}