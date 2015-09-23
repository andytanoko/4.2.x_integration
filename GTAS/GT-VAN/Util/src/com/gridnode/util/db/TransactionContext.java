/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionContext.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 12, 2007        i00107             Created
 */

package com.gridnode.util.db;

import java.util.Properties;

import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gridnode.util.SystemUtil;

/**
 * @author i00107
 * A TransactionContext is used for demarcating units of work in a Session.
 */
public class TransactionContext
{
  private SessionFactory _sessionFactory;
  private Session _currSession;
  
  private boolean _newSession;
  
  /**
   * Construct a TransactionContext
   * 
   * @param daoClass The DAO type for obtaining the SessionFactory
   * @param newSession <b>true</b> to open a new Session, <b>false</b> to use the current transaction-bound session.
   */
  public TransactionContext(Class daoClass, boolean newSession)
  {
    try
    {
      configure(daoClass);
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Unable to instantiate TransactionContext", ex);
    }
    _newSession = newSession;
    _currSession = newSession ? _sessionFactory.openSession() : _sessionFactory.getCurrentSession();
  }

  /**
   * Begin a new transaction in the current session. Commit and rollback can be called after this method on the same instance of
   * the TransactionContext.
   */
  public void beginTransaction()
  {
    assertInactiveTx();
    _currSession.beginTransaction();
  }
  
  /**
   * Commit the current transaction. This must be invoked only on the instance that made a call
   * to beginTransaction() earlier in the same session.
   */
  public void commitTransaction()
  {
    assertActiveTx();
    _currSession.getTransaction().commit();
    if (_newSession)
    {
      _currSession.flush();
    }
  }

  /**
   * Rollback the current transaction. This must be invoked only on the instance that made a call
   * to beginTransaction() earlier in the same session.
   */
  public void rollbackTransaction()
  {
    assertActiveTx();
    _currSession.getTransaction().rollback();
  }

  /**
   * Check if an active transaction is ongoing in the current session associated
   * with this transaction context.
   * @return <b>true</b> if an active transaction is ongoing, <b>false</b> otherwise.
   */
  public boolean hasActiveTransaction()
  {
    Transaction tx = _currSession.getTransaction();
    return (tx != null && tx.isActive());
  }
  
  /**
   * Returns the Session that this TransactionContext is associated with.
   * @return The Session that this TransactionContext is associated with, or <b>null</b> if this
   * TransactionContext has been closed.
   */
  public Session getSession()
  {
    return _currSession;
  }
  
  /**
   * Close this TransactionContext. After close, this context should not be used for any 
   * further for new transactions.
   */
  public void close()
  {
    assertInactiveTx();
    if (_newSession)
    {
      _currSession.close();
    }
    _currSession = null;
  }
  
  protected void assertActiveTx()
  {
    if (_currSession != null)
    {
      Transaction tx = _currSession.getTransaction();
      if (tx == null || !tx.isActive())
      {
        System.err.println("[TransactionContext.assertActiveTx] No transaction is active in this context.");
        throw new RuntimeException("[TransactionContext.assertActiveTx] No active transaction is in progress for the requested operation.");
      }
    }
    else
    {
      System.err.println("[TransactionContext.assertActiveTx] This context has already been closed.");
      throw new RuntimeException("[TransactionContext.assertActiveTx] This context has already been closed.");
    }
  }

  protected void assertInactiveTx()
  {
    if (_currSession != null)
    {
      //check any transaction on
      Transaction tx = _currSession.getTransaction();
      if (tx != null && tx.isActive())
      {
        System.err.println("[TransactionContext.assertInactiveTx] A transaction is active in this context.");
        throw new RuntimeException("[TransactionContext.assertInactiveTx] An active transaction is in progress.");
      }
    }
    else
    {
      System.err.println("[TransactionContext.assertInactiveTx] This context has already been closed.");
      throw new RuntimeException("[TransactionContext.assertInactiveTx] This context has already been closed.");
    }
  }

  /**
   * Configure the TransactionContext with the session factory to use. This is based on the
   * DAO.properties found in the system resource path.
   * @param daoClass The DAO class
   * @throws NamingException Unable to obtain the session factory for this DAO.
   */
  protected void configure(Class daoClass) throws NamingException
  {
    String facName = getDAOProperties().getProperty(daoClass.getName());
    System.out.println("facName is "+facName);
    setSessionFactoryName(facName);
  }
  
  /**
   * Obtain the DAO properties from system resource path: /META-INF/DAO.properties.
   * @return The properties obtained.
   */
  protected Properties getDAOProperties()
  {
    return SystemUtil.getResourceProperties(this.getClass(), "/META-INF/DAO.properties");
  }

  /**
   * Set the name of the session factory to use for this DAO. 
   * @param name The name of the session factory.
   * @throws NamingException Unable to obtain the session factory based on the specified name.
   */
  protected void setSessionFactoryName(String name) throws NamingException
  {
    _sessionFactory = HibernateUtil.getSessionFactory(name);
  }

}
