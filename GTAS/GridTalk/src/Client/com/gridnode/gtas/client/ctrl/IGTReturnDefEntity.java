/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTReturnDefEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IAction;
import com.gridnode.gtas.model.userprocedure.IOperator;
import com.gridnode.gtas.model.userprocedure.IReturnDef;

// These entities are embedded in a field of IGTUserProcedureEntity whose manager
// is thus responsible for them.
public interface IGTReturnDefEntity extends IGTEntity
{
  // Action (Enumeration for RETURN_LIST_ACTION)
  public static final Integer ACTION_ABORT              = IAction.ABORT;
  public static final Integer ACTION_CONTINUE           = IAction.CONTINUE;

  // Operator (Enumeration for RETURN_LIST_OPERATOR)
  public static final Integer OPERATOR_BETWEEN          = IOperator.BETWEEN;
  public static final Integer OPERATOR_EQUAL            = IOperator.EQUAL;
  public static final Integer OPERATOR_GREATER          = IOperator.GREATER;
  public static final Integer OPERATOR_GREATER_OR_EQUAL = IOperator.GREATER_OR_EQUAL;
  public static final Integer OPERATOR_LESS             = IOperator.LESS;
  public static final Integer OPERATOR_LESS_OR_EQUAL    = IOperator.LESS_OR_EQUAL;
  public static final Integer OPERATOR_NOT_EQUAL        = IOperator.NOT_EQUAL;

  // Fields
  public static final Number RETURN_LIST_OPERATOR       = IReturnDef.OPERATOR;
  public static final Number RETURN_LIST_VALUE          = IReturnDef.VALUE;
  public static final Number RETURN_LIST_ACTION         = IReturnDef.ACTION;
  public static final Number RETURN_LIST_ALERT          = IReturnDef.ALERT;
}
