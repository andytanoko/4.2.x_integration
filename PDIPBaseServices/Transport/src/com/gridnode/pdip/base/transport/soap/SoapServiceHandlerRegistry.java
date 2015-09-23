/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapServiceHandlerRegistry.java
 *
 ****************************************************************************
 * Date            Author                  Changes
 ****************************************************************************
 * SEP 23 2003     Jagadeesh               Created
 */

package com.gridnode.pdip.base.transport.soap;

import java.lang.reflect.Constructor;
import java.util.*;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.GNTransportException;
import com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class SoapServiceHandlerRegistry
{
	private static Map _soapServicesRegistry = new HashMap();
	private static final String URL_SEPERATOR = "/";
	private static final String CLASS_NAME = "SoapServiceHandlerRegistry";

	static
	{
	  initSoapServiceRegistry();
	}

	private static void initSoapServiceRegistry()
	{
		Configuration soapServicesConfig = ConfigurationManager.getInstance().getConfig(
			ITransportConfig.SOAP_SERVICES_CONFIG_NAME);
		_soapServicesRegistry.putAll(soapServicesConfig.getProperties());
	}

	public static ISoapServiceHandler getServiceHandler(ICommInfo commInfo)
		throws GNTransportException,ApplicationException
	{
		//ISoapServiceHandler soapServiceHandler = null;
		try
		{
			String serviceName = getServiceNameFromEndPoint(commInfo.getURL());
			String serviceHandlerImpl = null;
			if (serviceName == null)
				throw new ApplicationException("[Invalid Soap Service URL]"+commInfo.getURL());
		  else
				serviceHandlerImpl = (String)_soapServicesRegistry.get(serviceHandlerImpl);
		  return getServiceHandlerFromImpl(serviceHandlerImpl,commInfo);
		}
		catch (Exception ex)
		{
		  TptLogger.warnLog(getClassName(),"[getServiceHandler()]",ex.getLocalizedMessage());
		  throw new GNTransportException("["+getClassName()+"][getServiceHandler()]"+
				"[Could Not Create SoapServiceHandler]"+"\n"+ex.getLocalizedMessage(),ex);
		}
	}

	public static Iterator getRegisteredServiceHandlers()
		throws ApplicationException
	{
		List classInstance = new ArrayList();
		if (!_soapServicesRegistry.isEmpty())
		{
			Iterator handlerClass = _soapServicesRegistry.values().iterator();
			while (handlerClass.hasNext())
			{
				String className = (String)handlerClass.next();
				try
				{
					ISoapServiceHandler handler = createInstance(className,new Object[]{});
					classInstance.add(handler);
				}
				catch (ApplicationException ex)
				{
				  TptLogger.debugLog(getClassName(),"getRegisteredServiceHandlers()",
						"[Could Not Initilize Handler '"+className+"']");
				}
			}
		}
		else
		{
			TptLogger.infoLog(getClassName(),"getRegisteredServiceHandlers()",
				"[There are no Registered Listeners at this time...]");
		  return Collections.EMPTY_LIST.iterator();
		}
		return classInstance.iterator();
	}



	private static String getServiceNameFromEndPoint(String url)
	{
	  return url.substring(url.lastIndexOf(URL_SEPERATOR));
	}

	private static ISoapServiceHandler getServiceHandlerFromImpl(String implClass,ICommInfo commInfo)
		throws ApplicationException
	{
		try
		{
			if (implClass != null)
			{
				return  createInstance(implClass,new Object[]{commInfo});
			}
			else
				throw new ApplicationException("["+getClassName()+"][getServiceHandlerFromImpl()]"+
				"[Could Not Create SoapServiceHandler-Check Service Definiton Class in service.properties]");
		}
		catch (Exception ex)
		{
		  throw new ApplicationException(ex.getLocalizedMessage(),ex);
		}
	}

	private static ISoapServiceHandler createInstance(String implClass,Object[] params)
		throws ApplicationException
	{
    try
    {
      Constructor serviceImplConstructor = null;
			Class instClass = Class.forName(implClass.trim());
		  Class[] classParams = new Class[params.length];
			for (int i = 0; i < params.length; i++)
			{
				classParams[i] = params[i].getClass();
			}
			serviceImplConstructor =  instClass.getConstructor(classParams);
			Object obj = serviceImplConstructor.newInstance(params);
      if (!ISoapServiceHandler.class.isAssignableFrom(instClass))
          throw new ApplicationException("Class '"+implClass +
						  "' not of requiredType "+ISoapServiceHandler.class.getName());
      return (ISoapServiceHandler)obj;
    }
    catch(ClassNotFoundException clex) {
      throw new ApplicationException("Could not Load the Class '"+implClass+"'",clex);
    }
    catch(IllegalAccessException ilex) {
      throw new ApplicationException("Could not construct the Class '"+implClass+"'",ilex);
    }
    catch(InstantiationException iex) {
      throw new ApplicationException("Could not construct the Class '"+implClass+"'",iex);
    }
		catch(Exception ex) {
      throw new ApplicationException("Could not instantiate the Class '"+implClass+"'",ex);
		}
	}

	private static String getClassName()
	{
	  return CLASS_NAME;
	}

	private SoapServiceHandlerRegistry()
  {
  }

}