/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Util.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * 6 Dec 2005       SC									Created
 */
package com.gridnode.gtas.server.alert.helpers;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.gridnode.gtas.client.ctrl.SessionCreationException;
import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.AssertUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.gtas.server.rdm.ejb.*;

public class Util
{
	private static IGridTalkClientControllerObj _gtClientCtrl = null;
	
	/**
	 * This method fires <code>event</code> to <code>IGridTalkClientControllerObj</code>.
	 */
	public static Object fireEvent(IEvent event) throws Exception
	{
		AssertUtil.assertTrue(event != null);
    BasicEventResponse response = (BasicEventResponse) fireEventHelper(event);
    if(response == null)
    {
      throw new java.lang.NullPointerException("Response is null for event:" + event.getClass().getName());
    }
    if (!response.isEventSuccessful())
    {
      throw new Exception(event.getClass().getName() + " is not sucessful.");
    }
    return response.getReturnData();
	}
	
	private static IEventResponse fireEventHelper(IEvent event) throws EventException, RemoteException, NamingException, CreateException
	{
		if (_gtClientCtrl == null)
		{
			getClientClientControlFirstTime();
		}
		return _gtClientCtrl.processEvent(event);
	}
	
	private static void getClientClientControlFirstTime() throws NamingException, RemoteException, CreateException
	{
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		properties.put(Context.PROVIDER_URL, "localhost:31099");
		InitialContext jndiContext = new InitialContext(properties);
		Object ref = jndiContext.lookup("com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome");
		IGridTalkClientControllerHome home = (IGridTalkClientControllerHome) PortableRemoteObject.narrow(ref, IGridTalkClientControllerHome.class);
		_gtClientCtrl = home.create();
	}
	
	public static void login() throws Exception
	{
		UserLoginEvent loginEvent = new UserLoginEvent("admin", "admin");
		Util.fireEvent(loginEvent);
	}
}
