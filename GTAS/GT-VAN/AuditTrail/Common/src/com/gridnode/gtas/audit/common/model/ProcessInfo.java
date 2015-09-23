/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.common.model;

import java.util.Date;

/**
 * This class consists of the information about a particular ProcessInstance.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessInfo implements ITrailInfo
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -3357184536624603841L;

  /**
   * It is combination of process instance ID + process originator ID 
   */
  private String _processID;
  
  /**
   * It is the Process Start Time of a process. 
   */
  private Date _processStartTime;
  
  /**
   * It is the Process end time of a process.
   */
  private Date _processEndTime;
  
  /**
   * It indicate the current status of a process
   */
  private String _processStatus;
  
  /**
   * It indicates the error type of a process if any
   */
  private String _errorType;
  
  /**
   * It indicates the error reason of a process if any
   */
  private String _errorReason;
  
  /**
   * It is the UID of a process
   */
  private Long _processInstanceUID;
  
  public ProcessInfo()
  {
    
  }
  public ProcessInfo(String processID, Date processStartTime, Date processEndTime, 
                             String processStatus, String errorType, String errorReason,
                             Long processInstanceUID)
  {
    setProcessID(processID);
    setProcessStartTime(processStartTime);
    setProcessEndTime(processEndTime);
    setProcessStatus(processStatus);
    setErrorType(errorType);
    setErrorReason(errorReason);
    setProcessInstanceUID(processInstanceUID);
  }

  public String getErrorReason()
  {
    return _errorReason;
  }

  public void setErrorReason(String reason)
  {
    _errorReason = reason;
  }

  public String getErrorType()
  {
    return _errorType;
  }

  public void setErrorType(String type)
  {
    _errorType = type;
  }

  public Date getProcessEndTime()
  {
    return _processEndTime;
  }

  public void setProcessEndTime(Date endTime)
  {
    _processEndTime = endTime;
  }

  public String getProcessID()
  {
    return _processID;
  }

  public void setProcessID(String _processid)
  {
    _processID = _processid;
  }

  public Long getProcessInstanceUID()
  {
    return _processInstanceUID;
  }

  public void setProcessInstanceUID(Long instanceUID)
  {
    _processInstanceUID = instanceUID;
  }

  public Date getProcessStartTime()
  {
    return _processStartTime;
  }

  public void setProcessStartTime(Date startTime)
  {
    _processStartTime = startTime;
  }

  public String getProcessStatus()
  {
    return _processStatus;
  }

  public void setProcessStatus(String status)
  {
    _processStatus = status;
  }
  
  public String toString()
  {
    return "ProcessInfo[processID:"+getProcessID()+" processStartTime: "+getProcessStartTime()+" processEndTime: "+getProcessEndTime()+
           " processStatus: "+getProcessStatus()+" errorType: "+getErrorType()+" errorReason: "+getErrorReason()+" processInstanceUID: "+getProcessInstanceUID()+"]";
  }
}
