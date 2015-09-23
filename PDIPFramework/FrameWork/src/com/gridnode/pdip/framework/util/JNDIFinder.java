/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JNDIFinder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2002    Ang Meng Hua        Ceated
 * May 02 2002    Neo Sok Lay         JNDIFinder(Properties) constructor is
 *                                    improperly defined with a void return type.
 * Jan 06 2003    Goh Kan Mun         Provide a close method to release resource after
 *                                    using it.
 * Nov 02 2005    Neo Sok Lay         Add lookup(jndiName) method                                   
 */
package com.gridnode.pdip.framework.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
* A simple wrapper around a JNDI context.
*
* @author Ang Meng Hua
* @since 2.0 build 2.0.1
*/

public class JNDIFinder
{
  private InitialContext _ctx = null;

  public JNDIFinder() throws
    javax.naming.NamingException
  {
    setInitialContext(null);
  }

  public JNDIFinder(String initContextFactory, String providerUrl) throws
    javax.naming.NamingException
  {
    setInitialContext(initContextFactory, providerUrl, null, null);
  }

  public JNDIFinder(Properties prop) throws
    javax.naming.NamingException
  {
    setInitialContext(prop);
  }

  public void setInitialContext(
    String initContextFactory,
    String providerUrl,
    String user,
    String password) throws
    javax.naming.NamingException
  {
    Properties prop = new Properties();
    prop.put(Context.INITIAL_CONTEXT_FACTORY, initContextFactory);
    prop.put(Context.PROVIDER_URL, providerUrl);
    if (user != null)
    {
      prop.put(Context.SECURITY_PRINCIPAL, user);
      if (password == null)
        password = "";
      prop.put(Context.SECURITY_CREDENTIALS, password);
    }

    setInitialContext(prop);
  }

  public void setInitialContext(Properties prop) throws
    javax.naming.NamingException
  {
    if (prop != null)
      _ctx = new InitialContext(prop);
    else
      _ctx = new InitialContext();
  }

  public Object lookup(String jndiName, Class objClass) throws
    javax.naming.NameNotFoundException,
    javax.naming.NamingException
  {
      Object obj = _ctx.lookup(jndiName);
      return PortableRemoteObject.narrow(obj, objClass);
  }

  /**
   * Lookup object using the specified jndi name, without ensuring
   * the type of the returned object
   * @param jndiName The jndi lookup name for the object
   * @return The object found using the jndi name
   * @throws NamingException If the object is not found using the specified jndi name
   */
  public Object lookup(String jndiName)
  	throws NamingException
  {
  	return _ctx.lookup(jndiName);
  }
  
  public void close() throws NamingException
  {
    _ctx.close();
    _ctx = null;
  }

}
