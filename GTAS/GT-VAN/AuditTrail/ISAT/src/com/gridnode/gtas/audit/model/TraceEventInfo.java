/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TraceEventInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 31, 2007    Tam Wei Xiang       Add hibernate query for fetching list of
 *                                     TraceEventInfo given tracingID
 * Feb 02, 2007    Tam Wei Xiang       Added eventRemark     
 * Mar 01, 2007    Tam Wei Xiang       Added the reprocessLinkageUID to link the current
 *                                     event history to the new process transaction
 *                                     after we perform the reprocess operation.             
 * Apr 09, 2007    Tam Wei Xiang       Added in msgType.  
 * Jul 29, 2008    Tam Wei Xiang       #69 Add query for checking the existing of
 *                                         a particular TraceEventInfo record.                                                   
 */
package com.gridnode.gtas.audit.model;

import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class has a direct mapping on the record in the TraceEventInfo table.
 * @author Tam Wei Xiang
 * @since GT 4.0
 * 
 * @hibernate.class table = "`isat_trace_event_info`"
 * 
 * @hibernate.query name = "getTraceEventInfoByTraceIDAndEventName" 
 *    query = "FROM TraceEventInfo AS ti WHERE ti.eventName = :eventName AND ti.tracingID =:tracingID"   
 * @hibernate.query name = "getTraceEventInfoByTraceIDEventNameAndMsgID" 
 *    query = "FROM TraceEventInfo AS ti WHERE ti.eventName = :eventName AND ti.tracingID =:tracingID AND ti.messageID =:messageID"
 * @hibernate.query
 *   query = "FROM TraceEventInfo AS ti WHERE ti.tracingID = :tracingID"
 *   name = "getTraceEventInfoByTracingID"  
 * @hibernate.query
 *   query = "FROM TraceEventInfo AS ti WHERE ti.tracingID = :tracingID AND (ti.messageID IS NOT NULL) ORDER BY ti.eventOccurTime ASC"
 *   name = "getTraceEventInfoOrderByEventOccuredTime"
 * @hibernate.query
 *   query = "FROM TraceEventInfo AS ti WHERE ti.tracingID = :tracingID AND ti.eventName != :eventName"
 *   name = "getTraceEventInfoExcludeEventName" 
 * @hibernate.query
 *   query = "SELECT DISTINCT ti.eventName FROM TraceEventInfo AS ti WHERE ti.tracingID = :tracingID AND ti.eventName != :eventName"
 *   name = "getDistinctEventNameExcludeEventName"       
 * @hibernate.query
 *   query = "SELECT DISTINCT ti.tracingID FROM TraceEventInfo AS ti WHERE ti.eventOccurTime >= :fromDateTime AND ti.eventOccurTime <= :toDateTime"
 *   name = "getTraceEventInfoTracingIDByOccuredTime"  
 * @hibernate.query
 *   query = "FROM TraceEventInfo AS ti WHERE ti.UID = :uid"
 *   name = "getTraceEventInfoByUID"  
 * @hibernate.query
 *   query = "FROM TraceEventInfo AS ti WHERE ti.tracingID = :tracingID AND ti.eventName = :eventName ORDER BY ti.eventOccurTime DESC"  
 *   name = "getTraceEventInfoByTraceIDEventNameOrderByTime"  
 * @hibernate.query
 *   query = "SELECT ti.eventOccurTime FROM TraceEventInfo AS ti ORDER BY ti.eventOccurTime ASC"
 *   name = "getEarlistEventOccuredTime"
 * @hibernate.query
 *   query = "SELECT COUNT(*) FROM TraceEventInfo AS ti where ti.tracingID = :tracingID AND ti.messageID = :messageID AND ti.eventName = :eventName AND ti.eventOccurTime = :eventOccurTime"
 *   name = "getEventCountByEventNameTracingIDMsgIDEventOccur"
 * @hibernate.query
 *   query = "SELECT COUNT(*) FROM TraceEventInfo AS ti where ti.tracingID = :tracingID AND ti.eventName = :eventName AND ti.eventOccurTime = :eventOccurTime"
 *   name = "getEventCountByEventNameTracingIDEventOccur"
 */
public class TraceEventInfo extends AbstractPersistable implements IAuditTrailEntity
{
  private String _eventName;
  private java.util.Date _eventOccurTime;
  private String _messageID;
  private String _status;
  private String _errorReason;
  private String _bizDocumentUID;
  private String _tracingID;
  private Long _reprocessLinkageUID;
  private String _eventRemark;
  private String _groupName;
  private String _msgType; //indicate whether the event is belong to action msg or signal msg
  
  public TraceEventInfo()
  {
  }
  
  public TraceEventInfo(String groupName, String eventName, java.util.Date eventOccuredTime,
                        String messageID, String status, String errorReason, String bizDocumentUID,
                        String tracingID, String eventRemark, String msgType)
  {
    setEventName(eventName);
    setEventOccurTime(eventOccuredTime);
    setMessageID(messageID);
    setStatus(status);
    setErrorReason(errorReason);
    setBizDocumentUID(bizDocumentUID);
    setTracingID(tracingID);
    setEventRemark(eventRemark);
    setGroupName(groupName);
    setMsgType(msgType);
  }
  
  /**
   * @hibernate.property name="groupName" column = "`group_name`"
   */
  public String getGroupName()
  {
    return _groupName;
  }

  public void setGroupName(String name)
  {
    _groupName = name;
  }

  /**
   * hibernate.many-to-one constrained = "true" class = "com.gridnode.gtas.audit.model.BizDocument" not-null = "false" column = "biz_document_uid"
   * @hibernate.property column = "`biz_document_uid`"
   * hibernate.many-to-one constrained = "true" foreign-key = "bizDocumentUID" class = "com.gridnode.gtas.audit.model.BizDocument" not-null = "false" column = "biz_document_uid"
   * @return
   */
  public String getBizDocumentUID()
  {
    return _bizDocumentUID;
  }

  public void setBizDocumentUID(String documentUID)
  {
    _bizDocumentUID = documentUID;
  }
  
  /**
   * @hibernate.property name = "errorReason" column = "`error_reason`" type = "string"
   * @return
   */
  public String getErrorReason()
  {
    return _errorReason;
  }

  public void setErrorReason(String reason)
  {
    _errorReason = reason;
  }
  
  /**
   * @hibernate.property name = "eventName" column = "`event_name`"
   * @return
   */
  public String getEventName()
  {
    return _eventName;
  }

  public void setEventName(String name)
  {
    _eventName = name;
  }
  
  /**
   * has to specify the type of the field since hibernate won't know the date is mapped to SQL time, timestamp or date
   * @hibernate.property name = "eventOccurTime" column = "`event_occur_time`" type = "timestamp"
   * @return
   */
  public java.util.Date getEventOccurTime()
  {
    return _eventOccurTime;
  }

  public void setEventOccurTime(java.util.Date occuredTime)
  {
    _eventOccurTime = occuredTime;
  }
  
  /**
   * @hibernate.property name = "messageID" column = "`message_id`" type = "string"
   * @return
   */
  public String getMessageID()
  {
    return _messageID;
  }

  public void setMessageID(String _messageid)
  {
    _messageID = _messageid;
  }
  
  /**
   * @hibernate.property name = "status" column = "`status`" type = "string"
   * @return
   */
  public String getStatus()
  {
    return _status;
  }

  public void setStatus(String _status)
  {
    this._status = _status;
  }
  
  /**
   * @hibernate.property name = "tracingID" column = "`tracing_id`" type = "string"
   * @return
   */
  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String _tracingid)
  {
    _tracingID = _tracingid;
  }
  
  /**
   * @hibernate.property name = "eventRemark" column = "`event_remark`"
   * @return
   */
  public String getEventRemark()
  {
    return _eventRemark;
  }

  public void setEventRemark(String remark)
  {
    _eventRemark = remark;
  }
  
  /**
   * @hibernate.property name = "reprocessLinkageUID" column = "`reprocess_linkage_uid`"
   * @return
   */
  public Long getReprocessLinkageUID()
  {
    return _reprocessLinkageUID;
  }

  public void setReprocessLinkageUID(Long linkageUID)
  {
    _reprocessLinkageUID = linkageUID;
  }
  
  /**
   * @hibernate.property name= "msgType" column = "`msg_type`"
   * @return
   */
  public String getMsgType()
  {
    return _msgType;
  }

  public void setMsgType(String type)
  {
    _msgType = type;
  }

  public String toString()
  {
    return "TraceEventInfo["+(getUID()== null ? "" : getUID())+" eventName: "+getEventName()+" eventOccuredTime: "+getEventOccurTime()+
           " messageID: "+getMessageID()+" status: "+getStatus()+" errorReason "+getErrorReason()+
           " bizDocumentUID: "+getBizDocumentUID()+" tracingID: "+getTracingID()+" groupName: "+getGroupName()+
           " eventRemark: "+getEventRemark()+" reprocessLinkageUID :"+getReprocessLinkageUID()+" msgType: "+getMsgType()+"]";
  }
  
  
}
