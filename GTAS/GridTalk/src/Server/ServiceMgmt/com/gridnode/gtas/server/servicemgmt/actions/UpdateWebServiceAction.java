/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateWebServiceAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.servicemgmt.actions;

import java.util.Map;

import com.gridnode.gtas.events.servicemgmt.UpdateWebServiceEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.servicemgmt.helpers.ActionHelper;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;


public class UpdateWebServiceAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3175700365661287176L;

	private WebService _webService;

  public static final String ACTION_NAME = "UpdateWebServiceAction";

  protected Class getExpectedEventClass()
  {
    return UpdateWebServiceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertWebServiceToMap((WebService)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateWebServiceEvent updEvent = (UpdateWebServiceEvent)event;
    _webService = ActionHelper.getManager().findWebService(updEvent.getWebServiceUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateWebServiceEvent updEvent = (UpdateWebServiceEvent)event;

    _webService.setWsdlUrl(updEvent.getWsdlUrl());
    _webService.setEndPoint(updEvent.getEndPoint());
    _webService.setServiceGroup(updEvent.getServiceGroup());
    return _webService;
  }


  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateWebService((WebService)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findWebService(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateWebServiceEvent updEvent = (UpdateWebServiceEvent)event;
    return new Object[]
           {
             WebService.ENTITY_NAME,
             String.valueOf(updEvent.getWebServiceUid())
           };
  }

}
