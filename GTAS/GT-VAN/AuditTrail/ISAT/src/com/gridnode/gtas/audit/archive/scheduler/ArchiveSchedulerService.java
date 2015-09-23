/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveSchedulerService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerHome;
import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerObj;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveSchedulerService extends ServiceMBeanSupport 
          implements ArchiveSchedulerServiceMBean
{
  private static final String CLASS_NAME = "ArchiveSchedulerService";
  private Logger _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  private boolean _isProcessing;
  private int _preloadTime;
  private Properties _jndiProperties = null;
  private Date _lastActivateTime = null;
  private Date _lastEndProcessedTime = null;
  
  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.scheduler.ArchiveSchedulerServiceMBean#setProcessing(boolean)
   */
  public void setProcessing(boolean isProcessing)
  {
    _isProcessing = isProcessing;
  }

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.scheduler.ArchiveSchedulerServiceMBean#isProcessing()
   */
  public boolean isProcessing()
  {
    return _isProcessing;
  }

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.scheduler.ArchiveSchedulerServiceMBean#setPreloadTime(int)
   */
  public void setPreloadTime(int preloadInSecond)
  {
    if(preloadInSecond < 0)
    {
      throw new IllegalArgumentException("Preload time in sec must be greater than or equal to 1 sec");
    }
    _preloadTime = preloadInSecond;
  }

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.scheduler.ArchiveSchedulerServiceMBean#getPreloadTime()
   */
  public int getPreloadTime()
  {
    return _preloadTime;
  }

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.scheduler.ArchiveSchedulerServiceMBean#invokeArchiveScheduler()
   */
  public void invokeArchiveScheduler(Date timeOfCall)
  {
    String method = "invokeArchiveScheduler";
    _logger.logMessage(method, null, "invoking archive scheduler");
    setLastActivateTime(new Date());
    
    if(isProcessing())
    {
      _logger.logMessage(method, null, CLASS_NAME+". The operation for invoking ArchiveScheduler is still active");
    }
    else
    {
      setProcessing(true);
      
      
      Date endDateRange = getEndDateRange(timeOfCall, getPreloadTime());
      _logger.logMessage(method, null, "Time of Call: "+timeOfCall+" endDateRange: "+endDateRange+" preloadTime: "+getPreloadTime());
      
      try
      {
        getArchiveSchedulerMgr().loadOutstandingTask(timeOfCall, endDateRange);
      }
      catch(Exception ex)
      {
        _logger.logError(ILogErrorCodes.AT_ARCHIVE_SCHEDULER_TASK_KICK_START_ERROR, method, null, "Error in loading the outstanding scheduler for executing its archive task", ex);
      }
      
      
      setProcessing(false);
    }
    
    setLastEndProcssedTime(new Date());
    _logger.logMessage(method, null, "Invoked Archive Scheduler Completed");
  }
  
  private IArchiveSchedulerObj getArchiveSchedulerMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(getJndiProperties());
    IArchiveSchedulerHome home = (IArchiveSchedulerHome)finder.lookup(IArchiveSchedulerHome.class.getName(), IArchiveSchedulerHome.class);
    return home.create();
  }
  
  /**
   * To ensure that all the archive scheduler get invoked in time given their next run time,
   * we will preload those scheduler in advance. Thus the date range will be expanded by given the preloadTime.
   * @param currentDate
   * @param preloadTime
   * @return
   */
  private Date getEndDateRange(Date currentDate, int preloadTime)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(currentDate);
    c.set(Calendar.SECOND, c.get(Calendar.SECOND) + preloadTime);
    return c.getTime();
  }
  
  public Properties getJndiProperties()
  {
    return _jndiProperties;
  }

  public void setJndiProperties(Properties pro)
  {
    _jndiProperties = pro;
  }
  
  /**
   * Get the last activate time of the MBean. 
   * @return
   */
  public Date getLastActivateTime()
  {
    return _lastActivateTime;
  }
  
  private void setLastActivateTime(Date activateTime)
  {
    _lastActivateTime = activateTime;
  }
  
  /**
   * Get the last processed time of the MBean. It means the mbean has finished processing
   * the ArchiveSchedule record. Or the exit time if
   * there is another MBean instance still processing. 
   * @return
   */
  public Date getLastEndProcessedTime()
  {
    return _lastEndProcessedTime;
  }
  
  private void setLastEndProcssedTime(Date lastProcessedTime)
  {
    _lastEndProcessedTime = lastProcessedTime;
  }
}
