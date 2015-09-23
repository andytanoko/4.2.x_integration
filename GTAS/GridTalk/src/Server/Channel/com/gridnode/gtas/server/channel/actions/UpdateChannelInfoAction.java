/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateChannelInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Goh Kan Mun         Created
 * Aug 06 2002    Jagadeesh           Modified - Changed constructEventResponse to include
 *                                    Map representation of the Entity
 * Jan 17 2003    Jagadeesh           Modified - Not to check for TptProtocol Type of Channel.
 * Dec 22 2003    Jagadeesh           Modified - Added FlowControlProfile Embedded Entity.
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstactUpdateEntityAction
 */
package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.UpdateChannelInfoEvent;
import com.gridnode.gtas.model.channel.ChannelInfoEntityFieldId;
import com.gridnode.gtas.model.channel.FlowControlInfoEntityFieldId;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
public class UpdateChannelInfoAction extends AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5854821229947004329L;

	private static final String ACTION_NAME = "UpdateChannelInfoAction";

  private ChannelInfo _channelInfoToUpdate;

  protected Map convertToMap(AbstractEntity entity)
	{
		return ChannelInfo.convertToMap(entity, ChannelInfoEntityFieldId.getEntityFieldID(),null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateChannelInfoEvent updEvent = (UpdateChannelInfoEvent)event;
		return new Object[] {ChannelInfo.ENTITY_NAME, updEvent.getUId()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateChannelInfoEvent updEvent = (UpdateChannelInfoEvent)event;
 
		_channelInfoToUpdate.setDescription(updEvent.getDescription());
    _channelInfoToUpdate.setName(updEvent.getName());
    _channelInfoToUpdate.setTptProtocolType(updEvent.getProtocolType());
    _channelInfoToUpdate.setFlowControlInfo((FlowControlInfo)FlowControlInfo.convertMapToEntity(
    FlowControlInfoEntityFieldId.getEntityFieldID(),updEvent.getFlowControlProfile()));
    return _channelInfoToUpdate;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getChannelInfo(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updateChannelInfo((ChannelInfo)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateChannelInfoEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateChannelInfoEvent updEvent = (UpdateChannelInfoEvent)event;
    _channelInfoToUpdate = verifyValidChannelInfo(updEvent);
  }

  private ChannelInfo verifyValidChannelInfo(UpdateChannelInfoEvent updEvent) throws Exception
  {
    try
    {
      ChannelInfo channelInfo = (ChannelInfo) getManager().getChannelInfo(updEvent.getUId());
      CommInfo commInfo = getCommInfo(updEvent.getTptCommInfo());
      if (commInfo.getProtocolType().equals(updEvent.getProtocolType())) {
      	channelInfo.setTptCommInfo(commInfo);
      	channelInfo.setPackagingProfile(getPackagingProfile(updEvent.getPackagingProfile()));
      	channelInfo.setSecurityProfile(getSecurityProfile(updEvent.getSecurityProfile()));
        return channelInfo;
      }
      else
        throw new Exception("Type not the same: DB commInfo was of type " + commInfo.getProtocolType() +
              ", but specified was " + updEvent.getProtocolType());
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid ChannelInfo specified!");
    }
  }

  private CommInfo getCommInfo(Long commInfoUId) throws Exception
  {
    try
    {
      return getManager().getCommInfo(commInfoUId);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid CommInfo specified!");
    }
  }

  private PackagingInfo getPackagingProfile(Long packInfoUID) throws Exception
  {
    try
    {
      return getManager().getPackagingInfo(packInfoUID);
    }
    catch(FindEntityException ex)
    {
      throw new Exception("Invalid PackagingInfo Specified!");
    }
  }

  private SecurityInfo getSecurityProfile(Long securityInfoUID) throws Exception
  {
    try
    {
      return getManager().getSecurityInfo(securityInfoUID);
    }
    catch(FindEntityException ex)
    {
      throw new Exception("Invalid SecurityInfo Specified!");
    }
  }

  private IChannelManagerObj getManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           IChannelManagerHome.class.getName(),
           IChannelManagerHome.class,
           new Object[0]
           );
  }

}