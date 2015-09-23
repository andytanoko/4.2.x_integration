/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class ExportConfigException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7530044847993773915L;

	public ExportConfigException(String msg)
  {
    super(msg);
  }

  public ExportConfigException(Throwable ex)
  {
    super(ex);
  }

  public ExportConfigException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}