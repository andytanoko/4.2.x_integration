/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessTransactionDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 4, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.LockMode;

import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.ProcessTransaction;

/**
 * This class handle the CRUD operation for the entity ProcessTransaction.
 * NOTE: the caller require to under the JTA transaction. EG call from SessionBean, MDBean
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ProcessTransactionDAO extends AuditTrailEntityDAO
{
  private final String CLASS_NAME = "ProcessTransactionDAO";
  public ProcessTransactionDAO() 
  {
    super(false);
  }
  
  public ProcessTransactionDAO(boolean newSession)
  {
    super(newSession);
  }
  
  public ProcessTransaction retrieveProTransactionByProcessInstanceUID(Long processUID)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransByProUID";
    String[] paramName = new String[]{"processInstanceUID"};
    Long[] paramValue = new Long[]{processUID};
    
    ProcessTransaction proTran = (ProcessTransaction)queryOne(queryName, paramName, paramValue);
    return proTran;
  }
  
  
  public ProcessTransaction selectForUpdateProcessTransaction(Long processUID)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransByProUID";
    String[] paramName = new String[]{"processInstanceUID"};
    Long[] paramValue = new Long[]{processUID};
    
    List processTransList = queryWithLock(queryName, paramName, paramValue, "pt", LockMode.UPGRADE);
    if(processTransList != null && processTransList.size() > 0)
    {
      return (ProcessTransaction)processTransList.iterator().next();
    }
    return null;
  }
  
  public Collection<Long> retrieveProInstanceUID(Date fromStartTime, Date toStartTime)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransUIDByStartTimeWithoutGroup";
    String[] paramName = new String[]{"fromStartTime", "toStartTime"};
    Object[] paramValue = new Object[]{fromStartTime, toStartTime};
    return query(queryName, paramName, paramValue);
  }
  
  /**
   * Obtain a list of process instance uid that fall within the process start time range and fullfilling the group list  
   * @param fromStartTime The process start time range
   * @param toStartTime The process start time range
   * @param groupList List of group that will be archived
   * @return
   */
  public Collection<Long> retrieveProInstanceUID(Date fromStartTime, Date toStartTime, Collection groupList)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransUIDByStartTimeWithGroup";
    String[] paramName = new String[]{"fromStartTime", "toStartTime", "groupNameList"};
    Object[] paramValue = new Object[]{fromStartTime, toStartTime, groupList};
    return query(queryName, paramName, paramValue);
  }
  
  /**
   * Retrieve the first process instance uid that its process start time is the earliest given the
   * date range fromStartTime and toStartTime. 
   * @param fromStartTime
   * @param toStartTime
   * @param groupList
   * @return
   */
  public Long retrieveProInstanceUIDOrderByStartTime(Date fromStartTime, Date toStartTime, Collection groupList)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransUIDSortByStartTime";
    String[] paramName = new String[]{"fromStartTime", "toStartTime", "groupNameList"};
    Object[] paramValue = new Object[]{fromStartTime, toStartTime, groupList};
    return (Long)queryOne(queryName, paramName, paramValue);
  }
  
  /**
   * Obtain a list of process instance uid that is smaller than the given processInstanceUID and their process start
   * time is null.
   * @param processInstanceUID
   * @return
   */
  public List<Long> retrieveProInstanceUIDLesserThan(Long processInstanceUID, Collection groupList)
  {
    String queryName = getPersistenceClass().getName()+".getProcessTransUIDLesserThan";
    String[] paramName = new String[]{"processInstanceUID", "groupNameList"};
    Object[] paramValue = new Object[]{processInstanceUID, groupList};
    return query(queryName, paramName, paramValue);
  }
 
  public void insertProcessTrans(ProcessTransaction processTrans)
  {
    String queryName = getPersistenceClass().getName()+".insertProcessFromProcessTrans";
    String[] paramName = new String[]{"processID", "processStartTime", "processEndTime", "processStatus", "errorType", "errorReason", "processInstanceUID", "isProcessSuccess", "uid"};
    Object[] paramValues = new Object[]{processTrans.getProcessID(), processTrans.getProcessStartTime(), processTrans.getProcessEndTime(),
                                        processTrans.getProcessStatus(), processTrans.getErrorType(), processTrans.getErrorReason(),
                                        processTrans.getProcessInstanceUID(), processTrans.isProcessSuccess(), processTrans.getUID()};
    query(queryName, paramName, paramValues);
  }
  
  /**
   * Get the earliest ProcessTransaction's process start time
   * @return
   */
  public Date getEarliestProcessStartTime()
  {
    String queryName = getPersistenceClass().getName()+".getSmallestProcessStartTime";
    String[] paramName = null;
    String[] paramValues = null;
    return (Date)queryOne(queryName, paramName, paramValues);
  }
  
  /**
   * Return the Persistence Class that this DAO is handling.
   */
  public Class getPersistenceClass()
  {
    return ProcessTransaction.class;
  }
}
