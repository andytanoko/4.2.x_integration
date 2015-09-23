/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveDomainIdentifiersActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 14 Dec 2005		SC									Created
 * 16 Dec 2005		SC									Added isStringEquals method and fix bug in 
 * 																		getBusinessEntityByDunsNumber(IBizRegistryManagerObj manager, String sId, String eId, String sourceType)
 */
package com.gridnode.gtas.server.bizreg.helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gridnode.gtas.events.bizreg.DomainIdentifiersConstants;
import com.gridnode.gtas.events.bizreg.RetrieveDomainIdentifiersEvent;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.DomainIdentifier;
import com.gridnode.pdip.app.bizreg.model.IBusinessEntity;
import com.gridnode.pdip.app.bizreg.model.IWhitePage;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.AssertUtil;
import com.gridnode.pdip.framework.util.StringUtil;

public class RetrieveDomainIdentifiersActionHelper
{
	private HashMap map;
	
	private void createResultMap(String[] targetIdentifers, String[] enterpriseIds)
	{
		AssertUtil.assertTrue(targetIdentifers != null && enterpriseIds != null && targetIdentifers.length == enterpriseIds.length);
		map = new HashMap();
		map.put(DomainIdentifiersConstants.RESULT_TARGET_IDENTIFIERS, targetIdentifers);
		map.put(DomainIdentifiersConstants.RESULT_ENTERPRISE_IDENTIFIERS, enterpriseIds);
	}
	
	public HashMap getResultMap()
	{
		return map;
	}
	
	/**
	 * This method implements the algo specified in RetrieveDomainIdentifiers doc from Sok Lay.
	 */
	public RetrieveDomainIdentifiersActionHelper(IEvent event) throws ServiceLookupException, SystemException, FindEntityException, java.rmi.RemoteException
	{
		RetrieveDomainIdentifiersEvent retrieveEvent = (RetrieveDomainIdentifiersEvent) event;
		String[] sourceIdentifiers = retrieveEvent.getSourceIdentifiers();
		String sourceType = retrieveEvent.getSoureType();
		String targetType = retrieveEvent.getTargetType();
		String[] enterpriseIds = retrieveEvent.getEnterpriseIds();
		
		if (sourceType.equals(targetType))
		{
			createResultMap(sourceIdentifiers, enterpriseIds);
			return;
		}
		
		IBizRegistryManagerObj manager = ActionHelper.getBizRegManager();
		int length = sourceIdentifiers.length;
		String[] retTargetIdentifiers = new String[length];
		String[] retEnterpriseIds = new String[length];
		for (int i = 0; i < length; i++)
		{
			String sId = sourceIdentifiers[i];
			String eId = enterpriseIds[i];
			BusinessEntity entity = null;
			
			if (sourceType.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_ID))
			{
				log("SOURCE: BUSINESS_ENTITY_ID");
				entity = manager.findBusinessEntity(eId, sId);
				if (entity != null && isDeletedState(entity))
				{
					entity = null;
				}
			} else if (sourceType.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_UID))
			{
				log("SOURCE: BUSINESS_ENTITY_UID");
				entity = manager.findBusinessEntity(new Long(sId));
				if (entity != null && isDeletedState(entity))
				{
					entity = null;
				}
			} else if (sourceType.equals(DomainIdentifiersConstants.DUNS_NUMBER))
			{
				log("SOURCE: DUNS_NUMBER");
				entity = getBusinessEntityByDunsNumber(manager, sId, eId, sourceType);
			} else if (isInTheseType(sourceType))
			{
				log("SOURCE: in these type");
				entity = manager.findBusinessEntityByDomainIdentifier(sourceType, sId);
			} else
			{
				entity = null;
			}
			
			retTargetIdentifiers[i] = getTargetIdentifier(targetType, entity);
			retEnterpriseIds[i] = (entity == null) ? null : entity.getEnterpriseId();
		}
		
		createResultMap(retTargetIdentifiers, retEnterpriseIds);
	}
	
	/**
	 * This method implements the algo specified in RetrieveDomainIdentifiers doc from Sok Lay: step 7 & step 8 where they determine targetIdentifier.
	 */
	private String getTargetIdentifier(String targetType, BusinessEntity entity)
	{
		if (entity == null)
		{
			return null;
		}
		
		if (targetType.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_ID))
		{
			log("TARGET: BUSINESS_ENTITY_ID");
			return entity.getBusEntId();
		} else if (targetType.equals(DomainIdentifiersConstants.BUSINESS_ENTITY_UID))
		{
			log("TARGET: BUSINESS_ENTITY_UID");
			return String.valueOf(entity.getUId());
		} else if (targetType.equals(DomainIdentifiersConstants.DUNS_NUMBER))
		{
			log("TARGET: DUNS_NUMBER");
			WhitePage whitePage = entity.getWhitePage();
			String duns = whitePage.getDUNS();
			if (StringUtil.isNotEmpty(duns))
			{
				return duns;
			} else
			{
				log("TARGET: DUNS - domain id");
				return getValueFromDomainIdentifier(entity, DomainIdentifiersConstants.DUNS_NUMBER);
			}
		} else if (isInTheseType(targetType))
		{
			log("TARGET: in these type");
			return getValueFromDomainIdentifier(entity, targetType);
		}
		
		AssertUtil.assertTrue(false, "Cannot reach here");
		return null;
	}
	
	private boolean isDeletedState(BusinessEntity entity)
	{
		return entity.getState() == IBusinessEntity.STATE_DELETED;
	}
	
	private String getValueFromDomainIdentifier(BusinessEntity entity, String type)
	{
		Collection c = entity.getDomainIdentifiers();
		Iterator it = c.iterator();
		while (it.hasNext())
		{
			DomainIdentifier di = (DomainIdentifier) it.next();
			if (di.getType().equals(type))
			{
				return di.getValue();
			}
		}
		return null;
	}
	
	/**
	 * This method implements the algo specified in RetrieveDomainIdentifiers doc from Sok Lay: step 4.
	 */
	private BusinessEntity getBusinessEntityByDunsNumber(IBizRegistryManagerObj manager, String sId, String eId, String sourceType) 
			throws SystemException, FindEntityException, java.rmi.RemoteException
	{
		IDataFilter filter = new DataFilterImpl();
		FilterOperator equalOperator = filter.getEqualOperator();
		filter.addSingleFilter(null, IWhitePage.DUNS, equalOperator, sId, false);
		Collection c = manager.findBusinessEntitiesByWhitePage(filter);
		Iterator it = c.iterator();
		if (c.size() == 1)
		{
			return (BusinessEntity) it.next();
		}
		while (it.hasNext())
		{
			BusinessEntity temp = (BusinessEntity) it.next();
			String enterpriseId = temp.getEnterpriseId();
			if (isStringEquals(eId,enterpriseId))
			{
				return temp;
			}
		}
		log("SOURCE: DUNS - domain id");
		return manager.findBusinessEntityByDomainIdentifier(sourceType, sId);
	}
	
	/**
	 * This method check if a is equals to b.  It also handle the case where a and b are null.
	 */
	private boolean isStringEquals(String a, String b)
	{
		return (a != null && a.equals(b)) || (a == null && b == null);
	}
	
	private boolean isInTheseType(String type)
	{
		return type.equals(DomainIdentifiersConstants.STARFISH_ID)
			|| type.equals(DomainIdentifiersConstants.AS2_IDENTIFIER)
			|| type.equals(DomainIdentifiersConstants.DISCOVERY_URL)
			|| type.equals(DomainIdentifiersConstants.GLOBAL_LOCATION_NUMBER);
	}
	
	private void log(String message)
	{
		Logger.log("[RetrieveDomainIdentifiersActionHelper] " + message);
	}
}
