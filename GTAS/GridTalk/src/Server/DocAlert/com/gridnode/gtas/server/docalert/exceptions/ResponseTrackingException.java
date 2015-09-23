/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 30 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.exceptions;

/**
 * This exception class is used for indicating a failure in response tracking.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ResponseTrackingException
  extends Exception
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5688115535763848198L;
	public static final String DETERMINE_TRACK_DATE =
                               "Unable to determine Tracking Date using xpath from document: [";
  public static final String INVALID_TRACK_DATE =
                               "Invalid Start Tracking Date from document: [";
  public static final String DETERMINE_DOC_ID =
                               "Unable to determine Document Identifier using xpath from document: [";
  public static final String INVALID_DOC_ID =
                               "Invalid Document Identifier from document: [";

  public ResponseTrackingException(
    String failReason)
  {
    super("Response Tracking failed due to: "+failReason);
  }


  public static ResponseTrackingException unableToDetermineTrackDate(
    String xpath, String udocFilename)
  {
    String msg = new StringBuffer(DETERMINE_TRACK_DATE).append(xpath).append(",").
                     append(udocFilename).append("]").toString();
    return new ResponseTrackingException(msg);
  }

  public static ResponseTrackingException invalidTrackDate(String udocFilename)
  {
    String msg = new StringBuffer(INVALID_TRACK_DATE).append(udocFilename).append("]").toString();
    return new ResponseTrackingException(msg);
  }

  public static ResponseTrackingException unableToDetermineDocIdentifier(
    String xpath, String udocFilename)
  {
    String msg = new StringBuffer(DETERMINE_DOC_ID).append(xpath).append(",").
                     append(udocFilename).append("]").toString();
    return new ResponseTrackingException(msg);
  }

  public static ResponseTrackingException invalidDocIdentifier(String udocFilename)
  {
    String msg = new StringBuffer(INVALID_DOC_ID).append(udocFilename).append("]").toString();
    return new ResponseTrackingException(msg);
  }

}