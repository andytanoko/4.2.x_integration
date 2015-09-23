/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExecuteUserProcedureMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 11 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.userprocedure.listener.ejb;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.gtas.server.userprocedure.helpers.UserProcedureDelegate;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
 
/**
 * Listener for alarm for time to check for license expiry and node lock.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class ExecuteUserProcedureMDBean extends TimeInvokeMDBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8411891123022884777L;

	protected void invoke(AlarmInfo info)
  {
    try
    {
      Logger.log("[ExecuteUserProcedureMDB.invoke] Enter");

      String userProcedureName = info.getTaskId();

      UserProcedureDelegate.execute(userProcedureName);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_EXECUTE_USER_PROC_MDB,
                   "[ExecuteUserProcedureMDB.invoke] "+ex.getMessage(), ex);
    }
    finally
    {
      Logger.log("[ExecuteUserProcedureMDB.invoke] End");
    }
  }

}
