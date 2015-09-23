/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageTemplate.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 13 2002    Srinath	             Created
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 * Dec 28 2005    Tam Wei Xiang           Add in two attributes _jmsDestination
 *                                        and _messageProperties.
 *                                        method: getJmsDestinationUIDByCastor()
 *                                                setJmsDestinationUIDByCastor(Long)
 * Mar 01 2006    Neo Sok Lay             Use generics                                               
 */
package com.gridnode.pdip.app.alert.model;

import java.util.Collection;

import com.gridnode.pdip.framework.db.DataObjectList;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for MessageTemplate entity. It contains information about
 * entities and permissable messages.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a MessageTemplate entity instance.
 *   Name         - Name of the MessageTemplate.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 */

public class MessageTemplate extends AbstractEntity implements IMessageTemplate
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1583398057225099341L;
	
	protected String _name = null;
//  protected String _fieldFormat = null;
  protected String _contentType = null;
  protected String _messageType = null;
  protected String _fromaddr = null;
  protected String _toaddr = null;
  protected String _ccAddr = null;
  protected String _subject = null;
  protected String _message = null;
  protected String _location = null;
  protected Boolean _append = null;
  
  protected JmsDestination _jmsDestination = null;
  protected DataObjectList _messageProperties = new DataObjectList<MessageProperty>(); 
  
  public MessageTemplate()
  {
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_name).append('/').append(_messageType).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  public String getName()
  {
    return _name;
  }

/*
  public String getFieldFormat()
  {
    return _fieldFormat;
  }
*/

  public String getContentType()
  {
    return _contentType;
  }

  public String getMessageType()
  {
    return _messageType;
  }

  public String getFromAddr()
  {
    return _fromaddr;
  }

  public String getToAddr()
  {
    return _toaddr;
  }

  public String getCcAddr()
  {
    return _ccAddr;
  }

  public String getSubject()
  {
    return _subject;
  }

  public String getMessage()
  {
    return _message;
  }

  public String getLocation()
  {
    return _location;
  }

  public Boolean getAppend()
  {
    return _append;
  }
  
  public JmsDestination getJmsDestination()
  {
  	return _jmsDestination;
  }
  
  /**
   * use by castol xml
   * @return UID
   */
  public Long getJmsDestinationUIDByCastor()
  {
  	if(_jmsDestination != null)
  	{
  		return (Long)_jmsDestination.getKey();
  	}
  	return null;
  }
  
  public Collection<MessageProperty> getMessageProperties()
  {
  	return _messageProperties;
  }
  
  // ******************** Setters for attributes ***************************

  public void setName(String name)
  {
    this._name = name;
  }

/*
  public void setFieldFormat(String fieldFormat)
  {
    this._fieldFormat = fieldFormat;
  }
*/

  public void setContentType(String contentType)
  {
    this._contentType = contentType;
  }

  public void setMessageType(String messageType)
  {
    this._messageType = messageType;
  }

  public void setFromAddr(String fromaddr)
  {
    this._fromaddr = fromaddr;
  }

  public void setToAddr(String toaddr)
  {
    this._toaddr = toaddr;
  }

  public void setCcAddr(String cc)
  {
    this._ccAddr = cc;
  }

  public void setSubject(String subject)
  {
    this._subject = subject;
  }

  public void setMessage(String message)
  {
    this._message = message;
  }

  public void setLocation(String location)
  {
    this._location = location;
  }

  public void setAppend(Boolean append)
  {
    this._append = append;
  }
  
  public void setJmsDestination(JmsDestination jmsDesUID)
  {
  	this._jmsDestination = jmsDesUID;
  }
  
  public void setMessageProperty(Collection<MessageProperty> msgProperty)
  {
  	if(msgProperty != null)
  	{
  		this._messageProperties = new DataObjectList<MessageProperty>(msgProperty);
  	}
  	else
  	{
  		_messageProperties.clear();
  	}
  }
  
  /**
   * Use by castol xml 
   * @param jmsUID
   */
  public void setJmsDestinationUIDByCastor(Long jmsUID)
  {
  	_jmsDestination = new JmsDestination();
  	_jmsDestination.setKey(jmsUID);
  }
}