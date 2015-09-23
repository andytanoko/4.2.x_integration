/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 3, 2006    Tam Wei Xiang       Created
 * Feb 28 2006    Neo Sok Lay         Add Serial version UID
 */
package com.gridnode.pdip.app.alert.jms;

import java.util.Collection;
import java.util.Vector;

import com.gridnode.pdip.app.alert.model.MessageProperty;
import com.gridnode.pdip.framework.db.DataObject;

/**
 * The value object which is used to hold the value for the jms messages
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class MessageData
	extends DataObject
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -4495165984125160852L;
	private Vector<MessageProperty> _msgProperties = new Vector<MessageProperty>();
	private String _message;
	private int _retryCount;
	
	public MessageData() {}

	public String getMessage()
	{
		return _message;
	}

	public Vector<MessageProperty> getMsgProperties()
	{
		return _msgProperties;
	}

	public int getRetryCount()
	{
		return _retryCount;
	}

	public void setMessage(String _message)
	{
		this._message = _message;
	}

	public void setMsgProperties(Collection<MessageProperty> properties)
	{
		_msgProperties.addAll(properties);
	}

	public void setRetryCount(int count)
	{
		_retryCount = count;
	}
}
