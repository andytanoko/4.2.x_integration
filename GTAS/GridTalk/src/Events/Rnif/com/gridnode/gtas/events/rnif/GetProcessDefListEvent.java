package com.gridnode.gtas.events.rnif;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of ProcessDef(s),
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class GetProcessDefListEvent
  extends    GetEntityListEvent
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "PROCESS";
//  public static final String GUARDED_ACTION  = "GetProcessDefList";
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1785756132685540515L;

	public GetProcessDefListEvent()
  {
    super();
  }

  public GetProcessDefListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetProcessDefListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetProcessDefListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetProcessDefListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessDefListEvent";
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