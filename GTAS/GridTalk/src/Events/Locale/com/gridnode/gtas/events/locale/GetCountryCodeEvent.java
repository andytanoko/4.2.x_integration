/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCountryCodeEvent.java
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
 * This Event class contains the data for retrieve a CountryCode based on
 * Alpha-3 Code.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetCountryCodeEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6757163860621968410L;
	public static final String ALPHA_3_CODE  = "Alpha-3 Code";

  public GetCountryCodeEvent(String alpha3Code)
    throws EventException
  {
    checkSetString(ALPHA_3_CODE, alpha3Code);
  }

  public String getAlpha3Code()
  {
    return (String)getEventData(ALPHA_3_CODE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetCountryCodeEvent";
  }

}