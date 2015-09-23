/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 26 02      Neo Sok Lay         Created
 * Jan 07 2003    Neo Sok Lay         Send business entities to GridNodes.
 * Jan 07 2004    Neo Sok Lay         Remove passing of RouteChannelUID.
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.SendBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Collection;

/**
 * This Action class processes a SendBusinessEntityEvent to share one
 * or more BusinessEntity(s) to a partner enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I4
 */
public class SendBusinessEntityAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7278945471756771823L;

	public static final String ACTION_NAME = "SendBusinessEntityAction";

  private String[] _toEnterpriseIDs;
  private Long[]   _viaChannelUIDs;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return SendBusinessEntityEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    SendBusinessEntityEvent setEvent = (SendBusinessEntityEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        setEvent.getBeUIDs(),
                        setEvent.getPartnerGnUIDs(),
                        //setEvent.getToEnterpriseID(),
                        //setEvent.getViaChannelUID(),
                      };

    return constructEventResponse(
             IErrorCode.SEND_ENTITY_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SendBusinessEntityEvent setEvent = (SendBusinessEntityEvent) event;

    for (int i=0; i<_toEnterpriseIDs.length; i++)
    {
      Collection sharedResUIDs =
        ServiceLookupHelper.getSharedResourceMgr().shareResourceIfNotShared(
          IResourceTypes.BUSINESS_ENTITY,
          setEvent.getBeUIDs(),
          _toEnterpriseIDs[i]);

      ServiceLookupHelper.getSharedResourceMgr().synchronizeSharedResources(
        sharedResUIDs, _viaChannelUIDs[i]);
    }

    return constructEventResponse();
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    SendBusinessEntityEvent setEvent = (SendBusinessEntityEvent) event;

    ActionHelper.verifyOwnBusinessEntity(setEvent.getBeUIDs());

    //not verifying for mock scenario

    //for actual implementation, should verify list of enterprises
    /*if (setEvent.getPartnerGnUIDs() != null)
    {*/
      _toEnterpriseIDs = ActionHelper.getPartnerGridNodeIDs(setEvent.getPartnerGnUIDs());
      _viaChannelUIDs  = ActionHelper.getMasterChannelUIDs(_toEnterpriseIDs);
    /*}
    else
    {
      _toEnterpriseIDs = new String[]
                         {
                           setEvent.getToEnterpriseID()
                         };

      _viaChannelUIDs = new Long[]
                        {
                          setEvent.getViaChannelUID()
                        };
    }*/
  }

  // ******************** Own methods ***********************************

}