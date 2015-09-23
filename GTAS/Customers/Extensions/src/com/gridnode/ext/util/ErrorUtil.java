/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ErrorMessage.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 04 2003    Neo Sok Lay         Created
 */
package com.gridnode.ext.util;

/**
 * This class provide helpers to construct error message strings and/or
 * print the error messages to the System.err output stream.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class ErrorUtil
{
  /**
   * Constructs an error message string based on an encountered exception.
   *
   * @param errorCode The Error code associated to the encountered exception.
   * @param error The encountered exception.
   * @return The constructed error message string.
   */
  public static String getErrorMessage(int errorCode, Throwable error)
  {
    return new StringBuffer()
      .append(errorCode)
      .append(" : ")
      .append(error.getMessage())
      .toString();
  }

  /**
   * Constructs an error message string based on the error code and description
   * specified.
   *
   * @param errorCode The error code
   * @param description The error description.
   * @return The constructed error message string.
   */
  public static String getErrorMessage(String errorCode, String description)
  {
    return new StringBuffer()
      .append("[")
      .append(errorCode)
      .append("] ")
      .append(description)
      .toString();
  }

  /**
   * Prints an error message to System.err based on the encountered exception.
   *
   * @param errorCode The error code associated with the encountered exception.
   * @param error The encountered exception.
   */
  public static void printErrorMessage(int errorCode, Throwable error)
  {
    System.err.println("Encountered error: " + errorCode);
    error.printStackTrace(System.err);
  }

}