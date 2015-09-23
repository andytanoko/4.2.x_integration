/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 2 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception is used for indicating an error that occurs when
 * perform a search in the business registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SearchRegistryException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821045345873372797L;

	/**
   * Constructs a SearchRegistryException with an error message.
   * @param msg The error message.
   */
  public SearchRegistryException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs a SearchRegistryException with an error message and
   * exception object.
   * 
   * @param msg The error message.
   * @param ex The exception that occurred.
   */
  public SearchRegistryException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  /**
   * Constructs a SearchRegistryException with an exception object.
   * @param ex The exception that occurred.
   */
  public SearchRegistryException(Throwable ex)
  {
    super(ex);
  }

}
