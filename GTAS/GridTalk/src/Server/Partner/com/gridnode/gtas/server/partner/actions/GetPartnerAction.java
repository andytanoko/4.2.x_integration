/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Ang Meng Hua        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 * Dec 12 2005    Neo Sok Lay         To support get partner by partner id.
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Map;

import com.gridnode.gtas.events.partner.GetPartnerEvent;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a Partner.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.2
 */
public class GetPartnerAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8407869088994264858L;
	private static final String ACTION_NAME = "GetPartnerAction";
	
  protected Map convertToMap(AbstractEntity entity)
	{
		return Partner.convertToMap(entity,
		                            PartnerEntityFieldID.getEntityFieldID(),
		                            null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetPartnerEvent _event = (GetPartnerEvent)event;
		if (_event.getUID() == null)
		{
			return getManager().findPartnerByID(_event.getPartnerID());
		}
		return getManager().findPartner(_event.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetPartnerEvent _event = (GetPartnerEvent)event;
		Object getKey = _event.getUID()==null? _event.getPartnerID() : _event.getUID();
		return new Object[] {Partner.ENTITY_NAME, getKey};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetPartnerEvent.class;
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