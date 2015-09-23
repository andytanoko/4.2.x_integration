/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimerFailedJMSLoaderService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 15, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.time.services;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.system.ServiceMBeanSupport;

import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.notification.ProcessFailedJMSNotification;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class TimerFailedJMSLoaderService extends ServiceMBeanSupport 
                implements TimerFailedJMSLoaderServiceMBean
{
  private static Log _log = LogFactory.getLog(TimerFailedJMSLoaderService.class.getName());
  private Date _lastLoadTime;
  private boolean _isProcessing;
  private Properties _failedJmsHandlerJndi;
  private Integer _numRecordFetched;
  private Integer _maxRetry = 100;
  
  /* (non-Javadoc)
   * @see com.gridnode.pdip.base.time.services.TimerFailedJMSLoaderServiceMBean#getLastLoadTime()
   */
  public Date getLastLoadTime()
  {
    return _lastLoadTime;
  }

  /* (non-Javadoc)
   * @see com.gridnode.pdip.base.time.services.TimerFailedJMSLoaderServiceMBean#loadFailedJMS()
   */
  public void loadFailedJMS()
  {
    _log.debug("Entering timer failed jms loader");
    
    setLastLoadTime();
    if(isFailedJMSProcessing())
    {
      _log.debug("Outstanding failed jms is being processing. Wait for next round.");
      return;
    }
    broadCastJMSRetryNotification(getMaxRetry(), getNumRecordFetch());
    setFailedJMSProcessing(false);
  }
  
  private void broadCastJMSRetryNotification(int maxRetry, int numRetrieve)
  {
    ProcessFailedJMSNotification notification = null;
    _log.debug("Broadcasting ProcessFailedJMSNotification: with maxRetry: "+maxRetry+" numRetrieve: "+numRetrieve);
    try
    {
      notification = new ProcessFailedJMSNotification(numRetrieve, maxRetry);
      Notifier.getInstance().broadcast(notification);
    }
    catch (Exception ex) 
    {
      _log.error("Error in broadcasting the ProcessFailedJMSNotification:"+notification, ex);
    }
  }
  
  private void setLastLoadTime()
  {
    _lastLoadTime = new Date();
  }
  
  public void setJndiProperties(Properties jndiProps)
  {
    _failedJmsHandlerJndi = jndiProps;
  }
  
  public Properties getJndiProperties()
  {
    return _failedJmsHandlerJndi;
  }
  
  public void setNumRecordFetch(Integer numRecordFetch)
  {
    _numRecordFetched = numRecordFetch;
  }
  
  public Integer getNumRecordFetch()
  {
    return _numRecordFetched;
  }
  
  public boolean isFailedJMSProcessing()
  {
    return _isProcessing;
  }
  
  private void setFailedJMSProcessing(boolean isProcessing)
  {
    _isProcessing = isProcessing;
  }
  
  public Integer getMaxRetry()
  {
    return _maxRetry;
  }
  
  public void setMaxRetry(Integer maxRetry)
  {
    _maxRetry = maxRetry;
  }
}
