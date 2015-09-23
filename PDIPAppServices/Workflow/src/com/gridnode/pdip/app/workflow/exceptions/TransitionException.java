package com.gridnode.pdip.app.workflow.exceptions;

public class TransitionException  extends WorkflowException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -693818219140681673L;

	public TransitionException(String reason)
  {
    super(reason);
  }

  public TransitionException(Throwable th)
  {
    super(th);
  }

  public TransitionException(String reason, Throwable th)
  {
    super(reason, th);
  }
}