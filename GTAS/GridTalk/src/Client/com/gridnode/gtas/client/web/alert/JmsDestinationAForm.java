/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 28 Dec 2005		SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.be.DomainIdentifierAForm;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class JmsDestinationAForm extends GTActionFormBase
{
	/* normal fields */
	private String name;
	private String type;
	private String jndiName;
	private String deliveryMode;
	private String priority;
	private String connectionFactoryJndi;
	private String connectionUser;
	private String connectionPassword;
	private String retryInterval;
	private String maximumRetries;
	
	/* other fields */
	private ArrayList lookupProperties;
	private String updateAction;
	private boolean isNewEntity;
	
	public JmsDestinationAForm()
	{
		lookupProperties = new ArrayList();
	}
	
	/* unchecked the html checkbox */
	public void doReset(ActionMapping mapping, HttpServletRequest request)
	{
		if (lookupProperties != null)
		{
			Iterator it = lookupProperties.iterator();
			while (it.hasNext())
			{
				LookupPropertiesAForm form = (LookupPropertiesAForm) it.next();
				form.doReset(mapping, request);
			}
		}
	}

	/* getter/setter methods */
	
	public String getConnectionFactoryJndi()
	{
		return connectionFactoryJndi;
	}

	public void setConnectionFactoryJndi(String connectionFactoryJndi)
	{
		this.connectionFactoryJndi = connectionFactoryJndi;
	}

	public String getConnectionPassword()
	{
		return connectionPassword;
	}

	public void setConnectionPassword(String connectionPassword)
	{
		this.connectionPassword = connectionPassword;
	}

	public String getConnectionUser()
	{
		return connectionUser;
	}

	public void setConnectionUser(String connectionUser)
	{
		this.connectionUser = connectionUser;
	}

	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public String getJndiName()
	{
		return jndiName;
	}

	public void setJndiName(String jndiName)
	{
		this.jndiName = jndiName;
	}

	public String getMaximumRetries()
	{
		return maximumRetries;
	}

	public void setMaximumRetries(String maximumRetries)
	{
		this.maximumRetries = maximumRetries;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getRetryInterval()
	{
		return retryInterval;
	}

	public void setRetryInterval(String retryInterval)
	{
		this.retryInterval = retryInterval;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getUpdateAction()
	{
		return updateAction;
	}

	public void setUpdateAction(String updateAction)
	{
		this.updateAction = updateAction;
	}
	
	public boolean isNewEntity()
	{
		return isNewEntity;
	}

	public void setNewEntity(boolean isNewEntity)
	{
		this.isNewEntity = isNewEntity;
	}

	/* END: getter/setter methods */
	
	/* lookupProperties methods */
	public LookupPropertiesAForm[] getLookupProperties()
  {
    int size = lookupProperties.size();
    return (LookupPropertiesAForm[]) lookupProperties.toArray(new LookupPropertiesAForm[size]);
  }

  public void setLookupProperties(LookupPropertiesAForm[] forms)
  {
    lookupProperties = StaticUtils.arrayListValue(forms);
  }
  
  public void addNewLookupPropertiesAForm()
  {
  	lookupProperties.add(new LookupPropertiesAForm());
  }
  
  public void removeSelectedLookupProperties()
  {
    if (lookupProperties == null) 
    {
    	return;
    }
    Iterator it = lookupProperties.iterator();
    while(it.hasNext())
    {
      LookupPropertiesAForm form = (LookupPropertiesAForm) it.next();
      if (form.isSelected())
      {
      	it.remove();
      }
    }
  }

}
