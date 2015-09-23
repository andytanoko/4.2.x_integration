/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUtcTimeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import com.gridnode.gtas.server.locale.listener.ejb.UtcTimeServer;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.events.locale.GetUtcTimeEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This action handles retrieval of Utc time offset from local time.<p>
 * This Action returns:<p>
 * <ol>
 * <li>The "Accurate" Offset (Long) of Utc time to the Local time. Meaning, this is the
 * offset you need to add to the Local time to get the UTC time.
 * If the Offset is positive, the Local time is that number of milliseconds behind
 * the Utc time. If the Offset is negative, the Local time is that number
 * is milliseconds ahead of the Utc time.</li>
 * <li>The default TimeZone Offset as determined locally. This is the offset you
 * need to add to UTC to get Local time. This figure should be roughly the negation
 * of the Accurate" Offset with some minutes or seconds difference.</li>
 * </ol>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetUtcTimeAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7161232763592357582L;
	public static final String ACTION_NAME = "GetUtcTimeAction";

  public GetUtcTimeAction()
  {
  }

  protected Class getExpectedEventClass()
  {
    return GetUtcTimeEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(
             IErrorCode.GET_UTC_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    long utcOffset = UtcTimeServer.getInstance().getUtcOffset();
    long timezoneOffset = UtcTimeServer.getInstance().getDefaultTimeZoneOffset();

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             new Object[]
             {
              new Long(utcOffset),
              new Long(timezoneOffset)
             });

  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // do not require user authentication
  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(false);
  }

}