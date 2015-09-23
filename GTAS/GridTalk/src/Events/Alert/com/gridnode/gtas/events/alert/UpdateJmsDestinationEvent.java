/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJmsDestinationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 05				SC									Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.util.StringUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class UpdateJmsDestinationEvent
  extends    EventSupport
//  implements IGuardedEvent
{
//	public static final String GUARDED_FEATURE = "ENTERPRISE";
//  public static final String GUARDED_ACTION  = "UpdateJmsDestination";

	private static final long serialVersionUID = -8726102418072691512L;
	
  public static final String UID = "UID";
  
  public static final String NAME = "NAME";

	public static final String TYPE = "TYPE";

	public static final String JNDI_NAME = "JNDI_NAME";

	public static final String DELIVERY_MODE = "DELIVERY_MODE";

	public static final String PRIORITY = "PRIORITY";

	public static final String CONNECTION_FACTORY_JNDI = "CONNECTION_FACTORY_JNDI";

	public static final String CONNECTION_USER = "CONNECTION_USER";

	public static final String CONNECTION_PASSWORD = "CONNECTION_PASSWORD";

	public static final String LOOKUP_PROPERTIES = "LOOKUP_PROPERTIES";

	public static final String RETRY_INTERVAL = "RETRY_INTERVAL";

	public static final String MAXIMUM_RETRIES = "MAXIMUM_RETRIES";
  
	public UpdateJmsDestinationEvent(Long uid,
	                                 String name, Integer type, String jndiName, Integer deliveryMode, Integer priority, 
	                                 String connectionFactoryJndi, String connectionUser, String connectionPassword, 
	                                 Properties lookupProperties, Integer retryInterval, Integer maximumRetries) throws EventException
	{
		checkSetLong(UID, uid);
		checkSetString(NAME, name);
		checkSetInteger(TYPE, type);
		checkSetString(JNDI_NAME, jndiName);
		checkSetInteger(DELIVERY_MODE, deliveryMode);
		checkSetInteger(PRIORITY, priority);
		checkSetString(CONNECTION_FACTORY_JNDI, connectionFactoryJndi);
		
		/* both user and password are filled or empty. */
		if (StringUtil.isNotEmpty(connectionUser) && StringUtil.isNotEmpty(connectionPassword))
		{
			checkSetString(CONNECTION_USER, connectionUser);
			checkSetString(CONNECTION_PASSWORD, connectionPassword);
		}
		
		checkSetObject(LOOKUP_PROPERTIES, lookupProperties, Properties.class);
		checkSetInteger(RETRY_INTERVAL, retryInterval);
		checkSetInteger(MAXIMUM_RETRIES, maximumRetries);
	} 
	
	/* accessor methods */
	public Long getUid()
	{
		return (Long) getEventData(UID);
	}
	
	public String getName()
	{
		return (String) getEventData(NAME);
	}

	public Integer getType()
	{
		return (Integer) getEventData(TYPE);
	}
 
	public String getJndiName()
	{
		return (String) getEventData(JNDI_NAME);
	}
	
	public Integer getDeliveryMode()
	{
		return (Integer) getEventData(DELIVERY_MODE);
	}
	
	public Integer getPriority()
	{
		return (Integer) getEventData(PRIORITY);
	}

	public String getConnectionFactoryJndi()
	{
		return (String) getEventData(CONNECTION_FACTORY_JNDI);
	}
  
	public String getConnectionUser()
	{
		return (String) getEventData(CONNECTION_USER);
	}
	
	public String getConnectionPassword()
	{
		return (String) getEventData(CONNECTION_PASSWORD);
	}
	
	public Properties getLookupProperties()
	{
		return (Properties) getEventData(LOOKUP_PROPERTIES);
	}
	
	public Integer getRetryInterval()
	{
		return (Integer) getEventData(RETRY_INTERVAL);
	}
	
	public Integer getMaximumRetries()
	{
		return (Integer) getEventData(MAXIMUM_RETRIES);
	}
	
	/* END: accessor methods */
	
  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateJmsDestinationEvent";
  }

//  // ************* From IGuardedEvent *************************
//
//  public String getGuardedFeature()
//  {
//    return GUARDED_FEATURE;
//  }
//
//  public String getGuardedAction()
//  {
//    return GUARDED_ACTION;
//  }
}