package com.gridnode.pdip.base.worklist.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class WorklistException extends ApplicationException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2670735051475229183L;

	public WorklistException(String msg)
  {
    super(msg);
  }

  public WorklistException(Throwable ex)
  {
    super(ex);
  }

  public WorklistException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

}