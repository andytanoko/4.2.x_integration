/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: RetrieveDomainIdentifiersActionTest.java
 * 
 * ***************************************************************************
 * Date             Author Changes
 * *************************************************************************** 
 * 14 Dec 2005			SC		 Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.bizreg.DomainIdentifiersConstants;
import com.gridnode.gtas.events.bizreg.RetrieveDomainIdentifiersEvent;
import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.server.bizreg.helpers.Util;
import junit.framework.TestCase;

/**
 * In test method's javadoc comment, test data section specified: what data must be setuped in gtas for the test to run sucessfully.
 * Note: If "test data: specific to my own data" is specified in method comments, to run this method, you have to chage the value of 
 *       variables in method to your value in gtas db.
 */
public class RetrieveDomainIdentifiersActionTest extends TestCase implements DomainIdentifiersConstants
{
	private static boolean firstTime = true;
	
	private String[] sourceIdentifiers;
	private String sourceType;
	private String targetType;
	private String[] enterpriseIds;
	
	private String[] targetIdentifiers;
	private String[] retEnterpriseIds;
	
	public RetrieveDomainIdentifiersActionTest(String name) throws Exception
	{
		super(name);
		
		if (firstTime)
		{
			Util.login();
			firstTime = false;
		}
	}
	
	public void setUp()
	{
		sourceIdentifiers = null;
		sourceType = null;
		targetType = null;
		enterpriseIds = null;
		targetIdentifiers = null;
		retEnterpriseIds = null;
	}
	
	private void retrieveDomainIdentifiers() throws Exception
	{
		RetrieveDomainIdentifiersEvent event = new RetrieveDomainIdentifiersEvent();
		event.setSourceIdentifiers(sourceIdentifiers);
		event.setSourceType(sourceType);
		event.setTargetType(targetType);
		event.setEnterpriseIds(enterpriseIds);
		HashMap map = (HashMap) Util.fireEvent(event);
		targetIdentifiers = (String[]) map.get(RESULT_TARGET_IDENTIFIERS);
		retEnterpriseIds = (String[]) map.get(RESULT_ENTERPRISE_IDENTIFIERS);
	}
	
	/**
	 * Test for case where sourceType equals to target type.
	 * test data: none.
	 */
	public void testSourceTypeEqualsTargetType() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = sourceType;
		sourceIdentifiers = new String[] {"123"};
		enterpriseIds = new String[] {"ABC"};
		retrieveDomainIdentifiers();
		assertTrue(targetIdentifiers.length == 1);
		assertTrue(targetIdentifiers[0].equals("123"));
		assertTrue(retEnterpriseIds.length == 1);
		assertTrue(retEnterpriseIds[0].equals("ABC"));
	}
	
	/**
	 * Maps biz ent id to biz ent uid.
	 * test data: specific to my own data.
	 */
	public void testBizEntIdToBizEntUid() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = BUSINESS_ENTITY_UID;
		enterpriseIds = new String[] {"2112"};
		sourceIdentifiers = new String[] {"DEF"};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"1"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Maps biz ent uid to duns in white page of bus ent.
	 * test data: specific to my own data.
	 */
	public void testBizEntUidToDunsInWhitePage() throws Exception
	{
		sourceType = BUSINESS_ENTITY_UID;
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"1"};
		enterpriseIds = new String[] {"2112"};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"123123123"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Maps duns in white page of biz ent to biz ent id. (only 1 biz ent is returned from findBusinessEntitiesByWhitePage method.)
	 * test data: specific to my own data.
	 */
	public void testDunsInWhitePageToBizEntId() throws Exception
	{
		sourceType = DUNS_NUMBER;
		targetType = BUSINESS_ENTITY_ID;
		sourceIdentifiers = new String[] {"123123123"};
		enterpriseIds = new String[] {"2112"};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"DEF"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Maps duns in white page of biz ent to biz ent id. (> 1 biz ent is returned from findBusinessEntitiesByWhitePage method.)
	 * test data: Create biz ent (partner) wih duns = 008008008, starfish id = "Starfish100" and id = T1.
	 */
	public void testDunsInWhitePageToBizEntId2() throws Exception
	{
		sourceType = DUNS_NUMBER;
		targetType = BUSINESS_ENTITY_ID;
		sourceIdentifiers = new String[] {"008008008"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"T1"}, targetIdentifiers);
		// returned enterprise id may not be the same as original enter prise ids so don't check if they are the same.
	}
	
	/**
	 * Maps biz ent id to duns in domain identifier.
	 * test data: create biz ent (partner) with id = "ABC" and add duns to domain identifier as "004004004"
	 */
	public void testBizEntIdToDunsInDomainId() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"ABC"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"004004004"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Maps duns in domain identifier to starfish id.
	 * test data: create biz ent (partner) with duns in domain identifier page : 006006006.
	 *            With this biz ent specifiy starfish id as "Startfish1".
	 */
	public void testDunsInDomainIdToStarfishId() throws Exception
	{
		sourceType = DUNS_NUMBER;
		targetType = STARFISH_ID;
		sourceIdentifiers = new String[] {"006006006"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"Starfish1"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Maps starfish id to biz ent id.
	 * test data: create biz ent (partner) with starfish id = "Starfish2" and id = "e10".
	 */
	public void testStarfishIdToBizEntId() throws Exception
	{
		sourceType = STARFISH_ID;
		targetType = BUSINESS_ENTITY_ID;
		sourceIdentifiers = new String[] {"Starfish2"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {"e10"}, targetIdentifiers);
		assertEqualsArray(enterpriseIds, retEnterpriseIds);
	}
	
	/**
	 * Test for when sourceType is BUSINESS_ENTITY_ID and retrieved biz ent's state is DELETED.
	 * test data: create biz ent (partner) with id = "s7" and duns number = "111111111". 
	 * Then delete this biz ent. 
	 * Note: Before delete, this test must fail. After delete, this test must pass.
	 */
	public void testDeletedBizEnt() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"s7"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {null}, targetIdentifiers);
		assertEqualsArray(new String[] {null}, retEnterpriseIds);
	}
	
	/**
	 * Test for when sourceType is BUSINESS_ENTITY_UID and retrieved biz ent's state is DELETED.
	 * test data: This test uses deleted biz ent in method testDeletedBizEnt. sourceIdentifiers value can be found in gtas database.
	 */
	public void testDeteledBizEnt2() throws Exception
	{
		sourceType = BUSINESS_ENTITY_UID;
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"8"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {null}, targetIdentifiers);
		assertEqualsArray(new String[] {null}, retEnterpriseIds);
	}
	
	/**
	 * test data: make sure there is no entity with duns = 999999001.
	 */
	public void testNoEntity() throws Exception
	{
		sourceType = DUNS_NUMBER;
		targetType = BUSINESS_ENTITY_ID;
		sourceIdentifiers = new String[] {"999999001"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {null}, targetIdentifiers);
		assertEqualsArray(new String[] {null}, retEnterpriseIds);
	}
	
	/**
	 * This test we set sourceType to something system doesn't recognize.  It must return AssertionError.
	 */
	public void testSetInvalidSourceType() throws Exception
	{
		sourceType = "invalidType";
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"someValue"};
		enterpriseIds = new String[] {null};
		try
		{
			retrieveDomainIdentifiers();
			fail("AssertionError expected");
		} catch (AssertionError expected)
		{	
		}
	}
	
	/**
	 * Purpose: cannot find duns number.
	 * test data: create biz ent with id = "id10". Don't specifiy duns and starfish id.
	 */
	public void testDunsNotFound() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = DUNS_NUMBER;
		sourceIdentifiers = new String[] {"id10"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {null}, targetIdentifiers);
		assertEqualsArray(new String[] {null}, retEnterpriseIds);
	}
	
	/**
	 * Cannot find starfish id.
	 * test data: use same test data as method testDunsNotFound.
	 */
	public void testStarfishNotFound() throws Exception
	{
		sourceType = BUSINESS_ENTITY_ID;
		targetType = STARFISH_ID;
		sourceIdentifiers = new String[] {"id10"};
		enterpriseIds = new String[] {null};
		retrieveDomainIdentifiers();
		assertEqualsArray(new String[] {null}, targetIdentifiers);
		assertEqualsArray(new String[] {null}, retEnterpriseIds);
	}
	
	private void assertEqualsArray(String[] ar1, String[] ar2)
	{
		assertTrue(ar1.length == ar2.length);
		for (int i = 0; i < ar1.length; i++)
		{
			assertEquals(ar1[i], ar2[i]);
		}
	}
}
