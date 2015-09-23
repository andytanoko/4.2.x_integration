/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLocator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * MMM DD YYYY		????								Created
 * Feb 07 2007		Alain Ah Ming				Added MDC_KEY_ERROR_CODE for Log4J MDC key 
 * 																			to the error code.				
 */
package com.gridnode.pdip.framework.log;

/**
 * 
 * @author ????
 * @since 
 * @version GT 4.0 VAN
 */
public interface ILog {
	
  public static final String LOG_CATEGORY_CLIENT="pdip.log.remote";
  public static final String LOG_CATEGORY_SERVER="pdip.log.server";

  /**
   * The MDC key in the Log4J output that refers to the GN error code for an ERROR message.
   */
  public static final String MDC_KEY_ERROR_CODE	= "GnErrorCode";
  
  /**
   * Logging Category for Database module
   */
  public static final String DB   = "DB";  //Database

  /**
   * Logging Category for "Manager" Beans
   */
  public static final String MANAGER = "MGR";  //ManagerBean

  /**
   * Logging Category for "Entity" Beans
   */
  public static final String ENTITY  = "ENT";  //EntityBean

  /**
   * Logging Category for XML module
   */
  public static final String XML  = "XML"; //XML

  /**
   * Logging Category for Transport module
   */
  public static final String TPT  = "TPT"; //Transport

  /**
   * Logging Category for Communication module
   */
  public static final String COMM = "COM"; //Communication

  /**
   * Logging Category for Queue messaging module
   */
  public static final String QUEUE= "QUE"; //Queue

  /**
   * Logging Category for GUI module
   */
  public static final String GUI  = "GUI"; //GUI

  /**
   * Logging Category for WEB module
   */
  public static final String WEB = "WEB"; //WEB

  /**
   * Logging Category for Security module
   */
  public static final String SEC  = "SEC"; //Security

  /**
   * Logging Category for Framework module
   */
  public static final String FRAMEWORK  = "FRM"; //Framework

  /**
   * Logging Category for User module
   */
  public static final String USER  = "USER"; //User

  /**
   * Logging Category for Session module
   */
        public static final String SESSION  = "SESS"; //Session

  /**
   * Logging Category for Access control module
   */
  public static final String ACL = "ACL"; //AccessControl

  /**
   * Logging Category for GridForm module
   */
  public static final String GRIDFORM = "GF"; //GridForm

  /**
   * Logging Category for File service
   */
  public static final String FILE = "FILE"; //File service
  
  /**
   * Logging Category for Document Flow
   */
  public static final String NOTIFIER = "NTFR";
}

