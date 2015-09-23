/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttachmentRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in AttachmentRegistry entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAttachmentRegistry
{
  /**
   * Name for Activity entity.
   */
  public static final String ENTITY_NAME = "AttachmentRegistry";

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
   * FieldId for OriginalUid. A Number.
   */
  public static final Number ATTACHMENT_UID = new Integer(4);  //Long

  /**
   * FieldId for Filename. A Date
   */
  public static final Number DATE_PROCESSED  = new Integer(5);  //Date

}