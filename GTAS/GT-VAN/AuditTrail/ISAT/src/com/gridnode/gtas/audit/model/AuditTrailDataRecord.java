/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectContainer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 8, 2007    Tam Wei Xiang       Created
 * Mar 1, 2007    Tam Wei Xiang       Change the Hibernate query 'getAuditTrailDataUIDByAttemptCount'
 *                                    to be sorted by lastModifiedDate
 */
package com.gridnode.gtas.audit.model;

import java.io.Serializable;
import java.util.Date;

import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.util.db.AbstractPersistable;

/**
 * It encapsulate the obj that we are gonna to process in the later stage. 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 * 
 * @hibernate.class table = "`isat_audit_trail_data`"
 * 
 * @hibernate.query
 *   query = "SELECT COUNT(*) FROM AuditTrailDataRecord AS at WHERE (at.attemptCount < :attemptCount OR :attemptCount = -1)"
 *   name = "getTotalTrailDataByAttemptCount"  
 * @hibernate.query
 *   query = "FROM AuditTrailDataRecord AS at WHERE (at.attemptCount < :attemptCount OR :attemptCount = -1)"
 *   name = "getTrailDataByAttemptCount"
 * @hibernate.query
 *   query = "FROM AuditTrailDataRecord AS at WHERE at.UID= :uid"
 *   name = "getAuditTrailDataByUID"
 * @hibernate.query
 *   query = "SELECT at.UID FROM AuditTrailDataRecord AS at WHERE (at.attemptCount < :attemptCount OR :attemptCount = -1) ORDER BY at.lastModifiedDate ASC"
 *   name = "getAuditTrailDataUIDByAttemptCount"  
 */
public class AuditTrailDataRecord extends AbstractPersistable implements Serializable
{
  private AuditTrailData _obj;
  private int _attemptCount = 0; //The total count we has processed the record.
  private String _failedReason;
  private Date _lastModifiedDate;
  
  public AuditTrailDataRecord() {}
  
  public AuditTrailDataRecord(AuditTrailData obj)
  {
    _obj = obj;
  }
  
  /**
   * @hibernate.property name = "obj" column = "`object`" type="serializable"
   * @return
   */
  public AuditTrailData getObj()
  {
    return _obj;
  }

  public void setObj(AuditTrailData _obj)
  {
    this._obj = _obj;
  }
  
  /**
   * @hibernate.property name = "attemptCount" column = "`attempt_count`"
   * @return
   */
  public int getAttemptCount()
  {
    return _attemptCount;
  }

  public void setAttemptCount(int attemptCount)
  {
    _attemptCount = attemptCount;
  }
  
  /**
   * Increment the attempt count by 1
   *
   */
  public void incrementAttemptCount()
  {
    _attemptCount++;
  }
  
  /**
   * @hibernate.property name = "failedReason" column = "`failed_reason`"
   * @return
   */
  public String getFailedReason()
  {
    return _failedReason;
  }

  public void setFailedReason(String reason)
  {
    _failedReason = reason;
  }
  
  /**
   * @hibernate.property name = "lastModifiedDate" column = "`last_modified_date`"
   * @return
   */
  public Date getLastModifiedDate()
  {
    return _lastModifiedDate;
  }

  public void setLastModifiedDate(Date modifiedTime)
  {
    _lastModifiedDate = modifiedTime;
  }

  public String toString()
  {
    return "AuditTrailDataRecord[obj "+(_obj != null ?_obj.hashCode()+"" : "null")+" attemptCount: "+getAttemptCount()+" lastModifiedDate: "+getLastModifiedDate()+"]";
  }
}
