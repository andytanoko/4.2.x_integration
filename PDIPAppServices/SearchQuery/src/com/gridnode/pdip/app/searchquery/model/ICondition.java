/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.searchquery.model;


public interface ICondition
{
  public static final String EQUAL_OPERATOR          = "=";
  public static final String NOT_EQUAL_OPERATOR      = "<>";
  public static final String LESS_OPERATOR           = "<";
  public static final String LESS_EQUAL_OPERATOR     = "<=";
  public static final String GREATER_OPERATOR        = ">";
  public static final String GREATER_EQUAL_OPERATOR  = ">=";
  public static final String IN_OPERATOR             = "IN";
  public static final String BETWEEN_OPERATOR        = "BETWEEN";
  public static final String NOT_OPERATOR            = "NOT";
  public static final String LIKE_OPERATOR           = "LIKE";
  public static final String LOCATE_OPERATOR         = "LOCATE";

  public static final Short EQUAL          = new Short("0");
  public static final Short NOT_EQUAL      = new Short("1");
  public static final Short LESS           = new Short("2");
  public static final Short LESS_EQUAL     = new Short("3");
  public static final Short GREATER        = new Short("4");
  public static final Short GREATER_EQUAL  = new Short("5");
  public static final Short IN             = new Short("6");
  public static final Number BETWEEN        = new Short("7");
  public static final Short NOT            = new Short("8");
  public static final Short LIKE           = new Short("9");
  public static final Short LOCATE         = new Short("10");

  /**
   * Name of Condition entity.
   */
  public static final String  ENTITY_NAME = "Condition";

  /**
   * FieldId for the field of the entity to query. A Number.
   */
  public static final Number FIELD         = new Integer(0); //Integer

  /**
   * XPath to query. A String.
   */
  public static final Number XPATH         = new Integer(1); //String(255)

  /**
   * FieldId for operator of this Condition. A String.
   */
  public static final Number OPERATOR  = new Integer(2); //Short

  /**
   * FieldId for the values to search for. A List.
   */
  public static final Number VALUES     = new Integer(3); //ArrayList

  /**
   * FieldId for the type of the Condition. A Short.
   */
  public static final Number TYPE        = new Integer(4); //Short

}