/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 * 2003-05-21     Andrew Hill         Render new isAttachResponseDoc field
 */
package com.gridnode.gtas.client.web.alert;

import java.util.List;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class ResponseTrackRecordRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number fields[] =
  {
    IGTResponseTrackRecordEntity.SENT_DOC_TYPE,
    IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH,
    IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH,
    IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE,
    IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH,
    IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT,
    IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH,
    IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC, //20030521AH
  };

  public ResponseTrackRecordRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)getActionForm();
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)getEntity();

      renderCommonFormElements(responseTrackRecord.getType(), _edit);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, responseTrackRecord, fields);

      // Render group headings
      renderLabel("sentDoc_heading", "responseTrackRecord.sentDoc.heading",   false);
      renderLabel("responseDoc_heading", "responseTrackRecord.responseDoc.heading", false);
      renderLabel("receiveResponse_heading", "responseTrackRecord.receiveResponse.heading", false);

      addReminderAlertsElv(rContext,form);

      if(_edit)
      {
        renderLabel("sentDocType_create", "responseTrackRecord.documentType.create", false);
        renderLabel("responseDocType_create", "responseTrackRecord.documentType.create", false);
        renderLabel("receiveResponseAlert_create", "responseTrackRecord.alert.create", false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering responseTrackRecord screen",t);
    }
  }

  protected void addReminderAlertsElv(RenderingContext rContext,
                                      ResponseTrackRecordAForm form)
    throws RenderingException
  {
    try
    {
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)getEntity();
      Number[] columns = {  IGTReminderAlertEntity.DAYS_TO_REMINDER,
                            IGTReminderAlertEntity.ALERT_TO_RAISE,
                            IGTReminderAlertEntity.DOC_RECIPIENT_XPATH,
                            IGTReminderAlertEntity.DOC_SENDER_XPATH };

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_REMINDER_ALERT;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, entityType);
      List reminderAlerts = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();

      listOptions.setColumnAdapter(adapter);
      if(_edit)
      {
        listOptions.setCreateURL("divertUpdateReminderAlert");
        listOptions.setDeleteURL("divertDeleteReminderAlert");
        listOptions.setCreateLabelKey("responseTrackRecord.reminderAlerts.create");
        listOptions.setDeleteLabelKey("responseTrackRecord.reminderAlerts.delete");
        listOptions.setAllowsSelection(true);
        listOptions.setAllowsEdit(true);
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }

      listOptions.setHeadingLabelKey("responseTrackRecord.reminderAlerts");
      listOptions.setUpdateURL("divertUpdateReminderAlert");
      listOptions.setViewURL("divertViewReminderAlert");
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "reminderAlerts_details",
                                                listOptions,
                                                reminderAlerts);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(true);
      elvRenderer.setTableName("reminderAlerts");
      elvRenderer.setOrder(form.getReminderAlertsOrderExploded());
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the ReminderAlert table", e);
    }
  }
}