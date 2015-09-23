/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Modified to confront to coding standard
 */
package com.gridnode.pdip.base.xml.exceptions;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author unascribed
 * @version 1.0
 */

import com.gridnode.pdip.framework.exceptions.NestingException;

public class XMLException extends NestingException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8479322180376098098L;

	public XMLException()
  {
  }

  public XMLException(String message)
  {
    super(message);
  }

  public XMLException(String message,Exception nestedException)
  {
    super(message, nestedException);
  }
}


