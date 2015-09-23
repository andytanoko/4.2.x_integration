/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateServiceAssignmentAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import java.util.Map;

import com.gridnode.gtas.events.servicemgmt.UpdateServiceAssignmentEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;
import com.gridnode.pdip.app.servicemgmt.helpers.PasswordDigestHelper;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;


public class UpdateServiceAssignmentAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6778543403766608090L;

	private ServiceAssignment _serviceAssignment;

  public static final String ACTION_NAME = "UpdateServiceAssignmentAction";

  protected Class getExpectedEventClass()
  {
    return UpdateServiceAssignmentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertServiceAssignmentToMap((ServiceAssignment)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateServiceAssignmentEvent updEvent = (UpdateServiceAssignmentEvent)event;
    _serviceAssignment = ActionHelper.getManager().findServiceAssignment(updEvent.getServiceAssignmentUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateServiceAssignmentEvent updEvent = (UpdateServiceAssignmentEvent)event;
    String oldPassword = _serviceAssignment.getPassword();
    _serviceAssignment.setPasswordModified(!oldPassword.equals(updEvent.getPassword()));
    _serviceAssignment.setPassword(updEvent.getPassword());
    _serviceAssignment.setUserType(updEvent.getUserType());
    _serviceAssignment.setWebServiceUIds(updEvent.getWebServiceUIds());
    return _serviceAssignment;
  }


  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ServiceAssignment serviceAssignment = (ServiceAssignment) entity;
    if(serviceAssignment.isPasswordModified())
    {
      String digestPassword = PasswordDigestHelper.getInstance().getEncodedDigest(serviceAssignment.getPassword());
      serviceAssignment.setPassword(digestPassword);
    }
    ActionHelper.getManager().updateServiceAssignment(serviceAssignment);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findServiceAssignment(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateServiceAssignmentEvent updEvent = (UpdateServiceAssignmentEvent)event;
    return new Object[]
           {
             ServiceAssignment.ENTITY_NAME,
             String.valueOf(updEvent.getServiceAssignmentUid())
           };
  }

}
