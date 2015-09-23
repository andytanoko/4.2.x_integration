package com.gridnode.pdip.base.rolemap.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;


public class RoleMapException  extends ApplicationException {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1413785430340425705L;

	public RoleMapException(String reason)
  {
    super(reason);
  }

  public RoleMapException(Throwable th)
  {
    super(th);
  }

  public RoleMapException(String reason, Throwable th)
  {
    super(reason, th);
  }

}