/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityForPartnerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.events.enterprise.GetBizEntityForPartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a GetBizEntityForPartnerEvent to obtain
 * the BusinessEntity that a Partner is attached under, and the ChannelInfo
 * that is assigned to the Partner.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBizEntityForPartnerAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7557133544523428228L;
	public static final String ACTION_NAME = "GetBizEntityForPartnerAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetBizEntityForPartnerEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetBizEntityForPartnerEvent getEvent = (GetBizEntityForPartnerEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        getEvent.getPartnerUID(),
                      };

    return constructEventResponse(
             IErrorCode.FIND_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetBizEntityForPartnerEvent getEvent = (GetBizEntityForPartnerEvent) event;

    BusinessEntity be = ActionHelper.getBeAttachedToPartner(getEvent.getPartnerUID());
    ChannelInfo channel = ActionHelper.getChannelAttachedToPartner(getEvent.getPartnerUID());

    Collection results = new ArrayList();
    results.add(ActionHelper.convertBusinessEntityToMap(be));
    results.add(ActionHelper.convertChannelInfoToMap(channel));

    return constructEventResponse(results);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    GetBizEntityForPartnerEvent getEvent = (GetBizEntityForPartnerEvent) event;

    ActionHelper.verifyPartner(getEvent.getPartnerUID());
  }

  // ******************** Own methods ***********************************

}