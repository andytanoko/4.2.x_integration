/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduleReportServiceMBean.java
 *
 ****************************************************************************
 * Date                Author              Changes
 ****************************************************************************
 * Feb  9, 2007        Regina Zeng          Created
 * Feb 26, 2007 	   Regina Zeng			Added deleteReport		
 */

package com.gridnode.gridtalk.genreport.mbean;

import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBean;

/**
 * @author Regina Zeng
 * This MBean provides the alarm timer loading service for scheduling or reports
 */
public interface ScheduleReportServiceMBean extends ServiceMBean
{
  /**
   * Auto deleting of report history in the database.
   */	
  void deleteReport();	
  /**
   * Base on the time of call, this method will generate the end range time.
   * And thus, generate report between the time of call and end range time.
   * @param timeOfCall The time of call of this method. 
   */
  void generateReport(Date timeOfCall);
  
  /**
   * Set the preload time within the specified number of minutes due.
   * @param minutes due to fix the end range time
   */
  void setPreloadTime(int seconds);
  
  /**
   * Retrieve the preload time in mintues.
   * @return
   */
  int getPreloadTime();
  
  /**
   * Set the JndiName for the Delivery manager.
   * @param jndiName The JndiName for the Delivery manager to handle the processing.
   */
  void setDeliveryMgrJndiName(String deliveryMgrJndiName);
  
  /**
   * Get the JndiName for the Delivery manager.
   * @return The JndiName for the Delivery manager for handling the processing.
   */
  String getDeliveryMgrJndiName();

  /**
   * Set the Jndi properties to use to lookup the Delivery manager.
   * @param props The Jndi properties to use to lookup the Delivery manager.
   */
  void setJndiProperties(Properties jndiProps);
  
  /**
   * Get the Jndi properties to use to lookup the Delivery manager.
   * @return The Jndi properties to use to lookup the Delivery manager.
   */
  Properties getJndiProperties();  
}