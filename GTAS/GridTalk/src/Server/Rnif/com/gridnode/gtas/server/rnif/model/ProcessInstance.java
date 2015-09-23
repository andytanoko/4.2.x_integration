/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstance.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-17     Daniel D'Cotta      Created
 * Jul 15 2003    Neo Sok Lay         Enable the fields.
 * Aug 21 2003    Guo Jianyu          added _userTrackingID
 */
package com.gridnode.gtas.server.rnif.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;

public class ProcessInstance extends AbstractEntity implements IProcessInstance
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7538988210968227923L;
	// 20030117 DDJ: not used
  protected String _processInstanceId;
  protected String _partner;
  protected String _state;
  protected String _startTime;
  protected String _endTime;
  protected String _retryNum;
  protected boolean _isFailed;
  protected String _failReason;
  protected String _detailReason;
  protected String _processDefName;
  protected String _roleType;
  protected Collection _assocDocs = new ArrayList();
  protected String _userTrackingID;

  // ******************* Methods from AbstractEntity ******************
  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    String state = getIsFailed() ? "Failed" : "Active";
    StringBuffer buff = new StringBuffer();
    buff.append(_processDefName).append('/');
    buff.append(_processInstanceId).append('/');
    buff.append(_partner).append('/');
    buff.append(state);
    return buff.toString();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************
  // 20030117 DDJ: not used
  public String getProcessInstanceId()
  {
    return _processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId)
  {
    _processInstanceId = processInstanceId;
  }

  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String value)
  {
    _userTrackingID = value;
  }

  public String getPartner()
  {
    return _partner;
  }

  public void setPartner(String partner)
  {
    _partner = partner;
  }

  public String getState()
  {
    return _state;
  }

  public void setState(String state)
  {
    _state = state;
  }

  public String getStartTime()
  {
    return _startTime;
  }

  public void setStartTime(String startTime)
  {
    _startTime = startTime;
  }

  public String getEndTime()
  {
    return _endTime;
  }

  public void setEndTime(String endTime)
  {
    _endTime = endTime;
  }

  public String getRetryNum()
  {
    return _retryNum;
  }

  public void setRetryNum(String retryNum)
  {
    _retryNum = retryNum;
  }

  public boolean getIsFailed()
  {
    return _isFailed;
  }

  public void setIsFailed(boolean isFailed)
  {
    _isFailed = isFailed;
  }

  public String getFailReason()
  {
    return _failReason;
  }

  public void setFailReason(String failReason)
  {
    _failReason = failReason;
  }

  public String getDetailReason()
  {
    return _detailReason;
  }

  public void setDetailReason(String detailReason)
  {
    _detailReason = detailReason;
  }

  public String getProcessDefName()
  {
    return _processDefName;
  }

  public void setProcessDefName(String processDefName)
  {
    _processDefName = processDefName;
  }

  public String getRoleType()
  {
    return _roleType;
  }

  public void setRoleType(String roleType)
  {
    _roleType = roleType;
  }

  public Collection getAssocDocs()
  {
    return _assocDocs;
  }

  public void setAssocDocs(Collection assocDocs)
  {
    _assocDocs = assocDocs;
  }
}
