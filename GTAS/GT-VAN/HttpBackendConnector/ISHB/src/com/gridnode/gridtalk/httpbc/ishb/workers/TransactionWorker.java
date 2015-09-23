/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionWorker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 05 2007    i00107              commitTransaction() if no more tx after query.
 *                                    Add MessageID in alert.
 * Feb 06 2007    i00107              Add Run options.           
 * Mar 05 2007		Alain Ah Ming				Added error code to error logs                        
 * Feb 06 2007    i00107              Add Run options.   
 * Mar 05 2007    i00107              If MaxProcessCount is negative, do not loop, just perform once.                                
 */

package com.gridnode.gridtalk.httpbc.ishb.workers;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IAlertKeys;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.ishb.model.TransactionDAO;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This is an abstract implementation of a Worker for handling a type of transaction.
 */
public abstract class TransactionWorker
{
  /**
   * Constant for delivery target: Backend
   */
  public static final String TARGET_BACKEND = "Backend";
  /**
   * Constant for delivery target: Gateway
   */
  public static final String TARGET_GATEWAY = "Gateway";
  
  private TransactionDAO _dao;
  private String _type;
  private Logger _logger;
  private boolean _stopHandling;
  private int _maxProcessCount = 10;
  private int _maxFailedAttempts = 99;
  private int _alertThreshold = 20;
  
  /**
   * Run the worker.
   */
  public void run()
  {
    try
    {
      startWork();
    }
    catch (Exception ex)
    {
      _logger.logError(ILogErrorCodes.TRANSACTION_PROCESS, "run", null, "Error from startWork", ex);
    }
  }
  
  /**
   * Set the run options for the worker.
   * @param maxProcessCount Maximum number of tx to deliver successfully. 0 or less will result in no
   * delivery of any tx.
   * @param maxFailedAttempts Maximum number of times a tx can fail delivery.
   * @param alertThreshold Every number of failed deliveries to send alert.
   */
  public void setRunOptions(int maxProcessCount, int maxFailedAttempts, int alertThreshold)
  {
    _maxProcessCount = maxProcessCount;
    _maxFailedAttempts = maxFailedAttempts;
    _alertThreshold = alertThreshold;
  }
  
  /**
   * Constructs an instance of TransactionWorker to process the specified type of transactions.
   * @param type The type of transactions to process.
   */
  public TransactionWorker(String type)
  {
    _type = type;
    _dao = new TransactionDAO(_type);
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, getName());
  }
  
  /**
   * Get the logger for the transaction worker.
   * @return The Logger for the TransactionWorker.
   */
  protected Logger getLogger()
  {
    return _logger;
  }
  
  /**
   * Get the name of the TransactionWorker. This is an arbituary name to identify the TransactionWorker.
   * @return The name of the TransactionWorker.
   */
  protected abstract String getName();
  
  /**
   * Start working on processing the transactions it is task to do.
   * It will continue processing as long as there are transactions available for
   * process and no processing problem has occurred.
   */
  protected void startWork()
  {
    TransactionDoc tDoc;
    int count = 0;
    if (_maxProcessCount < 0) //NSL20070305
    {
      tDoc = getNextTransaction();
      if (tDoc != null)
      {
        processTransaction(tDoc);
      }
      else
      {
        _stopHandling = true;
      }
    }
    else
    {
      _stopHandling = count >= _maxProcessCount;
      while (!_stopHandling && (tDoc=getNextTransaction())!=null)
      {
        processTransaction(tDoc);
        _stopHandling = (_stopHandling || (++count >= _maxProcessCount));
      }
    }
  }
  
  public boolean isStopHandling()
  {
    return _stopHandling;
  }
  
  /**
   * Get the next transaction document to process.
   * @return The next transaction document available for processing.
   */
  protected TransactionDoc getNextTransaction()
  {
    return _dao.nextTx(_maxFailedAttempts);
  }
  
  /**
   * Remove the current transaction document that is being processed. This should be
   * called only after successful processing of the document.
   * @param doc The transaction document to remove.
   */
  protected void removeCurrTransaction(TransactionDoc doc)
  {
    _dao.removeCurrTx();
  }
  
  /**
   * Process the transaction document.
   * 
   * @param doc The transaction document to process.
   */
  protected abstract void processTransaction(TransactionDoc doc);
  
  /**
   * Pend the current transaction for later processing. This should be called only after
   * unsuccessful processing of the document. If there are too many unsuccessful attempts made
   * to process the document, an alert will be sent this. This is done for every N number of unsuccessful attempts.
   * @param doc The transaction document to pend for later processing.
   */
  public void pendCurrTransactionForLater(TransactionDoc doc)
  {
    int attempt = _dao.pendCurrTx();
    int limit = _alertThreshold; 
    if (attempt % limit == 0)
    {
      //every limit attempts, alert
      alertTooManyAttempts(doc, attempt);
    }
    _stopHandling = true;
  }
  
  /**
   * Send an alert informing that there are N unsuccessful attempts to process a document.
   *  
   * @param doc The document that failed in processing.
   * @param count The number of unsuccessful attempts made so far.
   */
  public void alertTooManyAttempts(TransactionDoc doc, int count)
  {
    AlertUtil.getInstance().sendAlert(IAlertKeys.TOO_MANY_FAILED_ATTEMPTS,
                                      doc.getTracingId(),
                                      doc.getDirection(),
                                      doc.getBizEntId(),
                                      doc.getPartnerId(),
                                      doc.getDocContent(),
                                      doc.getMessageID(),
                                      count);
  }
  
  /**
   * The limit for the number of the attempts before sending an alert.
   * @return The limit for the number of unsuccessful attempts. For every number of such attempts, an alert will be
   * sent out.
   */
//  private int getAttemptCountLimit()
//  {
//    //TODO to be reviewed
//    return ConfigurationStore.getInstance().getIntProperty(IConfigCats.ALERT, "failed.attempts.limit", 20);
//  }
}
