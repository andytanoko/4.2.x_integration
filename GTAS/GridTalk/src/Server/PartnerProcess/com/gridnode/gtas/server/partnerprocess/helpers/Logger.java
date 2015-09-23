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
 * Oct 08 2002    Koh Han Sing        Created
 * Nov 21 2002    Neo Sok Lay         Add FacadeLogger for ProcessMapping.
 * Jan 10 2003    Neo Sok Lay         Add FacadeLogger for BizCertMapping.
 * May 08 2006    Neo Sok Lay         Add FacadeLogger for EntityChangeListener
 * Feb 15 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the PartnerProcess module.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class Logger
{
  private static final String CATEGORY_MAIN = "GTAS.PARTNERPROCESS";

  private static final String[] PROCESS_MAPPING_FACADE = {
                                                    CATEGORY_MAIN + ".PROCESS.MAPPING",
                                                    "PartnerProcessManagerBean",
                                                  };

  private static final String[] CERT_MAPPING_FACADE = {
                                                    CATEGORY_MAIN + ".CERT.MAPPING",
                                                    "PartnerProcessManagerBean",
                                                  };

  private static final String[] ENTITY_CHANGE_LISTENER = {
    CATEGORY_MAIN + ".ENTITY.CHANGE",
    "EntityChangeListenerMDBean",
  };


  private static final int FACADE_INDEX = 1;
  private static final int CATEGORY_INDEX    = 0;

  /**
   * Get the FacadeLogger for the Process Mappper.
   */
  public static FacadeLogger getProcessMappingFacadeLogger()
  {
    return FacadeLogger.getLogger(
            PROCESS_MAPPING_FACADE[FACADE_INDEX], PROCESS_MAPPING_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the BizCert Mappper.
   */
  public static FacadeLogger getBizCertMappingFacadeLogger()
  {
    return FacadeLogger.getLogger(
            CERT_MAPPING_FACADE[FACADE_INDEX], CERT_MAPPING_FACADE[CATEGORY_INDEX]);
  }

  /**
   * Get the FacadeLogger for the BizCert Mappper.
   */
  public static FacadeLogger getEntityChangeFacadeLogger()
  {
    return FacadeLogger.getLogger(
            ENTITY_CHANGE_LISTENER[FACADE_INDEX], ENTITY_CHANGE_LISTENER[CATEGORY_INDEX]);
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