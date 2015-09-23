/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10, 2006   i00107              Created
 * Jan 05 2007    i00107              Use object update instead of update query
 *                                    to increment attemptCount
 * Jan 12 2007    i00107              Use TransactionContext.       
 * Feb 06 2007    i00107              Retrieve nextTx based on attemptCount.
 * Mar 05 2007		Alain Ah Ming				Added error code to error logs
 *                             
 */

package com.gridnode.gridtalk.httpbc.ishb.model;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.util.db.DAO;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This DAO handles the persistence for TxRec.
 */
public class TransactionDAO extends DAO
{
  private String _txDirection;
  private Logger _logger;
  private TxRec _currTx;
  
  /**
   * Constructs an instance of TransactionDAO
   */
  public TransactionDAO()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "TransactionDao");
  }
  
  /**
   * Constructs an instance of TransactionDAO for processing the transactions for the specified direction.
   * @param direction The direction of transactions to be processed.
   */
  public TransactionDAO(String direction)
  {
    this();
    _txDirection = direction;
  }

  /**
   * Insert the transaction doc for processing later. The transaction must
   * have started before calling this method.
   * @param tDoc The transaction document to insert
   * @return The txNo of the inserted transaction doc.
   */
  public String insertTx(TransactionDoc tDoc)
  {
    String mtdName = "insertTx";
    Object[] params = {tDoc};
    
    _logger.logEntry(mtdName, params);
    TxRec tx = new TxRec();
    tx.setDirection(tDoc.getDirection());
    tx.setTracingId(tDoc.getTracingId());
    tx.setTxDoc(tDoc);
    try
    {
      String txNo = (String)create(tx);
      return txNo;
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }

  /**
   * Get the next transaction doc available for processing. Only transaction with attemptCounts not exceeding
   * maxFailedAttempts will be returned.
   * @param maxFailedAttempts the Maximum number of failed attempts
   * @return The next transaction doc, of the direction that the DAO is set to process, available for processing. <b>null</b> if
   * no more transaction doc available for processing.
   */
  public TransactionDoc nextTx(int maxFailedAttempts)
  {
    associateTransactionContext(false);
    try
    {
      beginTransaction();
      TxRec tx = getNextTx(_txDirection, maxFailedAttempts);
      if (tx != null)
      {
        _currTx = tx;
        _logger.debugMessage("nextTx",null, "Retrieved "+_currTx);
        return tx.getTxDoc();
      }
      else
      {
        _logger.debugMessage("nextTx", null, "No more "+_txDirection+" transactions to process.");
        commitTransaction();
        _currTx = null;
        return null;
      }
    }
    catch (Exception ex)
    {
      _logger.logError(ILogErrorCodes.TRANSACTION_QUERY, "nextTx", null, "Unexpected error: "+ex.getMessage(), ex);
      if (hasTransaction())
      {
        rollbackTransaction();
      }
      _currTx = null;
      return null;
    }
    finally
    {
      if (_currTx == null) //no more tx
      {
        closeTransactionContext();
      }
    }
  }
  
  /**
   * Remove from database the current transaction doc that is being processed.
   */
  public void removeCurrTx()
  {
    String mtdName = "removeCurrTx";
    
    if (_currTx != null)
    {
      try
      {
        delete(_currTx);
        commitTransaction();
      }
      catch (Exception ex)
      {
        _logger.logError(ILogErrorCodes.TRANSACTION_DELETE, mtdName, null, "Unexpected error: "+ex.getMessage(), ex);
        if (hasTransaction())
        {
          rollbackTransaction();
        }
      }
      finally
      {
        _currTx = null;
        closeTransactionContext();
      }
    }
    else
    {
      _logger.logWarn(mtdName, null, "No currTx to remove!", null);
    }
  }

  /**
   * Pend the current transaction doc that is being processed for later attempt. This is called
   * due to error encountered when tried to process the current transaction doc.
   * 
   * @return The total number of attempts made so far to process the current transaction. 
   */
  public int pendCurrTx()
  {
    String mtdName = "pendCurrTx";
    int count = -1;
    if (_currTx != null)
    {
      try
      {
        _logger.debugMessage(mtdName,null, "Old Attempt count="+_currTx.getAttemptCount());
        _currTx.incrementAttemptCount();
        commitTransaction();
        count = _currTx.getAttemptCount();
        _logger.debugMessage(mtdName,null, "New Attempt count="+_currTx.getAttemptCount());
      }
      catch (Exception ex)
      {
        _logger.logError(ILogErrorCodes.TRANSACTION_SAVE,  mtdName, null, "Unexpected error: "+ex.getMessage(), ex);
        if (hasTransaction())
        {
          rollbackTransaction();
        }
      }
      finally
      {
        _currTx = null;
        closeTransactionContext();
      }
    }
    else
    {
      _logger.logWarn(mtdName, null, "No currTx!", null);
    }
    return count;
  }
  
  /**
   * Get the next transaction available for the specified direction.
   * @param direction The direction of transaction to retrieve.
   * @param maxFailedAttempts The maximum number of attempt counts, not exceeded by the retrieved transaction.
   * @return The next transaction record available. <b>null</b> if no more transaction available.
   */
  public TxRec getNextTx(String direction, int maxFailedAttempts)
  {
    boolean compareCount = maxFailedAttempts > -1;
    String queryName = TxRec.class.getName()+ (compareCount ? ".getNextTxWithAttemptCount" :".getNextTx");
    String[] paramNames = compareCount? new String[]{"direction", "maxAttempts"} : new String[] {"direction"};
    Object[] paramVals = compareCount? new Object[]{direction, maxFailedAttempts} : new Object[] {direction};
    
    return (TxRec)queryOne(queryName, paramNames, paramVals);
  }
}
