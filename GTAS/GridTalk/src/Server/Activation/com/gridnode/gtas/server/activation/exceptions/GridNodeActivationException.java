/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeActivationException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception indicates an application processing error during an activation
 * process, be it Activate/Approve/Abort/Deny/Deactivate.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GridNodeActivationException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7487466340605984415L;

	public GridNodeActivationException(String msg)
  {
    super(msg);
  }

  public GridNodeActivationException(String msg, Throwable t)
  {
    super(msg, t);
  }

  public GridNodeActivationException(Throwable t)
  {
    super(t);
  }
}