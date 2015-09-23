/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetScheduleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2004    Koh Han Sing        Created
 * Feb 18 2004    Neo Sok Lay         Factored out functionalities to ActionHelper.
 */
package com.gridnode.gtas.server.scheduler.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.scheduler.GetScheduleEvent;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.model.Schedule;

import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Schedule(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class GetScheduleAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4713024352027939912L;
	public static final String ACTION_NAME = "GetScheduleAction";

  protected Class getExpectedEventClass()
  {
    return GetScheduleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetScheduleEvent getEvent = (GetScheduleEvent)event;
    iCalAlarm alarm = ActionHelper.getManager().getAlarm(getEvent.getTaskUID());
    
    return ActionHelper.convertiCalAlarmToSchedule(alarm);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertScheduleToMap((Schedule)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetScheduleEvent getEvent = (GetScheduleEvent)event;
    return new Object[]
           {
             Schedule.ENTITY_NAME,
             getEvent.getTaskUID()
           };
  }
}
