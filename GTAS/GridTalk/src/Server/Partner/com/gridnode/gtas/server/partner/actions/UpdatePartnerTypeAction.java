/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.UpdatePartnerTypeEvent;
import com.gridnode.gtas.model.partner.PartnerTypeEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the update of a Partner Type.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0
 */
public class UpdatePartnerTypeAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1273152439805022857L;
	private static final String ACTION_NAME = "UpdatePartnerTypeAction";
	
	private PartnerType _partnerTypeToUpd;

  protected Map convertToMap(AbstractEntity entity)
	{
		return PartnerType.convertToMap(entity,
		                                PartnerTypeEntityFieldID.getEntityFieldID(),
		                                null);
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdatePartnerTypeEvent _event = (UpdatePartnerTypeEvent)event;
		_partnerTypeToUpd = getManager().findPartnerType(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdatePartnerTypeEvent _event = (UpdatePartnerTypeEvent)event;
		return new Object[] {PartnerType.ENTITY_NAME, _event.getUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdatePartnerTypeEvent _event = (UpdatePartnerTypeEvent)event;
		_partnerTypeToUpd.setDescription(_event.getDescription());
    return _partnerTypeToUpd;

	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findPartnerType(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updatePartnerType((PartnerType)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdatePartnerTypeEvent.class;
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