/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.model.partner;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in Partner entity.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPartner
{
  /**
   * Name for Partner entity.
   */
  public static final String  ENTITY_NAME = "Partner";

 //Possible values for STATE
  public static final short STATE_DISABLED = 0;
  public static final short STATE_ENABLED  = 1;
  public static final short STATE_DELETED  = 2;

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Integer

  /**
   * FieldId for CanDelete.
   */
  public static final Number CAN_DELETE = new Integer(2); // Boolean

  /**
   * FieldId for Partner ID. A String.
   */
  public static final Number PARTNER_ID = new Integer(3);  //String

  /**
   * FieldId for the Name of the Partner. A String.
   */
  public static final Number NAME = new Integer(4);  //String

  /**
   * FieldId for Description. A String.
   */
  public static final Number DESCRIPTION = new Integer(5);  //String

  /**
   * FieldId for Partner Type. A
   * {@link com.gridnode.pdip.app.partner.model.PartnerType PartnerType}.
   */
  public static final Number PARTNER_TYPE = new Integer(6);  //PartnerType

  /**
   * FieldId for PartnerGroup. A
   * {@link com.gridnode.pdip.app.partner.model.PartnerGroup PartnerGroup}.
   */
  public static final Number PARTNER_GROUP = new Integer(7);  //PartnerGroup

  /**
   * FieldId for create time. A Timestamp
   */
  public static final Number CREATE_TIME = new Integer(8);  //Timestamp

  /**
   * FieldId for create by. A String
   */
  public static final Number CREATE_BY = new Integer(9);  //String(15)
  /**
   * FieldId for State. A Short.
   */
  public static final Number STATE = new Integer(10);  //Short
}