/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTShellExecutableEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IProcedureType;
import com.gridnode.gtas.model.userprocedure.IShellExecutable;

// These entities are embedded in a field of IGTUserProcedureEntity whose manager
// is thus responsible for them.
public interface IGTShellExecutableEntity extends IGTEntity
{
  // Type (Enumeration for EXEC_TYPE)
  public static final Integer TYPE_EXECUTABLE           = IProcedureType.PROC_TYPE_EXEC;
  public static final Integer TYPE_JAVA                 = IProcedureType.PROC_TYPE_JAVA;

  // Fields
  public static final Number EXEC_ARGUMENTS             = IShellExecutable.ARGUMENTS;
  public static final Number EXEC_TYPE                  = IShellExecutable.TYPE;
}
