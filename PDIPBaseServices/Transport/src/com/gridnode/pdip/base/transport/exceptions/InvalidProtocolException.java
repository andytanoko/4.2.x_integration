
package com.gridnode.pdip.base.transport.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidProtocolException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3717596375954325184L;

	public InvalidProtocolException(String message)
  {
    super(message);
  }

  public InvalidProtocolException(Throwable t)
  {
    super(t);
  }

  public InvalidProtocolException(String message, Throwable t)
  {
    super(message, t);
  }

}