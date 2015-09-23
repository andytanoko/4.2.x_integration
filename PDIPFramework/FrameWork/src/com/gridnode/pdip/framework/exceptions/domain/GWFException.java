package com.gridnode.pdip.framework.exceptions.domain;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import com.gridnode.pdip.framework.exceptions.NestingException;

public class GWFException extends NestingException  {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8543513514613052719L;
	public GWFException() {
 
  }
  public GWFException(String p0) {
    super(p0);
  }
  public GWFException(Throwable p0) {
    super(p0);
  }
  public GWFException(String p0, Throwable p1) {
    super(p0,p1);
  }

}