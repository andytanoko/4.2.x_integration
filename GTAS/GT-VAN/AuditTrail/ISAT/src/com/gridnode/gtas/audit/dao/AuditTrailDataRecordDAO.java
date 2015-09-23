/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 8, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.dao;

import java.util.Collection;
import java.util.List;

import com.gridnode.gtas.audit.model.AuditTrailDataRecord;
import com.gridnode.util.db.DAO;

/**
 * This class handle the CRUD operation for the entity AuditTrailDataRecord.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailDataRecordDAO extends DAO
{
  public AuditTrailDataRecordDAO()
  {
    super(false);
  }
  
  public AuditTrailDataRecordDAO(boolean newSession)
  {
    super(newSession);
  }
  
  public Collection<AuditTrailDataRecord> retrieveObjects()
  {
    return getAll("AuditTrailDataRecord");
  }
  
  /**
   * Retrieve a AuditTrailDataRecord which has the attemptCount smaller than the
   * specify maxAttemptCount
   * @param maxAttemptCount The maximum number of attempt for processing an AuditTrailData record.
   * @return
   */
  public AuditTrailDataRecord retrieveSingleAuditTrailDataByMaxAttemptCount(int maxAttemptCount)
  {
    String queryName = getPersistentClass().getName()+".getTrailDataByAttemptCount";
    String[] paramName = new String[]{"attemptCount"};
    Integer[] paramValue = new Integer[]{maxAttemptCount};
    return (AuditTrailDataRecord)queryOne(queryName, paramName, paramValue);
  }
  
  public List<String> retrieveNAuditTrailDataByMaxAttemptCount(int maxAttemptCount, int totalNumberToFetch)
  {
    String queryName = getPersistentClass().getName()+".getAuditTrailDataUIDByAttemptCount";
    String[] paramName = new String[]{"attemptCount"};
    Integer[] paramValue = new Integer[]{maxAttemptCount};
    return (List<String>)queryN(queryName, paramName, paramValue, totalNumberToFetch);
  }
  
  public int retrieveTotalAuditTrailDataByMaxAttemptCount(int maxAttemptCount)
  {
    String queryName = getPersistentClass().getName()+".getTotalTrailDataByAttemptCount";
    String[] paramName = new String[]{"attemptCount"};
    Integer[] paramValue = new Integer[]{maxAttemptCount};
    Long total = (Long)queryOne(queryName, paramName, paramValue);
    return total == null ? 0 : total.intValue();
  }
  
  public AuditTrailDataRecord retrieveAuditTrailDataRecordByUID(String trailDataUID)
  {
    String queryName = getPersistentClass().getName()+".getAuditTrailDataByUID";
    String[] paramName = new String[]{"uid"};
    String[] paramValue = new String[]{trailDataUID};
    return (AuditTrailDataRecord)queryOne(queryName, paramName, paramValue);
  }
  
  public String persistAuditTrailData(AuditTrailDataRecord objContainer)
  {
    return (String)create(objContainer);
  }
  
  public void deleteAuditTrailData(AuditTrailDataRecord objContainer)
  {
    delete(objContainer);
  }
  
  private Class getPersistentClass()
  {
    return AuditTrailDataRecord.class;
  }
}
