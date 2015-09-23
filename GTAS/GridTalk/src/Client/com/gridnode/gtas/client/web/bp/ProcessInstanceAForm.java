/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-08-20     Andrew Hill         Added userTrackingId field
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ProcessInstanceAForm extends GTActionFormBase
{
  private String _processInstanceId;
  private String _partner;
  private String _state;
  private String _startTime;
  private String _endTime;
  private String _retryNum;
  private String _isFailed;
  private String _failReason;
  private String _detailReason;
  private String _processDefName;
  private String _roleType;
  private String _userTrackingId; //20030820AH

  // for ASSOC_DOCS embeded listview
  private String _assocDocsListOrder;
  private String[] _assocDocsListOrderExploded;

  public String getProcessInstanceId()
  { return _processInstanceId; }

  public void setProcessInstanceId(String processInstanceId)
  { _processInstanceId=processInstanceId; }

  public String getPartner()
  { return _partner; }

  public void setPartner(String partner)
  { _partner=partner; }

  public String getState()
  { return _state; }

  public void setState(String state)
  { _state=state; }

  public String getStartTime()
  { return _startTime; }

  public void setStartTime(String startTime)
  { _startTime=startTime; }

  public String getEndTime()
  { return _endTime; }

  public void setEndTime(String endTime)
  { _endTime=endTime; }

  public String getRetryNum()
  { return _retryNum; }

  public Integer getRetryNumInteger()
  { return StaticUtils.integerValue(_retryNum); }

  public void setRetryNum(String retryNum)
  { _retryNum=retryNum; }

  public String getIsFailed()
  { return _isFailed; }

  public Boolean getIsFailedBoolean()
  { return StaticUtils.booleanValue(_isFailed); }

  public boolean getIsFailedPrimitiveBoolean()
  { return StaticUtils.primitiveBooleanValue(_isFailed); }

  public void setIsFailed(String isFailed)
  { _isFailed=isFailed; }

  public String getFailReason()
  { return _failReason; }

  public Integer getFailReasonInteger()
  { return StaticUtils.integerValue(_failReason); }

  public void setFailReason(String failReason)
  { _failReason=failReason; }

  public String getDetailReason()
  { return _detailReason; }

  public void setDetailReason(String detailReason)
  { _detailReason=detailReason; }

  public String getProcessDefName()
  { return _processDefName; }

  public void setProcessDefName(String processDefName)
  { _processDefName=processDefName; }

  public String getRoleType()
  { return _roleType; }

  public void setRoleType(String roleType)
  { _roleType=roleType; }

  // for ASSOC_DOCS embeded listview
  public void setAssocDocsListOrder(String values)
  {
    _assocDocsListOrder = values;
    _assocDocsListOrderExploded = StaticUtils.explode(values,",");
  }

  public void setAssocDocsListOrderExploded(String[] values)
  {
    _assocDocsListOrderExploded = values;
    _assocDocsListOrder = StaticUtils.implode(values,",");
  }

  public void initAssocDocsListOrder(int size)
  {
    _assocDocsListOrderExploded = new String[size];
    for(int i=0; i < size; i++)
    {
      _assocDocsListOrderExploded[i] = "" + i;
    }
    _assocDocsListOrder = StaticUtils.implode(_assocDocsListOrderExploded,",");
  }

  public String[] getAssocDocsListOrderExploded()
  {
    return _assocDocsListOrderExploded;
  }

  public String getAssocDocsListOrder()
  { return _assocDocsListOrder; }

  public void doReset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
  {
    _isFailed = null;
  }
  
  public String getUserTrackingId()
  {
    return _userTrackingId;
  }

  public void setUserTrackingId(String string)
  {
    _userTrackingId = string;
  }

}