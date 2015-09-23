/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPackagingInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh           Created
 * Sep 01 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */


package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.GetPackagingInfoEvent;
import com.gridnode.gtas.model.channel.PackagingInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class GetPackagingInfoAction extends AbstractGetEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8470758901503868490L;
	private static final String ACTION_NAME = "GetPackagingInfoAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return PackagingInfo.convertToMap(entity, PackagingInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetPackagingInfoEvent getpackagingInfoEvent = (GetPackagingInfoEvent) event;

    return getManager().getPackagingInfo(getpackagingInfoEvent.getUId());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetPackagingInfoEvent getpackagingInfoEvent = (GetPackagingInfoEvent) event;
		return new Object[] {PackagingInfo.ENTITY_NAME, getpackagingInfoEvent.getUId()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetPackagingInfoEvent.class;
	}

	private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]);
  }




}

