package com.gridnode.pdip.app.workflow.exceptions;

public class InvalidStateException extends WorkflowException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1225673319427181734L;

	public InvalidStateException(String reason)
  {
    super(reason);
  }

  public InvalidStateException(Throwable th)
  {
    super(th);
  }

  public InvalidStateException(String reason, Throwable th)
  {
    super(reason, th);
  }
}