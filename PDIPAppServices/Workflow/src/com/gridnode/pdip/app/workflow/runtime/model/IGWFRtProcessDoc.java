/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 * Apr 05 2007    Tam Wei Xiang       Added in customer be ID. To support 
 *                                    archive by customer
 */
package com.gridnode.pdip.app.workflow.runtime.model;


public interface IGWFRtProcessDoc {

  public static final String ENTITY_NAME = "GWFRtProcessDoc";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID = new Integer(0);  //Integer

  public static final Number DOCUMENT_ID=new Integer(1);

  public static final Number DOCUMENT_TYPE=new Integer(2);

  public static final Number DOCUMENT_NAME=new Integer(3);

  public static final Number BUSINESS_TRANSACTIVITY_ID=new Integer(4);

  public static final Number BINARY_COLLABORATION_UID=new Integer(5);

  public static final Number RT_BINARY_COLLABORATION_UID=new Integer(6);

  public static final Number RT_BUSINESS_TRANSACTION_UID=new Integer(7);

  public static final Number IS_POSITIVE_RESPONSE=new Integer(8);

  public static final Number DOC_PROCESSED_FLAG=new Integer(9);

  public static final Number ACK_RECEIPTSIGNAL_FLAG=new Integer(10);

  public static final Number ACK_ACCEPTSIGNAL_FLAG=new Integer(11);

  public static final Number EXCEPTIONSIGNAL_TYPE=new Integer(12);

  public static final Number ROLE_TYPE=new Integer(13);

  public static final Number RETRY_COUNT=new Integer(14);

  public static final Number REQUESTDOC_TYPE=new Integer(15);

  public static final Number RESPONSEDOC_TYPES=new Integer(16);

  public static final Number PARTNER_KEY=new Integer(17);

  public static final Number STATUS=new Integer(18);

  public static final Number REASON=new Integer(19);

  public static final Number CUSTOMER_BE_ID = new Integer(20);
}
