/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimerTasksLoaderServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 1, 2007    i00107              Created
 */

package com.gridnode.pdip.base.time.services;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * @author i00107
 * This MBean provides the alarm timer loading service for GridTalk.
 */
public interface TimerTasksLoaderServiceMBean extends ServiceMBean
{
  /**
   * Load the alarm tasks due to perform between the specified time of calls.
   * 
   * @param timeOfCall The time of call of this method. 
   * @param nextTimeOfCall The estimated time of next call to this method.
   */
  void loadTasks(Date timeOfCall, Date nextTimeOfCall);
  
  /**
   * Check if the initial tasks loaded i.e. missed tasks are triggered.
   * @return <b>true</b> if initial tasks are loaded, <b>false</b> otherwise.
   */
  boolean isInitialTasksLoaded();
  
  /**
   * Get the time when the tasks are last loaded.
   * @return The date/time when the tasks are last loaded.
   */
  Date getLastLoadTime();
  
  /**
   * Set the time whereby alarm tasks due within the specified number of
   * seconds will be scheduled
   * @param seconds Alarm Tasks due within the number of seconds will be scheduled.
   */
  void setPreloadTime(int seconds);
  
  /**
   * Get the time whereby alarm tasks due within the specified number of seconds
   * will be scheduled.
   * @return
   */
  int getPreloadTime();
  
  /**
   * Set the JndiName for the TimeMgr in GridTalk.
   * @param timeMgrJndiName The JndiName for the TimeMgr in GridTalk.
   */
  void setTimeMgrJndiName(String timeMgrJndiName);
  
  /**
   * Get JndiName for the TimeMgr in GridTalk.
   * @return
   */
  String getTimeMgrJndiName();

  /**
   * Set the Jndi properties for lookup of TimeMgr.
   * @param jndiProps The Jndi properties to use for lookup of TimeMgr
   */
  void setJndiProperties(Properties jndiProps);
  
  /**
   * Get the Jndi properties for lookup of TimeMgr.
   * @return
   */
  Properties getJndiProperties();
  
}
