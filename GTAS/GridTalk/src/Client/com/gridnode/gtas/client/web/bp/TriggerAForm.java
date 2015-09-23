/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2003-08-08     Andrew Hill         isLocalPending property added
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class TriggerAForm extends GTActionFormBase
{
  private String _triggerType;
  private String _triggerLevel;
  private String _partnerFunctionId;
  private String _docType;
  private String _partnerType;
  private String _partnerGroup;
  private String _partnerId;
  private String _channelUid;
  private String _isLocalPending; //20030808AH

  private String _processId;
  private String _isRequest;
  private String _numOfRetries;
  private String _retryInterval;

  public String getTriggerType()
  { return _triggerType; }

  public Integer getTriggerTypeInteger()
  {
    return StaticUtils.integerValue(_triggerType);
  }

  public void setTriggerType(String triggerType)
  { _triggerType=triggerType; }

  public String getTriggerLevel()
  { return _triggerLevel; }

  public Integer getTriggerLevelInteger()
  {
    return StaticUtils.integerValue(_triggerLevel);
  }

  public void setTriggerLevel(String triggerLevel)
  { _triggerLevel=triggerLevel; }

  public String getPartnerFunctionId()
  { return _partnerFunctionId; }

  public void setPartnerFunctionId(String partnerFunctionId)
  { _partnerFunctionId=partnerFunctionId; }

  public String getDocType()
  { return _docType; }

  public void setDocType(String docType)
  { _docType=docType; }

  public String getPartnerType()
  { return _partnerType; }

  public void setPartnerType(String partnerType)
  { _partnerType=partnerType; }

  public String getPartnerGroup()
  { return _partnerGroup; }

  public void setPartnerGroup(String partnerGroup)
  { _partnerGroup=partnerGroup; }

  public String getPartnerId()
  { return _partnerId; }

  public void setPartnerId(String partnerId)
  { _partnerId=partnerId; }

  public String getChannelUid()
  { // 20031120 DDJ
    return _channelUid;
  }

  public Long getChannelUidAsLong()
  { // 20031120 DDJ
    return StaticUtils.longValue(_channelUid);
  }

  public void setChannelUid(String string)
  { // 20031120 DDJ
    _channelUid = string;
  }

  public String getIsLocalPending()
  { //20030808AH
    return _isLocalPending;
  }

  public void setIsLocalPending(String string)
  { //20030808AH
    _isLocalPending = string;
  }



  public String getProcessId()
  { return _processId; }

  public void setProcessId(String processId)
  { _processId=processId; }

  public String getIsRequest()
  { return _isRequest; }

  public void setIsRequest(String isRequest)
  { _isRequest=isRequest; }

  public String getNumOfRetries()
  { // 20031120 DDJ
    return _numOfRetries;
  }

  public Integer getNumOfRetriesAsInteger()
  { // 20031120 DDJ
    return StaticUtils.integerValue(_numOfRetries);
  }

  public void setNumOfRetries(String string)
  { // 20031120 DDJ
    _numOfRetries = string;
  }

  public String getRetryInterval()
  { // 20031120 DDJ
    return _retryInterval;
  }

  public Integer getRetryIntervalAsInteger()
  { // 20031120 DDJ
    return StaticUtils.integerValue(_retryInterval);
  }

  public void setRetryInterval(String string)
  { // 20031120 DDJ
    _retryInterval = string;
  }



  public void doReset(ActionMapping p0, HttpServletRequest p1)
  {
    // Fields for checkboxes must be reset due to dumb way html checkboxes are(nt) submitted
    _isRequest = "false";
  }
}