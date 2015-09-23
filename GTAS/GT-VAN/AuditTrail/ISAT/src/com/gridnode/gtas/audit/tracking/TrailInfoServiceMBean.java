/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TrailInfoServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 8, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * This MBean provides the services for starting the operation for processing
 * the AuditTrail data that were emitted from GT.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public interface TrailInfoServiceMBean extends ServiceMBean
{
  /**
   * set the status of processing the AuditTrail data.
   * @param isProcessing
   */
  void setProcessing(boolean isProcessing);
  
  /**
   * Determine whether the current MBean is processing the AuditTrailData
   * @return true if the MBean is processing the audit trail data. False, otherwise
   */
  boolean isProcessing();
  
  /**
   * Process the AuditTrailData we store in DB.
   *
   */
  void processAuditTrailData();
  
  
  /**
   * Get the total attempt count for reprocess the AuditTrailData we persist
   */
  int getAttemptCount();
  
  /**
   * Set the attempt count for reprocess the AuditTrailData
   * @param attempCount
   */
  void setAttemptCount(int attempCount);
  
  /**
   * Get the total number of record be processed per invocation
   * @return
   */
  int getTotalRecordForProcessPerCall();
  
  /**
   * Get the last activate time of the MBean. 
   * @return
   */
  Date getLastActivateTime();
  
  /**
   * Get the last processed time of the MBean. It means the mbean has finished processing
   * the outstanding audit trail data record for the current batch. Or the exit time if
   * there is another MBean instance still processing. 
   * @return
   */
  Date getLastEndProcessedTime();
  
  /**
   * Set the total number of record be processed per invocation
   * @param totalRecordForProcessPerCall
   */
  void setTotalRecordForProcessPerCall(int totalRecordForProcessPerCall);
  
  /**
   * Get the JNDI properties for the MBean to look up the SessionBean 
   * @return
   */
  Properties getJndiProperties();
  
  /**
   * Set the JNDI properties for the MBean to perform the JNDI lookup
   * @param pro
   */
  void setJndiProperties(Properties pro);
}
