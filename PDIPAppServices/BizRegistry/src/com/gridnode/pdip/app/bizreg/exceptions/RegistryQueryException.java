/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryQueryException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when exception occurs during querying from the registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryQueryException extends ApplicationException
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 978450663340960454L;
	private static final String PROVIDER_ERROR = "Registry provider error";
  private static final String UNKNOWN_APP_ERROR = "Unknown registry error";

  /**
   * Constructs a RegistryQueryException.
   * 
   * @param msg The error message.
   */
  public RegistryQueryException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs a RegistryQueryException.
   * 
   * @param msg The error message.
   * @param ex The cause of the exception.
   */
  public RegistryQueryException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  /**
   * Constructs a RegistryQueryException.
   * 
   * @param ex The cause of the exception.
   */
  public RegistryQueryException(Throwable ex)
  {
    super(ex);
  }

  /**
   * Creates a RegistryQueryException caused by exceptions thrown
   * by the registry provider.
   * 
   * @param e The exception thrown by the registry provider.
   * @return The created exception.
   */
  public static RegistryQueryException registryProviderError(
    RegistryProviderException e)
  {
    return new RegistryQueryException(PROVIDER_ERROR, e);
  }

  /**
   * Creates a RegistryQueryException caused by other unknown 
   * application errors.
   * 
   * @param t The application error that occurs.
   * @return The created exception.
   */
  public static RegistryQueryException unknownRegistryError(
    Throwable t)
  {
    return new RegistryQueryException(UNKNOWN_APP_ERROR, t);
  }

}
