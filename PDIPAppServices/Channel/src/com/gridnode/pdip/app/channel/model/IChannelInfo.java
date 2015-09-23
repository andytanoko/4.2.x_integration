/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Add new fields and Modified field names.
 * Sep 16 2002    Jagadeesh               Added Master/Partner Channel Flags.
 * Sep 17 2002    Jagadeesh               Added Packaging/Security Profiles.
 * Oct 03 2002    Ang Meng Hua            Added CanDelete flag
 * Nov 26 2002    Jagadeesh               Added isRelay Filed to identify Relay Channel.
 */

package com.gridnode.pdip.app.channel.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in ChannelInfo entity.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IChannelInfo
{
  /**
   * Entity Name for ChannelInfo.
   */
  public static final String ENTITY_NAME = "ChannelInfo";

  /**
   * FieldId for UId.
   */
  public static final Number UID = new Integer(0); // bigint(20)

  /**
   * FieldId for Name.
   */
  public static final Number NAME = new Integer(1); // varchar(30)

  /**
   * FieldId for Reference Id.
   */
  public static final Number REF_ID = new Integer(2); // varchar(30)

  /**
   * FieldId for Type of Transport Protocol.
   */
  public static final Number TPT_PROTOCOL_TYPE = new Integer(3); // varchar(10)

  /**
   * FieldId for Communication information of the Channel.
   */
  public static final Number TPT_COMM_INFO = new Integer(4); // bigint(20)

  /**
   * FieldId for Description of Channel.
   */
  public static final Number DESCRIPTION = new Integer(5); // varchar(80)

  /**
   * FieldId for Version of ChannelInfo.
   */
  public static final Number VERSION = new Integer(6); // double

  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_MASTER = new Integer(7); //Boolean

  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_PARTNER = new Integer(8); //Boolean

  /**
   * FieldId for PackagingProfile. A Integer.
   */

  public static final Number PACKAGING_PROFILE = new Integer(9); //UID.

  /**
   * FieldId for SecurityProfile. A Integer.
   */

  public static final Number SECURITY_PROFILE = new Integer(10); //UID.

  /**
  * FieldId for CanDelete flag .
  */
  public static final Number CAN_DELETE = new Integer(11); //Boolean

  /**
   * FieldId for Partner Category. A Integer.
   */

  public static final Number PARTNER_CAT = new Integer(12);

  /**
   * FieldId for ISDisable. A Integer.
   */

  public static final Number IS_DISABLE = new Integer(13);

  /**
   * FieldId for ISRelay. A Integer.
   */

  public static final Number IS_RELAY = new Integer(14);

  /**
   * FieldId for FlowControl Profile.
   */

  public static final Number FLOWCONTROL_PROFILE = new Integer(15);

}