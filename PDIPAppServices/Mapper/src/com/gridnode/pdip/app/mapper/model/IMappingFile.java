/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 26 2002    Koh Han Sing        Created
 * Feb 13 2003    Koh Han Sing        Add in XPath file
 */
package com.gridnode.pdip.app.mapper.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in MappingFile entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMappingFile
{
  /**
   * Name of MappingFile entity.
   */
  public static final String  ENTITY_NAME = "MappingFile";

  /**
   * Types of MappingFiles
   */
  /**
   * MappingFile Type for XSL.
   */
  public static final Short XSL   = new Short("0");

  /**
   * MappingFile Type for Conversion Rule.
   */
  public static final Short CONVERSION_RULE = new Short("1");

  /**
   * MappingFile Type for Reference Document.
   */
  public static final Short REFERENCE_DOC = new Short("2");

  /**
   * MappingFile Type for DTD File.
   */
  public static final Short DTD   = new Short("3");

  /**
   * MappingFile Type for xml Schema.
   */
  public static final Short SCHEMA = new Short("4");

  /**
   * MappingFile Type for Dictionary file.
   */
  public static final Short DICT = new Short("5");

  /**
   * MappingFile Type for XPath file.
   */
  public static final Short XPATH = new Short("6");
  
  /**
   * MappingFile Type for Java Binary.
   */
  public static final Short JAVA_BINARY = new Short("7");

  /**
   * FieldId for the UID for a MappingFile entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-MappingFile-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION     = new Integer(2); //Double

  /**
   * FieldId for the Name of the MappingFile. A String.
   */
  public static final Number NAME        = new Integer(3); //String(30)

  /**
   * FieldId for the Description of the MappingFile. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the Filename of the MappingFile. A String.
   */
  public static final Number FILENAME    = new Integer(5); //String(80)

  /**
   * FieldId for the Path of the MappingFile. A String.
   */
  public static final Number PATH        = new Integer(6); //String(80)

  /**
   * FieldId for the Type of the MappingFile. A Number.
   */
  public static final Number TYPE        = new Integer(7); //Short

  /**
   * FieldId for the SubPath of the MappingFile. A String.
   */
  public static final Number SUB_PATH    = new Integer(8); //String(80)
  
  /**
   * FieldId for the Mapping Class of the MappingFile. A String.
   */
  public static final Number MAPPING_CLASS = new Integer(9); //String(80)
}