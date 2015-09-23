/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InvalidCACertificateException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 31, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.exceptions;

/**
 * Indicate the certificate is not a CA cert
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class InvalidCACertificateException extends CertificateException
{
	public InvalidCACertificateException(String msg)
	{
		super(msg);
	}
	
	public InvalidCACertificateException(String msg, Throwable th)
	{
		super(msg, th);
	}
}
