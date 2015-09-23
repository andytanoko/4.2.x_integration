/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncTransactionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 21 2002    Neo Sok Lay         Created
 * Jan 05 2004    Neo Sok Lay         Track the transaction base on transactionId
 *                                    Change the inTransHandlers datatype to Hashtable.
 */
package com.gridnode.gtas.server.enterprise.sync;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.pdip.framework.util.ReflectionUtility;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A SyncTransactionHandler is specialised in returning an instance of one
 * type of SyncResourceHandler, for handling messages, when
 * a new transaction is to started. The SyncResourceHandler should not be used
 * for other transactions before the transaction ends.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SyncTransactionHandler
{
  private static int MAX_IDLE_HANDLERS  = 5;

  private SyncResourceHandlerConfig _syncHandlerConfig;
  //private List     _inTransHandlers    = new ArrayList();
  private static final Hashtable _inTransHandlers    = new Hashtable();
  private List     _idleHandlers       = new ArrayList();

  public SyncTransactionHandler()
  {
  }

  /**
   * Set the configuration properties for the SyncResourceHandler to be
   * produced by this SyncTransactionHandler.
   *
   * @param handlerClass The fully qualified class name of the SyncResourceHandler
   * class.
   * @param resType The type of resource that the SyncResourceHandler can
   * handle.
   * @param sendOrRouteMsgID The MsgID options for outgoing content messages which
   *                         may be routed if the targeted receipient is not
   *                         contactable at the moment of sending.
   * @param sendOnlyMsgID    The MsgID options for outgoing content messages which
   *                         may not be routed if the targeted receipient is not
   *                         contactable at the moment of sending.
   * @param routeMsgID       The MsgID options for outgoing content messages,
   *                         the receiving end being a router.
   * @param receiveOnlyMsgID The MsgID options for incoming content messages.
   *
   * @since 2.0 I4
   */
  public void setSyncResourceHandler(
    String handlerClass, String resType, List sendOrRouteMsgID,
    List sendOnlyMsgID, List routeMsgID, List receiveOnlyMsgID)
  {
    _syncHandlerConfig = new SyncResourceHandlerConfig(
                           handlerClass, resType, sendOrRouteMsgID,
                           sendOnlyMsgID, routeMsgID, receiveOnlyMsgID);
  }

  /**
   * Start a new synchronization transaction.
   *
   * @return A SyncResourceHandler to handle the synchronization.
   *
   * @since 2.0 I4
   */
  public AbstractSyncResourceHandler startTransaction()
    throws SynchronizationFailException
  {
    AbstractSyncResourceHandler syncHandler = null;

    try
    {
      if (_idleHandlers.size() > 0)
      {
        syncHandler = (AbstractSyncResourceHandler)_idleHandlers.get(0);
      }
      else
      {
        syncHandler = (AbstractSyncResourceHandler)ReflectionUtility.newInstance(
                        _syncHandlerConfig.getHandlerClass(),
                        new Class[] {},
                        new Object[] {});
        syncHandler.setConfiguration(_syncHandlerConfig);
      }

      //_inTransHandlers.add(syncHandler);
    }
    catch (Exception ex)
    {
      String errMsg = "Invalid synchronization handler <"+_syncHandlerConfig.getHandlerClass() +
        "> set for resource type: "+ _syncHandlerConfig.getResourceType();

      Logger.warn("[SyncTransactionHandler.startTransaction] "+errMsg, ex);

      throw new SynchronizationFailException(errMsg);
    }

    syncHandler.startTransaction();
    _inTransHandlers.put(syncHandler.getTransactionId(), syncHandler);
    return syncHandler;
  }

  /**
   * Ends a synchronization transaction.
   *
   * @param syncHandler The SyncResourceHandler that is handling the
   * synchronization.
   *
   * @since 2.0 I4
   */
  public void endTransaction(AbstractSyncResourceHandler syncHandler)
  {
    //if (_inTransHandlers.remove(syncHandler))
    Object exists = _inTransHandlers.remove(syncHandler.getTransactionId());
    if (exists != null)
    {
      syncHandler.endTransaction();
      if (_idleHandlers.size() < MAX_IDLE_HANDLERS)
        _idleHandlers.add(syncHandler);
    }
  }

  /**
   * Get the Sync handler for a transaction that is already in progress.
   * 
   * @param transId The transaction id
   * @return The Sync handler for the specified transaction, or <b>null</b> if no such transaction in progress.
   */
  public static final AbstractSyncResourceHandler getSyncHandler(String transId)
  {
    Logger.debug("[SyncTransactionHandler.getSyncHandler] transId = "+transId);  
    Logger.debug("[SyncTransactionHandler.getSyncHandler] currInTransIds = "+_inTransHandlers.keySet());  
    return (AbstractSyncResourceHandler)_inTransHandlers.get(transId);
  }
  
  void setTest(boolean isTest)
  {
    _syncHandlerConfig.setTest(isTest);
  }

  void setSerializeDir(String serDir)
  {
    _syncHandlerConfig.setSerializeDir(serDir);
  }
}