/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ProcessMapping entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IProcessMapping
{
  /**
   * Name of ProcessMapping entity.
   */
  public static final String  ENTITY_NAME = "ProcessMapping";

  /**
   * FieldId for the UID for a ProcessMapping entity instance. A Long.
   */
  public static final Number UID = new Integer(0); //Long

  /**
   * FieldId for Whether-the-ProcessMapping-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1); //Boolean

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(2); //Double

  /**
   * FieldId for ProcessDef. A String.
   */
  public static final Number PROCESS_DEF = new Integer(3);  //String(80)

  /**
   * FieldId for IsInitiatingRole. A Boolean.
   */
  public static final Number IS_INITIATING_ROLE = new Integer(4);

  /**
   * FieldId for DocType. A String.
   */
  public static final Number DOC_TYPE = new Integer(5);  //String(12)

  /**
   * FieldId for SendChannelUID. A Long.
   */
  public static final Number SEND_CHANNEL_UID = new Integer(6);

  /**
   * FieldId for PartnerId. A String.
   */
  public static final Number PARTNER_ID = new Integer(7);  //String(20)

  /**
   * FieldId for ProcessIndicatorCode. A String.
   */
  public static final Number PROCESS_INDICATOR_CODE = new Integer(8);  //String(80)

  /**
   * FieldId for ProcessVersionID. A String.
   */
  public static final Number PROCESS_VERSION_ID = new Integer(9);  //String(80)

  /**
   * FieldId for PartnerRoleMapping. A Long.
   */
  public static final Number PARTNER_ROLE_MAPPING = new Integer(10);



}