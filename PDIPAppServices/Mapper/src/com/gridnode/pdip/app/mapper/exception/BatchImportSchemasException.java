/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BatchImportSchemasException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 7, 2006    i00107             Created
 */

package com.gridnode.pdip.app.mapper.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * @author i00107
 * @since GT 4.0
 * This exception is thrown when problem is encountered during
 * batch import of schemas.
 */
public class BatchImportSchemasException extends ApplicationException
{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7287883872951931940L;

	/**
	 * @param msg
	 */
	public BatchImportSchemasException(String msg)
	{
		super(msg);
	}

	/**
	 * @param msg
	 * @param ex
	 */
	public BatchImportSchemasException(String msg, Throwable ex)
	{
		super(msg, ex);
	}


}
