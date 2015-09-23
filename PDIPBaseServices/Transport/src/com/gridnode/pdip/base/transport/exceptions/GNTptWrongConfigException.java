package com.gridnode.pdip.base.transport.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Title:        PDIP : Peer Data Interchange Platform
 * Description:  Transport Module - for PDIP GT(AS)
 * Copyright:    Copyright (c) 2002
 * Company:      GridNode Pte Ltd - Singapore
 * @author
 * @version 1.0
 */

public class GNTptWrongConfigException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7767584698902020518L;

	public GNTptWrongConfigException(String message)
  {
    super(message);
  }

  public GNTptWrongConfigException(Throwable t)
  {
    super(t);
  }

  public GNTptWrongConfigException(String message, Throwable t)
  {
    super(message, t);
  }

}