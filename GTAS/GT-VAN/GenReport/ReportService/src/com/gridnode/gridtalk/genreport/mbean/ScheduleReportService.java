/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduleReportService.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.mbean;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandler;
import com.gridnode.gridtalk.genreport.ejb.IGenScheduleHandlerHome;
import com.gridnode.gridtalk.genreport.util.IJndiNames;
import com.gridnode.util.jndi.JndiFinder;

/**
 * @author Regina Zeng
 * Implementation of ScheduleReportServiceMBean
 */
public class ScheduleReportService extends ServiceMBeanSupport implements ScheduleReportServiceMBean
{  
  private static Log _log = LogFactory.getLog(ScheduleReportService.class.getName());
  private int _preloadTime = 1; //default to 1 min
  private String _deliveryMgrJndiName;
  private Properties _jndiProperties;
  
  /**
   * @see com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#deleteReport()
   */
  public void deleteReport()
  {
    try
    {
      boolean status = (boolean)getScheduleHandler().deleteReport();
      if(status)
      {
    	getScheduleHandler().updateNextDeleteDate();
      }
    }
    catch(Throwable t)
    {
      _log.error("Error in deleting report.", t);
    }
  }
  
  /**
   * @see com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#generateReport(java.util.Date)
   */
  public void generateReport(Date timeOfCall)
  {
    //Date endRangeCall = DateUtil.processEndRangeDate(timeOfCall, _preloadTime);
    try
    {
      getScheduleHandler().generateReport(timeOfCall, _preloadTime); 
    }
    catch (Throwable t)
    {
      _log.error("Error in generating report.", t);
    }
  }
  
  /**
   * @see org.jboss.system.ServiceMBean#getName()
   */
  public String getName()
  {
    return "ScheduleReportService";
  }
  
  /**
   * @see com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#getPreloadTime()
   */
  public int getPreloadTime()
  {
    return _preloadTime;
  }
  
  /**
   * @see com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#setPreloadTime(int)
   */
  public void setPreloadTime(int minutes)
  {
    _preloadTime = minutes;
  }  
  
  private IGenScheduleHandler getScheduleHandler() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IGenScheduleHandlerHome home = (IGenScheduleHandlerHome)finder.lookup(IJndiNames.SCHEDULE_HANDLER, IGenScheduleHandlerHome.class);
    return home.create();
  }
  
  /**
   * @see  com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#getJndiProperties()
   */
  public Properties getJndiProperties()
  {
    return _jndiProperties;
  }
  
  /**
   * @see  com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#setJndiProperties(Properties)
   */
  public void setJndiProperties(Properties jndiProperties)
  {
    _jndiProperties = jndiProperties;
  }
  
  /**
   * @see  com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#getDeliveryMgrJndiName()
   */
  public String getDeliveryMgrJndiName()
  {
    return _deliveryMgrJndiName;
  }
  
  /**
   * @see  com.gridnode.gridtalk.genreport.mbean.ScheduleReportServiceMBean#setDeliveryMgrJndiName(String)
   */
  public void setDeliveryMgrJndiName(String deliveryMgrJndiName)
  {
    _deliveryMgrJndiName = deliveryMgrJndiName;
  }
}
