/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePartnerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractCreateEntityAction.
 * Mar 28 2006    Neo Sok Lay         Add checking for maxActivePartners
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.CreatePartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;
import com.gridnode.gtas.server.partner.exceptions.MaximumActivePartnersReachedException;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new Partner.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class CreatePartnerAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7930397535363608867L;
	private static final String ACTION_NAME = "CreatePartnerAction";
	
  protected Map convertToMap(AbstractEntity entity)
	{
		return Partner.convertToMap(entity, PartnerEntityFieldID.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		IPartnerManagerObj manager = getManager();
		Partner partner = (Partner)entity;
		if (partner.getState() == Partner.STATE_ENABLED)
		{
			int activeBal = manager.getActivePartnersBalCount(null);
			if (activeBal <= 0)
			{
				//throw error
				throw new MaximumActivePartnersReachedException("Maximum active partners allowed reached the maximum licensed. Not allowed to activate more partners.");
			}
		}
		return manager.createPartner(partner);
		//return getManager().createPartner((Partner)entity);
	}

	
	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
	 */
	@Override
	protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
	{
		if (ex instanceof MaximumActivePartnersReachedException)
		{
	    return constructEventResponse(IErrorCode.CREATE_PARTNER_MAX_ACTIVE_REACHED,
	                                  getErrorMessageParams(event),
	                                  ex);
		}
		else
		{
			return super.constructErrorResponse(event, ex);
		}
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {Partner.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreatePartnerEvent createEvent = (CreatePartnerEvent)event;
		
		Partner partner = new Partner();
		PartnerType  partnerType  = new PartnerType();
		PartnerGroup partnerGroup = new PartnerGroup();

		partner.setPartnerID(createEvent.getPartnerID());
		partner.setName(createEvent.getName());
		partner.setDescription(createEvent.getDescription());
		
		partnerType.setUId(createEvent.getPartnerTypeUID().longValue());
		
		// handle creating of partner without partner group
		Long uID = createEvent.getPartnerGroupUID();
		if (uID != null)
			partnerGroup.setUId(uID.longValue());
		else
			partnerGroup = null;
		
		partner.setPartnerType(partnerType);
		partner.setPartnerGroup(partnerGroup);
		partner.setState(createEvent.isEnabled() ? Partner.STATE_ENABLED : Partner.STATE_DISABLED);
		partner.setCreateBy(getUserID());
		
		return partner;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findPartner(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreatePartnerEvent.class;
	}

  private IPartnerManagerObj getManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IPartnerManagerHome.class.getName(),
               IPartnerManagerHome.class,
               new Object[0]);
  }
}