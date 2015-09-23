/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveServiceManagerLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 4, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.util.Hashtable;

import javax.ejb.EJBLocalObject;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IArchiveServiceManagerLocalObj extends EJBLocalObject
{
  /**
   * Invoke the correspond archive handler to distribute the archive task to
   * the nodes available in the cluster.
   * @param criteria the archive criteria as specified by the user
   * @param isEnabledMultiNode indicate the archive task is distributed within the nodes in the cluster
   * @return the archiveID which uniquely identified a particular archival action.
   * @throws ArchiveTrailDataException
   */
  public String archive(Hashtable criteria, boolean isEnabledMultiNode) throws ArchiveTrailDataException;
  
  /**
   * Check the archive status for a particular archive tasks as identified by the archiveID.
   * @param archiveID a unique identifier that identifiy a particular archive task.
   * @return true if all the sub task has been completed by the nodes. False if the archive sub task
   *         still processing by the nodes.
   * @throws ArchiveTrailDataException throws if the archive task list can't be located or processed successfully.
   */
  public boolean checkArchiveStatus(String archiveID) throws ArchiveTrailDataException;
  
  /**
   * Delete away the temp properties generated so far. They include the archive properties which keep track
   * the status of the sub-archival task distributed to a particular nodes. The other one is the jobProperties
   * which record down the status of the archive actions(archive TM record, archive by Process, archive by doc) 
   * in a particular nodes.
   * @param archiveID
   * @throws Exception
   */
  public void deleteTempArchiveProps(String archiveID) throws Exception;
  
  /**
   * Update the archive status which includes archive task status/ job properties
   * @param archiveID
   * @param jobID
   * @param archiveType
   * @param isArchiveSuccess
   * @param summaryFilename
   * @throws ArchiveTrailDataException
   */
  public void updateArchiveStatus(String archiveID, String jobID, String archiveType, boolean isArchiveSuccess, String summaryFilename) throws ArchiveTrailDataException;
  
  /**
   * Init the archive process. Appropriate handler will be invoked either supporting the cluster or single node. 
   * @param criteria
   * @throws ArchiveTrailDataException
   */
  public void initArchive(Hashtable criteria) throws ArchiveTrailDataException;
}
