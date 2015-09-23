package com.gridnode.pdip.base.contextdata.facade.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class DataException extends ApplicationException {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6613470872333683163L;

	public DataException(String reason) {
		super(reason);
	}
	
	public DataException(Throwable ex) {
		super(ex);
	}
	
	public DataException(String reason, Throwable ex) {
		super(reason, ex);
	}
}
