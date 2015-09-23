/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTJavaProcedureEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IJavaProcedure;
import com.gridnode.gtas.model.userprocedure.IProcedureType;

// These entities are embedded in a field of IGTUserProcedureEntity whose manager
// is thus responsible for them.
public interface IGTJavaProcedureEntity extends IGTEntity
{
  // Type (Enumeration for JAVA_TYPE)
  public static final Integer TYPE_EXECUTABLE           = IProcedureType.PROC_TYPE_EXEC;
  public static final Integer TYPE_JAVA                 = IProcedureType.PROC_TYPE_JAVA;

  // Fields
  public static final Number JAVA_CLASS_PATH            = IJavaProcedure.CLASS_PATH;
  public static final Number JAVA_CLASS_NAME            = IJavaProcedure.CLASS_NAME;
  public static final Number JAVA_METHOD_NAME           = IJavaProcedure.METHOD_NAME;
  public static final Number JAVA_IS_LOCAL              = IJavaProcedure.IS_LOCAL;
  public static final Number JAVA_JVM_OPTIONS           = IJavaProcedure.JVM_OPTIONS;
  public static final Number JAVA_ARGUMENTS             = IJavaProcedure.ARGUMENTS;
  public static final Number JAVA_TYPE                  = IJavaProcedure.TYPE;
}
