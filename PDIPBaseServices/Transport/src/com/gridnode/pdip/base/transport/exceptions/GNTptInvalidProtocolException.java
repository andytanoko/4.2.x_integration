
package com.gridnode.pdip.base.transport.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class GNTptInvalidProtocolException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3855383952493909319L;

	public GNTptInvalidProtocolException(String message)
  {
    super(message);
  }

  public GNTptInvalidProtocolException(Throwable t)
  {
    super(t);
  }

  public GNTptInvalidProtocolException(String message, Throwable t)
  {
    super(message, t);
  }

}