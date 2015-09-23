/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttachment.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 22 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Attachment entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAttachment
{
  /**
   * Name for Activity entity.
   */
  public static final String ENTITY_NAME = "Attachment";

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
   * FieldId for PartnerId. A String.
   */
  public static final Number PARTNER_ID    = new Integer(3);  //String(20)

  /**
   * FieldId for IsSendInitiated. A Boolean
   */
//  public static final Number IS_SEND_INITIATED  = new Integer(4);  //Boolean

  /**
   * FieldId for OriginalUid. A Number.
   */
  public static final Number ORIGINAL_UID = new Integer(4);  //Long

  /**
   * FieldId for Filename. A String
   */
  public static final Number FILENAME  = new Integer(5);  //String(80)

  /**
   * FieldId for OriginalFilename. A String
   */
  public static final Number ORIGINAL_FILENAME = new Integer(6);  //String(80)

  /**
   * FieldId for IsOutgoing. A Boolean
   */
  public static final Number IS_OUTGOING  = new Integer(7);  //Boolean

}