package com.gridnode.pdip.app.workflow.exceptions;

public class DispatcherException  extends WorkflowException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7903242886699452581L;

	public DispatcherException(String reason)
  {
    super(reason);
  }

  public DispatcherException(Throwable th)
  {
    super(th);
  }

  public DispatcherException(String reason, Throwable th)
  {
    super(reason, th);
  }
}