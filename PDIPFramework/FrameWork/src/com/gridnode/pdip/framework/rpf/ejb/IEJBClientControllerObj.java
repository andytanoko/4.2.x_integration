/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEJBClientControllerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This is the EJB-tier controller of the MVC.
 * It is implemented as a session EJB. It controls all the activities
 * that happen in a client session.
 * It also provides mechanisms to access other session EJBs.
 */
public interface IEJBClientControllerObj extends EJBObject
{
  /**
   * Feeds the specified event to the state machine of the business logic.
   * @return an EventResponse.
   */
  public IEventResponse processEvent(IEvent event)
    throws  EventException, RemoteException;
}

