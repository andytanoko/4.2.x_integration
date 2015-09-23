/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KeepAliveTimerMDBeanBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.timer.ejb;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * Listener for alarm for time to send Keep Alive signal to the GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class KeepAliveTimerMDBean extends TimeInvokeMDBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6387233339303614997L;

	protected void invoke(AlarmInfo info)
  {
    FacadeLogger logger = Logger.getTimerFacadeLogger();
    String methodName   = "invoke";
    Object[] params     = new Object[] {info};

    try
    {
      logger.logEntry(methodName, params);

      ServiceLookupHelper.getConnectionService().sendKeepAlive();
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

}