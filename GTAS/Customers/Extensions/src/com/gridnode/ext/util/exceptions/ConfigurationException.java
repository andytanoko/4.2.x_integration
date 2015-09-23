/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigurationException.java
 * Moved from com.gridnode.utadaptor package
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 14 2004    Neo Sok Lay         Created
 */
package com.gridnode.ext.util.exceptions;

/**
 * This exception indicates a problem with the Adaptor run config file
 * being used to run the Adaptor. Either the file provided is not
 * a valid configuration file or there is problem accessing the file.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class ConfigurationException extends Exception
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3676232301406525546L;

	/**
   * Constructs a ConfigurationException.
   *
   * @param msg The error message.
   */
  public ConfigurationException(String msg)
  {
    super(msg);
  }
}