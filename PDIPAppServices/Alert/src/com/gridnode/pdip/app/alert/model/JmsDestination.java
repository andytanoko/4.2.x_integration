/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestination.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 * Feb 28 2006    Neo Sok Lay         Add Serial Version UID
 */
package com.gridnode.pdip.app.alert.model;

import java.util.Properties;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * The value obj for JmsDestinationBean.
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsDestination
	extends AbstractEntity
	implements IJmsDestination
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -2334551801892072383L;
	private String _name;
	private Integer _type;
	private String _jndiName;
	private Integer _deliveryMode;
	private Integer _priority;
	private String _connectionFactoryJndi;
	private String _connectionUser;
	private String _connectionPassword;
	private Properties _lookupProperties = new Properties();
	private Integer _retryInterval;
	private Integer _maximumRetries;
	
	public JmsDestination() {}
	
	public JmsDestination(String name, Integer type, String jndiName, Integer deliveryMode,
	                      Integer priority, String connectionFactoryJndi, String connectionUser, 
	                      String connectionPassword, Properties lookupProperties, Integer retryInterval, 
	                      Integer maximumRetries)
	{
		_name = name;
		_type = type;
		_jndiName = jndiName;
		_deliveryMode = deliveryMode;
		_priority = priority;
		_connectionFactoryJndi = connectionFactoryJndi;
		_connectionUser = connectionUser;
		_connectionPassword = connectionPassword;
		_lookupProperties = lookupProperties;
		_retryInterval = retryInterval;
		_maximumRetries = maximumRetries;
	}
	
	/**
	 * Method from abstract entity
	 */
	public String getEntityName()
	{
		return ENTITY_NAME;
	}
	
	public String getEntityDescr()
	{
		return new StringBuffer(_name).toString();
	}
	
	public Number getKeyId()
	{
		return UID;
	}
	
	/**
	 * Getter and Setter for the attributes
	 */
	public String getConnectionFactoryJndi()
	{
		return _connectionFactoryJndi;
	}
	public String getConnectionPassword()
	{
		return _connectionPassword;
	}
	public String getConnectionUser()
	{
		return _connectionUser;
	}
	public Integer getDeliveryMode()
	{
		return _deliveryMode;
	}
	public String getJndiName()
	{
		return _jndiName;
	}
	public Properties getLookupProperties()
	{
		return _lookupProperties;
	}
	public Integer getMaximumRetries()
	{
		return _maximumRetries;
	}
	public String getName()
	{
		return _name;
	}
	public Integer getPriority()
	{
		return _priority;
	}
	public Integer getRetryInterval()
	{
		return _retryInterval;
	}
	public Integer getType()
	{
		return _type;
	}
	
	public void setConnectionFactoryJndi(String factoryJndi)
	{
		_connectionFactoryJndi = factoryJndi;
	}
	public void setConnectionPassword(String password)
	{
		_connectionPassword = password;
	}
	public void setConnectionUser(String user)
	{
		_connectionUser = user;
	}
	public void setDeliveryMode(Integer mode)
	{
		_deliveryMode = mode;
	}
	public void setJndiName(String name)
	{
		_jndiName = name;
	}
	public void setLookupProperties(Properties properties)
	{
		_lookupProperties = properties;
	}
	public void setMaximumRetries(Integer retries)
	{
		_maximumRetries = retries;
	}
	public void setName(String _name)
	{
		this._name = _name;
	}
	public void setPriority(Integer _priority)
	{
		this._priority = _priority;
	}
	public void setRetryInterval(Integer interval)
	{
		_retryInterval = interval;
	}
	public void setType(Integer _type)
	{
		this._type = _type;
	}
}
