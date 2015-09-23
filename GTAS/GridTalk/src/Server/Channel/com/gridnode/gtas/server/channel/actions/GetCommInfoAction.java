/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jul 11 2002    Goh Kan Mun         Modified - Change in name for getCommInfo method
 * Sep 01 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */
package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.GetCommInfoEvent;
import com.gridnode.gtas.model.channel.CommInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetCommInfoAction extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4896240903698594788L;
	private static final String ACTION_NAME = "GetCommInfoAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return CommInfo.convertToMap(entity, CommInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetCommInfoEvent getCommInfoEvent = (GetCommInfoEvent) event;
		return getManager().getCommInfo(getCommInfoEvent.getUId());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetCommInfoEvent getCommInfoEvent = (GetCommInfoEvent) event;
		return new Object[] {CommInfo.ENTITY_NAME, getCommInfoEvent.getUId()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetCommInfoEvent.class;
	}

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]);
  }

}