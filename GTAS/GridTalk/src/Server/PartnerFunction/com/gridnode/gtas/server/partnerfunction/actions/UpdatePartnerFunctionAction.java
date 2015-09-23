/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdatePartnerFunctionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 * Apr 29 2003    Neo Sok Lay         Add RaiseAlert workflow activity
 * Mar 01 2004    Mahesh              Added Suspend Activity parameters
 */
package com.gridnode.gtas.server.partnerfunction.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;

import com.gridnode.gtas.events.partnerfunction.UpdatePartnerFunctionEvent;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This Action class handles the update of a PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdatePartnerFunctionAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3704426289610528180L;

	private PartnerFunction _partnerFunction;

  public static final String ACTION_NAME = "UpdatePartnerFunctionAction";

  protected Class getExpectedEventClass()
  {
    return UpdatePartnerFunctionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPartnerFunctionToMap((PartnerFunction)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdatePartnerFunctionEvent updEvent = (UpdatePartnerFunctionEvent)event;
    _partnerFunction = ActionHelper.getManager().findPartnerFunction(updEvent.getPartnerFunctionUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    try
    {
      UpdatePartnerFunctionEvent updEvent = (UpdatePartnerFunctionEvent)event;

      _partnerFunction.setDescription(updEvent.getPartnerFunctionDesc());
      _partnerFunction.setTriggerOn(updEvent.getTriggerOn());

      List oldList = _partnerFunction.getWorkflowActivityUids();
      deleteOldActivities(oldList);
      _partnerFunction.clearWorkflowActivityUids();
      _partnerFunction.clearWorkflowActivities();

      List workflowActivities = updEvent.getWorkflowActivities();
      Logger.debug("workflowActivities size = "+workflowActivities.size());
      for(Iterator i = workflowActivities.iterator(); i.hasNext(); )
      {
        List workflowParams = (List)i.next();
        Logger.debug("workflowParams size = "+workflowParams.size());
        WorkflowActivity workflowActivity = createWorkflowActivity(workflowParams);
        Long workflowActivityUid = ActionHelper.getManager().createWorkflowActivity(workflowActivity);
        _partnerFunction.addWorkflowActivityUid(workflowActivityUid);
        _partnerFunction.addWorkflowActivity(workflowActivity);
      }

      return _partnerFunction;
    }
    catch (Exception ex)
    {
      Logger.warn("Error updating partner function", ex);
      return null;
    }
  }

  private void deleteOldActivities(List oldList)
  {
    try
    {
      for (Iterator i = oldList.iterator(); i.hasNext();)
      {
        Long uid = new Long(i.next().toString());
        ActionHelper.getManager().deleteWorkflowActivity(uid);
      }
    }
    catch (Exception ex)
    {
      Logger.warn("Error deleting old workflow activities", ex);
    }
  }

  private WorkflowActivity createWorkflowActivity(List workflowParams)
    throws Exception
  {
    WorkflowActivity workflowActivity = null;
    if (!workflowParams.isEmpty())
    {
      workflowActivity = new WorkflowActivity();
      Integer wfActivityType = (Integer)workflowParams.get(0);
      workflowActivity.setActivityType(wfActivityType);
      workflowActivity.setDescription(workflowParams.get(1).toString());
      if (wfActivityType.equals(WorkflowActivity.MAPPING_RULE))
      {
        workflowActivity.addParam(workflowParams.get(2));
        Logger.debug("MR workflowParams.get(2) = "+workflowParams.get(2));
      }
      else if (wfActivityType.equals(WorkflowActivity.USER_PROCEDURE))
      {
        workflowActivity.addParam(workflowParams.get(2));
        Logger.debug("UP workflowParams.get(2) = "+workflowParams.get(2));
      }
      else if (wfActivityType.equals(WorkflowActivity.EXIT_TO_EXPORT))
      {
        // do nothing
     }
      else if (wfActivityType.equals(WorkflowActivity.EXIT_TO_IMPORT))
      {
        // do nothing
      }
      else if (wfActivityType.equals(WorkflowActivity.EXIT_TO_OUTBOUND))
      {
        // do nothing
      }
      else if (wfActivityType.equals(WorkflowActivity.EXIT_WORKFLOW))
      {
        // do nothing
      }
      else if (wfActivityType.equals(WorkflowActivity.EXIT_TO_PORT))
      {
        workflowActivity.addParam(workflowParams.get(2));
        Logger.debug("Port workflowParams.get(2) = "+workflowParams.get(2));
      }
      else if (wfActivityType.equals(WorkflowActivity.SAVE_TO_FOLDER))
      {
        // do nothing
      }
      else if (wfActivityType.equals(WorkflowActivity.RAISE_ALERT))
      {
        workflowActivity.addParam(workflowParams.get(2));
        Logger.debug("Alert workflowParams.get(2) = "+workflowParams.get(2));
        if (workflowParams.size() > 3 && (workflowParams.get(3)!=null && !workflowParams.get(3).equals(WorkflowActivity.SUSPEND_ACTIVITY)))
        {
          workflowActivity.addParam(workflowParams.get(3));
          Logger.debug("Alert workflowParams.get(3) = "+workflowParams.get(3));
        }
        else
          workflowActivity.addParam("");
      }
      //    add SUSPEND_ACTIVITY params if exists
      int index = workflowParams.indexOf(WorkflowActivity.SUSPEND_ACTIVITY);
      if(index>-1) 
      {
        // if SUSPEND_ACTIVITY flag exists then both dispatchInterval, 
        // dispatchCount should exist
        Logger.debug("SUSPEND_ACTIVITY flag exists");
        workflowActivity.addParam(workflowParams.get(index)); // SUSPEND_ACTIVITY flag
        workflowActivity.addParam(workflowParams.get(index+1)); // dispatchInterval 
        workflowActivity.addParam(workflowParams.get(index+2)); // dispatchCount
      }
    }
    return workflowActivity;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updatePartnerFunction((PartnerFunction)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findPartnerFunction(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdatePartnerFunctionEvent updEvent = (UpdatePartnerFunctionEvent)event;
    return new Object[]
           {
             PartnerFunction.ENTITY_NAME,
             updEvent.getPartnerFunctionUid()
           };
  }

}