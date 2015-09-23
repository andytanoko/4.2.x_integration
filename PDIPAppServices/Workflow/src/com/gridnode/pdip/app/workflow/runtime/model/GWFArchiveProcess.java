/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveProcess.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.workflow.runtime.model;

import java.util.Date;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class GWFArchiveProcess 
    extends AbstractEntity 
      implements IGWFArchiveProcess
{
  private String _engineType;
  private String _processType;
  private Date _processStartTime;
  private Date _processEndTime;
  private Long _processUID;
  private Long _rtprocessDocUID;
  private String _customerBEId;
  private String _partnerKey;
  private Integer _processStatus;
  
  @Override
  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return ENTITY_NAME + " with rtprocessUID "+getKey()+ " and rtprocessDocUID "+getRtprocessDocUID();
  }

  public Number getKeyId()
  {
    return UID;
  }

  public String getCustomerBEId()
  {
    return _customerBEId;
  }

  public void setCustomerBEId(String id)
  {
    _customerBEId = id;
  }

  public String getEngineType()
  {
    return _engineType;
  }

  public void setEngineType(String type)
  {
    _engineType = type;
  }

  public String getPartnerKey()
  {
    return _partnerKey;
  }

  public void setPartnerKey(String key)
  {
    _partnerKey = key;
  }

  public Date getProcessEndTime()
  {
    return _processEndTime;
  }

  public void setProcessEndTime(Date endTime)
  {
    _processEndTime = endTime;
  }

  public Date getProcessStartTime()
  {
    return _processStartTime;
  }

  public void setProcessStartTime(Date startTime)
  {
    _processStartTime = startTime;
  }

  public String getProcessType()
  {
    return _processType;
  }

  public void setProcessType(String type)
  {
    _processType = type;
  }

  public Long getProcessUID()
  {
    return _processUID;
  }

  public void setProcessUID(Long _processuid)
  {
    _processUID = _processuid;
  }

  public Long getRtprocessDocUID()
  {
    return _rtprocessDocUID;
  }

  public void setRtprocessDocUID(Long docUID)
  {
    _rtprocessDocUID = docUID;
  }

  public Integer getProcessStatus()
  {
    return _processStatus;
  }

  public void setProcessStatus(Integer status)
  {
    _processStatus = status;
  }
}
