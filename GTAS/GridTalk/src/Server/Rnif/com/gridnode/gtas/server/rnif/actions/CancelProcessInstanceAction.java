/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CancelProcessInstanceAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Extend from AbstractGridTalkAction
 * Jan 27 2006    Tam Wei Xiang       To trigger 0A1 failure notification while we cancel 
 *                                    the process instance.
 *                                    Added method send0A1FailureNotification()
 * Feb 08 2006    Neo Sok Lay         To raise process instance failure for user cancelled
 *                                    error type. 
 *                                    Remove use of EntityDAO to retrieve GridDocument & GWDRtProcessDoc,
 *                                    instead retrieve via their managers.     
 * Jan 31 2007    Tam Wei Xiang       Move the logic for cancelling the Process Instance to RnifManagerBean                             
 */
package com.gridnode.gtas.server.rnif.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.events.rnif.CancelProcessInstanceEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.rnif.IProcessInstance;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.*;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the cancellation of one or more ProcessInstances
 * by the user.
 * 
 * @version GT 4.0
 */
public class CancelProcessInstanceAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6522840288842633304L;

	public static final String ACTION_NAME = "CancelProcessInstanceAction";

  private Collection _instListToCancel;

  // **************** Own methods *******************
  protected Object[] getErrorMessageParams(IEvent event)
  {
    CancelProcessInstanceEvent cancelEvent = (CancelProcessInstanceEvent) event;

    return new Object[] {
      IProcessInstance.ENTITY_NAME,
      String.valueOf(_instListToCancel),
      };
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    CancelProcessInstanceEvent cancelEvent = (CancelProcessInstanceEvent) event;

    return new Object[] {
      IProcessInstance.ENTITY_NAME,
      String.valueOf(_instListToCancel),
      };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return CancelProcessInstanceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    CancelProcessInstanceEvent cancelEvent = (CancelProcessInstanceEvent) event;
    if (cancelEvent.getInstUID() == null)
    {
      _instListToCancel = cancelEvent.getInstUIDs();
    }
    else
    {
      _instListToCancel = new ArrayList();
      Long instUid = cancelEvent.getInstUID();
      _instListToCancel.add(instUid);
      ProcessInstanceActionHelper.verifyProcessInstance(instUid);
    }
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(IEvent, TypedException)
   * @since GT 2.2 I1
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    return constructEventResponse(
      IErrorCode.GENERAL_ERROR,
      getErrorMessageParams(event),
      ex);

  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(IEvent)
   * @since GT 2.2 I1
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    String reason = ((CancelProcessInstanceEvent) event).getCancelReason();

    for (Iterator i = _instListToCancel.iterator(); i.hasNext();)
    {	
    	Long processUID = (Long) i.next();
      cancelProcess(processUID, reason);
    }
    
    return constructEventResponse(
      IErrorCode.NO_ERROR,
      getSuccessMessageParams(event));
  }
  
  //TWX 27012006 start
  private void cancelProcess(Long processInstanceUID, String reason)
  	throws Exception
  {
    //TODO require the action userID ? or can get from originalDoc.getSenderUserID ?
  		getRnifMgr().cancelProcess(processInstanceUID, reason, getUserID());
  }
  
  //TWX 27012006 end
  
  private IRnifManagerObj getRnifMgr() throws Exception
  {
    return (IRnifManagerObj)ServiceLocator.instance(
                                                      ServiceLocator.CLIENT_CONTEXT).getObj(
                                                      IRnifManagerHome.class.getName(),
                                                      IRnifManagerHome.class,
                                                      new Object[0]);
  }
}