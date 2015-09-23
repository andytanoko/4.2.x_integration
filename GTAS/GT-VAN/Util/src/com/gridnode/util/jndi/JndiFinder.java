/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JndiFinder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 08, 2006   i00107              Created
 * Mar 05, 2007   Alain Ah Ming       Added error code
 * Apr 26 2007    i00107              Add constructor with LoggerManager.
 * Oct 30 2007    Tam Wei Xiang       Add method setJndiSuffix.
 */

package com.gridnode.util.jndi;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.gridnode.util.exceptions.ILogErrorCodes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;


/**
 * @author i00107
 * This class handles the JNDI lookups for J2EE objects on the J2EE application
 * server.
 * 
 * NOTE: If this class is frequently being reinitialised (eg being call in the GT user procedure), the caller should initialize
 * the LoggerManager with LoggerManager.getOneTimeInstance and set it into this class. This is to prevent the FileWatch dog issue
 * discovered in GTVAN project. 
 */
public class JndiFinder
{
  private InitialContext _ctx = null;
  private LoggerManager _logMgr;
  private String _jndiSuffix = "";
  
  public JndiFinder(Properties props) throws NamingException
  {
    init(props);
  }
  
  public JndiFinder(Properties props, LoggerManager logMgr) throws NamingException
  {
    this(props);
    _logMgr = logMgr;
  }
  
  private void init(Properties props) throws NamingException
  {
    if (props != null)
    {
      _ctx = new InitialContext(props);
    }
    else
    {
      _ctx = new InitialContext();
    }
  }
  
  
  /**
   * Lookup a resource of the particular type.
   * @param jndiName The lookup name of the resource
   * @param type The type of the resource. Note: If the resource found is not of the specified type, it is still returned as it is.
   * @return The resource, if lookup is sucessful, or <b>null</b> if lookup is unsuccessful.
   */
  public Object lookup(String jndiName, Class type) throws NamingException
  {
    Logger logger = getLogger();
    String methodName = "lookup";
    Object[] params = new Object[] {jndiName, type};
    
    Object obj = null;
    try
    {
      obj = _ctx.lookup(jndiName+getJndiSuffix());
      if (obj != null && type != null)
      {
        obj = PortableRemoteObject.narrow(obj, type);
      }
    }
    catch (ClassCastException ex)
    {
      logger.logError(ILogErrorCodes.JNDI_FINDER_LOOKUP,methodName, params,  "Unable to lookup object with specified type", ex);
    }
    catch (NamingException ex)
    {
      logger.logError(ILogErrorCodes.JNDI_FINDER_LOOKUP, methodName, params, "Unable to lookup object with specified jndiName", ex);
      throw ex;
    }

    return obj;
  }
  
  /**
   * Lookup a resource of the specified lookup name.
   * 
   * @param jndiName The lookup name of the resource
   * @return The resource, if lookup is sucessful, or <b>null</b> if lookup is unsuccessful.
   */
  public Object lookup(String jndiName) throws NamingException
  {
    Logger logger = getLogger();
    String methodName = "lookup";
    Object[] params = new Object[] {jndiName};
    
    Object obj = null;
    try
    {
      obj = _ctx.lookup(jndiName+getJndiSuffix());
    }
    catch (NamingException ex)
    {
      logger.logError(ILogErrorCodes.JNDI_FINDER_LOOKUP, methodName, params, "Unable to lookup object with specified jndiName", ex);
      throw ex;
    }
    return obj;
  }
  
  public String getJndiSuffix()
  {
    return _jndiSuffix;
  }
  
  /**
   * The client is allowed to set the suffix of the JNDI lookup name. The main purpose for allowing this  
   * is because some EJB bean is coded in the Util component. However, the Util component is shared among different
   * TXMR module (HTTPBC, GTVAN, Report, etc). Thus, while doing packaging, we specify different EJB-Name, JNDI Name in different
   * module, so while looking up, we use jndi suffix to help us to differentiate.  
   * @param suffix
   */
  public void setJndiSuffix(String suffix)
  {
    _jndiSuffix = suffix;
  }

  private synchronized Logger getLogger()
  {
    if (_logMgr == null)
    {
      return LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "JndiFinder");
    }
    else
    {
      return _logMgr.getLogger(LoggerManager.TYPE_UTIL, "JndiFinder");
    }
  }
}
