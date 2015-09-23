/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJmsMessageRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 * Feb 17 2006    Neo Sok Lay         Change JMS_DESTINATION_NAME to JMS_DESTINATION_UID
 */
package com.gridnode.pdip.app.alert.model;

/**
 *
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public interface IJmsMessageRecord
{
	/**
	 * Name for JmsDestination Entity
	 */
	public static final String ENTITY_NAME = "JmsMessageRecord";
	
	public static final Number UID = new Integer(0); //Long
	
	public static final Number CAN_DELETE = new Integer(1); //Boolean
	
	public static final Number VERSION = new Integer(2); //Double
	
	/**
   * The following is the fieldID for the respective field(eg JMS_DESTINATION, MESSAGE_DATA). 
   * Their type is stated after the declaration of the Number object.
   */
	//public static final Number JMS_DESTINATION_NAME = new Integer(3); //String
  public static final Number JMS_DESTINATION_UID = new Integer(3); //Long
	
	public static final Number ALERT_TIMESTAMP = new Integer(4); //Date
	
	public static final Number MESSAGE_DATA = new Integer(5); //MessageData
	
	public static final Number PERMANENT_FAILED = new Integer(6); //Boolean
	
	public static final Number ALERT_TIME_IN_LONG = new Integer(7); //Long
}
