/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FilterOwnCertListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 13 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.events.partnerprocess.FilterOwnCertListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a FilterOwnCertListEvent and returns
 * the Certificate(s) that belong to this Enterprise.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class FilterOwnCertListAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3624592382175605517L;
	public static final String ACTION_NAME = "FilterOwnCertListAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return FilterOwnCertListEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    FilterOwnCertListEvent getEvent = (FilterOwnCertListEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(
             IErrorCode.FILTER_HELPER_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    FilterOwnCertListEvent getEvent = (FilterOwnCertListEvent)event;

    String enterpriseId = getEnterpriseID();

    ArrayList values = new ArrayList();
    values.add(new Integer("0"));
    values.add(new Integer(enterpriseId));

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, Certificate.ID, values, false);
    filter.addSingleFilter(filter.getAndConnector(), Certificate.IS_PARTNER,
      filter.getEqualOperator(), Boolean.FALSE, false);
    filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID,
      filter.getEqualOperator(), new Integer(0), false);

    Collection certs = ActionHelper.findCertificates(filter);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertCertsToMapObjects(certs)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************** Own methods ***********************************

}