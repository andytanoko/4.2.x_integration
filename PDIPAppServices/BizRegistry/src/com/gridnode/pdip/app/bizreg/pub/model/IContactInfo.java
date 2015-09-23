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
 * This interface defines the fields of a Contact information object.
 * A Contact information object contains the information of a contact person
 * of an organization.
 * Typical implementation of a Contact information object would be a String array.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IContactInfo
{
  /**
   * The number of fields in a Contact information object.
   */
  static final int NUM_FIELDS = 6;
  
  /**
   * Index to the Name field of the Contact information object.
   */
  static final int FIELD_CONTACT_NAME = 0;

  /**
   * Index to the Tel field of the Contact information object.
   */
  static final int FIELD_CONTACT_TEL = 1;

  /**
   * Index to the AltTel field of the Contact information object.
   */
  static final int FIELD_CONTACT_ALT_TEL = 2;

  /**
   * Index to the Fax field of the Contact information object.
   */
  static final int FIELD_CONTACT_FAX = 3;

  /**
   * Index to the Email field of the Contact information object.
   */
  static final int FIELD_CONTACT_EMAIL = 4;

  /**
   * Index to the AltEmail field of the Contact information object.
   */
  static final int FIELD_CONTACT_ALT_EMAIL = 5;

//  NOT SUPPORTED FOR UDDI
//  static final int FIELD_CONTACT_HOMEPAGE = 6;
  
  /**
   * Type description for the Tel field. 
   */
  static final String TEL_TYPE = "Main Tel";
  
  /**
   * Type description for the AltTel field.
   */
  static final String ALT_TEL_TYPE = "Alternate Tel";
  
  /**
   * Type description for the Fax field. 
   */
  static final String FAX_TYPE = "Fax";
  
  /**
   * Type description for the Email field. 
   */
  static final String EMAIL_TYPE = "Default Email";
  
  /**
   * Type description for the AltEmail field.
   */
  static final String ALT_EMAIL_TYPE = "Alternate Email";
  
}
