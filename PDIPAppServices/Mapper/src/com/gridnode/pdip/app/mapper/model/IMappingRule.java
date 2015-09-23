/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingRule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 26 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in MappingRule entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMappingRule
{
  /**
   * Name of MappingFile entity.
   */
  public static final String  ENTITY_NAME = "MappingRule";

  //Mapping types
  /**
   * MappingType for Convert.
   */
  public static final Short MAPPING_CONVERT   = new Short("0");

  /**
   * MappingType for Transform.
   */
  public static final Short MAPPING_TRANSFORM = new Short("1");

  /**
   * MappingType for Split.
   */
  public static final Short MAPPING_SPLIT = new Short("2");


  /**
   * FieldId for the UID for a MappingRule entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-MappingRule-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION     = new Integer(2); //Double

  /**
   * FieldId for the Name of the MappingRule. A String.
   */
  public static final Number NAME        = new Integer(3); //String(30)

  /**
   * FieldId for the Description of the MappingRule. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the Type of the MappingRule. A Number.
   */
  public static final Number TYPE        = new Integer(5); //Integer

  /**
   * FieldId for the MappingFile. A
   * {@link com.gridnode.pdip.app.mapper.model.MappingFile MappingFile}.
   */
  public static final Number MAPPING_FILE  = new Integer(6); //MappingFile

  /**
   * FieldId for the TransformRefDoc of the MappingRule.
   */
  public static final Number TRANSFORM_REF_DOC = new Integer(7);  //Boolean

  /**
   * FieldId for the ReferenceDocUID of the MappingRule.
   */
  public static final Number REFERENCE_DOC_UID = new Integer(8);   //Integer

  /**
   * FieldId for the XPath of the MappingRule.
   */
  public static final Number XPATH       = new Integer(9);   //String(1024)

  /**
   * FieldId for the ParamName of the MappingRule.
   */
  public static final Number PARAM_NAME  = new Integer(10);  //String(40)

  /**
   * FieldId for the KeepOriginal of the MappingRule.
   */
  public static final Number KEEP_ORIGINAL    = new Integer(11);    //Boolean
  
  /**
   * FieldId for the Mapping class of the MappingFile. A number.
   */
  public static final Number MAPPING_CLASS    = new Integer(12);

}