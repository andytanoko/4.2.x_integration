/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransportHandlerFactory.java
 *
 ****************************************************************************
 * Date            Author                  Changes
 ****************************************************************************
 * DEC 10 2002     Jagadeesh               Created
 * Dec 11 2002     Kan Mun                 Modified - to throw exception if unrecognized
 *                                                    protocol type.
 *                                                  - remove getDefaultHandler.
 * Oct 17 2003     Neo Sok Lay             Determine handler by reflection.
 * Dec 17 2003     Jagadeesh               Modified - To throw InvalidProtocolException.
 */

package com.gridnode.pdip.base.transport.handler;

import java.util.Hashtable;

import com.gridnode.pdip.base.transport.exceptions.InvalidProtocolException;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
 
/**
 * This factory class manufactures TransportHandler(s) for specific protocol types.
 *
 * @author Jagadeesh
 * @version GT 2.3 I1
 */
public class TransportHandlerFactory
{
  private static final Hashtable _handlerInstanceTable = new Hashtable();
  private static final Configuration _config =
    ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

  private TransportHandlerFactory()
  {
  }

  /**
   * This method creates instance of Protocol Specific Handler based on
   * the "handler.XXX" properties configured in the Transport config file.
   *
   * @param protocolHandlerType - The protocol Handler type ex.(JMS,HTTP...)
   * This corresponds to the XXX portion of a 'handler.XXX' property.
   * @return - instance of Concrete implementation of Transport Handler.
   */
  public static ITransportHandler getTransportHandler(String protocolHandlerType)
    throws InvalidProtocolException
  {
    TptLogger.infoLog(
      "TransportHandlerFactory",
      "getTransportHandler",
      "Protocol Type: " + protocolHandlerType);
    if (protocolHandlerType == null)
      throw new IllegalArgumentException(
        "[Please Provide Valid ProtocolHandler "
          + "type to initilize Tpt Services]");

    ITransportHandler handler = null;

    try
    {
      //Get Handler implementation class
      handler = getHandlerInstance(protocolHandlerType);
    }
    catch (Exception e)
    {
      TptLogger.warnLog("TransportHandlerFactory", "getTransportHandler",
        "Error instantiating TransportHandler instance", e);
    }

    if (handler == null)
      throw new InvalidProtocolException(
        "Invalid protocol type:" + protocolHandlerType);

    return handler;

    /*031017NSL
    if (ICommInfo.JMS.equals(protocolHandlerType))
    {
      TptLogger.infoLog(
        "TransportHandlerFactory",
        "getTransportHandler",
        "Return instance of JMSTransportHandler");
      return new JMSTransportHandler(); //JMS Transport Handler.
    }
    else if (ICommInfo.HTTP.equals(protocolHandlerType))
    {
      TptLogger.infoLog(
        "TransportHandlerFactory",
        "getTransportHandler",
        "Return instance of HttpTransportHandler");
      return new HTTPTransportHandler(); //HTTP Transport Handler.
    }
    else if (ICommInfo.SOAP.equals(protocolHandlerType))
    {
      TptLogger.infoLog(
        "TransportHandlerFactory",
        "getTransportHandler()",
        "[Return instance of SoapHttpTransportHandler]");
      return new SoapTransportHandler(); //Soap Transport Handler.
    }
    else
      throw new GNTptInvalidProtocolException(
        "Invalid protocol type:" + protocolHandlerType);
    */
  }

  /**
   * Get a TransportHandler instance of the specified protocol type.
   *
   * @param protocolType The Protocol type.
   * @return An TransportHandler instance of the specified protocol type.
   */
  private static ITransportHandler getHandlerInstance(
    String protocolType)
    throws Exception
  {
    // Use the cached object instance if one has already been instantiated
    ITransportHandler handlerInstance = (ITransportHandler)_handlerInstanceTable.get(protocolType);
    if (handlerInstance == null)
    {
      String handlerName = getHandlerImplementationName(protocolType);
      if (handlerName != null)
      {
        Class classInstance = getClassFor(handlerName);
        handlerInstance = (ITransportHandler)classInstance.newInstance();
        // cache the object instance for re-use
        _handlerInstanceTable.put(protocolType, handlerInstance);
      }
    }
    return handlerInstance;
  }

  /**
   * Get the Class instance using the specified class name
   *
   * @param className Full qualified name of the class
   * @return Class instance obtained.
   * @throws ClassNotFoundException Invalid class name specified.
   */
  private static Class getClassFor(String className) throws ClassNotFoundException
  {
    return Class.forName(className);
  }

  /**
   * Get the implementation class name of the transport handler for the specified
   * protocol type.
   *
   * @param protocolType The transport protocol type.
   * @return The implementation class name configured to handle the specified
   * protocolType. Returns <b>null</b> if none has been configured.
   */
  private static String getHandlerImplementationName(String protocolType)
  {
    return _config.getString(ITransportConfig.HANDLER_PREFIX+protocolType, null);
  }
}