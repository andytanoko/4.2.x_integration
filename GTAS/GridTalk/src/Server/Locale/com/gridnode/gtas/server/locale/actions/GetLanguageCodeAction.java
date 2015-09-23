/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetLanguageCodeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.locale.helpers.LocaleUtil;
import com.gridnode.gtas.events.locale.GetLanguageCodeEvent;

import com.gridnode.pdip.base.locale.model.LanguageCode;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one LanguageCode.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetLanguageCodeAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 355374006777054384L;
	public static final String ACTION_NAME = "GetLanguageCodeAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return LocaleUtil.convertLangCodeToMap((LanguageCode)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetLanguageCodeEvent getEvent = (GetLanguageCodeEvent)event;

    return LocaleUtil.findLangCodeByAlpha2Code(getEvent.getAlpha2Code());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetLanguageCodeEvent getEvent = (GetLanguageCodeEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getAlpha2Code()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetLanguageCodeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}