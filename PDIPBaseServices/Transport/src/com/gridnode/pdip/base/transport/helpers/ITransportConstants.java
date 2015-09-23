/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransportConstants.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 04 2002    Jagadeesh               Created
 * Dec 26 2002    Jagadeesh               Modified:- Fixed EVENT_SUB_ID bug.
 * Jan 12 2002    Jagadeesh               Modified:- To Add Additional Header to
 *                                        identify GT/AS Message.
 * Jan 30 2003    Kan Mun                 Modified - Add GridNodeCommInfo String
 *                                                 - Add eventType.
 * Apr 28 2003    Qingsong                Added: fields for Http Transport
 *
 * OCT 02 2003    Jagadeesh               Added: Header's to support BE-UDDI Search.
 * OCT 21 2003	  Jagadeesh			          Modified: Fix bug GNDB00015739.Added Constant
 *						 	                          for undefined doctype.
 * OCT 07 2010    Tam Wei Xiang           #1897 - Added INCOMING_CONNECTION_TYPE_DIRECT,
 *                                        PACKAGE_TYPE_NONE
 */

package com.gridnode.pdip.base.transport.helpers;


public interface ITransportConstants
{

  public static final String  EVENT_ID = "EventId";

  public static final String  TRANSACTION_ID = "TransactionId";

  public static final String RECEIPENT_NODE_ID = "ReceipentNodeId";

  public static final String SENDER_NODE_ID = "SenderNodeId";

  public static final String  EVENT_SUB_ID = "EventSubId";

  public static final String  PROCESS_ID  = "ProcessId";

  public static final String FILE_ID = "FileId";

  public static final String GRIDNODE_COMM_INFO_STR = "GridNodeCommInfoStr";

  public static final String EVENT_TYPE = "EventType";

  public static final String UNDEFINED_FILE_ID = "Undefined File Id";

  public static final String UNDEFINED_URL = "Undefined URL";

  public static final String UNDEFINED_DOC_TYPE = "Undefined DocumentType";

  public static final String INCOMING_CONNECTION_TYPE_DIRECT = "Direct"; //TWX 20100930 Indicate the incoming msg is not using the AS2/RNIF
  /**
   * Transport message type
   */
  //for receiver
  public static final String PACKAGE_TYPE_KEY =  "x-gn-package-type";

  public static final String PACKAGE_TYPE_GTAS = "GTAS_PACKAGE";

  public static final String PACKAGE_TYPE_GNBACKWARDCOMPATIBLE="GN_BACKWARDCOMPATIBLE";

  public static final String PACKAGE_TYPE_RosettaNet = "ROSETTA_PACKAGE";

  public static final String PACKAGE_TYPE_NONE = "NONE";
  
  //for sender
  public static final String PACKAGE_TYPE_SENDER_KEY =  "x-gn-sender-package-type";

  public static final String PACKAGE_TYPE_SENDER_PAYLOAD = "Payload";

  public static final String PACKAGE_TYPE_SENDER_CMD = "cmd";

  public static final String SENDER_CMD_KEY = "x-gn-sender-cmd";

  public static final String SENDER_CMD_SET_TRUSTSTORE = "set-trust-keystore";

  public static final String SENDER_CMD_SET_KEYSTORE = "set-keystore";

  /**
   * HTTP Synchronous message
   */

  public static final String GN_MESSAGE_ID_KEY = "x-gn-message-id";

  public static final String GTAS_MESSAGE_TYPE="GT/AS Message";


  /** Headers to represent WEB-SERVICES Information in JAXR(Impl)-Registry **/

  public static final String SENDER_UUID = "SenderUUID";

  public static final String SENDER_QUERY_URL = "SenderQueryURL";

  public static final String RECEIPENT_UUID = "ReceipentUUID";

  public static final String RECEIPENT_QUERY_URL = "ReceipentQueryURL";

  public static final String DOCUMENT_TYPE = "DocumentType";

 //public static final String UNDEFINED_URL = "Undefined URL";

  //public static final String UNDEFINED_DOC_TYPE = "Undefined DocumentType";
  public static final String TPT_COMMINFO_CONFIG_NAME ="transport.comminfo";

  //TWX 20100518 #1470 
  public static final String HTTP_AUTHORIZATION = "Authorization";
  public static final String HTTP_AUTHORIZATION_MODE_BASIC = "Basic";
}




