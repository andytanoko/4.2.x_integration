/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 * 2003-05-21     Andrew Hill         Support for isAttachResponseDoc field
 * 2003-08-26     Andrew Hill         Fixed dodgy override of getDivertForward() to allow fbds to work
 */
package com.gridnode.gtas.client.web.alert;

import java.util.List;
import java.util.Vector;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTReminderAlertEntity;
import com.gridnode.gtas.client.ctrl.IGTResponseTrackRecordEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ResponseTrackRecordDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_RESPONSE_TRACK_RECORD;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ResponseTrackRecordRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.RESPONSE_TRACK_RECORD_UPDATE : IDocumentKeys.RESPONSE_TRACK_RECORD_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)entity;

    ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)actionContext.getActionForm();
    form.setSentDocType(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_TYPE));
    form.setSentDocIdXpath(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH));
    form.setStartTrackDateXpath(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH));
    form.setResponseDocType(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE));
    form.setResponseDocIdXpath(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH));
    form.setReceiveResponseAlert(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT));
    form.setAlertRecipientXpath(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH));
    form.setIsAttachResponseDoc(responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC)); //20030521AH

    List reminderAlerts = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
    form.initReminderAlertsOrder(reminderAlerts == null ? 0 : reminderAlerts.size());
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ResponseTrackRecordAForm();
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
    //IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)entity;
    //ResponseTrackRecordAForm tform = (ResponseTrackRecordAForm)form;

    basicValidateString(errors, IGTResponseTrackRecordEntity.SENT_DOC_TYPE, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH, form, entity);
    basicValidateString(errors, IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC, form, entity); //20030521AH

    // No validation needed for reminderAlerts
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)entity;
    ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)actionContext.getActionForm();

    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.SENT_DOC_TYPE, form.getSentDocType());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH, form.getSentDocIdXpath());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH, form.getStartTrackDateXpath());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE, form.getResponseDocType());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH, form.getResponseDocIdXpath());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT, form.getReceiveResponseAlert());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH, form.getAlertRecipientXpath());
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC, StaticUtils.booleanValue(form.getIsAttachResponseDoc())); //20030521AH
  }

  protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  { //20030826 - It wasn't delegating back to super properly and fbds were broken as a result!!!!
    //Have re-arranged it to fix the problem.
    if(     ("divertUpdateReminderAlert".equals(divertTo))
        ||  ("divertViewReminderAlert".equals(divertTo)))
    {
      String index = actionContext.getRequest().getParameter("singleIndex");
      index = translateIndex(actionContext,index);
      ActionForward divertForward = mapping.findForward(divertTo);
      if(divertForward == null)
      {
        throw new GTClientException("No mapping found for " + divertTo);
      }
      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(), "index", index),
                          divertForward.getRedirect());
      return processSOCForward(divertForward, opCon); //20030826
    }
    else
    { //20030826 - Use normal code if its not one of our specific cases!
      return super.getDivertForward(actionContext, opCon, mapping, divertTo);  
    }    
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeReminderAlerts(actionContext);
  }

  protected void performPreSaveProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeReminderAlerts(actionContext);
  }

  protected String performPreDivertProcessing(ActionContext actionContext, String divertMappingName)
    throws GTClientException
  {
    arrangeReminderAlerts(actionContext);
    return divertMappingName;
  }

  private String translateIndex(ActionContext actionContext, String oldIndex)
  {
    if("new".equals(oldIndex)) return oldIndex;
    String newIndex = null;
    ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)actionContext.getActionForm();
    String[] order = form.getReminderAlertsOrderExploded();
    for(int i=0; i < order.length; i++)
    {
      if(order[i].equals(oldIndex))
      {
        newIndex = "" + i;
      }
    }
    return newIndex;
  }

  private void arrangeReminderAlerts(ActionContext actionContext)
    throws GTClientException
  {
    ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)actionContext.getActionForm();
    IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)getEntity(actionContext);
    List oldOrder = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
    if( (oldOrder == null) || (oldOrder.size() == 0) )
    {
      return;
    }

    String[] order = form.getReminderAlertsOrderExploded();
    if(order == null)
    {
      order = new String[0];
    }
    List newOrder = new Vector(order.length);

    for(int i=0; i < order.length; i++)
    {
      String nextStr = order[i];
      int next = StaticUtils.primitiveIntValue(nextStr);
      IGTReminderAlertEntity entity = (IGTReminderAlertEntity)oldOrder.get(next);
      newOrder.add(entity);
    }
    responseTrackRecord.setFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS, newOrder);
  }

  protected void prepareView(ActionContext actionContext,
                                    OperationContext opCon,
                                    boolean edit)
    throws GTClientException
  {
    try
    {
      ResponseTrackRecordAForm form = (ResponseTrackRecordAForm)actionContext.getActionForm();
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)getEntity(actionContext);
      List activities = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
      int size = activities == null ? 0 : activities.size();
      form.initReminderAlertsOrder(size);
      super.prepareView(actionContext, opCon, edit);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing entity view", t);
    }
  }
}