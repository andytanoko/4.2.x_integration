/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 * Feb 09 2007		Alain Ah Ming				Deprecate err(...)
 * 																		Add error(...) with error codes and warn(...)
 */
package com.gridnode.pdip.base.gwfbase.util;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the User module.
 * 
 * @author Mahesh
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public class Logger
{
    public static final String GWF_CATEGORY = "GWF";
    public static final String GWF_BASE = "GWF.BASE";
    public static final String GWF_WORKLIST = "GWF.WORKLIST";
    public static final String GWF_DEPLOYMENT = "GWF.DEPLOY";
    public static final String GWF_MGR = "GWF.MGR";
    public static final String GWF_JMS = "GWF.JMS";
    public static final String GWF_MDB = "GWF.JMS.MDB";
    public static final String GWF_FLOW = "GWF.FLOW";
    public static final String GWF_ENGINE = "GWF.ENG";
    public static final String GWF_ENGINE_ACTIVITY = "GWF.ENG.ACTIVITY";
    public static final String GWF_ENGINE_PROCESS = "GWF.ENG.PROCESS";
    public static final String GWF_ENGINE_RESTRICTION = "GWF.ENG.RESTRICTION";
    public static final String GWF_RUNTIME = "GWF.RUNTIME";

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     */
    public static void log(Object msg)
    {
        Log.log(GWF_CATEGORY, msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     */
    public static void debug(Object msg)
    {
        Log.debug(GWF_CATEGORY, msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     * @param ex DOCUMENT ME!
     */
    public static void debug(String msg, Throwable ex)
    {
        Log.debug(GWF_CATEGORY, msg, ex);
    }

    /**
     * @deprecated
     * @see #error(String, String, Object)
     *
     * @param msg DOCUMENT ME!
     */
    public static void err(Object msg)
    {
        Log.err(GWF_CATEGORY, msg);
    }

    /**
     * @deprecated
     * @see #error(String, String, String, Throwable)
     *
     * @param msg DOCUMENT ME!
     * @param ex DOCUMENT ME!
     */
    public static void err(String msg, Throwable ex)
    {
        Log.err(GWF_CATEGORY, msg, ex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public static void log(String category, Object msg)
    {
        Log.log(category, msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public static void debug(String category, Object msg)
    {
        Log.debug(category, msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     * @param msg DOCUMENT ME!
     * @param ex DOCUMENT ME!
     */
    public static void debug(String category, String msg, Throwable ex)
    {
        Log.debug(category, msg, ex);
    }

    /**
     * Log a warning message with the specified category
     *
     * @param category The warning log category
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
     * @param category The warning log category
     * @param msg The warning message
     * @param t The <code>Throwable</code> object of origin
     * 
     * @since GT 4.0 VAN
     * @version GT 4.0 VAN
     */
    public static void warn(String category, String msg, Throwable t)
    {
        Log.warn(category, msg, t);
    }

    /**
     * @deprecated
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public static void err(String category, Object msg)
    {
        Log.err(category, msg);
    }

    /**
     * @deprecated
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     * @param msg DOCUMENT ME!
     * @param ex DOCUMENT ME!
     */
    public static void err(String category, String msg, Throwable ex)
    {
        Log.err(category, msg, ex);
    }

    /**
     * Log an error message with the specified error code and category
     * 
     * @param errorCode The error code 
     * @param category The error log category
     * @param msg The error message
     * 
     * @since GT 4.0 VAN
     * @version GT 4.0 VAN
     */
    public static void error(String errorCode, String category, Object msg)
    {
        Log.error(errorCode, category, msg);
    }

    /**
     * Log an error message with the specified error code, category and
     * <code>Throwable</code> object of origin
     * @param errorCode The error code
     * @param category The error log category
     * @param msg The error message
     * @param t The <code>Throwable</code> object of origin
     * 
     * @since GT 4.0 VAN
     * @version GT 4.0 VAN
     */
    public static void error(String errorCode, String category, String msg, Throwable t)
    {
        Log.error(errorCode, category, msg, t);
    }
}
