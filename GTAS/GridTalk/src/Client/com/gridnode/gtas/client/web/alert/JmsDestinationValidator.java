/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 30 Dec 2005		SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTJmsDestinationEntity;
import com.gridnode.gtas.client.utils.Logger;
import com.gridnode.gtas.client.web.be.BusinessEntityAForm;
import com.gridnode.gtas.client.web.be.DomainIdentifierAForm;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;
import com.gridnode.pdip.framework.util.AssertUtil;
import com.gridnode.pdip.framework.util.StringUtil;

public class JmsDestinationValidator
{
	private ActionContext actionContext;
	private IGTJmsDestinationEntity entity;
	private JmsDestinationAForm form;
	private ActionErrors actionErrors;
	
	public JmsDestinationValidator(ActionContext actionContext, IGTJmsDestinationEntity entity,
																	ActionForm form, ActionErrors actionErrors)
	{
		this.actionContext = actionContext;
		this.entity = entity;
		this.form = (JmsDestinationAForm) form;
		this.actionErrors = actionErrors;
	}
	
	public void validate() throws GTClientException
	{
		validateName();
    basicValidateString(actionErrors, IGTJmsDestinationEntity.TYPE, form, entity);
    validateJndiName();
    basicValidateString(actionErrors, IGTJmsDestinationEntity.DELIVERY_MODE, form, entity);
    basicValidateString(actionErrors, IGTJmsDestinationEntity.PRIORITY, form, entity);
    validateConnectionFactoryJndi();
    validateUsernameAndPassword();
    basicValidateString(actionErrors, IGTJmsDestinationEntity.RETRY_INTERVAL, form, entity);
    basicValidateString(actionErrors, IGTJmsDestinationEntity.MAXIMUM_RETRIES, form, entity);
    validateLookupProperties();
	}
	
	private void validateName() throws GTClientException
  {
  	boolean result = basicValidateString(actionErrors, IGTJmsDestinationEntity.NAME, form, entity);
    if (result)
    {
    	String name = form.getName();
    	if (containsWordCharactersOnly(name) == false)
    	{
    		addInvalidError(IGTJmsDestinationEntity.NAME);
    	}
    }
  }
  
  private void validateJndiName() throws GTClientException
  {
  	boolean result = basicValidateString(actionErrors, IGTJmsDestinationEntity.JNDI_NAME, form, entity);
  	if (result)
    {
    	String jndiName = form.getJndiName();
    	if (isValidJndi(jndiName) == false)
    	{
    		addInvalidError(IGTJmsDestinationEntity.JNDI_NAME);
    	}
    }
  }
  
  private void validateConnectionFactoryJndi() throws GTClientException
  {
  	boolean result = basicValidateString(actionErrors, IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI, form, entity);
  	if (result)
    {
    	String value = form.getConnectionFactoryJndi();
    	if (isValidJndi(value) == false)
    	{
    		addInvalidError(IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI);
    	}
    }
  }
  
  private void addInvalidError(Number fieldId) throws GTClientException
  {
  	String fieldName = entity.getFieldName(fieldId);
  	EntityFieldValidator.addFieldError(actionErrors, fieldName,
																				IGTEntity.ENTITY_JMS_DESTINATION,
																				FieldValidator.INVALID, null);
  }
  

  /**
   * Returns true if str contains only characcters from \w character class. See Pattern class for more info.
   */
  private boolean containsWordCharactersOnly(String str)
  {
  	AssertUtil.assertTrue(str != null);
		int length = str.length();
		Pattern p = Pattern.compile("\\w{" + length + "}+");
		Matcher m = p.matcher(str);
		return m.matches();
  }
  
  /** 
   * returns true if str is any string combination of [a-z][A-Z][0-9] and underscore (_), backslash (/) colon (:) and space 
   */
  private boolean isValidJndiHelper1(String str)
  {
  	AssertUtil.assertTrue(str != null);
  	int length = str.length();
		Pattern p = Pattern.compile("[\\w\\:\\ /]{" + length + "}+");
		Matcher m = p.matcher(str);
		return m.matches();
  }
  
  /**
   * returns true if 
   *   If ':' is specified, there should be only one occurrence and not in the first or last position.
   */
  private boolean isValidJndiHelper2(String str)
  {
  	AssertUtil.assertTrue(str != null);
  	Pattern p = Pattern.compile("(\\A[^:][^:]*[:]{1}[^:]*[^:]\\z)|([^:]*)");
		Matcher m = p.matcher(str);
		return m.matches();
  }
  
  private boolean isValidJndi(String str)
  {
  	return isValidJndiHelper1(str) && isValidJndiHelper2(str);
  }
  
  protected boolean basicValidateString(ActionErrors actionErrors,
																				Number fieldId,
																				ActionForm form,
																				IGTEntity entity) throws GTClientException
	{
		return EntityFieldValidator.basicValidateString(actionErrors, fieldId,
																										form, entity);
	}
  
  private void validateUsernameAndPassword() throws GTClientException
  {
  	basicValidateString(actionErrors, IGTJmsDestinationEntity.CONNECTION_USER, form, entity);
  	basicValidateString(actionErrors, IGTJmsDestinationEntity.CONNECTION_PASSWORD, form, entity);
  	String username = form.getConnectionUser();
  	String password = form.getConnectionPassword();
  	if ((StringUtil.isNotEmpty(username) && StringUtil.isEmpty(password))
  			||
  			(StringUtil.isEmpty(username) && StringUtil.isNotEmpty(password)))
  	{
  		addInvalidError(IGTJmsDestinationEntity.CONNECTION_USER);
  		addInvalidError(IGTJmsDestinationEntity.CONNECTION_PASSWORD);
  	}
  }
  
  private void validateLookupProperties()
  {
  	LookupPropertiesAForm[] lookupPropertiesForms = form.getLookupProperties();
    for (int i = 0; i < lookupPropertiesForms.length; i++)
    {
      boolean nameEmpty = StringUtil.isEmpty(lookupPropertiesForms[i].getLp_name());
      boolean valueEmpty = StringUtil.isEmpty(lookupPropertiesForms[i].getLp_value());
      
      if (nameEmpty || valueEmpty)
      {
      	EntityFieldValidator.addFieldError(actionErrors, "lookupProperties[" + i + "].error.nameAndValue.required", entity.getType(), FieldValidator.REQUIRED, null);
      }
    }  
  }
  
  private void log(String message)
  {
  	Logger.log("[JmsDestinationValidator] " + message);
  }
}
