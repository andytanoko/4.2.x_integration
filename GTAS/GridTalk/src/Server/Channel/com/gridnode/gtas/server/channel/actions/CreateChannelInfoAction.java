/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateChannelInfoAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 * Jul 12 2002    Goh Kan Mun             Modified - Change of Class name from Add to Create.
 * Aug 06 2002    Jagadeesh               Modified - Changed constructEventResponse to include
 *                                        Map representation of the Entity
 * Jan 17 2003    Jagadeesh               Modified - No TptProtocol type required.
 * Dec 22 2003    Jagadeesh               Modified - Added support for Embedded FlowControl Profile.
 * Sep 01 2005    Neo Sok Lay             Change to extend from AbstractCreateEntityAction.
 *
 */
package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.CreateChannelInfoEvent;
import com.gridnode.gtas.model.channel.ChannelInfoEntityFieldId;
import com.gridnode.gtas.model.channel.FlowControlInfoEntityFieldId;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new CommInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 4.0
 * @since 2.0
 */
public class CreateChannelInfoAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1614259190141048919L;
	public static final String ACTION_NAME = "CreateChannelInfoAction";
	
  private CommInfo _commInfo = null;
  private PackagingInfo _packagingInfo=null;
  private SecurityInfo _securityInfo=null;

  // ****************** AbstractGridTalkAction methods *****************
	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateChannelInfoEvent.class;
	}

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  	CreateChannelInfoEvent _event = (CreateChannelInfoEvent) event;
    _commInfo = verifyValidCommInfo(_event.getCommInfo(), _event.getTptProtocolType());
    _packagingInfo = verifyValidPackagingInfo(_event.getPackagingProfile());
    _securityInfo = verifyValidSecurityInfo(_event.getSecurityProfile());
  }
  
  // ******************* AbstractCreateEntityAction methods *************
	protected Map convertToMap(AbstractEntity entity)
	{
		return ChannelInfo.convertToMap(entity, ChannelInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createChannelInfo((ChannelInfo)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		//CreateChannelInfoEvent _event = (CreateChannelInfoEvent)event;
		return new Object[] {ChannelInfo.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateChannelInfoEvent _event = (CreateChannelInfoEvent)event;
    ChannelInfo channelInfo = new ChannelInfo();
    channelInfo.setDescription(_event.getDescription());
    channelInfo.setName(_event.getName());
    channelInfo.setTptCommInfo(_commInfo);
    channelInfo.setTptProtocolType(_event.getTptProtocolType());
    channelInfo.setIsPartner((_event.isPartner()).booleanValue());
    channelInfo.setPackagingProfile(_packagingInfo);
    channelInfo.setSecurityProfile(_securityInfo);

    if (_event.isPartner().booleanValue()) // can only create for partner of "Other" category.
      channelInfo.setPartnerCategory(IChannelInfo.CATEGORY_OTHERS);
    else
      channelInfo.setReferenceId(getEnterpriseID());

    channelInfo.setFlowControlInfo((FlowControlInfo) FlowControlInfo.convertMapToEntity(
     FlowControlInfoEntityFieldId.getEntityFieldID(),_event.getFlowControlProfile()));

    return channelInfo;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getChannelInfo(key);
	}


  // ***************************** Own Methods ****************************

  private CommInfo verifyValidCommInfo(Long commInfoUId, String protocolType) throws Exception
  {
    try
    {
      CommInfo commInfo = (CommInfo) getManager().getCommInfo(commInfoUId);
      if (commInfo.getProtocolType().equals(protocolType))
        return commInfo;
      else
        throw new Exception("Type specified not the same as the CommInfo. Specified: "
              + protocolType + ", database: " + commInfo.getProtocolType());
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid CommInfo specified!");
    }
  }


  private PackagingInfo verifyValidPackagingInfo(Long packagingInfoUId) throws Exception
  {
    try
    {
      PackagingInfo packInfo = getManager().getPackagingInfo(packagingInfoUId);
      return packInfo;
    }
    catch(FindEntityException ex)
    {
      throw new Exception("Invalid PackagingProfile specified!");
    }

  }

  private SecurityInfo verifyValidSecurityInfo(Long securityInfoUId) throws Exception
  {
    try
    {
      SecurityInfo securityInfo = getManager().getSecurityInfo(securityInfoUId);
      return securityInfo;
    }
    catch(FindEntityException ex)
    {
      throw new Exception("Invalid SecurityProfile specified!");
    }

  }

  private IChannelManagerObj getManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IChannelManagerHome.class.getName(),
               IChannelManagerHome.class,
               new Object[0]);
  }


}