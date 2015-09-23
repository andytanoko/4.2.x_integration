/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateWebServiceAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import java.util.Map;

import com.gridnode.gtas.events.servicemgmt.CreateWebServiceEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class CreateWebServiceAction extends AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2983672737390197164L;
	public static final String ACTION_NAME = "CreateWebServiceAction";

  protected Class getExpectedEventClass()
  {
    return CreateWebServiceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findWebService(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateWebServiceEvent createEvent = (CreateWebServiceEvent) event;

    WebService newWebService = new WebService();
    newWebService.setWsdlUrl(createEvent.getWsdlUrl());
    newWebService.setEndPoint(createEvent.getEndPoint());
    newWebService.setServiceName(createEvent.getServiceName());
    newWebService.setServiceGroup(createEvent.getServiceGroup());
    return newWebService;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateWebServiceEvent createEvent = (CreateWebServiceEvent) event;
    return new Object[] { WebService.ENTITY_NAME, createEvent.getServiceName(), createEvent.getServiceGroup() };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createWebService((WebService) entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertWebServiceToMap((WebService) entity);
  }

}
