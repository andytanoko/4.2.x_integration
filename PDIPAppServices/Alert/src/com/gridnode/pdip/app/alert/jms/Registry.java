/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Registry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.alert.jms;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * This class keep track the accessible flags of jmsDestnames.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class Registry
{
	private static Registry _registry;
	private Hashtable<String, Boolean> _registryHTB = new Hashtable<String, Boolean>(); //<jmsDestname,true> meaning we are locking the jmsDest false otherwise.
	
	private Registry() {}
	
	public static synchronized Registry getInstance()
	{
		if(_registry == null)
		{
			_registry = new Registry();
		}
		return _registry;
	}
	
	/**
	 * Cache a list of jms destination name and its correspond accesible flag.
	 * @param jmsDestinationName It contains the latest jms destination name we have so far.
	 */
	public void syncJmsDestination(Collection<String> jmsDestinationName)
	{
		Hashtable<String, Boolean> newRegistryHTB = new Hashtable<String, Boolean>();
		Iterator<String> i = jmsDestinationName.iterator();
		synchronized(_registryHTB)
		{
			while(i.hasNext())
			{
				String jmsDestName = i.next();
				if(_registryHTB.containsKey(jmsDestName))
				{
					newRegistryHTB.put(jmsDestName, _registryHTB.get(jmsDestName));
				}
				else //the jmsDestName is newly created by user.
				{
					newRegistryHTB.put(jmsDestName, false);
				}
				//Note: for those jmsDestname that is in _registryHTB but not in the
				//      latest jmsDestinationName, we will not keep track of them.
			}
		
			_registryHTB.clear();
		
			_registryHTB.putAll(newRegistryHTB);
		}
	}
	
	/**
	 * Release the lock on a particular jmsDestname.
	 * @param jmsDestname name of the jms destination
	 * @throws SystemException
	 */
	public void releaseLock(String jmsDestname)
		throws SystemException
	{
		synchronized(_registryHTB)
		{
			if(_registryHTB.containsKey(jmsDestname))
			{
				_registryHTB.put(jmsDestname, false);
				return;
			}
		}
		Exception ex = new Exception("The jms record with name '"+ jmsDestname+"' is not found in table jms_destination.");
		AlertLogger.warnLog("Registry", "releaseLock", "Error", ex);
	}
	
	/**
	 * Acquire a lock on a particular jms destination.
	 * @param jmsDestname the JmsDestination name.
	 * @return true if successful acquire a lock false otherwise.
	 */
	public boolean acquireLock(String jmsDestname)
		throws SystemException, Exception
	{
		synchronized(_registryHTB)
		{
			if(_registryHTB.containsKey(jmsDestname))
			{
				if(_registryHTB.get(jmsDestname)) //the jms dest name is locked.
				{
					return false;
				}
				else
				{
					_registryHTB.put(jmsDestname, true);
					return true;
				}
			}
		}
		Exception ex = new Exception("The jms record with name '"+ jmsDestname+"' is not found in table jms_destination.");
		throw new SystemException("Error: "+ex.getMessage(), ex);
	}
	
/*	
	// debugging purpose
	public boolean getLockStatus(String jmsDestname)
	{
		return _registryHTB.get(jmsDestname);
	}
	
	private synchronized Hashtable getRegistry()
	{
		return this._registryHTB;
	}
	
	public static void main(String args[])
		throws Exception
	{
		
		Registry r = Registry.getInstance();
		ArrayList a = new ArrayList();
		a.add("a");
		a.add("b");
		a.add("c");
		a.add("d");
		r.syncJmsDestination(a);
		
		/*
		a.clear();
		a.add("a");
		a.add("d");
		r.syncJmsDestination(a);
		r.acquireLock("a");
		
		Enumeration enu = r.getRegistry().keys();
		while(enu.hasMoreElements())
		{
			String key = (String)enu.nextElement();
			System.out.println("key is "+ key+" value is "+r._registryHTB.get(key));
		} 
		myRunner [] runner = new myRunner[]{new myRunner(), new myRunner()};
		runner[0].start();
		Thread d = new Thread(); d.sleep(1000);
		runner[1].start("123");
		
		
		Enumeration enu = r.getRegistry().keys();
		while(enu.hasMoreElements())
		{
			String key = (String)enu.nextElement();
			System.out.println("key is "+ key+" value is "+r._registryHTB.get(key));
		} 
		
	} */
	
}



