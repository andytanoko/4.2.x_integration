/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AssertUtil.java
 *
 ****************************************************************************
 * Date           	Author              		Changes
 ****************************************************************************
 * 15 Dec 2005			SC											Created
 */
package com.gridnode.pdip.framework.util;

/**
 * Note: I created this file so I won't change anything in UtilString.java.
 *
 */
public class StringUtil
{
	public static boolean isNotEmpty(String str)
	{
		return str != null && str.trim().equals("") == false;
	}
	
	public static boolean isEmpty(String str)
	{
		return str == null || str.trim().equals("");
	}
}
