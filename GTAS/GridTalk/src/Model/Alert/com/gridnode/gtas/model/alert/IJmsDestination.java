/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsDestination.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 * Dec 30, 2005		 SC									 Adopt com.gridnode.pdip.app.alert.model.IJmsDestination file into this file. 
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in JmsDestination entity.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public interface IJmsDestination
{
	/**
	 * Name for JmsDestination Entity
	 */
	public static final String ENTITY_NAME = "JmsDestination";
	
	public static final Number UID = new Integer(0); //LONG
	
	public static final Number VERSION = new Integer(1); //Double
	
	/**
   * FieldId for Whether-the-JmsDestination-can-be-deleted flag. A Boolean.
   */
	public static final Number CAN_DELETE = new Integer(2); //Boolean
	
	/**
   * The following is the fieldID for the respective field(eg NAME, JNDI_NAME). 
   * Their type is stated after the declaration of the Number object.
   */
	public static final Number NAME = new Integer(3); //String
	
	public static final Number TYPE = new Integer(4); //Integer
	
	public static final Number JNDI_NAME = new Integer(5); //String
	
	public static final Number DELIVERY_MODE = new Integer(6); //Integer
	
	public static final Number PRIORITY= new Integer(7); //Integer
	
	public static final Number CONNECTION_FACTORY_JNDI = new Integer(8); //String
	
	public static final Number CONNECTION_USER = new Integer(9); //String
	
	public static final Number CONNECTION_PASSWORD = new Integer(10); //String
	
	public static final Number LOOKUP_PROPERTIES = new Integer(11); //String
	
	public static final Number RETRY_INTERVAL = new Integer(12); //Integer
	
	public static final Number MAXIMUM_RETRIES = new Integer(13); //Integer
	
}
