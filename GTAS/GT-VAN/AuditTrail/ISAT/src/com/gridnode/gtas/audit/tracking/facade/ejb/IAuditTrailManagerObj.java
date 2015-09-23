/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 4, 2007    Tam Wei Xiang       Created
 * Jul 29,2007    Tam Wei Xiang       #69: Enable the TXMR to handle the AuditTrailData
 *                                         that maybe processed before. This can happen
 *                                         when the JMS msg redelivered mechanism kick in.
 *                                         By allowing handling of the redelivered msg,
 *                                         the TXMR event can survive during the Clustering
 *                                         fail over kick in.
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBObject;

import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.model.AuditTrailDataRecord;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditTrailManagerObj extends EJBObject
{
  
  /**
   * Delegate the processing of AuditTrailData to TrailInfoHandler which in turn will process the AuditTrailData
   * and persist in DB.
   * @param trailData
   * @throws Exception
   */
  public void handleAuditTrailData(AuditTrailData trailData) throws AuditTrailTrackingException, RemoteException;
  
  /**
   * #69 Delegate the processing of AuditTrailData to TrailInfoHandler which in turn will process the AuditTrailData.
   * @param trailData The redelivered trailData from the JMS provider
   * @throws AuditTrailTrackingException
   * @throws RemoteException
   */
  public void handleRedeliveredAuditTrailData(AuditTrailData trailData) throws AuditTrailTrackingException, RemoteException;
  
  /**
   * Delegate the processing of AuditTrailData UID to TrailInfoHandler which in turn will process the AuditTrailData
   * and persist in DB.
   * @param trailDataUID
   * @throws Exception
   */
  public void handleAuditTrailData(String trailDataUID) throws AuditTrailTrackingException, RemoteException;
  
  /**
   * In clustering environment, the JMS msg will be delivered to the available node in the clusther based on
   * load-balance mode. And we want to enforce the processing of the AuditTrailData should not be in paraller; thus
   * we delay the processing of the AuditTrail data by persisting it in second storage first, after that a sceduler will
   * invoke a MBean service to perform the actual handling of the AuditTrail data which ensure we process the msg by sequence.
   * @param auditTrailData
   */
  public void delayProcessAuditTrailData(AuditTrailData auditTrailData) throws RemoteException;
  
  /**
   * Update the info for the failed AuditTrailData record. Eg increase the attemptCount, the reason that failed
   * to process the record. No update will be executed if System error has occured eg the DB err 
   * @param failedReason
   * @param trailDataRecord
   * @param dao
   */
  public void updateAuditTrailRecord(String trailDataRecordUID, String exceptionMsg) throws Exception, RemoteException;
  
  /**
   * Delete the AuditTrailData if we successfully process the trailDataRecord.
   * @param trailDataRecord
   * @param dao
   */
  public void deleteAuditTrailData(AuditTrailDataRecord trailDataRecord) throws RemoteException;
  
  /**
   * Determine whether we have any AuditTrailData record which its current attempt count is less than the given maxAttemptCount.
   * The totalRecordToFetch indicate how many record will be fetched.
   * @param maxAttemptCount 
   * @param totalRecordToFetch
   * @return
   * @throws RemoteException
   */
  public List<String> retrieveAuditTrailDataRecordUID(int maxAttemptCount, int totalRecordToFetch) throws Exception, RemoteException;
}
