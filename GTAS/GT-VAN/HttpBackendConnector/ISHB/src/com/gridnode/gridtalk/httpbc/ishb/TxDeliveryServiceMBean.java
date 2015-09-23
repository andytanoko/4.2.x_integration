/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeliveryTimerServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 5, 2007    i00107              Created
 * Nov 4, 2009    Tam Wei Xiang       #1105 - add method isRunningAsDefault()
 */

package com.gridnode.gridtalk.httpbc.ishb;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * @author i00107
 * This service MBean is used to trigger tx delivery to
 * Backend as well as GridTalk Gateway. 
 */
public interface TxDeliveryServiceMBean extends ServiceMBean
{
  /**
   * Start processing the incoming tx which came from the Gateway.
   * This will not perform anything if the previous call to this
   * method has not finished.
   * @param timeOfCall The time of call of this method.
   */
  void processInTx(Date timeOfCall);
  
  /**
   * Start processing the outgoing tx which came from the Backend.
   * This will not perform anything if the previous call to this
   * method has not finished.
   * @param timeOfCall The time of call of this method.
   */
  void processOutTx(Date timeOfCall);
  
  /**
   * Get the time when the processInTx() is last called and performing something.
   * @return When the last time the processInTx() has been called and performed something.
   */
  Date getLastInTxProcessTime();
  
  /**
   * Get the time when the processOutTx() is last called and performing something.
   * @return When the last time the processOutTx() has been called and performed something.
   */
  Date getLastOutTxProcessTime();
  
  /**
   * Get whether the processInTx() is still performing something.
   * @return Whether the processInTx() is currently performing something.
   */
  boolean isProcessingInTx();
  
  /**
   * Get whether the processOutTx() is still performing something.
   * @return Whether the processOutTx() is currently performing something.
   */
  boolean isProcessingOutTx();
  
  /**
   * Set the JndiName for the Delivery manager.
   * @param jndiName The JndiName for the Delivery manager to handle the processing.
   */
  void setDeliveryMgrJndiName(String jndiName);
  
  /**
   * Get the JndiName for the Delivery manager.
   * @return The JndiName for the Delivery manager for handling the processing.
   */
  String getDeliveryMgrJndiName();
  
  /**
   * Set the Jndi properties to use to lookup the Delivery manager.
   * @param props The Jndi properties to use to lookup the Delivery manager.
   */
  void setJndiProperties(Properties props);
  
  /**
   * Get the Jndi properties to use to lookup the Delivery manager.
   * @return The Jndi properties to use to lookup the Delivery manager.
   */
  Properties getJndiProperties();
  
  /**
   * Set the maximum number of tx to deliver for each call to processInTx() or processOutTx().
   * @param max The maximum number of tx to deliver per call to processInTx() or processOutTx(). Must be greater than 0
   * to turn on any delivery. 
   */
  void setMaxProcessCountPerCall(int max);

  /**
   * Get the maximum number of tx to deliver for each call to processInTx() or processOutTx().
   * @return The maximum number of tx to deliver per call to processInTx() or processOutTx(). Must be greater than 0
   * to turn on any delivery. 
   */
  int getMaxProcessCountPerCall();
  
  /**
   * Set the maximum number of failed attempts to deliver that can be tolerated for each tx. 
   * @param max The maximum number of failed attempts to deliver that can be tolerated per tx, after which, the
   * tx will not be attempted anymore for delivery. Specify -1 to attempt infinitely.
   */
  void setMaxFailedAttemptsPerTx(int max);
  
  /**
   * Get the maximum number of failed attempts to deliver that can be tolerated for each tx. 
   * @return The maximum number of failed attempts to deliver that can be tolerated per tx, after which, the
   * tx will not be attempted anymore for delivery. A value of -1 indicates each tx will be tried infinitely.
   */
  int getMaxFailedAttemptsPerTx();
  
  /**
   * Set the threshold to send alert when a tx has failed to be delivered for the specified number of times.
   * For every <code>threshold</code> number of failed attempts, an alert will be sent out.
   * @param threshold The threshold to send alert when a tx has failed to be delivered for the specified number of times.
   */
  void setFailedAttemptsAlertThreshold(int threshold);
  
  /**
   * Get the threshold to send alert when a tx has failed to be delivered for the specified number of times.
   * For every <code>threshold</code> number of failed attempts, an alert will be sent out.
   * @return The threshold to send alert when a tx has failed to be delivered for the specified number of times.
   */
  int getFailedAttemptsAlertThreshold();
  
  /**
   * Check whether we run the TxDeliveryService as default model. 
   * @return True if we run the delivery doc task independent from the Timer thread. false otherwise (fall back to the previous
   *         implementation eg. The timer thread is performing the delivery of the document. This is not recommended as it cause all the
   *         GT scheduled task to delay).
   */
  boolean isRunningAsDefault();
  
  /**
   * Set whether the TxDeliveryService running model.
   * @param runningAsDefault True if we run the delivery doc task independent from the Timer thread. false otherwise (fall back to the previous
   *         implementation eg. The timer thread is performing the delivery of the document. This is not recommended as it cause all the
   *         GT scheduled task to delay).
   */
  void setRunningAsDefault(boolean runningAsDefault);
  
}
