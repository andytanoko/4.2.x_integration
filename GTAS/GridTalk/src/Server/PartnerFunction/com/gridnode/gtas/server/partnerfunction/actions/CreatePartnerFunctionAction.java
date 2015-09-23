/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreatePartnerFunctionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Koh Han Sing        Created
 * Apr 29 2003    Neo Sok Lay         Add RaiseAlert workflow activity
 * Mar 01 2004    Mahesh              Added Suspend Activity parameters 
 */
package com.gridnode.gtas.server.partnerfunction.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.gtas.events.partnerfunction.CreatePartnerFunctionEvent;
import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreatePartnerFunctionAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -776403075571892703L;
	public static final String ACTION_NAME = "CreatePartnerFunctionAction";

  protected Class getExpectedEventClass()
  {
    return CreatePartnerFunctionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findPartnerFunction(key);
  }

  /**
   * When creating a partner function, only the workflow activity uids will
   * be stored to the database. So we only need to set the workflow activity
   * uids fields.
   */
  protected AbstractEntity prepareCreationData(IEvent event)
  {
    try
    {
      CreatePartnerFunctionEvent createEvent = (CreatePartnerFunctionEvent)event;

      PartnerFunction newPartnerFunction = new PartnerFunction();
      newPartnerFunction.setPartnerFunctionId(createEvent.getPartnerFunctionId());
      newPartnerFunction.setDescription(createEvent.getPartnerFunctionDesc());
      newPartnerFunction.setTriggerOn(createEvent.getTriggerOn());

      List workflowActivities = createEvent.getWorkflowActivities();
      Logger.debug("workflowActivities size = "+workflowActivities.size());
      for(Iterator i = workflowActivities.iterator(); i.hasNext(); )
      {
        List workflowParams = (List)i.next();
        Logger.debug("workflowParams size = "+workflowParams.size());
        WorkflowActivity workflowActivity = createWorkflowActivity(workflowParams);
        Long workflowActivityUid = ActionHelper.getManager().createWorkflowActivity(workflowActivity);
        newPartnerFunction.addWorkflowActivityUid(workflowActivityUid);
        newPartnerFunction.addWorkflowActivity(workflowActivity);
      }

      return newPartnerFunction;
    }
    catch (Exception ex)
    {
      Logger.warn("Unable to create partner function", ex);
      return null;
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
      //add SUSPEND_ACTIVITY params if exists
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

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreatePartnerFunctionEvent createEvent = (CreatePartnerFunctionEvent)event;
    return new Object[]
           {
             PartnerFunction.ENTITY_NAME,
             createEvent.getPartnerFunctionId()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createPartnerFunction((PartnerFunction)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPartnerFunctionToMap((PartnerFunction)entity);
  }

}