/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLookupException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2002    Ang Meng Hua        Ceated
 * May 04 2002    Ang Meng Hua        Modify class to extend from
 *                                    SystemException
 */

package com.gridnode.pdip.framework.exceptions;

public class ServiceLookupException extends SystemException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8807264347170711212L;

	public ServiceLookupException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  public ServiceLookupException(Throwable ex)
  {
    super(ex);
  }
}

