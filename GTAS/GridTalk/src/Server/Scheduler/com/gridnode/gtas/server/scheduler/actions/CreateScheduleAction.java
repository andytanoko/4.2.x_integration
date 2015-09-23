/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateScheduleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 * Feb 18 2004    Neo Sok Lay         Factored out functionalities to ActionHelper
 */
package com.gridnode.gtas.server.scheduler.actions;

import com.gridnode.gtas.events.scheduler.CreateScheduleEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the creation of a new Scheduler(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class CreateScheduleAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1541399487834221556L;
	public static final String ACTION_NAME = "CreateScheduleAction";

  protected Class getExpectedEventClass()
  {
    return CreateScheduleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    iCalAlarm alarm = ActionHelper.getManager().getAlarm(key);
    Schedule schedule = ActionHelper.convertiCalAlarmToSchedule((iCalAlarm)alarm);
    return schedule;
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateScheduleEvent createEvent = (CreateScheduleEvent)event;
    
    Schedule schedule = new Schedule();
    schedule.setDaysOfWeek(createEvent.getDayOfWeek());
    schedule.setDelayPeriod(createEvent.getInterval());
    schedule.setDisabled(createEvent.isDisable());
    schedule.setFrequency(createEvent.getFrequency());
    schedule.setStartDate(createEvent.getStartDate());
    schedule.setStartTime(createEvent.getStartTime());
    schedule.setTaskId(createEvent.getTaskId());
    schedule.setTaskType(createEvent.getTaskType());
    schedule.setTimesToRun(createEvent.getCount());
    schedule.setWeekOfMonth(createEvent.getWeekOfMonth());
    
    return schedule;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateScheduleEvent createEvent = (CreateScheduleEvent)event;
    return new Object[]
           {
             Schedule.ENTITY_NAME,
             createEvent.getTaskType(),
             createEvent.getTaskId(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    Schedule schedule = (Schedule)entity;
    
    iCalAlarm alarm = ActionHelper.createAlarmFromSchedule(schedule);
    return new Long(alarm.getUId());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertScheduleToMap((Schedule)entity);
  }


}
