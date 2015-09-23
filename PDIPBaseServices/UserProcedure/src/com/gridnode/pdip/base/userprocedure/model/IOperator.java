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
 * July 31 2002    Jagadeesh              Created
 */


package com.gridnode.pdip.base.userprocedure.model;


public interface IOperator
{

  public static final int EQUAL            = 0;

  public static final int LESS             = 1;

  public static final int LESS_OR_EQUAL    = 2;

  public static final int GREATER_OR_EQUAL = 3;

  public static final int GREATER          = 4;

  public static final int NOT_EQUAL        = 5;

  public static final int BETWEEN          = 6;

}