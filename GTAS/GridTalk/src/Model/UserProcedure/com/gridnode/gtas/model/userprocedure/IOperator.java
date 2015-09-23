/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IOperator.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 23 2002    Daniel D'Cotta          Created
 */
package com.gridnode.gtas.model.userprocedure;

public interface IOperator
{
  public static final Integer EQUAL            = new Integer(0);

  public static final Integer LESS             = new Integer(1);

  public static final Integer LESS_OR_EQUAL    = new Integer(2);

  public static final Integer GREATER_OR_EQUAL = new Integer(3);

  public static final Integer GREATER          = new Integer(4);

  public static final Integer NOT_EQUAL        = new Integer(5);

  public static final Integer BETWEEN          = new Integer(6);
}