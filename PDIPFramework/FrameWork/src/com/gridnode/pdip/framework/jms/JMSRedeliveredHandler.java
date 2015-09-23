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
 * Jul 25 2008   Tam Wei Xiang       #69: Added method to check whether we should rollback
 *                                   the entire transaction.
 */
package com.gridnode.pdip.framework.jms;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.JMSFailureException;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class JMSRedeliveredHandler
{
  private static boolean _isEnabledJMSRedelivered = false;
  private static final String FRAMEWORK_JMS_CONFIG = "jms";
  private static final String IS_ENABLED_JMS_REDELIVERED = "jms.is.enabled.redelivered";
  static
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(FRAMEWORK_JMS_CONFIG);
    _isEnabledJMSRedelivered = config.getBoolean(IS_ENABLED_JMS_REDELIVERED, false);
  }
  
  /**
   * To indicate whether we turn on the JMS redelivered checking. During the fail over process,
   * JMS msg will be redelivered; thus, we added in the JMS redelivered checking on those JMS listener
   * that are not capable to process redelivered jms.
   */
  public static boolean isEnabledJMSRedelivered()
  {
    return _isEnabledJMSRedelivered;
  }
  
  /**
   * #69: Indicate whether we should signal the MD Bean to rollback all the transaction so far, and request for a 
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
