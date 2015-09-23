package com.gridnode.pdip.app.license.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class LicenseRevocationException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4259182874898488846L;

	public LicenseRevocationException(String msg)
  {
    super(msg);
  }

  public LicenseRevocationException(String msg, Throwable t)
  {
    super(msg, t);
  }
}