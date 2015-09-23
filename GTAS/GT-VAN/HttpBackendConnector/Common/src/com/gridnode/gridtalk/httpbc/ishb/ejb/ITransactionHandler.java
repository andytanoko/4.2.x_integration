/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransactionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00107              Created
 * Feb 06 2007    i00107              Add deliverIncomingTransaction() and
 *                                    deliverOutgoingTransaction().
 */

package com.gridnode.gridtalk.httpbc.ishb.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;

/**
 * @author i00107
 * This remote interface defines the business methods for TransactionHandlerBean.
 */
public interface ITransactionHandler extends EJBObject
{
  /**
   * Handles the transaction received from trading partner.
   * 
   * @param tDoc The transaction document info.
   * @return The Transaction number of saved transaction, if successful.
   * @throws RemoteException
   * @throws RuntimeException If unable to save transaction to database.
   */
  public String handleIncomingTransaction(TransactionDoc tDoc) throws RemoteException;
  
  /**
   * Handles the transaction received from customer.
   * 
   * @param tDoc The transaction document info.
   * @return The transaction number of the saved transaction, if successful.
   * @throws RemoteException
   * @throws RuntimeException If unable to save transaction to database.
   */
  public String handleOutgoingTransaction(TransactionDoc tDoc) throws RemoteException;
  
  /**
   * Deliver the incoming transactions.
   * @param maxProcessCount The maximum number of transactions to deliver
   * @param maxFailedAttempts The maximum number of failed attempts to tolerate for a transaction.
   * @param alertThreshold The threshold for alerting failed attempts.
   * @throws RemoteException
   */
  public void deliverIncomingTransaction(int maxProcessCount, int maxFailedAttempts, int alertThreshold) throws RemoteException;
  
  /**
   * Deliver the outgoing transactions.
   * @param maxProcessCount The maximum number of transactions to deliver
   * @param maxFailedAttempts The maximum number of failed attempts to tolerate for a transaction.
   * @param alertThreshold The threshold for alerting failed attempts.
   * @throws RemoteException
   */
  public void deliverOutgoingTransaction(int maxProcessCount, int maxFailedAttempts, int alertThreshold) throws RemoteException;
 
  /**
   * Deliver an incoming transaction.
   * @param maxFailedAttempts The maximum number of failed attempts to tolerate for a transaction.
   * @param alertThreshold The threshold for alerting failed attempts.
   * @throws RemoteException
   */
  public boolean deliverIncomingTransaction(int maxFailedAttempts, int alertThreshold) throws RemoteException;
  
  /**
   * Deliver an outgoing transaction.
   * @param maxFailedAttempts The maximum number of failed attempts to tolerate for a transaction.
   * @param alertThreshold The threshold for alerting failed attempts.
   * @throws RemoteException
   */
  public boolean deliverOutgoingTransaction(int maxFailedAttempts, int alertThreshold) throws RemoteException;
}
