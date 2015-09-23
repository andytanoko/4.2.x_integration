/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessSummary.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.common.model;

import java.io.Serializable;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessSummary implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 6380230322368368002L;
  private String _pipName;
  private String _pipVersion;
  private String _tradingPartnerDuns;
  private String _tradingPartnerName;
  private String _customerDuns;
  private String _customerName;
  private Long _processInstanceUID;
  private String _processInitiatorID;
  
  public ProcessSummary() {}
  
  public ProcessSummary(String pipName, String pipVersion, String tradingPartnerDuns,
                        String tradingPartnerName, String customerDuns, String customerName,
                        Long processInstanceUID, String processInitiator)
  {
    setPipName(pipName);
    setPipVersion(pipVersion);
    setTradingPartnerDuns(tradingPartnerDuns);
    setTradingPartnerName(tradingPartnerName);
    setCustomerDuns(customerDuns);
    setCustomerName(customerName);
    setProcessInstanceUID(processInstanceUID);
    setProcessInitiatorID(processInitiator);
  }

  public String getCustomerDuns()
  {
    return _customerDuns;
  }

  public void setCustomerDuns(String duns)
  {
    _customerDuns = duns;
  }

  public String getCustomerName()
  {
    return _customerName;
  }

  public void setCustomerName(String name)
  {
    _customerName = name;
  }

  public String getPipName()
  {
    return _pipName;
  }

  public void setPipName(String name)
  {
    _pipName = name;
  }

  public String getPipVersion()
  {
    return _pipVersion;
  }

  public void setPipVersion(String version)
  {
    _pipVersion = version;
  }

  public Long getProcessInstanceUID()
  {
    return _processInstanceUID;
  }

  public void setProcessInstanceUID(Long instanceUID)
  {
    _processInstanceUID = instanceUID;
  }

  public String getTradingPartnerDuns()
  {
    return _tradingPartnerDuns;
  }

  public void setTradingPartnerDuns(String partnerDuns)
  {
    _tradingPartnerDuns = partnerDuns;
  }

  public String getTradingPartnerName()
  {
    return _tradingPartnerName;
  }

  public void setTradingPartnerName(String partnerName)
  {
    _tradingPartnerName = partnerName;
  }
  
  public String getProcessInitiatorID()
  {
    return _processInitiatorID;
  }

  public void setProcessInitiatorID(String initiatorID)
  {
    _processInitiatorID = initiatorID;
  }

  public String toString()
  {
    return "ProcessSummary[ pipName:"+getPipName()+" tradingPartnerDuns:"+getTradingPartnerDuns()+
           " tradingPartnerName:"+getTradingPartnerName()+" customerDuns:"+getCustomerDuns()+
           " customerName:"+getCustomerName()+" processInstanceUID:"+getProcessInstanceUID()+" processInitiatorID:"+getProcessInitiatorID()+"]";
  }
}
