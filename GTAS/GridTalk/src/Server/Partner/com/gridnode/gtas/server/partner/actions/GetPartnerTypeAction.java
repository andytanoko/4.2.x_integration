/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.GetPartnerTypeEvent;
import com.gridnode.gtas.model.partner.PartnerTypeEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This Action class handles the retrieving of a Partner Type.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class GetPartnerTypeAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7510160126917261163L;
	private static final String ACTION_NAME = "GetPartnerTypeAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return PartnerType.convertToMap(entity,
		                                PartnerTypeEntityFieldID.getEntityFieldID(),
		                                null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetPartnerTypeEvent _event = (GetPartnerTypeEvent)event;
		return getManager().findPartnerType(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetPartnerTypeEvent _event = (GetPartnerTypeEvent)event;
		return new Object[] {PartnerType.ENTITY_NAME, _event.getUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetPartnerTypeEvent.class;
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