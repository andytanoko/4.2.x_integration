/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistryConnectInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 22 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Move RegistryConnectInfo to 
 *                                    PDIPAppServices/BizRegistry
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the GetRegistryConnectInfoEvent to retrieve 
 * one RegistryConnectInfo.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class GetRegistryConnectInfoAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4493586048677519362L;
	public static final String ACTION_NAME = "GetRegistryConnectInfoAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRegConnInfoToMap((RegistryConnectInfo)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetRegistryConnectInfoEvent getEvent = (GetRegistryConnectInfoEvent)event;

    return ActionHelper.getBizRegManager().findRegistryConnectInfo(
              getEvent.getUid());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetRegistryConnectInfoEvent getEvent = (GetRegistryConnectInfoEvent)event;
    return new Object[]
           {
             getEvent.getUid(),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetRegistryConnectInfoEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}