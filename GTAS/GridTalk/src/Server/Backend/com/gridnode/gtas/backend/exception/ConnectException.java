/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 */
package com.gridnode.gtas.backend.exception;

/**
 * Title:        Base Framework Layer
 * Description:  Base Framework Layer
 * Copyright:    Copyright (c) 2000
 * Company:      GridNode Pte Ltd
 * @author Neo Sok Lay
 * @version 1.0
 */

public class ConnectException extends Exception
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7088856062606474840L;

	public ConnectException(String s)
  {
    super(s);
  }
}