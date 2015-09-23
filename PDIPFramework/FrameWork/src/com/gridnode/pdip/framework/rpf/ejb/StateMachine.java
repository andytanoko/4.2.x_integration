/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.gridnode.pdip.framework.rpf.ejb;

// rpf imports
import java.util.HashMap;

import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This class is a responsible for processing Events recieved from the
 * client tier. Af part of the WAF framework the events are generated
 * by web actions.
 *
 * The State Machine ties all EJB components together dynamically at
 * runtime thus providing support for reusable components.
 *
 * This class should not be updated to handle various event types.
 * This class will use ActionHandlers to handle events that require
 * processing beyond the scope of this class.
 *
 * The mapping of the event names to handlers is mangaged by the JNDI
 * key contained in the Event:getEventName() which is looked up from
 * an environment entry located in the EJB Deployment descriptor of the
 * EJBClientController. A second option to event handling is to do so
 * in the XML file.
 *
 * State may be stored in the attributeMap
 *
 *
 */
public class StateMachine implements java.io.Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6017118186250309108L;
	private EJBClientControllerBean clientCtrlBean;
  private HashMap attributeMap;
  private HashMap actionMap;
  private SessionContext sc;

  public StateMachine(
    EJBClientControllerBean clientCtrlBean,
    SessionContext sc)
  {
    this.clientCtrlBean = clientCtrlBean;
    this.sc = sc;
    attributeMap = new HashMap();
    actionMap = new HashMap();
  }

  public IEventResponse processEvent(IEvent event) throws EventException
  {
    String eventName = event.getEventName();
    String actionName = null;
    IEventResponse response = null;

    if (eventName != null)
    {
      actionName = getActionName(eventName);
      IEJBAction action = null;
      try
      {
        if (actionMap.get(actionName) != null)
        {
          action = (IEJBAction)actionMap.get(actionName);
        }
        else
        {
          action = (IEJBAction)Class.forName(actionName).newInstance();
          actionMap.put(actionName, action);
        }
      }
      catch (Exception ex)
      {
        System.err.println("StateMachine: error loading " + actionName + " :" + ex);
      }

      if (action != null)
      {
        action.init(this);
        action.doStart();
        action.validateEvent(event);
        response = action.perform(event);
        action.doEnd();
      }
    }

    return response;
  }

  private String getActionName(String eventName)
  {
    // do the lookup
    try
    {
      InitialContext ic = new InitialContext();
      return  (String)ic.lookup(eventName);
    }
    catch (javax.naming.NamingException ex)
    {
      System.err.println("StateMachine caught: " + ex);
      // ignore.. we are working around it below.
    }
    return null;
  }

  public void setAttribute(String key, Object value)
  {
    attributeMap.put(key, value);
  }

  public Object getAttribute(String key)
  {
    return attributeMap.get(key);
  }

  public EJBClientControllerBean getEJBClientController()
  {
    return clientCtrlBean;
  }

  public SessionContext getSessionContext()
  {
    return sc;
  }
}

