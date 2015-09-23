/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 * Sep 11 2002    Neo Sok Lay         Add FacadeLogger for Company profile.
 * Sep 17 2002    Neo Sok Lay         Add FacadeLogger for GridNode and
 *                                    ConnectionStatus.
 * Feb 15 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.gtas.server.gridnode.helpers;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the GridNode module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class Logger
{
  private static final String CATEGORY_MAIN = "GTAS.GRIDNODE";

  private static final String[] CATEGORY_FACADE = {
                                                    CATEGORY_MAIN + ".CATEGORY",
                                                    "GridNodeManagerBean",
                                                  };

  private static final String[] PROFILE_FACADE = {
                                                    CATEGORY_MAIN + ".PROFILE",
                                                    "GridNodeManagerBean",
                                                  };

  private static final String[] GRIDNODE_FACADE = {
                                                    CATEGORY_MAIN + ".GN",
                                                    "GridNodeManagerBean",
                                                  };

  private static final String[] CONNECTION_FACADE = {
                                                    CATEGORY_MAIN + ".CONN",
                                                    "GridNodeManagerBean",
                                                  };

  private static final String[] POSTMAN_FACADE = {
                                                    CATEGORY_MAIN + ".POST",
                                                    "GridMasterPostOfficeMDB",
                                                  };

  private static final int FACADE_INDEX = 1;
  private static final int CATEGORY_INDEX    = 0;

  /**
   * Get the FacadeLogger for the Category facade.
   */
  public static FacadeLogger getCategoryFacadeLogger()
  {
    return FacadeLogger.getLogger(
            CATEGORY_FACADE[FACADE_INDEX], CATEGORY_FACADE[CATEGORY_INDEX]);
  }


  /**
   * Get the FacadeLogger for the CompanyProfile facade.
   */
  public static FacadeLogger getProfileFacadeLogger()
  {
    return FacadeLogger.getLogger(
            PROFILE_FACADE[FACADE_INDEX], PROFILE_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the GridNode facade.
   */
  public static FacadeLogger getGridNodeFacadeLogger()
  {
    return FacadeLogger.getLogger(
            GRIDNODE_FACADE[FACADE_INDEX], GRIDNODE_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the Connection facade.
   */
  public static FacadeLogger getConnectionFacadeLogger()
  {
    return FacadeLogger.getLogger(
            CONNECTION_FACADE[FACADE_INDEX], CONNECTION_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the Postman facade.
   */
  public static FacadeLogger getPostmanFacadeLogger()
  {
    return FacadeLogger.getLogger(
            POSTMAN_FACADE[FACADE_INDEX], POSTMAN_FACADE[CATEGORY_INDEX]);
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