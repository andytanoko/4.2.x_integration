/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2002    Goh Kan Mun         Created
 * Sep 01 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */
package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.GetChannelInfoEvent;
import com.gridnode.gtas.model.channel.ChannelInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetChannelInfoAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7857113687983759324L;
	private static final String ACTION_NAME = "GetChannelInfoAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return ChannelInfo.convertToMap(entity, ChannelInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetChannelInfoEvent getChannelInfoEvent = (GetChannelInfoEvent) event;
		return getManager().getChannelInfo(getChannelInfoEvent.getUId());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetChannelInfoEvent getChannelInfoEvent = (GetChannelInfoEvent) event;
		return new Object[] {ChannelInfo.ENTITY_NAME, getChannelInfoEvent.getUId()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetChannelInfoEvent.class;
	}
	
  // ****************** Own methods ********************

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]);
  }

}