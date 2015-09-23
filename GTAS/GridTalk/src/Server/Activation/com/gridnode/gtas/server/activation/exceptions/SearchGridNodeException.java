/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 * Nov 18 2002    Neo Sok Lay         Extend from GridNodeActivationException.
 */
package com.gridnode.gtas.server.activation.exceptions;

/**
 * This exception indicates an application processing error when searching
 * for GridNodes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchGridNodeException
  extends    GridNodeActivationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4317771121126288405L;

	public SearchGridNodeException(String msg)
  {
    super(msg);
  }

  public SearchGridNodeException(String msg, Throwable t)
  {
    super(msg, t);
  }

  public SearchGridNodeException(Throwable t)
  {
    super(t);
  }
}