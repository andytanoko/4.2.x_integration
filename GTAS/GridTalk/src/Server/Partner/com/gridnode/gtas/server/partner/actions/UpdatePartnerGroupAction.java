/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerGroupAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2007    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.UpdatePartnerGroupEvent;
import com.gridnode.gtas.model.partner.PartnerGroupEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the update of a Partner Group.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class UpdatePartnerGroupAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 522884280517191645L;
	private static final String ACTION_NAME = "UpdatePartnerGroupAction";
	
	private PartnerGroup _partnerGrpToUpd;

  protected Map convertToMap(AbstractEntity entity)
	{
		return PartnerGroup.convertToMap(entity,
		                                 PartnerGroupEntityFieldID.getEntityFieldID(),
		                                 null);
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdatePartnerGroupEvent _event = (UpdatePartnerGroupEvent)event;
		_partnerGrpToUpd = getManager().findPartnerGroup(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdatePartnerGroupEvent _event = (UpdatePartnerGroupEvent)event;
		return new Object[] {PartnerGroup.ENTITY_NAME, _event.getUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdatePartnerGroupEvent _event = (UpdatePartnerGroupEvent)event;
		_partnerGrpToUpd.setName(_event.getName());
    _partnerGrpToUpd.setDescription(_event.getDescription());

		return _partnerGrpToUpd;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findPartnerGroup(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updatePartnerGroup((PartnerGroup)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdatePartnerGroupEvent.class;
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