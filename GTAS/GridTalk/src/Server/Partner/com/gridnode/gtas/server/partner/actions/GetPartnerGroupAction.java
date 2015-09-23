/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerGroupAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction.
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.GetPartnerGroupEvent;
import com.gridnode.gtas.model.partner.PartnerGroupEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This Action class handles the retrieving of a Partner Group.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class GetPartnerGroupAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1528228452966699624L;
	private static final String ACTION_NAME = "GetPartnerGroupAction";
	
  protected Map convertToMap(AbstractEntity entity)
	{
		return PartnerGroup.convertToMap(entity,
		                                 PartnerGroupEntityFieldID.getEntityFieldID(),
		                                 null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetPartnerGroupEvent _event = (GetPartnerGroupEvent)event;
		return getManager().findPartnerGroup(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetPartnerGroupEvent _event = (GetPartnerGroupEvent)event;

		return new Object[] {PartnerGroup.ENTITY_NAME, _event.getUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetPartnerGroupEvent.class;
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