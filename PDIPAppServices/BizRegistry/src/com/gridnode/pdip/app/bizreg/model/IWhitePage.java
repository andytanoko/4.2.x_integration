/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWhitePage.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in WhitePage entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IWhitePage
{
  /**
   * Name for WhitePage entity.
   */
  public static final String ENTITY_NAME = "WhitePage";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Long

  /**
   * FieldId for BeUID. A Number.
   */
  public static final Number BE_UID = new Integer(1); //Long

  /**
   * FieldId for BusDesc. A String.
   */
  public static final Number BUSINESS_DESC = new Integer(2);  //string(80)

  /**
   * FieldId for DUNS. A String.
   */
  public static final Number DUNS = new Integer(3);  //string(14)

  /**
   * FieldId for GlobalSupplyChainCode. A String.
   */
  public static final Number G_SUPPLY_CHAIN_CODE = new Integer(4); //string(30)

  /**
   * FieldId for ContactPerson. A String.
   */
  public static final Number CONTACT_PERSON = new Integer(5);  //string(80)

  /**
   * FieldId for Email. A String.
   */
  public static final Number EMAIL = new Integer(6);  //string(50)

  /**
   * FieldId for Tel. A String.
   */
  public static final Number TEL = new Integer(7);  //string(16)

  /**
   * FieldId for Fax. A String.
   */
  public static final Number FAX = new Integer(8);  //string(16)

  /**
   * FieldId for Website. A String.
   */
  public static final Number WEBSITE = new Integer(9);  //string(80)

  /**
   * FieldId for Address. A String.
   */
  public static final Number ADDRESS = new Integer(10); //string(128)

  /**
   * FieldId for PoBox. A String.
   */
  public static final Number PO_BOX = new Integer(11); //string(10)

  /**
   * FieldId for City. A String.
   */
  public static final Number CITY = new Integer(12); //string(50)

  /**
   * FieldId for State. A String.
   */
  public static final Number STATE = new Integer(13); //string(6)

  /**
   * FieldId for ZipCode. A String.
   */
  public static final Number ZIP_CODE = new Integer(14); //string(10)

  /**
   * FieldId for Country. A String of 3-char code.
   */
  public static final Number COUNTRY = new Integer(15); //string(3)

  /**
   * FieldId for Langauge. A String of 3-char code.
   */
  public static final Number LANGUAGE = new Integer(16); //string(3)


}