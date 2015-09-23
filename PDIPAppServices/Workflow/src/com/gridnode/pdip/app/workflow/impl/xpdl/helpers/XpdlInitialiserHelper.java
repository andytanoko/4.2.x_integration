/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpdlInitialiserHelper.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 29, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.workflow.impl.xpdl.helpers;

import java.util.Timer;
import java.util.TimerTask;

import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
 
public class XpdlInitialiserHelper
{
  private static final Timer xpdlCacheTimeoutTimer = new Timer(true);
  private static boolean isTimeoutTimerSet=false;
  
  public static void initializeXpdl()
  {
    setXpdlCacheTimeout();
  }
  

  private static void setXpdlCacheTimeout()
  {
    Logger.log("[XpdlInitialiserHelper.setXpdlCacheTimeout] Enter , isTimeoutTimerSet="+isTimeoutTimerSet); 
    try
    {
      if(isTimeoutTimerSet)
      {
        Logger.log("[XpdlInitialiserHelper.setXpdlCacheTimeout] XpdlCacheTimeoutTimer already set");
        return;
      }
      
      String str = WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_XPDL_CACHE_TIMEOUT);
      if(str!=null && str.trim().length()>0)
      {
        long timeout = Long.parseLong(str);
        if(timeout>0)
        {
          timeout= timeout * 60 * 1000; // min to millis
          xpdlCacheTimeoutTimer.schedule(new XpdlCacheTimeoutTask(timeout),timeout,timeout);
          isTimeoutTimerSet=true;
        }
      }
    }
    catch(Throwable th)
    {
      Logger.error(ILogErrorCodes.WORKFLOW_INITIALISE_XPDL,
                   "[XpdlInitialiserHelper.setXpdlCacheTimeout] Error: "+th.getMessage(),th);
    }
  }
}

class XpdlCacheTimeoutTask extends TimerTask
{
  long _cacheTimeout;
    
  public XpdlCacheTimeoutTask(long cacheTimeout)
  {
    _cacheTimeout=cacheTimeout;
  }
    
  public void run()
  {
    try
    {
      XpdlDefinitionHandler.checkAndUnloadDefinition(_cacheTimeout);
    }
    catch(Throwable th)
    {
      Logger.error(ILogErrorCodes.WORKFLOW_RUN_TIMEOUT_TASK,
                   "[XpdlCacheTimeoutTask.run] Error while running task: "+th.getMessage(),th);
    }
  }
}
