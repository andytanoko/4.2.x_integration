/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailArchiveManagerLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJBLocalObject;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.helper.ArchiveHelper;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditTrailArchiveManagerLocalObj extends EJBLocalObject
{
/**
   * Retrieve a collection of process instance UID (as we use in GT) that fulfil the given criteria
   * @param criteria The criteria to retrieve the process instance UID
   * @return a collection of process instance uid.
   */
  public Collection<Long> retrieveProcessTransactionUID(Hashtable criteria);
  
  /**
   * Archive the Process Transaction related doc eg the DocumentTransaction, EventInfo, BizDocument
   * @param processUID The process instance UID (use in GT)
   * @param archiveZip The archive zip helper
   * @throws ArchiveTrailDataException throw while we have error in the archival process
   * @throws RemoteException
   */
  public void archiveProcessTransaction(Long processUID, ArchiveHelper archiveHelper) throws ArchiveTrailDataException;
  
  /**
   * Return the working dir for storing the arhive files.
   * @return
   */
  public String getArchiveFolderPath();
  
  /**
   * Return the total allowable number of ProcessTrans entities that can be stored in the arcihve zip file.
   * @return
   */
  public int getTotalProcessTransEntryInArchiveZip();
  
  /**
   * Return the total allowable number of orphan TraceEventInfo entities that can be stored in the arcihve zip file.
   * @return
   */
  public int getTotalOrphanTraceEventInfoInArchiveZip();
  
  /**
   * Restore the given trailEntity into DB if it doesn't exist
   * @param trailEntity
   */
  public void restoreAuditTrailEntity(IAuditTrailEntity trailEntity);
  
  /**
   * Archive trace event info, biz document, trace event info header that linked to the given tracingID.
   * @param tracingID The tracingID of the trace event info
   * @param archiveHelper The helper that convert the entity into xml form, and add into archive zip file
   * @throws ArchiveTrailDataException Throw while we have problem in archive the audit trail data.
   */
  public void archiveTraceEventInfoGroup(String tracingID, ArchiveHelper archiveHelper) throws ArchiveTrailDataException;
  
  /**
   * Retrieve a collection of trace event info's tracingID that is not belong to any group.
   * @param criteria
   * @return
   */
  public Collection<String> retrieveOrphanTraceEventInfoTracingID(Hashtable criteria);
  
  /**
   * Retrieve a collection of orphan process instance uid that the process transaction is not containing the process start/end time
   * @param criteria
   * @return
   */
  public Collection<Long> getOrphanProcessInstanceUIDWithGroupInfo(Hashtable criteria);
  
  /**
   * Retrieve a collection of orphan process instance uid that the process transaction is not containing the group info.
   * @param criteria
   * @return
   */
  public Collection<Long> getOrphanProcessInstanceUIDWithoutGroupInfo(Hashtable criteria);
  
  /**
   * Return those tracing IDs that its correspond TraceEventInfo is tagged with group info.
   * @param criteria 
   * @return
   */
  public Collection<String> retrieveTraceEventInfoTracingIDWithGroup(Hashtable criteria);
  
  /**
   * Check whether the tracingID is linked to one or more DocumentTransaction
   * @param tracingID
   * @return
   */
  public boolean isDependentOnDocument(String tracingID);
  
  /**
   * Delegate the archive criteria to the queue as identified by the given category
   * @param criteria the archive criteria
   * @param category the jms category that we will send to
   * @throws Exception
   */
  public void delegateArchiveRequest(Hashtable criteria, String category) throws Exception;
  
/**
   * Determine whether we will perform the archive by document in GT based on the archiveRestoreStatus
   * @param archiveStatusHT
   * @throws Exception
   */
  public void handleGTArchiveStatus(Hashtable archiveStatusHT) throws Exception;
  
/**
   * Update the status of a particular job activity. The activity include GT archive by process, GT archive by document, and the TM archive.
   * If all archive activities completed, it will update the job status to the master node.
   * 
   * NOTE: associated a global lock prior calling this method.
   * @param archiveType
   * @param jobID
   * @param isArchiveSuccess
   * @param archiveID
   * @param isArchiveOrphan
   * @throws Exception
   */
  public void updateJobStatus(String archiveType, String jobID, boolean isArchiveSuccess, String archiveID,
                                          boolean isArchiveOrphan) throws Exception;
  
/**
   * The GT side has finished performed the archive. We will update the GT summary filename.
   * NOTE: associated a global lock prior calling this method.
   * @param archiveType
   * @param isArchiveSuccess
   * @param gtSummaryFilename
   * @param archiveID
   * @throws Exception
   */
  public void updateGTArchiveStatus(String archiveType, Boolean isArchiveSuccess, String gtSummaryFilename, String archiveID) throws Exception;
  
  
/**
   * Delegate the archive request to Gridtalk. 
   * @param criteria The criteria that is used for archive out the Process
   * @param archiveID The unique ID that is used to link back the archive summary in TM.
   * @throws Exception
   */
  public void delegateArchiveRequestToGT(Hashtable criteria, String archiveID) throws ArchiveTrailDataException;
  
  /**
   * Update the TM archive status
   * @param archiveID
   * @param jobID
   * @param isTMArchivedSuccess
   * @param zipFileList
   * @param totalArchiveProcessTransaction
   * @param totalIncompleteDocTrans
   * @param totalIncompleteProcess
   * @throws Exception
   */
  public void updateTMArchiveStatus(String archiveID, String jobID, boolean isTMArchivedSuccess,
                                    String[] zipFileList, int totalArchiveProcessTransaction, int totalIncompleteDocTrans,
                                    int totalIncompleteProcess) throws Exception;
  
  /**
   * Update the GT restore status
   * @param archiveType
   * @param isRestoreSuccess
   * @param archiveID
   * @throws Exception
   */
  public void updateGTRestoreStatus(String archiveType, Boolean isRestoreSuccess,String archiveID) throws Exception;
  
  /**
   * Update the TM restore status
   * @param isRestoreSuccess
   * @param summaryFilename
   * @throws Exception
   */
  public void updateTMRestoreStatus(boolean isRestoreSuccess, String summaryFilename) throws Exception;
  
  /**
   * Get the available group in TM
   * @return
   */
  public List<String> getAllAvailableGroup();
}
