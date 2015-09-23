/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivateBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-07-17     Teh Yu Phei         Created (Ticket 31)
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.ActivateBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the deletion of a BE.
 *
 * @author Teh Yu Phei
 *
 * @version 
 * @since 
 */
public class ActivateBusinessEntityAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7398432654383379584L;
	public static final String ACTION_NAME = "ActivateBusinessEntityAction";

  protected Class getExpectedEventClass()
  {
    return ActivateBusinessEntityEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
	  ActivateBusinessEntityEvent msEvent = (ActivateBusinessEntityEvent)event;
    return new Object[]
           {
             msEvent.getBEIDs()
           };
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
	  ActivateBusinessEntityEvent msEvent = (ActivateBusinessEntityEvent)event;
      long[] beIds = msEvent.getBEIDs();

    
      if(beIds != null && beIds.length>0)
      {
    	  for (int i = 0; i< beIds.length; i++ )
          {
    		  Long bizID = beIds[i];
    		  ActionHelper.getBizRegManager().markActivateBusinessEntity(bizID);
          }
      }
      return constructEventResponse();
  }
}