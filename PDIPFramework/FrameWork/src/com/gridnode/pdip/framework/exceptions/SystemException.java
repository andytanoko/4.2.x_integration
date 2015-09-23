/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SystemException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26 2002    Neo Sok Lay         Created
 * May 07 2002    Ang Meng Hua        Modify class to extend from
 *                                    TypedException
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * Indicates a problem with the services that support the application.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class SystemException
  extends    TypedException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6484900953043853865L;
	public static final short SYSTEM = 0;

  public SystemException(String msg, Throwable ex)
  {
    super(msg, ex, SYSTEM);
  }

  public SystemException(Throwable ex)
  {
    super(ex, SYSTEM);
  }
}