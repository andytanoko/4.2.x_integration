/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionMessageReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.
 * Oct 01 2003    Neo Sok Lay         use ChannelReceiveHeader to index
 *                                    the header array.		
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.helpers.Logger;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * Receiver for messages during the Connection process. The
 * receiver will call the
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class ConnectionMessageReceiver
  implements IReceiveMessageHandler
{

  public ConnectionMessageReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[ConnectionMessageReceiver.handlerMessage] "+
        "Received message for Event "+header[ChannelReceiveHeader.EVENT_ID_POS]);

      ServiceLookupHelper.getConnectionService().receiveConnectionMessage(
        header[ChannelReceiveHeader.EVENT_ID_POS], 
        header[ChannelReceiveHeader.EVENT_SUB_ID_POS], 
        header[ChannelReceiveHeader.TRANSACTION_ID_POS], 
        dataReceived, 
        filesReceived);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_MESSAGE_RECEIVER,
        "[ConnectionMessageReceiver.handlerMessage] Error processing received message",
        ex);
      /**@todo raise alert/log in future */
    }
  }


}