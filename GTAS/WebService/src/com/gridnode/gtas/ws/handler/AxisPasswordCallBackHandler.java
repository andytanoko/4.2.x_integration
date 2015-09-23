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
 * File: AxisPasswordCallBackHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.handler;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSPasswordCallback;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class AxisPasswordCallBackHandler implements CallbackHandler
{
  private Log _log = LogFactory.getLog(this.getClass());
  private IGridTalkClientControllerObj _clientController;
  private static final String GT_CLIENT_CONTROLER = "gtClientController";
  
  public void handle(Callback[] callbacks) throws UnsupportedCallbackException
  {

    for(Callback callback : callbacks)
    {
      //Somehow the Apache API does not impl the javax PasswordCallback interface,
      //need to explicitly cast to Apache class
      WSPasswordCallback pwcb = (WSPasswordCallback)callback;
      String username = pwcb.getIdentifer();
      String password = pwcb.getPassword();
//      if("apache".equals(pwcb.getIdentifer()) && "password".equals(pwcb.getPassword()))
//      {
//        return;
//      }
      try
      {
        login(username, password);
        _log.debug("User="+username+" login successfully!");
        return;
      }
      catch (ServiceLookupException e)
      {
        _log.error("Failed to look up GTClientCtr", e);
        throw new UnsupportedCallbackException(callback, "Failed authentication, internal server error!");
      }
      catch (RemoteException e)
      {
        _log.error("Failed to process UserLoginEvent", e);
        throw new UnsupportedCallbackException(callback, "Failed authentication, internal server error!");
      }
      catch (EventException e)
      {
        _log.error("Encounter event exception", e);
        throw new UnsupportedCallbackException(callback, "Failed authentication, internal server error!");
      }
    }
      
  }

  private boolean login(String userId, String password) throws EventException, ServiceLookupException, RemoteException
  {
    UserLoginEvent loginEvent = new UserLoginEvent(userId, password);

    fireEvent(loginEvent);
    return true;
    
  }
  
  private Object fireEvent(IEvent event)
    throws ServiceLookupException,RemoteException,EventException
  {
    if(event == null)
    {
      throw new java.lang.NullPointerException("event is null");
    }
  
    BasicEventResponse response = (BasicEventResponse)handleEvent(event);
    if(response == null)
    {
      throw new java.lang.NullPointerException(
        "Response is null for event:" + event.getClass().getName());
    }
  
    if(!response.isEventSuccessful())
    {
      _log.error(response.getErrorReason() + "\r\n" + response.getErrorTrace());
      throw new EventException(
        "Response exception for event:" + event.getClass().getName());
    }
  
    return response.getReturnData();
  }

  private IEventResponse handleEvent(IEvent event)
    throws EventException,RemoteException,ServiceLookupException
  {
    if(event == null)
    {
      throw new java.lang.NullPointerException("null event");
    }
  
    if(_clientController == null)
    {
      getController();
    }
    
    String eventClass = event.getClass().getName();
    _log.debug("Sending " + eventClass + " to GridTalk Client Controller");
  
    long startTime = System.currentTimeMillis();
    IEventResponse response = _clientController.processEvent(event);
    long endTime = System.currentTimeMillis();
    _log.debug(
      "Approximate response time for " + eventClass + " was " +
      (endTime - startTime) + " milliseconds");
  
    return response;
  }
  
  private void getController()
    throws ServiceLookupException
  {
    if(_clientController == null)
    {
      _clientController = getCacheController();
  
      if(_clientController != null)
      {
        _log.info("Obtained a cached reference to IGridTalkClientController");
        return;
      }
  
      try
      {
        _log.debug("Attempting to get reference to IGridTalkClientController");
        long startTime = new Date().getTime();
        _clientController = (IGridTalkClientControllerObj)ServiceLocator.instance(
            ServiceLocator.CLIENT_CONTEXT).getObj(
            IGridTalkClientControllerHome.class.getName(),
            IGridTalkClientControllerHome.class,
            new Object[0]);
        saveCacheController();
  
        long endTime = new Date().getTime();
        _log.info(
          "GridTalk client controller reference obtained in approx " +
          (endTime - startTime) + " milliseconds");
      }
      catch(ServiceLookupException ex)
      {
        // perform a 2nd attempt to get the client controller remote object
        // invalidate the cache in the SeviceLocator
        //_log.info("Attempting to get reference to IGridTalkClientController again");
        ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT)
                      .invalidate();
        _clientController = (IGridTalkClientControllerObj)ServiceLocator.instance(
            ServiceLocator.CLIENT_CONTEXT).getObj(
            IGridTalkClientControllerHome.class.getName(),
            IGridTalkClientControllerHome.class,
            new Object[0]);
        saveCacheController();
      }
    }
  }
  
//  protected HttpSession getSession()
//  {
//    try
//    {
//      MessageContext context = MessageContext.getCurrentContext();
//      HttpServletRequest request = (HttpServletRequest)context.getProperty(
//          HTTPConstants.MC_HTTP_SERVLETREQUEST);
//      HttpSession session = request.getSession();
//
//      return session;
//    }
//    catch(Exception ex)
//    {
//      return null;
//    }
//  }
  
  private void saveCacheController()
  {
//    HttpSession session = getSession();
//
//    if(session != null)
//    {
//      session.setAttribute(
//        GT_CLIENT_CONTROLER,
//        getClientController());
//      
//      _log.info("Saving GT Client controller to http session.");
//    }
  }
  
  private IGridTalkClientControllerObj getClientController()
  {
    return _clientController;
  }
  
  private IGridTalkClientControllerObj getCacheController()
  {
//    HttpSession session = getSession();
//    if(session != null)
//    {
//      Object ob = session.getAttribute(AxisPasswordCallBackHandler.GT_CLIENT_CONTROLER);
//      if(ob == null)
//      {
//        _log.debug("Can not find GT client ctrl from Http Session cache");
//        return null;
//      }
//      else
//      {
//        _log.debug("Obtaining GT client ctrl from Http Session cache");
//        return (IGridTalkClientControllerObj)ob;
//      }
//    }
//    else
//    {
//      return null;
//    }
    //TODO explore the Axis2 HTTP Context
    return null;
  }
}
