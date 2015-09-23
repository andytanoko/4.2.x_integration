/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingListener.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Sep 15, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.housekeeping.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.housekeeping.HousekeepingService;
import com.gridnode.gtas.server.housekeeping.helpers.Logger;

public class HousekeepingListener implements  MessageListener
{
  public void onMessage(Message msg)
  {
    Logger.log("[HousekeepingListener.onMessage] Enter");
    try
    {
      HousekeepingService hkService=HousekeepingService.getInstance();
      hkService.doHousekeeping();
    }
    catch(Throwable th)
    {
      Logger.error(ILogErrorCodes.GT_HOUSEKEEPING_LISTENER,
                   "[HousekeepingListener.onMessage] Error: "+th.getMessage(),th);
    }
  }
}
