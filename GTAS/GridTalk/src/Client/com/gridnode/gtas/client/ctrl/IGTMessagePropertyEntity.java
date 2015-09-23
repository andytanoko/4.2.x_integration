/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMessagePropertiesEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 * Jan  5, 2006		 SC									 Adopt com.gridnode.pdip.app.alert.model.IMessageProperty into this file
 */
package com.gridnode.gtas.client.ctrl;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in MessageProperty entity.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public interface IGTMessagePropertyEntity extends IGTEntity
{
  /**
	 * Name for JmsDestination Entity
	 */
	public static final String ENTITY_NAME = "MessageProperty";
	
	public static final Number UID = new Integer(0); //Long
	
	public static final Number CAN_DELETE = new Integer(1); //Boolean
	
	public static final Number VERSION = new Integer(2); //Double
	
	/**
   * The following is the fieldID for the respective field(eg KEY, TYPE). 
   * Their type is stated after the declaration of the Number object.
   */
	public static final Number KEY = new Integer(3); //String
	
	public static final Number TYPE = new Integer(4); //Integer
	
	public static final Number VALUE = new Integer(5); //String
	
	//Possible MessageProperty type
	public static final int TYPE_BOOLEAN = 1;
	public static final int TYPE_BYTE = 2;
	public static final int TYPE_DOUBLE = 3;
	public static final int TYPE_FLOAT = 4;
	public static final int TYPE_INT = 5;
	public static final int TYPE_LONG = 6;
	public static final int TYPE_SHORT = 7;
	public static final int TYPE_STRING = 8;
	
}
