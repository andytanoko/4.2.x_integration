/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestIdGenerator.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.util;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;

import java.util.UUID;

public class TestIdGenerator
{
	public static String generateTestId()
	{
//		return "testtestid";
		return ConfigMgr.getPartnerConfig().getDuns()+"-"+UUID.randomUUID().toString();
	}
}
