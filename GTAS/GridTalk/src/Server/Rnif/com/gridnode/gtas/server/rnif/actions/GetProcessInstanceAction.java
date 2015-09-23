package com.gridnode.gtas.server.rnif.actions;

import com.gridnode.gtas.events.rnif.GetProcessInstanceEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Map;

/**
 * This Action class handles the retrieving of one ProcessInstance.
 */

public class GetProcessInstanceAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7757013568091593420L;
	public static final String ACTION_NAME= "GetProcessInstanceAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    return constructEventResponse(
      IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
      getErrorMessageParams(event),
      ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetProcessInstanceEvent getInstEvent= (GetProcessInstanceEvent) event;
    Map retrieved=
      ProcessInstanceActionHelper.getProcessInstance(
        getInstEvent.getInstUID(),
        getInstEvent.getDefName());
    if (retrieved == null)
      return constructEventResponse(java.util.Collections.EMPTY_MAP);

    return constructEventResponse(retrieved);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetProcessInstanceEvent getEvent= (GetProcessInstanceEvent) event;
    return new Object[] { String.valueOf(getEvent.getInstUID()), getEvent.getDefName()};
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetProcessInstanceEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}