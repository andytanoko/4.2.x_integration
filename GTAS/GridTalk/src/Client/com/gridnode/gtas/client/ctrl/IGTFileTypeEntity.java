/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFileTypeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.document.IFileType;

public interface IGTFileTypeEntity extends IGTEntity
{
  // Fields
  public static final Number UID = IFileType.UID;
  public static final Number FILE_TYPE = IFileType.FILE_TYPE;
  public static final Number DESCRIPTION = IFileType.DESCRIPTION;
  public static final Number PROGRAM_NAME = IFileType.PROGRAM_NAME;
  public static final Number PROGRAM_PATH = IFileType.PROGRAM_PATH;
  public static final Number PARAMETERS = IFileType.PARAMETERS;
  public static final Number WORKING_DIR = IFileType.WORKING_DIR;
  public static final Number CAN_DELETE = IFileType.CAN_DELETE;
}