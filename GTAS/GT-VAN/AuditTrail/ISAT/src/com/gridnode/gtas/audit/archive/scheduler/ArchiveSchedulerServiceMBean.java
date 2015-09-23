/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveSchedulerServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * This MBean provides the services for invoking the ArchiveScheduler to start their 
 * own archive task.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public interface ArchiveSchedulerServiceMBean extends ServiceMBean
{
  /**
   * set the status of processing the archive scheduler
   * @param isProcessing
   */
  void setProcessing(boolean isProcessing);
  
  /**
   * Determine whether the current MBean is processing the archive scheduler
   * @return true if the MBean is processing the archive scheduler. False, otherwise
   */
  boolean isProcessing();
  
  /**
   * While picking up the scheduler task for processing, to ensure
   * the task has been executed in time, we are allowed to fetch the task in advance by
   * setting the preload time    
   * @param seconds
   */
  void setPreloadTime(int preloadInSecond);
  
  /**
   * Return back the preload time
   * @return
   */
  int getPreloadTime();
  
  /**
   * Retrieve the ArchiveScheduler record based on the timeOfCall, and perform the archive services
   * @param timeOfCall the time this method is invoked
   */
  void invokeArchiveScheduler(Date timeOfCall);
  
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
  
/**
   * Get the last activate time of the MBean. 
   * @return
   */
  Date getLastActivateTime();
  
  /**
   * Get the last processed time of the MBean. It means the mbean has finished processing
   * the ArchiveSchedule record. Or the exit time if
   * there is another MBean instance still processing. 
   * @return
   */
  Date getLastEndProcessedTime();
}
