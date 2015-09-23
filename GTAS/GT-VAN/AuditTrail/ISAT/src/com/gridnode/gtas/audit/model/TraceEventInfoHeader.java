/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventTrailHeader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.model;

import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class encapsulates the common information from a collection of
 * TraceEventInfo. The common info included the lastEventStatus, groupID, and errorCount.
 * It has a direct mapping on one of the record in TraceEventInfoHeeader table.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 * 
 * @hibernate.class table = "`isat_trace_event_header`"
 * @hibernate.cache usage = "read-write"
 * 
 * @hibernate.query name = "getTraceEventHeader"
 *    query = "FROM TraceEventInfoHeader as th WHERE th.tracingID = :tracingID"
 * @hibernate.query
 *   query = "SELECT th.tracingID FROM TraceEventInfoHeader th WHERE th.tracingID IN (:tracingID) AND th.groupName IN (:group)"
 *   name = "getTracingIDWithGroupInfo" 
 * @hibernate.query
 *   query = "SELECT th.tracingID FROM TraceEventInfoHeader th WHERE th.tracingID IN (:tracingID) AND (th.groupName IS NULL OR th.groupName='')"
 *   name = "getTracingIDWithoutGroupInfo"    
 */
public class TraceEventInfoHeader extends AbstractPersistable implements IAuditTrailEntity
{
  private String _lastEventStatus;
  private Integer _errorCount;
  private Date _lastEventOccurredTime;
  private String _tracingID;
  private String _msgType;
  private String _groupName;
  
  public TraceEventInfoHeader()
  {
  }
  
  public TraceEventInfoHeader(String groupName, String lastEventStatus,
                          Integer errorCount, Date lastEventOccuredTime,
                          String tracingID, String msgType)
  {
    setLastEventStatus(lastEventStatus);
    setErrorCount(errorCount);
    setLastEventOccurredTime(lastEventOccuredTime);
    setTracingID(tracingID);
    setMsgType(msgType);
    setGroupName(groupName);
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
   * @hibernate.property name = "errorCount" column = "`error_count`"
   * @return
   */
  public Integer getErrorCount()
  {
    return _errorCount;
  }

  public void setErrorCount(Integer count)
  {
    _errorCount = count;
  }
  
  /**
   * @hibernate.property name = "lastEventOccurredTime" column = "`last_event_occur_time`" type = "timestamp"
   * @return
   */
  public Date getLastEventOccurredTime()
  {
    return _lastEventOccurredTime;
  }

  public void setLastEventOccurredTime(Date eventOccuredTime)
  {
    _lastEventOccurredTime = eventOccuredTime;
  }
  
  /**
   * @hibernate.property name = "lastEventStatus" column = "`last_event_status`"
   * @return
   */
  public String getLastEventStatus()
  {
    return _lastEventStatus;
  }

  public void setLastEventStatus(String eventStatus)
  {
    _lastEventStatus = eventStatus;
  }
  
  /**
   * @hibernate.property name = "tracingID" column = "`tracing_id`"
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
   * @hibernate.property name = "msgType" column = "`msg_type`"
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
    return "TraceEventInfoHeader["+(getUID()== null ? "" : getUID())+" lastEventStatus: "+getLastEventStatus()+" errorCount: "+getErrorCount()+
           " lastEventOccuredTime: "+getLastEventOccurredTime()+" tracingID: "+getTracingID()+" msgType: "+getMsgType()+" groupName: "+getGroupName()+"]";
  }
  
}
