/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureDefFile.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 27 2002    Jagadeesh               Created
 */


package com.gridnode.gtas.model.userprocedure;


public interface IJavaProcedure
{

  public static final String ENTITY_NAME="JavaProcedure";

  public static final Number CLASS_PATH = new Integer(0);

  public static final Number CLASS_NAME = new Integer(1);

  public static final Number METHOD_NAME = new Integer(2);

  public static final Number IS_LOCAL = new Integer(3);

  public static final Number JVM_OPTIONS = new Integer(4);

  public static final Number ARGUMENTS = new Integer(5);

  public static final Number TYPE = new Integer(6);

  //** Need to confirm  to have this field id or not.
  //public static final Number CMD_LINE_EXPR = new Integer(7);

}