/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMappingFileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 * 2003-02-18     Andrew Hill         Add TYPE_XPATH
 * 2003-02-26     Daniel D'Cotta      Added SUB_PATH
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.mapper.IMappingFile;

public interface IGTMappingFileEntity extends IGTEntity
{
  // Types for TYPE field
  public static final Short TYPE_XSL              = IMappingFile.XSL;
  public static final Short TYPE_CONVERSION_RULE  = IMappingFile.CONVERSION_RULE;
  public static final Short TYPE_REFERENCE_DOC    = IMappingFile.REFERENCE_DOC;
  public static final Short TYPE_DTD              = IMappingFile.DTD;
  public static final Short TYPE_SCHEMA           = IMappingFile.SCHEMA;
  public static final Short TYPE_DICT             = IMappingFile.DICT;
  public static final Short TYPE_XPATH            = IMappingFile.XPATH; //20030218AH
  public static final Short TYPE_JAVA_BINARY      = IMappingFile.JAVA_BINARY;

  // Entity fields
  public static final Number UID              = IMappingFile.UID;
  public static final Number CAN_DELETE       = IMappingFile.CAN_DELETE;
  public static final Number NAME             = IMappingFile.NAME;
  public static final Number DESCRIPTION      = IMappingFile.DESCRIPTION;
  public static final Number FILEPATH         = IMappingFile.PATH;
  public static final Number SUB_PATH         = IMappingFile.SUB_PATH;
  public static final Number TYPE             = IMappingFile.TYPE;
  public static final Number FILENAME         = IMappingFile.FILENAME;
  public static final Number MAPPNG_CLASS     = IMappingFile.MAPPING_CLASS;

  // The following can be factored out when metainfo is refactored in I4 :-)
  /*public String getTypeLabelKey() throws GTClientException;
  public String getTypeLabelKey(Short type) throws GTClientException;
  public Collection getAllowedTypes() throws GTClientException;*/
}