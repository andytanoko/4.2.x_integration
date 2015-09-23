/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractSyncResourceHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 2002    Neo Sok Lay         Created
 * Oct 02 2002    Neo Sok Lay         Add abstract method: getDelegate() for
 *                                    returning a Sync<Resource>Delegate class
 *                                    for sychronizing.
 * Oct 04 2002    Neo Sok Lay         Change testFileCount to static member.
 * Jan 05 2004    Neo Sok Lay         Add generation of TransactionId.
 *                                    The SyncResourceHandler will now track the
 *                                    synchronization from start till end base
 *                                    on the transaction id.
 */
package com.gridnode.gtas.server.enterprise.sync;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;

import java.io.Serializable;
import java.util.Random;

/**
 * This abstract class defines the basic implementation of a handler for
 * synchronizing <b>one</b> type of resource.
 * <p>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public abstract class AbstractSyncResourceHandler
  implements Serializable
{
  protected static final String SYNC_FILE_EXT = ".ser";

  private SyncResourceHandlerConfig _config;
  private boolean                   _inTransaction;
  private String                    _transId;
  private Object                    _transData;
  private static long               _testFileCount;
  private static final Random       _randGen = new Random(System.currentTimeMillis());

  /**
   * Set the configuration for this handler.
   *
   * @param config The configuration for this handler.
   *
   * @since 2.0 I4
   */
  protected final void setConfiguration(SyncResourceHandlerConfig config)
  {
    _config = config;
  }

  /**
   * Get the configuration of this handler.
   *
   * @return The configuration of this handler.
   *
   * @since 2.0
   */
  protected final SyncResourceHandlerConfig getConfiguration()
  {
    return _config;
  }

  /**
   * Checks whether this handler is currently in the midst of a synchronization
   * transaction. If the handler is not in transaction, it may be used to
   * handle any messages.
   *
   * @return <b>true</b> if the handler is in the midst of transaction, <b>false</b>
   * otherwise.
   *
   * @since 2.0 I4
   */
  final boolean isInTransaction()
  {
    return _inTransaction;
  }

  /**
   * Engage this handler in a synchronization transaction. This handler should
   * not be used for other transaction until it's state is cleared.
   *
   * @since 2.0 I4
   */
  final void startTransaction()
  {
    _inTransaction = true;
    _transId = generateTransID();
  }

  /**
   * Disengage this handler from a synchronization transaction.
   *
   * @since 2.0 I4
   */
  final void endTransaction()
  {
    clearState();
    //_testFileCount = 0;
    _inTransaction = false;
    _transId = null;
    _transData = null;
  }

  /**
   * Get the transaction id of the current transaction.
   * 
   * @return Transaction id of the current started transaction, or <b>null</b> if none has started.
   */
  final String getTransactionId()
  {
    return _transId;
  }
  
  /**
   * Set the transient transaction data for the current transaction for retrieval later on. This is useful
   * for long transaction. This data will be cleared when the transaction ends.
   * 
   * @param data The transaction data to set.
   */
  final void setTransactionData(Object data)
  {
    _transData = data;
  }
  
  /**
   * Get the transient transaction data set earlier for the same transaction.
   * 
   * @return The transaction data set earlier using setTransactionData() method.
   */
  final Object getTransactionData()
  {
    return _transData;
  }
  
  /**
   * Clear any states kept within the handler (excluding configuration).
   * Subclasses may keep states within the handler until this method is called.
   * This method is only called when a synchronization transaction has ended.
   * Subclasses should be refrained from calling this method on other
   * occasions.
   *
   * @since 2.0 I4
   */
  protected abstract void clearState();

  /**
   * Handle a received Synchronization content message.
   *
   * @param msg The content message received.
   *
   * @since 2.0 I4
   */
  protected abstract void handleReceive(SyncMessage msg)
    throws SynchronizationFailException;

  /**
   * Handle a received Acknowledgement receipt message.
   *
   * @param ackMsg The acknowledge message received.
   *
   * @since 2.0 I4
   */
  protected abstract void handleReceiveAck(SyncMessage ackMsg)
    throws SynchronizationFailException;

  /**
   * Handle a message for sending. Typical requirement for subclasses implementations
   * is to add the payloads (data and file, if applicable) to be sent.
   * This will only be called for outgoing content messages.
   *
   * @param message The outgoing content message.
   * @param sendResourceUID The UID of the resource that is undergoing
   * synchronization via the message.
   * @param toEnterpriseID ID of recipient enterprise.
   *
   * @since 2.0 I4
   */
  protected abstract void handleSend(
    SyncMessage message, Long sendResourceUID, String toEnterpriseID)
    throws SynchronizationFailException;

  /**
   * Handle an acknowledgement message in respond to a received content message.
   * Typical requirement for subclasses implementations is to: (a) add the payloads
   * (data and file, if applicable) to be sent; (b) set the channel to be used
   * for sending the acknowledgement message. This will only
   * be called for outgoing acknowledge messages.
   *
   * @param ackMsg The outgoing acknowledgement.
   * @param receivedMsg The received content message.
   *
   * @since 2.0 I4
   */
  protected abstract void handleSendAck(SyncMessage ackMsg, SyncMessage receivedMsg)
    throws SynchronizationFailException;

  /**
   * Get a Sync<Resource>Delegate implementation for synchronizing the
   * resource that this handler can handle.
   *
   * @return An implementation of AbstractSyncResourceDelegate class.
   *
   * @since 2.0 I6
   */
  protected abstract AbstractSyncResourceDelegate getDelegate();

  /**
   * Get an unique name for a serialized file.
   *
   * @param id An identifier for the type of serialized file. Typically refers
   * to the type of content.
   * @param recipient An identifier for the targeted recipient of the serialized
   * file.
   * @return An unique name generated based on the specified arguments.
   *
   * @since 2.0 I4
   */
  protected String getUniqueSerFilename(String id, String recipient)
  {
    /**@todo should need to append the temp directory */
    StringBuffer buff = new StringBuffer("/sync");
    buff.append(id).append("_R").append(recipient).append("_T");
    buff.append(getTime()).append(SYNC_FILE_EXT);

    buff.insert(0, _config.getSerializeDir());
    return buff.toString();
  }

  /**
   * Get the current system time.
   *
   * @return The current system time in milliseconds.
   *
   * @since 2.0 I4
   */
  protected synchronized long getTime()
  {
    return _config.isTest()? _testFileCount++ : System.currentTimeMillis();
  }

  /**
   * Generate an unique transaction Id for a new transaction that is starting
   * 
   * @return The generated transaction id.
   */
  private synchronized String generateTransID()
  {
    int transId = _randGen.nextInt();
    if (transId == Integer.MIN_VALUE)
      return "0";
    return String.valueOf(Math.abs(transId));
  }

}