/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: ScheduledTaskRenderer.java
 * 
 **************************************************************************** 
 * Date           Author           Changes
 **************************************************************************** 
 * 2004-02-11     Neo Sok Lay      Created
 * 2009-04-11     Tam Wei Xiang    #122 - use archiveID instead of archiveName
 * 2009-08-14     Tam Wei Xiang    #122 - modified renderTaskType(....)
 */
package com.gridnode.gtas.client.web.scheduler;

import com.gridnode.gtas.client.ctrl.IGTArchiveEntity;
import com.gridnode.gtas.client.ctrl.IGTScheduledTaskEntity;
import com.gridnode.gtas.client.ctrl.IGTUserProcedureEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.FakeEnumeratedConstraint;
import com.gridnode.gtas.client.web.strutsbase.FakeForeignEntityConstraint;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;

public class ScheduledTaskRenderer extends AbstractRenderer
{
  private boolean _edit;

  private static final Number[] _scheduledTaskFields = {
      IGTScheduledTaskEntity.FREQUENCY, IGTScheduledTaskEntity.IS_DISABLED,
      IGTScheduledTaskEntity.RUN_TIME, IGTScheduledTaskEntity.START_DATE,
  };

  public ScheduledTaskRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      if (_edit) includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);

      RenderingContext rContext = getRenderingContext();
      ScheduledTaskAForm form = (ScheduledTaskAForm) getActionForm();
      IGTScheduledTaskEntity scheduledTask = (IGTScheduledTaskEntity) getEntity();
      ActionErrors errors = rContext.getActionErrors();

      renderCommonFormElements(scheduledTask.getType(), _edit);

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(
                                                                           rContext);
      renderFields(bfpr, scheduledTask, _scheduledTaskFields);
      if (isValidDateFormat(form.getStartDate()))
        renderElementValue("startDate_value", form.getStartDate()); // 20040218
      // DDJ: Quick fix for Start Date not displaying due to BFPR

      renderTaskType(bfpr, scheduledTask, errors);
      String taskType = form.getTaskType();
      if (IGTScheduledTaskEntity.TYPE_DB_ARCHIVE.equals(taskType))
      {

        bfpr.reset();
        bfpr.setBindings(scheduledTask, IGTScheduledTaskEntity.TASK_ID, errors);
        if (_edit)
        { // Render diversion labels

          bfpr
              .setConstraint(new FakeForeignEntityConstraint(
                                                             IGTArchiveEntity.ENTITY_ARCHIVE,
                                                             "archiveID",
                                                             "archiveID"));
        }
        bfpr.render(_target);

      }
      else if (taskType.equals(IGTScheduledTaskEntity.TYPE_USER_PROCEDURE))
      {
        renderUserProcedureTasks(bfpr, scheduledTask, errors);
      }
      else
      {
        removeNode("taskId_details");
      }
      renderScheduleFrequency(bfpr, errors, scheduledTask, form);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering scheduledTask screen", t);
    }
  }

  private boolean isValidDateFormat(String dateStr)
  {
    boolean valid = false;
    try
    {
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
      valid = date != null;
    }
    catch (Exception e)
    {
    }

    return valid;
  }

  protected void renderTaskType(BindingFieldPropertyRenderer bfpr,
                                IGTScheduledTaskEntity scheduledTask,
                                ActionErrors errors) throws RenderingException
  {
    bfpr.reset();
    bfpr.setBindings(scheduledTask, IGTScheduledTaskEntity.TYPE, errors);
    if (_edit && scheduledTask.isNewEntity())
    {
      // limit only User Procedure schedule can be created. others are system
      // created and only allow edit
      bfpr.setConstraint(new FakeEnumeratedConstraint(new String[] {
          "scheduledTask.taskType.userProcedure",
          "scheduledTask.taskType.dbArchive" //TWX 20090814 changed from scheduledTask.taskType.dBArchive
      }, new String[] {
          IGTScheduledTaskEntity.TYPE_USER_PROCEDURE,
          IGTScheduledTaskEntity.TYPE_DB_ARCHIVE
      }));
    }
    bfpr.render(_target);
  }

  protected void renderUserProcedureTasks(BindingFieldPropertyRenderer bfpr,
                                          IGTScheduledTaskEntity scheduledTask,
                                          ActionErrors errors) throws RenderingException
  {
    bfpr.reset();
    bfpr.setBindings(scheduledTask, IGTScheduledTaskEntity.TASK_ID, errors);
    if (_edit)
    {
      bfpr
          .setConstraint(new FakeForeignEntityConstraint(
                                                         IGTUserProcedureEntity.ENTITY_USER_PROCEDURE,
                                                         "name", "name"));

    }
    bfpr.render(_target);
  }

  protected void renderScheduleFrequency(BindingFieldPropertyRenderer bfpr,
                                         ActionErrors errors,
                                         IGTScheduledTaskEntity scheduledTask,
                                         ScheduledTaskAForm form) throws RenderingException
  {
    Integer frequency = StaticUtils.integerValue(form.getFrequency());
    if (frequency == null)
    {
      removeNode("weekly_details");
      removeNode("monthly_details");
      removeNode("daily_details");
      removeNode("hourly_details");
      removeNode("minutely_details");
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_DAILY))
    {
      removeNode("weekly_details");
      removeNode("monthly_details");
      removeNode("hourly_details");
      removeNode("minutely_details");
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.COUNT);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.INTERVAL);
      renderLabelCarefully("interval_label", "scheduledTask.interval.daily",
                           false);
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_HOURLY))
    {
      removeNode("weekly_details");
      removeNode("monthly_details");
      removeNode("daily_details");
      removeNode("minutely_details");
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.COUNT);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.INTERVAL);
      renderLabelCarefully("interval_label", "scheduledTask.interval.hourly",
                           false);
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_MINUTELY))
    {
      removeNode("weekly_details");
      removeNode("monthly_details");
      removeNode("hourly_details");
      removeNode("daily_details");
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.COUNT);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.INTERVAL);
      renderLabelCarefully("interval_label", "scheduledTask.interval.minutely",
                           false);
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_MONTHLY))
    {
      removeNode("weekly_details");
      removeNode("minutely_details");
      removeNode("hourly_details");
      removeNode("daily_details");

      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.COUNT);
      renderMonthOption(form.getMonthOption());
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.INTERVAL);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.REVERSE_INTERVAL);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.DAY_OF_WEEK);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.WEEK_OF_MONTH);
      renderLabelCarefully("interval_label", "scheduledTask.interval.monthly",
                           false);

      if (!_edit)
      {
        String monthOption = form.getMonthOption();
        if (monthOption.equals(IGTScheduledTaskEntity.MONTH_OPTION_DAY))
        {
          removeNode("monthOption_lastDay_details");
          removeNode("monthOption_week_details");
        }
        else if (monthOption
            .equals(IGTScheduledTaskEntity.MONTH_OPTION_LASTDAY))
        {
          removeNode("monthOption_day_details");
          removeNode("monthOption_week_details");
        }
        else
        {
          removeNode("monthOption_lastDay_details");
          removeNode("monthOption_day_details");
        }
      }
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_ONCE))
    {
      removeNode("count_details");
      removeNode("weekly_details");
      removeNode("monthly_details");
      removeNode("hourly_details");
      removeNode("minutely_details");
      removeNode("daily_details");
    }
    else if (frequency.equals(IGTScheduledTaskEntity.FREQUENCY_WEEKLY))
    {
      removeNode("minutely_details");
      removeNode("monthly_details");
      removeNode("hourly_details");
      removeNode("daily_details");
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.COUNT);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.INTERVAL);
      renderField(bfpr, scheduledTask, IGTScheduledTaskEntity.DAYS_OF_WEEK);
      renderLabelCarefully("interval_label", "scheduledTask.interval.weekly",
                           false);
    }
  }

  private void renderMonthOption(String monthOption) throws RenderingException
  {
    Element[] options = getElementsById("monthOption_value");
    for (int i = 0; i < options.length; i++)
    {
      renderCheckboxSelection(options[i], monthOption);
    }
  }

}
