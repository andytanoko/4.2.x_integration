/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTJmsDestination.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 * Dec 30, 2005		 SC									 Adopt com.gridnode.pdip.app.alert.model.IJmsDestination file into this file. 
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IJmsDestination;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in JmsDestination entity.
 */
public interface IGTJmsDestinationEntity extends IGTEntity
{
	public static final Number UID = IJmsDestination.UID;
	
	/**
   * The following is the fieldID for the respective field(eg NAME, JNDI_NAME). 
   * Their type is stated after the declaration of the Number object.
   */
	public static final Number NAME = IJmsDestination.NAME;
	
	public static final Number TYPE = IJmsDestination.TYPE;
	
	public static final Number JNDI_NAME = IJmsDestination.JNDI_NAME;
	
	public static final Number DELIVERY_MODE = IJmsDestination.DELIVERY_MODE;
	
	public static final Number PRIORITY = IJmsDestination.PRIORITY;
	
	public static final Number CONNECTION_FACTORY_JNDI = IJmsDestination.CONNECTION_FACTORY_JNDI;
	
	public static final Number CONNECTION_USER = IJmsDestination.CONNECTION_USER;
	
	public static final Number CONNECTION_PASSWORD = IJmsDestination.CONNECTION_PASSWORD;
	
	public static final Number LOOKUP_PROPERTIES = IJmsDestination.LOOKUP_PROPERTIES;
	
	public static final Number RETRY_INTERVAL = IJmsDestination.RETRY_INTERVAL;
	
	public static final Number MAXIMUM_RETRIES = IJmsDestination.MAXIMUM_RETRIES;
	
	/* virtual field */
	public static final Number VIRTUAL_LOOKUP_PROPERTIES = -100;
	
	/* constants for value of this entity fields */
	public static final Integer QUEUE = new Integer(1);
	public static final Integer TOPIC = new Integer(2);
	public static final Integer DELIVERY_MODE_DEFAULT = new Integer(0);
	public static final Integer PRIORITY_DEFAULT = new Integer(-1);
}
