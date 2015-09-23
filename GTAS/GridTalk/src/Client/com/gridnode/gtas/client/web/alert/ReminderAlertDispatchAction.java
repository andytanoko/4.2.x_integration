/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ReminderAlertDispatchAction extends EntityDispatchAction2
{
  private static final String INDEX_KEY = "reminderAlert index";

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_REMINDER_ALERT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ReminderAlertRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.REMINDER_ALERT_UPDATE : IDocumentKeys.REMINDER_ALERT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)entity;

    ReminderAlertAForm form = (ReminderAlertAForm)actionContext.getActionForm();
    form.setDaysToReminder(reminderAlert.getFieldString(IGTReminderAlertEntity.DAYS_TO_REMINDER));
    form.setAlertToRaise(reminderAlert.getFieldString(IGTReminderAlertEntity.ALERT_TO_RAISE));
    form.setDocRecipientXpath(reminderAlert.getFieldString(IGTReminderAlertEntity.DOC_RECIPIENT_XPATH));
    form.setDocSenderXpath(reminderAlert.getFieldString(IGTReminderAlertEntity.DOC_SENDER_XPATH));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ReminderAlertAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_RESPONSE_TRACK_RECORD;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)entity;
    //ReminderAlertAForm tform = (ReminderAlertAForm)form;

    basicValidateString(errors, IGTReminderAlertEntity.DAYS_TO_REMINDER, form, entity);
    basicValidateString(errors, IGTReminderAlertEntity.ALERT_TO_RAISE, form, entity);
    basicValidateString(errors, IGTReminderAlertEntity.DOC_RECIPIENT_XPATH, form, entity);
    basicValidateString(errors, IGTReminderAlertEntity.DOC_SENDER_XPATH, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)entity;
    ReminderAlertAForm form = (ReminderAlertAForm)actionContext.getActionForm();

    reminderAlert.setFieldValue(IGTReminderAlertEntity.DAYS_TO_REMINDER, form.getDaysToReminderInteger());
    reminderAlert.setFieldValue(IGTReminderAlertEntity.ALERT_TO_RAISE, form.getAlertToRaise());
    reminderAlert.setFieldValue(IGTReminderAlertEntity.DOC_RECIPIENT_XPATH, form.getDocRecipientXpath());
    reminderAlert.setFieldValue(IGTReminderAlertEntity.DOC_SENDER_XPATH, form.getDocSenderXpath());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTResponseTrackRecordEntity responseTrackRecord = getResponseTrackRecord(actionContext);
      List reminderAlerts = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
      IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)entity;

      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      String indexString = (String)opContext.getAttribute(INDEX_KEY);
      if("new".equals(indexString))
      {
        reminderAlerts.add(reminderAlert);
      }
      else
      {
        int index = Integer.parseInt(indexString);
        reminderAlerts.remove(index);
        reminderAlerts.add(index, reminderAlert);
      }

      // update parent action form odering
      OperationContext pOpContext = opContext.getPreviousContext();
      ResponseTrackRecordAForm responseTrackRecordForm = (ResponseTrackRecordAForm)pOpContext.getActionForm();
      responseTrackRecordForm.initReminderAlertsOrder(reminderAlerts == null ? 0 : reminderAlerts.size());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving returnAlert entity in responseTrackRecord:" + entity, t);
    }
  }

  protected IGTResponseTrackRecordEntity getResponseTrackRecord(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
      OperationContext pOpContext = opContext.getPreviousContext();
      if(pOpContext == null)
      {
        throw new java.lang.NullPointerException("null parent OperationContext reference");
      }
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)
                                      pOpContext.getAttribute(IOperationContextKeys.ENTITY);
      if(responseTrackRecord == null)
      {
        throw new java.lang.NullPointerException("No entity object found in parent OperationContext");
      }
      if(!(responseTrackRecord instanceof IGTResponseTrackRecordEntity))
      {
        throw new java.lang.IllegalStateException("Entity found in parent OperationContext is"
                                              + " not a responseTrackRecord entity. Entity=" + responseTrackRecord);
      }
      return responseTrackRecord;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving responseTrackRecord entity from parent OperationContext", t);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    try
    {
      IGTReminderAlertEntity entity = null;
      String indexString = actionContext.getRequest().getParameter("index");
      opContext.setAttribute(INDEX_KEY, indexString);
      if(indexString != null)
      {
        if(indexString.equals("new"))
        {
          try
          {
            IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
            IGTResponseTrackRecordManager manager = (IGTResponseTrackRecordManager)gtasSession.getManager(IGTManager.MANAGER_RESPONSE_TRACK_RECORD);
            entity = manager.newReminderAlert();
            initialiseNewEntity(actionContext, entity);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error instantiating new reminderAlert entity object",t);
          }
        }
        else
        {
          try
          {
            int index = Integer.parseInt(indexString);
            IGTResponseTrackRecordEntity responseTrackRecord = getResponseTrackRecord(actionContext);
            List reminderAlertsList = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
            if(reminderAlertsList == null)
            {
              throw new java.lang.NullPointerException("Null value for responseTrackRecord.reminderAlerts in responseTrackRecord entity=" + responseTrackRecord);
            }
            try
            {
              entity = (IGTReminderAlertEntity)reminderAlertsList.get(index);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error retrieving reminderAlert from list at index "
                                          + index, t);
            }
            if(entity == null)
            {
              throw new java.lang.NullPointerException("Null entity object at index "
                        + index + " of reminderAlert list retrieved from responseTrackRecord entity "
                        + responseTrackRecord);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error retrieving reminderAlert entity object from"
                                        + " responseTrackRecord entity in parent OperationContext", t);
          }
        }
      }
      else
      {
        throw new GTClientException("No index specified");
      }
      opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
      ActionForward submitForward = actionContext.getMapping().findForward("submit");
      opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error obtaining reminderAlert entity object", t);
    }
  }
}