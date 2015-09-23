/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LogoutListener.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.login;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gridnode.gtas.client.ctrl.IGTSession;

/**
 * Class that listens for session timeout and logouts the user from GTAS.
 * We dont want to introduce servlet classes into the ctrl package so this is added seperately to
 * the session, however, on a timeout they will both be unbound and this one can call the
 * logout on the gtasSession. The instance of this bound in the session should not be removed
 * manually as it will then call the gtasSession.logout() - though if you want to remove this without
 * logging out of GTAS you could first call setGtasSession(null) to kill its reference to the
 * IGTSession object. The static utiltity method clearListener() can do this for you. When
 * setting up the listener you can make use of the static utiltity method setupListener()
 */ 
public class LogoutListener implements HttpSessionBindingListener
{
  private static final Log _log = LogFactory.getLog(LogoutListener.class); // 20031209 DDJ

  private IGTSession _gtasSession;
  public static final String KEY = "com.gridnode.gtas.client.web.login.LogoutListener";

  /**
   * Utility method to set up a logout listener for an IGTSession in the Session.
   * It is assumed there will only be one for each HttpSession and is bound under its classname.
   * For convienience the created listener is returned
   * @param session
   * @param gtasSession
   * @return listener
   */
  public static LogoutListener setupListener(HttpSession httpSession, IGTSession gtasSession)
  {
    if(httpSession == null) throw new NullPointerException("httpSession is null"); //20030416AH
    if(gtasSession == null) throw new NullPointerException("gtasSession is null"); //20030416AH
    LogoutListener listener = new LogoutListener();
    listener.setGtasSession(gtasSession);
    httpSession.setAttribute(KEY, listener);
    return listener;
  }

  /**
   * Removes the LogoutListener stored under key from the HttpSession without causing it
   * to invoke a logout.
   * @param httpSession
   */
  public static void clearListener(HttpSession httpSession)
  {
    LogoutListener listener = (LogoutListener)httpSession.getAttribute(KEY);
    if(listener != null)
    {
      listener.setGtasSession(null); //set gtasSession to null so it doesnt get logged out!
      httpSession.removeAttribute(KEY); //remove the listener stored under constant KEY
    }
  }

  /**
   * Set the IGTSession object on which logout() will be called when this LogoutListener
   * is unbound ffrom the HttpSession.
   * @param gtasSession
   */
  public void setGtasSession(IGTSession gtasSession)
  {
    _gtasSession = gtasSession;
  }

  /**
   * Return the IGTSession object
   * @return gtasSession
   */
  public IGTSession getGtasSession()
  {
    return _gtasSession;
  }

  /**
   * Called by the servlet api when this bean is bound to an httpSession attribute.
   * We arent doing anything important in this method, just recording a log entry to note that
   * it happened, and the userId of the loggedIn user in the gtasSession.
   */
  public void valueBound(HttpSessionBindingEvent event)
  {
    if(_log.isInfoEnabled())
    {
      if(_gtasSession != null)
      {
        StringBuffer buffer = new StringBuffer("LogoutListener object bound in HttpSession for '");
        buffer.append(_gtasSession.getUserId());
        buffer.append("'");
        _log.info(buffer);
      }
      else
      {
        // If the gtasSession is not set , we log the fact as chances are it should have been and
        // this is a mistake which whoever is debugging will be interested in knowing about
        _log.info("LogoutListener bound in HttpSession - no gtasSession was set");
      }
    }
  }

  /**
   * This will be called by the servlet api when this instance is unbound from an httpServlet
   * attribute - either manually or by a session timeout. It is this latter case we are interested
   * in, and the reason for this beans existence. If there is a gtasSession property set this method
   * will cause its logout() method to be invoked. (If gtasSession is null then no action)
   */
  public void valueUnbound(HttpSessionBindingEvent event)
  {
    if(_gtasSession != null)
    {
      try
      {
        if(_log.isInfoEnabled())
        {
          StringBuffer buffer = new StringBuffer("LogoutListener object unbound in HttpSession - ");
          buffer.append("invoking GTAS logout for '");
          buffer.append(_gtasSession.getUserId());
          buffer.append("'");
          _log.info(buffer);
        }
        _gtasSession.logout();
      }
      catch(Throwable t)
      {
        if(_log.isErrorEnabled())
        {
          _log.error("Error occured invoking GTAS logout from LogoutListener", t);
        }
      }
    }
  }
}

