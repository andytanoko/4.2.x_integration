/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: ScheduleHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 15, 2011   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.time.handler;

import com.gridnode.pdip.base.time.helper.ITime;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

/**
 * @author Tam Wei Xiang
 * @version GT4.1.4.8
 * @since GT4.1.4.8
 */
public class TimerTaskHandler
{
  private Configuration _timeConfig = ConfigurationManager.getInstance().getConfig(ITime.TIME_CONFIG);
  
  public TimerTaskHandler()
  {
    
  }
  
  /**
   * Whether to perform active check on the scheduled task that is missed previosly
   * @return
   */
  public boolean isCheckOnInvalidTimerTask()
  {
    return _timeConfig.getBoolean(ITime.IS_CHECK_ON_MISSED_TASK, true);
  }
  
  /**
   * Get the interval to determine whether a scheduled task is considered as 
   * missed executed task.
   * @return
   */
  public int getMissedScheduledTaskInterval()
  {
    return _timeConfig.getInt(ITime.MISSED_SCHEDULED_TASK_INTERVAL, 300);
  }
}
