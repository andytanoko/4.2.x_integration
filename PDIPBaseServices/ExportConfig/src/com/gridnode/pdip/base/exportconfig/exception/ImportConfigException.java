/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportConfigException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class ImportConfigException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9160759929035281657L;

	public ImportConfigException(String msg)
  {
    super(msg);
  }

  public ImportConfigException(Throwable ex)
  {
    super(ex);
  }

  public ImportConfigException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}