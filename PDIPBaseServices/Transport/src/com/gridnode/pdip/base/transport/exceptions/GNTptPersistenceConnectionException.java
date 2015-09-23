package com.gridnode.pdip.base.transport.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class GNTptPersistenceConnectionException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8595811032731972695L;
	public GNTptPersistenceConnectionException(String message)
  {
    super(message);
  }

  public GNTptPersistenceConnectionException(Throwable t)
  {
    super(t);
  }
  public GNTptPersistenceConnectionException(String message, Throwable t)
  {
    super(message, t);
  }

}