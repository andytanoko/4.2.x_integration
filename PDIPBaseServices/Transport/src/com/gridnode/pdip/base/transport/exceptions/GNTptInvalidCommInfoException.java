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

public class GNTptInvalidCommInfoException extends ApplicationException
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6142634635748771657L;

	public GNTptInvalidCommInfoException(String message)
  {
    super(message);
  }

  public GNTptInvalidCommInfoException(Throwable t)
  {
    super(t);
  }

  public GNTptInvalidCommInfoException(String message, Throwable t)
  {
    super(message, t);
  }

}