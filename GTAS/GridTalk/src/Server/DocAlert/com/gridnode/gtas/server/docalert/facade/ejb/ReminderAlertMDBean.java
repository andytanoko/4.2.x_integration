/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 26 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import com.gridnode.gtas.server.docalert.helpers.AlarmScheduler;
import com.gridnode.gtas.server.docalert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.docalert.helpers.Logger;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;

import com.gridnode.pdip.framework.log.FacadeLogger;

import java.util.Date;

/**
 * This MDBean is invoked when the alarm for sending reminder email is due for
 * a certain previously sent document.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ReminderAlertMDBean extends TimeInvokeMDBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6215459339344135052L;

	protected void invoke(AlarmInfo info)
  {
    FacadeLogger logger = Logger.getReminderFacadeLogger();
    String methodName   = "invoke";
    Object[] params     = new Object[] {info};


    try
    {
      logger.logEntry(methodName, params);

      Long alarmUID = new Long(info.getAlarmUid());
      Date dueDate = AlarmScheduler.findAlarm(alarmUID).getStartDt();
      ServiceLookupHelper.getDocAlertMgr().timeForReminder(
        new Long(info.getAlarmUid()), dueDate);
    }
    catch (Throwable t)
    {
      logger.logMessage(methodName, params, t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

}