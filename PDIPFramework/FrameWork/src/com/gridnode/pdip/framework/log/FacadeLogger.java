/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FacadeLogger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Neo Sok Lay         Created
 * Aug 07 2002    Neo Sok Lay         Add generic logError() method.
 * Aug 14 2002    Neo Sok Lay         Add generic logMessage() method.
 * Jan 04 2006    Neo Sok Lay         Change logging format
 * Feb 07 2007		Alain Ah Ming				Refactor printError to include error code
 */
package com.gridnode.pdip.framework.log;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;


import javax.ejb.*;
import java.util.Hashtable;

/**
 * This class is a utility helper class for use by the manager facades
 * (EJB Session Beans).
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I4
 */
public final class FacadeLogger
{
  private static final String APP_ERROR = "Application Error";
  private static final String SYS_ERROR = "Unexpected Error";
  private static final String ENTRY     = "Enter";
  private static final String EXIT      = "Exit";

  private static final Hashtable FACADE_LOGGER_REGISTRY = new Hashtable();

  /**
   * Obtain the FacadeLogger for a facade.
   *
   * @param facadeName The name of the facade.
   * @return the FacadeLogger for the facade.
   *
   * @since 2.0 I4
   */
  public static FacadeLogger getLogger(String facadeName, String category)
  {
    FacadeLogger logger = null;

    if (FACADE_LOGGER_REGISTRY.containsKey(facadeName))
      logger = (FacadeLogger)FACADE_LOGGER_REGISTRY.get(facadeName);
    else
    {
      logger = new FacadeLogger(facadeName, category);
      FACADE_LOGGER_REGISTRY.put(facadeName, logger);
    }

    return logger;
  }

  String _facadeName;
  String _category;

  private FacadeLogger(String facadeName, String category)
  {
    _facadeName = facadeName;
    _category   = category;
  }

  /**
   * Log the Entry to a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   */
  public void logEntry(String method, Object[] params)
  {
    logMessage(method, params, ENTRY);
  }

  /**
   * Log the Exit from a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   */
  public void logExit(String method, Object[] params)
  {
    logMessage(method, params, EXIT);
  }

  /**
   * Log a message from a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param message The message to log.
   */
  public void logMessage(String method, Object[] params, String message)
  {
    String prefix = getPrefix(method);
    printLog(prefix, message, params);
  }

  /**
   * Log the error that occurred in a method providing Update service in the
   * facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   * @throws FindEntityException If the error that occurred is an Application
   * exception.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logUpdateError(String method, Object[] params, Throwable t)
    throws UpdateEntityException, SystemException
  {
  	logWarn(method, params, t);
    throw new UpdateEntityException(t.getMessage());
  }

  /**
   * Log the error that occurred in a method providing Create service in the
   * facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   * @throws FindEntityException If the error that occurred is an Application
   * exception.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logCreateError(String method, Object[] params, Throwable t)
    throws CreateEntityException, SystemException
  {
  	logWarn(method, params, t);
    throw new CreateEntityException(t.getMessage());
  }

  /**
   * Log the error that occurred in a method providing Delete service in the
   * facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   */
  public void logDeleteError(String method, Object[] params, Throwable t)
    throws DeleteEntityException, SystemException
  {
  	logWarn(method, params, t);
    throw new DeleteEntityException(t.getMessage());
  }

  /**
   * Log the error that occurred in a method providing Finder service in the
   * facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   * @throws FindEntityException If the error that occurred is an Application
   * exception.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logFinderError(String method, Object[] params, Throwable t)
    throws FindEntityException, SystemException
  {
    logWarn(method, params, t);
    throw new FindEntityException(t.getMessage());
  }
  
  /**
   * Logs an warning, using the default application warning message.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logWarn(
    String method, Object[] params, Throwable t)
    throws SystemException
  {
    logWarn(method, params, APP_ERROR, t);
  }

  /**
   * @deprecated Use logError(String, String, Object[], String, Throwable)
   * Logs an error, using the default application error message.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logError(
    String method, Object[] params, Throwable t)
    throws SystemException
  {
    logError(method, params, APP_ERROR, t);
  }

  /**
   * @deprecated Use logError(String, String, Object[], String, Throwable)
   * Logs an error.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param appErrorMsg The message if it is an Application exception.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logError(
    String method, Object[] params, String appErrorMsg, Throwable t)
    throws SystemException
  {
    String prefix = getPrefix(method);

    if (isAppError(t))
    {
      printError(prefix, appErrorMsg, t);
    }
    else
    {
      printError(prefix, SYS_ERROR, t);
      throw new SystemException(getSysMessage(prefix, params), t);
    }
  }
  
  /**
   * Logs an warning.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param appErrorMsg The message if it is an Application exception.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logWarn(
    String method, Object[] params, String appErrorMsg, Throwable t)
    throws SystemException
  {
    String prefix = getPrefix(method);

    if (isAppError(t))
    {
      printWarn(prefix, appErrorMsg, t);
    }
    else
    {
      printWarn(prefix, SYS_ERROR, t);
      throw new SystemException(getSysMessage(prefix, params), t);
    }
  }

  /**
   * Logs an error.
   *
   * @param errorCode The error code
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param appErrorMsg The message if it is an Application exception.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logError(
    String errorCode, String method, Object[] params, String appErrorMsg, Throwable t)
    throws SystemException
  {
    String prefix = getPrefix(method);

    if (isAppError(t))
    {
      printError(errorCode, prefix, appErrorMsg, t);
    }
    else
    {
      printWarn(prefix, SYS_ERROR, t);
      throw new SystemException(getSysMessage(prefix, params), t);
    }
  }

  private boolean isAppError(Throwable t)
  {
    return (!(t instanceof EJBException) &&
            (t instanceof ApplicationException ||
             t.getClass().getPackage().getName().equals("javax.ejb")));
  }

  private String getPrefix(String method)
  {
    return new StringBuffer(_facadeName).append('.').append(method).toString();
  }

  private void printLog(String prefix, String type, Object[] params)
  {
    //StringBuffer msg = new StringBuffer(prefix).append("] ").append(type).insert(0, '[');
    StringBuffer msg = new StringBuffer("[").append(prefix);

    if (params != null)
    {
      msg.append('(');
      for (int i=0; i<params.length; i++)
      {
        msg.append(getFormattedParam(params[i]));
        if (i+1 <params.length)
          msg.append(',');
      }
      msg.append(')');
    }
    msg.append("] ").append(type);
    Log.log(_category, msg.toString());
  }

  private void printWarn(String prefix, String type, Throwable t)
  {
    //StringBuffer msg = new StringBuffer(prefix).append("] ").append(type).insert(0, '[');
    StringBuffer msg = new StringBuffer("[").append(prefix).append("] ").append(type);
    Log.warn(_category, msg.toString(), t);
  }

  /**
   * @deprecated
   * @param errorCode
   * @param prefix
   * @param type
   * @param t
   */
  private void printError(String prefix, String type, Throwable t)
  {
    //StringBuffer msg = new StringBuffer(prefix).append("] ").append(type).insert(0, '[');
    StringBuffer msg = new StringBuffer("[").append(prefix).append("] ").append(type);
    Log.err(_category, msg.toString(), t);
  }

  private void printError(String errorCode, String prefix, String type, Throwable t)
  {
    //StringBuffer msg = new StringBuffer(prefix).append("] ").append(type).insert(0, '[');
    StringBuffer msg = new StringBuffer("[").append(prefix).append("] ").append(type);
    Log.error(errorCode, _category, msg.toString(), t);
  }

  private String getSysMessage(String prefix, Object[] params)
  {
    StringBuffer msg = new StringBuffer(prefix).append('(');
    if (params != null)
    {
      for (int i=0; i<params.length; i++)
      {
        msg.append(getFormattedParam(params[i]));
        if (i+1 <params.length)
          msg.append(',');
      }
    }
    msg.append(") ").append(SYS_ERROR);

    return msg.toString();
  }

  private Object getFormattedParam(Object param)
  {
    if (param == null)
      return param;

    if (param instanceof IDataFilter)
      return (((IDataFilter)param).getFilterExpr());

    if (param instanceof IEntity)
      return (((IEntity)param).getEntityDescr());

    return param;
  }

/*
  public static void main(String[] args)
  {
    FacadeLogger logger = getLogger("MyFacadeBean");

    try
    {
      logger.logCreateError("myCreateMethod",
        new Object[] {null}, new CreateException("ejbCreateException"));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

    try
    {
      logger.logUpdateError("myUpdateMethod",
        new Object[] {}, new ApplicationException("someApplicationException"));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

    try
    {
      logger.logDeleteError("myDeleteMethod",
        null, new SystemException("someSystemException", new Exception("occurredException")));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

    try
    {
      logger.logFinderError("myFinderMethod",
        new Object[] {new Integer(34), "abcd"}, new Exception("someUnknownException"));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

  }
  */
}