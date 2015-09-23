/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Feb 02, 2007    Tam Wei Xiang       Added eventRemark
 */
package com.gridnode.gtas.audit.common.model;

import java.util.Date;

/**
 * This class contains the required information about a particular activity that has been 
 * performed on the document (or about the event that has acted on the document). Intermediate
 * document will be encapsulated in a BusinessDocument instance.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class EventInfo implements ITrailInfo
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -596179620980248805L;

  /**
   * It is the name of the event that has acted on the document 
   */
  private String _eventName;
  
  /**
   * It indicate the time that the event has occured
   */
  private Date _eventOccurredTime;
  
  /**
   * It is the combination of folder name and the gdoc ID. (Note: not every EventInfo will have the messageID)
   */
  private String _messageID;
  
  /**
   * It indicates the status of executing a particular event that represent by the eventName
   */
  private String _status;
  
  /**
   * It indicates the reason that the event fail to execute
   */
  private String _errorReason;
  
  /**
   * It can link all the events that has acted on a particular transaction doc
   */
  private String _tracingID;
  
  /**
   * It indicates the type of transaction doc that this event has acted on. 
   */
  private String _eventType;
  
  /**
   * It will be used to derive the groupName in the OTC.
   */
  private String _beID;
  
  /**
   * It indicate the additional info about the event eg the name of the mapping rule we are applying
   */
  private String _eventRemark;
  
  public EventInfo()
  {
    
  }
  public EventInfo(String eventName, Date eventOccuredTime, String messageID, String status,
                   String errorReason, String tracingID, String eventType, String beID,
                   String eventRemark)
  {
    setEventName(eventName);
    setEventOccurredTime(eventOccuredTime);
    setMessageID(messageID);
    setStatus(status);
    setErrorReason(errorReason);
    setTracingID(tracingID);
    setEventType(eventType);
    setBeID(beID);
    setEventRemark(eventRemark);
  }

  public String getBeID()
  {
    return _beID;
  }

  public void setBeID(String _beid)
  {
    _beID = _beid;
  }

  public String getErrorReason()
  {
    return _errorReason;
  }

  public void setErrorReason(String reason)
  {
    _errorReason = reason;
  }

  public String getEventName()
  {
    return _eventName;
  }

  public void setEventName(String name)
  {
    _eventName = name;
  }

  public Date getEventOccurredTime()
  {
    return _eventOccurredTime;
  }

  public void setEventOccurredTime(Date occuredTime)
  {
    _eventOccurredTime = occuredTime;
  }

  public String getEventType()
  {
    return _eventType;
  }

  public void setEventType(String type)
  {
    _eventType = type;
  }

  public String getMessageID()
  {
    return _messageID;
  }

  public void setMessageID(String _messageid)
  {
    _messageID = _messageid;
  }

  public String getStatus()
  {
    return _status;
  }

  public void setStatus(String _status)
  {
    this._status = _status;
  }

  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String _tracingid)
  {
    _tracingID = _tracingid;
  }

  public String getEventRemark()
  {
    return _eventRemark;
  }

  public void setEventRemark(String remark)
  {
    _eventRemark = remark;
  }

  public String toString()
  {
    return "EventInfo[eventName: "+getEventName()+" eventOccuredTime: "+getEventOccurredTime()+
           " messageID: "+getMessageID()+" status: "+getStatus()+" errorReason: "+getErrorReason()+
           " tracingID: "+getTracingID()+" eventType: "+getEventType()+" beID: "+getBeID()+" eventRemark:" +getEventRemark()+"]";
  }
}
