/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TxDeliveryService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 5, 2007    i00107              Created
 * Mar 05 2007    i00107              Control the processing here.
 * Nov 06,2009    Tam Wei Xiang       #1105 - If there is delay in delivery the document to customer
 *                                    backend, it will cause the JBOSS scheduler thread (Timer) to delay.
 *                                    It will impact the rest of the scheduled task. We should delegate 
 *                                    the processing to a different thread.
 */

package com.gridnode.gridtalk.httpbc.ishb;

import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandler;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandlerHome;
import com.gridnode.gridtalk.httpbc.ishb.model.TxDeliveryInfo;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jms.JmsSenderException;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This service MBean is used to trigger tx delivery to
 * Backend as well as GridTalk Gateway. 
 */
public class TxDeliveryService extends ServiceMBeanSupport implements
                                                          TxDeliveryServiceMBean
{
  private static Log _log = LogFactory.getLog(TxDeliveryService.class.getName());
  private static String JMS_MSG_PROPS = "direction";
  private static String HOST_ID = "hostid";
  
  private String _deliveryMgrJndiName;
  private Properties _jndiProps;
  private Date _lastInTxProcessTime;
  private Date _lastOutTxProcessTime;
  
  private boolean _processingInTx;
  private boolean _processingOutTx;
  
  private boolean _isDefault = true;
  private Properties _jmsProps = null;
  
  //TODO to persist the options in config_props so that no need to set again upon restart of MBean.
  private int _maxFailedAttemptsPerTx;
  private int _maxProcessCountPerCall;
  private int _failedAttemptsAlertThreshold;
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getDeliveryMgrJndiName()
   */
  public String getDeliveryMgrJndiName()
  {
    return _deliveryMgrJndiName;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getJndiProperties()
   */
  public Properties getJndiProperties()
  {
    return _jndiProps;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getLastInTxProcessTime()
   */
  public Date getLastInTxProcessTime()
  {
    return _lastInTxProcessTime;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getLastOutTxProcessTime()
   */
  public Date getLastOutTxProcessTime()
  {
    return _lastOutTxProcessTime;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#isProcessingInTx()
   */
  public boolean isProcessingInTx()
  {
    return _processingInTx;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#isProcessingOutTx()
   */
  public boolean isProcessingOutTx()
  {
    return _processingOutTx;
  }
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#processInTx(java.util.Date)
   */
  public void processInTx(Date timeOfCall)
  {
    if (isProcessingInTx())
    {
      _log.debug("In Tx processing already in progress. No need to process at this time: "+timeOfCall);
      return;
    }
    try
    {
      _lastInTxProcessTime = timeOfCall;
      _processingInTx = true;
      
      
      //NSL20070305 Control the number to process here.
      if(! isRunningAsDefault())
      {
        int count = 0;
        boolean stopHandling = count >= _maxProcessCountPerCall;
        while (!stopHandling)
        {
          boolean success = getTxHandler().deliverIncomingTransaction(getMaxFailedAttemptsPerTx(), getFailedAttemptsAlertThreshold());
          stopHandling = (!success || (++count >= _maxProcessCountPerCall));
        }
//      getTxHandler().deliverIncomingTransaction(getMaxProcessCountPerCall(), getMaxFailedAttemptsPerTx(), getFailedAttemptsAlertThreshold());
      }
      else
      {
        //#1105: TWX 20091104 delegate to md bean for the handling.
        TxDeliveryInfo deliverInfo = new TxDeliveryInfo();
        deliverInfo.setMaxProcessCountPerCall(_maxProcessCountPerCall);
        deliverInfo.setMaxFailedAttemptsPerTx(getMaxFailedAttemptsPerTx());
        deliverInfo.setFailedAttemptsAlertThreshold(getFailedAttemptsAlertThreshold());
        deliverInfo.setJndiProps(getJndiProperties());
        deliverInfo.setDeliveryManagerJndiName(getDeliveryMgrJndiName());
        
        _log.debug("processInTx: sending incoming tx notification");
        sendProcessIncomingTxNotification(deliverInfo);
      }
    }
    catch (Throwable t)
    {
      _log.error("Error in processInTx()", t);
    }
    finally
    {
      _processingInTx = false;
    }
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#processOutTx(java.util.Date)
   */
  public void processOutTx(Date timeOfCall)
  {
    if (isProcessingOutTx())
    {
      _log.debug("Out Tx processing already in progress. No need to process at this time: "+timeOfCall);
      return;
    }

    try
    {
      _lastOutTxProcessTime = timeOfCall;
      _processingOutTx = true;
      
      if(!isRunningAsDefault())
      {
        //NSL20070305 Control the number to process here.
        int count = 0;
        boolean stopHandling = count >= _maxProcessCountPerCall;
        while (!stopHandling)
        {
          boolean success = getTxHandler().deliverOutgoingTransaction(getMaxFailedAttemptsPerTx(), getFailedAttemptsAlertThreshold());
          stopHandling = (!success || (++count >= _maxProcessCountPerCall));
        }

        //getTxHandler().deliverOutgoingTransaction(getMaxProcessCountPerCall(), getMaxFailedAttemptsPerTx(), getFailedAttemptsAlertThreshold());
      }
      else
      {
//      #1105: TWX 20091104 delegate to md bean for the handling.
        TxDeliveryInfo deliverInfo = new TxDeliveryInfo();
        deliverInfo.setMaxProcessCountPerCall(_maxProcessCountPerCall);
        deliverInfo.setMaxFailedAttemptsPerTx(getMaxFailedAttemptsPerTx());
        deliverInfo.setFailedAttemptsAlertThreshold(getFailedAttemptsAlertThreshold());
        deliverInfo.setJndiProps(getJndiProperties());
        deliverInfo.setDeliveryManagerJndiName(getDeliveryMgrJndiName());
        
        _log.debug("processOutTx: sending outgoing tx notification");
        sendProcessOutgoingTxNotification(deliverInfo);
      }
    }
    catch (Throwable t)
    {
      _log.error("Error in processOutTx()", t);
    }
    finally
    {
      _processingOutTx = false;
    }
  }

  private void sendProcessIncomingTxNotification(TxDeliveryInfo deliverInfo) throws JmsSenderException
  {
    JmsSender sender = new JmsSender(LoggerManager.getOneTimeInstance());
    sender.setSendProperties(getJmsDeliveryProps());
    
    Hashtable<String,String> msgProps = new Hashtable<String,String>();
    msgProps.put(JMS_MSG_PROPS, "incoming");
    msgProps.put(HOST_ID, getHostId());
    
    sender.send(deliverInfo, msgProps);
  }
  
  private void sendProcessOutgoingTxNotification(TxDeliveryInfo deliverInfo) throws JmsSenderException
  {
    JmsSender sender = new JmsSender(LoggerManager.getOneTimeInstance());
    sender.setSendProperties(getJmsDeliveryProps());
    
    Hashtable<String,String> msgProps = new Hashtable<String,String>();
    msgProps.put(JMS_MSG_PROPS, "outgoing");
    msgProps.put(HOST_ID, getHostId());
    
    sender.send(deliverInfo, msgProps);
  }
  
  private String getHostId()
  {
    return SystemUtil.getHostId();
  }
  
  private Properties getJmsDeliveryProps()
  {
    if(_jmsProps == null)
    {
      ConfigurationStore store = ConfigurationStore.getInstance();
      _jmsProps = store.getProperties(IConfigCats.TX_DELIVERY_JMS);
    }
    return _jmsProps;
  }
  
  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#setDeliveryMgrJndiName(java.lang.String)
   */
  public void setDeliveryMgrJndiName(String jndiName)
  {
    _deliveryMgrJndiName = jndiName;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#setJndiProperties(java.util.Properties)
   */
  public void setJndiProperties(Properties props)
  {
    _jndiProps = props;
  }

  /**
   * @see org.jboss.system.ServiceMBean#getName()
   */
  public String getName()
  {
    return "TxDeliveryService";
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getFailedAttemptsAlertThreshold()
   */
  public int getFailedAttemptsAlertThreshold()
  {
    return _failedAttemptsAlertThreshold;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getMaxFailedAttemptsPerTx()
   */
  public int getMaxFailedAttemptsPerTx()
  {
    return _maxFailedAttemptsPerTx;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#getMaxProcessCountPerCall()
   */
  public int getMaxProcessCountPerCall()
  {
    return _maxProcessCountPerCall;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#setFailedAttemptsAlertThreshold(int)
   */
  public void setFailedAttemptsAlertThreshold(int threshold)
  {
    _failedAttemptsAlertThreshold = threshold;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#setMaxFailedAttemptsPerTx(int)
   */
  public void setMaxFailedAttemptsPerTx(int max)
  {
    _maxFailedAttemptsPerTx = max;
  }

  /**
   * @see com.gridnode.gridtalk.httpbc.ishb.TxDeliveryServiceMBean#setMaxProcessCountPerCall(int)
   */
  public void setMaxProcessCountPerCall(int max)
  {
    _maxProcessCountPerCall = max;
  }
  
  private ITransactionHandler getTxHandler() throws Exception
  {
    InitialContext ctx = new InitialContext(getJndiProperties());
    ITransactionHandlerHome home = (ITransactionHandlerHome)ctx.lookup(getDeliveryMgrJndiName());
    return home.create();
  }

  public boolean isRunningAsDefault()
  {
    return _isDefault;
  }

  public void setRunningAsDefault(boolean default1)
  {
    _isDefault = default1;
  }
  
  
}
