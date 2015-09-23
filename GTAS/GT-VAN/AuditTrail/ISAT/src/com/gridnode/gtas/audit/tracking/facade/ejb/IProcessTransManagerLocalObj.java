/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessTransManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import javax.ejb.EJBLocalObject;

import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IProcessTransManagerLocalObj extends EJBLocalObject
{
  /**
   * Perform creation of entity Process Transaction
   * @param processTrans
   */
  public void persistProcessTrans(ProcessTransaction processTrans);
  
  /**
   * Perform update on Process Transaction given ProcessInfo
   * @param processInfo the process that emitted from OTC-Plug-in
   * @throws AuditTrailTrackingException
   */
  public boolean updateProcessTrans(ProcessInfo processInfo) throws AuditTrailTrackingException;
  
  /**
   * Perform update on Process Transaction given ProcessSummary, DocInfo
   * @param summary The ProcessSummary includes the info Pip name, Pip version, customer info, partner info etc
   * @param docInfo The document trans that emittend from OTC-plug-in
   * @param groupName the group that the docInfo correspond ProcessTransaction belong to.
   * @exception AuditTrailTrackingException throw if we found to locate the ProcessTransaction
   */
  public boolean updateProcessTrans(ProcessSummary summary, DocInfo docInfo, String groupName) throws AuditTrailTrackingException;
}
