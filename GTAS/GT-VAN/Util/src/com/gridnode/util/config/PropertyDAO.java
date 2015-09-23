/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PropertyDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 * Dec 12, 2006   i00107              Add rollbackTransaction() on failure.
 * Jan 05 2007    i00107              Prefix query name with object class name.
 * Jan 12 2007    i00107              Use TransactionContext to handle transactions.
 * Mar 14, 2007	  i00118			  Added updateProperty()
 * Mar 21, 2007   i00118			  Modified updateProperty()
 * Nov 09, 2009   Tam Wei Xiang       #1105 - Add method lockPropertyNoWait().
 */

package com.gridnode.util.config;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.exception.LockAcquisitionException;

import com.gridnode.util.db.DAO;

/**
 * @author i00107
 * This DAO handles the retrieval of configuration properties.
 */
public class PropertyDAO extends DAO
{ 
  protected PropertyDAO()
  {
  }
  
  public synchronized Property getProperty(String category, String propertyKey)
  {
    String[] paramNames = {"category", "key"};
    Object[] paramVals = {category, propertyKey};
    String queryName = Property.class.getName()+".getProperty";
    Property prop = null;

    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if (!hasTx)
      {
        beginTransaction();
      }
      prop = (Property)queryOne(queryName, paramNames, paramVals);
      if (!hasTx)
      {
        commitTransaction();
      }
    }
    catch (Exception ex)
    {
      System.out.println("[PropertyDAO.getProperty] Error fetching a property: "+category+", "+propertyKey);
      ex.printStackTrace(System.out);
      if (!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if (!hasTx)
      {
        closeTransactionContext();
      }
    }
    return prop;
  }
  
  public synchronized List getPropertyList(String category)
  {
    String[] paramNames = {"category"};
    Object[] paramVals = {category};
    String queryName = Property.class.getName()+".getPropertyList";
    List result = null;
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if (!hasTx)
      {
        beginTransaction();
      }
      result = query(queryName, paramNames, paramVals);
      if (!hasTx)
      {
        commitTransaction();
      }
    }
    catch (Exception ex)
    {
      System.out.println("[PropertyDAO.getPropertyList] Error fetching properties for category: "+category);
      ex.printStackTrace(System.out);
      if (!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if (!hasTx)
      {
        closeTransactionContext();
      }
    }
    return result;
  }

  public synchronized int updateProperty(String cat, String key, String value)
  {
	String[] paramNames = {"category", "key", "value"};
	String[] paramVals = {cat, key, value};	  
    String queryName = Property.class.getName()+".updateProperty";
    int result = -1;
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if (!hasTx)
      {
        beginTransaction();
      }
      result = updateQuery(queryName, paramNames, paramVals);
      if (!hasTx)
      {
        commitTransaction();
      }
    }
    catch (Exception ex)
    {
      System.out.println("[PropertyDAO.getPropertyList] Error updating property for category: "+cat+" and property key: "+key);
      ex.printStackTrace(System.out);
      if (!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if (!hasTx)
      {
        closeTransactionContext();
      }
    }
    return result;
  }  
  
  /**
   * TODO: TWX: The approach to acquire the lock is dirty (causing DB error logged in the log), 
   * explore JBOSS Cache !!!...
   * 
   * #1105 TWX 20091104
   * Lock a particular config_props record in DB to simulate the java lock. This will be useful in
   * the clustering environment. However it may not be scalable due to it is an global lock (eg all
   * other threads will be trying to obtain the same lock). If the lock has been acquired by one of the thread,
   * the other thread will not be able to get it and return it.
   * @param category
   * @param propertyKey
   * @return true if the db can lock the given property record. false if other thread already locking the record.
   */
  public boolean lockPropertyNoWait(String category, String propertyKey) throws Throwable
  {
    String[] paramNames = {"category", "key"};
    Object[] paramVals = {category, propertyKey};
    String queryName = Property.class.getName()+".getProperty";
    
    associateTransactionContext(false);
    
    try
    {
      List list = queryWithLock(queryName, paramNames, paramVals, "p", LockMode.UPGRADE_NOWAIT);
      if(list != null && list.size() > 0)
      {
        return true;
      }
      else
      {
        throw new Exception("Please init the properties with category: "+category+" and propertyKey: "+propertyKey);
      }
      
    }
    catch(Throwable th)
    {
      if(isLockAcquisitionException(th))
      {
        System.out.println("Failed to acquire lock on Property category:"+category+", propertyKey:"+propertyKey+". Err msg:"+th.getMessage());        
        return false;
      }
      else
      {
        throw th;
      }
    }
    
  }
  
  public boolean isLockAcquisitionException(Throwable ex)
  {
    if(ex == null)
    {
      return false;
    }
    
    if(ex != null && ex instanceof LockAcquisitionException)
    {
      return true;
    }
    else
    {
      return isLockAcquisitionException(ex.getCause());
    }
  }
}
