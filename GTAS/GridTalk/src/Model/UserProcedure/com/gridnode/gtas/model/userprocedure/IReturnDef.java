/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReturnDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 23 2002    Daniel D'Cotta          Created
 */
package com.gridnode.gtas.model.userprocedure;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in UserProcedure entity.
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */
public interface IReturnDef
{
  public static final String  ENTITY_NAME = "ReturnDef";

  public static final Number OPERATOR = new Integer(1);

  public static final Number VALUE = new Integer(2);

  public static final Number ACTION = new Integer(3);

  public static final Number ALERT = new Integer(4);
}