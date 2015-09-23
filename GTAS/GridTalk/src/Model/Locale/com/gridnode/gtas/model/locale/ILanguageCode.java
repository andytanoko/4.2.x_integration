/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILanguageCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.locale;

/**
 * This interface defines the field IDs and constants for the LanguageCode
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ILanguageCode
{
  /**
   * Name of the LanguageCode entity.
   */
  static final String ENTITY_NAME       = "LanguageCode";

  /**
   * FieldID for Name which is a String.
   */
  static final Number NAME              = new Integer(1);

  /**
   * FieldID for Alpha2Code which is a 2-letter String.
   */
  static final Number ALPHA_2_CODE     = new Integer(2);

  /**
   * FieldID for BAlpha3Code which is a 3-letter String.
   */
  static final Number B_ALPHA_3_CODE     = new Integer(3);

  /**
   * FieldID for TAlpha3Code which is a 3-letter String.
   */
  static final Number T_ALPHA_3_CODE     = new Integer(4);
}