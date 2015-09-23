/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentStatusBroadcaster.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 19 2003    Neo Sok Lay         Created
 * Jul 26 2008    Tam Wei Xiang       #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.notify.DocumentActivityNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.INotification;

/**
 * This helper class is responsible for broadcasting when 
 * document status changes.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class DocumentStatusBroadcaster
{
 
  /**
   * When sending has started on a document.
   * 
   * @param gdoc The GridDocument of the document that has started sending. 
   */ 
  public static void documentSendStart(GridDocument gdoc)throws Exception
  {
    broadcast(DocumentActivityNotification.TYPE_START_SEND, gdoc);
  }

  /**
   * When the sending has sended on a document. This means that
   * the document has successfully been sent out of this system.
   * However, this does not guarantee that intended recipient partner
   * system has received the document. This only means that the immediate recipient, 
   * which maybe a middleman, has received the document.
   * 
   * @param gdoc The GridDocument of the document that has been sent
   */  
  public static void documentSendEnd(GridDocument gdoc)throws Exception
  {
    broadcast(DocumentActivityNotification.TYPE_END_SEND,gdoc);
  }

  /**
   * When the document has been successfully received and accepted for
   * processing by the intended recipient partner system.
   * 
   * @param gdoc The GridDocument of the document that has been successfully
   * received by the intended recipient partner system.
   */
  public static void documentTransCompleted(GridDocument gdoc)throws Exception
  {
    broadcast(DocumentActivityNotification.TYPE_END_TRANS, gdoc);
  }

  /**
   * When the document has been received by the intended recipient partner system
   * but rejected for processing.
   * 
   * @param gdoc The GridDocument of the document that has been rejected
   * for processing by the intended recipient partner system.
   */
  public static void documentTransRejected(GridDocument gdoc) throws Exception
  {
    broadcast(DocumentActivityNotification.TYPE_REJECT_TRANS, gdoc);
  }

  /**
   * Broadcast a DocumentActivityNotification based on the specified
   * type and document.
   * 
   * @param type The type of activity.
   * @param gdoc The GridDocument
   */
  private static void broadcast(short type, GridDocument gdoc) throws Exception
  {
    try
    {
      if(! JMSRetrySender.isSendViaDefMode())
      {
        //Logger.debug("DocumentStatusBroadcaster.broadcast via JMSRetrySender");
        JMSRetrySender.broadcast(new DocumentActivityNotification(type, gdoc));
      }
      else
      {
        Notifier.getInstance().broadcast(new DocumentActivityNotification(type, gdoc));
      }
    }
    catch (Exception ex)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  throw ex;
      }
      Logger.error(ILogErrorCodes.GT_DOC_STATUS_BROADCASTER,
                   "[DocumentStatusBroadcaster.broadcast] Error: "+ex.getMessage(), ex);
    }
  }
}
