/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAS2Headers.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Apr 19 2004      Guo Jianyu              Created
 * Aug 30 2006      Neo Sok Lay             Add PAYLOAD_FILENAME
 */
package com.gridnode.pdip.framework.messaging;

public interface IAS2Headers
{
  public static final String HEADER_CATEGORY =
    IHeaderCategory.CATEGORY_COMMON_HEADERS;

  public static final String IS_ACK_REQ = "IsAckRequired";
  public static final String IS_ACK_SIGNED = "IsAckToBeSigned";
  public static final String IS_ACK_SYNC = "IsAckSync";
  public static final String RETURN_URL = "RETURN URL";

  public static final String CONTENT_TYPE = "CONTENT-TYPE";
  public static final String SUBJECT = "SUBJECT";
  public static final String MESSAGE_ID = "MESSAGE-ID";
  public static final String AS2_VERSION = "AS2-VERSION";
  public static final String AS2_FROM = "AS2-FROM";
  public static final String AS2_TO = "AS2-TO";
  public static final String ORIGINAL_MESSAGE_ID = "ORIGINAL-MESSAGE-ID";
  public static final String DISPOSITION = "DISPOSITION";
  public static final String DISPOSITION_NOTIFICATION_TO = "DISPOSITION-NOTIFICATION-TO";
  public static final String DISPOSITION_NOTIFICATION_OPTIONS = "DISPOSITION-NOTIFICATION-OPTIONS";
  public static final String SIGNED_RECEIPT_PROTOCOL = "signed-receipt-protocol";
  public static final String SIGNED_RECEIPT_MICALG = "signed-receipt-micalg";
  public static final String PKCS7_SIGNATURE = "pkcs7-signature";
  public static final String SHA1 = "sha1";
  public static final String MD5 = "md5";
  //added by Nazir on 10/19/2015
  public static final String SHA224 = "sha224";
  public static final String SHA256 = "sha256";
  public static final String SHA384 = "sha384";
  public static final String SHA512 = "sha512";
  
  public static final String REQUIRED = "required";
  public static final String OPTIONAL = "optional";
  public static final String RECEIPT_DELIVERY_OPTION = "RECEIPT-DELIVERY-OPTION";
  public static final String DATE = "DATE";
  public static final String MIME_VERSION = "MIME-VERSION";
  public static final String RECEIVED_CONTENT_MIC = "RECEIVED-CONTENT-MIC";
  public static final String DISPOSITION_TYPE = "DISPOSITION_TYPE";

  public static final String SYNC = "sync";
  public static final String ASYNC = "async";
  public static final String MIC = "MIC";
  public static final String AUDIT_FILE_NAME = "AuditFileName";
  public static final String ERROR = "ERROR";
  public static final String MICALG = "MICALG";
  public static final String MDN = "MDN";
  public static final String PROCESSED = "processed";
  public static final String PAYLOAD_FILENAME = "PayloadFilename";
}