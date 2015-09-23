/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageProperty.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 *
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class MessageProperty
	extends AbstractEntity
	implements IMessageProperty
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _key;
	private Integer _type;
	private String _value;
	
	public MessageProperty() {}
	
	public MessageProperty(String key, Integer type, String value)
	{
		_key = key;
		_type = type;
		_value = value;
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
		return new StringBuffer(_key).append(" ").append(_type).append(" ").append(_value).toString();
	}
	
	/**
	 * Getter and Setter for the attributes
	 */
	public Number getKeyId()
	{
		return UID;
	}
	
	public String getPropertyKey()
	{
		return _key;
	}

	public Integer getType()
	{
		return _type;
	}

	public String getValue()
	{
		return _value;
	}

	public void setPropertyKey(String _key)
	{
		this._key = _key;
	}

	public void setType(Integer _type)
	{
		this._type = _type;
	}

	public void setValue(String _value)
	{
		this._value = _value;
	}
	
	
	
}
