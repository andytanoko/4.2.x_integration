/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryProviderException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.exceptions;

import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * Thrown when error occurs in the registry provider.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryProviderException extends SystemException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6062812673872931089L;
	
	private final static String DEF_MSG = "Unexpected Provider Exception";
 
  /**
   * Constructor for RegistryProviderException.
   * @param msg
   * @param ex
   */
  public RegistryProviderException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  /**
   * Constructor for RegistryProviderException.
   * @param ex
   */
  public RegistryProviderException(Throwable ex)
  {
    this(DEF_MSG, ex);
  }

}
