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
package com.gridnode.gtas.model.document;

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
   * FieldId for Filename. A String
   */
  public static final Number FILENAME  = new Integer(6);  //String(80)

  /**
   * FieldId for OriginalFilename. A String
   */
  public static final Number ORIGINAL_FILENAME = new Integer(7);  //String(80)

}