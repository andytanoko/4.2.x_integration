/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 17 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.transport.comminfo;

import com.gridnode.pdip.base.transport.exceptions.GNTptInvalidProtocolException;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import java.util.Hashtable;

/**
 * This factory takes care of returning the correct Communication info
 * implementation for each protocol type.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class CommInfoFactory
{
  private static final Hashtable _classInstanceTable = new Hashtable();
  private static final Configuration _config = 
    ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

  /**
   * Get a Transport CommInfo implementation instance for the specified
   * protocol type.
   *  
   * @param protocolType The transport protocol type.
   * @return A CommInfo implementation for the specified protocol type.
   * @throws GNTptInvalidProtocolException Unsupported protocol type i.e.
   * no implementation available for the specified protocol type.
   */
  public static ICommInfo getTransportCommInfo(String protocolType)
    throws GNTptInvalidProtocolException
  {
    TptLogger.infoLog(
      "CommInfoFactory",
      "getTransportCommInfo",
      "Protocol Type: " + protocolType);
    if (protocolType == null)
      throw new IllegalArgumentException(
        "[Please Provide Valid Protocol Type to initilize Tpt Services]");
    
    ICommInfo commInfo = null;
    
    try
    {
      //Get CommInfo implementation instance
      commInfo = getCommInfoInstance(protocolType);
    }
    catch (Exception e)
    {
      TptLogger.warnLog("CommInfoFactory", "getTransportCommInfo",
        "Error instantiating CommInfo instance", e);
    }
    
    if (commInfo == null)
      throw new GNTptInvalidProtocolException(
        "Invalid protocol type:" + protocolType);
    
    return commInfo;
    
  }
  
  /**
   * Get a CommInfo instance for the specified protocol type.
   * 
   * @param protocolType The Protocol type.
   * @return A CommInfo instance for the specified protocol type.
   */
  private static ICommInfo getCommInfoInstance(
    String protocolType) 
    throws Exception
  {
    // Use the cached object instance if one has already been instantiated
    Class classInstance = (Class)_classInstanceTable.get(protocolType);
    if (classInstance == null)
    {
      String className = getCommInfoImplementationName(protocolType);
      if (className != null)
      {
        classInstance = getClassFor(className);
        _classInstanceTable.put(protocolType, classInstance);
      }
    }
    
    return (ICommInfo)classInstance.newInstance();
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
   * Get the implementation class name of the transport comminfo for the specified
   * protocol type.
   * 
   * @param protocolType The transport protocol type.
   * @return The comminfo implementation class name configured to for the specified
   * protocolType. Returns <b>null</b> if none has been configured.
   */
  private static String getCommInfoImplementationName(String protocolType)
  {
    return _config.getString(ITransportConfig.COMMINFO_PREFIX+protocolType, null); 
  }  
}
