/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.alert;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAlertTriggerEntity;
import com.gridnode.gtas.client.ctrl.IGTAlertTriggerManager;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class AlertTriggerDispatchAction extends EntityDispatchAction2
{
  private static final String DO_ADD_RECIPIENT = "addAlertRecipient";
  private static final String DO_REMOVE_RECIPIENT = "removeAlertRecipient";

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ALERT_TRIGGER;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new AlertTriggerRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ALERT_TRIGGER_UPDATE : IDocumentKeys.ALERT_TRIGGER_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)entity;
    AlertTriggerAForm form = (AlertTriggerAForm)actionContext.getActionForm();
    
    form.setLevel(at.getFieldString(IGTAlertTriggerEntity.LEVEL));
    form.setAlertUid(at.getFieldString(IGTAlertTriggerEntity.ALERT_UID));
    form.setAlertType(at.getFieldString(IGTAlertTriggerEntity.ALERT_TYPE));
    form.setDocType(at.getFieldString(IGTAlertTriggerEntity.DOC_TYPE));
    form.setPartnerType(at.getFieldString(IGTAlertTriggerEntity.PARTNER_TYPE));
    form.setPartnerGroup(at.getFieldString(IGTAlertTriggerEntity.PARTNER_GROUP));
    form.setPartnerId(at.getFieldString(IGTAlertTriggerEntity.PARTNER_ID));
    form.setIsEnabled(at.getFieldString(IGTAlertTriggerEntity.IS_ENABLED));
    form.setIsAttachDoc(at.getFieldString(IGTAlertTriggerEntity.IS_ATTACH_DOC));
    
    Collection recipients = (Collection)at.getFieldValue(IGTAlertTriggerEntity.RECIPIENTS);
    StaticUtils.dumpCollection(recipients);
    
    form.setRecipients( StaticUtils.getStringArray((Collection)at.getFieldValue(IGTAlertTriggerEntity.RECIPIENTS)) );
    
    if(at.isNewEntity())
    {
      String level = actionContext.getRequest().getParameter("level");
      form.setLevel(level);
    }
    
    form.setRecipientType(IGTAlertTriggerEntity.RECIPIENT_TYPE_USER);
    form.setRecipientValue(null);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    //@todo:
    // for crying out loud andrew!
    // The info needed is in the mapping. Factor out this method
    // from all these subclasses dude! 20030507AH
    return new AlertTriggerAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    //@todo:
    // We have the entity name. Move it to the superclass to get the manager!
    return IGTManager.MANAGER_ALERT_TRIGGER;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)entity;
    AlertTriggerAForm form = (AlertTriggerAForm)actionForm;

    if( basicValidateString(errors, IGTAlertTriggerEntity.LEVEL, form, at ) )
    {
      Integer level = form.getLevelInteger();
      if(level == null)
      {
        EntityFieldValidator.addFieldError(errors,
                                          "level",
                                          at.getType(),
                                          EntityFieldValidator.REQUIRED, null);
      }
      else
      {
        /*if(level.intValue() > 0)
        {
          String[] recipients = form.getRecipients();
          if( (recipients == null)  || (recipients.length == 0) )
          {
            EntityFieldValidator.addFieldError(errors,
                                                "recipients",
                                                at.getType(),
                                                EntityFieldValidator.REQUIRED, null);
          }
        }*/       
        Number[] fields = AlertTriggerUtils._fields[level.intValue()];
        for(int i=0; i < fields.length; i++)
        {
          basicValidateString(errors, fields[i], form, at);
        }
      }
      Number[] common = AlertTriggerUtils._commonFields;
      for(int i=0; i < common.length; i++)
      {
        basicValidateString(errors, common[i], form, at);
      }
  
    }    
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)entity;
    AlertTriggerAForm form = (AlertTriggerAForm)actionContext.getActionForm();
    
    Integer level       = form.getLevelInteger();
    Long alertUid       = StaticUtils.longValue( form.getAlertUid() );
    String alertType    = StaticUtils.clean(form.getAlertType());
    String docType      = StaticUtils.clean(form.getDocType());
    String partnerType  = StaticUtils.clean(form.getPartnerType());
    String partnerGroup = StaticUtils.clean(form.getPartnerGroup());
    String partnerId    = StaticUtils.clean(form.getPartnerId());
    Boolean isEnabled   = StaticUtils.booleanValue( form.getIsEnabled() );
    Boolean isAttachDoc = StaticUtils.booleanValue( form.getIsAttachDoc() );
    Collection recipients = StaticUtils.collectionValue(form.getRecipients());   
    
    at.setFieldValue( IGTAlertTriggerEntity.LEVEL,           level         );
    at.setFieldValue( IGTAlertTriggerEntity.ALERT_UID,       alertUid      );
    at.setFieldValue( IGTAlertTriggerEntity.ALERT_TYPE,      alertType     );
    at.setFieldValue( IGTAlertTriggerEntity.DOC_TYPE,        docType       );
    at.setFieldValue( IGTAlertTriggerEntity.PARTNER_TYPE,    partnerType   );
    at.setFieldValue( IGTAlertTriggerEntity.PARTNER_GROUP,   partnerGroup  );
    at.setFieldValue( IGTAlertTriggerEntity.PARTNER_ID,      partnerId     );
    at.setFieldValue( IGTAlertTriggerEntity.IS_ENABLED,      isEnabled     );
    at.setFieldValue( IGTAlertTriggerEntity.IS_ATTACH_DOC,   isAttachDoc   );
    at.setFieldValue( IGTAlertTriggerEntity.RECIPIENTS,      recipients    );
  }
  
  
  public ActionForward addAlertRecipient(
    ActionMapping mapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException, GTClientException
  {
    request.setAttribute(DO_ADD_RECIPIENT,Boolean.TRUE);    
    return update(mapping, actionForm, request, response);
  }
  
  public ActionForward removeAlertRecipient(
    ActionMapping mapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException, GTClientException
  {
    request.setAttribute(DO_REMOVE_RECIPIENT,Boolean.TRUE);    
    return update(mapping, actionForm, request, response);
  }
  
  

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    HttpServletRequest request = actionContext.getRequest();
    AlertTriggerAForm form = (AlertTriggerAForm)actionContext.getActionForm();
    IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)getEntity(actionContext);
    IGTAlertTriggerManager mgr = (IGTAlertTriggerManager)at.getSession().getManager(at.getType());
    
    Boolean addAlertRecipient = (Boolean)request.getAttribute(DO_ADD_RECIPIENT);
    Boolean removeAlertRecipient = (Boolean)request.getAttribute(DO_REMOVE_RECIPIENT);
    
    if(Boolean.TRUE.equals(removeAlertRecipient))
    {
      String[] recipients = form.getRecipients();
      String[] deleteRecipients = form.getDeleteRecipients();
      recipients = StaticUtils.removeFromArray(recipients, deleteRecipients);
      form.setRecipients(recipients); 
    }
    
    if(Boolean.TRUE.equals(addAlertRecipient))
    {
      String[] recipients = form.getRecipients();
      String recipientType = form.getRecipientType();
      String recipientValue = form.getRecipientValue();
      if( validateNewRecipient(actionContext, recipientType, recipientValue) )
      {
        String recipient = mgr.makeRecipient(recipientType, recipientValue);
        form.setRecipients( (String[])StaticUtils.appendArray(recipients, recipient) );
        form.setRecipientValue(null); //Clear out successful value ready to type a new one
      }
    }
  }
  
  private boolean validateNewRecipient(ActionContext actionContext, String recipientType, String recipientValue)
    throws GTClientException
  {
    try
    {
      boolean noErrors = true;
      ActionErrors errors = new ActionErrors();
      if( StaticUtils.stringEmpty(recipientType) )
      {
        EntityFieldValidator.addFieldError( errors, "type", "alertRecipient",
                                            EntityFieldValidator.REQUIRED, null);
        noErrors = false;
      }
      else
      { // Check that the value submitted for the type is valid
        if( !StaticUtils.arrayContains(AlertTriggerUtils._recipientTypeValues, recipientType) )
        {
          EntityFieldValidator.addFieldError( errors, "type", "alertRecipient",
                                              EntityFieldValidator.INVALID, null);
          noErrors = false;
        }
      }
      
      if( StaticUtils.stringEmpty(recipientValue) )
      {
        EntityFieldValidator.addFieldError( errors, "value",
                                            "alertRecipient", EntityFieldValidator.REQUIRED, null);
        noErrors = false;
      }
      
      if(!noErrors)
      {
        this.saveErrors(actionContext.getRequest(), errors);
      }
      return noErrors;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating new recipient details",t);
    }
  }

}