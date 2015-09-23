/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRegistryConnectInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.CreateRegistryConnectInfoEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the CreateRegistryConnectInfoEvent
 * for creation of a new RegistryConnectInfo.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class CreateRegistryConnectInfoAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1290815764017800917L;
	public static final String ACTION_NAME = "CreateRegistryConnectInfoAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateRegistryConnectInfoEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getBizRegManager().findRegistryConnectInfo(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateRegistryConnectInfoEvent addEvent = (CreateRegistryConnectInfoEvent)event;

    RegistryConnectInfo connInfo = new RegistryConnectInfo();
    connInfo.setName(addEvent.getName());
    connInfo.setPublishPassword(addEvent.getPassword());
    connInfo.setPublishUrl(addEvent.getPublishUrl());
    connInfo.setPublishUser(addEvent.getUsername());
    connInfo.setQueryUrl(addEvent.getQueryUrl());

    return connInfo;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateRegistryConnectInfoEvent addEvent = (CreateRegistryConnectInfoEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             RegistryConnectInfo.ENTITY_NAME,
             addEvent.getName(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return ActionHelper.getBizRegManager().createRegistryConnectInfo(
                            (RegistryConnectInfo)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRegConnInfoToMap((RegistryConnectInfo)entity);
  }

  // ****************** Own methods **********************************
}