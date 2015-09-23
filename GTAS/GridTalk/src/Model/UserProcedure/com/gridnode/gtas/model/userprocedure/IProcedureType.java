/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureType.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 23 2002    Daniel D'Cotta          Created
 */
package com.gridnode.gtas.model.userprocedure;

public interface IProcedureType
{
  public static final Integer PROC_TYPE_EXEC = new Integer(1);

  public static final Integer PROC_TYPE_JAVA = new Integer(2);

  public static final Integer PROC_TYPE_SOAP = new Integer(3);
}