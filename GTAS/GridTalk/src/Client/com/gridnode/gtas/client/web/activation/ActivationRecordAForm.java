/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-15     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.activation;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

import com.gridnode.gtas.client.utils.StaticUtils;

public class ActivationRecordAForm extends GTActionFormBase
{
  private String _actDirection;
  private String _deactDirection;
  private String _gridNodeId;
  private String _gridNodeName;
  private String _dtRequested;
  private String _dtApproved;
  private String _dtAborted;
  private String _dtDenied;
  private String _dtDeactivated;
  private String _isLatest;
  private String _activateReason;
  private String[] _requestorBeList;
  private String[] _approverBeList;
  private String _currentType;
  private Short _doAction;

  public Short getDoAction()
  { return _doAction; }

  public void setDoAction(Short doAction)
  { _doAction = doAction; }

  public String getActDirection()
  { return _actDirection; }

  public Short getActDirectionShort()
  { return StaticUtils.shortValue(_actDirection); }

  public void setActDirection(String actDirection)
  { _actDirection=actDirection; }

  public String getDeactDirection()
  { return _deactDirection; }

  public Short getDeactDirectionShort()
  { return StaticUtils.shortValue(_deactDirection); }

  public void setDeactDirection(String deactDirection)
  { _deactDirection=deactDirection; }

  public String getGridNodeId()
  { return _gridNodeId; }

  public void setGridNodeId(String gridNodeId)
  { _gridNodeId=gridNodeId; }

  public String getGridNodeName()
  { return _gridNodeName; }

  public void setGridNodeName(String gridNodeName)
  { _gridNodeName=gridNodeName; }

  public String getDtRequested()
  { return _dtRequested; }

  public void setDtRequested(String dtRequested)
  { _dtRequested=dtRequested; }

  public String getDtApproved()
  { return _dtApproved; }

  public void setDtApproved(String dtApproved)
  { _dtApproved=dtApproved; }

  public String getDtAborted()
  { return _dtAborted; }

  public void setDtAborted(String dtAborted)
  { _dtAborted=dtAborted; }

  public String getDtDenied()
  { return _dtDenied; }

  public void setDtDenied(String dtDenied)
  { _dtDenied=dtDenied; }

  public String getDtDeactivated()
  { return _dtDeactivated; }

  public void setDtDeactivated(String dtDeactivated)
  { _dtDeactivated=dtDeactivated; }

  public String getIsLatest()
  { return _isLatest; }

  public boolean isLatest()
  { return StaticUtils.primitiveBooleanValue(_isLatest); }

  public void setIsLatest(String isLatest)
  { _isLatest=isLatest; }

  public String getActivateReason()
  { return _activateReason; }

  public void setActivateReason(String activateReason)
  { _activateReason=activateReason; }

  public String[] getRequestorBeList()
  { return _requestorBeList; }

  public void setRequestorBeList(String[] requestorBeList)
  { _requestorBeList=requestorBeList; }

  public String[] getApproverBeList()
  { return _approverBeList; }

  public void setApproverBeList(String[] approverBeList)
  { _approverBeList=approverBeList; }

  public String getCurrentType()
  { return _currentType; }

  public Short getCurrentTypeShort()
  { return StaticUtils.shortValue(_currentType); }

  public void setCurrentType(String currentType)
  { _currentType=currentType; }
}