/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityForPartnerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.SetBizEntityForPartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a SetBizEntityForPartnerEvent to update
 * the latest attachments to BusinessEntity and ChannelInfo for a Partner.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SetBizEntityForPartnerAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3858420080197656046L;
	public static final String ACTION_NAME = "SetBizEntityForPartnerAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return SetBizEntityForPartnerEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    SetBizEntityForPartnerEvent setEvent = (SetBizEntityForPartnerEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        setEvent.getPartnerUID(),
                        setEvent.getBizEntityUID(),
                        setEvent.getChannelUID(),
                      };

    return constructEventResponse(
             IErrorCode.UPDATE_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SetBizEntityForPartnerEvent setEvent = (SetBizEntityForPartnerEvent) event;

    if (setEvent.getBizEntityUID() != null)
      ActionHelper.verifyChannelAssignedToBe(
        setEvent.getBizEntityUID(), setEvent.getChannelUID());

    ServiceLookupHelper.getEnterpriseHierarchyMgr().setBizEntityForPartner(
      setEvent.getPartnerUID(), setEvent.getBizEntityUID());

    ServiceLookupHelper.getEnterpriseHierarchyMgr().setChannelForPartner(
      setEvent.getPartnerUID(), setEvent.getChannelUID());

//    Collection beList = ActionHelper.getBusinessEntities(_beUIDs);

    return constructEventResponse(/**new EntityListResponseData(
             ActionHelper.convertBEsToMapObjects(beList))*/);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    SetBizEntityForPartnerEvent setEvent = (SetBizEntityForPartnerEvent) event;

    ActionHelper.verifyPartner(setEvent.getPartnerUID());
    if (setEvent.getBizEntityUID() != null)
    {
      ActionHelper.verifyBusinessEntity(setEvent.getBizEntityUID());
      ActionHelper.verifyChannelInfo(setEvent.getChannelUID());
    }
  }

  // ******************** Own methods ***********************************

}