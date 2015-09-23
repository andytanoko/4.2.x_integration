/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityDBManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 3, 2006    Tam Wei Xiang       Created
 * Feb 09 2007		Alain Ah Ming				Add error codes to error log. Otherwise, 
 * 																		log as warning
 * Jun 29 2009    Tam Wei Xiang       #560 - commented RSA BSAFE/JSAFE related IMPL
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.gridnode.pdip.base.certificate.exceptions.ILogErrorCodes;

/**
 * It serves as a lightweight 'Pool' for SecurityDB instance. All the access to the SecurityDB must go through
 * this pool. 
 * 
 * It help to ensure that the same SecurityDB instance (contains the CertJ instance) will not be concurrently used by different 
 * thread at the same time. See GNDB00026569 for more detail.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 * @version GT 4.0 VAN
 */
public class SecurityDBManager
{
	private int _maxFreeSecDBInstance = 5; // the total available SecurityDB instances that is allowed to be existed in the system
	private long _cleanupInterval = 10 * 1000; 
	private static final SecurityDBManager _dbManager = new SecurityDBManager();
	private Hashtable _inUsedSecurityDB = new Hashtable();
	private Hashtable _availableSecurityDB = new Hashtable();
	
	private SecurityDBManager()
	{
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new CleanUpTask(this), new Date(), _cleanupInterval);
	}
	
	public static synchronized SecurityDBManager getInstance()
	{
		return _dbManager;
	}
	
	/**
	 * Return a SecurityInstance
	 * Every checkout SecurityDB instance must be released.
	 * @return
	 */
	public SecurityDB getSecurityDB()
	{
		try
		{
			return checkOut();
		}
		catch(Exception ex)
		{
			CertificateLogger.warn("[SecurityDBManager] Error in getting the SecurityDB instance. Err is "+ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * Release the SecurityDB instance we have checked out before.
	 * Every checkout SecurityDB instance must be released.
	 * @param secDB
	 */
	public void releaseSecurityDB(SecurityDB secDB)
	{
		checkIn(secDB);
	}
	
	/**
	 * Obtains the SecurityDB instance from the available resource or initiate
	 * a new instance if all the availabe SecurityDB instance has been checkout(
	 * or no SecurityDB instance exist at all). 
	 * @return
	 * @throws Exception
	 */
	private SecurityDB checkOut()
		throws Exception
	{
		synchronized(this)
		{
			CertificateLogger.log("[SecurityDBManager] Checking out .....");
			SecurityDB secDB = null;
			
			if(_availableSecurityDB.size() > 0)
			{
				CertificateLogger.log("[SecurityDBManager.checkOut] Get from available SecurityDB ... ");
				Enumeration keys = _availableSecurityDB.keys();
				secDB = (SecurityDB)keys.nextElement();
				_inUsedSecurityDB.put(secDB, "");
				_availableSecurityDB.remove(secDB);
			}
			else
			{
				CertificateLogger.log("[SecurityDBManager.checkOut] initiate new SecurityDB... ");
				secDB = new SecurityDB();
				_inUsedSecurityDB.put(secDB, "");
			}
			CertificateLogger.log("[SecurityDBManager.checkOut] Available SecDB "+getTotalAvailableSecurityDBInstance()+". In used SecDB "+getTotalInUsedSecurityDBInstance());
			
			return secDB;
		}
	}
	
	/**
	 * Check in the SecurityDB instance that previously has been checked out.
	 * Invalid injection into the pool will be ignored.
	 * @param secDB
	 */
	private void checkIn(SecurityDB secDB)
	{
		synchronized(this)
		{
			CertificateLogger.log("[SecurityDBManager.checkIn] releasing resource .....");
			
			if(_inUsedSecurityDB.containsKey(secDB))
			{
				_availableSecurityDB.put(secDB, "");
				_inUsedSecurityDB.remove(secDB);
			}
			
			CertificateLogger.log("[SecurityDBManager.checkIn] Available SecDB "+getTotalAvailableSecurityDBInstance()+". In used SecDB "+getTotalInUsedSecurityDBInstance());
		}
	}
	
	/**
	 * Get the total amount of SecurityDB instance that has been created.
	 * @return
	 */
	public synchronized int  getTotalCreatedInstanceSoFar()
	{
		return _inUsedSecurityDB.size() + _availableSecurityDB.size();
	}
	
	/**
	 * Get the total amount of SecurityDB instance that has been checkout.
	 * @return
	 */
	public synchronized int getTotalInUsedSecurityDBInstance()
	{
		return _inUsedSecurityDB.size();
	}
	
	/**
	 * Get the total amount of SecurityDB instance that is available for the client
	 * to use.
	 * @return
	 */
	public synchronized int getTotalAvailableSecurityDBInstance()
	{
		return _availableSecurityDB.size();
	}
	
	/**
	 * Get the total amount of allowable SecurityDB instance that is available for
	 * a client to use. If the available instance has exceed the max allowed value,
	 * a timer task will be performed to clean up the extra instance (Exclude those that
	 * have been checkout).
	 * @return
	 */
	public synchronized int getMaxAllowedInstance()
	{
		return _maxFreeSecDBInstance;
	}
	
	/*
	//TODO to be implemented
	public void releaseDirtySecurityDB(SecurityDB secDB)
	{
		
	} */
	
	/**
	 * Periodically perform clean up on the _availableSecurityDB given the interval _cleanupInterval.
	 * Exclude those that have been checkout.
	 */
	public void cleanup()
	{
//		synchronized(this)
//		{
//			if(_availableSecurityDB.size() > _maxFreeSecDBInstance)
//			{
//				int totalToBeRemoved = _availableSecurityDB.size() - _maxFreeSecDBInstance;
//				CertificateLogger.log("[SecurityDBManager.cleanup] Max allowed Free SecurityDB instance is "+getMaxAllowedInstance()+". Start cleanup extra SecurityDB instance. Total to be cleanup "+totalToBeRemoved);
//			
//				Enumeration enu = _availableSecurityDB.keys();
//				while(totalToBeRemoved > 0)
//				{
//					SecurityDB secDB = (SecurityDB)enu.nextElement();
//					_availableSecurityDB.remove(secDB);
//					secDB.cleanup();
//					secDB = null;
//					--totalToBeRemoved;
//				}
//				
//				CertificateLogger.log("[SecurityDBManager.cleanup] After cleanup. Available SecDB "+getTotalAvailableSecurityDBInstance()+". In used SecDB "+getTotalInUsedSecurityDBInstance());
//			}
//		}
	}
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args)
		throws Exception
	{
		final SecurityDBManager secManager = SecurityDBManager.getInstance();
		
		SecurityDB secDB = null;
		SecurityDB secDB1 = null;
		ArrayList secList = new ArrayList();
		
		/*
		//release the resource
		secDB= secManager.getSecurityDB();
		secManager.releaseSecurityDB(secDB);
		
		
		//in used, new instance
		secDB = secManager.getSecurityDB();
		secDB1 = secManager.getSecurityDB();
		secManager.releaseSecurityDB(secDB);
		secManager.releaseSecurityDB(secDB1);
		
		
		
		//get from availabe resource
		secDB = secManager.getSecurityDB();
		secManager.releaseSecurityDB(secDB);
		secDB = secManager.getSecurityDB();
		secManager.releaseSecurityDB(secDB);
		*/
		
		/*
		//reach max allowed
		int i = 10;
		while(i > 0)
		{
			secDB = secManager.getSecurityDB();
			secList.add(secDB);
			--i;
		}
		
		i = 8;
		while(i > 0)
		{
			secManager.releaseSecurityDB((SecurityDB)secList.get(i));
			--i;
			Thread.sleep(1000);
		}
		
		i = 10;
		while(i > 0)
		{
			secDB = secManager.getSecurityDB();
			secList.add(secDB);
			--i;
		} 
	}*/

}

/**
 * A timer task to perform cleanup on SecurityDBManager
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
class CleanUpTask extends TimerTask
{
	private SecurityDBManager _secDBManager = null;
	
	public CleanUpTask(SecurityDBManager secDBManager)
	{
		_secDBManager = secDBManager;
	}
	
	public void run()
	{
		_secDBManager.cleanup();
	}
}
