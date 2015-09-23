/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ChannelInfo entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IChannelInfo
{
  /**
   * Name for Activity entity.
   */
  public static final String ENTITY_NAME = "ChannelInfo";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID         = new Integer(0);  //Long

  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(2); //Double

  /**
   * FieldId for ChannelUID. A Long.
   */
  public static final Number CHANNEL_UID    = new Integer(3);  //Long

  /**
   * FieldId for ChannelName. A String.
   */
  public static final Number CHANNEL_NAME  = new Integer(4);  //String

  /**
   * FieldId for ChannelProtocol. A String.
   */
  public static final Number CHANNEL_PROTOCOL = new Integer(5);  //String

}