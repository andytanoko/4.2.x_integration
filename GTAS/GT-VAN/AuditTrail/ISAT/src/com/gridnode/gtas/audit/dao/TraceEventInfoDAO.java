/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TraceEventInfoDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 1, 2006    Tam Wei Xiang       Created
 * Jan 31, 2007   Tam Wei Xiang       add method for retrieve list of TraceEventInfo
 *                                    entity.
 * Jul 29, 2008   Tam Wei Xiang       #69: Add in the method to check the exist of a particular 
 *                                    event info.
 */
package com.gridnode.gtas.audit.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.model.TraceEventInfoHeader;
import com.gridnode.util.db.DAO;

/**
 * This class handle the CRUD operation for the entity TraceEventInfo.
 * NOTE: the caller require to under the JTA transaction. EG call from SessionBean, MDBean
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class TraceEventInfoDAO extends AuditTrailEntityDAO
{
  private static final String CLASS_NAME = "TraceEventInfoDAO";
  
  public TraceEventInfoDAO()
  {
    super(false);
  }
  
  public TraceEventInfoDAO(boolean newSession)
  {
    super(newSession);
  }
  
  public TraceEventInfo retrieveTraceEventInfo(String tracingID, String eventType)
  {
      String queryName = getPersistenceClass().getName()+".getTraceEventInfoByTraceIDAndEventName";
      String[] params = new String[]{"tracingID", "eventName"};
      String[] paramsValue = new String[]{tracingID, eventType};
      return (TraceEventInfo)queryOne(queryName, params, paramsValue);
  }
  
  public Collection<TraceEventInfo> retrieveTraceEventInfos(String tracingID, String eventType)
  {
      String queryName = getPersistenceClass().getName()+".getTraceEventInfoByTraceIDAndEventName";
      String[] params = new String[]{"tracingID", "eventName"};
      String[] paramsValue = new String[]{tracingID, eventType};
      
      Collection<TraceEventInfo> eventInfoList = null;
      eventInfoList = query(queryName, params, paramsValue);
      return eventInfoList;
  }
  
  public TraceEventInfo retrieveTraceEventInfo(String tracingID, String eventName, String msgID)
  {
      String queryName = getPersistenceClass().getName()+".getTraceEventInfoByTraceIDEventNameAndMsgID";
      String[] params = new String[]{"tracingID", "eventName", "messageID"};
      String[] paramsValue = new String[]{tracingID,eventName,  msgID};
      
      return (TraceEventInfo)queryOne(queryName, params, paramsValue);
  }
  
  public TraceEventInfoHeader getTraceEventInfoHeader(String tracingID)
  {
      String queryName = TraceEventInfoHeader.class.getName()+".getTraceEventHeader";
      String[] params = new String[]{"tracingID"};
      String[] paramsValue = new String[]{tracingID};
      TraceEventInfoHeader header = (TraceEventInfoHeader)queryOne(queryName, params, paramsValue);
      System.out.println("The retrieve TraceEventInfoHeader is "+header+" given tracingID "+tracingID);
      return header;
  }
  
  /**
   * Do filtering on the passed in tracing ID that correspond trace event info is associated with group info.
   * @param tracingIDs a list of tracing IDs
   * @return subset of the tracingIDs that contain the group info
   */
  public List<String> getTracingIDWithGroup(Collection<String> tracingIDs, Collection<String> groups)
  {
    String queryName = TraceEventInfoHeader.class.getName()+".getTracingIDWithGroupInfo";
    String[] params = new String[]{"tracingID", "group"};
    Object[] paramsValue = new Object[]{tracingIDs, groups};
    return query(queryName, params, paramsValue);
  }
  
/**
   * Do filtering on the passed in tracing ID that correspond trace event info is NOT associated with group info.
   * @param tracingIDs a list of tracing IDs
   * @return subset of the tracingIDs that are NOT containing the group info
   */
  public List<String> getTracingIDWithoutGroup(Collection<String> tracingIDs)
  {
    String queryName = TraceEventInfoHeader.class.getName()+".getTracingIDWithoutGroupInfo";
    String[] params = new String[]{"tracingID"};
    Object[] paramsValue = new Object[]{tracingIDs};
    return query(queryName, params, paramsValue);
  }
  
  public Collection<TraceEventInfo> getTraceEventInfoByTracingID(String tracingID)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoByTracingID";
    String[] params = new String[]{"tracingID"};
    String[] paramsValue = new String[]{tracingID};
    
    return query(queryName, params, paramsValue);
  }
  
  /**
   * Get the TraceEventInfo given the tracingID and TraceEventInfo will contain the 
   * msgID and order by asc EventOccuredTime.
   * @param tracingID
   * @return
   */
  public Collection<TraceEventInfo> getTraceEventInfoOrderByEventOccurTime(String tracingID)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoOrderByEventOccuredTime";
    String[] params = new String[]{"tracingID"};
    
    String[] paramsValue = new String[]{tracingID};
    
    return query(queryName, params, paramsValue);
  }
  
  /**
   * Retrieve a colection of TraceEventInfo entity given the tracing id and exclude the EventInfo that has
   * the specified eventName.
   * @param tracingID
   * @param eventName
   * @return
   */
  public Collection<TraceEventInfo> getTraceEventInfoExcludeEventName(String tracingID, String eventName)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoExcludeEventName";
    String[] params = new String[]{"tracingID", "eventName"};
    String[] paramsValue = new String[]{tracingID, eventName};
    
    return query(queryName, params, paramsValue);
  }
  
  public Collection<String> getDistintTraceEventInfoName(String tracingID, String eventName)
  {
    String queryName = getPersistenceClass().getName()+".getDistinctEventNameExcludeEventName";
    String[] params = new String[]{"tracingID", "eventName"};
    String[] paramsValue = new String[]{tracingID, eventName};
    
    return query(queryName, params, paramsValue);
  }
  
  /**
   * Retrieve a collection of TraceEventInfo UID given the date range based on eventOccuredTime.
   * @param fromDateTime
   * @param toDateTime
   * @return
   */
  public List<String> getTraceEventInfoTracingIDByEventOccuredTime(Date fromDateTime, Date toDateTime)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoTracingIDByOccuredTime";
    String[] params = new String[]{"fromDateTime", "toDateTime"};
    Object[] paramsValue = new Object[]{fromDateTime, toDateTime};
    
    return query(queryName, params, paramsValue);
  }
  
  public TraceEventInfo getTraceEventInfoByUID(String traceInfoUID)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoByUID";
    String[] params = new String[]{"uid"};
    String[] paramsValue = new String[]{traceInfoUID};
    
    return (TraceEventInfo)queryOne(queryName, params, paramsValue);
  }
  
  /**
   * Return the 1st TraceEventInfo from a set of TraceEventInfos given the tracingID, eventName, and order by the specify order.
   * @param tracingID
   * @param eventName
   * @param orderByAsc
   * @return
   */
  public TraceEventInfo getTraceEventInfoByTraceIDEventName(String tracingID, String eventName)
  {
    String queryName = getPersistenceClass().getName()+".getTraceEventInfoByTraceIDEventNameOrderByTime";
    String[] params = new String[]{"tracingID", "eventName"};
    String[] paramsValue = new String[]{tracingID, eventName};
    
    return (TraceEventInfo)queryOne(queryName, params, paramsValue);
  }
  
  /**
   * Retrieve the earliest TraceEventInfo's event occured time.
   * @return
   */
  public Date getEarliestEventOccuredTime()
  {
    String queryName = getPersistenceClass().getName()+".getEarlistEventOccuredTime";
    String[] params = null;
    String[] paramsValue = null;
    
    return (Date)queryOne(queryName, params, paramsValue);
  }
  
  /**
   * #69: Check whether a particular trace event info is exist given tracingID, eventName, eventOccurTime. This is unique for the txmr event for example "Unpack Payload" etc.
   * @param tracingID
   * @param eventName
   * @param eventOccurTime
   * @return
   * @throws AuditTrailDBServiceException
   */
  public int getEventCountByEventNameTracingIDEventOccur(String tracingID, String eventName, Date eventOccurTime) throws AuditTrailDBServiceException
  {    
    String queryName = getPersistenceClass().getName()+".getEventCountByEventNameTracingIDEventOccur";
    String[] paramNames = new String[]{"tracingID", "eventName", "eventOccurTime"};
    Object[] paramValues = new Object[]{tracingID, eventName, eventOccurTime};
    
    try
    {
      Long eventCount = (Long)queryOne(queryName, paramNames, paramValues);
      return (eventCount == null ? 0 : eventCount.intValue());
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".getEventCountByEventNameTracingIDEventOccur]Error in getting the total count for eventInfo coountgiven criteria[tracingID: "+tracingID+", eventName: "+eventName+", eventOccurTime: "+ eventOccurTime+"]", ex);
    }
  }
  
  /**
   * #69: Check whether a particular trace event info is exist given tracingID, eventName, msgID, eventOccurTime. This is unique for the txmr event for example "Reprocess Doc" etc.
   * @param tracingID
   * @param eventName
   * @param msgID
   * @param eventOccurTime
   * @return
   * @throws AuditTrailDBServiceException
   */
  public int getEventCountByEventNameTracingIDMsgIDEventOccur(String tracingID, String eventName, String msgID, Date eventOccurTime) throws AuditTrailDBServiceException
  {    
    String queryName = getPersistenceClass().getName()+".getEventCountByEventNameTracingIDMsgIDEventOccur";
    String[] paramNames = new String[]{"tracingID", "eventName", "messageID", "eventOccurTime"};
    Object[] paramValues = new Object[]{tracingID, eventName, msgID, eventOccurTime};
    
    try
    {
      Long eventCount = (Long)queryOne(queryName, paramNames, paramValues);
      return (eventCount == null ? 0 : eventCount.intValue());
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".getEventCountByEventNameTracingIDMsgIDEventOccur]Error in getting the total count for eventInfo coountgiven criteria[tracingID: "+tracingID+", eventName: "+eventName+" msgID: "+ msgID+", eventOccurTime: "+ eventOccurTime+"]", ex);
    }
  }
  
  /**
   * Return the Persistence Class that this DAO is handling.
   */
  public Class getPersistenceClass()
  {
    return TraceEventInfo.class;
  }
}
