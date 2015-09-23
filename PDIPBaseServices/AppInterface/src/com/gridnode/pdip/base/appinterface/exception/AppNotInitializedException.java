package com.gridnode.pdip.base.appinterface.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
 
public class AppNotInitializedException extends ApplicationException {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5813080213832470963L;

	public AppNotInitializedException(String msg)
  {
    super(msg);
  }

  public AppNotInitializedException(Throwable ex)
  {
    super(ex);
  }

  public AppNotInitializedException(String msg, Throwable ex)
  {
    super(msg, ex);
  }
}