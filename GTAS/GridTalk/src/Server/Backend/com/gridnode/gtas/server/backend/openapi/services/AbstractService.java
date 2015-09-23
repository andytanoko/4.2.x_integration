package com.gridnode.gtas.server.backend.openapi.services;

import java.rmi.RemoteException;

import com.gridnode.gtas.server.backend.helpers.Logger;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

abstract public class AbstractService implements IGTAPIService
{
  GTServiceFactory _factory = null;

  public AbstractService(GTServiceFactory factory)
  {
    _factory = factory;
  }

  protected IEventResponse handleEvent(IEvent event)
      throws EventException, RemoteException, ServiceLookupException
  {
    return _factory.handleEvent(event);
  }

  protected GTServiceFactory getFactory()
  {
    return _factory;
  }

  /**
   * @deprecated Use errorLog(String,String,String,String,Throwable)
   * To log error message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @version GT 4.0 VAN
   */
  protected final void err(String className, String methodName, String message, Throwable t)
  {
    Logger.err("[" + className + "." + methodName + "] " + message, t);
  }

  /**
   * @deprecated Use errorLog(String,String,String,String)
   * To log error message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @version GT 4.0 VAN
   */
  protected final void err(String className, String methodName, String message)
  {
    Logger.err("[" + className + "." + methodName + "] " + message);
  }
  
  /**
   * To log error message with the invoked class name and method name.
   * @param errorCode The error code of this error
   * @param className The name of the class that is invoking this method.
   * @param methodName The name of the method that is invoking this method.
   * @param message The message to be logged.
   * @param t The <p>Throwable</p> object to be included in the log.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  protected final void error(String errorCode, String className, String methodName, String message, Throwable t)
  {
    Logger.error(errorCode, "[" + className + "." + methodName + "] " + message, t);
  }

  /**
   * To log error message with the invoked class name and method name.
   *@param errorCode The error code of this error
   * @param className The name of the class that is invoking this method.
   * @param methodName The name of the method that is invoking this method.
   * @param message The message to be logged.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  protected final void error(String errorCode, String className, String methodName, String message)
  {
    Logger.error(errorCode, "[" + className + "." + methodName + "] " + message);
  }
  
  /**
   * To log warning message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  protected final void warn(String className, String methodName, String message, Throwable t)
  {
    Logger.warn("[" + className + "." + methodName + "] " + message, t);
  }

  /**
   * To log warning message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  protected final void warn(String className, String methodName, String message)
  {
    Logger.warn("[" + className + "." + methodName + "] " + message);
  }

  protected final void debug(String className, String methodName, String message, Throwable t)
  {
    Logger.debug("[" + className + "." + methodName + "] " + message, t);
  }

  protected final void debug(String className, String methodName, String message)
  {
    Logger.debug("[" + className + "." + methodName + "] " + message);
  }

  protected final void log(String className, String methodName, String message)
  {
    Logger.log("[" + className + "." + methodName + "] " + message);
  }

}
