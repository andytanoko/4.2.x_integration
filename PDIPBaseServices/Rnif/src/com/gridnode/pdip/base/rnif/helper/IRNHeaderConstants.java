/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRNHeaderConstants.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2003    Jagadeesh           Created
 */



package com.gridnode.pdip.base.rnif.helper;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public interface IRNHeaderConstants
{

  /**
   * Gridnode RosettaNet
   */
  public static final String GN_MESSAGE_RESPONSE_KEY = "x-GN-RN-Response";
  public static final String GN_MESSAGE_ID_KEY = "x-gn-message-id";

  public static final String SENDER_DUNS= "SenderDUNS";
  public static final String RNDOC_FLAG= "IS_RN_DOC";
  public static final String AUDIT_FILE_NAME = "AuditFileName";
  public static final String SIGNATURE_VERIFY_EXCEPTION = "SignatureVerifyException";


  /**
   * RosettaNet defined
   */
  public static final String CONTENT_TYPE_KEY = "Content-Type";
  public static final String TYPE_KEY = "type";

  public static final String RN_VERSION_KEY = "x-RN-Version";
  public static final String RN_RESPONSE_TYPE_KEY = "x-RN-Response-Type";
  public static final String RN_RESPONSE_TYPE_SYNC = "sync";
  public static final String RN_RESPONSE_TYPE_ASYNC = "async";


}