/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizCertMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.partnerprocess;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in BizCertMapping entity (for P-Tier).
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IBizCertMapping
{
  /**
   * Name of BizCertMapping entity.
   */
  public static final String  ENTITY_NAME = "BizCertMapping";

  /**
   * FieldId for the UID for a BizCertMapping entity instance. A Long.
   */
  public static final Number UID = new Integer(0); //Long

  /**
   * FieldId for Whether-the-BizCertMapping-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1); //Boolean

  /**
   * FieldId for PartnerId. A String.
   */
  public static final Number PARTNER_ID = new Integer(3);  //String(15)

  /**
   * FieldId for PartnerCert. A Certificate.
   */
  public static final Number PARTNER_CERT = new Integer(4);

  /**
   * FieldId for OwnCert. A Certificate.
   */
  public static final Number OWN_CERT = new Integer(5);


}