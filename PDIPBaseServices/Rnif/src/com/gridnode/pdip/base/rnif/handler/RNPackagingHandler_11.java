/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNPackagingHandler_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 20 2003      Guo Jianyu              Created
 */
package com.gridnode.pdip.base.rnif.handler;

import com.gridnode.pdip.base.packaging.helper.IPackagingInfo;
/**
 * The packaging handler for RNIF 1.1.
 * The main differences from RNPackagingHandler_20 lie in the HTTP headers.
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since 1.0
 */
public class RNPackagingHandler_11 extends RNPackagingHandler
{

  public static final String SENDER_DUNS= "SenderDUNS";
  public static final String RNDOC_FLAG= "IS_RN_DOC";

  private static final String CLASS_NAME= "RNPackagingHandler_11";

  public RNPackagingHandler_11()
  {
    super(IPackagingInfo.RNIF1_ENVELOPE_TYPE);
  }

  protected String getClassName()
  {
    return CLASS_NAME;
  }

/*
  public Hashtable getHTTPHeaders(RNPackInfo packinfo, Hashtable headers)
  {
    GNTransportHeader header= new GNTransportHeader(headers);
    header.setContentType("application/x-rosettanet-agent; version=1.0");

    return header.getProperties();
  }
*/
}