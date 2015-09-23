/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EJBActionSupport.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    Neo Sok Lay         Add validateEvent() method.
 */

package com.gridnode.pdip.framework.rpf.ejb.action;

import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

public abstract class EJBActionSupport
  implements java.io.Serializable, IEJBAction
{
  protected StateMachine sm = null;

  public void init(StateMachine sm)
  {
    this.sm = sm;
  }

  public void validateEvent(IEvent event) throws EventException
  {
  }

  public void doStart() throws EventException
  {
  }

  public void doEnd() throws EventException
  {
  }
}

