/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.document;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in DocumentType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IDocumentType
{
  /**
   * Name of DocumentType entity.
   */
  public static final String  ENTITY_NAME = "DocumentType";

  /**
   * FieldId for the UID for a DocumentType entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for the Name of the DocumentType. A String.
   */
  public static final Number DOC_TYPE    = new Integer(1); //String(12)

  /**
   * FieldId for the Description of the DocumentType. A String.
   */
  public static final Number DESCRIPTION = new Integer(2); //String(80)

  /**
   * FieldId for Whether-the-DocumentType-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(3); //Boolean
}