package com.gridnode.pdip.app.workflow.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class WorkflowException extends ApplicationException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1920526695213414002L;

	public WorkflowException(String reason)
  {
    super(reason);
  }

  public WorkflowException(Throwable th)
  {
    super(th);
  }

  public WorkflowException(String reason, Throwable th)
  {
    super(reason, th);
  }
}