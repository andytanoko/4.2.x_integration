/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidLicenseFileException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when the license file is invalid.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class InvalidLicenseFileException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2668830685344559860L;

	/**
   * Thrown when exception occurs while reading the license file for
   * registration.
   *
   * @param ex The exception encountered.
   */
  public InvalidLicenseFileException(Throwable ex)
  {
    super(ex);
  }
}