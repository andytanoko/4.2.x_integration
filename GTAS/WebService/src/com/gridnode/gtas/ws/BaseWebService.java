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
 * File: SoapMessageRetrieveService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??             ??                  Created
 * 04 OCT 2012    Tam Wei Xiang       #3850: The performing of the logout
 *                                           will be done at the client side.
 *                                       
 **/
package com.gridnode.gtas.ws;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * <p>Title: Mr</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Gridnode</p>
 * @author unascribed
 * @version 1.0
 */
public class BaseWebService
{
  static public String GT_CLIENT_CONTROLLER = "GridTalkClientController";
  static public String USERID = "UserID";
 
  private IGridTalkClientControllerObj _clientController;
  private String userID = null;
  protected Log _log = LogFactory.getLog(this.getClass());
  
  public BaseWebService()
  {
  }

  protected Vector processEntityListResponse(
    EntityListResponseData results,
    IEvent event)
    throws Exception
  {
    if(results == null)
    {
      throw new NullPointerException("results is null");
    }

    if(event == null)
    {
      throw new NullPointerException("event is null");
    }

    Vector returnList = new Vector();
    Collection list = results.getEntityList();

    if(list != null)
    {
      Iterator i = list.iterator();

      while(i.hasNext())
      {
        Object elementObject = i.next(); //20030130AH
        returnList.add(elementObject);
      }
    }

    return returnList;
  }

  protected Vector fireGetListEvent(IEvent event)
    throws Exception
  {
    EntityListResponseData results = (EntityListResponseData)fireEvent(event);
    return processEntityListResponse(results,event);
  }

  protected HttpSession getSession()
  {
    try
    {
      MessageContext context = MessageContext.getCurrentContext();
      HttpServletRequest request = (HttpServletRequest)context.getProperty(
          HTTPConstants.MC_HTTP_SERVLETREQUEST);
      HttpSession session = request.getSession();

      return session;
    }
    catch(Exception ex)
    {
      return null;
    }
  }

  protected void saveCacheController()
  {
    HttpSession session = getSession();

    if(session != null)
    {
      session.setAttribute(
        GT_CLIENT_CONTROLLER,
        getClientController());
    }
  }

  protected IGridTalkClientControllerObj getCacheController()
  {
    HttpSession session = getSession();
    
    if(session != null)
    {
      Object ob = session.getAttribute(GT_CLIENT_CONTROLLER);
      if(ob == null)
      {
        return null;
      }
      else
      {
        return (IGridTalkClientControllerObj)ob;
      }
    }
    else
    {
      return null;
    }
  }

  protected void getController()
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

  protected Object fireEvent(IEvent event)
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

  final synchronized IEventResponse handleEvent(IEvent event)
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

  protected boolean login(
    String userId,
    String password)
  {
    //#3850 TWX
//    if(getUserID() != null)
//    {
//      logout();
//    }

    try
    {
      fireEvent(new UserLoginEvent(userId,password));
      setUserID(userId);
      return true;
    }
    catch(Exception ex)
    {
      _log.error("cannot login",ex);
      return false;
    }
  }

  protected boolean logout()
  {
    if(getUserID() == null)
    {
      return true;
    }

    try
    {
      fireEvent(new UserLogoutEvent());
      setUserID(null);
      return true;
    }
    catch(Exception ex)
    {
      _log.error("cannot logout",ex);
      return false;
    }
  }

  protected void setUserID(String userID)
  {
    this.userID = userID;

    HttpSession session = getSession();

    if(session != null)
    {
      if(userID == null)
      {
        session.removeAttribute(USERID);
      }
      else
      {
        session.setAttribute(USERID,userID);
      }
    }
  }

  protected String getUserID()
  {
    if(userID == null)
    {
      HttpSession session = getSession();

      if(session != null)
      {
        Object ob = session.getAttribute(USERID);
        if(ob != null)
        {
          userID = (String)ob;
        }
      }
    }

    return userID;
  }

  protected IGridTalkClientControllerObj getClientController()
  {
    return _clientController;
  }

  protected void setClientController(IGridTalkClientControllerObj clientController)
  {
    this._clientController = clientController;
  }
}
