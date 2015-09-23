/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultScheduledTaskManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-02-11     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FakeEnumeratedConstraint;
import com.gridnode.gtas.client.web.strutsbase.FakeSingleRangeConstraint;
import com.gridnode.gtas.events.scheduler.*;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.*;

class DefaultScheduledTaskManager
  extends DefaultAbstractManager
  implements IGTScheduledTaskManager
{
  private static final Collection TASK_TYPES =
    StaticUtils.arrayListValue(
      new Object[] {
        IGTScheduledTaskEntity.TYPE_CHECK_LICENSE,
        IGTScheduledTaskEntity.TYPE_USER_PROCEDURE,
        IGTScheduledTaskEntity.TYPE_HOUSEKEEPING_INFO,
        IGTScheduledTaskEntity.TYPE_DB_ARCHIVE});
        
  private static final com.gridnode.pdip.framework.db.filter.IDataFilter LIST_FILTER =
    new com.gridnode.pdip.framework.db.filter.DataFilterImpl();
  static {
    LIST_FILTER.addDomainFilter(
      null,
      IGTScheduledTaskEntity.TYPE,
      TASK_TYPES,
      false);
  }

  DefaultScheduledTaskManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_SCHEDULED_TASK, session);
  }

  protected void doUpdate(IGTEntity entity)
    throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) entity;
      UpdateScheduleEvent event = (UpdateScheduleEvent)constructEvent(scheduledTask, true);
      handleUpdateEvent(event, (AbstractGTEntity) entity);
    }
    catch (Exception e)
    {
      throw new GTClientException(
        "GTAS Error attempting to update ScheduledTask",
        e);
    }
  }

  protected void doCreate(IGTEntity entity)
    throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) entity;
      CreateScheduleEvent event = (CreateScheduleEvent)constructEvent(scheduledTask, false);
      handleCreateEvent(event, (AbstractGTEntity) entity);
    }
    catch (Exception e)
    {
   e.printStackTrace();
      throw new GTClientException(
        "GTAS Error attempting to update ScheduledTask",
        e);
    }
  }

  private IEvent constructEvent(IGTScheduledTaskEntity scheduledTask, boolean update)
    throws GTClientException
  {
    String type = scheduledTask.getFieldString(IGTScheduledTaskEntity.TYPE);
    String taskId =
      scheduledTask.getFieldString(IGTScheduledTaskEntity.TASK_ID);
    Boolean isDisabled =
      StaticUtils.booleanValue(
        scheduledTask.getFieldString(IGTScheduledTaskEntity.IS_DISABLED));
    Integer frequency =
      StaticUtils.integerValue(
        scheduledTask.getFieldString(IGTScheduledTaskEntity.FREQUENCY));
    Date startDate = (Date)scheduledTask.getFieldValue(IGTScheduledTaskEntity.START_DATE);         
    String runTime =
      scheduledTask.getFieldString(IGTScheduledTaskEntity.RUN_TIME);
    Integer count =
      StaticUtils.integerValue(
        scheduledTask.getFieldString(IGTScheduledTaskEntity.COUNT));
    Integer interval =
      StaticUtils.integerValue(
        scheduledTask.getFieldString(IGTScheduledTaskEntity.INTERVAL));
    Integer weekOfMonth =
      StaticUtils.integerValue(
        scheduledTask.getFieldString(IGTScheduledTaskEntity.WEEK_OF_MONTH));
    List daysOfWeek = (List)scheduledTask.getFieldValue(IGTScheduledTaskEntity.DAYS_OF_WEEK);

    if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_MONTHLY))
    {
      String monthOption =
        scheduledTask.getFieldString(IGTScheduledTaskEntity.MONTH_OPTION);
      if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_LASTDAY))
      {
        Integer reverseInterval = StaticUtils.integerValue(scheduledTask.getFieldString(
            IGTScheduledTaskEntity.REVERSE_INTERVAL));
        interval = new Integer(0 - reverseInterval.intValue());
        
        daysOfWeek  = null; // 20040924 DDJ: Return null to server if not needed
        weekOfMonth = null; // 20040924 DDJ: Return null to server if not needed
      }
      else if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_WEEK))
      {
        Integer dayOfWeek =
          StaticUtils.integerValue(
            scheduledTask.getFieldString(IGTScheduledTaskEntity.DAY_OF_WEEK));
        daysOfWeek = new ArrayList();
        daysOfWeek.add(dayOfWeek);
        
        interval    = null; // 20040924 DDJ: Return null to server if not needed
      }
      else if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_DAY))
      {
        daysOfWeek  = null; // 20040924 DDJ: Return null to server if not needed
        weekOfMonth = null; // 20040924 DDJ: Return null to server if not needed
      }
      else
      {
        throw new GTClientException("Invalid monthOption: " + monthOption);
      }
    }

    IEvent event = null;
    if (update)
    {
      Long uid = scheduledTask.getUidLong();
      
      event = new UpdateScheduleEvent(
                uid,
                type,
                taskId,
                isDisabled,
                frequency,
                startDate,
                runTime,
                count,
                interval,
                daysOfWeek,
                weekOfMonth);
    }
    else
    {
      event = new CreateScheduleEvent(
          type,
          taskId,
          isDisabled,
          frequency,
          startDate,
          runTime,
          count,
          interval,
          daysOfWeek,
          weekOfMonth);
    }
    
    return event;
  }
  
  protected int getManagerType()
  {
    return IGTManager.MANAGER_SCHEDULED_TASK;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SCHEDULED_TASK;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetScheduleEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(
    com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if (filter == null)
    {
      filter = LIST_FILTER;
    }
    return new GetScheduleListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(
    Collection uids)
    throws EventException
  {
    return new DeleteScheduleEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if (IGTEntity.ENTITY_SCHEDULED_TASK.equals(entityType))
    {
      return new DefaultScheduledTaskEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException(
        "Manager:"
          + this
          + " cannot create entity object of type "
          + entityType);
    }
  }

  /**
   * @see com.gridnode.gtas.client.ctrl.DefaultAbstractManager#defineVirtualFields(java.lang.String)
   */
  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[3];

    sharedVfmi[0] =
      new VirtualSharedFMI(
        "scheduledTask.monthOption",
        IGTScheduledTaskEntity.MONTH_OPTION);
    sharedVfmi[0].setConstraint(
      new FakeEnumeratedConstraint(
        new String[] {
          "scheduledTask.monthOption.day",
          "scheduledTask.monthOption.lastDay",
          "scheduledTask.monthOption.week" },
        new String[] {
          IGTScheduledTaskEntity.MONTH_OPTION_DAY,
          IGTScheduledTaskEntity.MONTH_OPTION_LASTDAY,
          IGTScheduledTaskEntity.MONTH_OPTION_WEEK }));
    sharedVfmi[0].setValueClass("java.lang.String");
    sharedVfmi[0].setMandatoryCreate(true);
    sharedVfmi[0].setMandatoryUpdate(true);

    sharedVfmi[1] =
      new VirtualSharedFMI(
        "scheduledTask.reverseInterval",
        IGTScheduledTaskEntity.REVERSE_INTERVAL);
    sharedVfmi[1].setConstraint(
      new FakeSingleRangeConstraint(
        new Integer(1),
        new Integer(Integer.MAX_VALUE)));
    sharedVfmi[1].setValueClass("java.lang.Integer");
    sharedVfmi[1].setMandatoryCreate(true);
    sharedVfmi[1].setMandatoryUpdate(true);

    sharedVfmi[2] =
      new VirtualSharedFMI(
        "scheduledTask.dayOfWeek",
        IGTScheduledTaskEntity.DAY_OF_WEEK);
    sharedVfmi[2].setConstraint(
      new FakeEnumeratedConstraint(
        new String[] {
          "scheduledTask.daysOfWeek.monday",
          "scheduledTask.daysOfWeek.tuesday",
          "scheduledTask.daysOfWeek.wednesday",
          "scheduledTask.daysOfWeek.thursday",
          "scheduledTask.daysOfWeek.friday",
          "scheduledTask.daysOfWeek.saturday",
          "scheduledTask.daysOfWeek.sunday" },
        new String[] {
          IGTScheduledTaskEntity.DAY_MONDAY.toString(),
          IGTScheduledTaskEntity.DAY_TUESDAY.toString(),
          IGTScheduledTaskEntity.DAY_WEDNESDAY.toString(),
          IGTScheduledTaskEntity.DAY_THURSDAY.toString(),
          IGTScheduledTaskEntity.DAY_FRIDAY.toString(),
          IGTScheduledTaskEntity.DAY_SATURDAY.toString(),
          IGTScheduledTaskEntity.DAY_SUNDAY.toString()}));
    sharedVfmi[2].setValueClass("java.lang.Integer");
    sharedVfmi[2].setMandatoryCreate(true);
    sharedVfmi[2].setMandatoryUpdate(true);
    return sharedVfmi;
  }

  /**
   * @see com.gridnode.gtas.client.ctrl.DefaultAbstractManager#initVirtualEntityFields(java.lang.String, com.gridnode.gtas.client.ctrl.AbstractGTEntity, java.util.Map)
   */
  void initVirtualEntityFields(
    String entityType,
    AbstractGTEntity entity,
    Map fieldMap)
    throws GTClientException
  {
    try
    {
      if (IGTEntity.ENTITY_SCHEDULED_TASK.equals(entityType))
      {
        DefaultScheduledTaskEntity scheduledTask =
          (DefaultScheduledTaskEntity) entity;
        Integer frequency =
          (Integer) fieldMap.get(IGTScheduledTaskEntity.FREQUENCY);

        if (frequency != null && frequency.equals(IGTScheduledTaskEntity.FREQUENCY_MONTHLY))
        {
          Integer intervalInt = (Integer)fieldMap.get(IGTScheduledTaskEntity.INTERVAL);
          
          int interval = intervalInt != null ? intervalInt.intValue() : 0;
          if (interval < 0)
          {
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.REVERSE_INTERVAL,
              new Integer(0 - interval));
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.INTERVAL,
              null);
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.MONTH_OPTION,
              IGTScheduledTaskEntity.MONTH_OPTION_LASTDAY);
          }
          else if (interval == 0)
          {
            List daysOfWeek =
              (List) fieldMap.get(IGTScheduledTaskEntity.DAYS_OF_WEEK);
            Integer dayOfWeek = (Integer) StaticUtils.getFirst(daysOfWeek);
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.DAY_OF_WEEK,
              dayOfWeek);
            daysOfWeek.clear();
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.DAYS_OF_WEEK,
              daysOfWeek);
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.MONTH_OPTION,
              IGTScheduledTaskEntity.MONTH_OPTION_WEEK);
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.REVERSE_INTERVAL,
              null);
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.INTERVAL,
              null);
          }
          else
          {
            scheduledTask.setNewFieldValue(
              IGTScheduledTaskEntity.MONTH_OPTION,
              IGTScheduledTaskEntity.MONTH_OPTION_DAY);
          }
        }
        
      }
    }
    catch (Throwable t)
    {
      throw new GTClientException(
        "Error initialising virtual entity fields for " + entity,
        t);
    }
  }
}