/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetChannelListForBizEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 * Nov 28 2002    Neo Sok Lay         Retain link to Master channel.
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;

import com.gridnode.gtas.events.enterprise.SetChannelListForBizEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a SetChannelListForBizEntityEvent to update
 * the latest attachments of ChannelInfo(s) to a BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I4
 */
public class SetChannelListForBizEntityAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5359035491058959374L;

	public static final String ACTION_NAME = "SetChannelListForBizEntityAction";

  private Collection _channelUIDs;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return SetChannelListForBizEntityEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    SetChannelListForBizEntityEvent setEvent = (SetChannelListForBizEntityEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        setEvent.getBizEntityUID(),
                        setEvent.getChannelList(),
                      };

    return constructEventResponse(
             IErrorCode.UPDATE_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SetChannelListForBizEntityEvent setEvent = (SetChannelListForBizEntityEvent) event;

    if (ActionHelper.isOwnBusinessEntity(setEvent.getBizEntityUID()))
    {
      //get master channel uid
      Long masterChannel = ActionHelper.getMasterChannelUID();
      //if not in _channelUIDs, add in
      if (masterChannel != null && !_channelUIDs.contains(masterChannel))
        _channelUIDs.add(masterChannel);
    }

    ServiceLookupHelper.getEnterpriseHierarchyMgr().setChannelsForBizEntity(
      setEvent.getBizEntityUID(), _channelUIDs);

    Collection channelList = ActionHelper.getChannels(_channelUIDs);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertChannelsToMapObjects(channelList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    SetChannelListForBizEntityEvent setEvent = (SetChannelListForBizEntityEvent) event;

    ActionHelper.verifyBusinessEntity(setEvent.getBizEntityUID());

    //determine the existing ChannelInfos.
    _channelUIDs = ActionHelper.getExistChannelUIDs(setEvent.getChannelList());

    //throw error?
    if (!_channelUIDs.containsAll(setEvent.getChannelList()))
      throw new Exception("Invalid ChannelInfo to attach!");
  }

  // ******************** Own methods ***********************************

}