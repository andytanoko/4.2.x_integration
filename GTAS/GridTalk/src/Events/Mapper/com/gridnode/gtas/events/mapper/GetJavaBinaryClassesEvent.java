package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event class contains the name of the jar file to retrieve the classes
 * from.
 *
 * @author Wong Ming Qian
 *
 * @since 4.2.2
 */

public class GetJavaBinaryClassesEvent extends EventSupport
{ 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 6463124671732449535L;
  public static final String MAPPING_FILE_UID = "Mapping File Uid";

  public GetJavaBinaryClassesEvent(Long procedureDefUid) throws EventException
  {
    checkSetLong(MAPPING_FILE_UID,procedureDefUid);
  }

  public Long getJavaBinaryClassesUid()
  {
    return (Long) getEventData(MAPPING_FILE_UID);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetJavaBinaryClassesEvent";
  }
}