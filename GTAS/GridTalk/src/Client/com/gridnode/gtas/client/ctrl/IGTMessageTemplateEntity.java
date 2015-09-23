/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMessageTemplateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created (2002???? hmmm)
 * 2003-05-14     Andrew Hill         Support for log messageType fields
 * 2005-01-05     SC				              1)Add in MsgType 'JMS'
 *                                        2)Add in two fieldID JMSDestination
 *                                        and MessageProperties.      
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IMessageTemplate;
 
public interface IGTMessageTemplateEntity extends IGTEntity
{
 // Possible values for MSG_TYPE
  public static final String MSG_TYPE_EMAIL      = IMessageTemplate.MSG_TYPE_EMAIL;
  public static final String MSG_TYPE_LOG        = IMessageTemplate.MSG_TYPE_LOG;
  public static final String MSG_TYPE_ALERT_LIST = IMessageTemplate.MSG_TYPE_ALERT_LIST;
  public static final String MSG_TYPE_JMS = IMessageTemplate.MSG_TYPE_JMS;

 // Possible values for CONTENT_TYPE
  public static final String CONTENT_TYPE_TEXT   = IMessageTemplate.CONTENT_TYPE_TEXT;
  public static final String CONTENT_TYPE_HTML   = IMessageTemplate.CONTENT_TYPE_HTML;

  // Fields
  public static final Number UID          = IMessageTemplate.UID;
  public static final Number NAME         = IMessageTemplate.NAME;
  public static final Number CONTENT_TYPE = IMessageTemplate.CONTENT_TYPE;
  public static final Number MESSAGE_TYPE = IMessageTemplate.MESSAGE_TYPE;
  public static final Number FROM_ADDR    = IMessageTemplate.FROM_ADDR;
  public static final Number TO_ADDR      = IMessageTemplate.TO_ADDR;
  public static final Number CC_ADDR      = IMessageTemplate.CC_ADDR;
  public static final Number SUBJECT      = IMessageTemplate.SUBJECT;
  public static final Number MESSAGE      = IMessageTemplate.MESSAGE;
  public static final Number LOCATION     = IMessageTemplate.LOCATION; //20030514AH
  public static final Number APPEND       = IMessageTemplate.APPEND; //20030514AH
  public static final Number JMS_DESTINATION = IMessageTemplate.JMS_DESTINATION;
  public static final Number MESSAGE_PROPERTIES = IMessageTemplate.MESSAGE_PROPERTIES;
}
