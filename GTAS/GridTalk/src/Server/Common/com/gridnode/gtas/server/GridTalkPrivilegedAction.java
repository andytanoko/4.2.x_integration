/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkPrivilegedAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Re-enable Access control checking.
 */
package com.gridnode.gtas.server;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.base.acl.auth.ACLSecurityManager;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;

import java.security.PrivilegedExceptionAction;

/**
 * A GridTalkPrivilegedAction performs a privilege checking before an event is
 * performed.
 * <P>
 * If checks if the event is a IGuardedEvent and if a GridTalkSecurityManager
 * is installed for the current application.
 * If yes, it would then check the action access permission with the security
 * manager. If the check passes, the guarded event would be performed.
 * <P>If the event is not a IGuardedEvent or no security manager has been
 * installed, the event is performed straightaway.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GridTalkPrivilegedAction implements PrivilegedExceptionAction
{
  private static ACLSecurityManager _aclSecMgr;
  private IEvent _event;
  private StateMachine _sm;

  /**
   * Construct a GridTalkPrivilegedAction
   *
   * @param event The Guarded event
   * @param sm The state machine to perform the event.
   *
   * @since 2.0
   */
  public GridTalkPrivilegedAction(IEvent event, StateMachine sm)
  {
    _event = event;
    _sm = sm;
  }

  /**
   * Performs action access permission checking and if the check passes process
   * the guarded event. If no security manager installed, process the event
   * straightaway.
   *
   * @return the IEventResponse object returned from processing the event.
   * @exception EventException Error in processing the event.
   *
   * @since 2.0
   */
  public Object run() throws EventException
  {
    if (_event instanceof IGuardedEvent)
    {
      if (_aclSecMgr != null)
      {
        IGuardedEvent guardedEvent = (IGuardedEvent)_event;
        _aclSecMgr.checkActionAccess(
          guardedEvent.getGuardedFeature(),
          guardedEvent.getGuardedAction());

      }
    }
    return _sm.processEvent(_event);
  }

  /**
   * Set the security manager for checking permissions.
   *
   * @param mgr The ACLSecurityManager to handle the Action acces permission
   * checking.
   */
  public static void setSecurityManager(ACLSecurityManager mgr)
  {
    _aclSecMgr = mgr;
  }
}