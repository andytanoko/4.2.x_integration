/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AppInterfaceHelper.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 23, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.base.appinterface.helpers;

import java.util.Vector;

import com.gridnode.pdip.base.appinterface.data.AppDefinitionDoc;
import com.gridnode.pdip.base.appinterface.data.IAppConstants;
import com.gridnode.pdip.base.appinterface.exception.AppNotInitializedException;
import com.gridnode.pdip.base.appinterface.interfaces.IExecutable;
import com.gridnode.pdip.base.appinterface.interfaces.IJavaProcedure;
import com.gridnode.pdip.base.appinterface.interfaces.adaptor.GenericJavaAdaptor;

public class AppInterfaceHelper implements IAppConstants
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3431753904775953129L;
	private static long concurrentApplicationCount;
  private static long applicationCounter;

  public static IExecutable createApp(AppDefinitionDoc appDefDoc, int appType) throws AppNotInitializedException
  {
    try
    {
      switch (appType)
      {
        case JAVA_PROC :
          IExecutable executable =createJavaProcedure(
                        (String) appDefDoc.getParam(IJavaProcedure.PARAM_CLASSNAME),
                        (String) appDefDoc.getParam(IJavaProcedure.PARAM_METHODNAME),
                        (Vector) appDefDoc.getParam(IJavaProcedure.PARAM_PARAMTYPES));
          return executable;
        default:
            throw new Exception("AppType is NotSupported, appType="+appType);
      }
    }
    catch(Throwable th)
    {
      throw new AppNotInitializedException("Error while initializing the application, appDefinitionDoc="+appDefDoc,th);
    }
  }

  protected static IJavaProcedure createJavaProcedure(String className, String methodName, Vector paramTypes)
  {
    IJavaProcedure app = new GenericJavaAdaptor();
    app.setClassName(className);
    app.setMethodName(methodName);
    app.setParameters(paramTypes);
    return app;
  }
  
  /**
   * Increments concurrentApplicationCount,applicationCounter
   * This is just for debugging
   * @return applicationCounter
   */
  public static synchronized long enterApplication()
  {
    ++concurrentApplicationCount;
    return ++applicationCounter;    
  }

  /**
   * Decrements concurrentApplicationCount
   * This is just for debugging, currentApplicationCount should be zero if there is no 
   * activity in server, otherwise means some applications might have been struck
   * @return concurrentApplicationCount
   */
  public static synchronized long exitApplication()
  {
    return --concurrentApplicationCount;
  }

  
}
