package com.gridnode.pdip.base.worklist.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class WLAssignmentException extends ApplicationException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8454628887672533732L;

	public WLAssignmentException(String msg)
  {
    super(msg);
  }

  public WLAssignmentException(Throwable ex)
  {
    super(ex);
  }

  public WLAssignmentException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

}