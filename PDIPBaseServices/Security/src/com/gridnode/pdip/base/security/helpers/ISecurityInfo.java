/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 29-May-2002    Jagadeesh           Created.
 */


package com.gridnode.pdip.base.security.helpers;

/**
 * The ISecurityInfo is the root interface of all SecurityInfo Value Objects. It
 * defines the standard encoding and encrypt and decrypt Key Length.
 *
 * The User Docuemtn is encryptd and decrypted using a file identifier.
 *
 * This interface enables extensibility of the Value Object implementation, to more
 * specific types.
 */

import java.io.Serializable;

public interface ISecurityInfo extends Serializable
{
    public static final String ENCODING = "ISO-8859-1";

    public static final String PREFIX = "~DF";

    public static final int ENCRYPT_STRING_KEYLENGTH=64;

    public static final String DIGEST_ALGORITHM_MD5 = "MD5";

    public static final String DIGEST_ALGORITHM_SHA1 = "SHA1";
	//added by Nazir on 10/20/2015
	public static final String DIGEST_ALGORITHM_SHA224 = "SHA224";
	public static final String DIGEST_ALGORITHM_SHA256 = "SHA256";
	public static final String DIGEST_ALGORITHM_SHA384 = "SHA384";
	public static final String DIGEST_ALGORITHM_SHA512 = "SHA512";


    public static final byte[] FILE_ID_SIGN = {'G','R','I','D','D','O','C','1','.','0'};

    public static final byte[] FILE_ID_NOT_SIGN = {'G','R','I','D','D','O','C','1','.','1'};


    /**
     * Security Level settings
     * Security Level 1:both payload and data in message are encrypted
     * Security Level 2:only payload in message is encrypted
     * Security Level 2:only data in message is encrypted
     */

    public static final int SECURITY_LEVEL_1 = 1;
    public static final int SECURITY_LEVEL_2 = 2;
    public static final int SECURITY_LEVEL_3 = 3;


    /**
     * Set the data that need's to be encrypted.
     * @param encryptdata - byte array of data to encrypt.
     */
    public void setEncryptedData(byte[] encryptdata);

     /**
      * Set the data that need's to be decrypted.
      * @param decryptData - byte array of data to decrypt.
      */

    public void setDecryptedData(byte[] decryptData);

     /**
      * Get the encrypted OutptuData in byte array.
      * @return - byte array of encrypted OutputData.
      */

    public byte[] getEncryptedOutputByte();

    /**
     * Get the Decrypted OutputData in byte array.
     * @return - byte array of decrypted data.
     */


    public byte[] getDecryptedOutputByte();

  }