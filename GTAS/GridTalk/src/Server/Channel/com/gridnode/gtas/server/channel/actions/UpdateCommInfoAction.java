/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJMSCommInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Goh Kan Mun         Created
 * Jul 11 2002    Goh Kan Mun         Modified - Check for Destination Type
 *                                             - Check for Protocol Type
 * Aug 06 2002    Jagadeesh           Modified - Changed constructEventResponse to include
 *                                    Map representation of the Entity
 * Oct 17 2003    Neo Sok Lay         Use CommURLValidator to validate the
 *                                    URL.
 * NOV 03 2003    Jagadeesh           Fix Defect ID: 14276:GT2.2 - TptImpl Version Change.	
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */

package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.UpdateCommInfoEvent;
import com.gridnode.gtas.model.channel.CommInfoEntityFieldId;
import com.gridnode.gtas.server.channel.helpers.CommURLValidator;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class UpdateCommInfoAction extends AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9179768257800344443L;

	private static final String ACTION_NAME = "UpdateCommInfoAction";

  private CommInfo _commInfo;

  protected Map convertToMap(AbstractEntity entity)
	{
		return CommInfo.convertToMap(entity, CommInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateCommInfoEvent updEvent = (UpdateCommInfoEvent)event;
		return new Object[] {CommInfo.ENTITY_NAME, updEvent.getUId()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateCommInfoEvent updEvent = (UpdateCommInfoEvent)event;
    _commInfo.setName(updEvent.getName());
    _commInfo.setDescription(updEvent.getDescription());
    _commInfo.setURL(updEvent.getURL());
    _commInfo.setProtocolType(updEvent.getProtocolType());
    return _commInfo;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getCommInfo(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updateCommInfo((CommInfo)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateCommInfoEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateCommInfoEvent updEvent = (UpdateCommInfoEvent)event;
    _commInfo = verifyValidCommInfo(updEvent);

//    //Check for valid URL.
//    if(!CommInfo.isValidURL(updEvent.getURL()))
//     throw new EventException("Invalid URL: Please Provide the URL In :"+
//     ICommInfo.URL_FORMAT);
  }

  private CommInfo verifyValidCommInfo(UpdateCommInfoEvent updEvent) throws Exception
  {
    try
    {
      /*031017NSL
      //Check for valid URL.
      if(!CommInfo.isValidURL(updEvent.getURL()))
       throw new EventException("Invalid URL: Please Provide the URL In :" + ICommInfo.URL_FORMAT);
      */
      CommURLValidator.validateURL(updEvent.getProtocolType(), updEvent.getURL());
      
      CommInfo commInfo = (CommInfo) getManager().getCommInfo(updEvent.getUId());
      return commInfo;
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid CommInfo specified!");
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

