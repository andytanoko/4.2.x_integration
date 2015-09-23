/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AdaptorReturnException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.ErrorUtil;

/**
 * This exception indicates an error returned from the Adaptor that
 * is being run.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class AdaptorReturnException extends Exception
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6904258421086286259L;

	/**
   * Constructs an AdaptorReturnException
   * 
   * @param errorCode The error code returned from the Adaptor error
   * message.
   * @param description The error description returned from the Adaptor
   * error message.
   */
  public AdaptorReturnException(String errorCode, String description)
  {
    super(ErrorUtil.getErrorMessage(errorCode, description));
  }
}
