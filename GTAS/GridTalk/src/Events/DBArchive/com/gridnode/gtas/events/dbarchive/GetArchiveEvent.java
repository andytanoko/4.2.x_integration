package com.gridnode.gtas.events.dbarchive;


import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;


public class GetArchiveEvent extends EventSupport
{ 
 	public static final String UID = "UID";

  public GetArchiveEvent(Long uID)
    throws EventException
  {
      checkSetLong(UID, uID);
  }

  public Long getUID()
  {
    return (Long) getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetArchiveEvent";
  }
}



