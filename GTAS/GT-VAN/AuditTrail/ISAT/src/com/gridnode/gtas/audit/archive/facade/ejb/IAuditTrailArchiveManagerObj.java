/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailArchiveManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 27, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IAuditTrailArchiveManagerObj extends EJBObject
{
  /**
   * Retrieve all the TM summary filename, and the file last modified date
   * @return
   */
  public Collection<String[]> getAllArchiveSummaryFile() throws RemoteException;
  
  /**
   * Get the TM summary file in byte[] format given the summary filename
   * @param summaryFilename the name of the archive summary file.
   * @return
   * @throws Exception 
   */
  public byte[] getTMSummaryFileContent(String summaryFilename) throws Exception, RemoteException;  
  
  public String getArchiveSummaryRenderingUrl() throws RemoteException;
  
  /**
   * Get the URL to forward the archive/restore status to
   * @throws RemoteException
   */
  public String getArchiveStatusForwardUrl() throws RemoteException;
  
  /**
   * Return the working dir for storing the arhive files.
   * @return
   */
  public String getArchiveFolderPath() throws RemoteException;
}
