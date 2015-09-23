/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRegistryConnectInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.UpdateRegistryConnectInfoEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the UpdateRegistryConnectInfoEvent for update 
 * of a RegistryConnectInfo.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class UpdateRegistryConnectInfoAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3304216962949796008L;

	public static final String ACTION_NAME = "UpdateRegistryConnectInfoAction";

  private RegistryConnectInfo _connInfo;
   
  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getBizRegManager().updateRegistryConnectInfo(
      (RegistryConnectInfo)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateRegistryConnectInfoEvent updEvent = (UpdateRegistryConnectInfoEvent)event;

    _connInfo.setPublishPassword(updEvent.getPassword());
    _connInfo.setPublishUrl(updEvent.getPublishUrl());
    _connInfo.setPublishUser(updEvent.getUsername());
    _connInfo.setQueryUrl(updEvent.getQueryUrl());

    return _connInfo;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateRegistryConnectInfoEvent updEvent = (UpdateRegistryConnectInfoEvent)event;

    try
    {
      _connInfo = (RegistryConnectInfo)retrieveEntity(updEvent.getUid());
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad RegistryConnectInfo UID: "+updEvent.getUid());
    }
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateRegistryConnectInfoEvent updEvent = (UpdateRegistryConnectInfoEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             RegistryConnectInfo.ENTITY_NAME,
             updEvent.getUid(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateRegistryConnectInfoEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getBizRegManager().findRegistryConnectInfo(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRegConnInfoToMap((RegistryConnectInfo)entity);
  }

  // **************************** Own Methods *****************************

}