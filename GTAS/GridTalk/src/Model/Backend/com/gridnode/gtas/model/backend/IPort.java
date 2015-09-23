/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPort.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 26 2003    Jagadeesh           Added: Fields to support Port Enhancement.
 * Aug 23 2005    Tam Wei Xiang       Field ATTACHMENT_DIR has been deleted.
 * Mar 03 2006    Tam Wei Xiang       Added new field 'FILE_GROUPING'
 */
package com.gridnode.gtas.model.backend;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Port entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IPort
{
  /**
   * Name for Port entity.
   */
  public static final String ENTITY_NAME = "Port";

  /**
   * FieldId for UID. A Number.
   */
  public static final Number UID = new Integer(0);  //Integer

  /**
   * FieldId for CanDelete flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1);  //Boolean

  /**
   * FieldId for Name. A String.
   */
  public static final Number NAME = new Integer(3);  //String(15)

  /**
   * FieldId for Desc. A String.
   */
  public static final Number DESCRIPTION = new Integer(4);  //String(80)

  /**
   * FieldId for HostDir. A String.
   */
  public static final Number HOST_DIR = new Integer(5);  //String(80)

  /**
   * FieldId for IsRfc flag. A Boolean.
   */
  public static final Number IS_RFC = new Integer(6);  //Boolean

  /**
   * FieldId for Rfc. A {@link com.gridnode.gridtalk.entity.Rfc Rfc}.
   */
  public static final Number RFC = new Integer(7);  //Rfc

  /**
   * FieldId for IsDiffFileName flag. A Boolean.
   */
  public static final Number IS_DIFF_FILE_NAME = new Integer(8);  //Boolean

  /**
   * FieldId for IsOverwrite flag. A Boolean.
   */
  public static final Number IS_OVERWRITE = new Integer(9);  //Boolean

  /**
   * FieldId for FileName. A String.
   */
  public static final Number FILE_NAME = new Integer(10);  //String(80)

  /**
   * FieldId for IsAddFileExt flag. A Boolean.
   */
  public static final Number IS_ADD_FILE_EXT = new Integer(11);  //Boolean

  /**
   * FieldId for FileExtType. A Integer.
   */
  public static final Number FILE_EXT_TYPE = new Integer(12);  //Integer

  /**
   * FieldId for FileExtValue. A String.
   */
  public static final Number FILE_EXT_VALUE = new Integer(13);  //String(64)

  /**
   * FieldId for AttachmentDir. A String.
   */
  //public static final Number ATTACHMENT_DIR = new Integer(14);  //String(80)

  /**
   * FieldId for IsExportGdoc flag. A Boolean.
   */
  public static final Number IS_EXPORT_GDOC = new Integer(15);  //Boolean


  /**
   * FieldId for minimum number for running no sequence.
   */
  public static final Number START_NUM = new Integer(16); //Integer

  /**
   * FieldId for maximum sequence for running no.
   */
  public static final Number ROLLOVER_NUM = new Integer(17); //Integer

  /**
   * FieldId for current no.
   */
  public static final Number NEXT_NUM = new Integer(18); //Integer

  /**
   * FieldId for isPadded.
   */
  public static final Number IS_PADDED = new Integer(19); //Integer

  /**
   * FieldId for Fixed Number Length.
   */
  public static final Number FIX_NUM_LENGTH = new Integer(20); //Integer
  
  /**
   * FieldId for Fixed Number File_Grouping.
   */
  public static final Number FILE_GROUPING = new Integer(21);  //Integer

  // FileExtType constants
  /**
   * FileExt Type: DateTime
   */
  public static final Integer DATE_TIME = new Integer(1);

  /**
   * FileExt Type: GDoc
   */
  public static final Integer GDOC = new Integer(2);

  /**
   * FileExt Type: Sequential Running No.
   */
  public static final Integer SEQ_RUNNING_NUM = new Integer(3);


}
