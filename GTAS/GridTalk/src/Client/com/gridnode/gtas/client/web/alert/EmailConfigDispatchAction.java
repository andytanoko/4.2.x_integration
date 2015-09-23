/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailConfigDispatchAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.web.alert;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEmailConfigEntity;
import com.gridnode.gtas.client.ctrl.IGTEmailConfigManager;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class EmailConfigDispatchAction  extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_EMAIL_CONFIG;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new EmailConfigRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.EMAIL_CONFIG_UPDATE : IDocumentKeys.EMAIL_CONFIG_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    EmailConfigAForm form = (EmailConfigAForm)actionContext.getActionForm();
    form.setSmtpServerHost(   entity.getFieldString(IGTEmailConfigEntity.SMTP_SERVER_HOST));
    form.setSmtpServerPort(   entity.getFieldString(IGTEmailConfigEntity.SMTP_SERVER_PORT));
    form.setAuthUser(         entity.getFieldString(IGTEmailConfigEntity.AUTH_USER));
    form.setAuthPassword(     entity.getFieldString(IGTEmailConfigEntity.AUTH_PASSWORD));
    form.setRetryInterval(    entity.getFieldString(IGTEmailConfigEntity.RETRY_INTERVAL));
    form.setMaxRetries(       entity.getFieldString(IGTEmailConfigEntity.MAX_RETRIES));
    
    form.setSaveFailedEmails( entity.getFieldString(IGTEmailConfigEntity.SAVE_FAILED_EMAILS));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new EmailConfigAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_EMAIL_CONFIG;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    basicValidateString(errors, IGTEmailConfigEntity.SMTP_SERVER_HOST, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.SMTP_SERVER_PORT, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.AUTH_USER, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.AUTH_PASSWORD, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.RETRY_INTERVAL, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.MAX_RETRIES, form, entity);
    basicValidateString(errors, IGTEmailConfigEntity.SAVE_FAILED_EMAILS, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    EmailConfigAForm emailConfigAForm = (EmailConfigAForm)actionContext.getActionForm();
    entity.setFieldValue(IGTEmailConfigEntity.SMTP_SERVER_HOST,   emailConfigAForm.getSmtpServerHost());
    entity.setFieldValue(IGTEmailConfigEntity.SMTP_SERVER_PORT,   emailConfigAForm.getSmtpServerPortAsInteger());
    entity.setFieldValue(IGTEmailConfigEntity.AUTH_USER,          emailConfigAForm.getAuthUser());
    entity.setFieldValue(IGTEmailConfigEntity.AUTH_PASSWORD,      emailConfigAForm.getAuthPassword());
    entity.setFieldValue(IGTEmailConfigEntity.RETRY_INTERVAL,     emailConfigAForm.getRetryIntervalAsLong());
    entity.setFieldValue(IGTEmailConfigEntity.MAX_RETRIES,        emailConfigAForm.getMaxRetriesAsInteger());
    entity.setFieldValue(IGTEmailConfigEntity.SAVE_FAILED_EMAILS, emailConfigAForm.isSaveFailedEmailsAsBoolean());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    ((IGTEmailConfigManager)manager).saveEmailConfig((IGTEmailConfigEntity)entity);
    setReturnToView(actionContext, false);
  }
}