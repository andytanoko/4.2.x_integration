/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExpiryChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 17, 2006    Tam Wei Xiang       Created
 * Dec 20, 2006    Tam Wei Xiang       To add in the alertTriggerAfterExpiredDay.
 *                                     This allow user to specify the number of day
 *                                     to trigger the cert expired alert start counting
 *                                     from the date the cert expired.
 * Dec 21, 2006    Neo Sok Lay         Change parameter names and sequence.                                    
 */
package com.gridnode.gridtalk.certificate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * A Java procedure to perform checking on the expiry of certs in GT 
 * cert store.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ExpiryChecker
{
	private static String CERT_EXPIRY_CHECKER = "com.gridnode.gtas.server.certificate.helpers.ExpiryCheckerDelegate";
	
	public ExpiryChecker()
	{
		
	}
	
	public void start(Integer daysBefore, Integer daysAfter, String alertName)
		throws Exception
	{
		Class certExpiryChecker = Class.forName(CERT_EXPIRY_CHECKER);
		Constructor constructor = certExpiryChecker.getConstructor(Integer.class, Integer.class, String.class);
		Object certExpCheckerIns = constructor.newInstance(new Object[]{daysBefore, daysAfter, alertName});
		
		Method execute = certExpiryChecker.getMethod("execute", new Class[]{});
		
		execute.invoke(certExpCheckerIns, new Object[]{});
	}

}
