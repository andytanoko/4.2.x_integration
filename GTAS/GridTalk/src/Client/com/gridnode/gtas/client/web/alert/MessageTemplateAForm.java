/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageTemplateAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 * 2003-05-14     Andrew Hill         Fields to support Log messageType
 * 2005-01-05			SC									Add fields: jms destination uid, MessageProperty, update action.  Also add methods relating to these fields.
 * 																		In doReset method, add code to reset checkboxes for message properties.
 */
package com.gridnode.gtas.client.web.alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.be.DomainIdentifierAForm;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class MessageTemplateAForm extends GTActionFormBase
{
  private String _name;
  private String _contentType;
  private String _messageType;
  private String _fromAddr;
  private String _toAddr;
  private String _ccAddr;
  private String _subject;
  private String _message;
  private String _location; //20030514AH
  private String _append; //20030514AH
  private String _jmsDestinationUid;
  private Vector _messageProperties;
  private String _updateAction;

  // Substitution List
  private String _object;
  private String _field;

  public MessageTemplateAForm()
  {
  	_messageProperties = new Vector();
  }
  
  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getContentType()
  { return _contentType; }

  public void setContentType(String contentType)
  { _contentType=contentType; }

  public String getMessageType()
  { return _messageType; }

  public void setMessageType(String messageType)
  { _messageType=messageType; }

  public String getFromAddr()
  { return _fromAddr; }

  public void setFromAddr(String fromAddr)
  { _fromAddr=fromAddr; }

  public String getToAddr()
  { return _toAddr; }

  public void setToAddr(String toAddr)
  { _toAddr=toAddr; }

  public String getCcAddr()
  { return _ccAddr; }

  public void setCcAddr(String ccAddr)
  { _ccAddr=ccAddr; }

  public String getSubject()
  { return _subject; }

  public void setSubject(String subject)
  { _subject=subject; }

  public String getMessage()
  { return _message; }

  public void setMessage(String message)
  { _message=message; }

  // Substitution List
  public String getObject()
  { return _object; }

  public void setObject(String object)
  { _object=object; }

  public String getField()
  { return _field; }

  public void setField(String field)
  { _field=field; }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _message = null;  // text area
    _append = "false"; //20030514AH
    
    if (_messageProperties != null)
    {
    	Iterator it = _messageProperties.iterator();
    	while (it.hasNext())
    	{
    		MessagePropertyAForm form = (MessagePropertyAForm) it.next();
    		form.doReset(mapping, request);
    	}
    }
  }
  
  
  public String getAppend()
  {
    return _append;
  }

  public String getLocation()
  {
    return _location;
  }

  public void setAppend(String append)
  {
    _append = append;
  }

  public void setLocation(String location)
  {
    _location = location;
  }

  /**
   * Return jms destination uid.
   */
  public String getJmsDestination()
  {
  	return _jmsDestinationUid;
  }
  
  /**
   * Set jms destination uid.
   */
  public void setJmsDestination(String uid)
  {
  	_jmsDestinationUid = uid;
  }
  
  public String getUpdateAction()
  {
    return _updateAction;
  }

  public void setUpdateAction(String updateAction)
  {
    _updateAction = updateAction;
  }
  
  /* methods for message properties field */
  public MessagePropertyAForm[] getMessageProperties()
  {
    return (MessagePropertyAForm[]) _messageProperties.toArray(new MessagePropertyAForm[_messageProperties.size()]);
  }

  public void setMessageProperties(MessagePropertyAForm[] forms)
  {
  	_messageProperties = (Vector) StaticUtils.collectionValue(forms);
  }
  
  public void addNewMessageProperty()
  {
  	_messageProperties.add(new MessagePropertyAForm());
  }
  
  public void removeSelectedMessageProperties()
  {
    if( _messageProperties == null) 
    {
    	return;
    }
    Iterator it = _messageProperties.iterator();
    while (it.hasNext())
    {
    	MessagePropertyAForm form = (MessagePropertyAForm) it.next();
    	if (form.isSelected())
    	{
    		it.remove();
    	}
    }
  }
}