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

public class InvalidCommInfoException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3405895764800806865L;

	public InvalidCommInfoException(String message)
  {
    super(message);
  }

  public InvalidCommInfoException(Throwable t)
  {
    super(t);
  }

  public InvalidCommInfoException(String message, Throwable t)
  {
    super(message, t);
  }

}