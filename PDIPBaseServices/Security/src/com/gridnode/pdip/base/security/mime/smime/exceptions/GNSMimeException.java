package com.gridnode.pdip.base.security.mime.smime.exceptions;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public class GNSMimeException extends TypedException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4667764795555001449L;
	public static final short GNSMIME_EXCEPTION_COMPRESS   = 1000;
  public static final short GNSMIME_EXCEPTION_DECOMPRESS = 1100;
  public static final short GNSMIME_EXCEPTION_ENCRYPT    = 1200;
  public static final short GNSMIME_EXCEPTION_DECRYPT    = 1300;
  public static final short GNSMIME_EXCEPTION_SIGN       = 1400;
  public static final short GNSMIME_EXCEPTION_VERIFY     = 1500;
  public static final short GNSMIME_EXCEPTION_WRONG_CONTENT   = 1600;
  public GNSMimeException()
  {
    this(ApplicationException.APPLICATION);
  }

  public GNSMimeException(String msg)
  {
    this(msg, ApplicationException.APPLICATION);
  }

  public GNSMimeException(Throwable nestedException)
  {
    this(nestedException, ApplicationException.APPLICATION);
  }

  public GNSMimeException(String msg, Throwable nestedException)
  {
    this(msg, nestedException, ApplicationException.APPLICATION);
  }

  public GNSMimeException(short type)
  {
    super(type);
  }

  public GNSMimeException(String msg, short type)
  {
    super(msg, type);
  }

  public GNSMimeException(Throwable nestedException, short type)
  {
    super(nestedException, type);
  }

  public GNSMimeException(String msg, Throwable nestedException, short type)
  {
    super(msg, nestedException, type);
  }
}