/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelListForBizEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;

import com.gridnode.gtas.events.enterprise.GetChannelListForBizEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a GetChannelListForBizEntityEvent to obtain
 * the latest attachments of ChannelInfo(s) to a BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetChannelListForBizEntityAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2079077506831582946L;
	public static final String ACTION_NAME = "GetChannelListForBizEntityAction";

  //private Collection _channelUIDs;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetChannelListForBizEntityEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetChannelListForBizEntityEvent getEvent = (GetChannelListForBizEntityEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        getEvent.getBizEntityUID(),
                      };

    return constructEventResponse(
             IErrorCode.FIND_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetChannelListForBizEntityEvent getEvent = (GetChannelListForBizEntityEvent) event;

    Collection channelList = ServiceLookupHelper.getEnterpriseHierarchyMgr().getChannelsForBizEntity(
      getEvent.getBizEntityUID());
    if (!channelList.isEmpty())
      channelList = ActionHelper.getChannels(channelList);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertChannelsToMapObjects(channelList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    GetChannelListForBizEntityEvent getEvent = (GetChannelListForBizEntityEvent) event;

    ActionHelper.verifyBusinessEntity(getEvent.getBizEntityUID());
  }

  // ******************** Own methods ***********************************

}