/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivateBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-07-17     Teh Yu Phei         Created (Ticket 31)
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for activate deleted Business Entities.
 *
 * @author Teh Yu Phei
 *
 * @version 
 * @since 
 */
public class ActivateBusinessEntityEvent
  extends    EventSupport
  implements IGuardedEvent
{
	private static final long serialVersionUID = -726696737525353294L;
	
	public static final String GUARDED_FEATURE = "ENTERPRISE";
	public static final String GUARDED_ACTION  = "ActivateBusinessEntity";
	public static final String BE_ID = "BE ID";
	
  /**
   * Construct a ActivateBusinessEntityEvent to change the selected BEs' state to 0.
   */
  public ActivateBusinessEntityEvent(long[] bizEntities) throws EventException
  {
	  setEventData(BE_ID, bizEntities);
  }
  
  public long[] getBEIDs()
  {
    return (long[])getEventData(BE_ID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/ActivateBusinessEntityEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

}