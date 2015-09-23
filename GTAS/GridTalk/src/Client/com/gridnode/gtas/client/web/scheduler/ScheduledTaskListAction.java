/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduledTaskListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-02-11     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.web.scheduler;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.GTClientException;

public class ScheduledTaskListAction extends EntityListAction
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Object[] columns = {  IGTScheduledTaskEntity.TYPE,
                          IGTScheduledTaskEntity.TASK_ID,
                          IGTScheduledTaskEntity.IS_DISABLED,
                          IGTScheduledTaskEntity.FREQUENCY,
                          IGTScheduledTaskEntity.START_DATE,
                          IGTScheduledTaskEntity.RUN_TIME,
                       };
    return columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_SCHEDULED_TASK;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "scheduledTask";
  }
}
