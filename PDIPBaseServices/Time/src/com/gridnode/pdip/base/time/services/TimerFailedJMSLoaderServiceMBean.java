/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimerFailedJMSLoaderServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 15, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.time.services;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface TimerFailedJMSLoaderServiceMBean extends ServiceMBean
{
  void loadFailedJMS();
  
  /**
   * Get the time when the tasks are last loaded.
   * @return The date/time when the tasks are last loaded.
   */
  Date getLastLoadTime();
  
  void setJndiProperties(Properties jndiProps);
  
  Properties getJndiProperties();
  
  void setNumRecordFetch(Integer numRecordFetch);
  
  Integer getNumRecordFetch();
  
  boolean isFailedJMSProcessing();
  
  Integer getMaxRetry();
  
  void setMaxRetry(Integer maxRetry);
}
