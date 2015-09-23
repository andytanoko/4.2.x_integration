/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTConditionEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.searchquery.ICondition;

public interface IGTConditionEntity extends IGTEntity
{
  //Constants
  public static final String OPERATOR_EQUAL         = ICondition.EQUAL_OPERATOR;
  public static final String OPERATOR_NOT_EQUAL     = ICondition.NOT_EQUAL_OPERATOR;
  public static final String OPERATOR_LESS          = ICondition.LESS_OPERATOR;
  public static final String OPERATOR_LESS_EQUAL    = ICondition.LESS_EQUAL_OPERATOR;
  public static final String OPERATOR_GREATER       = ICondition.GREATER_OPERATOR;
  public static final String OPERATOR_GREATER_EQUAL = ICondition.GREATER_EQUAL_OPERATOR;
  public static final String OPERATOR_IN            = ICondition.IN_OPERATOR;
  public static final String OPERATOR_BETWEEN       = ICondition.BETWEEN_OPERATOR;
  public static final String OPERATOR_NOT           = ICondition.NOT_OPERATOR;
  public static final String OPERATOR_LIKE          = ICondition.LIKE_OPERATOR;
  public static final String OPERATOR_LOCATE        = ICondition.LOCATE_OPERATOR;

  public static final Short TYPE_GDOC               = ICondition.TYPE_GDOC;
  public static final Short TYPE_UDOC               = ICondition.TYPE_UDOC;

  //Fields
  public static final Number FIELD                  = ICondition.FIELD;     // Integer
  public static final Number XPATH                  = ICondition.XPATH;     // String(255)
  public static final Number OPERATOR               = ICondition.OPERATOR;  // String
  public static final Number VALUES                 = ICondition.VALUES;    // ArrayList
  public static final Number TYPE                   = ICondition.TYPE;      // Short
}
