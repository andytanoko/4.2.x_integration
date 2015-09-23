/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMethodsFromWSDLEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 24 2003    Koh Han Sing            Created.
 */
package com.gridnode.gtas.events.userprocedure;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event class contains the name of the WSDL to retrieve the methods
 * from.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */

public class GetMethodsFromWSDLEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4523592387959665134L;
	public static final String PROCEDURE_DEF_UID = "Procedure Def Uid";

  public GetMethodsFromWSDLEvent(Long procedureDefUid) throws EventException
  {
    checkSetLong(PROCEDURE_DEF_UID, procedureDefUid);
  }

  public Long getProcedureDefUid()
  {
    return (Long) getEventData(PROCEDURE_DEF_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMethodsFromWSDLEvent";
  }
}