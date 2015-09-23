/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentAckHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30 2002    Koh Han Sing        Created
 * Oct 28 2002    Neo Sok Lay         Modified handlerMessage() params in
 *                                    IReceiveMessageHandler.
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to
 *									  include Hashtable additionalHeader.
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader to index the
 *                                    header array.
 */
package com.gridnode.gtas.server.document.handlers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.IDocumentEvents;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.Hashtable;

public class ReceiveDocumentAckHandler
       implements IReceiveMessageHandler
{

  public ReceiveDocumentAckHandler()
  {
  }

  public IDocumentManagerObj getDocumentManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  public void handlerMessage(/*String eventID,*/
                             String[] header,
                             String[] dataReceived,
                             File[] filesReceived,
                             Hashtable additionalHeader)
  {
    try
    {
      Logger.debug("[ReceiveDocumentAckHandler.handlerMessage] Receiving document");
      String eventID = header[ChannelReceiveHeader.EVENT_ID_POS];
      String trxID   = header[ChannelReceiveHeader.TRANSACTION_ID_POS];
      //getDocumentManager().receiveDocAck(
      //  eventID, dataReceived);
      handleAcknowledgement(eventID, trxID, dataReceived);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_RECEIVE_DOC_ACK_HANDLER,
                   "[ReceiveDocumentAckHandler.handlerMessage] Error in receiving document: "+ex.getMessage(), ex);
    }
  }

  protected void handleAcknowledgement(
    String eventID, String trxID, String[] dataReceived) throws Throwable
  {
    if (IDocumentEvents.SEND_GRIDDOC_ACK.equals(eventID))
    {
      getDocumentManager().handleSendDocAck(trxID, dataReceived);
    }
    else if (IDocumentEvents.UPLOAD_GRIDDOC_ACK.equals(eventID))
    {
      getDocumentManager().handleUploadDocAck(trxID);
    }
    else if (IDocumentEvents.TRANS_COMPLETED.equals(eventID))
    {
      getDocumentManager().handleDocTransCompleted(trxID, dataReceived);
    }
  }

}