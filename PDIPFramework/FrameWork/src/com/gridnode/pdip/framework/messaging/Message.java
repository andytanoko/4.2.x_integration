/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Message.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 26 2003    Jagadeesh           Created
 * Mar 01 2007    Tam Wei Xiang       Added httpResponse code into Message 
 */

package com.gridnode.pdip.framework.messaging;

/**
 * Within a general form of definition :
 * -------------------------------------
 *
 * A Message is an entity that is composed of following Parts
 *
 * 		1.Header :  All Messages support a set of Header fields. Header fields
 * 							  contain Header values that are used to identify and route
 * 							  the message.
 *
 * 		2.PayLoad : A Message defines two types of payload(in context specific use).
 * 								A collection of Files, or processed payLoad in the form of byte[].
 *
 *
 * 		3.Legacy MetaInfo: A Message can Optionally contain Legacy MetaInfo, or info
 * 											 required when transacting with (GT1.x/GM).
 *
 *
 * @author Jagadeesh
 * @since 2.3
 *
 */

import java.util.Map;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashMap;
import java.util.Collections;

import java.io.File;
import java.io.Serializable;

public class Message implements IMessage, Serializable
{ 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9184120732138682020L;
	private Map _headers = new HashMap();
	private Map _messageAttributes = new HashMap();
  private Integer _httpResponseCode;

	private String[] _data = {
	};

	private File[] _payLoad = {
	};

	private byte[] _payLoadData = {
	};

	public Message()
	{
	}

	/*** Setter Methods ***/

	public void setCommonHeaders(Map commonHeaders)
	{
		_headers.put(IHeaderCategory.CATEGORY_COMMON_HEADERS, commonHeaders);
	}

	public void setMessageHeaders(Map messageHeaders)
	{
		_headers.put(IHeaderCategory.CATEGORY_MESSAGE_HEADERS, messageHeaders);
	}

	public void setData(String[] data)
	{
		_data = data;
	}

	public void setPayLoad(File[] payLoad)
	{
		_payLoad = payLoad;
	}

	public void setPayLoad(byte[] payLoad)
	{
		_payLoadData = payLoad;
	}
	
  public void setHttpResponseCode(Integer responseCode)
  {
    _httpResponseCode = responseCode;
  }
  
	/************** Getter Methods  *****/

	public Map getCommonHeaders()
	{
		return (Map) _headers.get(IHeaderCategory.CATEGORY_COMMON_HEADERS);
	}

	public Map getMessageHeaders()
	{
		return (Map) _headers.get(IHeaderCategory.CATEGORY_MESSAGE_HEADERS);
	}

	public String[] getData()
	{
		return _data;
	}

	public File[] getPayLoad()
	{
		return _payLoad;
	}

	public byte[] getPayLoadData()
	{
		return _payLoadData;
	}

	public void setAttribute(String key, Object value)
	{
		_messageAttributes.put(key, value);
	}

	public Object getAttribute(String key)
	{
		return _messageAttributes.get(key);
	}

	public Enumeration getAttributeNames()
	{
		if (_messageAttributes.keySet() != null)
		{
			Set keySet = _messageAttributes.keySet();
			return Collections.enumeration(keySet);
		}
		else
		{
			return Collections.enumeration(Collections.EMPTY_SET);
		}
	}
	
	public Integer getHttpResponseCode()
  {
    return _httpResponseCode;
  }

  public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[Common Headers]" + this.getCommonHeaders());
		sb.append("\n[DataContent]");
		if (this.getData() != null)
		{
			String[] data = this.getData();
			for (int i = 0; i < data.length; i++)
			{
				sb.append("[Data][" + i + "]");
				sb.append("=[" + data[i] + "]");
			}
		}
		sb.append("[Message Headers]" + this.getMessageHeaders());
		if (this.getPayLoad() != null)
		{
			if (this.getPayLoad().length > 0)
				sb.append("[File PayLoad Length]=" + this.getPayLoad().length);
		}

		if (this.getPayLoadData() != null)
		{
			if (this.getPayLoadData().length > 0)
				sb.append("[Byte PayLoad Length]=" + this.getPayLoadData().length);
		}
    			if(getHttpResponseCode() != null)
    			{
      				  sb.append("[HttpResponse Code]" + getHttpResponseCode());
    			}
    
		return sb.toString();
	}

}
