/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BasicTypedException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * Define two types for the exception.
 * <PRE>
 * SYSTEM - this indicates a system level exception. Normally are unexpected
 *          errors.
 * APPLICATION - this indicates a application level exception. Normally are
 *               due to business logic validation.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class BasicTypedException
  extends    TypedException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8135786045594510077L;
	public static final short SYSTEM      = 0;
  public static final short APPLICATION = 1;

  /**
   * Construct a BasicTypedException which is APPLICATION type.
   *
   * @param msg The message of the exception.
   */
  public BasicTypedException(String msg)
  {
    super(msg, APPLICATION);
  }

  /**
   * Construct a BasicTypedException which is SYSTEM type.
   *
   * @param nestedException A nesting exception.
   */
  public BasicTypedException(Throwable nestedException)
  {
    super(nestedException, SYSTEM);
  }

  /**
   * Construct a BasicTypedException which is SYSTEM type.
   *
   * @param msg The message of the exception.
   * @param nestedException A nesting exception.
   */
  public BasicTypedException(String msg, Throwable nestedException)
  {
    super(msg, nestedException, SYSTEM);
  }

  /**
   * Checks if this exception is SYSTEM type.
   *
   * @return <B>true</B> if this exception is of SYSTEM type.
   */
  public boolean isSystemException()
  {
    return SYSTEM == getType();
  }

  /**
   * Checks if this exception is APPLICATION type.
   *
   * @return <B>true</B> if this exception is of APPLICATION type.
   */
  public boolean isApplicationException()
  {
    return APPLICATION == getType();
  }
}