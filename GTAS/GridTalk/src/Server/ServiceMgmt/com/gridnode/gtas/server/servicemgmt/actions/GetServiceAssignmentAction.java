/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetServiceAssignmentAction.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.servicemgmt.GetServiceAssignmentEvent;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;

import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a ServiceAssignment.
 *
 */
public class GetServiceAssignmentAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7523232787951863307L;
	public static final String ACTION_NAME = "GetServiceAssignmentAction";

  protected Class getExpectedEventClass()
  {
    return GetServiceAssignmentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetServiceAssignmentEvent getEvent = (GetServiceAssignmentEvent)event;
    return ActionHelper.getManager().findServiceAssignment(getEvent.getServiceAssignmentUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertServiceAssignmentToMap((ServiceAssignment)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetServiceAssignmentEvent getEvent = (GetServiceAssignmentEvent)event;
    return new Object[]
           {
             ServiceAssignment.ENTITY_NAME,
             getEvent.getServiceAssignmentUID()
           };
  }
}