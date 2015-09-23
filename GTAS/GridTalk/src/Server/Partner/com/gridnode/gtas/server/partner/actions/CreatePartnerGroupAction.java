/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePartnerGroupAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractCreateEntityAction.
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.CreatePartnerGroupEvent;
import com.gridnode.gtas.model.partner.PartnerGroupEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new Partner Group.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class CreatePartnerGroupAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2683496930672656816L;
	private static final String ACTION_NAME = "CreatePartnerGroupAction";
	
  protected Map convertToMap(AbstractEntity entity)
	{
		return PartnerGroup.convertToMap(entity,
		                                 PartnerGroupEntityFieldID.getEntityFieldID(),
		                                 null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createPartnerGroup((PartnerGroup)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {PartnerGroup.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreatePartnerGroupEvent _event = (CreatePartnerGroupEvent)event;
    PartnerGroup partnerGroup = new PartnerGroup();
    PartnerType  partnerType  = new PartnerType();
    partnerGroup.setName(_event.getName());
    partnerGroup.setDescription(_event.getDescription());
    partnerType.setUId(_event.getPartnerTypeUID().longValue());
    partnerGroup.setPartnerType(partnerType);
    return partnerGroup;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findPartnerGroup(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreatePartnerGroupEvent.class;
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