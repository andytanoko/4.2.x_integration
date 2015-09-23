/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerListByBizEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 15 Dec 2005		SC									Created
 * 16 Dec 2005		SC									Remove commented code.
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;

import com.gridnode.gtas.events.enterprise.GetPartnerListByBizEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.AssertUtil;

public class GetPartnerListByBizEntityAction extends AbstractGridTalkAction
{
  public static final String ACTION_NAME = "GetPartnerListByBizEntityAction";
  private BusinessEntity entity = null;
  
  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetPartnerListByBizEntityEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
  	GetPartnerListByBizEntityEvent getEvent = (GetPartnerListByBizEntityEvent) event;
  	Object[] params = new Object[] { getEvent.getBusinessEntityUid(), getEvent.getBusinessEntityId(), getEvent.getEnterpriseId() };
  	return constructEventResponse(IErrorCode.FIND_ENTITY_LIST_ERROR, params, ex);  	
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
  	AssertUtil.assertTrue(entity != null);
  	long uid = entity.getUId();
  	Collection keyCollection = ActionHelper.getPartnersForBusinessEntity(new Long(uid));
  	Collection entityCollection = ActionHelper.getPartners(keyCollection);
  	return constructEventResponse(new EntityListResponseData(
  	                                                         ActionHelper.convertPartnersToMapObjects(entityCollection)));
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  	GetPartnerListByBizEntityEvent getEvent = (GetPartnerListByBizEntityEvent) event;
  	Long uid = getEvent.getBusinessEntityUid();
  	if (uid != null)
  	{
  		log("uid = " + uid);
  		entity = ActionHelper.verifyBusinessEntity(uid);
  	} else 
  	{
  		String bId = getEvent.getBusinessEntityId();
  		String eId = getEvent.getEnterpriseId();
  		log("bId = " + bId + ", eId = " + eId);
  		
  		// eId can be null.
  		AssertUtil.assertTrue(bId != null, "must specifiy (BE UID) OR (BE ID AND enterprise ID)");
  		entity = ActionHelper.verifyBusinessEntity(bId, eId);
  	}
  }

  // ******************** Own methods ***********************************

  private void log(String message)
  {
  	Logger.log("[GetPartnerListByBizEntityAction] " + message);
  }
}