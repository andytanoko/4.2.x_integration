
package com.gridnode.gtas.events.rnif;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for Canceling one or more ProcessInstance
 * based on UID.
 *
 * @version GT 2.1.19
 * @since 2.0 I7
 */
public class CancelProcessInstanceEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3971961609232136583L;
	public static final String GUARDED_FEATURE = "PROCESS";
  public static final String GUARDED_ACTION  = "CancelProcessInstance";

  public static final String INST_UID  = "ProcessInstance UID";
  public static final String INST_UIDS = "ProcessInstance UIDS";
  
  public static final String CANCEL_REASON = "Cancel Reason";



  public CancelProcessInstanceEvent(Long instUID, String reason)
    throws EventException
  {
    checkSetLong(INST_UID, instUID);
    checkSetString(CANCEL_REASON, reason);
  }

  public CancelProcessInstanceEvent(Collection instUIDs, String reason)
    throws EventException
  {
    checkSetCollection(INST_UIDS, instUIDs, Long.class);
    checkSetString(CANCEL_REASON, reason);
  }

  public Long getInstUID()
  {
    return (Long)getEventData(INST_UID);
  }

  public Collection getInstUIDs()
  {
    return (Collection)getEventData(INST_UIDS);
  }
  
  public String getCancelReason()
  {
    return (String)getEventData(CANCEL_REASON);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CancelProcessInstanceEvent";
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