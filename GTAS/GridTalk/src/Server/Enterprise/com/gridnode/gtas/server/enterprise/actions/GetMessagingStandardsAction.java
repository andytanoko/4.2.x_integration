/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMessagingStandardsAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         getFingerprintTypes() refactored to Namespace
 *                                    class.   
 * Sep 23 2003    Neo Sok Lay         Change getFingerprintTypes() to getFingerprintValues() 
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.GetMessagingStandardsEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.bizreg.model.Namespace;
import com.gridnode.gtas.server.enterprise.helpers.IMessagingStandardKeys;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This action handles the GetMessagingStandardsEvent.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class GetMessagingStandardsAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5490365386872080895L;
	private static final String ACTION_NAME = "GetMessagingStandardsAction";
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    Object[] params = new Object[]
                      {

                      };

    return constructEventResponse(
             IErrorCode.GET_MSG_STDS_ERROR,
             params,
             ex);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(com.gridnode.pdip.framework.rpf.event.IEvent)
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    Namespace namespace = PublishDelegate.getTechnicalSpecifications().findNamespace(
                            IMessagingStandardKeys.NAMESPACE_TYPE);

    return constructEventResponse(namespace.getFingerprintValues());
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   */
  protected Class getExpectedEventClass()
  {
    return GetMessagingStandardsEvent.class;
  }

  // *************** Own methods ****************************
}
