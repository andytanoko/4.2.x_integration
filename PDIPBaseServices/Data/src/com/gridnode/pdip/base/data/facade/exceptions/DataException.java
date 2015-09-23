package com.gridnode.pdip.base.data.facade.exceptions;

import com.gridnode.pdip.framework.exceptions.NestingException;

public class DataException extends NestingException {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4465069897239932229L;

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
