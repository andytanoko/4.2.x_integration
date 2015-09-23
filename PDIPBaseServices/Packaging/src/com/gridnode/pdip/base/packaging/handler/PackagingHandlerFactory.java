/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingHandlerFactory
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 * Jan 17 2002    Goh Kan Mun         Modified - Added Backward Compatible Handler.
 * 25-FEB-2003    Jagadeesh           Modified: To read Packaging Properties to
 *                                    invoke the PackagingHandler.
 *
 * 28-FEB-2003    Jagadeesh           Modified: To check for Handler Not Null.
 *                                    (To be more defensive.)
 * Mar 21 2003    Kan Mun             Modified - Change Packaging Handler name from
 *                                               BackwardCompatible to FileSplit.
 */


package com.gridnode.pdip.base.packaging.handler;
import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.IPackagingInfo;
import com.gridnode.pdip.base.packaging.helper.IPackagingConfig;
import com.gridnode.pdip.base.packaging.helper.PackagingLogger;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;


public class PackagingHandlerFactory
{

  private static final String CLASS_NAME = "PackagingHandlerFactory";
  private static Configuration _config = null;
  private static String _defaultPackagingHandler = null;
  private static String _rnifPackagingHandler=null;
  private static String _as2PackagingHandler = null;

  private PackagingHandlerFactory()
  {
  }

  public static IPackagingHandler getPackagingHandler(String handler)   throws Exception
  {
    final String METHOD_NAME = "getPackagingHandler()";
    PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "argument handler=="+ handler);
    if(handler != null)
    {
      String className =   getClassNameByPackagingType(handler);
      return  (IPackagingHandler)getPackagingHandlerImpl(className,IPackagingHandler.class);
    }
    else
      throw new PackagingException("Please Check to Set EnvelopeType."+
              "EnvelopeType NULL is not a Valid EnvelopeType");
  }

  private static Object getPackagingHandlerImpl(String className,Class reqType)
    throws PackagingException
  {
    try
    {
      Class instClass = Class.forName(className.trim());
      Object obj = instClass.newInstance();
      if(!reqType.isAssignableFrom(instClass))
          throw new PackagingException("Class '"+className +
                                                  "' not of requiredType "+reqType);
      return obj;

    }
    catch(ClassNotFoundException clex) {
      throw new PackagingException("Could not Load the Class '"+className+"'",clex);
    }
    catch(IllegalAccessException ilex) {
      throw new PackagingException("Could not construct the Class '"+className+"'",ilex);
    }
    catch(InstantiationException iex) {
      throw new PackagingException("Could not construct the Class '"+className+"'",iex);
    }

  }

  private static String getClassNameByPackagingType(String envelopeType)
      throws Exception
  {
    final String METHOD_NAME = "getClassNameByPackagingType()";
    if(IPackagingInfo.DEFAULT_ENVELOPE_TYPE.equals(envelopeType))
    {
      if(_defaultPackagingHandler == null)
        _defaultPackagingHandler = getDefaultPackagingHandler();
      return _defaultPackagingHandler;
    }
    else if (IPackagingInfo.FILE_SPLIT_ENVELOPE_TYPE.equals(envelopeType))
    {
      return getFileSplitPackagingHandler();
    }

    if(IPackagingInfo.RNIF1_ENVELOPE_TYPE.equals(envelopeType) ||
          IPackagingInfo.RNIF2_ENVELOPE_TYPE.equals(envelopeType))
    {
      PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME,
      "To invoke RNIF2 Packaging Handler At GTAS Layer");
      if(_rnifPackagingHandler == null)
        _rnifPackagingHandler = getRNIFPackagingHandler();
      return _rnifPackagingHandler;
    }

    if (IPackagingInfo.AS2_ENVELOPE_TYPE.equals(envelopeType))
    {
      PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME,
        "To invoke AS2 Packaging Handler At GTAS Layer");
      if (_as2PackagingHandler == null)
        _as2PackagingHandler = getAS2PackagingHandler();
      return _as2PackagingHandler;
    }

    if(IPackagingConfig.SOAP_PACKAGING_HANDLER.equals(envelopeType))
    {
      PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME,
          "To invoke SOAP Packaging Handler At GTAS Layer");
      return getSoapPackagingHandler();
    }
    return null;
  }


  private static String getDefaultPackagingHandler()
  {
    return getPackagingConfiguration().getString(
        IPackagingConfig.DEFAULT_PACKAGING_HANDLER);
  }

  private static String getFileSplitPackagingHandler()
  {
    return getPackagingConfiguration().getString(
        IPackagingConfig.FILE_SPLIT_PACKAGING_HANDLER);
  }

  private static String getRNIFPackagingHandler() throws  Exception
  {
    return getPackagingConfiguration().getString(
        IPackagingConfig.RN_PACKAGING_HANDLER);
  }

  private static String getAS2PackagingHandler() throws  Exception
  {
    return getPackagingConfiguration().getString(
        IPackagingConfig.AS2_PACKAGING_HANDLER);
  }

  private static String getSoapPackagingHandler() throws Exception
  {
    return getPackagingConfiguration().getString(IPackagingConfig.SOAP_PACKAGING_HANDLER);
  }

  private static Configuration getPackagingConfiguration()
  {
    if(_config == null)
    _config = ConfigurationManager.getInstance().getConfig(
                                                   IPackagingConfig.CONFIG_NAME);
    return _config;
  }



/*  public static IPackagingHandler getPackagingHandler(String handler)   throws Exception
  {
    PackagingLogger.debugLog("PackagingHandlerFactory", "getPackagingHandler", "argument handler=="+ handler);
    if(handler.equals(IPackagingInfo.DEFAULT_ENVELOPE_TYPE))
    {
      return new DefaultPackagingHandler(); //Default Packaging Handler.
    }
    else if (handler.equals(IPackagingInfo.BACKWARD_COMPATIBLE_ENVELOPE_TYPE))
    {
      return new BackwardCompatiblePackagingHandler();
    }
    if(handler.equals(IPackagingInfo.RNIF1_ENVELOPE_TYPE) ||
                     handler.equals(IPackagingInfo.RNIF2_ENVELOPE_TYPE))
    {
      PackagingLogger.debugLog("PackagingHandlerFactory", "getPackagingHandler",
      "To invoke RNIF2 Packaging Handler At GTAS Layer");
      if(_gtasPackagingHandler == null)
         _gtasPackagingHandler = getRNIFPackagingHandler();
      return  (IPackagingHandler)_gtasPackagingHandler.newInstance();
    }
    return null;
  }

  private static Class getRNIFPackagingHandler() throws  Exception
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(
        IPackagingConfig.CONFIG_NAME);
    String className = config.getString(IPackagingConfig.GTAS_RN_PACKAGING_HANDLER);
    PackagingLogger.debugLog("PackagingHandlerFactory","getRNIFPackagingHandler()",
    "Class Name "+className);
    if(className != null)
      return Class.forName(className);
    else
      throw new PackagingServiceException("Cannot Get GTAS RNIF Packaging Handler from Config");
  }
 */


}