package com.gridnode.pdip.base.appinterface.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
 
public class AppExecutionException extends ApplicationException {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7073890334029892237L;

	public AppExecutionException(String msg)
  {
    super(msg);
  }

  public AppExecutionException(Throwable ex)
  {
    super(ex);
  }

  public AppExecutionException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

}