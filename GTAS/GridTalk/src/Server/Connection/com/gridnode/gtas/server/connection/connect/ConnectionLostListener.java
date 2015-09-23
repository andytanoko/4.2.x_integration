/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionLostListener.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;

/**
 * Listener for Connection lost feedback.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ConnectionLostListener
  implements IReceiveFeedbackHandler
{

  public ConnectionLostListener()
  {
  }

  public void handlerFeedback(String[] header, boolean success, String message)
  {
    try
    {
      Logger.log("[ConnectionLostListener.handlerFeedback] "+
        "Connection lost due to: "+message);

      ServiceLookupHelper.getConnectionService().onConnectionLost(header, message);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_LOST_LISTENER,
        "[ConnectionLostListener.handlerFeedback] Error handling connection lost",
        ex);

    }
  }

}