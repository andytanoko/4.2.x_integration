/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReprocessServletConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 24, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.reprocess;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IReprocessServletConstant
{
  public static final String ACTIVITY_TYPE = "actType";
  public static final String USER_NAME = "usrName";
  
  public static final String PROCESS_UID = "processUID";
  public static final String TRACING_ID = "traceID";
  
  public static final String OPERATION_NAME = "operationName";
  public static final String STATUS_CODE = "statusCode";
  
  public static final String ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS = "reprocessTraceEvent";
  public static final String ACTIVITY_CANCEL_PROCESS = "cancelProcess";
  
  //Status/Error code for UI
  public static final String STATUS_INITIATE_FAIL = "1";  //The initiating reprocess fail
  public static final String STATUS_REPRPOCESS_DENIED_HAS_OB = "2"; //The reprocess request is denied. The role is responding role, and OTc contain OB action doc
  public static final String STATUS_FAIL_SEND_TO_GT = "3"; //fail sending request to GT for reprocessing
  public static final String STATUS_SUCCESS_SEND_TO_GT = "4"; //reprocess request successful send to GT. 
  public static final String STATUS_REPROCESS_DENIED_NO_IB = "5"; //No IB transaction. We have the event Doc Received, Unpack Payload (fail at this point)
  public static final String STATUS_REPROCESS_DENIED_HAS_HTTP_BC_EVENT = "6"; //contained the HTTP BC event
  
  public static final String EXP_REPRPOCESS_DENIED_HAS_OB = "The role type is responding role and the process contain OB transaction";
  public static final String EXP_REPROCESS_DENIED_NO_IB = "Event for "+IAuditTrailConstant.DOCUMENT_RECEIVED+" or "+IAuditTrailConstant.UNPACK_PAYLOAD+" or "
                                                          +IAuditTrailConstant.PROCESS_INJECTION+" is failed";
  public static final String EXP_REPROCESS_DENIED_HAS_HTTP_BC = "Contain the send event from HTTP BC only"; //contain the 'Document Received from Backend' n 'Document Delivery to Gateway' only
  
  public static final String HTTPBC_EVENT_DOC_RECEIVED_FROM_BACKEND = "Document Received from Backend";
  public static final String HTTPBC_EVENT_DOC_DELIVERED_TO_GW = "Document Delivery to Gateway";
}
