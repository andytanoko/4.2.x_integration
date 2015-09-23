/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 * Oct 20 2005    Neo Sok Lay         Throws clause of ejbCreate method must not 
 *                                    define any application exceptions.
 * Dec 05, 2007   Tam Wei Xiang     To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception.                                                                   
 *
 */
package com.gridnode.pdip.app.workflow.jms.ejb;

import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFActivityManagerObj;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

public class GWFActivityMDBean implements MessageDrivenBean, MessageListener
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -1332791276151114752L;
  private MessageDrivenContext m_context;

  public void ejbRemove()
  {
  }

  /**
   * Sets the session context.
   *
   * @param ctx       MessageDrivenContext Context for session
   */
  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    m_context = ctx;
  }

  public void ejbCreate()
  {
  }

  // Implementation of MessageListener
  public void onMessage(Message msg)
  {
    Map mapMsg = null;
    String engineType = "";
    int eventId = -1;
    
    String jmsMessageID = "";  
    try
    {
      if(msg.getJMSRedelivered())
      {
        Logger.log("[GWFActivityMDBean.onMessage]", "Redelivered msg found. Message: "+msg);
      }
      
      jmsMessageID = msg.getJMSMessageID();
      ObjectMessage objMsg = (ObjectMessage) msg;
      mapMsg = (Map) objMsg.getObject();
      eventId = ((Integer) mapMsg.get(IGWFRouteDispatcher.EVENT_ID)).intValue();
      engineType = (String) mapMsg.get(IGWFRouteDispatcher.ENGINE_TYPE);
      IGWFActivityManagerObj activityRemote = WorkflowUtil.getActivityManager();
      Logger.debug("[GWFActivityMDBean.onMessage] eventId=" + eventId);
      
      switch (eventId)
      {
        case IGWFRouteDispatcher.CREATE_EVENT : //create rtActivity
          GWFRtActivity rtActivity = new GWFRtActivity();
          rtActivity.setProcessDefKey((String) mapMsg.get(IGWFRouteDispatcher.PROCESSDEF_KEY));
          rtActivity.setActivityUId((Long) mapMsg.get(IGWFRouteDispatcher.ACTIVITY_UID));
          rtActivity.setActivityType((String) mapMsg.get(IGWFRouteDispatcher.ACTIVITY_TYPE));
          rtActivity.setBranchName((String) mapMsg.get(IGWFRouteDispatcher.BRANCH_NAME));
          rtActivity.setRtProcessUId((Long) mapMsg.get(IGWFRouteDispatcher.RTPROCESS_UID));
          rtActivity.setEngineType((String) mapMsg.get(IGWFRouteDispatcher.ENGINE_TYPE));
          rtActivity.setContextUId((Long) mapMsg.get(IGWFRouteDispatcher.CONTEXT_UID));
          if (IWorkflowConstants.XPDL_ENGINE.equals(engineType))
          {
        	if(msg.getJMSRedelivered())
            {
              Logger.log("[GWFActivityMDBean.onMessage]", "Redelivered msg found for creating XPDL Event, ignored it. Message: "+msg);
              return;
            }  
        	  
            XpdlWorkflowEngine.getInstance().createRtActivity(rtActivity);
          }
          else
            activityRemote.createRtActivity(rtActivity);
          break;

        case IGWFRouteDispatcher.ROUTE_EVENT :
          activityRemote.selectActivityRoute((Long) mapMsg.get(IGWFRouteDispatcher.RTACTIVITY_UID));
          break;

        case IGWFRouteDispatcher.CHANGE_STATE_EVENT :
          activityRemote.changeActivityState((Long) mapMsg.get(IGWFRouteDispatcher.RTACTIVITY_UID), (Integer) mapMsg.get(IGWFRouteDispatcher.STATE));
          break;

        case IGWFRouteDispatcher.EXECUTE_EVENT :
          if (IWorkflowConstants.XPDL_ENGINE.equals(engineType))
          {
        	if(msg.getJMSRedelivered())
            {
              Logger.log("[GWFActivityMDBean.onMessage]", "Redelivered msg found for executing XPDL Event, ignored it. Message: "+msg);
              return;
            }
            XpdlWorkflowEngine.getInstance().executeActivity((Long) mapMsg.get(IGWFRouteDispatcher.RTACTIVITY_UID));
          }
          else
            throw new Exception("EXECUTE_EVENT is supported for XPDL_ENGINE only, eventId=" + eventId);
          break;

        default :
          throw new Exception("Invalid eventId, eventId=" + eventId);
        
        
      }
      
    }
    catch (Throwable th)
    {
      if (JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
      {
        Logger.warn("[GWFActivityMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
        m_context.setRollbackOnly();
      }
      Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                   "[GWFActivityMDBean.onMessage] Could not perform onMessage. map message: "+mapMsg+". Error: " + th.getMessage(), th);
    }
  }

}
