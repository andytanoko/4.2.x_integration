/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpdlActivityHelper.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 22, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.workflow.impl.xpdl.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gridnode.pdip.app.workflow.exceptions.DataDefinitionException;
import com.gridnode.pdip.app.workflow.exceptions.TransitionException;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;
import com.gridnode.pdip.app.workflow.runtime.ejb.IGWFRtProcessObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlConstants;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;

public class XpdlActivityHelper
{


  public static boolean isAsyncRequired(XpdlActivity xpdlActivity)
  {
    String extAttr = xpdlActivity.getExtendedAttributes();
    return  xpdlActivity.getActivityType()==XpdlActivity.AUTOMATIC_TYPE &&
            (
              extAttr==null || 
              extAttr.indexOf(IWorkflowConstants.WORKFLOW_DISPATCH_INTERVAL)==-1 ||
              extAttr.indexOf(IWorkflowConstants.WORKFLOW_DISPATCH_COUNT)==-1 
            );
  }


  
  // process the next activities from this activity
  public static void processNextActivity(GWFRtActivity rtActivity) throws WorkflowException, SystemException
  {
    try
    {
      
      XpdlActivity fromActivity = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getXpdlActivity(rtActivity.getActivityUId());
      Logger.debug("[XpdlActivityHelper.processNextActivity] fromRtActivityUId="+rtActivity.getKey()+", fromActivity=(" + fromActivity.getUId() + "," + fromActivity.getActivityId() + ")");
      

      Collection transRestColl = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getTransitionRestrictions(fromActivity.getTransitionRestrictionListUId());
      XpdlTransitionRestriction transRest = null;
      String split =null;
      if (transRestColl != null && !transRestColl.isEmpty())
      {
        transRest=(XpdlTransitionRestriction) transRestColl.iterator().next();
        split = transRest.getSplitType();
      }
      
      if (split==null) // normal transition
      {  
        Collection fromCol = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getTransitions(fromActivity.getActivityId(), null);
        if (fromCol.size() == 0)
          throw new DataDefinitionException(
            "Atleast one Transition should be form activity " + fromActivity.getActivityId());
        if (fromCol.size() > 1)
          throw new DataDefinitionException(
            "Cannot have more than one Transition form activity " + fromActivity.getActivityId());
        XpdlTransition trans = (XpdlTransition) fromCol.iterator().next();
        joinActivity(trans, rtActivity);
      }
      else  //split exists
      {
        Collection fromCol = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getTransFromTransRefListUId(transRest.getTransitionRefListUId());
        Logger.debug("[XpdlActivityHelper.processNextActivity] split="+split+", fromColSize=" + fromCol.size());
        List transList = new ArrayList();
        
        if (split.equals(XpdlConstants.CT_AND)) 
        { // no need to evaluate conditions for ANDSPLIT since it has to execute all transitions
          transList.addAll(fromCol);
        }
        else // XOR split
        {
          XpdlTransition otherwise = null;
          HashMap contextData = WorkflowUtil.getDataManager().getContextData(rtActivity.getContextUId());
          for(Iterator fromIt = fromCol.iterator();fromIt.hasNext(); )
          {
            XpdlTransition transition = (XpdlTransition) fromIt.next();
            if (transition.getConditionType() != null && transition.getConditionType().equals(XpdlConstants.TC_OTHERWISE))
            {
              otherwise = transition;
              continue;
            }
            else
            {
              // evaluate the condition expression
              boolean transitionOk = WorkflowUtil.executeExpression(transition.getConditionExpr(), contextData);
              Logger.debug("[XpdlActivityHelper.processNextActivity] ConditionChecking Expression=" + transition.getConditionExpr() + ", RESULT=" + transitionOk);
              if (transitionOk)
              {
                transList.add(transition);
                break;
              }
            }
          }
          if(transList.isEmpty() && otherwise!=null)
            transList.add(otherwise);
        }
        
        if (transList.size() > 0)
        {
          if((transList.size() - 1)>0) // add max concurrency
          {
            AbstractEntityHandler rtProcessHandler=getHandlerFor(GWFRtProcess.ENTITY_NAME);
            IGWFRtProcessObj rtProcessObj=(IGWFRtProcessObj)rtProcessHandler.findByPrimaryKey(rtActivity.getRtProcessUId());
            GWFRtProcess rtProcess  = (GWFRtProcess)rtProcessObj.getData();
            rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()+(transList.size() - 1)));
            rtProcessObj.setData(rtProcess);
          }

          //joinActivity
          for (Iterator iterator = transList.iterator(); iterator.hasNext();)
          {
            XpdlTransition trans = (XpdlTransition) iterator.next();
            joinActivity(trans, rtActivity);
          }
        }
        else
        {
          throw new TransitionException("No outgoing transition from " + fromActivity+", fromRtActivityUId="+rtActivity.getKey());
        }          
      }
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlActivityHelper.processNextActivity] Error", th);
      throw new SystemException("Error in processing next activity: "+th.getMessage(), th);
    }
  }

  public static void joinActivity(XpdlTransition transition, GWFRtActivity fromRtActivity) throws WorkflowException, SystemException
  {
    try
    {
      XpdlDefinitionHandler defHandler = XpdlDefinitionHandler.getInstance(fromRtActivity.getProcessDefKey());
      XpdlActivity toActivity = defHandler.getXpdlActivity(transition.getToActivityId());
      
      //check if it is end activity
      if (toActivity.getActivityId() != null && toActivity.getActivityId().equalsIgnoreCase(XpdlConstants.END_ACTIVITYID))
      {
        processEnd(fromRtActivity);
      }
      else
      {
        String join = null; // default join is AND
        Collection toTransRestColl =defHandler.getTransitionRestrictions(toActivity.getTransitionRestrictionListUId());
        if (toTransRestColl != null && !toTransRestColl.isEmpty())
        {
          XpdlTransitionRestriction toTransRest = (XpdlTransitionRestriction) toTransRestColl.iterator().next();
          join = toTransRest.getJoinType();
        }
        if (join==null || join.equals(XpdlConstants.CT_XOR)) // normal join or XOR join
        {
          XpdlRouteDispatcher.createRtActivity((Long)toActivity.getKey(),fromRtActivity.getRtProcessUId(),fromRtActivity.getContextUId(),fromRtActivity.getProcessDefKey(),isAsyncRequired(toActivity));
        }
        else // AND Join
        {
          Collection transList = defHandler.getTransitions(null,toActivity.getActivityId());
          if(transList!=null && transList.size()==1) 
          { // no need to check for incomming complete activity since only one transition exists
            XpdlRouteDispatcher.createRtActivity((Long)toActivity.getKey(),fromRtActivity.getRtProcessUId(),fromRtActivity.getContextUId(),fromRtActivity.getProcessDefKey(),isAsyncRequired(toActivity));
          }
          else if(transList!=null && transList.size()>1)
          {
            IDataFilter subFilter = null;
            for(Iterator i = transList.iterator();i.hasNext();)
            {
              XpdlTransition tmpToTransition =(XpdlTransition)i.next();
              XpdlActivity tmpFromActivity = defHandler.getXpdlActivity(tmpToTransition.getFromActivityId());
              if(subFilter==null)
              {
                subFilter = new DataFilterImpl();
                subFilter.addSingleFilter(null,GWFRtActivity.ACTIVITY_UID,subFilter.getEqualOperator(),tmpFromActivity.getKey(),false);
              }
              else subFilter.addSingleFilter(subFilter.getOrConnector(),GWFRtActivity.ACTIVITY_UID,subFilter.getEqualOperator(),tmpFromActivity.getKey(),false);
            }
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null,GWFRtActivity.RT_PROCESS_UID,filter.getEqualOperator(),fromRtActivity.getKey(),false);
            filter.addFilter(filter.getAndConnector(),subFilter);
            filter.addSingleFilter(filter.getAndConnector(),GWFRtActivity.STATE,filter.getEqualOperator(),new Integer(GWFRtActivity.CLOSED_COMPLETED),false);
            int finishedCount = getHandlerFor(GWFRtActivity.ENTITY_NAME).getEntityCount(filter);
            if(finishedCount==transList.size())
            {
              AbstractEntityHandler rtProcessHandler=getHandlerFor(GWFRtProcess.ENTITY_NAME);
              IGWFRtProcessObj rtProcessObj=(IGWFRtProcessObj)rtProcessHandler.findByPrimaryKey(fromRtActivity.getRtProcessUId());
              GWFRtProcess rtProcess  = (GWFRtProcess)rtProcessObj.getData();
              rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()-(finishedCount - 1)));
              rtProcessObj.setData(rtProcess);
              // all incomming had reached so start the toActivity
              XpdlRouteDispatcher.createRtActivity((Long)toActivity.getKey(),fromRtActivity.getRtProcessUId(),fromRtActivity.getContextUId(),fromRtActivity.getProcessDefKey(),isAsyncRequired(toActivity));
            }
          }
        }
      }
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlRouteHelper.joinActivity] Exception", th);
      throw new SystemException("Exception in joining activities: "+th.getMessage(), th);
    }
  }

  public static void processEnd(GWFRtActivity fromRtActivity) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlActivityHelper.processEnd] Enter , fromRtActivityUId="+fromRtActivity.getKey());
    try
    {
      AbstractEntityHandler rtProcessHandler=getHandlerFor(GWFRtProcess.ENTITY_NAME);
      IGWFRtProcessObj rtProcessObj=(IGWFRtProcessObj)rtProcessHandler.findByPrimaryKey(fromRtActivity.getRtProcessUId());
      GWFRtProcess rtProcess  = (GWFRtProcess)rtProcessObj.getData();
      if(rtProcess.getMaxConcurrency().intValue()<=1)
      {
        XpdlWorkflowEngine.getInstance().completeProcess((Long)rtProcess.getKey());
      }
      else
      { //decrement the MaxConcurrency since end activity is reached
        rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()- 1));
        rtProcessObj.setData(rtProcess);
        Logger.debug("[XpdlActivityHelper.processEnd] rtProcessUId="+rtProcess.getKey()+", MaxConcurrency"+rtProcess.getMaxConcurrency());
      }
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlActivityHelper.processEnd] Exception", th);
      throw new SystemException("Exception in process end: "+th.getMessage(), th);
    }
  }
  
  public static void removeActivityAlarm(Long rtActivityUId) throws Throwable
  {
    WorkflowUtil.cancelAlarm(
              KeyConverter.getKey((Long) rtActivityUId, GWFRtActivity.ENTITY_NAME, IWorkflowConstants.XPDL_ENGINE),
              XpdlActivity.ENTITY_NAME,
              IWorkflowConstants.XPDL_ENGINE);
  }
  
  private static AbstractEntityHandler getHandlerFor(String entityName)
  {
    return EntityHandlerFactory.getHandlerFor(entityName, true);
  }  
}
