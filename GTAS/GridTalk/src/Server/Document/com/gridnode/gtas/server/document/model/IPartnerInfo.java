/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 13 2002    Koh Han Sing        Created
 * Oct 01 2003    Neo Sok Lay         Add fields: BIZ_ENTITY_UUID, REGISTRY_QUERY_URL
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in PartnerInfo entity.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2
 * @since 2.0
 */
public interface IPartnerInfo
{
  /**
   * Name for Activity entity.
   */
  public static final String ENTITY_NAME = "PartnerInfo";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID         = new Integer(0);  //Long

  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(2); //Double

  /**
   * FieldId for PartnerID. A String.
   */
  public static final Number PARTNER_ID    = new Integer(3);  //String

  /**
   * FieldId for PartnerName. A String.
   */
  public static final Number PARTNER_NAME  = new Integer(4);  //String

  /**
   * FieldId for PartnerType. A String.
   */
  public static final Number PARTNER_TYPE = new Integer(5);  //String

  /**
   * FieldId for PartnerGroup. A String.
   */
  public static final Number PARTNER_GROUP = new Integer(6);  //String

  /**
   * FieldId for NodeID. A Long.
   */
  public static final Number NODE_ID = new Integer(7);  //Long

  /**
   * FieldId for BizEntityID. A String.
   */
  public static final Number BIZ_ENTITY_ID = new Integer(8);  //String

  /**
   * FieldId for BizEntityUuid. A String.
   */
  public static final Number BIZ_ENTITY_UUID = new Integer(9);  //String

  /**
   * FieldId for RegistryQueryUrl. A String.
   */
  public static final Number REGISTRY_QUERY_URL = new Integer(10);  //String

}