/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConfigCats.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10, 2006   i00107              Created
 * Jan 05 2007    i00107              Add categories for event.notfn
 * Mar 15 2007    i00107              Add categories for GTAS_JNDI & GTAS_LOGIN
 */

package com.gridnode.gridtalk.httpbc.common.util;

/**
 * @author i00107
 * This interface defines the configuration categories for the components
 * of HTTPBC.
 */
public interface IConfigCats
{
  static final String ISHB_TX_OUT = "ishb.tx.out";
  static final String ISHB_TX_IN = "ishb.tx.in";
  
  static final String EVENT_NOTFN = "event.notfn";
  static final String EVENT_NOTFN_DOC_RECEIVED = "event.notfn.doc.received";
  static final String EVENT_NOTFN_DOC_DELIVERED = "event.notfn.doc.delivered";
  static final String EVENT_NOTFN_DOC_DELIVERY_FAILED = "event.notfn.doc.delivery.failed";
  
  static final String DOC_TYPE_FILE_EXT_MAP = "doctype.fext.map";
  
  static final String HTTP_TARGET_CONN = "http.target.conn.";
  static final String HTTP_DOC_HEADERS = "http.doc.headers.";
  static final String HTTP_DOC_HEADERS_DEF = "http.doc.headers.default";
  static final String HTTP_PROXY_AUTH = "http.proxy.auth";
  
  static final String BACKEND_SEND = "backend.sender";
  static final String GTAS_JNDI = "gtas.jndi";
  static final String GTAS_LOGIN = "gtas.login.";
  static final String GTAS_LOGIN_DEFAULT = "gtas.login.default";
  
  static final String ALERT = "alert";
 
  static final String DOC_HEADER_TRACING_ID = "${tracing.id}";
  static final String DOC_HEADER_DOC_TYPE = "${doc.type}";
  static final String DOC_HEADER_BIZENT_ID = "${bizent.id}";
  static final String DOC_HEADER_PARTNER_ID = "${partner.id}";
  static final String DOC_HEADER_FILENAME = "${doc.filename}";
 
  //#1105 TWX load the jms properties for delivering to queue "processBackTxQueue"
  static final String TX_DELIVERY_JMS = "tx.delivery.jms";
  
  //load the delivery info
  static final String TX_DELIVERY_INFO = "tx.delivery.info";
  static final String TX_MAX_PROCESS_COUNT_PER_CALL = "max.process.count.percall";
  static final String TX_MAX_FAILED_ATTEMPTS_PER_TX = "max.failed.attempts.pertx";
  static final String TX_FAILED_ATTEMPT_ALERT_THRESHOLD = "failed.attempt.alert.threshold";
  static final String TX_DELIVERY_MGR_JNDI = "tx.delivery.mgr.jndi";
}
