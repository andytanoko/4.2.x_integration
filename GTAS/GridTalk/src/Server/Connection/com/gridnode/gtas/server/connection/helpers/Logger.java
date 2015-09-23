/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 * Nov 07 2002    Neo Sok Lay         Add getActivationListenerFacadeLogger().
 *                                    Add getKeepAliveTimerFacadeLogger().
 * Feb 06 2004    Neo Sok Lay         Add getStartupListenerFacadeLogger().
 * Feb 15 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.gtas.server.connection.helpers;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the Connection module.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I6
 */
public class Logger
{
  private static final String CATEGORY_MAIN = "GTAS.CONNECTION";

  private static final String[] CON_FACADE = {
                                               CATEGORY_MAIN + ".CON",
                                               "ConnectionServiceBean",
                                             };

  private static final String[] ACT_LISTEN_FACADE = {
                                               CATEGORY_MAIN + ".ACT",
                                               "ActivationListenerMDBean",
                                             };

  private static final String[] TIMER_FACADE = {
                                               CATEGORY_MAIN + ".TIMER",
                                               "KeepAliveTimerMDBean",
                                             };

  private static final String[] STARTUP_FACADE = {
                                               CATEGORY_MAIN + ".STARTUP",
                                               "StartupListenerMDBean",
                                             };

  private static final int CATEGORY_INDEX    = 0;
  private static final int FACADE_INDEX = 1;

  /**
   * Get the FacadeLogger for the Connection facade.
   */
  public static FacadeLogger getConnectionFacadeLogger()
  {
    return FacadeLogger.getLogger(
            CON_FACADE[FACADE_INDEX], CON_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the ActivationListenerMDBean.
   */
  public static FacadeLogger getActivationListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            ACT_LISTEN_FACADE[FACADE_INDEX], ACT_LISTEN_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the KeepAliveTimerMDBean.
   */
  public static FacadeLogger getTimerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            TIMER_FACADE[FACADE_INDEX], TIMER_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the StartupListenerMDBean.
   */
  public static FacadeLogger getStartupListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            STARTUP_FACADE[FACADE_INDEX], STARTUP_FACADE[CATEGORY_INDEX]);
  }

  public static void log(Object msg)
  {
    Log.log(CATEGORY_MAIN, msg);
  }

  public static void debug(Object msg)
  {
    Log.debug(CATEGORY_MAIN, msg);
  }

  public static void debug(String msg, Throwable ex)
  {
    Log.debug(CATEGORY_MAIN, msg, ex);
  }

  /**
   * Log a warning message with the specified category
   * @param msg The warning message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(Object msg)
  {
    Log.warn(CATEGORY_MAIN, msg);
  }

  /**
   * Log a warning message with the specified category and
   * <code>Throwable</code> object of origin
   * 
   * @param msg The warning message
   * @param ex The <code>Throwable</code> object of origin
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(String msg, Throwable ex)
  {
    Log.warn(CATEGORY_MAIN, msg, ex);
  }

  /**
   * @deprecated Use error(String, Object)
   * @see Logger#error(String, Object)
   * @param msg
   */
  public static void err(Object msg)
  {
    Log.err(CATEGORY_MAIN, msg);
  }

  /**
   * @deprecated Use error(String,String,Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    Log.err(CATEGORY_MAIN, msg, ex);
  }
  
  /**
   * Log an error message with the specified error code
   * @see com.gridnode.gtas.exceptions.ILogErrorCodes
   * 
   * @param errorCode
   * @param msg
   */
  public static void error(String errorCode, Object msg)
  {
    Log.error(errorCode, CATEGORY_MAIN, msg);
  }

  /**
   * Log an error message with the specified error code and
   * <code>Throwable</code> object of origin
   * @see com.gridnode.gtas.exceptions.ILogErrorCodes
   * 
   * @param errorCode The error code
   * @param msg The error message
   * @param t The <code>Throwable</code> object of origin
   */
  public static void error(String errorCode, String msg, Throwable t)
  {
    Log.error(errorCode, CATEGORY_MAIN, msg, t);
  }
}