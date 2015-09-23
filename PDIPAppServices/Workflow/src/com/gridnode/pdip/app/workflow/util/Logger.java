package com.gridnode.pdip.app.workflow.util;

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
 * Jun 10 2002   Mahesh	      Created
 * Feb 14 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */

import com.gridnode.pdip.framework.log.Log;


/**
 * This class handles the logging for the Workflow module.
 */
public class Logger
{
    public static final String category="APP.WORKFLOW";
    public static final String auditCategory="APP.WORKFLOW.AUDIT";
    public static final String activityCategory="APP.WORKFLOW.ACTIVITY";
    public static final String processCategory="APP.WORKFLOW.PROCESS";

    public static void log(Object msg)
    {
        Log.log(category, msg);
    }

    public static void debug(Object msg)
    {
        Log.debug(category, msg);
    }

    public static void debug(String msg, Throwable th)
    {
        Log.debug(category, msg, th);
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
      Log.warn(category, msg);
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
      Log.warn(category, msg, ex);
    }
    
    /**
     * @deprecated Use error(String, Object)
     * @see Logger#error(String, Object)
     * @param msg
     */
    public static void err(Object msg)
    {
        Log.err(category, msg);
    }

    /**
     * @deprecated Use error(String,String,Throwable)
     * @param msg
     * @param ex
     */
    public static void err(String msg, Throwable th)
    {
        Log.err(category, msg, th);
    }

    //--------------------------------------------------

    public static void log(String category,Object msg)
    {
        Log.log(category, msg);
    }

    public static void debug(String category,Object msg)
    {
        Log.debug(category, msg);
    }

    public static void debug(String category,String msg, Throwable th)
    {
        Log.debug(category, msg, th);
    }

    /**
     * Log a warning message with the specified category
     * @param category The category of the message
     * @param msg The warning message
     * 
     * @since GT 4.0 VAN
     * @version GT 4.0 VAN
     */
    public static void warn(String category, Object msg)
    {
      Log.warn(category, msg);
    }

    /**
     * Log a warning message with the specified category and
     * <code>Throwable</code> object of origin
     * 
     * @param category The category of the message
     * @param msg The warning message
     * @param ex The <code>Throwable</code> object of origin
     * 
     * @since GT 4.0 VAN
     * @version GT 4.0 VAN
     */
    public static void warn(String category, String msg, Throwable ex)
    {
      Log.warn(category, msg, ex);
    }
    
    /**
     * @deprecated Use error(String, String, Object)
     * @see Logger#error(String, String, Object)
     * @param msg
     */
    public static void err(String category,Object msg)
    {
        Log.err(category, msg);
    }

    /**
     * @deprecated Use error(String, String, String, Object)
     * @see Logger#error(String, String, String, Object)
     * @param msg
     */
    public static void err(String category,String msg, Throwable th)
    {
        Log.err(category, msg, th);
    }
    
    /**
     * Log an error message with the specified error code
     * @see com.gridnode.pdip.app.mapper.exception.ILogErrorCodes
     * 
     * @param errorCode
     * @param msg
     */
    public static void error(String errorCode, Object msg)
    {
      Log.error(errorCode, category, msg);
    }

    /**
     * Log an error message with the specified error code and
     * <code>Throwable</code> object of origin
     * @see com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes
     * 
     * @param errorCode The error code
     * @param msg The error message
     * @param t The <code>Throwable</code> object of origin
     */
    public static void error(String errorCode, String msg, Throwable t)
    {
      Log.error(errorCode, category, msg, t);
    }

    /**
     * Log an error message with the specified error code
     * @see com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes
     * 
     * @param errorCode
     * @param msg
     */
    public static void error(String errorCode, String category, Object msg)
    {
      Log.error(errorCode, category, msg);
    }

    /**
     * Log an error message with the specified error code and
     * <code>Throwable</code> object of origin
     * @see com.gridnode.pdip.app.mapper.exception.ILogErrorCodes
     * 
     * @param errorCode The error code
     * @param msg The error message
     * @param t The <code>Throwable</code> object of origin
     */
    public static void error(String errorCode, String category, String msg, Throwable t)
    {
      Log.error(errorCode, category, msg, t);
    }

}
