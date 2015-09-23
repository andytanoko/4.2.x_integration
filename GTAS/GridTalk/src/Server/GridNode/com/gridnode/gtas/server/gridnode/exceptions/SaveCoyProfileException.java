package com.gridnode.gtas.server.gridnode.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;


public class SaveCoyProfileException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1009765317271889400L;

	public SaveCoyProfileException(String msg)
  {
    super(msg);
  }

  public SaveCoyProfileException(String msg, Throwable t)
  {
    super(msg, t);
  }

  public SaveCoyProfileException(Throwable t)
  {
    super(t);
  }
}