/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerListByBizEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 15 Dec 2005		SC									Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import com.gridnode.gtas.events.enterprise.GetPartnerListByBizEntityEvent;
import com.gridnode.gtas.server.bizreg.helpers.*;
import com.gridnode.pdip.app.partner.model.IPartner;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;

/**
 * Test data that needed to be setup in gtas to run this test (use ui to input data):
 *	- BE with id = A1, this BE has 2 partners (partner id = P1 and P2)
 *  - BE with id = A2, this BE has 2 partners (partner id = P3 and P4)
 *  - BE with id = A3, this BE has no partner.
 *  - BE with id = A4, this BE has no partner.
 *  - Make sure there is no BE with id = 100,000 and no BE with id = A6 and enterpriseId = null.  
 *
 * Check the console for the actual output. Compared the actual output with the expected output.
 */
public class GetPartnerListByBizEntityActionTest extends TestCase
{
	private static boolean firstTime = true;
	private Collection entityList = null;
	private GetPartnerListByBizEntityEvent event = null;
	
	public GetPartnerListByBizEntityActionTest(String name) throws Exception
	{
		super(name);
		if (firstTime)
		{
			firstTime = false;
			Util.login();
		}
	}
	
	/**
	 * For now, I print the message to console.
	 */
	private void log(String message)
	{
		System.out.println(message);
	}
	
	private void logBegin(String message)
	{
		log("--- " + message + " ---");
	}
	
	private void doTest() throws Exception
	{
		Object obj = Util.fireEvent(event);
		EntityListResponseData listData = (EntityListResponseData) obj;
		entityList = listData.getEntityList();
		Iterator it = entityList.iterator();
		while (it.hasNext())
		{
			HashMap map = (HashMap) it.next();
			log("partner id = " + map.get(IPartner.PARTNER_ID));
		}
	}

	/**
	 * Expected output: 2 partners with partner id = P1, P2.
	 */
	public void testGetByUid() throws Exception
	{
		logBegin("testGetByUid");
		event = new GetPartnerListByBizEntityEvent(new Long(12));
		doTest();
	}
	
	/**
	 * Expected output: 2 partners with partner id = P3, P4.
	 */
	public void testGetByIdAndEnterpriseId() throws Exception
	{
		logBegin("testGetByIdAndEnterpriseId");
		event = new GetPartnerListByBizEntityEvent("A2", null);
		doTest();
	}

	/**
	 * Expected output: nothing.
	 */
	public void testGetByUidNoPartner() throws Exception
	{
		logBegin("testGetByUidNoPartner");
		event = new GetPartnerListByBizEntityEvent(new Long(14));
		doTest();
	}
	
	/**
	 * Expected output: nothing.
	 */
	public void testGetByIdAndEnterpriseIdNoPartner() throws Exception
	{
		logBegin("testGetByIdAndEnterpriseIdNoPartner");
		event = new GetPartnerListByBizEntityEvent("A4", null);
		doTest();
	}
	
	/**
	 * Get partner list by be uid but the be doesn't exist.
	 * Expected output: nothing.
	 */
	public void testGetByUidBeNotExist() throws Exception
	{
		logBegin("testGetByUidBeNotExist");
		event = new GetPartnerListByBizEntityEvent(new Long(100000l));
		try
		{
			doTest();
			fail("Exception expected");
		} catch (Exception expected)
		{
		}
	}
	
	/**
	 * Get partner list by be id and enterprise id but the be doesn't exist.
	 * Expected output: nothing.
	 */
	public void testGetByIdAndEnterpriseIdBeNotExist() throws Exception
	{
		logBegin("testGetByIdAndEnterpriseIdBeNotExist");
		event = new GetPartnerListByBizEntityEvent("A6", null);
		try
		{
			doTest();
			fail("Exception expected");
		} catch (Exception expected)
		{
		}
	}
}
