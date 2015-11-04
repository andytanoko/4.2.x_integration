/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISecurityInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 26 2002    Jagadeesh               Created
 * Sep 16 2002    Jagadeesh               Modified Field ID's for Certificates.
 * Sep 18 2002    Andrew Hill             Added constant defs for enum fields
 * Nov 23 2003    Guo Jianyu              Added compression
 * Nov 26 2003    Guo Jianyu              Added encryptionALgorithm
 */


package com.gridnode.gtas.model.channel;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in SecurityInfo entity.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public interface ISecurityInfo
{
  //20020918AH - added these constant definitions
  public static final String ENCRYPTION_TYPE_NONE       = "None";
  public static final String ENCRYPTION_TYPE_ASYMMETRIC = "Asymmetric";
  public static final String ENCRYPTION_TYPE_SMIME      = "S/MIME";

  public static final String SIGNATURE_TYPE_DEFAULT = "Default";
  public static final String SIGNATURE_TYPE_NONE = ENCRYPTION_TYPE_NONE;
  public static final String SIGNATURE_TYPE_SMIME = ENCRYPTION_TYPE_SMIME;

  public static final String DIGEST_ALGORITHM_MD5 = "MD5";
  public static final String DIGEST_ALGORITHM_SHA1 = "SHA1";
  //added by Nazir on 10/19/2015
  public static final String DIGEST_ALGORITHM_SHA224 = "SHA224";
  public static final String DIGEST_ALGORITHM_SHA256 = "SHA256";
  public static final String DIGEST_ALGORITHM_SHA384 = "SHA384";
  public static final String DIGEST_ALGORITHM_SHA512 = "SHA512";

  public static final String COMPRESSION_TYPE_NONE = ENCRYPTION_TYPE_NONE;
  public static final String COMPRESSION_TYPE_SMIME = ENCRYPTION_TYPE_SMIME;
  public static final String COMPRESSION_METHOD_ZLIB = "ZLIB";

  /**
   * Entity Name for Packaging Info.
   */
  public static final String ENTITY_NAME = "SecurityInfo";

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
   * FieldId for Encryption Type.
   */

  public static final Number ENCRYPTION_TYPE = new Integer(3); // varchar(80)

  /**
   * FieldId for Encryption Level.
   */

  public static final Number ENCRYPTION_LEVEL = new Integer(4); // varchar(80)

  /**
   * FieldId for Encryption Certificate ID.
   */

  public static final Number ENCRYPTION_CERTIFICATE_ID = new Integer(5); // varchar(80)

  /**
   * FieldId for Signature Type.
   */

  public static final Number SIGNATURE_TYPE = new Integer(6); // varchar(80)

  /**
   * FieldId for Digest Algorithm.
   */

  public static final Number DIGEST_ALGORITHM = new Integer(7); // varchar(80)

  /**
   * FieldId for SignatureEncryption Certificate ID.
   */

  public static final Number SIGNATURE_ENCRYPTION_CERTIFICATE_ID = new Integer(8); // varchar(80)


   /**
   * FieldId for CanDelete flag .
   */
  public static final Number CAN_DELETE         = new Integer(9);  //Boolean
  /**
   * FieldId for Partner Category. A Integer.
   */

  public static final Number PARTNER_CAT        = new Integer(11);


  /**
   * FieldId for RefrenceID.
   */

  public static final Number REF_ID              = new Integer(12);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_DISABLE         = new Integer(13);

  /**
   * FieldId for ISDISABLE.
   */

  public static final Number IS_PARTNER         = new Integer(14);

  /**
   * FieldId for compression Type. A String.
   */
  public static final Number COMPRESSION_TYPE = new Integer(15); // varchar(80)

  /**
   * FieldId for compression method.
   */
  public static final Number COMPRESSION_METHOD = new Integer(16); // varchar(80)

  /**
   * FieldId for Encryption Level.
   */
  public static final Number COMPRESSION_LEVEL = new Integer(17); // varchar(80)

  /**
   * FieldId for sequence.
   */
  public static final Number SEQUENCE = new Integer(18);

  /**
   * FieldId for encryptionAlgorithm
   */
  public static final Number ENCRYPTION_ALGORITHM = new Integer(19);

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
}


