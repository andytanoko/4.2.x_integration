package com.gridnode.gtas.events.rnif;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Map;

/**
 * This Event class contains the data for update a ProcessDef.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class UpdateProcessDefEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3342674510877656720L;
	public static final String GUARDED_FEATURE = "PROCESS";
  public static final String GUARDED_ACTION  = "UpdateProcessDef";

  public static final String DEF_UID     = "ProcessDef UID";
  public static final String REQUEST_ACT = "RequestAct";
  public static final String RESPONSE_ACT = "ResponseAct";
  public static final String OTHER_FIELDS = "OtherFields";

  public UpdateProcessDefEvent(Long uID, Map otherFields, Map requestAct, Map responseAct) throws EventException
  {
    checkSetLong(DEF_UID, uID);
    setEventData(REQUEST_ACT, requestAct);
    if (responseAct != null)
      setEventData(RESPONSE_ACT, responseAct);
    
    setEventData(OTHER_FIELDS, otherFields);
  }

  public Long getDefUID()
  {
    return (Long)getEventData(DEF_UID);
  }

  public Map getRequestAct()
  {
    return (Map) getEventData(REQUEST_ACT);
  }
  public Map getResponseAct()
  {
    return (Map) getEventData(RESPONSE_ACT);
  }
  
  public Map getOtherFields()
  {
    return (Map) getEventData(OTHER_FIELDS);
  }


  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateProcessDefEvent";
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