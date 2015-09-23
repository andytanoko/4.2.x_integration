/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Jan 07 2003    Goh Kan Mun             Modified - Added fieldIds for split, splitSize
 *                                                   and splitThreshold.
 * Jan 17 2003    Goh Kan Mun             Modified - Added BackwardCompatible type.
 * Mar 21 2003    Goh Kan Mun             Modified - Change Envelope Type name from
 *                                                   BackwardCompatible to FileSplit.
 * Oct 29 2003    Guo Jianyu              Added AS2_ENVELOPE_TYPE
 * Oct 30 2003    Guo Jianyu              Added PKG_INFO_EXTENSION
 * 2006-01-19			SC											Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 */

package com.gridnode.pdip.app.channel.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in PackagingInfo entity.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */

public interface IPackagingInfo
{

  /**
   * Entity Name for Packaging Info.
   */
  public static final String ENTITY_NAME = "PackagingInfo";

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
   * FieldId for Envelope.
   */

  public static final Number ENVELOPE = new Integer(3); // varchar(80)

//  /**
//   * FieldId for Zip.
//   */
//
//  public static final Number ZIP = new Integer(4); // varchar(80)

//  /**
//   * FieldId for ZipThreshold.
//   */
//
//  public static final Number ZIPTHRESHOLD = new Integer(5); // varchar(80)

  /**
  * FieldId for CanDelete flag .
  */
  public static final Number CAN_DELETE = new Integer(6); //Boolean

  /**
   * FieldId for Version.
   */
  public static final Number VERSION = new Integer(7); //Double

  /**
   * FieldId for Partner Category. A Integer.
   */

  public static final Number PARTNER_CAT = new Integer(8);

  /**
   * FieldId for RefrenceID.
   */

  public static final Number REF_ID = new Integer(9);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_DISABLE = new Integer(10);

  /**
   * FieldId for isMaster. A Integer.
   */
  public static final Number IS_PARTNER = new Integer(11); //Boolean

  /**
   * FieldId for Split.
   */

  public static final Number SPLIT = new Integer(12); // Boolean(80)

  /**
   * FieldId for splitThreshold.
   */

  public static final Number SPLIT_THRESHOLD = new Integer(13); // bigint(20)

  /**
   * FieldId for splitSize.
   */

  public static final Number SPLIT_SIZE = new Integer(14); // bigint(20)

  /**
   * FieldId for pkgInfoExtension
   */
  public static final Number PKG_INFO_EXTENSION = new Integer(15);
  // PackagingInfoExtension

  /** Envelope Type **/
  public static final String DEFAULT_ENVELOPE_TYPE = "NONE";

  public static final String RNIF1_ENVELOPE_TYPE = "RNIF1";

  public static final String RNIF2_ENVELOPE_TYPE = "RNIF2";

  public static final String FILE_SPLIT_ENVELOPE_TYPE = "FILESPLIT";

  public static final String AS2_ENVELOPE_TYPE = "AS2";

}
