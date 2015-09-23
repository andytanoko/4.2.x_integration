/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 5, 2007        i00107             Created
 * Feb 24 2007    i00107              Add event remark.
 */

package com.gridnode.gridtalk.httpbc.common.model;

import java.util.Date;

/**
 * @author i00107
 * This bean is used in event notification to hold the event information.
 */
public class EventInfo
{
  private String _eventName;
  private Date _eventOccurredTime;
  private String _messageID;
  private String _status;
  private String _errorReason;
  private String _tracingID;
  private String _eventType;
  private String _beID;
  private String _eventRemark;
  
  public EventInfo()
  {
    
  }
  
  /**
   * @return the beID
   */
  public String getBeID()
  {
    return _beID;
  }
  /**
   * @param beID the beID to set
   */
  public void setBeID(String beID)
  {
    _beID = beID;
  }
  /**
   * @return the errorReason
   */
  public String getErrorReason()
  {
    return _errorReason;
  }
  /**
   * @param errorReason the errorReason to set
   */
  public void setErrorReason(String errorReason)
  {
    _errorReason = errorReason;
  }
  /**
   * @return the eventName
   */
  public String getEventName()
  {
    return _eventName;
  }
  /**
   * @param eventName the eventName to set
   */
  public void setEventName(String eventName)
  {
    _eventName = eventName;
  }
  /**
   * @return the eventOccurTime
   */
  public Date getEventOccurredTime()
  {
    return _eventOccurredTime;
  }
  /**
   * @param eventOccurTime the eventOccurTime to set
   */
  public void setEventOccurredTime(Date eventOccurTime)
  {
    _eventOccurredTime = eventOccurTime;
  }
  /**
   * @return the eventType
   */
  public String getEventType()
  {
    return _eventType;
  }
  /**
   * @param eventType the eventType to set
   */
  public void setEventType(String eventType)
  {
    _eventType = eventType;
  }
  /**
   * @return the messageID
   */
  public String getMessageID()
  {
    return _messageID;
  }
  /**
   * @param messageID the messageID to set
   */
  public void setMessageID(String messageID)
  {
    _messageID = messageID;
  }
  /**
   * @return the status
   */
  public String getStatus()
  {
    return _status;
  }
  /**
   * @param status the status to set
   */
  public void setStatus(String status)
  {
    _status = status;
  }
  /**
   * @return the tracingID
   */
  public String getTracingID()
  {
    return _tracingID;
  }
  /**
   * @param tracingID the tracingID to set
   */
  public void setTracingID(String tracingID)
  {
    _tracingID = tracingID;
  }

  /**
   * @return the eventRemark
   */
  public String getEventRemark()
  {
    return _eventRemark;
  }

  /**
   * @param eventRemark the eventRemark to set.
   */
  public void setEventRemark(String eventRemark)
  {
    _eventRemark = eventRemark;
  }
  
}
