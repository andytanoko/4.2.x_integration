/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMethodsFromClassEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 07 2003    Koh Han Sing            Created.
 */
package com.gridnode.gtas.events.userprocedure;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event class contains the name of the class to retrieve the methods
 * from.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */

public class GetMethodsFromClassEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 581061398704282029L;
	public static final String CLASS_NAME = "Class Name";

  public GetMethodsFromClassEvent(String className) throws EventException
  {
    checkSetString(CLASS_NAME, className);
  }

  public String getClassName()
  {
    return (String) getEventData(CLASS_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMethodsFromClassEvent";
  }
}