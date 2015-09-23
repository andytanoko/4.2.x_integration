/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageTemplate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Daniel D'Cotta      Created
 * 2005-01-05     SC				              1)Add in MsgType 'JMS'
 *                                        2)Add in two fieldID JMSDestination
 *                                        and MessageProperties.
 */
package com.gridnode.gtas.model.alert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in MessageTemplate entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMessageTemplate
{
  /**
   * Name for Alert entity.
   */
  public static final String  ENTITY_NAME = "MessageTemplate";

 // Possible values for MSG_TYPE
  public static final String MSG_TYPE_EMAIL       = "EMail";
  public static final String MSG_TYPE_LOG         = "Log";
  public static final String MSG_TYPE_ALERT_LIST  = "AlertList";
  public static final String MSG_TYPE_JMS = "JMS";

 // Possible values for CONTENT_TYPE
  public static final String CONTENT_TYPE_TEXT    = "Text";
  public static final String CONTENT_TYPE_HTML    = "HTML";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for name.
   */
  public static final Number NAME = new Integer(1); // String 80

  /**
   * FieldId for field format
   */
//  public static final Number FIELDFORMAT = new Integer(2); // string 50

  /**
   * FieldId for content type.
   */
  public static final Number CONTENT_TYPE = new Integer(3); // string 30

  /**
   * FieldId for message type.
   */
  public static final Number MESSAGE_TYPE = new Integer(4); // string 30

  /**
   * FieldId for from Address.
   */
  public static final Number FROM_ADDR = new Integer(5); // string 255

  /**
   * FieldId for to address.
   */
  public static final Number TO_ADDR = new Integer(6); // string 255

  /**
   * FieldId for cc address.
   */
  public static final Number CC_ADDR = new Integer(7); // string 255

  /**
   * FieldId for Subject.
   */
  public static final Number SUBJECT = new Integer(8); // string 255

  /**
   * FieldId for Content message.
   */
  public static final Number MESSAGE = new Integer(9); // string 2000

  /**
   * FieldId for File locaton.
   */
  public static final Number LOCATION = new Integer(10); // string 255

  /**
   * FieldId for Append to file.
   */
  public static final Number APPEND = new Integer(11); // int 1
  
  /**
   * FieldId for JMSDestination. A Long.
   */
  public static final Number JMS_DESTINATION = new Integer(14); //Long
  
  /**
   * FieldId for MessageProperties. A Collection.
   */
  public static final Number MESSAGE_PROPERTIES = new Integer(15); //Collection
  
  
}