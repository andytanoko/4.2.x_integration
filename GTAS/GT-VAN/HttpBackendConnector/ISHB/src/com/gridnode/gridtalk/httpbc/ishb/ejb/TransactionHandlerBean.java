/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionHandlerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00107              Created
 * Feb 06 2007    i00107              Add deliverIncomingTransaction() and
 *                                    deliverOutgoingTransaction().
 *                                    Also notify documentReceived for incoming 
 *                                    transactions.
 * Mar 05 2007    i00107              Add deliverIncomingTransaction() and
 *                                    deliverOutgoingTransaction() with max process count
 *                                    parameter..                                    
 * mar 05 2007		Alain Ah Ming				Log either error codes or warning   
 * Mar 17 2007    i00107              Add deliverIncomingTransaction() and
 *                                    deliverOutgoingTransaction() for single rec processing.                                
 */

package com.gridnode.gridtalk.httpbc.ishb.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.EventNotifier;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.ishb.model.TransactionDAO;
import com.gridnode.gridtalk.httpbc.ishb.workers.IncomingTxWorker;
import com.gridnode.gridtalk.httpbc.ishb.workers.OutgoingTxWorker;
import com.gridnode.gridtalk.httpbc.ishb.workers.TransactionWorker;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This SessionBean handles the persistence of incoming and outgoing
 * transactions for processing later on.
 */
public class TransactionHandlerBean implements SessionBean
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 6926958802119141576L;
  private SessionContext _sc;
  private Logger _logger;
  private TransactionDAO _dao;
  
  /**
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate()
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate()
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove()
  {
    _logger = null;
    _dao = null;
  }

  public void ejbCreate()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "TransactionHandlerBean");
    _dao = new TransactionDAO();
  }
  
  /**
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext arg0) 
  {
    _sc = arg0;
  }

  /**
   * @see ITransactionHandler#handleIncomingTransaction(TransactionDoc) 
   */
  public String handleIncomingTransaction(TransactionDoc tx)
  {
    String mtdName = "handleIncomingTransaction";
    Object[] params = {tx};
    
    _logger.logEntry(mtdName, params);
    
    _dao.associateTransactionContext(false);
    
    try
    {
      
      _dao.beginTransaction();

      String txNo = _dao.insertTx(tx);

      _dao.commitTransaction();
      _dao.flushSession(); //force commit to db so that I can catch the exception here
      
      //notify doc received only if successful save of transaction
      notifyTransactionReceived(TransactionWorker.TARGET_GATEWAY, tx);
      
      _dao.closeTransactionContext();
      
      String msg = "Saved Tx "+txNo+" with tracingId "+tx.getTracingId();
      _logger.logMessage(mtdName, params, msg);
      
      return txNo;
    }
    catch (Exception ex)
    {
      String msg = "Unable to save incoming Tx with tracingId "+tx.getTracingId();
      _logger.logWarn(mtdName, params, msg, null);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, ex);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  /**
   * @see ITransactionHandler#handleOutgoingTransaction(TransactionDoc)
   */
  public String handleOutgoingTransaction(TransactionDoc tx)
  {
    String mtdName = "handleOutgoingTransaction";
    Object[] params = {tx};
    
    _logger.logEntry(mtdName, params);
    
    _dao.associateTransactionContext(false);
    try
    {
      _dao.beginTransaction();
      
      String txNo = _dao.insertTx(tx);

      _dao.commitTransaction();
      _dao.flushSession();  //force commit to db so I can catch any exception here
      
      //notify doc received only if successful save of transaction
      notifyTransactionReceived(TransactionWorker.TARGET_BACKEND, tx);
      
      _dao.closeTransactionContext();

      String msg = "Saved Tx "+txNo+" with tracingId "+tx.getTracingId();
      _logger.logMessage(mtdName, params, msg);

      return txNo;
    }
    catch (Exception ex)
    {
      String msg = "Unable to save outgoing Tx with tracingId "+tx.getTracingId();
      _logger.logWarn(mtdName, params, msg, null);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, ex);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }

  /**
   * Notify the event that transaction has been received
   * @param source the source from which the transaction is received from.
   * @param tx The transaction document
   */
  private void notifyTransactionReceived(String source, TransactionDoc tx)
  {
    EventNotifier.getInstance().onDocumentReceived(source, tx);
  }

  /**
   * @see ITransactionHandler#deliverIncomingTransaction(int, int, int)
   */
  public void deliverIncomingTransaction(int maxProcessCount, int maxFailedAttempts, int alertThreshold)
  {
    String mtdName = "deliverIncomingTransaction";
    Object[] params = {maxProcessCount, maxFailedAttempts, alertThreshold};
    
    _logger.logEntry(mtdName, params);

    try
    {
      IncomingTxWorker worker = new IncomingTxWorker();
      worker.setRunOptions(maxProcessCount, maxFailedAttempts, alertThreshold);

      worker.run();
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  /**
   * @see ITransactionHandler#deliverOutgoingTransaction(int, int, int)
   */
  public void deliverOutgoingTransaction(int maxProcessCount, int maxFailedAttempts, int alertThreshold)
  {
    String mtdName = "deliverOutgoingTransaction";
    Object[] params = {maxProcessCount, maxFailedAttempts, alertThreshold};
    
    _logger.logEntry(mtdName, params);
    try
    {
      OutgoingTxWorker worker = new OutgoingTxWorker();
      worker.setRunOptions(maxProcessCount, maxFailedAttempts, alertThreshold);

      worker.run();
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  /**
   * @see ITransactionHandler#deliverIncomingTransaction(int, int)
   */
  public boolean deliverIncomingTransaction(int maxFailedAttempts, int alertThreshold)
  {
    IncomingTxWorker worker = new IncomingTxWorker();
    worker.setRunOptions(-1, maxFailedAttempts, alertThreshold);

    worker.run();
    return !worker.isStopHandling();
  }
  
  /**
   * @see ITransactionHandler#deliverOutgoingTransaction(int, int)
   */
  public boolean deliverOutgoingTransaction(int maxFailedAttempts, int alertThreshold)
  {
    OutgoingTxWorker worker = new OutgoingTxWorker();
    worker.setRunOptions(-1, maxFailedAttempts, alertThreshold);

    worker.run();
    return !worker.isStopHandling();
  }

}
