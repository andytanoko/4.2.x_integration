/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityListForUserAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.SetBizEntityListForUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Collection;

/**
 * This Action class processes a SetBizEntityListForUserEvent to update
 * the latest attachments to BusinessEntity(s) for a User.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SetBizEntityListForUserAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8352062675070207749L;

	public static final String ACTION_NAME = "SetBizEntityListForUserAction";

  private Collection _beUIDs;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return SetBizEntityListForUserEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    SetBizEntityListForUserEvent setEvent = (SetBizEntityListForUserEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        setEvent.getUserAccountUID(),
                      };

    return constructEventResponse(
             IErrorCode.UPDATE_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SetBizEntityListForUserEvent setEvent = (SetBizEntityListForUserEvent) event;

    ActionHelper.verifyNonPartnerBusinessEntity(_beUIDs, "User");

    //get default Be uid
    Long defBe = ActionHelper.getDefaultBeUID(this.getEnterpriseID());
    //if not in _beUIDs, add in
    if (defBe != null && !_beUIDs.contains(defBe))
      _beUIDs.add(defBe);

    ServiceLookupHelper.getEnterpriseHierarchyMgr().setBizEntitiesForUser(
      setEvent.getUserAccountUID(), _beUIDs);

    Collection beList = ActionHelper.getBusinessEntities(_beUIDs);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertBEsToMapObjects(beList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    SetBizEntityListForUserEvent setEvent = (SetBizEntityListForUserEvent) event;

    ActionHelper.verifyUserAccount(setEvent.getUserAccountUID());

    //determine the existing BEs.
    _beUIDs = ActionHelper.getExistBeUIDs(setEvent.getBizEntityList());

    //throw error?
    if (!_beUIDs.containsAll(setEvent.getBizEntityList()))
      throw new Exception("Invalid BusinessEntity to attach to!");
  }

  // ******************** Own methods ***********************************

}