/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcedureDefFileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         listClassesInJar()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IProcedureDefFile;
import com.gridnode.gtas.model.userprocedure.IProcedureType;

import com.gridnode.gtas.client.GTClientException;
import java.util.Collection;

public interface IGTProcedureDefFileEntity extends IGTEntity
{
  // Type
  public static final Integer TYPE_EXECUTABLE     = IProcedureType.PROC_TYPE_EXEC;
  public static final Integer TYPE_JAVA           = IProcedureType.PROC_TYPE_JAVA;
  public static final Integer TYPE_SOAP           = IProcedureType.PROC_TYPE_SOAP; //20030730AH

  // Fields
  public static final Number UID                  = IProcedureDefFile.UID;
  public static final Number NAME                 = IProcedureDefFile.NAME;
  public static final Number DESCRIPTION          = IProcedureDefFile.DESCRIPTION;
  public static final Number TYPE                 = IProcedureDefFile.TYPE;
  public static final Number FILE_NAME            = IProcedureDefFile.FILE_NAME;
  public static final Number FILE_PATH            = IProcedureDefFile.FILE_PATH;
  public static final Number CAN_DELETE           = IProcedureDefFile.CAN_DELETE; //030130AMH

  public Collection listClassesInJar() throws GTClientException; //20030716AH
}
