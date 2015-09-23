package com.gridnode.pdip.app.license.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class InvalidLicenseException extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6290213719812859394L;

	public InvalidLicenseException(String msg)
  {
    super(msg);
  }

  public InvalidLicenseException(String msg, Throwable t)
  {
    super(msg, t);
  }
}