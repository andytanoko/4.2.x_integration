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

//import com.gridnode.pdip.framework.exceptions.ApplicationException;


public class CertificateException extends Exception
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7903350738451549639L;

	public CertificateException(String message)
  {
    super(message);
  }


  public CertificateException(Throwable ex)
  {
    super(ex);
  }

  public CertificateException(String message, Throwable ex)
  {
    super(message, ex);
  }


}