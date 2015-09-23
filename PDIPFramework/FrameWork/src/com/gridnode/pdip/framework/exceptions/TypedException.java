/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TypedException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * Tag a "type" to the exception.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class TypedException
  extends    NestingException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7640892932728614354L;
	private short _type = -1;

  public TypedException(short type)
  {
    super();
    _type = type;
  }

  public TypedException(String msg, short type)
  {
    super(msg);
    _type = type;
  }

  public TypedException(Throwable nestedException, short type)
  {
    super(nestedException);
    _type = type;
  }

  public TypedException(String msg, Throwable nestedException, short type)
  {
    super(msg, nestedException);
    _type = type;
  }

  /**
   * Get the "type" of the exception
   *
   * @return Exception type.
   *
   * @since 2.0
   */
  public short getType()
  {
    return _type;
  }
}