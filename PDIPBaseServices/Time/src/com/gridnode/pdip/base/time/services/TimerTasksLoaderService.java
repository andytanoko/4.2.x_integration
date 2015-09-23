/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimerTasksLoaderService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 1, 2007    i00107              Created
 * Apr 19,2008    Tam Wei Xiang       #21: Ensure that there is no delay in loading 
 *                                         the scheduled task. Load scheduled task
 *                                         in advance is required.
 * Feb 16,2011    Tam Wei Xiang       #2115 - Actively check on missed executed
 *                                            scheduled task.
 */

package com.gridnode.pdip.base.time.services;

import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.time.handler.TimerTaskHandler;

/**
 * @author i00107
 * Implementation of TimerTasksLoaderServiceMBean.
 */
public class TimerTasksLoaderService 
  extends ServiceMBeanSupport 
  implements  TimerTasksLoaderServiceMBean
{
  private static Log _log = LogFactory.getLog(TimerTasksLoaderService.class.getName());
  private int _preloadTime = 5; //default to 5 seconds
  private Date _lastLoadTime;
  private String _timeMgrJndiName;
  private Properties _jndiProperties;
  private TimerTaskHandler _helper;
  
  /**
   * @see com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#loadTasks(java.util.Date,java.util.Date)
   */
  public void loadTasks(Date timeOfCall, Date nextTimeOfCall)
  {
    try
    {
      _log.debug("loadTasks() invoked at "+timeOfCall);
      
      if (_lastLoadTime != null)
      {
        //#21: TWX load tasks in advance
        Date curLoadTime = new Date(timeOfCall.getTime() + _preloadTime);
        getTimeMgr().loadTasks(_lastLoadTime, curLoadTime);
        _lastLoadTime = curLoadTime;
        
        //TWX #2115 - pro-active check outdated task
        _log.debug("is check on invalidTimer task: "+getTimerTaskHelper().isCheckOnInvalidTimerTask()+", missed task interval: "+getTimerTaskHelper().getMissedScheduledTaskInterval());
        if(getTimerTaskHelper().isCheckOnInvalidTimerTask())
        {
          int missedScheduledInterval = getTimerTaskHelper().getMissedScheduledTaskInterval();
          Date retrievedMissedDate = new Date(timeOfCall.getTime() - missedScheduledInterval * 1000);
          getTimeMgr().loadMissedTasks(retrievedMissedDate, curLoadTime);
        }
        else
        {
          _log.debug("active check on invalid timer task is disabled");
        }
      }
      else
      {
        //load missed tasks
        //Date nextLoadTime = new Date(nextTimeOfCall.getTime() + _preloadTime);
        Date nextLoadTime = new Date(timeOfCall.getTime() + _preloadTime);
        getTimeMgr().loadMissedTasks(timeOfCall, nextLoadTime);
        
        //#21: TWX Pre-load the alarm. So that the alarm task will not get executed delay.
        getTimeMgr().loadTasks(timeOfCall, nextLoadTime);
        _lastLoadTime = nextLoadTime;
      }
      _log.debug("loadTasks() finished.");
    }
    catch (Throwable t)
    {
      _log.error("Error in loadTasks", t);
    }
  }

  /**
   * @see com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#isInitialTasksLoaded()
   */
  public boolean isInitialTasksLoaded()
  {
    return _lastLoadTime != null;
  }

  /**
   * @see com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#getLastLoadTime()
   */
  public Date getLastLoadTime()
  {
    return _lastLoadTime;
  }

  /**
   * @see org.jboss.system.ServiceMBean#getName()
   */
  public String getName()
  {
    return "TimerTasksLoaderService";
  }

  /**
   * @see com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#getPreloadTime()
   */
  public int getPreloadTime()
  {
    return _preloadTime;
  }

  /**
   * @see com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#setPreloadTime(int)
   */
  public void setPreloadTime(int seconds)
  {
    _preloadTime = seconds;
  }
  
  
  private IiCalTimeMgrObj getTimeMgr() throws Exception
  {
    InitialContext ctx = new InitialContext(getJndiProperties());
    IiCalTimeMgrHome home = (IiCalTimeMgrHome)ctx.lookup(getTimeMgrJndiName());
    return home.create();
  }

  private TimerTaskHandler getTimerTaskHelper()
  {
    if(_helper == null)
    {
      _helper = new TimerTaskHandler();
    }
    return _helper;
  }
  
  /**
   * @see  com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#getJndiProperties()
   */
  public Properties getJndiProperties()
  {
    return _jndiProperties;
  }

  /**
   * @see  com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#setJndiProperties(Properties)
   */
  public void setJndiProperties(Properties jndiProperties)
  {
    _jndiProperties = jndiProperties;
  }

  /**
   * @see  com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#getTimeMgrJndiName()
   */
  public String getTimeMgrJndiName()
  {
    return _timeMgrJndiName;
  }

  /**
   * @see  com.gridnode.pdip.base.time.services.TimerTasksLoaderServiceMBean#setTimeMgrJndiName(String)
   */
  public void setTimeMgrJndiName(String timeMgrJndiName)
  {
    _timeMgrJndiName = timeMgrJndiName;
  }

}
