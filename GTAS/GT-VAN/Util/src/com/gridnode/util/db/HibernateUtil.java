/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HibernateUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 */

package com.gridnode.util.db;

import javax.naming.NamingException;

import org.hibernate.SessionFactory;

import com.gridnode.util.jndi.JndiFinder;

/**
 * @author i00107
 * This class handles the need for access to Hibernate SessionFactory
 */
public class HibernateUtil
{
//  public static SessionFactory getSessionFactory() throws NamingException
//  {
//    return getSessionFactory("HibernateService");
//  }
  
  public static SessionFactory getSessionFactory(String name) throws NamingException
  {
    //TODO to be reviewed 
    JndiFinder finder = new JndiFinder(null);
    return (SessionFactory)finder.lookup(name, SessionFactory.class);
  }

}
