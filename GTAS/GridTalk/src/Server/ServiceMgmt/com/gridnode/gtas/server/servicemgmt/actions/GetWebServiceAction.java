/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetWebServiceAction.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.servicemgmt.GetWebServiceEvent;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;

import com.gridnode.pdip.app.servicemgmt.model.WebService;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a WebService.
 *
 */
public class GetWebServiceAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4601030168110418666L;
	public static final String ACTION_NAME = "GetWebServiceAction";

  protected Class getExpectedEventClass()
  {
    return GetWebServiceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetWebServiceEvent getEvent = (GetWebServiceEvent)event;
    return ActionHelper.getManager().findWebService(getEvent.getWebServiceUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertWebServiceToMap((WebService)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetWebServiceEvent getEvent = (GetWebServiceEvent)event;
    return new Object[]
           {
             WebService.ENTITY_NAME,
             getEvent.getWebServiceUID()
           };
  }
}