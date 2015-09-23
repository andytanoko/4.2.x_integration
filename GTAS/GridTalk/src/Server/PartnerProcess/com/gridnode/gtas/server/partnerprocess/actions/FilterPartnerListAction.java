/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FilterPartnerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.events.partnerprocess.FilterPartnerListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.helpers.Logger;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a FilterPartnerListEvent and returns
 * the Partner(s) that satisfy the condition specified in the event.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class FilterPartnerListAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4290053913304755015L;
	public static final String ACTION_NAME = "FilterPartnerListAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return FilterPartnerListEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    FilterPartnerListEvent getEvent = (FilterPartnerListEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        getEvent.getCondition(),
                      };

    return constructEventResponse(
             IErrorCode.FILTER_HELPER_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    FilterPartnerListEvent getEvent = (FilterPartnerListEvent)event;

    Collection pnList = null;

    if (getEvent.getCondition().equals(getEvent.NO_CERT_MAPPING))
    {
      pnList = getPartners(getPartnerIdsHaveMapping(), false);
    }
    else //if (getEvent.getCondition().equals(getEvent.HAVE_CERT_MAPPING))
    {
      pnList = getPartners(getPartnerIdsHaveMapping(), true);
    }

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertPartnersToMapObjects(pnList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************** Own methods ***********************************

  /**
   * Get the Partner Ids of the Partners that have BizCertMapping.
   *
   * @return Collection of String (PartnerId).
   */
  protected Collection getPartnerIdsHaveMapping()
  {
    ArrayList list = new ArrayList();
    try
    {
      Collection mappings = ActionHelper.getManager().findBizCertMappingByFilter(null);
      for (Iterator i=mappings.iterator(); i.hasNext(); )
      {
        BizCertMapping mapping = (BizCertMapping)i.next();
        list.add(mapping.getPartnerID());
      }

    }
    catch (Exception ex)
    {
      Logger.warn("[FilterPartnerListAction.getPartnerIdHaveMapping] Error ", ex);
    }

    return list;
  }

  /**
   * Get the Partners having or not having the Partner Id specified in
   * <code>partnerIds</code>.
   *
   * @param partnerIds Collection of String(partnerId)
   * @param contain <b>true</b> to get the partners having Partner Id specified
   * in partnerIds, <b>false</b> to get the partners having Partner Id not
   * specified in partnerIds.
   * @return Collection of Partner(s).
   */
  protected Collection getPartners(Collection partnerIds, boolean contain)
  {
    Collection partners = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();

      if (!contain || partnerIds.size() > 0)
      {
        if (partnerIds.size() > 0)
          filter.addDomainFilter(null, Partner.PARTNER_ID,
            partnerIds, !contain);
        else
        {
          filter = null;
        }

        partners = ActionHelper.getPartnerManager().findPartner(filter);
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[FilterPartnerListAction.getPartners] Error ", ex);
    }

    if (partners == null)
      partners = new ArrayList();

    return partners;
  }

}