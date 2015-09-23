package com.gridnode.gtas.server.rnif.actions;

import com.gridnode.gtas.events.rnif.GetProcessDefListEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.server.rnif.helpers.ActionHelper;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the retrieving of a list of ProcessDefs.
 *
 */
public class GetProcessDefListAction extends AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5044768782667452823L;
	public static final String CURSOR_PREFIX = "ProcessDefListCursor.";
  public static final String ACTION_NAME = "GetProcessDefListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter) throws Exception
  {
    //exclude those marked deleted
    return ActionHelper.getProcessDefManager().findProcessDefs(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertDefsToMapObjects(entityList);
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  // ***************** AbstractGridTalkAction methods ***********************

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetProcessDefListEvent.class;
  }

}