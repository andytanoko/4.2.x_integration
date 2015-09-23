/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityModifiedException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * This exception is thrown during an update of an entity when the same record
 * is modified by another concurrent transaction.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class EntityModifiedException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6969503838568293588L;

	public EntityModifiedException(String msg)
  {
    super(msg);
  }

  public EntityModifiedException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  public EntityModifiedException(Throwable ex)
  {
    super(ex);
  }
}