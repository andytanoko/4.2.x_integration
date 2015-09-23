package com.gridnode.gtas.server.rnif.actions;

import com.gridnode.gtas.events.rnif.GetProcessDefEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.rnif.helpers.ActionHelper;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one ProcessDef.
 */

public class GetProcessDefAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7103774962784856546L;
	public static final String ACTION_NAME = "GetProcessDefAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessDefToMap((ProcessDef)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetProcessDefEvent getEvent = (GetProcessDefEvent)event;

    return ActionHelper.getProcessDefManager().findProcessDef(getEvent.getDefUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetProcessDefEvent getEvent = (GetProcessDefEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getDefUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetProcessDefEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}