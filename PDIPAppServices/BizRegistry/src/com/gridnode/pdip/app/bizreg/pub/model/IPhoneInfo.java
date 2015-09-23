/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAddressInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * This interface defines the fields of a Phone information object.
 * A Phone information models a Telephone number.
 * A Typical implementation of a Phone information object would be a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IPhoneInfo
{
  /**
   * The number of fields in a Phone information object.
   */
  static final int NUM_FIELDS = 2;
  
  /**
   * Index to the Type field of the Phone information object.
   */
  static final int FIELD_TYPE = 0;
  
  /**
   * Index to the Number field of the Phone information object.
   */
  static final int FIELD_NUMBER = 1;
  
}
