/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEJBAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    Neo Sok Lay         Throw EventException in doStart and
 *                                    doEnd.
 */
package com.gridnode.pdip.framework.rpf.ejb.action;

import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;

public interface IEJBAction
{
  public void init(StateMachine sm);

  public void doStart() throws EventException;

  public void validateEvent(IEvent event) throws EventException;

  public IEventResponse perform(IEvent event) throws EventException;

  public void doEnd() throws EventException;
}

