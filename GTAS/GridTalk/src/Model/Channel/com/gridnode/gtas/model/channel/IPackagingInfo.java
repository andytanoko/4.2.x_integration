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
 * OCT 25 2002    Jagadeesh               Added: Constants for Envelope Type.
 * Oct 30 2003    Guo Jianyu              Added PKG_INFO_EXTENSION and AS2_ENVELOPE_TYPE
 * 2006-01-19			SC											Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 */

package com.gridnode.gtas.model.channel;

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
  public static final Number UID = new Integer(0);  // bigint(20)

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
  public static final Number CAN_DELETE         = new Integer(6);  //Boolean


  /**
   * FieldId for Partner Category. A Integer.
   */

  public static final Number PARTNER_CAT = new Integer(8);


  /**
   * FieldId for RefrenceID.
   */

  public static final Number REF_ID              = new Integer(9);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_DISABLE         = new Integer(10);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_PARTNER         = new Integer(11);

  /**
   * FieldId for pkgInfoExtension
   */
  public static final Number PKG_INFO_EXTENSION = new Integer(15); // PackagingInfoExtension


  // Values for PARTNER_CATEGORY
  /**
   * Possible value for PartnerCategory. This indicates "Others" category of
   * partner which is not listed otherwise.
   */
  public static final Short CATEGORY_OTHERS = new Short("0");

  /**
   * Possible value for PartnerCategory. This indicates "GridTalk" category of
   * partner.
   */
  public static final Short CATEGORY_GRIDTALK = new Short("1");

  /** Envelope Types for Packaging Services **/

  public static final String DEFAULT_ENVELOPE_TYPE = "NONE";

  public static final String RNIF1_ENVELOPE_TYPE = "RNIF1";

  public static final String RNIF2_ENVELOPE_TYPE ="RNIF2";

  public static final String AS2_ENVELOPE_TYPE ="AS2";

}


