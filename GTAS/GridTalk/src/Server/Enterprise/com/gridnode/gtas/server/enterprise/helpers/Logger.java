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
 * Aug 01 2002    Neo Sok Lay         Created
 * Aug 13 2002    Neo Sok Lay         Add FacadeLoggers.
 * Sep 02 2003    Neo Sok Lay         Add FacadeLogger for SearchListenerMDBean.
 * Jan 07 2004    Neo Sok Lay         Add FacadeLogger for ConnectionListenerMDBean.
 * Feb 15 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the Enterprise module.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I4
 */
public final class Logger
{
  private static final String CATEGORY_MAIN = "GTAS.ENTERPRISE";

  private static final String[] RES_LINK_FACADE = {
                                                    CATEGORY_MAIN + ".RES.LINK",
                                                    "ResourceLinkManagerBean",
                                                  };

  private static final String[] SHARED_RES_FACADE = {
                                                    CATEGORY_MAIN + ".RES.SHARED",
                                                    "SharedResourceManagerBean",
                                                  };
  private static final String[] HIERARCHY_FACADE = {
                                                    CATEGORY_MAIN + ".HIERARCHY",
                                                    "EnterpriseHierarchyManagerBean",
                                                  };

  private static final String[] LISTENER_FACADE = {
                                                    CATEGORY_MAIN + ".LISTENER",
                                                    "ResourceChangeListenerMDBean",
                                                  };

  private static final String[] POSTMAN_FACADE = {
                                                    CATEGORY_MAIN + ".POST",
                                                    "GridMasterPostmanMDBean",
                                                  };

  private static final String[] POSTOFFICE_FACADE = {
                                                    CATEGORY_MAIN + ".POST",
                                                    "PostOfficeBean",
                                                  };

  private static final String[] ACT_LISTEN_FACADE = {
                                                    CATEGORY_MAIN + ".LISTENER",
                                                    "ActivationListenerMDBean",
                                                  };

  private static final String[] ENT_LISTEN_FACADE = {
                                                    CATEGORY_MAIN + ".LISTENER",
                                                    "EnterpriseCreatedListenerMDBean",
                                                  };

  private static final String[] SEARCH_LISTEN_FACADE = {
                                                    CATEGORY_MAIN + ".LISTENER",
                                                    "SearchListenerMDBean",
                                                  };


  private static final String[] CONN_LISTEN_FACADE = {
                                                    CATEGORY_MAIN + ".LISTENER",
                                                    "ConnectionListenerMDBean",
                                                  };


  private static final int FACADE_INDEX = 1;
  private static final int CATEGORY_INDEX    = 0;

  /**
   * Get the FacadeLogger for the ResourceLinkManagerBean.
   */
  public static FacadeLogger getResourceLinkFacadeLogger()
  {
    return FacadeLogger.getLogger(
            RES_LINK_FACADE[FACADE_INDEX], RES_LINK_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the SharedResourceManagerBean.
   */
  public static FacadeLogger getSharedResourceFacadeLogger()
  {
    return FacadeLogger.getLogger(
            SHARED_RES_FACADE[FACADE_INDEX], SHARED_RES_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the EnterpriseHierarchyManagerBean.
   */
  public static FacadeLogger getHierarchyFacadeLogger()
  {
    return FacadeLogger.getLogger(
            HIERARCHY_FACADE[FACADE_INDEX], HIERARCHY_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the ResourceChangeListenerMDBean.
   */
  public static FacadeLogger getListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            LISTENER_FACADE[FACADE_INDEX], LISTENER_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the Postman facade.
   */
  public static FacadeLogger getPostmanFacadeLogger()
  {
    return FacadeLogger.getLogger(
            POSTMAN_FACADE[FACADE_INDEX], POSTMAN_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the PostOffice facade.
   */
  public static FacadeLogger getPostOfficeFacadeLogger()
  {
    return FacadeLogger.getLogger(
            POSTOFFICE_FACADE[FACADE_INDEX], POSTOFFICE_FACADE[CATEGORY_INDEX]);
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
   * Get the FacadeLogger for the EnterpriseCreatedListenerMDBean.
   */
  public static FacadeLogger getEnterpriseCreatedListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            ENT_LISTEN_FACADE[FACADE_INDEX], ENT_LISTEN_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the SearchListenerMDBean.
   */
  public static FacadeLogger getSearchListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            SEARCH_LISTEN_FACADE[FACADE_INDEX], SEARCH_LISTEN_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the ConnectionListenerMDBean.
   */
  public static FacadeLogger getConnectionListenerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            CONN_LISTEN_FACADE[FACADE_INDEX], CONN_LISTEN_FACADE[CATEGORY_INDEX]);
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