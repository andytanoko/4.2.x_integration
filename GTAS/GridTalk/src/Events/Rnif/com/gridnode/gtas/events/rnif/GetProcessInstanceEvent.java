
package com.gridnode.gtas.events.rnif;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a ProcessInstance based on
 * UID.
 */
public class GetProcessInstanceEvent
  extends    EventSupport
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "PROCESS";
//  public static final String GUARDED_ACTION  = "GetProcessInstance";
  
   
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1453685520308785400L;

	public static final String DEF_NAME  = "ProcessDef Name";

  public static final String INST_UID  = "ProcessInstance UID";

  public GetProcessInstanceEvent(Long instUID, String defName)
    throws EventException
  {
    checkSetLong(INST_UID, instUID);
    setEventData(DEF_NAME, defName);
  }

  public Long getInstUID()
  {
    return (Long)getEventData(INST_UID);
  }
  
  public String getDefName()
  {
    return (String)getEventData(DEF_NAME);
  }
  

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessInstanceEvent";
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