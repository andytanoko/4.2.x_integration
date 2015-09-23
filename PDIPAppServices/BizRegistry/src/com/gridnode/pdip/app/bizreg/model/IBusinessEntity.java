/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBusinessEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 * Sep 30 2002    Neo Sok Lay         Add PartnerCategory field.
 * Dec 22 2003    Neo Sok Lay         Add fieldID for DomainIdentifiers field.
 */
package com.gridnode.pdip.app.bizreg.model;

/**
 * This interface defines the the properties and FieldIds for accessing fields
 * in BusinessEntity entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public interface IBusinessEntity
{
  /**
   * Name of the BusinessEntity entity.
   */
  public static final String ENTITY_NAME = "BusinessEntity";

  /**
   * FieldId for UID of a BusinessEntity entity instance. A Number.
   */
  public static final Number UID                  = new Integer(0);  //integer

  /**
   * FieldId for EnterpriseId of a BusinessEntity entity instance. A String.
   */
  public static final Number ENTERPRISE_ID        = new Integer(1);  //String

  /**
   * FieldId for BusinessEntityId of a BusinessEntity entity instance. A String.
   */
  public static final Number ID                   = new Integer(2);  //string(4)

  /**
   * FieldId for BusinessEntityDescription of a BusinessEntity entity instance.
   * A String.
   */
  public static final Number DESCRIPTION          = new Integer(3);  //string(80)

  /**
   * FieldId for IsPartner flag of a BusinessEntity entity instance.
   * A Boolean.
   */
  public static final Number IS_PARTNER       = new Integer(4);  //boolean

  /**
   * FieldId for IsPublishable flag of a BusinessEntity entity instance.
   * A Boolean.
   */
  public static final Number IS_PUBLISHABLE    = new Integer(5);  //boolean

  /**
   * FieldId for IsSyncToServer flag of a Business entity instance.
   * A Boolean.
   */
  public static final Number IS_SYNC_TO_SERVER   = new Integer(6);  //Boolean

  /**
   * FieldId for State of a BusinessEntity entity instance. An integer.
   */
  public static final Number STATE              = new Integer(7);  //integer

  /**
   * FieldId for CanDelete flag of a BusinessEntity instance. A Boolean.
   */
  public static final Number CAN_DELETE         = new Integer(8);  //Boolean

  /**
   * FieldId for Version of a BusinessEntity instance, A Double.
   */
  public static final Number VERSION            = new Integer(9);  //Double

  /**
   * FieldId for WhitePage of a BusinessEntity entity instance. A
   * {@link WhitePage}.
   */
  public static final Number WHITE_PAGE           = new Integer(10);  //WhitePage

  /**
   * FieldID for PartnerCategory if IsPartner is true. A Short.
   */
  public static final Number PARTNER_CATEGORY     = new Integer(11);

  /**
   * FieldID for DomainIdentifiers. A Collection of DomainIdentifier.
   */
  public static final Number DOMAIN_IDENTIFIERS   = new Integer(12);
  
  // Values for STATE
  /**
   * Normal state. Default for all new BusinessEntities if one is not set.
   * Value = 0.
   */
  public static final int STATE_NORMAL  = 0;

  /**
   * Deleted state. The BusinessEntity is marked for deletion. Value = 1.
   */
  public static final int STATE_DELETED = 1;
}
