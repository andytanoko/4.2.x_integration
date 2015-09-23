/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageTemplateRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 * 2003-02-17     Daniel D'Cotta      Modified to handle either = or . as a
 *                                    separator; use UserId instead of UserName;
 *                                    insert a non-i18n user and role for B-Tier.
 * 2003-04-08     Andrew Hill         Use IGlobals script url constant
 * 2003-05-14     Andrew Hill         Handling for Log messageType
 * 2006-01-06			SC									rename constants to COMMON_FIELDS, LOG_FIELDS and EMAIL_FIELDS 
 *																		add JMS_FIELDS
 *																		add removeMessageProperties method
 *																		in render method, modify code to render 3 cases for message type = log, email, jms.
 *																		add renderMessageProperties method
 */
package com.gridnode.gtas.client.web.alert;

import java.util.*;

import org.apache.struts.action.ActionError;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.Logger;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.be.BusinessEntityAForm;
import com.gridnode.gtas.client.web.be.DomainIdentifierAForm;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.pdip.framework.util.AssertUtil;

public class MessageTemplateRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number COMMON_FIELDS[] =
  { //20030514AH - Array now only contains common fields
    IGTMessageTemplateEntity.NAME,
    IGTMessageTemplateEntity.MESSAGE_TYPE,
    IGTMessageTemplateEntity.MESSAGE,
  };
  
  /* fields when message type = LOG */
  private static final Number[] LOG_FIELDS = 
  { 
    IGTMessageTemplateEntity.LOCATION,
    IGTMessageTemplateEntity.APPEND,
  };
  
  /* fields when message type = EMAIL */
  private static final Number[] EMAIL_FIELDS = 
  { 
    IGTMessageTemplateEntity.CONTENT_TYPE,
    IGTMessageTemplateEntity.FROM_ADDR,
    IGTMessageTemplateEntity.TO_ADDR,
    IGTMessageTemplateEntity.CC_ADDR,
    IGTMessageTemplateEntity.SUBJECT,
  };
  
  private static final Number[] JMS_FIELDS =
  {
  	IGTMessageTemplateEntity.JMS_DESTINATION
  };
  
  private static final Number[] MESSAGE_PROPERTIES_FIELDS =
  {
  	IGTMessagePropertyEntity.KEY,
  	IGTMessagePropertyEntity.TYPE,
  	IGTMessagePropertyEntity.VALUE
  };
 
  private static final String USER = "USER";
  private static final String ROLE = "ROLE";

  protected String _i18nUser;
  protected String _i18nRole;

  public MessageTemplateRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
    _i18nUser = rContext.getResourceLookup().getMessage("messageTemplate.substitutionList.user");
    _i18nRole = rContext.getResourceLookup().getMessage("messageTemplate.substitutionList.role");
  }

  protected void render() throws RenderingException
  {
    try
    {
      //RenderingContext rContext = getRenderingContext();
      MessageTemplateAForm form = (MessageTemplateAForm)getActionForm();
      IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity)getEntity();

      renderCommonFormElements(messageTemplate.getType(), _edit);
      /* render diversion link */
      String messageType = form.getMessageType();
      if (_edit && IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
      {
      	renderLabel("jmsDestination_create","messageTemplate.jmsDestination.create",false);
      }
      
      BindingFieldPropertyRenderer bfpr = renderFields(null, messageTemplate, COMMON_FIELDS);
      if (IGTMessageTemplateEntity.MSG_TYPE_LOG.equals(messageType))
      {
      	removeFields(messageTemplate, EMAIL_FIELDS, null);
      	removeFields(messageTemplate, JMS_FIELDS, null);
      	renderFields(bfpr, messageTemplate, LOG_FIELDS);
      }
      else if (IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
      {
      	removeFields(messageTemplate, LOG_FIELDS, null);
      	removeFields(messageTemplate, EMAIL_FIELDS, null);
      	if (_edit)
    		{
    	    includeJavaScript(IGlobals.JS_PROPERTY);
    		}
      	renderFields(bfpr, messageTemplate, JMS_FIELDS);
      }
      else /* this is for case where message type = email or mesasge type is empty */
      {
      	removeFields(messageTemplate, LOG_FIELDS, null);
      	removeFields(messageTemplate, JMS_FIELDS, null);
      	renderFields(bfpr, messageTemplate, EMAIL_FIELDS);
      }
      
      /* message properties */
      if (IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
      {
      	renderMessageProperties();
      } else
      {
      	removeMessageProperties();
      }

      if(_edit)
      {
        renderLabel("actions_create", "messageTemplate.action.create", false);
        
        // Substitution List
        renderLabel("object_label", "messageTemplate.substitutionList.object");
        renderLabel("field_label", "messageTemplate.substitutionList.field");
        renderLabel("insert_button", "messageTemplate.substitutionList.insert");

        includeJavaScript(IGlobals.JS_SUBSTITUTION_UTILS); //20030408AH

        IGTSession gtasSession = messageTemplate.getSession();
        IGTMessageTemplateManager manager = (IGTMessageTemplateManager)gtasSession.getManager(IGTManager.MANAGER_MESSAGE_TEMPLATE);
        Map substitutionList =  manager.getSubstitutionList();

        String selectedObject = trimObject(form.getObject());
        String selectedField = trimField(form.getField());

        /**
         * Use the below formating for insertion
         *  <#USER=userId#>
         *  <#ROLE=roleId#>
         *  <%SubstitutionObject.SubstitutionField%>
         *
         * Note: Need to insert a non-i18n user & role for B-Tier
         */
        final char symbol1; // store either # or %
        final char symbol2; // store either = or .

        // Object options
        Collection objectValues = new HashSet(substitutionList.keySet());
        objectValues.add(_i18nUser);
        objectValues.add(_i18nRole);

        // Field options
        Collection fieldValues;
        if(USER.equals(selectedObject))
        {
          fieldValues = getUserIdCollection(gtasSession);
          symbol1 = '#';
          symbol2 = '=';
        }
        else if(ROLE.equals(selectedObject))
        {
          fieldValues= getRoleNameCollection(gtasSession);
          symbol1 = '#';
          symbol2 = '=';
        }
        else
        {
          fieldValues = (Collection)substitutionList.get(selectedObject);
          symbol1 = '%';
          symbol2 = '.';
        }

        // Object combo box
        IOptionValueRetriever objectRetriever = new IOptionValueRetriever()
        {
          public String getOptionText(Object choice) throws GTClientException
          {
            return (String)choice;
          }

          public String getOptionValue(Object choice) throws GTClientException
          {
            // Use non-i18n for value
            if(_i18nUser.equals(choice))
              choice = USER;
            else if(_i18nRole.equals(choice))
              choice = ROLE;

            return "<" + symbol1 + (String)choice;
          }
        };

        renderSelectOptions("object_value", objectValues, objectRetriever, true, "");
        renderSelectedOptions("object_value", "<" + symbol1 + selectedObject, false);

        // Field combo box
        IOptionValueRetriever fieldRetriever = new IOptionValueRetriever()
        {
          public String getOptionText(Object choice) throws GTClientException
          {
            return (String)choice;
          }

          public String getOptionValue(Object choice) throws GTClientException
          {
            return symbol2 + (String)choice + symbol1 + ">";
          }
        };

        renderSelectOptions("field_value", fieldValues, fieldRetriever, true, "");
        renderSelectedOptions("field_value", symbol2 + selectedField + symbol1 + ">", false);
        
        
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering messageTemplate screen",t);
    }
  }

  protected Collection getUserIdCollection(IGTSession gtasSession) throws GTClientException
  {
    IGTUserManager manager = (IGTUserManager)gtasSession.getManager(IGTManager.MANAGER_USER);
    Collection users =  manager.getAll();

    Collection userIds = new Vector(users.size());

    Iterator i = users.iterator();
    while(i.hasNext())
    {
      String userId = ((IGTUserEntity)i.next()).getFieldString(IGTUserEntity.USER_ID);
      userIds.add(userId);
    }
    return userIds;
  }

  protected Collection getRoleNameCollection(IGTSession gtasSession) throws GTClientException
  {
    IGTRoleManager manager = (IGTRoleManager)gtasSession.getManager(IGTManager.MANAGER_ROLE);
    Collection roles =  manager.getAll();

    Collection roleNames = new Vector(roles.size());

    Iterator i = roles.iterator();
    while(i.hasNext())
    {
      String roleName = ((IGTRoleEntity)i.next()).getFieldString(IGTRoleEntity.ROLE);
      roleNames.add(roleName);
    }
    return roleNames;
  }

  protected String trimObject(String str)
  { // remove "<#" or "<%" from the front
    if(str != null && str.length() > 2)
      str = str.substring(2, str.length());
    return str;
  }

  protected String trimField(String str)
  { // remove "=" or "." from the front
    if(str != null && str.length() > 1)
      str = str.substring(1, str.length());

    // remove "#>"or "%>" from the back
    if(str != null && str.length() > 2)
      str = str.substring(0, str.length() - 2);
    return str;
  }
  
  protected void renderMessageProperties() throws Throwable
  {
    RenderingContext rContext = getRenderingContext();
    IGTMessageTemplateEntity entity = (IGTMessageTemplateEntity) getEntity();
    MessageTemplateAForm form = (MessageTemplateAForm) rContext.getOperationContext().getActionForm();

    renderLabel("messageProperties.key_label",         "messageProperties.key");
    renderLabel("messageProperties.type_label",         "messageProperties.type");
    renderLabel("messageProperties.value_label",         "messageProperties.value");

//    renderLabelCarefully("addDomainIdentifier",        "domainIdentifier.addDomainIdentifier",     false);
//    renderLabelCarefully("removeDomainIdentifier",     "domainIdentifier.removeDomainIdentifier",  false);

    Node mpRow = getElementById("messageProperties_row");
    Node mpRowParent = mpRow.getParentNode();
    mpRowParent.removeChild(mpRow);
    Node controlRow = getElementById("control_row");
    
    Collection mpCollection = (Collection) entity.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
    
    IGTMessagePropertyEntity[] mps = (IGTMessagePropertyEntity[]) mpCollection.toArray(new IGTMessagePropertyEntity[mpCollection.size()]);
    MessagePropertyAForm[] mpForms = form.getMessageProperties();
    for(int i = 0; i < mpForms.length; i++)
    {
      try
      {
        Node clonedRow = mpRow.cloneNode(true);
        mpRowParent.insertBefore(clonedRow, controlRow);
        
        // Render MessageProperty
        IGTMessagePropertyEntity mp = (IGTMessagePropertyEntity) mps[i];
        MessagePropertyAForm mpForm = mpForms[i];

        renderFields(null, mp, MESSAGE_PROPERTIES_FIELDS, mpForm, "");

        // Make id unique for each row
        String idPrefix = "messageProperties[" + i + "].";

        if(_edit)
        {
          Element selectCol = getElementById("selected_value");
          selectCol.setAttribute("id", idPrefix + "selected_value");
          selectCol.setAttribute("name", idPrefix + "selected");

          if(mpForm.isSelected())
          {
            selectCol.setAttribute("checked", "checked");
          }
        }
        
        Element keyCol = getElementById("key_value");
        keyCol.setAttribute("id", idPrefix + "key_value");
        keyCol.setAttribute("name", idPrefix + "key");
        
        Element typeCol = getElementById("type_value");
        typeCol.setAttribute("id", idPrefix + "type_value");
        typeCol.setAttribute("name", idPrefix + "type");
        
        Element valueCol = getElementById("value_value");
        valueCol.setAttribute("id", idPrefix + "value_value");
        valueCol.setAttribute("name", idPrefix + "value");

        // 20040512 DDJ: manually display error
        if (_edit)
        {
          Element errorCol = getElementById("messageProperties_error");
          errorCol.setAttribute("id", idPrefix + "messageProperties_error");

          ActionError error = MessageUtils.getFirstError(rContext.getActionErrors(), MessageTemplateHelper.getMessagePropertyError(i)); 
          if(error != null)
          {
            //String errorMsg = rContext.getResourceLookup().getMessage(error.getKey());
            String errorMsg = rContext.getResourceLookup().getMessage("messageProperty.error.keyAndTypeAndValue.required");
            AssertUtil.assertTrue(errorMsg != null);
            replaceMultilineText(errorCol, errorMsg);
            errorCol.setAttribute("class", "errortext");
          }
        }
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering message properties, i=" + i, t);
      }
    }
  }
  
  private void removeMessageProperties() throws RenderingException
  {
		removeNode("messagePropertyRoot", true);
  }
  
  private void log(String message)
	{
		Logger.log("[MessageTemplateRenderer] " + message);
	}
}