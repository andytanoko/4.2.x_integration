/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocProcessingErrorConstants.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.document.helpers;

/**
 * Constants for raising processing error alerts.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IDocProcessingErrorConstants
{

  static final String TYPE_IMPORT_DOC_FAILURE = "1";
  static final String TYPE_EXPORT_DOC_FAILURE = "2";
  static final String TYPE_SEND_DOC_FAILURE   = "3";
  static final String TYPE_RECEIVE_DOC_FAILURE= "4";

  static final String REASON_CONNECT_PORT_FAIL     = "1";
  static final String REASON_UNKNOWN_PORT          = "2";
  static final String REASON_EXCEED_FILESIZE_LIMIT = "3";
  static final String REASON_UNKNOWN_PARTNER       = "4";
  static final String REASON_UNKNOWN_DESTINATION   = "5";
  static final String REASON_PARTNER_NOT_ENABLED   = "6";
  static final String REASON_LICENSE_INVALID       = "7";
  static final String REASON_FILE_PROCESSING_ERROR = "8";
  static final String REASON_GENERAL_ERROR         = "9";

  static final Exception NO_RECIPIENT_PARTNER      = new Exception("Recipient Partner Id Not Specified");
  static final Exception NO_RECIPIENT_CHANNEL      = new Exception("Recipient Channel Not Specified");
  static final Exception NO_EXPORT_PORT            = new Exception("Export Port Not Specified");
  static final Exception RECEIVE_DISALLOWED        = new Exception("Receive Not Allowed");
  static final Exception SEND_DISALLOWED           = new Exception("Send Not Allowed");

}
