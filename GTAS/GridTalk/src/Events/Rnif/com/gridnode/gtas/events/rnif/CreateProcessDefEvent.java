package com.gridnode.gtas.events.rnif;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Map;

public class CreateProcessDefEvent extends EventSupport implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 57402021780971258L;
	public static final String GUARDED_FEATURE = "PROCESS";
  public static final String GUARDED_ACTION = "CreateProcessDef";

  public static final String DEF_NAME = "ProcessDef Name";
  public static final String REQUEST_ACT = "RequestAct";
  public static final String RESPONSE_ACT = "ResponseAct";
  public static final String DEF_FIELDS = "ProcessDefFields";

  public CreateProcessDefEvent(Map defFields, Map requestAct, Map responseAct) throws EventException
  {
//    checkSetString(DEF_NAME, defFields.get("_processDef"));
    setEventData(REQUEST_ACT, requestAct);
    if (responseAct != null)
      setEventData(RESPONSE_ACT, responseAct);
    
    setEventData(DEF_FIELDS, defFields);
    
    //may need to check whitepage fields in future
  }


  public Map getRequestAct()
  {
    return (Map) getEventData(REQUEST_ACT);
  }
  public Map getResponseAct()
  {
    return (Map) getEventData(RESPONSE_ACT);
  }
  
  public Map getDefFields()
  {
    return (Map) getEventData(DEF_FIELDS);
  }


  public String getEventName()
  {
    return "java:comp/env/param/event/CreateProcessDefEvent";
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