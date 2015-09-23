package com.gridnode.gtas.events.rnif;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of ProcessInstance,
 * optionally based on a filtering condition.
 *
 * @author Liu Xiaohua
 *
 * @version GT 2.1.19
 * @since 2.0 I7
 */
public class GetProcessInstanceListEvent
  extends    GetEntityListEvent
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "PROCESS";
//  public static final String GUARDED_ACTION  = "GetProcessInstanceList";
 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 478650139158358204L;
	public static final String DEF_NAME  = "ProcessDef Name";

  public GetProcessInstanceListEvent()
  {
    super();
  }

//  public GetProcessInstanceListEvent(String defName)
//  throws EventException
//  {
//    super();
//    checkSetString(DEF_NAME, defName);
//  }
//  
  public GetProcessInstanceListEvent(IDataFilter filter)
  throws EventException
  {
    super(filter);
  }
  
  
//  public String getDefName()
//  {
//    return (String)getEventData(DEF_NAME);
//  }
  

  public GetProcessInstanceListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetProcessInstanceListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetProcessInstanceListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessInstanceListEvent";
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