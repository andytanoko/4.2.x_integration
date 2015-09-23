/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2003      Mahesh         Created
 */ 
package com.gridnode.gtas.server.housekeeping.listener.ejb;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.housekeeping.HousekeepingService;
import com.gridnode.gtas.server.housekeeping.helpers.Logger;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;

public class HousekeepingMDBean extends TimeInvokeMDBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1277668978383632499L;

	protected void invoke(AlarmInfo info)
  {
    try
    {
      Logger.log("[HousekeepingMDBean.invoke] Enter");
      HousekeepingService hkService=HousekeepingService.getInstance();
      hkService.doHousekeeping();
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_HOUSEKEEPING_MDB,
                   "[HousekeepingMDBean.invoke] "+ex.getMessage(), ex);
    }
    finally
    {
      Logger.log("[HousekeepingMDBean.invoke] End");
    }
  }
}
