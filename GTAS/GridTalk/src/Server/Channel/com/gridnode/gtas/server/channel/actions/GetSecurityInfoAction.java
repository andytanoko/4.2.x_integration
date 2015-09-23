/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSecurityInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh           Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */

package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.GetSecurityInfoEvent;
import com.gridnode.gtas.model.channel.SecurityInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class GetSecurityInfoAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7449432992228139093L;
	private static final String ACTION_NAME = "GetSecurityInfoAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return SecurityInfo.convertToMap(entity, SecurityInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetSecurityInfoEvent getsecurityInfoEvent = (GetSecurityInfoEvent) event;
		return getManager().getSecurityInfo(getsecurityInfoEvent.getUId());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetSecurityInfoEvent getsecurityInfoEvent = (GetSecurityInfoEvent) event;
		return new Object[] {SecurityInfo.ENTITY_NAME, getsecurityInfoEvent.getUId()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetSecurityInfoEvent.class;
	}
	
  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]);
  }


}


