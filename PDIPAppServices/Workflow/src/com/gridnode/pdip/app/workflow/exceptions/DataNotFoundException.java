package com.gridnode.pdip.app.workflow.exceptions;

public class DataNotFoundException extends WorkflowException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6827510513507762817L;

	public DataNotFoundException(String reason)
  {
    super(reason);
  }

  public DataNotFoundException(Throwable th)
  {
    super(th);
  }

  public DataNotFoundException(String reason, Throwable th)
  {
    super(reason, th);
  }
}