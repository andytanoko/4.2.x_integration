/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AdaptorRunException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.utadaptor;

/**
 * This exception indicates an error encountered when trying to execute
 * the Adaptor.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class AdaptorRunException extends Exception
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7616878233845847069L;

	/** 
   * Constructs an AdaptorRunException.
   * 
   * @param msg The error message.
   */
  public AdaptorRunException(String msg)
  {
    super(msg);
  }
}