/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSRedeliveredHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 5, 2007   Tam Wei Xiang       Created
 * Jul 28,2008   Tam Wei Xiang       #69: Added method for checking whether
 *                                        we should request a redelivered from
 *                                        the JMS server given a particular exception.
 */
package com.gridnode.util.jms;

import java.util.Properties;

import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.exceptions.JMSFailureException;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSRedeliveredHandler
{
  private static String JMS_HANDLE_CAT = "jms.handle.mode";
  private static String JMS_REDELIVERED = "jms.redelivered";
  
  private static boolean isAllowedJMSRedelivered = false;
  
  static
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    Properties props = configStore.getProperties(JMS_HANDLE_CAT);
    isAllowedJMSRedelivered = new Boolean(props.getProperty(JMS_REDELIVERED, "false"));
    System.out.println("[GTVAN]JMSRedeliveredHandler: "+isAllowedJMSRedelivered);
  }
  
  /**
   * To indicate whether we turn on the JMS redelivered checking. During the fail over process,
   * JMS msg will be redelivered; thus, we added in the JMS redelivered checking on those JMS listener
   * that are not capable to process redelivered jms.
   */
  public static boolean isEnabledJMSRedelivered()
  {
    return isAllowedJMSRedelivered;
  }
  
  /**
   * #69 TWX: Indicate whether we should signal the MD Bean to rollback all the transaction so far, and request for a 
   * redelivered of the JMS msg
   * @param th The exception
   * @return true if the th contain the JMSFailureException. false otherwise.
   */
  public static boolean isRedeliverableException(Throwable th)
  {
    if(th == null)
    {
      return false;
    }
    if(th instanceof JMSFailureException)
    {
      return true;
    }
    else
    {
      Throwable nestTh = th.getCause();
      return isRedeliverableException(nestTh);
    }
  }
}
