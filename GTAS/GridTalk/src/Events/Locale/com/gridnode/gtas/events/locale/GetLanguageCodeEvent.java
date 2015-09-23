/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetLanguageCodeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.locale;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a LanguageCode based on
 * Alpha-2 Code.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetLanguageCodeEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7941436543617251781L;
	public static final String ALPHA_2_CODE  = "Alpha-2 Code";

  public GetLanguageCodeEvent(String alpha2Code)
    throws EventException
  {
    checkSetString(ALPHA_2_CODE, alpha2Code);
  }

  public String getAlpha2Code()
  {
    return (String)getEventData(ALPHA_2_CODE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetLanguageCodeEvent";
  }

}