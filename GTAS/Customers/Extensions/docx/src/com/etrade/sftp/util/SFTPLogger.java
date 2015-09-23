/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 20, 2010   <Developer Name>       Created
 */
package com.etrade.sftp.util;

import com.gridnode.pdip.framework.log.Log;

/**
 * Name it as SFTPLogger to indicate that this logger is used by SFTP component
 * @author Eric Loh
 * @version
 * @since
 */
public class SFTPLogger
{
  private static final String CATEGORY_MAIN = "GTAS.SFTP";
  private static final String ERRORCODE_MAIN = "GTAS.SFTP.ERROR";
  
  /**
   * @param msg
   */
  public static void log(String message)
  {
    info(message);//log() is actually calling info()
  }

  /**
   * @param msg
   */
  public static void info(String message)
  {
    Log.log(CATEGORY_MAIN, message); //log() is actually calling info()
  }
  
  /**
   * @param msg
   */
  public static void debug(String message)
  {
    Log.debug(CATEGORY_MAIN, message);
  }
  
  /**
   * @param msg
   */
  public static void warn(String message)
  {
    Log.debug(CATEGORY_MAIN, message);
  }
  
  /**
   * Convenience method to use a main errorCode
   * @param message
   */
  public static void error(String message)
  {
    error(ERRORCODE_MAIN, message);
  }

  /**
   * Convenience method to use a main errorCode
   * @param message
   */
  public static void error(Throwable t)
  {
    error(ERRORCODE_MAIN, "", t);
  }

  /**
   * Convenience method to use a main errorCode
   * @param message
   */
  public static void error(String message, Throwable t)
  {
    error(ERRORCODE_MAIN, message, t);
  }

  /**
   * @param errorCode
   * @param message
   */
  public static void error(String errorCode, String message)
  {
    Log.error(errorCode, CATEGORY_MAIN, message);
  }

  /**
   * @param errorCode
   * @param message
   * @param t
   */
  public static void error(String errorCode, String message, Throwable t)
  {
    Log.error(errorCode, CATEGORY_MAIN, message, t);
  }

  /**
   * @param args
   */
  public static void main(String args[])
  {
    System.out.println(SFTPLogger.class.getSimpleName() + " started");
    
    debug("Test debugging");
    
    System.out.println(SFTPLogger.class.getSimpleName() + " done");
  }
}
