/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventInfoHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 08, 2007    Tam Wei Xiang       Change the UID from Long to String
 * Jan 31, 2007    Tam Wei Xiang       Retrieve a list of EventInfo given TracingID
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.util.Collection;
import java.util.Date;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.common.model.ITrailInfo;
import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.dao.TraceEventInfoDAO;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.model.TraceEventInfoHeader;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.pdip.framework.notification.IDocumentFlow;

/**
 * This class provides the necessary methods that to be used by the TrailInfoHandler to perform
 * creation, update, retrieve, and persistence for TraceEventInfo.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class EventInfoHelper extends AbstractInfoHelper
{
  private static final String CLASS_NAME = "EventInfoHelper";
  private static TraceEventInfoDAO _dao;
  
  public EventInfoHelper()
  {
    //super(_dao);
    _dao = new TraceEventInfoDAO(false);
  }
  
  /*
  public static EventInfoHelper getInstance()
  {
    return _helper;
  }*/
  
  public TraceEventInfo createTraceEventInfo(EventInfo info, String groupName, String bizDocUID)
  {
    return new TraceEventInfo(groupName, info.getEventName(), info.getEventOccurredTime(),
                              info.getMessageID(), info.getStatus(), info.getErrorReason(), bizDocUID,
                              info.getTracingID(), info.getEventRemark(), info.getEventType());
  }
  
  public TraceEventInfoHeader createTraceEventInfoHeader(EventInfo info, String groupName)
  {
    Integer errorCount = getErrorCount(info.getStatus());
    return new TraceEventInfoHeader(groupName, info.getStatus(), 
                                    errorCount, info.getEventOccurredTime(), info.getTracingID(), info.getEventType());
  }
  
  public TraceEventInfoHeader getTraceEventInfoHeader(String tracingID) throws AuditTrailDBServiceException
  {
    return _dao.getTraceEventInfoHeader(tracingID);
  } 
  
  public void updateTraceEventInfoHeader(EventInfo eventInfo, TraceEventInfoHeader header,
                                         String groupName)
  {
    //the field required to be updated
    String lastEventStatus = eventInfo.getStatus();
    Integer errorCount = header.getErrorCount() + getErrorCount(eventInfo.getStatus());
    Date lastEventOccuredTime = eventInfo.getEventOccurredTime();
    
    header.setLastEventStatus(lastEventStatus);
    header.setErrorCount(errorCount);
    header.setLastEventOccurredTime(lastEventOccuredTime);
    
    if(header.getGroupName() == null || "".equals(header.getGroupName()))
    {
      header.setGroupName(groupName);
    }
    
    if(header.getMsgType() == null || "".equals(header.getMsgType()))
    {
      header.setMsgType(eventInfo.getEventType());
    }
    updateAuditTrailEntity(header);
  }
  
  /**
   * Get the TraceEventInfo given the tracingId and eventType. Use only we are sure given the criteria
   * it only return exactly one result. (consider the reprocessing case where two set of event history
   * from two diff ProcessTransaction can share the same tracingID .) TracingID + event TYpe is not unique
   * for some event type eg the ChannelConnectivity.
   * @param tracingID
   * @param eventType
   * @return
   * @throws AuditTrailDBServiceException
   */
  public TraceEventInfo getTraceEventInfo(String tracingID, String eventType) throws AuditTrailDBServiceException
  {
    return _dao.retrieveTraceEventInfo(tracingID, eventType);
  } 
  
  /**
   * Return the TraceEventInfo entity given the tracingID, eventName, and msgID
   * @param tracingID
   * @param eventName
   * @param msgID
   * @return
   * @throws AuditTrailTrackingException
   */
  public TraceEventInfo getTraceEventInfo(String tracingID, String eventName, String msgID) throws AuditTrailTrackingException
  {
      return _dao.retrieveTraceEventInfo(tracingID, eventName, msgID);
  }
  
  /**
   * Return the 1st TraceEventInfo from a set of TraceEventInfos given the tracingID, eventName, and order by desc order
   * based on the event occured time.
   * @param tracingID
   * @param eventName
   * @return
   */
  public TraceEventInfo getTraceEventInfoOrderByDescOccuredTime(String tracingID, String eventName)
  {
    return _dao.getTraceEventInfoByTraceIDEventName(tracingID, eventName);
  }
  
  /**
   * If the status is Success, the returned error count will be 0. If fail, the return
   * error count will be 1.
   * @param status The status of the TraceEventInfo
   * @return 0 if the status is sucess; 1 if the status is fail
   */
  public Integer getErrorCount(String status)
  {
    return IAuditTrailConstant.STATUS_FAIL.equals(status) ? 1 : 0;
  }
  
  protected AuditTrailEntityDAO getDAO()
  {
    return _dao;
  }
}
