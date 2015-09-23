/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchFilterOperator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.helpers;

public interface SearchFilterOperator
{
  public static final String AND_CONNECTOR                = "AND";
  public static final String OR_CONNECTOR                 =  "OR";
  public static final String EQUAL_OPERATOR               = "=";
  public static final String LESS_THAN_OPERATOR           = "<";
  public static final String LESS_THAN_EQUALTO_OPERATOR   = "<=";
  public static final String GREATER_THAN_OPERATOR        = ">";
  public static final String GREATER_THAN_EQUALTO_OPERATOR= "AND";
  public static final String IN_OPERATOR                  = "IN";
  public static final String BETWEEN_OPERATOR             = "BETWEEN";
  public static final String NOT_OPERATOR                 = "NOT";
  public static final String LIKE_OPERATOR                = "LIKE";
}