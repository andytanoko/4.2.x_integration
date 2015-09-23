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
 * This interface defines the fields of an Address information object.
 * An Address information object contains information regarding a Postal address.
 * Typical implementation of an Address information object is a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IAddressInfo
{
  /**
   * The number of fields in an Address information object.
   */
  static final int NUM_FIELDS = 5;
  
  /**
   * Index to the City field of the Address information object.
   */
  static final int FIELD_CITY = 0;
  
  /**
   * Index to the Country field of the Address information object.
   */
  static final int FIELD_COUNTRY = 1;
  
  /**
   * Index to the State field of the Address information object.
   */
  static final int FIELD_STATE = 2;
  
  /**
   * Index to the Street field of the Address information object.
   */
  static final int FIELD_STREET = 3;
  
  /**
   * Index to the ZipCode field of the Address information object.
   */
  static final int FIELD_ZIPCODE = 4;
  
}
