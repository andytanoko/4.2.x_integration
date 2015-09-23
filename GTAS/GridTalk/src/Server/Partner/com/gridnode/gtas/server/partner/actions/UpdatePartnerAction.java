/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 * Mar 28 2006    Neo Sok Lay         Add checking for max active partners
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.UpdatePartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;
import com.gridnode.gtas.server.partner.exceptions.MaximumActivePartnersReachedException;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This Action class handles the update of a Partner.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class UpdatePartnerAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8775231974321739612L;
	private static final String ACTION_NAME = "UpdatePartnerAction";

	private Partner _partnerToUpd;
	
  protected Map convertToMap(AbstractEntity entity)
	{
		return Partner.convertToMap(entity,
		                            PartnerEntityFieldID.getEntityFieldID(),
		                            null);
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdatePartnerEvent _event = (UpdatePartnerEvent)event;
		_partnerToUpd = getManager().findPartner(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdatePartnerEvent _event = (UpdatePartnerEvent)event;
		return new Object[] {Partner.ENTITY_NAME, _event.getUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdatePartnerEvent _event = (UpdatePartnerEvent)event;
    PartnerType  partnerType  = new PartnerType();
    PartnerGroup partnerGroup = new PartnerGroup();

    _partnerToUpd.setName(_event.getName());
    _partnerToUpd.setDescription(_event.getDescription());
    partnerType.setUId(_event.getPartnerTypeUID().longValue());

    // handle creating of partner without partner group
    Long uID = _event.getPartnerGroupUID();
    if (uID != null)
      partnerGroup.setUId(uID.longValue());
    else
      partnerGroup = null;

    _partnerToUpd.setPartnerType(partnerType);
    _partnerToUpd.setPartnerGroup(partnerGroup);
    _partnerToUpd.setState(_event.isEnabled()?
                     Partner.STATE_ENABLED : Partner.STATE_DISABLED);
    return _partnerToUpd;

	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return  getManager().findPartner(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		IPartnerManagerObj manager = getManager();
		Partner partner = (Partner)entity;
		if (partner.getState() == Partner.STATE_ENABLED)
		{
			int activeBal = manager.getActivePartnersBalCount(partner.getPartnerID());
			if (activeBal <= 0)
			{
				//throw error
				throw new MaximumActivePartnersReachedException("Maximum active partners allowed reached the maximum licensed. Not allowed to activate more partners.");
			}
		}
		manager.updatePartner(partner);
		//getManager().updatePartner((Partner)entity);
	}

	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
	 */
	@Override
	protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
	{
		if (ex instanceof MaximumActivePartnersReachedException)
		{
	    return constructEventResponse(IErrorCode.UPDATE_PARTNER_MAX_ACTIVE_REACHED,
	                                  getErrorMessageParams(event),
	                                  ex);
		}
		else
		{
			return super.constructErrorResponse(event, ex);
		}
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdatePartnerEvent.class;
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