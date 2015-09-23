package com.gridnode.gtas.server.rnif.actions;

import java.util.Collection;

import com.gridnode.gtas.events.rnif.GetProcessInstanceListEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.model.ProcessInstance;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of ProcessInstances.
 *
 */
public class GetProcessInstanceListAction extends AbstractGetEntityListAction2
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8636688491609257371L;
	public static final String CURSOR_PREFIX = "ProcessInstanceListCursor.";
  public static final String ACTION_NAME = "GetProcessInstanceListAction";


  // ******************* AbstractGetEntityAction methods *******************

//  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
//  {
//    return constructEventResponse(
//      IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
//      getErrorMessageParams(event),
//      ex);
//  }
//
//  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
//  {
//    GetProcessInstanceListEvent getInstListEvent= (GetProcessInstanceListEvent) event;
//    Collection retrieved=
//      ProcessInstanceActionHelper.findProcessInstancesByName(getInstListEvent.getFilter());
//    if (retrieved == null)
//      return constructEventResponse(java.util.Collections.EMPTY_MAP);
//
//    return constructEventResponse(retrieved);
//  }
//
//  protected Object[] getErrorMessageParams(IEvent event)
//  {
//    GetProcessInstanceListEvent getEvent= (GetProcessInstanceListEvent) event;
//    return new Object[] {getEvent.getFilter()};
//  }

  protected Number getEntityKeyID()
  {
    return ProcessInstance.UID;    
  }
  
  protected Collection retrieveEntityKeys(IDataFilter filter)
    throws Exception
  {
    Collection entityKeys = 
	  ProcessInstanceActionHelper.findProcessInstancesKeys(filter);
	return entityKeys;  	  
  }

  protected Collection retrieveEntityList(IDataFilter filter) throws Exception
  {
     //return ProcessInstanceActionHelper.findProcessInstancesByName(filter);
     return ProcessInstanceActionHelper.findProcessInstances(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return entityList;
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
    return GetProcessInstanceListEvent.class;
  }

}