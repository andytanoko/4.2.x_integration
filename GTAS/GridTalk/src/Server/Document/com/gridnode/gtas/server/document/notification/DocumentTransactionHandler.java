/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTransactionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10, 2006    Tam Wei Xiang       Created
 * Jan 10, 2007    Tam Wei Xiang       Added flag isRetry
 * Jul 25  2008    Tam Wei Xiang        #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.
 */
package com.gridnode.gtas.server.document.notification;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.notification.Notifier;

/**
 * This class will provide the services for sending out the notification (indicate the creation
 * of IB or OB document)
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DocumentTransactionHandler
{
  
  /**
   * Indicate the creation of the IB or OB document.
   * @param gdoc
   * @param isDuplicate indicate whether we are receiving the same doc from Partner
   * @param isRetry indicate whether we resend the doc from GT.
   */
  public static void triggerDocumentTransaction(GridDocument gdoc, boolean isDuplicate, boolean isRetry) throws SystemException
  {
    if(gdoc == null)
    {
      NullPointerException ex = new NullPointerException("[DocumentTransactionHandler.triggerDocumentTransaction] gdoc is null");
      Logger.warn("[DocumentTransactionHandler.triggerDocumentTransaction] gdoc is null, no DocumentTransaction notification will be triggered !", ex);
      return;
    }
    
    String tracingID = gdoc.getTracingID();
    if(tracingID == null || "".equals(tracingID))
    {
      ApplicationException ex = new ApplicationException("[DocumentTransactionHandler.triggerDocumentTransaction] TracingID is null or empty !");
      Logger.warn("[DocumentTransactionHandler.triggerDocumentTransaction] tracingID is empty , no DocumentTransaction notification will be triggered !", ex);
      return;
    }
    
    //TODO the state of the GDOC may vary while processing in OTC-plug-in eg the udoc file may vaary !!
    GridDocument newGdoc = (GridDocument)gdoc.clone();
    DocumentTransactionNotification notification = createDocumentTransaction(newGdoc, isDuplicate, isRetry);
    broadcastNotification(notification);
  }
  
  private static DocumentTransactionNotification createDocumentTransaction(GridDocument gdoc, boolean isDuplicate, boolean isRetry)
  {
    return new DocumentTransactionNotification(gdoc, isDuplicate, isRetry);
  }
  
  private static void broadcastNotification(DocumentTransactionNotification notification) throws SystemException
  {
    try
    {
      if(JMSRetrySender.isSendViaDefMode())
      {
        Notifier.getInstance().broadcast(notification);
      }
      else
      {
        JMSRetrySender.broadcast(notification);
      }
    }
    catch(Exception ex)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 TWX 25072008
      {  	  
      	  throw new SystemException("Error in broadcasting Document Transaction: "+notification, ex);
      }
      Logger.error(ILogErrorCodes.GT_DOC_TRANSACTION_HANDLER,
                "[DocumentTransactionHandler.broadcastNotification] error in broadcasting the document notification: "+notification+". Error: "+ex.getMessage(), ex);
    }
  }
}
