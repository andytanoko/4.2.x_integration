/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNJMSExceptionHandler.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 02 2002    Goh Kan Mun                 Created
 */
package com.gridnode.pdip.base.transport.jms;

import com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.transport.helpers.TptLogger;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * This class is the generic ExceptionListener for the JMS connection
 * to inform the upper layer (Tpt, Channel and BL) of a connection fail.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GNJMSExceptionHandler implements ExceptionListener
{
  private String _connectionKey = null;

  /**
   * Constructor
   *
   * @param           connectionKey     the key to identify a persistence connection.
   */
  public GNJMSExceptionHandler(String connectionKey)
  {
    _connectionKey = connectionKey;
  }

  /**
   * This method is invoked by the JMS connection when there is an error occured.
   * It will then invoke the GNJMSController class to handle the connection error.
   *
   * @param           jmse    the <code>JMSException</code> thrown by the JMS layer.
   */
  public void onException(JMSException jmse)
  {
    try
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_JMS_EXCEPTION_HANDLE,
                         "GNSMQExceptionHandler", "onException", "Error encountered: "+jmse.getMessage(), jmse);
      GNJMSController.getInstance().handlePersistenceConnectionException(
        _connectionKey,
        jmse.getLocalizedMessage()
        );
    }
    catch (Throwable t)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_JMS_EXCEPTION_HANDLE,
                         "GNSMQExceptionHandler", "onException", "Unable to notify controller!!!", t);
    }
  }

}