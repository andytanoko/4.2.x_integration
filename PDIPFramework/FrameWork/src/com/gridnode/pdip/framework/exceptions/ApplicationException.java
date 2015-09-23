/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ApplicationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 * May 07 2002    Ang Meng Hua        Modify class to extend from
 *                                    TypedException
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * Signals an error in the business logic.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ApplicationException
  extends    TypedException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3003761434979459193L;
	public static final short APPLICATION = 1;

  public ApplicationException(String msg)
  {
    super(msg, APPLICATION);
  }

  public ApplicationException(String msg, Throwable ex)
  {
    super(msg, ex, APPLICATION);
  }

  public ApplicationException(Throwable ex)
  {
    super(ex, APPLICATION);
  }
}