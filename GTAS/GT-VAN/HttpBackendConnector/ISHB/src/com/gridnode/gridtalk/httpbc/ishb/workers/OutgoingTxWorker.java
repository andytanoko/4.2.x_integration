/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OutgoingTxWorker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Feb 06 2007    i00107              Add event notification.
 * Feb 24 2007    i00107              Set send status as event remark.
 * Mar 17 2007    i00107              Use BackendImporter instead of BackendSender.
 */

package com.gridnode.gridtalk.httpbc.ishb.workers;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.EventNotifier;
import com.gridnode.gridtalk.httpbc.ishb.senders.BackendImporter;
import com.gridnode.gridtalk.httpbc.ishb.senders.BackendSender;
import com.gridnode.gridtalk.httpbc.ishb.senders.SenderException;
import com.gridnode.util.ExceptionUtil;

/**
 * @author i00107
 * This TransactionWorker is responsible for handling Outgoing transactions.
 */
public class OutgoingTxWorker extends TransactionWorker
{
  public OutgoingTxWorker()
  {
    super(TransactionDoc.DIRECTION_OUT);
  }
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.workers.TransactionWorker#processTransaction(com.gridnode.gridtalk.httpbc.common.model.TransactionDoc)
   */
  protected void processTransaction(TransactionDoc doc)
  {
    //BackendSender sender = new BackendSender();
    BackendImporter sender = new BackendImporter();
    try
    {
      sender.send(doc);
      removeCurrTransaction(doc);
      EventNotifier.getInstance().onDocumentDelivered(TARGET_GATEWAY, doc, getStatusStr(
                                                                                        //sender.getReturnCode()
                                                                                        sender.getStatus()
                                                                                        ));
    }
    catch (SenderException ex)
    {
      getLogger().logError(ILogErrorCodes.OUTGOING_TRANSACTION_PROCESS, "processTransaction", null, "Error sending document", ex);
      pendCurrTransactionForLater(doc);
      EventNotifier.getInstance().onDocumentDeliveryFailed(TARGET_GATEWAY, doc, 
                                                           "Unable to send document via Backend Import: "+
                                                           ExceptionUtil.getStackStraceStr(ex),
                                                           getStatusStr(
                                                                        //sender.getReturnCode()
                                                                        sender.getStatus()
                                                                        )); 
    }    
    catch (Throwable t)
    {
      getLogger().logError(ILogErrorCodes.OUTGOING_TRANSACTION_PROCESS, "processTransaction", null, "Unexpected error: "+t.getMessage(), t);
      pendCurrTransactionForLater(doc);
      EventNotifier.getInstance().onDocumentDeliveryFailed(TARGET_GATEWAY, doc, 
                                                           "Unable to send document via Backend Import: "+
                                                           ExceptionUtil.getStackStraceStr(t),
                                                           getStatusStr(
                                                                        //sender.getReturnCode()
                                                                        sender.getStatus()
                                                                        )); 
    
    }
  }

  private String getStatusStr(int retCode)
  {
    return "Sender Return Code: " + retCode;
  }

  private String getStatusStr(boolean status)
  {
    return "Backend Import status: " + (status?"success" : "failed");
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.workers.TransactionWorker#getName()
   */
  @Override
  protected String getName()
  {
    return "OutgoingTxWorker";
  }

}
