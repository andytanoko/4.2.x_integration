/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ErrorMessage.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2003    Neo Sok Lay         Created
 */
package com.gridnode.simulation.utadaptor;

import java.text.MessageFormat;

/**
 * This class provides simple utility to format error messages into UCCnetErrorMessage
 * format (XML).
 *
 * <p>Title: UTAdaptor Simulator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author Neo Sok Lay
 * @version GT 2.3 I1
 */
public class ErrorMessage {
  private static final StringBuffer MSG_ERROR_DTD =
      new StringBuffer("<?xml version=\"1.0\" standalone=\"yes\"?>")
      .append("\r\n")
      .append("<!DOCTYPE UCCnetMsgError [")
      .append("\r\n")
      .append("<!ELEMENT  UCCnetMsgError  (error)>")
      .append("\r\n")
      .append("<!ATTLIST  UCCnetMsgError  version  CDATA  #FIXED  \"1.0\">")
      .append("\r\n")
      .append("<!ELEMENT  error  (code, description?, detail?)>")
      .append("\r\n")
      .append("<!ELEMENT  code         (#PCDATA)>")
      .append("\r\n")
      .append("<!ELEMENT  description  (#PCDATA)>")
      .append("\r\n")
      .append("<!ELEMENT  detail       (#PCDATA)>")
      .append("\r\n")
      .append("]>")
      .append("\r\n");

  private static final StringBuffer MSG_ERR_STR =
      new StringBuffer("<UCCnetMsgError>")
      .append("\r\n")
      .append("\t").append("<error>").append("\r\n")
      .append("\t\t").append("<code>{0}</code>").append("\r\n")
      .append("\t\t").append("<description>{1}</description>").append("\r\n")
      .append("\t\t").append("<detail>{2}</detail>").append("\r\n")
      .append("\t").append("</error>").append("\r\n")
      .append("</UCCnetMsgError>").append("\r\n");

  private static final MessageFormat ERR_MSG_FORMAT = new MessageFormat(
      MSG_ERROR_DTD.append(MSG_ERR_STR).toString());

  /**
   * Get the formatted error message in UCCnetErrorMsg format.
   *
   * @param errorCode The Error code
   * @param description The error description
   * @param detail The error details (usually stack trace)
   * @return Formatted error message.
   */
  public static String getErrorMessage(String errorCode, String description,
                                       String detail) {
    return ERR_MSG_FORMAT.format(new String[] {errorCode, description, detail});
  }

}