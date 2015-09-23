/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: RetrieveDomainIdentifiersAction.java
 * 
 * ***************************************************************************
 * Date             Author Changes
 * *************************************************************************** 
 * 5 Dec 2005       SC     Created
 * 14 Dec 2005			SC		 Modify code to add support for retrival of various types of id.
 * 16 Dec 2005			SC		 Remove commented code.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.events.bizreg.DomainIdentifiersConstants;
import com.gridnode.gtas.events.bizreg.RetrieveDomainIdentifiersEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.bizreg.helpers.RetrieveDomainIdentifiersActionHelper;

import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.IBusinessEntity;
import com.gridnode.pdip.app.bizreg.model.IDomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.IWhitePage;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.AssertUtil;

import java.util.Collection;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;

public class RetrieveDomainIdentifiersAction extends AbstractGridTalkAction
{
	public static final String ACTION_NAME = "RetrieveDomainIdentifiersAction";
	
	protected IEventResponse constructErrorResponse(IEvent event,
																									TypedException ex)
	{
		Object[] params = new Object[] {};
		return constructEventResponse(IErrorCode.GENERAL_ERROR, params, ex);
	}

	protected IEventResponse doProcess(IEvent event) throws Throwable
	{
		RetrieveDomainIdentifiersActionHelper helper = new RetrieveDomainIdentifiersActionHelper(event);
		HashMap map = helper.getResultMap();
		return constructEventResponse(map);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return RetrieveDomainIdentifiersEvent.class;
	}

	private void debug(String message)
	{
		com.gridnode.gtas.server.bizreg.helpers.Logger.log("[RetrieveDomainIdentifiersAction]: " + message);
	}
}