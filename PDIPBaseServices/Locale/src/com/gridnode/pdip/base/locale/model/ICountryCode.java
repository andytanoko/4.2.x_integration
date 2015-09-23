/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICountryCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.model;

/**
 * This interface defines the field IDs and constants for the CountryCode
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ICountryCode
{
  /**
   * Name of the CountryCode entity.
   */
  static final String ENTITY_NAME       = "CountryCode";

  /**
   * FieldID for Name which is a String.
   */
  static final Number NAME              = new Integer(1);

  /**
   * FieldID for NumericalCode which is an Integer.
   */
  static final Number NUMERICAL_CODE    = new Integer(2);

  /**
   * FieldID for Alpha2Code which is a 2-letter String.
   */
  static final Number ALPHA_2_CODE      = new Integer(3);

  /**
   * FieldID for Alpha3Code which is a 3-letter String.
   */
  static final Number ALPHA_3_CODE      = new Integer(4);

}