/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IIdentifierInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of an Identifier information object.
 * An Indentifier information object defines additional identifier information
 * of a registry information object.
 * A Typical implementation of an Identifier information object would be a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IIdentifierInfo
{
  /**
   * The number of fields in an Identifier information object.
   */
  static final int NUM_FIELDS = 3;
  
  /**
   * Index to the IdentificationSchemeKey field of the Identifier information object.
   */
  static final int FIELD_IDENTIFICATION_SCHEME_KEY = 0;

  /**
   * Index to the Name field of the Identifier information object.
   */
  static final int FIELD_IDENTIFIER_NAME = 1;

  /**
   * Index to the Value field of the Identifier information object.
   */
  static final int FIELD_IDENTIFIER_VALUE = 2;
}
