/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRNConstant_11.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Created
 */
package com.gridnode.pdip.base.rnif.model;

public interface IRNConstant_11 extends IRNConstant
{
  public final static String CIDX_CODE = "CIDX";
  public final static String RNIF_VERSION = "1.1";// for preamble

  // Fixed values for rnsubtype of each part
  public final static String PREAMBLE_HEADER = "preamble-header";
  public final static String SERVICE_HEADER = "service-header";
  public final static String SERVICE_CONTENT = "service-content";

  public final static int RN_VERSION_NUMBER = 0x00010100;

  public final static String PROTOCOL_RNIF = "RNIF1"; // used for RNPackInfo.setProtocol()
  public final static String PROTOCOL_CIDX = "CIDX";    // used for RNPackInfo.setProtocol()

  //DTD files
  public final static String PREAMBLE_DTD = "PreamblePartMessageGuideline.dtd";
  public final static String SERVICE_HEADER_DTD  = "ServiceHeaderPartMessageGuideline.dtd";
  public final static String ACKNOWLEDGEMENT_DTD = "ReceiptAcknowledgementMessageGuideline.dtd";
  public final static String EXCEPTION_DTD       = "ExceptionMessageGuideline.dtd";
  public final static String RECEIPT_ACK_EXP_DTD = "ReceiptAcknowledgementExceptionMessageGuideline.dtd";

  //Schema files
  public final static String PREAMBLE_XSD = "PreamblePartMessageGuideline.xsd";
  public final static String SERVICE_HEADER_XSD  = "ServiceHeaderPartMessageGuideline.xsd";
  public final static String ACKNOWLEDGEMENT_XSD = "ReceiptAcknowledgementMessageGuideline.xsd";
  public final static String EXCEPTION_XSD = "ExceptionMessageGuideline.xsd";

  //Dictonary file
  public final static String PREAMBLE_DIC        = "PreamblePartMessageGuideline.xml";
  public final static String SERVICE_HEADER_DIC  = "ServiceHeaderPartMessageGuideline.xml";
  public final static String ACKNOWLEDGEMENT_DIC = "ReceiptAcknowledgementMessageGuideline.xml";
  public final static String EXCEPTION_DIC       = "ExceptionMessageGuideline.xml";

}