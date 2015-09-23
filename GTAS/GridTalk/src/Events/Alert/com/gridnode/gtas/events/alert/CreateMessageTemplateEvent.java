/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateMessageTemplateEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 02 May 2003    Neo Sok Lay         Add Log message template constructor.
 * 6 Jan 2005			SC									Add support for jms message type (two new fields are needed: JMS destination and message properties).
 */
package com.gridnode.gtas.events.alert;

import java.util.Collection;
import java.util.Map;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new MessageTemplate.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.1
 * @since 2.0
 */
public class CreateMessageTemplateEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3412359542087702245L;
	public static final String NAME         = "Name";
  public static final String CONTENT_TYPE = "Content Type";
  public static final String MESSAGE_TYPE = "Message Type";
  public static final String FROM_ADDR    = "From Addr";
  public static final String TO_ADDR      = "To Addr";
  public static final String CC_ADDR      = "Cc Addr";
  public static final String SUBJECT      = "Subject";
  public static final String MESSAGE      = "Message";

  // for Log message type
  public static final String LOCATION     = "Location";
  public static final String IS_APPEND    = "Is Append";
  
  /* for jms message type */
  public static final String JMS_DESTINATION_UID = "JmsDestinationUid";
  public static final String MESSAGE_PROPERTIES     = "MessageProperties";

  /**
   * Constructor for Log Message Template
   *
   * @param name Name of the Message Template
   * @param messageType MSG_TYPE_LOG
   * @param location Location of the file to log message to
   * @param isAppend Whether to append log message or overwrite (false).
   * @param message The log message.
   */
  public CreateMessageTemplateEvent(
    String  name,
    String  messageType,
    String  location,
    Boolean isAppend,
    String  message)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(MESSAGE_TYPE, messageType);
    checkSetString(MESSAGE, message);
    checkSetString(LOCATION, location);
    setEventData(IS_APPEND, isAppend);
  }

  public CreateMessageTemplateEvent(
    String  name,
    String  contentType,
    String  messageType,
    String  fromAddr,
    String  toAddr,
    String  ccAddr,
    String  subject,
    String  message)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(CONTENT_TYPE, contentType);
    checkSetString(MESSAGE_TYPE, messageType);
    setEventData(FROM_ADDR, fromAddr);
    setEventData(TO_ADDR, toAddr);
    setEventData(CC_ADDR, ccAddr);
    checkSetString(SUBJECT, subject);
    checkSetString(MESSAGE, message);
  }
  
  public CreateMessageTemplateEvent(String name, String messageType,
																		Long jmdDestinationUid,
																		Collection messageProperties, String message) throws EventException
  {
  	checkSetString(NAME, name);
  	checkSetString(MESSAGE_TYPE, messageType);
  	checkSetLong(JMS_DESTINATION_UID, jmdDestinationUid);
  	checkSetString(MESSAGE, message);
  	
  	if (messageProperties != null)
  	{
  		checkSetObject(MESSAGE_PROPERTIES, messageProperties, Collection.class);
  	}
  }
  
  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getMessageType()
  {
    return (String)getEventData(MESSAGE_TYPE);
  }

  public String getFromAddr()
  {
    return (String)getEventData(FROM_ADDR);
  }

  public String getToAddr()
  {
    return (String)getEventData(TO_ADDR);
  }

  public String getCcAddr()
  {
    return (String)getEventData(CC_ADDR);
  }

  public String getSubject()
  {
    return (String)getEventData(SUBJECT);
  }

  public String getMessage()
  {
    return (String)getEventData(MESSAGE);
  }

  public String getContentType()
  {
    return (String)getEventData(CONTENT_TYPE);
  }

  public String getLocation()
  {
    return (String)getEventData(LOCATION);
  }

  public Boolean getIsAppend()
  {
    return (Boolean)getEventData(IS_APPEND);
  }

  public Long getJmsDestinationUid()
  {
    return (Long)getEventData(JMS_DESTINATION_UID);
  }
  
  public Collection getMessageProperties()
  {
    return (Collection) getEventData(MESSAGE_PROPERTIES);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/CreateMessageTemplateEvent";
  }
}