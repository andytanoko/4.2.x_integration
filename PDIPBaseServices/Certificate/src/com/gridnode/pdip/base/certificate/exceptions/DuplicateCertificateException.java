/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateException
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-July-2002    Jagadeesh           Created.
 */


package com.gridnode.pdip.base.certificate.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class DuplicateCertificateException extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5590096278346894726L;

	public DuplicateCertificateException(String message)
  {
    super(message);
  }

  public DuplicateCertificateException(Throwable th)
  {
    super(th);
  }

  public DuplicateCertificateException(String message,Throwable th)
  {
    super(message,th);
  }


}


