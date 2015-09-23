/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: TransactionDeliveryConfigStore.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 3, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.ishb.workers;

import java.util.Properties;

import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.ishb.model.TxDeliveryInfo;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.config.ConfigurationStore;

/**
 * @author Tam Wei Xiang
 * @version GT4.1.4.2
 * @since GT4.1.4.2
 */
public class TransactionDeliveryConfigStore
{  
  private static Integer DEF_MAX_PROCESS_COUNT_PER_CALL;
  private static Integer DEF_MAX_FAILED_ATTEMPTS_PER_TX;
  private static Integer DEF_FAILED_ATTEMPT_ALERT_THRESHOLD;
  
  private int _maxProcessCountPerCall;
  private int _maxFailedAttemptsPerTx;
  private int _failedAttemptsAlertThreshold;
  
  private static final TransactionDeliveryConfigStore _txConfigStore = new TransactionDeliveryConfigStore();
  private Properties txDeliveryProps = null;
  
  private TransactionDeliveryConfigStore()
  {
    ConfigurationStore store = ConfigurationStore.getInstance();
    txDeliveryProps = store.getProperties(IConfigCats.TX_DELIVERY_INFO);
  }
  
  public static synchronized TransactionDeliveryConfigStore getInstance()
  {
    return _txConfigStore;
  }
  
  public String getTxDeliveryMgrJndi()
  {
    return txDeliveryProps.getProperty(IConfigCats.TX_DELIVERY_MGR_JNDI, "ISHB_TransactionHandlerBean");
  }
  
  public Properties getJndiProps()
  {
    Properties jndiProps = new Properties();
    jndiProps.putAll(txDeliveryProps);
    return jndiProps;
  }
  
  public synchronized int getMaxProcessCountPerCall(int defMaxProcessCountPerCall)
  {
    //init the def value when GT is just started up (def value specify in TXDeliveryService MBean)
    if(DEF_MAX_PROCESS_COUNT_PER_CALL == null)
    {
      DEF_MAX_PROCESS_COUNT_PER_CALL = defMaxProcessCountPerCall;
      _maxProcessCountPerCall = Integer.parseInt(txDeliveryProps.getProperty(IConfigCats.TX_MAX_PROCESS_COUNT_PER_CALL, ""+defMaxProcessCountPerCall));
      System.out.println("load from DB _maxProcessCountPerCall:"+_maxProcessCountPerCall);
    }
    
    //User has make modification in the Schedule service cache (define in the TXDeliveryService MBean)
    //FYI: allow user to reconfigure it without restart the GT; however to permanently store such configuration,
    //     such config is required to be persisted and the GT is required to be restarted.
    if(DEF_MAX_PROCESS_COUNT_PER_CALL!= defMaxProcessCountPerCall)
    {
      _maxProcessCountPerCall = defMaxProcessCountPerCall;
      DEF_MAX_PROCESS_COUNT_PER_CALL = defMaxProcessCountPerCall;
      System.out.println("user make changes _maxProcessCountPerCall: "+_maxProcessCountPerCall);
    }
    return _maxProcessCountPerCall;
  }
  
  public synchronized int getMaxFailedAttemptsPerTx(int defMaxFailedAttemptsPerTx)
  {
//  init the def value when GT is just started up (def value specify in TXDeliveryService MBean)
    if(DEF_MAX_FAILED_ATTEMPTS_PER_TX == null)
    {
      DEF_MAX_FAILED_ATTEMPTS_PER_TX = defMaxFailedAttemptsPerTx;
      _maxFailedAttemptsPerTx =Integer.parseInt(txDeliveryProps.getProperty(IConfigCats.TX_MAX_FAILED_ATTEMPTS_PER_TX, ""+defMaxFailedAttemptsPerTx));
      System.out.println("load from DB _maxFailedAttemptsPerTx: "+_maxFailedAttemptsPerTx);
    }

    if(DEF_MAX_FAILED_ATTEMPTS_PER_TX!= defMaxFailedAttemptsPerTx)
    {
      _maxFailedAttemptsPerTx = defMaxFailedAttemptsPerTx;
      DEF_MAX_FAILED_ATTEMPTS_PER_TX = defMaxFailedAttemptsPerTx;
      System.out.println("user make changes _maxFailedAttemptsPerTx: "+_maxFailedAttemptsPerTx);
    }
    return _maxFailedAttemptsPerTx;
  }
  
  public synchronized int getFailedAttemptsAlertThreshold(int defFailedAttemptsAlertThreshold)
  {
//  init the def value when GT is just started up (def value specify in TXDeliveryService MBean)
    if(DEF_FAILED_ATTEMPT_ALERT_THRESHOLD == null)
    {
      DEF_FAILED_ATTEMPT_ALERT_THRESHOLD = defFailedAttemptsAlertThreshold;
      _failedAttemptsAlertThreshold = Integer.parseInt(txDeliveryProps.getProperty(IConfigCats.TX_FAILED_ATTEMPT_ALERT_THRESHOLD, ""+defFailedAttemptsAlertThreshold));
      System.out.println("load from DB _maxFailedAttemptsPerTx: "+_failedAttemptsAlertThreshold);
    }

    if(DEF_FAILED_ATTEMPT_ALERT_THRESHOLD!= defFailedAttemptsAlertThreshold)
    {
      _failedAttemptsAlertThreshold = defFailedAttemptsAlertThreshold;
      DEF_FAILED_ATTEMPT_ALERT_THRESHOLD = defFailedAttemptsAlertThreshold;
      System.out.println("user make changes _failedAttemptsAlertThreshold: "+_failedAttemptsAlertThreshold);
    }
    return _failedAttemptsAlertThreshold;
  }
  
  public synchronized String getIncomingDeliveryLockFilePath()
  {
    //TODO: load from DB
    String workDir = SystemUtil.getWorkingDirPath()+"/";
    
    return workDir + "gtvan/httpbc/incomingTx.lock";
    
    //return "/export/home/gridnode/GridTalk/incomingTx.lock";
  }
  
  public synchronized String getOutgoingDeliveryLockFilePath()
  {
    //  TODO: load from DB
    String workDir = SystemUtil.getWorkingDirPath()+"/";
    
    return workDir + "gtvan/httpbc/outgoingTx.lock";
  }
}
