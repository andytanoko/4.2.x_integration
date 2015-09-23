/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateJMSCommInfoAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 18 2002    Goh Kan Mun             Created
 * Jul 05 2002    Goh Kan Mun             Modified - Add new fields.
 * Jul 11 2002    Goh Kan Mun             Modified - Check for Dest Type.
 * Jul 12 2002    Goh Kan Mun             Modified - Change of Class name from Add to Create.
 * Aug 06 2002    Jagadeesh               Modified - Changed constructEventResponse to include
 *                                        Map representation of the Entity
 * Oct 17 2003    Neo Sok Lay             Use CommURLValidator to validate
 *                                        the URL.
 * Sep 01 2005    Neo Sok Lay             Change to extend from AbstractCreateEntityAction                                       
 */

package com.gridnode.gtas.server.channel.actions;

import java.util.Map;

import com.gridnode.gtas.events.channel.CreateCommInfoEvent;
import com.gridnode.gtas.model.channel.CommInfoEntityFieldId;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.server.channel.helpers.CommURLValidator;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new CommInfo.
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0
 * @since 2.0
 */

public class CreateCommInfoAction extends AbstractCreateEntityAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1566090834572935861L;
	public static final String ACTION_NAME = "CreateCommInfoAction";
	

  protected Map convertToMap(AbstractEntity entity)
	{
		return CommInfo.convertToMap(entity, CommInfoEntityFieldId.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createCommInfo((CommInfo)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {CommInfo.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateCommInfoEvent _event = (CreateCommInfoEvent)event;
		CommInfo commInfo = new CommInfo();
		commInfo.setName(_event.getName());
		commInfo.setDescription(_event.getDescription());
		commInfo.setURL(_event.getURL());
		commInfo.setTptImplVersion(ICommInfo.DEFAULT_TPTIMPL_VERSION);
		commInfo.setProtocolType(_event.getProtocolType());
		commInfo.setIsPartner((_event.isPartner()).booleanValue());
		if (_event.isPartner().booleanValue())
			// can only create for partner of "Other" category.
			commInfo.setPartnerCategory(ICommInfo.CATEGORY_OTHERS);
		else
			commInfo.setRefId(getEnterpriseID());
		
		return commInfo;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getCommInfo(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateCommInfoEvent.class;
	}

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  	CreateCommInfoEvent _event = (CreateCommInfoEvent) event;
    /*031017NSL
    //Check for a valid URL.  Some thing like
   if(!CommInfo.isValidURL(_event.getURL()))
      throw new EventException("Invalid URL: Please Provide the URL In "+
      ICommInfo.URL_FORMAT);
    */
    CommURLValidator.validateURL(_event.getProtocolType(), _event.getURL());  
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



