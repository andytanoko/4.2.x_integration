/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkMappingRule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in IGridTalkMappingRule entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IGridTalkMappingRule
{
  /**
   * Name of MappingFile entity.
   */
  public static final String  ENTITY_NAME = "GridTalkMappingRule";

  /**
   * FieldId for the UID for a GridTalkMappingRule entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-GridTalkMappingRule-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION     = new Integer(2); //Double

  /**
   * FieldId for the Name of the GridTalkMappingRule. A String.
   */
  public static final Number NAME        = new Integer(3); //String(30)

  /**
   * FieldId for the Description of the GridTalkMappingRule. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the SourceDocType of the GridTalkMappingRule.
   */
  public static final Number SOURCE_DOC_TYPE = new Integer(5);  //String

  /**
   * FieldId for the TargetDocType of the GridTalkMappingRule.
   */
  public static final Number TARGET_DOC_TYPE = new Integer(6);  //String

  /**
   * FieldId for the SourceDocFileType of the GridTalkMappingRule.
   */
  public static final Number SOURCE_DOC_FILE_TYPE = new Integer(7);  //String

  /**
   * FieldId for the TargetDocFileType of the GridTalkMappingRule.
   */
  public static final Number TARGET_DOC_FILE_TYPE = new Integer(8);  //String

  /**
   * FieldId for the HeaderTransformation flag of the GridTalkMappingRule.
   */
  public static final Number HEADER_TRANSFORMATION = new Integer(9);  //Boolean

  /**
   * FieldId for the TransformWithHeader flag of the GridTalkMappingRule.
   */
  public static final Number TRANSFORM_WITH_HEADER = new Integer(10);  //Boolean

  /**
   * FieldId for the TransformWithSource flag of the GridTalkMappingRule.
   */
  public static final Number TRANSFORM_WITH_SOURCE = new Integer(11);  //Boolean

  /**
   * FieldId for the MappingRule. A
   * {@link com.gridnode.pdip.app.mapper.model.MappingRule MappingRule}.
   */
  public static final Number MAPPING_RULE  = new Integer(12); //MappingRule

  public static final Number MAPPING_CLASS = new Integer(13);
}