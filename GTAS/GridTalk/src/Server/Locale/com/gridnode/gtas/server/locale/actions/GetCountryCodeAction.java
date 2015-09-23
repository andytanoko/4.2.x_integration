/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCountryCodeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.locale.helpers.LocaleUtil;
import com.gridnode.gtas.events.locale.GetCountryCodeEvent;

import com.gridnode.pdip.base.locale.model.CountryCode;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one CountryCode.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetCountryCodeAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7842767182145183737L;
	public static final String ACTION_NAME = "GetCountryCodeAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return LocaleUtil.convertCountryCodeToMap((CountryCode)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetCountryCodeEvent getEvent = (GetCountryCodeEvent)event;

    return LocaleUtil.findCountryCodeByAlpha3Code(getEvent.getAlpha3Code());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetCountryCodeEvent getEvent = (GetCountryCodeEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getAlpha3Code()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetCountryCodeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}