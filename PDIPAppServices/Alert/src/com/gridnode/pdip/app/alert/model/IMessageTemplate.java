/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageTemplate.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 13 2002    Srinath	             Created
 * Jan 23 2003    Neo Sok Lay             Renamed from IMessage.
 * Mar 03 2003    Neo Sok Lay             Add Version and CanDelete fields.
 * Dec 28 2005    Tam Wei Xiang           1)Add in MsgType 'JMS'
 *                                        2)Add in two fieldID JMSDestination
 *                                        and MessageProperties.
 */

package com.gridnode.pdip.app.alert.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in Message entity.
 *
 * @author Srinath
 *
 */

public interface IMessageTemplate
{
  /**
   * Name for Message entity.
   */
  public static final String ENTITY_NAME = "MessageTemplate";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for name.
   */
  public static final Number NAME = new Integer(1); // String 30

  /**
   * FieldId for field format
   */
//  public static final Number FIELDFORMAT = new Integer(2); // string 50

  /**
   * FieldId for content type.
   */
  public static final Number CONTENTTYPE = new Integer(3); // tinyint

  /**
   * FieldId for message type.
   */
  public static final Number MESSAGETYPE = new Integer(4); // tinyint

  /**
   * FieldId for from Address.
   */
  public static final Number FROMADDR = new Integer(5); // string 255

  /**
   * FieldId for to address.
   */
  public static final Number TOADDR = new Integer(6); // string 255

  /**
   * FieldId for cc address.
   */
  public static final Number CCADDR = new Integer(7); // string 255

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
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(12); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(13); //boolean
  
  /**
   * FieldId for JMSDestination. JmsDestination.
   */
  public static final Number JMS_DESTINATION = new Integer(14); //JmsDestination
  
  /**
   * FieldId for MessageProperties. A Collection.
   */
  public static final Number MESSAGE_PROPERTIES = new Integer(15); //Collection
  
  // Possible Message Types
  public static final String MSG_TYPE_EMAIL       = "EMail";
  public static final String MSG_TYPE_LOG         = "Log";
  public static final String MSG_TYPE_ALERT_LIST  = "AlertList";
  public static final String MSG_TYPE_JMS = "JMS";

  public static final String CONTENT_TYPE_TEXT    = "Text";
  public static final String CONTENT_TYPE_HTML    = "HTML";

}