/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduledTaskDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-02-11     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.web.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTScheduledTaskEntity;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ScheduledTaskDispatchAction extends EntityDispatchAction2
{
  //private static final Log _log =
  //  LogFactory.getLog(ScheduledTaskDispatchAction.class);

  protected String getNavgroup(
    com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

    private static final Number[] _scheduledTaskFields =
  //normal single value fields
  {
    IGTScheduledTaskEntity.COUNT,
    IGTScheduledTaskEntity.FREQUENCY,
    IGTScheduledTaskEntity.INTERVAL,
    IGTScheduledTaskEntity.IS_DISABLED,
    IGTScheduledTaskEntity.RUN_TIME,
    IGTScheduledTaskEntity.START_DATE,
    IGTScheduledTaskEntity.TASK_ID,
    IGTScheduledTaskEntity.TYPE,
    IGTScheduledTaskEntity.MONTH_OPTION,
    IGTScheduledTaskEntity.REVERSE_INTERVAL,
    IGTScheduledTaskEntity.WEEK_OF_MONTH,
    IGTScheduledTaskEntity.DAY_OF_WEEK,
    };

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SCHEDULED_TASK;
  }

  protected IDocumentRenderer getFormRenderer(
    ActionContext actionContext,
    RenderingContext rContext,
    boolean edit)
    throws GTClientException
  {
    return new ScheduledTaskRenderer(rContext, edit);
  }

  protected String getNavigatorDocumentKey(
    boolean edit,
    ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(
    boolean edit,
    ActionContext actionContext)
    throws GTClientException
  {
    return (
      edit
        ? IDocumentKeys.SCHEDULED_TASK_UPDATE
        : IDocumentKeys.SCHEDULED_TASK_VIEW);
  }

  protected void initialiseActionForm(
    ActionContext actionContext,
    IGTEntity entity)
    throws GTClientException
  {
    ScheduledTaskAForm form =
      (ScheduledTaskAForm) actionContext.getActionForm();
    IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) entity;

    initFormFields(actionContext, _scheduledTaskFields);

    String[] daysOfWeek = null;
    if (scheduledTask.isNewEntity())
    {
      daysOfWeek = new String[0];
    }
    else
    {
      daysOfWeek =
        getDaysOfWeek(
          (List) scheduledTask.getFieldValue(
            IGTScheduledTaskEntity.DAYS_OF_WEEK));
    }
    form.setDaysOfWeek(daysOfWeek);

  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ScheduledTaskAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SCHEDULED_TASK;
  }

  protected void validateActionForm(
    ActionContext actionContext,
    IGTEntity entity,
    ActionForm actionForm,
    ActionErrors actionErrors)
    throws GTClientException
  {
    ScheduledTaskAForm form = (ScheduledTaskAForm) actionForm;
    IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) entity;

    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.TYPE,
      form,
      scheduledTask);

    String type = form.getTaskType();
    if (type.equals(IGTScheduledTaskEntity.TYPE_USER_PROCEDURE))
    {
      basicValidateString(
        actionErrors,
        IGTScheduledTaskEntity.TASK_ID,
        form,
        scheduledTask);
    }
    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.COUNT,
      form,
      scheduledTask);
    validatePositive(
      scheduledTask.getType(),
      "count",
      form.getCount(),
      actionErrors);
    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.FREQUENCY,
      form,
      scheduledTask);
    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.IS_DISABLED,
      form,
      scheduledTask);
    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.START_DATE,
      form,
      scheduledTask);
    validateDate(
      scheduledTask.getType(),
      "startDate",
      form.getStartDate(),
      "yyyy-MM-dd",
      actionErrors);
    basicValidateString(
      actionErrors,
      IGTScheduledTaskEntity.RUN_TIME,
      form,
      scheduledTask);
    validateDate(
      scheduledTask.getType(),
      "runTime",
      form.getRunTime(),
      "HH:mm",
      actionErrors);

    Integer frequency = StaticUtils.integerValue(form.getFrequency());

    if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_MONTHLY))
    {
      String monthOption = form.getMonthOption();
      basicValidateString(
        actionErrors,
        IGTScheduledTaskEntity.MONTH_OPTION,
        form,
        scheduledTask);
      if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_DAY))
      {
        basicValidateString(
          actionErrors,
          IGTScheduledTaskEntity.INTERVAL,
          form,
          scheduledTask);
        validatePositive(
          scheduledTask.getType(),
          "interval",
          form.getInterval(),
          actionErrors);
      }
      else if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_LASTDAY))
      {
        basicValidateString(
          actionErrors,
          IGTScheduledTaskEntity.REVERSE_INTERVAL,
          form,
          scheduledTask);
        validatePositive(
          scheduledTask.getType(),
          "reverseInterval",
          form.getReverseInterval(),
          actionErrors);
      }
      else if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_WEEK))
      {
        basicValidateString(
          actionErrors,
          IGTScheduledTaskEntity.WEEK_OF_MONTH,
          form,
          scheduledTask);
        basicValidateString(
          actionErrors,
          IGTScheduledTaskEntity.DAY_OF_WEEK,
          form,
          scheduledTask);
      }
    }
    else if (!frequency.equals(IGTScheduledTaskEntity.FREQUENCY_ONCE))
    {
      basicValidateString(
        actionErrors,
        IGTScheduledTaskEntity.INTERVAL,
        form,
        scheduledTask);
      validatePositive(
        scheduledTask.getType(),
        "interval",
        form.getInterval(),
        actionErrors);

      if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_WEEKLY))
      {
        basicValidateStringArray(
          actionErrors,
          IGTScheduledTaskEntity.DAYS_OF_WEEK,
          form,
          scheduledTask);
      }
    }
  }

  protected void validateDate(
    String entityType,
    String field,
    String value,
    String format,
    ActionErrors actionErrors)
  {
    Date date = null;
    if (!value.equals(""))
    {         
      date = DateUtils.parseDate(value, null, null, new SimpleDateFormat(format));
    
      if (date == null)
        EntityFieldValidator.addFieldError(
          actionErrors,
          field,
          entityType,
          EntityFieldValidator.INVALID,
          null);
    }
  }

  protected void validatePositive(
    String entityType,
    String field,
    String value,
    ActionErrors actionErrors)
  {
    if (value.startsWith("-"))
      EntityFieldValidator.addFieldError(
        actionErrors,
        field,
        entityType,
        EntityFieldValidator.INVALID,
        null);
  }

  protected void validateRequired(
    String entityType,
    String field,
    String value,
    ActionErrors actionErrors)
  {
    if (value.equals(""))
      EntityFieldValidator.addFieldError(
        actionErrors,
        field,
        entityType,
        EntityFieldValidator.REQUIRED,
        null);
  }

  protected void updateEntityFields(
    ActionContext actionContext,
    IGTEntity entity)
    throws GTClientException
  {
    ScheduledTaskAForm scheduledTaskAForm =
      (ScheduledTaskAForm) actionContext.getActionForm();
    IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) entity;
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getTaskType(),
      IGTScheduledTaskEntity.TYPE,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getTaskId(),
      IGTScheduledTaskEntity.TASK_ID,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getCount(),
      IGTScheduledTaskEntity.COUNT,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getFrequency(),
      IGTScheduledTaskEntity.FREQUENCY,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getInterval(),
      IGTScheduledTaskEntity.INTERVAL,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getIsDisabled(),
      IGTScheduledTaskEntity.IS_DISABLED,
      Boolean.FALSE);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getRunTime(),
      IGTScheduledTaskEntity.RUN_TIME,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getWeekOfMonth(),
      IGTScheduledTaskEntity.WEEK_OF_MONTH,
      null);

    String startDateStr = scheduledTaskAForm.getStartDate();
    Date startDate = null;
    if (!startDateStr.equals(""))         
      startDate = DateUtils.parseDate(startDateStr, null, null, new SimpleDateFormat("yyyy-MM-dd"));
    scheduledTask.setFieldValue(IGTScheduledTaskEntity.START_DATE, startDate);  

    String[] daysArray = scheduledTaskAForm.getDaysOfWeek();
    scheduledTask.setFieldValue(
      IGTScheduledTaskEntity.DAYS_OF_WEEK,
      getDaysOfWeek(daysArray));

    //virtual fields
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getMonthOption(),
      IGTScheduledTaskEntity.MONTH_OPTION,
      IGTScheduledTaskEntity.MONTH_OPTION_DAY);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getDayOfWeek(),
      IGTScheduledTaskEntity.DAY_OF_WEEK,
      null);
    setIfEmpty(
      scheduledTask,
      scheduledTaskAForm.getReverseInterval(),
      IGTScheduledTaskEntity.REVERSE_INTERVAL,
      null);

  }

  protected void setIfEmpty(
    IGTScheduledTaskEntity scheduledTask,
    String value,
    Number fieldId,
    Object newVal)
    throws GTClientException
  {
    System.out.println("****** set field "+fieldId +" to value "+value);    
    if ("".equals(value))
      scheduledTask.setFieldValue(fieldId, newVal);
    else
      scheduledTask.setFieldValue(fieldId, value);
  }

  protected List getDaysOfWeek(String[] daysSelectedArray)
  {
    if (daysSelectedArray == null)
      return null;
    ArrayList days = new ArrayList(daysSelectedArray.length);
    for (int i = 0; i < daysSelectedArray.length; i++)
    {
      Integer value = StaticUtils.integerValue(daysSelectedArray[i]);
      if (value != null)
      {
        days.add(value);
      }
    }
    return days;
  }

  protected String[] getDaysOfWeek(List daysOfWeek)
  {
    if (daysOfWeek == null || daysOfWeek.size() == 0)
      return new String[0];

    String[] days = new String[daysOfWeek.size()];
    Iterator iter = daysOfWeek.iterator();
    for (int i = 0; i < days.length; i++)
    {
      days[i] = "" + iter.next();
    }
    return days;
  }

  /**
   * @see com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction#initialiseNewEntity(com.gridnode.gtas.client.web.strutsbase.ActionContext, com.gridnode.gtas.client.ctrl.IGTEntity)
   */
  protected void initialiseNewEntity(
    ActionContext actionContext,
    IGTEntity entity)
    throws GTClientException
  {
    entity.setFieldValue(IGTScheduledTaskEntity.IS_DISABLED, Boolean.FALSE);
    entity.setFieldValue(
      IGTScheduledTaskEntity.FREQUENCY,
      IGTScheduledTaskEntity.FREQUENCY_ONCE);
    //entity.setFieldValue(IGTScheduledTaskEntity.COUNT, "1"); // 20040923 DDJ: remove default  
  }
}