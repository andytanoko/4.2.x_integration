/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.ctrl.IGTReminderAlertEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ReminderAlertRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number fields[] = 
  {
    IGTReminderAlertEntity.DAYS_TO_REMINDER,
    IGTReminderAlertEntity.ALERT_TO_RAISE,
    IGTReminderAlertEntity.DOC_RECIPIENT_XPATH,
    IGTReminderAlertEntity.DOC_SENDER_XPATH,
  };

  public ReminderAlertRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      //RenderingContext rContext = getRenderingContext();
      //ReminderAlertAForm form = (ReminderAlertAForm)getActionForm();
      IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)getEntity();

      renderCommonFormElements(reminderAlert.getType(), _edit);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, reminderAlert, fields);

      if(_edit)
      {
        renderLabel("alertToRaise_create", "reminderAlert.alert.create", false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering reminderAlert screen",t);
    }
  }
}