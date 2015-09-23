/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 * 2006-05-08     Neo Sok Lay         GNDB00027030: Hide User Administrator role
 */
package com.gridnode.gtas.client.web.alert;

import java.util.*;
import org.w3c.dom.*;
import org.apache.struts.action.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.*;

public class AlertTriggerRenderer extends AbstractRenderer implements IFilter, IBFPROptionSource
{
  private boolean _edit;
  private Integer _level;
  private String _partnerType;
  
  private static final String[] _recipientTypeLabels = new String[]
  {
    "alertRecipient.type.user",
    "alertRecipient.type.role",
    "alertRecipient.type.email_address",
    "alertRecipient.type.email_code",
    "alertRecipient.type.email_code_xpath",
    "alertRecipient.type.email_address_xpath",
  };
  
  
  private static final IEnumeratedConstraint _recipientTypeConstraint
                        = new FakeEnumeratedConstraint(_recipientTypeLabels,
                                                       AlertTriggerUtils._recipientTypeValues);
  
  // The level field determines which of the subarrays will be rendered. Note that the level field
  // itself is included in a seperate array so that it can always be rendered even if the user has not
  // selected a level. We dont include the recipients in these array as that needs special rendering
  // beyond what renderFields and the BFPR provides.
  
  
  private static final Number[][] _removeFields =
  {
    { //Level 0
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_ID,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.PARTNER_GROUP,
      IGTAlertTriggerEntity.RECIPIENTS,
    },
    { //Level 1
      IGTAlertTriggerEntity.PARTNER_ID,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.PARTNER_GROUP,
    },
    { //Level 2,
      IGTAlertTriggerEntity.PARTNER_ID,
      IGTAlertTriggerEntity.PARTNER_GROUP,
    },
    { //Level 3
      IGTAlertTriggerEntity.PARTNER_ID,
    },
    { //Level 4
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.PARTNER_GROUP,
    },    
  };
  
  private static final Number[] _levelNullRemoveFields =
  {
    IGTAlertTriggerEntity.DOC_TYPE,
    IGTAlertTriggerEntity.PARTNER_ID,
    IGTAlertTriggerEntity.PARTNER_TYPE,
    IGTAlertTriggerEntity.PARTNER_GROUP,
    IGTAlertTriggerEntity.RECIPIENTS,
    IGTAlertTriggerEntity.ALERT_TYPE,
    IGTAlertTriggerEntity.ALERT_UID,
    IGTAlertTriggerEntity.RECIPIENTS,
  };

  public AlertTriggerRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      AlertTriggerAForm form = (AlertTriggerAForm)getActionForm();
      IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)getEntity();
      
      if(_edit && !at.isNewEntity())
      {
        replaceNodeWithNewElement("span", "level_value", false, false);
        replaceNodeWithNewElement("span", "alertType_value", false, false);
      }
      
      _level = form.getLevelInteger(); //Taking advantage of renderers stateful nature
      _partnerType = form.getPartnerType();

      renderCommonFormElements(at.getType(), _edit);      
      BindingFieldPropertyRenderer bfpr = renderFields(null, at, AlertTriggerUtils._commonFields, null, form, null);
      
      if(_level != null)
      {
        Number[] levelFields = AlertTriggerUtils._fields[_level.intValue()];
        Number[] levelRemoveFields = _removeFields[_level.intValue()];
        bfpr.setOptionSource(this);
        bfpr = renderFields(bfpr, at, levelFields, this, form, null);
        if( _level.intValue() > 0)
        {
          renderRecipientCreator(rContext, at, form, _edit);
          renderRecipients(rContext, at, form);
        }
        if( _level.intValue() == 2 )
        {
          Element partnerTypeElement = getElementById("partnerType_value",false);
          if(partnerTypeElement != null)
          {
            partnerTypeElement.removeAttribute("onchange");
          }
        }
        removeFields(at,levelRemoveFields,null);
      }
      else
      {
        removeFields(at,_levelNullRemoveFields,null);
      }
      renderDiversionLabels(rContext);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering alertTrigger screen",t);
    }
  }
  
  private void renderRecipients(RenderingContext rContext,
                                IGTAlertTriggerEntity at,
                                AlertTriggerAForm form)
    throws RenderingException
  {
    try
    { 
      renderLabel("recipients_label","alertTrigger.recipients",false);    
      IGTAlertTriggerManager mgr = (IGTAlertTriggerManager)at.getSession().getManager(at.getType());
      String[] recipients = form.getRecipients();
      if(recipients == null) recipients = StaticUtils.EMPTY_STRING_ARRAY;
      
      Element recipientElement = getElementById("recipients_value",false);
      if(recipientElement != null)
      {
        if(!"select".equals(recipientElement.getNodeName()))
        {
          throw new RenderingException("recipients_value element must be a <select>");
        }
        recipientElement.setAttribute("name","deleteRecipients");
        removeAllChildren(recipientElement);
        for(int i = 0; i < recipients.length; i++)
        {
        	//NSL20060508 hide user admin role type
        	String recptType = mgr.extractRecipientType(recipients[i]);
        	String recptValue = mgr.extractRecipientValue(recipients[i]);
        	if (IGTAlertTriggerEntity.RECIPIENT_TYPE_ROLE.equalsIgnoreCase(recptType) && "User Administrator".equalsIgnoreCase(recptValue))
        	{
        		continue; //skip rendering recipients of User Administrator role
        	}
          Element option = _target.createElement("option");
          option.setAttribute("value",recipients[i]);
          String typeLabel = rContext.getResourceLookup().getMessage("alertRecipient.type."
                            + recptType.toLowerCase()) ;
          String recipientValue = recptValue;
          replaceText(option,typeLabel + ":" + recipientValue);          
          recipientElement.appendChild(option);  
        }
        ActionErrors errors = rContext.getActionErrors();
        ActionError recipientsError = MessageUtils.getFirstError(errors, "recipients");
        if(recipientsError != null)
        {
          renderLabelCarefully("recipients_error",recipientsError.getKey(),false);
        }
        else
        {
          removeNode("recipients_error",false);
        }
      }
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering recipients",t);
    }
  } 
  
  private void renderRecipientCreator(RenderingContext rContext,
                                      IGTAlertTriggerEntity at,
                                      AlertTriggerAForm form,
                                      boolean visible)
    throws RenderingException
  {
    try
    { 
      if(visible)
      {
        renderLabel("recipient_type_label","alertRecipient.type",false);
        renderLabel("recipient_value_label","alertRecipient.value",false);
        renderLabelCarefully("addAlertRecipient","alertRecipient.addAlertRecipient",false);
        renderLabelCarefully("removeAlertRecipient","alertRecipient.removeAlertRecipient",false);
        renderElementAttribute("recipient_type","name","recipientType");
        
        Element recipientValueElement = getElementById("recipient_value",false);
        if(recipientValueElement != null)
        {
          renderRecipientValueElement(rContext, at, form.getRecipientValue(), form.getRecipientType(), recipientValueElement);
        }
        
        Element recipientType = getElementById("recipient_type",false);
        if(recipientType != null)
        {
          renderSelectOptions( recipientType, _recipientTypeConstraint, true, null );
          renderSelectedOptions(recipientType,form.getRecipientType());
        }
        
        ActionErrors errors = rContext.getActionErrors();
        ActionError recipientTypeError = MessageUtils.getFirstError(errors, "type");
        if(recipientTypeError != null)
        {
          renderLabelCarefully("recipient_type_error",recipientTypeError.getKey(),false);
        }
        else
        {
          removeNode("recipient_type_error",false);
        }
        ActionError recipientValueError = MessageUtils.getFirstError(errors, "value");
        if(recipientValueError != null)
        {
          renderLabelCarefully("recipient_value_error",recipientValueError.getKey(),false);
        }
        else
        {
          removeNode("recipient_value_error",false);
        }
        /*String[] recipients = form.getRecipients();
        if( (recipients != null) && (recipients.length == 0) )
        {
          renderElementAttribute("recipient_type","class","mandatory");
          renderElementAttribute("recipient_value","class","mandatory");
        }*/
      }
      else
      {
        removeNode("recipients_creator",false);
      }
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering recipient creator widget",t);
    }
  } 
  
  private void renderRecipientValueElement( RenderingContext rContext,
                                            IGTAlertTriggerEntity at,
                                            String recipientValue,
                                            String recipientType,
                                            Element recipientValueElement)
    throws RenderingException
  {
    try
    {
      if( IGTAlertTriggerEntity.RECIPIENT_TYPE_ROLE.equals(recipientType) )
      {
        recipientValueElement = replaceNodeWithNewElement("select", recipientValueElement, false);
        IGTRoleManager roleMgr = (IGTRoleManager)at.getSession().getManager(IGTManager.MANAGER_ROLE);
        Collection roles = roleMgr.getAll();
        IOptionValueRetriever r = new EntityOptionValueRetriever(IGTRoleEntity.ROLE, IGTRoleEntity.ROLE);
        renderSelectOptions("recipient_value",roles,r,true,"");
        renderSelectedOptions(recipientValueElement, recipientValue);
        removeNode("user_create",false);
      }
      else if( IGTAlertTriggerEntity.RECIPIENT_TYPE_USER.equals(recipientType) )
      {
        recipientValueElement = replaceNodeWithNewElement("select", recipientValueElement, false);
        IGTUserManager userMgr = (IGTUserManager)at.getSession().getManager(IGTManager.MANAGER_USER);
        Collection users = userMgr.getAll();
        IOptionValueRetriever r = new EntityOptionValueRetriever(IGTUserEntity.USER_ID, IGTUserEntity.USER_ID);
        renderSelectOptions("recipient_value",users,r,true,"");
        renderSelectedOptions(recipientValueElement, recipientValue);
        //removeNode("role_create",false);
      }
      else
      {
        recipientValueElement.setAttribute("value",recipientValue);
        removeNode("user_create",false);
        //removeNode("role_create",false);
      }
      removeNode("role_create", false); //NSL20060508 Always hide create role link
      
      recipientValueElement.setAttribute("name","recipientValue");
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering recipient value element",t);
    }
  }  
  
  private void renderDiversionLabels(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      renderLabelCarefully("docType_create","alertTrigger.docType.create",false);
      renderLabelCarefully("partnerId_create","alertTrigger.partnerId.create",false);
      renderLabelCarefully("partnerType_create","alertTrigger.partnerType.create",false);
      renderLabelCarefully("partnerGroup_create","alertTrigger.partnerGroup.create",false);
      renderLabelCarefully("alertUid_create","alertTrigger.alertUid.create",false);
      renderLabelCarefully("user_create","alertTrigger.recipients.create.user",false);
      renderLabelCarefully("role_create","alertTrigger.recipients.create.role",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering diversion labels",t);
    }  
  }
  public boolean allows(Object object, Object context) throws GTClientException
  {
    //I have taken the liberty of assuming that the context is a bfpr
    BindingFieldPropertyRenderer bfpr = (BindingFieldPropertyRenderer)context;
    //AlertTriggerAForm form = (AlertTriggerAForm)bfpr.getActionForm();
    Number fieldId = bfpr.getFieldId();
    
    if( IGTAlertTriggerEntity.ALERT_TYPE.equals( fieldId ) )
    { 
      if(_level != null)
      {
        // Get a list of valid names to select from
        String[] applicableAlertTypes = IGTAlertTriggerEntity.ALERT_TYPE_NAMES[_level.intValue()];
        // Extract the name value for the alertType instance we are filtering here
        String name = ((IGTAlertTypeEntity)object).getFieldString(IGTAlertTypeEntity.NAME);
        // Return true if the name is in the list of permitted names at this alertTrigger level
        return (StaticUtils.findValueInArray(applicableAlertTypes, name, false) > -1);
      }
      else
      { // No level selected - so cannot choose an alert type
        return false; // Dont let this alertType through filter
      }
    }
    else if( IGTAlertTriggerEntity.PARTNER_GROUP.equals( fieldId ) )
    {
      IGTPartnerGroupEntity pg = (IGTPartnerGroupEntity)object;
      IGTPartnerTypeEntity pt = (IGTPartnerTypeEntity)StaticUtils.getFirst(pg.getFieldEntities(IGTPartnerGroupEntity.PARTNER_TYPE));
      return pt.getFieldString(IGTPartnerTypeEntity.PARTNER_TYPE).equals(_partnerType);
    }    
    return true; // Default is to permit object to be included
  } 

  public Collection getOptions(RenderingContext rContext,BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  {
    try
    {
      if( bfpr.getFieldId().equals(IGTAlertTriggerEntity.ALERT_UID) )
      {
        try
        {
          IGTAlertTriggerEntity at = (IGTAlertTriggerEntity)getEntity();
          AlertTriggerAForm form = (AlertTriggerAForm)getActionForm();
          IGTSession gtasSession = at.getSession();
          
          IGTAlertTypeManager alertTypeMgr = (IGTAlertTypeManager)gtasSession.getManager(IGTManager.MANAGER_ALERT_TYPE);
          IGTAlertManager alertMgr = (IGTAlertManager)gtasSession.getManager(IGTManager.MANAGER_ALERT);
          IGTAlertTypeEntity alertType = (IGTAlertTypeEntity)
                                          StaticUtils.getFirst(
                                          alertTypeMgr.getByKey(
                                          form.getAlertType(),
                                          IGTAlertTypeEntity.NAME));      
          Long alertTypeUid = alertType == null ? null : alertType.getUidLong();
          return alertTypeUid == null ? Collections.EMPTY_LIST : alertMgr.getByKey(alertTypeUid, IGTAlertEntity.TYPE);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error providing options for ALERT_UID field",t);
        }
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error providing options",t);
    }
  }

}