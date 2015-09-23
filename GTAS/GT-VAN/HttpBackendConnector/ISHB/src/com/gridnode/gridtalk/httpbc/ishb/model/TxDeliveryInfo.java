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
 * File: TxDeliveryInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 3, 2009    Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.ishb.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Tam Wei Xiang
 * @version GT4.1.4.2
 * @since GT4.1.4.2
 */
public class TxDeliveryInfo implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -692124423638871723L;
  private int _maxProcessCountPerCall;
  private int _maxFailedAttemptsPerTx;
  private int _failedAttemptsAlertThreshold;
  private String _deliveryManagerJndiName;
  private Properties _jndiProps;
  
  public TxDeliveryInfo()
  {
    
  }

  public int getFailedAttemptsAlertThreshold()
  {
    return _failedAttemptsAlertThreshold;
  }

  public void setFailedAttemptsAlertThreshold(int attemptsAlertThreshold)
  {
    _failedAttemptsAlertThreshold = attemptsAlertThreshold;
  }

  public int getMaxFailedAttemptsPerTx()
  {
    return _maxFailedAttemptsPerTx;
  }

  public void setMaxFailedAttemptsPerTx(int failedAttemptsPerTx)
  {
    _maxFailedAttemptsPerTx = failedAttemptsPerTx;
  }

  public int getMaxProcessCountPerCall()
  {
    return _maxProcessCountPerCall;
  }

  public void setMaxProcessCountPerCall(int processCountPerCall)
  {
    _maxProcessCountPerCall = processCountPerCall;
  }

  public String getDeliveryManagerJndiName()
  {
    return _deliveryManagerJndiName;
  }

  public void setDeliveryManagerJndiName(String managerJndiName)
  {
    _deliveryManagerJndiName = managerJndiName;
  }

  public Properties getJndiProps()
  {
    return _jndiProps;
  }

  public void setJndiProps(Properties props)
  {
    _jndiProps = props;
  }
  
  
}
