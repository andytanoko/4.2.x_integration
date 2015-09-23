/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRNConstant_20.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 21 2003      Guo Jianyu              Created
 * Jul 21 2003      Guo Jianyu              Modified this interface to a class
 *                                          to have class initializer
 */
package com.gridnode.pdip.base.rnif.model;


public interface IRNConstant_20 extends IRNConstant
{
  public final static String PREAMBLE_DTD = "Preamble_MS_V02_00.dtd";
  public final static String DELIVERY_HEADER_DTD =
    "DeliveryHeader_MS_V02_00.dtd";
  public final static String SERVICE_HEADER_DTD = "ServiceHeader_MS_V02_00.dtd";
  public final static String ACKNOWLEDGEMENT_DTD =
    "AcknowledgmentOfReceipt_MS_V02_00.dtd";
  public final static String EXCEPTION_DTD = "Exception_MS_V02_00.dtd";

  public final static String PREAMBLE_DIC = "Preamble_MS_V02_00.xml";
  public final static String DELIVERY_HEADER_DIC =
    "DeliveryHeader_MS_V02_00.xml";
  public final static String SERVICE_HEADER_DIC = "ServiceHeader_MS_V02_00.xml";
  public final static String ACKNOWLEDGEMENT_DIC =
    "AcknowledgmentOfReceipt_MS_V02_00.xml";
  public final static String EXCEPTION_DIC = "Exception_MS_V02_00.xml";

  public final static String PREAMBLE_XSD = "Preamble_MS_V02_00.xsd";
  public final static String DELIVERY_HEADER_XSD =
    "DeliveryHeader_MS_V02_00.xsd";
  public final static String SERVICE_HEADER_XSD = "ServiceHeader_MS_V02_00.xsd";
  public final static String ACKNOWLEDGEMENT_XSD =
    "AcknowledgmentOfReceipt_MS_V02_00.xsd";
  public final static String EXCEPTION_XSD = "Exception_MS_V02_00.xsd";

  public final static String RNIF_VERSION = "V02.00"; // for preamble

  // The default content type of attachments
  public final static String ATT_TYPE = "application/binary";

  // Fixed values for Content Location header of MIME part
  public final static String CONTENT_LOC = "Content-Location";
  public final static String PREAMBLE_CL = "RN-Preamble";
  public final static String DELIVERY_HEADER_CL = "RN-Delivery-Header";
  public final static String SERVICE_HEADER_CL = "RN-Service-Header";
  public final static String SERVICE_CONTENT_CL = "RN-Service-Content";

  public final static String PROTOCOL_RNIF = "RNIF2"; // used for RNPackInfo.setProtocol()
}