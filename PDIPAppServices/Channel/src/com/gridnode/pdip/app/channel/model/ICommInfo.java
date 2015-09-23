/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICommInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Add new fields and Modified field names.
 * Oct 03 2002    Ang Meng Hua            Added CanDelete flag
 * Nov 23 2002    Jagadeesh               Added URL field for this CommInfo.
 */

package com.gridnode.pdip.app.channel.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in CommInfo entity.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface ICommInfo
{
  /**
   * Entity Name for CommInfo.
   */
  public static final String ENTITY_NAME = "CommInfo";

  /**
   * FieldId for UId.
   */
  public static final Number UID = new Integer(0); // bigint(20)

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // varchar(30)

  /**
   * FieldId for Description.
   */
  public static final Number DESCRIPTION = new Integer(2); // varchar(80)

  /**
   * FieldId for isDefaultTpt.
   */
  public static final Number IS_DEFAULT_TPT = new Integer(3); // boolean

  /**
   * FieldId for HostName.
   */
  //  public static final Number HOST = new Integer(4); // varchar(30)

  /**
   * FieldId for Port Number.
   */
  //  public static final Number PORT = new Integer(5); // int(10)

  /**
   * FieldId for Protocol Type.
   */
  public static final Number PROTOCOL_TYPE = new Integer(4); // varchar(10)

  /**
   * FieldId for Version of Protocol.
   */
  //  public static final Number PROTOCOL_VERSION = new Integer(7); // varchar(10)

  /**
   * FieldId for Version of Implementation.
   */
  public static final Number TPT_IMPL_VERSION = new Integer(5); // varchar(6)

  /**
   * FieldId for Protocol Specific Information.
   */
  //public static final Number PROTOCOL_DETAIL = new Integer(9); // text

  /**
   * FieldId for Reference Id.
   */
  public static final Number REF_ID = new Integer(6); // varchar(30)

  /**
   * FieldId for Version for this data.
   */
  public static final Number VERSION = new Integer(7); // double

  /**
  * FieldId for CanDelete flag .
  */
  public static final Number CAN_DELETE = new Integer(8); //Boolean

  /**
   * FieldId for Partner Category. A Integer.
   */

  public static final Number PARTNER_CAT = new Integer(9);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_DISABLE = new Integer(10);

  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_PARTNER = new Integer(11); //Boolean

  /**
   * FieldId for URL.
   */
  public static final Number URL = new Integer(12);

  /**
   * Static constant for TPTImplVersion.
   */
  public static final String DEFAULT_TPTIMPL_VERSION = "030000";

  /**
   * Constant value for protocol type.
   */
  public static final String JMS = "JMS";

  /**
   * Constant value for protocol type.
   */
  public static final String HTTP = "HTTP";

  /**
   * Constant value for protocol type.
   */
  public static final String SOAP = "SOAP-HTTP";

  /**
   * Constant value for protocol type.
   */
  public static final String UPC = "UPC";

  /**
   * Constant value for URL Format.
   */
  public static final String URL_FORMAT =
    "protocol://[username:password@]host:port/URI";
}