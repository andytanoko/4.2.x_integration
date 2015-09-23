package com.gridnode.pdip.app.workflow.exceptions;


public class DataDefinitionException extends WorkflowException {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2640258994991852149L;

	public DataDefinitionException(String reason)
  {
    super(reason);
  }

  public DataDefinitionException(Throwable th)
  {
    super(th);
  }

  public DataDefinitionException(String reason, Throwable th)
  {
    super(reason, th);
  }
}