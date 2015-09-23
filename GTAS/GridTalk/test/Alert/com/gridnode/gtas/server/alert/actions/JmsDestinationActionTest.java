/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationActionTest.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * 4 Jan 06         SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.TestCase;

import com.gridnode.gtas.events.alert.CreateJmsDestinationEvent;
import com.gridnode.gtas.events.alert.DeleteJmsDestinationEvent;
import com.gridnode.gtas.events.alert.GetJmsDestinationEvent;
import com.gridnode.gtas.events.alert.GetJmsDestinationListEvent;
import com.gridnode.gtas.events.alert.UpdateJmsDestinationEvent;
import com.gridnode.gtas.server.alert.helpers.Util;
import com.gridnode.pdip.app.alert.model.IJmsDestination;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * The test are to be run sequentially. First comment all tests. 
 * Then uncomment 1st test. Run. Check db. Comment 1st test.
 * continue with 2st test.
 * 
 * 13 Jan 06: all test methods are pass.
 */
public class JmsDestinationActionTest extends TestCase
{
	public static final Integer INT = new Integer(1);
	private IEvent event;
	
	private void logBegin(String message)
	{
		log("TEST: " + message);
	}

	private void log(String message)
	{
		System.out.println(message);
	}
	
	public JmsDestinationActionTest(String name) throws Exception
	{
		super(name);
		Util.login();
	}
	
//	public void testCreate1() throws Exception
//	{
//		logBegin("testCreate1");
//		Properties p = new Properties();
//		p.setProperty("2", "2");
//		p.setProperty("1", "1");
//		event = new CreateJmsDestinationEvent("myJMS19", new Integer(1),
//																					"java:/com/myJMSDest",
//																					new Integer(0), new Integer(2),
//																					"java:/factoryjndi", "admin",
//																					"admin", p, new Integer(3300),
//																					new Integer(2));
//		Util.fireEvent(event);
//	}
	
//	public void testCreate2() throws Exception
//	{
//		logBegin("testCreate2");
//		Properties p = new Properties();
//		p.setProperty("2", "2");
//		p.setProperty("1", "1");
//		event = new CreateJmsDestinationEvent("myJMS", new Integer(1),
//																					"queue/myTestQueue", new Integer(0),
//																					new Integer(2),
//																					"ConnectionFacctory123", "admin",
//																					"admin", p, new Integer(3300),
//																					new Integer(2));
//		Util.fireEvent(event);
//	}
	
//	public void testGet() throws Exception
//	{
//		logBegin("testGet");
//		//*** fill in  uid ***
//		event = new GetJmsDestinationEvent(new Long(1));
//		HashMap map = (HashMap) Util.fireEvent(event);
//		log("name = " + map.get(IJmsDestination.NAME));
//		log("jndi name = " + map.get(IJmsDestination.JNDI_NAME));
//	}
	
//	public void testGetList() throws Exception
//	{
//		logBegin("testGetList");
//		event = new GetJmsDestinationListEvent();
//		Object obj = Util.fireEvent(event);
//		EntityListResponseData listData = (EntityListResponseData) obj;
//		Collection entityList = listData.getEntityList();
//		Iterator it = entityList.iterator();
//		while (it.hasNext())
//		{
//			HashMap map = (HashMap) it.next();
//			log("name = " + map.get(IJmsDestination.NAME));
//			log("jndi name = " + map.get(IJmsDestination.JNDI_NAME));
//		}
//	}
	
//	public void testUpdate() throws Exception
//	{
//		logBegin("testUpdate");
//		Properties p = new Properties();
//		p.setProperty("2", "2");
//		p.setProperty("1", "1");
//		//*** fill in  uid ***
//		event = new UpdateJmsDestinationEvent(new Long(1),
//		                                      "newName", new Integer(1),
//																				"java:/com/myJMSDest",
//																				new Integer(0), new Integer(2),
//																				"java:/factoryjndi", "admin",
//																				"admin", p, new Integer(3300),
//																				new Integer(2));
//		Util.fireEvent(event);
//	}
	
//	public void testDelete() throws Exception
//	{
//		logBegin("testDelete");
//		//*** fill in  uid ***
//		event = new DeleteJmsDestinationEvent(new Long(1));
//		Util.fireEvent(event);
//	}
	
//	//TEST
//	public void testCreate1() throws Exception
//	{
//		logBegin("testCreate1");
//		Properties p = new Properties();
//		p.setProperty("n1", "v1");
//		event = new CreateJmsDestinationEvent("name2", new Integer(1),
//																					"java:/com/myJMSDest",
//																					new Integer(0), new Integer(2),
//																					"java:/factoryjndi", "admin",
//																					"admin", p, new Integer(3300),
//																					new Integer(2));
//		Util.fireEvent(event);
//	}
	
	public void testUpdate() throws Exception
	{
		logBegin("testUpdate");
		Properties p = new Properties();
		p.setProperty("n1", "change");
		p.setProperty("n2", "v2");
		//*** fill in  uid ***
		event = new UpdateJmsDestinationEvent(new Long(21),
		                                      "name2", new Integer(1),
																				"java:/com/myJMSDest",
																				new Integer(0), new Integer(2),
																				"java:/factoryjndi", "admin",
																				"admin", p, new Integer(3300),
																				new Integer(2));
		Util.fireEvent(event);
	}
}