/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFileType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.document;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in FileType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IFileType
{
  /**
   * Name of FileType entity.
   */
  public static final String  ENTITY_NAME = "FileType";

  /**
   * FieldId for the UID for a FileType entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for the Name of the FileType. A String.
   */
  public static final Number FILE_TYPE    = new Integer(1); //String(10)

  /**
   * FieldId for the Description of the FileType. A String.
   */
  public static final Number DESCRIPTION = new Integer(2); //String(80)

  /**
   * FieldId for ProgramName. A String.
   */
  public static final Number PROGRAM_NAME = new Integer(3);  //String(120)

  /**
   * FieldId for ProgramPath. A String.
   */
  public static final Number PROGRAM_PATH = new Integer(4);  //String(120)

  /**
   * FieldId for Parameters. A String.
   */
  public static final Number PARAMETERS   = new Integer(5);  //String(120)

  /**
   * FieldId for WorkingDir. A String.
   */
  public static final Number WORKING_DIR  = new Integer(6);  //String(120)

  /**
   * FieldId for Whether-the-FileType-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(7); //Boolean

}