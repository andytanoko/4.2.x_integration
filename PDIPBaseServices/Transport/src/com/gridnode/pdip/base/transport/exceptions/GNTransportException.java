/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTTransportException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? 2002    Jagadesh                Created
 * Jun 21 2002    Goh Kan Mun             Modified - extends from SystemException
 * Nov 07 2002    Goh Kan Mun             Modified - change in Class name.
 */
package com.gridnode.pdip.base.transport.exceptions;

/**
 * <p>Title: PDIP : Peer Data Interchange Platform</p>
 * <p>Description: Transport Module - for PDIP GT(AS)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd - Singapore</p>
 * @author Jagadeesh
 * @version 1.0
 */


import com.gridnode.pdip.framework.exceptions.SystemException;

public class GNTransportException extends SystemException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5505479614049510795L;

	public GNTransportException(Throwable ex)
  {
    super(ex);
  }

  public GNTransportException(String message, Throwable ex)
  {
    super(message, ex);
  }

}