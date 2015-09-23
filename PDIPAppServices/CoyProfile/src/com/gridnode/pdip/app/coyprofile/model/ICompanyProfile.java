/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICompanyProfile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in CompanyProfile entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface ICompanyProfile
{
  /**
   * Name for CompanyProfile entity.
   */
  public static final String ENTITY_NAME = "CompanyProfile";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Long

  /**
   * FieldId for CoyName. A String.
   */
  public static final Number COY_NAME = new Integer(1);  //string(255)

  /**
   * FieldId for Email. A String.
   */
  public static final Number EMAIL = new Integer(2);  //string(255)

  /**
   * FieldId for AltEmail. A String.
   */
  public static final Number ALT_EMAIL = new Integer(3);  //string(255)

  /**
   * FieldId for Tel. A String.
   */
  public static final Number TEL = new Integer(4);  //string(16)

  /**
   * FieldId for AltTel. A String.
   */
  public static final Number ALT_TEL = new Integer(5);  //string(16)

  /**
   * FieldId for Fax. A String.
   */
  public static final Number FAX = new Integer(6);  //string(16)

  /**
   * FieldId for Address. A String.
   */
  public static final Number ADDRESS = new Integer(7); //string(255)

  /**
   * FieldId for City. A String.
   */
  public static final Number CITY = new Integer(8); //string(50)

  /**
   * FieldId for State. A String.
   */
  public static final Number STATE = new Integer(9); //string(6)

  /**
   * FieldId for ZipCode. A String.
   */
  public static final Number ZIP_CODE = new Integer(10); //string(10)

  /**
   * FieldId for Country. A String of 3-char code.
   */
  public static final Number COUNTRY = new Integer(11); //string(3)

  /**
   * FieldId for Langauge. A String of 2-char code.
   */
  public static final Number LANGUAGE = new Integer(12); //string(3)

  /**
   * FieldId for IsPartner. A Boolean
   */
  public static final Number IS_PARTNER = new Integer(13); //boolean

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(14); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(15); //boolean

}