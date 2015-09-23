/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    Koh Han Sing        Created
 * May 07 2003    Neo Sok Lay         Add portConnectFail().
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class ExportException
  extends    ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3508265469804846795L;

	public ExportException(String message)
  {
    super(message);
  }

  public ExportException(Throwable ex)
  {
    super(ex);
  }

  public ExportException(String message, Throwable ex)
  {
    super(message, ex);
  }

  public static ExportException portConnectFail(Port port)
  {
    StringBuffer buff = new StringBuffer();
    buff.append("Fail to connect to Port: ").append(port.getName());
    buff.append("-").append(port.getDescription());
    if (port.getRfc() != null)
      buff.append(", RFC[").append(port.getRfc().getEntityDescr()).append("]");

    return new ExportException(buff.toString());
  }
}