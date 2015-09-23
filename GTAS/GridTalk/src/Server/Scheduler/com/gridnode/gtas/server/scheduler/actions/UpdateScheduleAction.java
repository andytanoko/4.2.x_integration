/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateScheduleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2004    Koh Han Sing        Created
 * Feb 18 2004    Neo Sok Lay         Factored out functionalities to ActionHelper
 */
package com.gridnode.gtas.server.scheduler.actions;

import com.gridnode.gtas.events.scheduler.UpdateScheduleEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the update of a Schedule(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class UpdateScheduleAction extends AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7964979749319330226L;

	private iCalAlarm _alarm;

  public static final String ACTION_NAME = "UpdateScheduleAction";

  protected Class getExpectedEventClass()
  {
    return UpdateScheduleEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertScheduleToMap((Schedule)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateScheduleEvent updEvent = (UpdateScheduleEvent) event;
    _alarm = ActionHelper.getManager().getAlarm(updEvent.getTaskUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateScheduleEvent updEvent = (UpdateScheduleEvent) event;

    Schedule schedule = new Schedule();
    schedule.setKey(updEvent.getTaskUid());
    schedule.setDaysOfWeek(updEvent.getDayOfWeek());
    schedule.setDelayPeriod(updEvent.getInterval());
    schedule.setDisabled(updEvent.isDisable());
    schedule.setFrequency(updEvent.getFrequency());
    schedule.setStartDate(updEvent.getStartDate());
    schedule.setStartTime(updEvent.getStartTime());
    schedule.setTaskId(updEvent.getTaskId());
    schedule.setTaskType(updEvent.getTaskType());
    schedule.setTimesToRun(updEvent.getCount());
    schedule.setWeekOfMonth(updEvent.getWeekOfMonth());

    return schedule;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    Schedule schedule = (Schedule)entity;
    
    ActionHelper.updateAlarmFromSchedule(schedule, _alarm);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    iCalAlarm alarm = ActionHelper.getManager().getAlarm(key);
    Schedule schedule = ActionHelper.convertiCalAlarmToSchedule((iCalAlarm)alarm);
    return schedule;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateScheduleEvent updEvent = (UpdateScheduleEvent) event;
    return new Object[] {
      Schedule.ENTITY_NAME,
      String.valueOf(updEvent.getTaskUid())};
  }

}
