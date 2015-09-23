/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateServiceAssignmentAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import java.util.Map;

import com.gridnode.gtas.events.servicemgmt.CreateServiceAssignmentEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;
import com.gridnode.pdip.app.servicemgmt.helpers.PasswordDigestHelper;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class CreateServiceAssignmentAction extends AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7262276665793849995L;
	public static final String ACTION_NAME = "CreateServiceAssignmentAction";

  protected Class getExpectedEventClass()
  {
    return CreateServiceAssignmentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findServiceAssignment(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateServiceAssignmentEvent createEvent = (CreateServiceAssignmentEvent) event;
    ServiceAssignment newServiceAssignment = new ServiceAssignment();
    newServiceAssignment.setUserName(createEvent.getUserName());
    newServiceAssignment.setUserType(createEvent.getUserType());
    newServiceAssignment.setPassword(createEvent.getPassword());
    newServiceAssignment.setWebServiceUIds(createEvent.getWebServiceUIds());
    return newServiceAssignment;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateServiceAssignmentEvent createEvent = (CreateServiceAssignmentEvent) event;
    return new Object[] { ServiceAssignment.ENTITY_NAME, createEvent.getUserName(), createEvent.getUserType()};
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    ServiceAssignment serviceAssignment = (ServiceAssignment) entity;
    String digestPassword = PasswordDigestHelper.getInstance().getEncodedDigest(serviceAssignment.getPassword());
    serviceAssignment.setPassword(digestPassword);
    return ActionHelper.getManager().createServiceAssignment(serviceAssignment);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertServiceAssignmentToMap((ServiceAssignment) entity);
  }

}
