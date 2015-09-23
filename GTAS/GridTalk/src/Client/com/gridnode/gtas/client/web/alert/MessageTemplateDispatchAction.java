/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: MessageTemplateDispatchAction.java
 * 
 * ***************************************************************************
 * Date                  Author                   Changes
 * ***************************************************************************
 * 2003-02-06            Daniel D'Cotta           Created 
 * 2003-02-17            Neo Sok Lay              Inversed FROM_ADDR,
 *                                                TO_ADDR. 
 * 2003-05-14            Andrew Hill 							Support for log messageType 
 * 2005-01-05 					 SC 											In
 *																							  initialiseActionForm method: add code for jms destination uid, message
 *  																						  properties
																								  
 *																								add method getMessagePropertyArray(IGTMessageTemplateEntity entity)
 *																								  
 *																							  add method validateMesageProperty(MessagePropertiesAForm[] forms,
 *																								ActionErrors actionErrors)
 *
 *																								in validateActionForm method: add code to validate jms destination uid,
 *																								message properties
 *																								  
 *																								in updateEntityFields method: add code for jms destination uid, message
 *																								properties
 *																								  
 *																								add ADD_CONDITION, REMOVE_CONDITION fields
 *																								  
 *																								add method for updateAction field
 * 2005-01-13						SC												Fix validateMesageProperty method
 */
package com.gridnode.gtas.client.web.alert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityManager;
import com.gridnode.gtas.client.ctrl.IGTDomainIdentifierEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTMessagePropertyEntity;
import com.gridnode.gtas.client.ctrl.IGTMessageTemplateEntity;
import com.gridnode.gtas.client.ctrl.IGTMessageTemplateManager;
import com.gridnode.gtas.client.utils.Logger;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.be.BusinessEntityAForm;
import com.gridnode.gtas.client.web.be.DomainIdentifierAForm;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.pdip.framework.util.StringUtil;

public class MessageTemplateDispatchAction extends EntityDispatchAction2
{
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_MESSAGE_TEMPLATE;
	}

	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new MessageTemplateRenderer(rContext, edit);
	}

	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		return (edit ? IDocumentKeys.MESSAGE_TEMPLATE_UPDATE
								: IDocumentKeys.MESSAGE_TEMPLATE_VIEW);
	}

	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new MessageTemplateAForm();
	}

	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_MESSAGE_TEMPLATE;
	}

	/* entity processing methods */
	protected void initialiseNewEntity(	ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity) entity;
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES,
																	new Vector());
	}

	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity) entity;

		MessageTemplateAForm form = (MessageTemplateAForm) actionContext
				.getActionForm();
		form.setName(messageTemplate.getFieldString(IGTMessageTemplateEntity.NAME));
		form.setContentType(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.CONTENT_TYPE));
		form.setMessageType(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.MESSAGE_TYPE));
		form.setFromAddr(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.FROM_ADDR));
		form.setToAddr(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.TO_ADDR));
		form.setCcAddr(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.CC_ADDR));
		form.setSubject(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.SUBJECT));
		form.setMessage(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.MESSAGE));
		form.setLocation(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.LOCATION)); // 20030514AH
		form.setAppend(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.APPEND)); // 20030514AH
		form.setJmsDestination(messageTemplate
				.getFieldString(IGTMessageTemplateEntity.JMS_DESTINATION));

		/* MessageProperty */
		if (entity.isNewEntity() == false)
		{
			int noOfMessageProperty = getMessagePropertyArray(messageTemplate).length;
			for (int i = 0; i < noOfMessageProperty; i++)
			{
				form.addNewMessageProperty();
			}
		}

		IGTMessagePropertyEntity[] mps = getMessagePropertyArray(messageTemplate);
		MessagePropertyAForm[] mpForms = form.getMessageProperties();
		for (int i = 0; i < mps.length; i++)
		{
			mpForms[i].setKey(mps[i].getFieldString(IGTMessagePropertyEntity.KEY));
			mpForms[i].setType(mps[i].getFieldString(IGTMessagePropertyEntity.TYPE));
			mpForms[i]
					.setValue(mps[i].getFieldString(IGTMessagePropertyEntity.VALUE));
		}
	}

	protected void performUpdateProcessing(ActionContext actionContext) throws GTClientException
	{
		IGTMessageTemplateEntity entity = (IGTMessageTemplateEntity) getEntity(actionContext);
		MessageTemplateAForm form = (MessageTemplateAForm) actionContext
				.getActionForm();
		if (form == null) { return; }

		String updateAction = form.getUpdateAction();
		if (IGlobals.ADD_UPDATE_ACTION.equals(updateAction))
		{
			addNewMessageProperty(entity, form);
		}
		else if (IGlobals.REMOVE_UPDATE_ACTION.equals(updateAction))
		{
			removeSelectedMessageProperties(entity, form);
		}

		form.setUpdateAction(null);
	}

	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		IGTMessageTemplateEntity messageTemplate = (IGTMessageTemplateEntity) entity;
		MessageTemplateAForm form = (MessageTemplateAForm) actionContext
				.getActionForm();
		
		messageTemplate
				.setFieldValue(IGTMessageTemplateEntity.NAME, form.getName());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.CONTENT_TYPE, form
				.getContentType());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.MESSAGE_TYPE, form
				.getMessageType());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.FROM_ADDR, form
				.getFromAddr());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.TO_ADDR, form
				.getToAddr());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.CC_ADDR, form
				.getCcAddr());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.SUBJECT, form
				.getSubject());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.MESSAGE, form
				.getMessage());
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.LOCATION, form
				.getLocation()); // 20030514AH
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.APPEND, StaticUtils
				.booleanValue(form.getAppend())); // 20030514AH
		String jmsDestination = form.getJmsDestination();
		if (StringUtil.isNotEmpty(jmsDestination))
		{
			messageTemplate.setFieldValue(IGTMessageTemplateEntity.JMS_DESTINATION,
																		new Long(jmsDestination));
		}

		/* MessageProperty */
		IGTMessagePropertyEntity[] mps = getMessagePropertyArray(messageTemplate);
		MessagePropertyAForm[] mpForms = form.getMessageProperties();
		Vector resultMps = new Vector();
		Vector resultMpForms = new Vector();
		for (int i = 0; i < mpForms.length; i++)
		{
			String key = mpForms[i].getKey();
			String type = mpForms[i].getType();
			String value = mpForms[i].getValue();

			if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(type)
					&& StringUtil.isNotEmpty(value))
			{
				IGTMessagePropertyEntity mp = mps[i];
				mp.setFieldValue(IGTMessagePropertyEntity.KEY, key);
				mp.setFieldValue(IGTMessagePropertyEntity.TYPE, Integer.parseInt(type));
				mp.setFieldValue(IGTMessagePropertyEntity.VALUE, value);
				resultMps.add(mp);
				resultMpForms.add(mpForms[i]);
			}
		}
		int size = resultMps.size();
		form.setMessageProperties((MessagePropertyAForm[]) resultMpForms
				.toArray(new MessagePropertyAForm[size]));
		messageTemplate.setFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES,
																	resultMps);
	}

	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm actionForm,
																		ActionErrors errors) throws GTClientException
	{
		// IGTMessageTemplateEntity messageTemplate =
		// (IGTMessageTemplateEntity)entity;
		MessageTemplateAForm form = (MessageTemplateAForm) actionForm;

		basicValidateString(errors, IGTMessageTemplateEntity.NAME, form, entity);
		basicValidateString(errors, IGTMessageTemplateEntity.MESSAGE_TYPE, form,
												entity);
		basicValidateString(errors, IGTMessageTemplateEntity.MESSAGE, form,
												entity);

		// String messageType = form.getMessageType();
		String messageType = form.getMessageType();
		if (IGTMessageTemplateEntity.MSG_TYPE_JMS.equals(messageType))
		{
			log("validate jms");
			basicValidateString(errors, IGTMessageTemplateEntity.JMS_DESTINATION,
													form, entity);
			validateMesageProperty(form.getMessageProperties(), errors);
		}
		else if (IGTMessageTemplateEntity.MSG_TYPE_LOG.equals(messageType))
		{ // 20030514AH
			log("validate log");
			basicValidateString(errors, IGTMessageTemplateEntity.LOCATION, form,
													entity);
			basicValidateString(errors, IGTMessageTemplateEntity.APPEND, form, entity);
		}
		else if (IGTMessageTemplateEntity.MSG_TYPE_EMAIL.equals(messageType))
		{ // 20030514AH
			log("validate email");
			basicValidateString(errors, IGTMessageTemplateEntity.CONTENT_TYPE, form,
													entity);
			basicValidateString(errors, IGTMessageTemplateEntity.FROM_ADDR, form,
													entity);
			basicValidateString(errors, IGTMessageTemplateEntity.TO_ADDR, form,
													entity);
			basicValidateString(errors, IGTMessageTemplateEntity.CC_ADDR, form,
													entity);
			basicValidateString(errors, IGTMessageTemplateEntity.SUBJECT, form,
													entity);
		}
		else
		{
			log("not type specified");
		}
	}

	// Helper method
	private IGTMessagePropertyEntity[] getMessagePropertyArray(IGTMessageTemplateEntity entity) throws GTClientException
	{
		Collection c = (Collection) entity
				.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
		return (IGTMessagePropertyEntity[]) c
				.toArray(new IGTMessagePropertyEntity[c.size()]);
	}

	private void validateMesageProperty(MessagePropertyAForm[] forms,
																			ActionErrors actionErrors)
	{
		for (int i = 0; i < forms.length; i++)
		{
			boolean keyEmpty = StringUtil.isEmpty(forms[i].getKey());
			boolean typeEmpty = StringUtil.isEmpty(forms[i].getType());
			boolean valueEmpty = StringUtil.isEmpty(forms[i].getValue());
			
			if (keyEmpty || typeEmpty || valueEmpty)
			{
				log("validateMesageProperty fails: " + i);
				EntityFieldValidator
						.addFieldError(actionErrors,
														MessageTemplateHelper.getMessagePropertyError(i),
														IGTEntity.ENTITY_MESSAGE_TEMPLATE,
														FieldValidator.REQUIRED, null);
			}
		}
	}

	private void addNewMessageProperty(	IGTMessageTemplateEntity entity,
																			MessageTemplateAForm form) throws GTClientException
	{
		// Add 1 new MessageProperty to the entity
		IGTMessageTemplateManager manager = (IGTMessageTemplateManager) entity
				.getSession().getManager(IGTManager.MANAGER_MESSAGE_TEMPLATE);
		Collection mps = (Collection) entity
				.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
		mps.add(manager.newMessageProperty());
		entity.setFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES, mps);

		// Add 1 new MessageProperty to the action form
		form.addNewMessageProperty();
	}

	private void removeSelectedMessageProperties(	IGTMessageTemplateEntity entity,
																								MessageTemplateAForm form) throws GTClientException
	{
		// Remove selected message properties to the entity
		Collection mps = (Collection) entity
				.getFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES);
		MessagePropertyAForm[] mpForms = form.getMessageProperties();

		if (mpForms == null) { return; }

		Iterator it = mps.iterator();
		for (int i = 0; i < mpForms.length; i++)
		{
			it.next();
			if (mpForms[i].isSelected())
			{
				it.remove();
			}
		}
		entity.setFieldValue(IGTMessageTemplateEntity.MESSAGE_PROPERTIES, mps);

		// Remove selected message properties to the action form
		form.removeSelectedMessageProperties();
	}
	
	private void log(String message)
	{
		Logger.log("[MessageTemplateDispatchAction] " + message);
	}
}