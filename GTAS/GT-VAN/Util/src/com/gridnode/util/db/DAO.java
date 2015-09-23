/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 * Dec 12, 2006   i00107              No longer cache the current session
 * Jan 05 2007    i00107              Add beginTransaction(newSession).
 * Jan 12 2007    i00107              Use TransactionContext to handle transactions.
 * Feb 14 2007    Tam Wei Xiang       Added method queryN: to support the retrieving
 *                                    of a number of entity which defined
 *                                    by the caller.
 * Feb 22,2007    Tam Wei Xiang       Added method get() to support the checking
 *                                    on whether a particular entity is exist in
 *                                    DB.           
 * May 03, 2007   Tam Wei Xiang       Added method queryWithLock(...) to support query
 *                                    with certain lock mode. For example LockMode.Upgrade
 *                                    that will be used as sql 'select ... for update'.  
 * May 22, 2007   Tam Wei Xiang       updated createQuery(...) to support binding collection 
 *                                    of values into the query(eg WHERE ... in (...) ).                                    
 */

package com.gridnode.util.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author i00107
 * This class handles the database access routines.
 */
public class DAO
{
  private TransactionContext _ctx;
  
  /**
   * Constructs an instance of DAO, not associating to a TransactionContext.
   */
  public DAO()
  {
  }

  /**
   * Constructs an instance of DAO, associating to a default TransactionContext
   * @param newSession Whether to open a new session for the TransactionContext or
   * use the current transaction-bound session.
   */
  public DAO(boolean newSession)
  {
    associateTransactionContext(newSession);
  }
  
  /**
   * Associate a new TransactionContext with this DAO.
   * @param newSession Whether to open a new session for the TransactionContext or
   * use the current transaction-bound session.
   */
  public void associateTransactionContext(boolean newSession)
  {
    setTransactionContext(new TransactionContext(getClass(), newSession));
  }
  
  /**
   * Set a TransactionContext for this DAO.
   * @param ctx The TransactionContext.
   */
  public void setTransactionContext(TransactionContext ctx)
  {
    _ctx = ctx;
  }
  
  /**
   * Get the TransactionContext associated with this DAO.
   * @return The TransactionContext.
   */
  public TransactionContext getTransactionContext()
  {
    return _ctx;
  }
  
  protected Session getCurrentSession()
  {
    if (_ctx == null)
    {
      throw new IllegalArgumentException("No TransactionContext is associated with this DAO.");
    }
    return _ctx.getSession();
  }
  
  /**
   * Begin a unit of work using the current transaction context associated
   * with this DAO.
   */
  public void beginTransaction()
  {
    getTransactionContext().beginTransaction();
  }

  /**
   * Commit the changes in unit of work started in the current transaction context
   * associated with this DAO.
   */
  public void commitTransaction()
  {
    getTransactionContext().commitTransaction();
  }

  /**
   * Rollback the changes in the unit of work started in the current transaction
   * context associated with this DAO.
   */
  public void rollbackTransaction()
  {
    getTransactionContext().rollbackTransaction();
  }
  
  /**
   * Check if an active transaction is ongoing in the current session.
   * 
   * @return <b>true</b> if an active transaction is ongoing, <b>false</b>
   * otherwise.
   */
  public boolean hasTransaction()
  {
    return getTransactionContext().hasActiveTransaction();
  }
  
  /**
   * Close the current transaction context associated with this DAO.
   */
  public void closeTransactionContext()
  {
    getTransactionContext().close();
  }
  
  /**
   * Load the persisted instance of entity with the specified key.
   * 
   * @param objClass The type of the entity to load.
   * @param key The primary key of the entity to load.
   * @return The loaded persisted instance of the entity. This may throw exception if an instance
   * is not found with the specified key.
   */
  public Object load(Class objClass, Serializable key)
  {
    return getCurrentSession().load(objClass, key);
  }
  
  /**
   * Persist the specified entity instance.
   * @param obj The entity instance to persist to database.
   * @return The primary key of the entity instance persisted.
   */
  public Serializable create(Object obj)
  {
    return getCurrentSession().save(obj);
  }
  
  /**
   * Persist the changes made to an existing entity instance.
   * @param obj The entity instance with the made changes.
   */
  public void update(Object obj)
  {
    getCurrentSession().update(obj);
  }
  
  /**
   * Delete an existing entity instance.
   * @param obj The entity instance to delete.
   */
  public void delete(Object obj)
  {
    getCurrentSession().delete(obj);
  }
  
  /**
   * Retrieve all entity instances of the specified type.
   * @param objName The entity type.
   * @return The list of entity instances retrieved.
   */
  public List getAll(String objName)
  {
    return getCurrentSession().createQuery("from "+objName).list();
  }
  
  /**
   * Execute a retrieval query of the specified name.
   * 
   * @param queryName The name of the query to execute.
   * @param paramNames The names of the parameters to pass to the query.
   * @param paramVals The values of the parameters to pass to the query.
   * @return The list of entity instances retrieved after execution of the query. <b>null</b> if
   * the specified query does not exist.
   */
  public List query(String queryName, String[] paramNames, Object[] paramVals)
  {
    Query query = createQuery(queryName, paramNames, paramVals);
    if (query == null)
    {
      return null;
    }
    return query.list();
  }

  /**
   * Execute a retrieval query of the specified name and return only one entity instance.
   * 
   * @param queryName The name of the query to execute.
   * @param paramNames The names of the parameters to pass to the query.
   * @param paramVals The values of the parameters to pass to the query.
   * @return The entity instance retrieved after execution of the query. <b>null</b> if the
   * specified query does not exist.
   */
  public Object queryOne(String queryName, String[] paramNames, Object[] paramVals)
  {
    Query query = createQuery(queryName, paramNames, paramVals);
    if (query == null)
    {
      return null;
    }
    query.setMaxResults(1);
    return query.uniqueResult();
  }
  
  /**
   * Execute a retrieval query of the specified name and return at most number of
   * entity instance as given by the maxResult.
   * @param queryName The name of the query to execute.
   * @param paramNames The names of the parameters to pass to the query.
   * @param paramVals The values of the parameters to pass to the query.
   * @param maxResult The maximum number of entity to be returned
   * @return The entity instance retrieved after execution of the query. <b>null</b> if the
   * specified query does not exist.
   */
  public List queryN(String queryName, String[] paramNames, Object[] paramVals, int maxResult)
  {
    Query query = createQuery(queryName, paramNames, paramVals);
    if (query == null)
    {
      return null;
    }
    query.setMaxResults(maxResult);
    return query.list();
  }
  
  /**
   * TWX 02 May 07 Execute a retrieval query of the specified name and return list of entities
   * 
   * @param queryName The name of the query to execute.
   * @param paramNames The names of the parameters to pass to the query.
   * @param paramVals The values of the parameters to pass to the query.
   * @param alias The alias specify in the FROM clause of named query
   * @param lockMode The lock mode provided by Hibernate
   * @return The entity instance retrieved after execution of the query. <b>null</b> if the
   * specified query does not exist.
   */
  public List queryWithLock(String queryName, String[] paramNames, Object[] paramVals, String alias, LockMode lockMode)
  {
    Query query = createQuery(queryName, paramNames, paramVals);
    if (query == null)
    {
      return null;
    }
    
    query.setLockMode(alias, lockMode);
    return query.list();
  }
  
  /**
   * Execute an update query of the specified name.
   * 
   * @param queryName The name of the query to execute.
   * @param paramNames The names of the parameters to pass to the query.
   * @param paramVals The values of the parameters to pass to the query.
   * @return The result of the execution of the update, indicating the number of entities affected
   * by the update query. <b>-1</b> if the specified query does not exist. 
   */
  public int updateQuery(String queryName, String[] paramNames, Object[] paramVals)
  {
    Query query = createQuery(queryName, paramNames, paramVals);
    if (query == null)
    {
      return -1;
    }
    return query.executeUpdate();
  }
  
  /**
   * Create a Query object for a named query, and set the parameters for the query execution.
   * @param queryName The name of the query.
   * @param paramNames The names of the parameters to pass to the query. 
   * @param paramVals The values of the parameters to pass to the query.
   * @return The created Query object. <b>null</b> if the query of the specified name does not
   * exist.
   */
  protected Query createQuery(String queryName, String[] paramNames, Object[] paramVals)
  {
    Query query = getCurrentSession().getNamedQuery(queryName);
    if (query == null)
    {
      System.err.println("Unable to find named query: "+queryName);
      return null;
    }
    if (paramNames != null && paramVals != null && paramNames.length<=paramVals.length)
    {
      for (int i=0; i<paramNames.length; i++)
      {
        Object paramValue = paramVals[i];
        String paramName = paramNames[i];
        if(paramValue instanceof Collection)
        {
          query.setParameterList(paramName, (Collection)paramValue);
        }
        else
        {
          query.setParameter(paramName, paramValue);
        }
      }
    }

    return query;
  }
  
  /**
   * Flush the current session.
   */
  public void flushSession()
  {
    getCurrentSession().flush();
  }
  
  /**
   * Refresh the state of the specified entity instance.
   * @param obj The entity instance to refresh. This instance must be one that is loaded earlier in
   * the current session.
   */
  public void refreshObject(Object obj)
  {
    getCurrentSession().refresh(obj);
  }
  
  /**
   * TWX Get the persistent instance of the given entity class with the given identifier, 
   * or null if there is no such persistent instance. No exception will be thrown
   * if there is no such an entity in DB.
   * @param cls the entity class that we are loading  
   * @param uid the unique identifier of the entity
   * @return
   */
  public Object get(Class cls, Serializable uid)
  {
    return getCurrentSession().get(cls, uid);
  }
}
