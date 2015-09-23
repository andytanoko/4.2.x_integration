
package com.gridnode.gtas.events.rnif;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a ProcessDef based on
 * UID.
 */
public class GetProcessDefEvent
  extends    EventSupport
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "PROCESS";
//  public static final String GUARDED_ACTION  = "GetProcessDef";
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 67404942926061552L;
	public static final String DEF_UID  = "ProcessDef UID";

  public GetProcessDefEvent(Long defUID)
    throws EventException
  {
    checkSetLong(DEF_UID, defUID);
  }

  public Long getDefUID()
  {
    return (Long)getEventData(DEF_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessDefEvent";
  }

  // ************* From IGuardedEvent *************************

//  public String getGuardedFeature()
//  {
//    return GUARDED_FEATURE;
//  }
//
//  public String getGuardedAction()
//  {
//    return GUARDED_ACTION;
//  }

}